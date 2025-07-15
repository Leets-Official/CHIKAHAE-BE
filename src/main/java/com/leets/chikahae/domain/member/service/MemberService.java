package com.leets.chikahae.domain.member.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    public Member registerChild(Long parentId, String name, String nickname,
                                LocalDate birth, Boolean gender, String profileImage) {

        Member member = Member.builder()
                .parentId(parentId)
                .name(name)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .profileImage(profileImage)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return memberRepository.save(member);
    }

    public Optional<Member> findFirstChildByParentId(Long parentId) {
        return memberRepository.findFirstByParentId(parentId);
    }

}//class
