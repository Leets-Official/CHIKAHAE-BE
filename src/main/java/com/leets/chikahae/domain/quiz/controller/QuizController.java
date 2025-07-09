package com.leets.chikahae.domain.quiz.controller;


import com.leets.chikahae.domain.quiz.dto.request.CheckQuizRequest;
import com.leets.chikahae.domain.quiz.dto.response.CheckQuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.TodayQuizListResponse;
import com.leets.chikahae.domain.quiz.service.QuizService;
import com.leets.chikahae.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/check")
    public ApiResponse<CheckQuizResponse> checkQuizAnswer(@RequestBody CheckQuizRequest request) {
        //임시 memberId
        Long memberId = 101L;
        // 퀴즈 정답 확인 및 기록 저장 로직
        CheckQuizResponse checkQuizResponse=quizService.checkQuizAnswer(request.quizId(),request.selectedAnswer(),memberId);
        return ApiResponse.ok(checkQuizResponse);
    }

    // 퀴즈 결과 반환 및 보상 처리
//    @GetMapping("/result")
//    public ApiResponse<CheckQuizResponse> getQuizResult(@RequestParam Long quizId) {
//        // 퀴즈 결과 반환 및 보상 처리 로직
//        CheckQuizResponse checkQuizResponse = quizService.getQuizResult(quizId);
//        return ApiResponse.ok(checkQuizResponse);
//    }

}
