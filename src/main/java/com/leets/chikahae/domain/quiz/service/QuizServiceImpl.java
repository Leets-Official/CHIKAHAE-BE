package com.leets.chikahae.domain.quiz.service;

import com.leets.chikahae.domain.quiz.dto.response.CheckQuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import com.leets.chikahae.domain.quiz.entity.DailyQuiz;
import com.leets.chikahae.domain.quiz.entity.MemberQuiz;
import com.leets.chikahae.domain.quiz.entity.Quiz;
import com.leets.chikahae.domain.quiz.repository.DailyQuizRepository;
import com.leets.chikahae.domain.quiz.repository.MemberQuizRepository;
import com.leets.chikahae.domain.quiz.repository.QuizRepository;
import com.leets.chikahae.domain.user.Member;
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
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final MemberQuizRepository memberQuizRepository;

    @Override
    public List<QuizResponse> getQuizzes() {
        // 1. 오늘 날짜 구하기
        LocalDate quizDate = LocalDate.now();

        // 2. 오늘 날짜의 DailyQuiz 조회 (없으면 예외 발생)
        DailyQuiz dailyQuiz = dailyQuizRepository.findByQuizDate(quizDate)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.DATE_NOT_FOUND.getMessage()));

        // 3. DailyQuiz에 속한 Quiz들을 QuizResponse로 변환하여 반환
        return dailyQuiz.getQuizzes().stream()
                .map(QuizResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public CheckQuizResponse checkQuizAnswer(Long quizId, String selectedAnswer, Long memberId) {
        // quizId로 퀴즈 조회 (없으면 예외 발생)
        Quiz quiz = quizRepository.findByQuizId(quizId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.QUIZ_NOT_FOUND.getMessage()));

        // 정답 비교
        boolean isCorrect = quiz.getAnswer().equalsIgnoreCase(selectedAnswer);

        // memberId로 회원 조회 (없으면 예외 발생)
        Member member = findMemberById(memberId);

        // 중복 응답(이미 푼 퀴즈) 확인
        checkDuplicateResponse(quiz, member);

        // 퀴즈 응답 기록 저장
        MemberQuiz memberQuiz = MemberQuiz.of(quiz, member, selectedAnswer, isCorrect);
        memberQuizRepository.save(memberQuiz);

        // 결과 응답 생성 및 반환
        return CheckQuizResponse.from(quiz.getQuizId(), isCorrect, quiz.getAnswer(), quiz.getAnswerDescription());
    }

    // memberId로 회원 조회 (없으면 예외 발생)
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
    }

    //이미 해당 퀴즈에 응답했는지 확인 (중복 응답 방지)
    private void checkDuplicateResponse(Quiz quiz, Member member) {
        if (memberQuizRepository.existsByQuizAndMember(quiz, member)) {
            throw new IllegalStateException("이미 해당 퀴즈에 응답하셨습니다.");
        }
    }


    // 퀴즈 결과 반환 및 보상 처리
//    @Override
//    public CheckQuizResponse getQuizResult(Long quizId) {
//        // quizId로 퀴즈 조회
//        Quiz quiz = quizRepository.findByQuizId(quizId)
//                .orElseThrow(() -> new NoSuchElementException(ErrorCode.QUIZ_NOT_FOUND.getMessage()));
//
//        // CheckQuizResponse 생성
//        return CheckQuizResponse.from(quiz.getQuizId(), true, quiz.getAnswer(), quiz.getAnswerDescription());
//    }



}
