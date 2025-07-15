package com.leets.chikahae.domain.quiz.dto.response;

import com.leets.chikahae.domain.quiz.entity.Quiz;
import com.leets.chikahae.domain.quiz.entity.QuizType;
import lombok.Builder;

import java.util.List;

@Builder
public record QuizResponse(

    Long quizId,
    QuizType type,
    String question,
    List<String> options

) {
    public static QuizResponse from(Quiz quiz) {
        return QuizResponse.builder()
                .quizId(quiz.getQuizId())
                .type(quiz.getType())
                .question(quiz.getQuestion())
                .options(quiz.getOptions())
                .build();
    }
}
