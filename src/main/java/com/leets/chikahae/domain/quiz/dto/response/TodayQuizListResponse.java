package com.leets.chikahae.domain.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record
TodayQuizListResponse(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate quizDate,

        List<QuizResponse> quizList
) {
}
