package com.leets.chikahae.domain.quiz.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @ManyToOne
    @JoinColumn(name = "daily_quiz_id", nullable = false)
    private DailyQuiz dailyQuiz;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "answer_description", nullable = true)
    private String answerDescription;

    @Convert(converter = StringListConverter.class)
    @Column(name = "options", nullable = false)
    private List<String> options; // 객관식 옵션 (JSON 형태로 저장)

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuizType type; // 문제 유형 (OX, 객관식 등)

}
