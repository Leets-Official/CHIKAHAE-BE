package com.leets.chikahae.domain.quiz.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record QuizResultResponse(

        Integer correctCount,
        Integer coinReward,
        List<CheckQuizResponse> checkQuizResponse

) {
    public static QuizResultResponse from(int correctCount, int coinReward, List<CheckQuizResponse> responses) {
        return QuizResultResponse.builder()
                .correctCount(correctCount)
                .coinReward(coinReward)
                .checkQuizResponse(responses)
                .build();
    }
}
