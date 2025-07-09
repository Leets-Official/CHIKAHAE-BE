package com.leets.chikahae.domain.quiz.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuizType {

    OX("OX"), // OX 문제
    MCQ("객관식"); // 객관식 문제

    private final String typeName; // 문제 유형 이름
}
