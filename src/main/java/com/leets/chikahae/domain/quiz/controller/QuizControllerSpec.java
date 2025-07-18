package com.leets.chikahae.domain.quiz.controller;

import com.leets.chikahae.domain.quiz.dto.request.CheckQuizRequest;
import com.leets.chikahae.domain.quiz.dto.response.CheckQuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResultResponse;
import com.leets.chikahae.domain.quiz.dto.response.TodayQuizListResponse;
import com.leets.chikahae.global.response.ApiResponse;
import com.leets.chikahae.security.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "Quiz", description = "퀴즈를 조회하고, 정답을 확인하고, 결과를 반환하는 API")
public interface QuizControllerSpec {

    @Operation(
            summary = "오늘 퀴즈 목록 조회",
            description = "오늘자 퀴즈 3문제를 조회합니다. 순서는 OX 문제 2개, 객관식 1문제입니다."
    )
    ApiResponse<TodayQuizListResponse> getTodayQuizList(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails user
    );

    @Operation(
            summary = "퀴즈 정답 제출",
            description = "퀴즈 ID와 사용자의 선택한 답변을 전송하고, 정답 여부를 확인합니다."
    )
    ApiResponse<CheckQuizResponse> checkQuizAnswer(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody(
                    description = "퀴즈 정답 요청 바디",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "퀴즈 정답 예시",
                                    value = """
                        {
                          "quizId": 17,
                          "selectedAnswer": "X"
                        }
                        """
                            )
                    )
            )
            CheckQuizRequest request
    );

    @Operation(
            summary = "퀴즈 결과 및 보상 조회",
            description = "퀴즈 3문제를 모두 푼 사용자의 결과(정답 개수, 코인 보상, 퀴즈 응답 내역)를 반환합니다."
    )
    ApiResponse<QuizResultResponse> getQuizResult(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails user
    );
}
