package com.leets.chikahae.domain.quiz.dto.response;

import lombok.Builder;

@Builder
public record CheckQuizResponse(
        Long quizId,
        boolean isCorrect,
        String answer,
        String answerDescription

) {

    public static CheckQuizResponse from(Long quizId, boolean isCorrect, String answer, String answerDescription) {
        return CheckQuizResponse.builder()
                .quizId(quizId)
                .isCorrect(isCorrect)
                .answer(answer)
                .answerDescription(answerDescription)
                .build();
    }
}
