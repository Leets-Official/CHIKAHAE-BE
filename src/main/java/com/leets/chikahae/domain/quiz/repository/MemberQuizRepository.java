package com.leets.chikahae.domain.quiz.repository;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.quiz.entity.MemberQuiz;
import com.leets.chikahae.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {

    boolean existsByQuizAndMember(Quiz quiz, Member member);

    void deleteByMember_MemberId(long memberId);

    @Query("SELECT COUNT(mq) FROM MemberQuiz mq WHERE mq.member.memberId = :memberId AND DATE(mq.createdAt) = :today")
    int countTodaySolved(@Param("memberId") Long memberId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(mq) FROM MemberQuiz mq WHERE mq.member.memberId = :memberId AND mq.isCorrect = true AND DATE(mq.createdAt) = :today")
    int countTodayCorrect(@Param("memberId") Long memberId, @Param("today") LocalDate today);

    List<MemberQuiz> findByMember_MemberId(long memberId);

    boolean existsByMemberIdAndDate(long memberId, LocalDate now);
}
