package com.leets.chikahae.domain.member.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.domain.point.entity.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NotificationSlotService notificationSlotService;
    /**
     * 자녀 등록
     */
    @Transactional
    public Member registerChild(Long parentId, String nickname,
                                LocalDate birth, Boolean gender, String profileImage) {

        Member member = Member.builder()
                .parentId(parentId)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .profileImage(profileImage)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Member savedMember=memberRepository.saveAndFlush(member);

        //  Point 엔티티 생성 및 저장
        Point point = Point.of(member);
        member.setPoint(point); // 연관 관계 편의 메서드

        notificationSlotService.createDefaultSlots(member, ZoneId.of("Asia/Seoul"));
        return savedMember;
    }

    /**
     * 부모 ID로 첫 번째 자녀 조회
     */
    public Optional<Member> findFirstChildByParentId(Long parentId) {
        return memberRepository.findFirstByParentId(parentId);
    }

    /**
     * 카카오 ID로 회원 조회
     */
    public Optional<Member> findByKakaoId(String kakaoId) {
        return memberRepository.findByParentKakaoId(kakaoId);
    }

}
