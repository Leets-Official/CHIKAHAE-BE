package com.leets.chikahae.domain.quiz.repository;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.quiz.entity.MemberQuiz;
import com.leets.chikahae.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {

    boolean existsByQuizAndMember(Quiz quiz, Member member);

    void deleteByMember_MemberId(long memberId);

    int countByMember_MemberId(long memberId);

    int countByMember_MemberIdAndIsCorrectTrue(long memberId);

    List<MemberQuiz> findByMember_MemberId(long memberId);
}
