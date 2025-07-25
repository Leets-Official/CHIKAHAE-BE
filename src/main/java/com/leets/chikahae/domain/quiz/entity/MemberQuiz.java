package com.leets.chikahae.domain.quiz.entity;

import com.leets.chikahae.domain.BaseEntity;
import com.leets.chikahae.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_quiz")
public class MemberQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_quiz_id")
    private Long memberQuizId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "selected_answer")
    private String selectedAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public static MemberQuiz of(Quiz quiz, Member member, String userAnswer, Boolean isCorrect) {
        return MemberQuiz.builder()
                .quiz(quiz)
                .member(member)
                .selectedAnswer(userAnswer)
                .isCorrect(isCorrect)
                .date(LocalDate.now())
                .build();
    }

    public boolean isCorrect() {
        return isCorrect != null && isCorrect;
    }
}
