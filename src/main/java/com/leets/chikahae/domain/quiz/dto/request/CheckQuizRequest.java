package com.leets.chikahae.domain.quiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CheckQuizRequest(
        @NotNull(message = "퀴즈 ID는 필수입니다.")
        Long quizId,

        @NotBlank(message = "정답 입력은 필수입니다.")
        String selectedAnswer

) {
}
