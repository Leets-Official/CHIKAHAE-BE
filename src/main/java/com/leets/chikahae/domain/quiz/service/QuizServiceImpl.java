package com.leets.chikahae.domain.quiz.service;

import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import com.leets.chikahae.domain.quiz.entity.DailyQuiz;
import com.leets.chikahae.domain.quiz.repository.DailyQuizRepository;
import com.leets.chikahae.domain.quiz.repository.QuizRepository;
import com.leets.chikahae.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final DailyQuizRepository dailyQuizRepository;

    @Override
    public List<QuizResponse> getQuizzes() {
        LocalDate quizDate = LocalDate.now();

        DailyQuiz dailyQuiz = dailyQuizRepository.findByQuizDate(quizDate)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.DATE_NOT_FOUND.getMessage()));

        return dailyQuiz.getQuizzes().stream()
                .map(QuizResponse::from)
                .toList();
    }


}
