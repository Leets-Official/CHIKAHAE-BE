package com.leets.chikahae.domain.quiz.repository;

import com.leets.chikahae.domain.quiz.entity.MemberQuiz;
import com.leets.chikahae.domain.quiz.entity.Quiz;
import com.leets.chikahae.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {

    boolean existsByQuizAndMember(Quiz quiz, Member member);
}
