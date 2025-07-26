package com.leets.chikahae.domain.member.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ParentRepository parentRepository;

    /**
     * 사용자 등록
     * - 14세 미만일 경우에만 보호자(parentId 저장)
     * -14세 이상이라면 parentId 없이 등록
     */
    @Transactional
    public Member registerMember(@Nullable Long parentId, String kakaoId, String nickname,
                                LocalDate birth, Boolean gender, String profileImage) {


        Member member = Member.builder()
                .parentId(parentId)
                .kakaoId(kakaoId)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .profileImage(profileImage)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return memberRepository.saveAndFlush(member);
    }

    /**
     * 카카오 ID로 사용자 조회
     */
    public Optional<Member> findByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    /**
     * 만나이 계산
     */
    private boolean isUnder14(LocalDate birth) {
        return Period.between(birth, LocalDate.now()).getYears() < 14;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        // 1. 자녀 정보 조회 (부모 정보 얻기 위해)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 member가 존재하지 않습니다."));

        Long parentId = member.getParentId(); // null일 수 있음 (14세 이상 회원)

        // 2. 자녀 삭제
        memberRepository.deleteById(memberId);

        // 3. 부모 삭제 조건: 해당 부모에게 자녀가 더 이상 없을 경우
        if (parentId != null) {
            boolean hasOtherChildren = memberRepository.existsByParentId(parentId);
            if (!hasOtherChildren) {
                parentRepository.deleteById(parentId);
            }
        }
    }







}//class
