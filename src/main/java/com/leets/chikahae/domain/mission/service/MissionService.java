package com.leets.chikahae.domain.mission.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.mission.dto.MissionResponse;
import com.leets.chikahae.domain.mission.entity.MemberMission;
import com.leets.chikahae.domain.mission.entity.Mission;
import com.leets.chikahae.domain.mission.repository.MemberMissionRepository;
import com.leets.chikahae.domain.mission.repository.MissionRepository;
import com.leets.chikahae.domain.point.service.PointService;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final MemberRepository memberRepository;
    private final PointService pointService;


    // 미션 목록 조회
    @Transactional(readOnly = true)
    public List<MissionResponse> getAllMissions(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Mission> missions = missionRepository.findAll();

        return missions.stream()
                .map(mission -> {
                    // member + mission으로 MemberMission 조회
                    Optional<MemberMission> mmOpt = memberMissionRepository.findByMemberAndMission(member, mission);

                    boolean isCompleted = mmOpt.isPresent() &&
                            (mmOpt.get().getStatus() == MemberMission.Status.COMPLETED ||
                                    mmOpt.get().getStatus() == MemberMission.Status.REWARDED);

                    return MissionResponse.from(mission, isCompleted);
                })
                .toList();
    }

    // 미션 완료시 처리 로직
    @Transactional
    public void completeMission(Member member, Mission.MissionCode missionCode) {
        Mission mission = missionRepository.findByCode(missionCode)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSION_NOT_FOUND));

        // 미션 기록이 있으면 조회, 없으면 새로 생성해서 저장
        MemberMission memberMission = memberMissionRepository
                .findByMemberAndMission(member, mission)
                .orElseGet(() -> MemberMission.builder()
                        .member(member)
                        .mission(mission)
                        .status(MemberMission.Status.IN_PROGRESS)
                        .build());

        // 미션이 이미 완료되었거나 보상 처리된 경우 예외 발생
        if (memberMission.getStatus() == MemberMission.Status.REWARDED) {
            throw new CustomException(ErrorCode.ALREADY_REWARDED);
        }

        // 미션 포인트 지급
        pointService.earnPoint(member.getId(), mission.getRewardPoint(), "미션 보상: " + mission.getName());

        // 완료 처리 (날짜, 상태 변경)
        memberMission.markRewarded();
        memberMissionRepository.save(memberMission);
    }


    @Transactional
    public void completeRewardedMission(Member member, Mission.MissionCode missionCode) {

        missionRepository.findByCode(missionCode)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSION_NOT_FOUND));

        completeMission(member, missionCode);
    }



}
