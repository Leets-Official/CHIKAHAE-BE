package com.leets.chikahae.domain.member.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

        return memberRepository.saveAndFlush(member);
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
