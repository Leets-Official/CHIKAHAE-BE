package com.leets.chikahae.domain.quiz.repository;

import com.leets.chikahae.domain.quiz.entity.DailyQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyQuizRepository extends JpaRepository<DailyQuiz, Long> {

    @Query("SELECT dq FROM DailyQuiz dq LEFT JOIN FETCH dq.quizzes WHERE dq.quizDate = :quizDate")
    Optional<DailyQuiz> findByQuizDate(@Param("quizDate") LocalDate quizDate);
}
