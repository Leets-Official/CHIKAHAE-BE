package com.leets.chikahae.domain.quiz.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CheckQuizRequest(
        @NotNull(message = "퀴즈 ID는 필수입니다.")
        @Schema(description = "퀴즈 ID", example = "1")
        Long quizId,

        @NotBlank(message = "정답 입력은 필수입니다.")
        @Schema(description = "사용자가 선택한 답", example = "X")
        String selectedAnswer

) {
}
