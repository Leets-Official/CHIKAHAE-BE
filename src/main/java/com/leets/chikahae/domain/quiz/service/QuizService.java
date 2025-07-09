package com.leets.chikahae.domain.quiz.service;

import com.leets.chikahae.domain.quiz.dto.response.CheckQuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import java.util.List;


public interface QuizService {

    List<QuizResponse> getQuizzes();

    CheckQuizResponse checkQuizAnswer(Long quizId, String selectedAnswer, Long memberId);

//    CheckQuizResponse getQuizResult(Long quizId);
}
