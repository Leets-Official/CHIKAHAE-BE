package com.leets.chikahae.domain.member.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
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


}//class
