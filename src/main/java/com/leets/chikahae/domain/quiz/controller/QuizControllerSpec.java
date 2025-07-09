package com.leets.chikahae.domain.quiz.controller;

import com.leets.chikahae.domain.quiz.dto.response.TodayQuizListResponse;
import com.leets.chikahae.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "퀴즈 API", description = "퀴즈를 조회하고 퀴즈 정답을 획인하는 api입니다.")
public interface QuizControllerSpec {

    @Operation(
            summary = "퀴즈 조회 API",
            description = "오늘에 해당하는 퀴즈 목록 3개를 조회합니다.순서는 ox 2문제 객관식 1문제 순입니다."
    )
    ApiResponse<TodayQuizListResponse> getTodayQuizList();


}
