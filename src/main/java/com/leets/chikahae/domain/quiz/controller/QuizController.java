package com.leets.chikahae.domain.quiz.controller;


import com.leets.chikahae.domain.quiz.dto.request.CheckQuizRequest;
import com.leets.chikahae.domain.quiz.dto.response.CheckQuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResultResponse;
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
public class QuizController {

    private final QuizService quizService;

    // 퀴즈 문제 조회
    @GetMapping("/today") // @AuthenticationPrincipal PrincipalDetails user
    public ApiResponse<TodayQuizListResponse> getTodayQuizList(@RequestParam long memberId) {
        // 이탈 시 사용자 이력 초기화
        quizService.resetQuizHistory(memberId);

        //  퀴즈 세트 제공
        List<QuizResponse> quizListResponse = quizService.getQuizzes();
        TodayQuizListResponse todayQuizListResponse=TodayQuizListResponse.builder().quizDate(LocalDate.now()).quizList(quizListResponse).build();
        return ApiResponse.ok(todayQuizListResponse);
    }

    // 퀴즈 정답 확인 및 기록 저장
    @PostMapping("/check")
    public ApiResponse<CheckQuizResponse> checkQuizAnswer(@RequestParam long memberId,@RequestBody CheckQuizRequest request) {

        // 퀴즈 정답 확인 및 기록 저장 로직
        CheckQuizResponse checkQuizResponse=quizService.checkQuizAnswer(request.quizId(),request.selectedAnswer(),memberId);
        return ApiResponse.ok(checkQuizResponse);
    }

    // 퀴즈 결과 반환 및 보상 처리
    @GetMapping("/result")
    public ApiResponse<QuizResultResponse> getQuizResult(@RequestParam long memberId) {
        // 퀴즈 결과 반환 및 보상 처리 로직
        QuizResultResponse quizResult = quizService.getQuizResult(memberId);
        return ApiResponse.ok(quizResult);
    }

}
