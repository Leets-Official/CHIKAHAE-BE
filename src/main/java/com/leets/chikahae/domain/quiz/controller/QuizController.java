package com.leets.chikahae.domain.quiz.controller;


import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.TodayQuizListResponse;
import com.leets.chikahae.domain.quiz.service.QuizService;
import com.leets.chikahae.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
public class QuizController implements QuizControllerSpec {

    private final QuizService quizService;

    // 퀴즈 문제 조회
    @GetMapping("/today")
    public ApiResponse<TodayQuizListResponse> getTodayQuizList() {
        List<QuizResponse> quizListResponse = quizService.getQuizzes();
        TodayQuizListResponse todayQuizListResponse=TodayQuizListResponse.builder().quizDate(LocalDate.now()).quizList(quizListResponse).build();
        return ApiResponse.ok(todayQuizListResponse);
    }

    // 퀴즈 정답 확인 및 기록 저장

    // 퀴즈 결과 반환 및 보상 처리
}
