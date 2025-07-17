package com.leets.chikahae.domain.quiz.service;


import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.point.entity.Point;
import com.leets.chikahae.domain.point.repository.PointRepository;
import com.leets.chikahae.domain.quiz.dto.response.CheckQuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResponse;
import com.leets.chikahae.domain.quiz.dto.response.QuizResultResponse;
import com.leets.chikahae.domain.quiz.entity.DailyQuiz;
import com.leets.chikahae.domain.quiz.entity.MemberQuiz;
import com.leets.chikahae.domain.quiz.entity.Quiz;
import com.leets.chikahae.domain.quiz.repository.DailyQuizRepository;
import com.leets.chikahae.domain.quiz.repository.MemberQuizRepository;
import com.leets.chikahae.domain.quiz.repository.QuizRepository;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private static final int QUIZ_COUNT_PER_DAY = 3;
    private static final int REWARD_ALL_CORRECT = 30;
    private static final int REWARD_TWO_CORRECT = 20;
    private static final int REWARD_ONE_CORRECT = 10;

    private final DailyQuizRepository dailyQuizRepository;
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final PointRepository pointRepository;

    @Override
    @Transactional(readOnly = true)
    public List<QuizResponse> getQuizzes() {
        // 오늘 날짜의 DailyQuiz 조회
        DailyQuiz dailyQuiz = dailyQuizRepository.findByQuizDate(LocalDate.now())
                .orElseThrow(() -> new CustomException(ErrorCode.DATE_NOT_FOUND));

        // DailyQuiz에 속한 Quiz들을 QuizResponse로 변환하여 반환
        return dailyQuiz.getQuizzes().stream()
                .map(QuizResponse::from)
                .toList();
    }

    @Override
    public CheckQuizResponse checkQuizAnswer(Long quizId, String selectedAnswer, Long memberId) {
        // quizId로 퀴즈 조회
        Quiz quiz = quizRepository.findByQuizId(quizId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));

        // 정답 비교
        boolean isCorrect = quiz.getAnswer().equalsIgnoreCase(selectedAnswer);

        // memberId로 회원 조회
        Member member = findMemberById(memberId);

        // 중복 응답(이미 푼 퀴즈) 확인
        checkDuplicateResponse(quiz, member);

        // 퀴즈 응답 기록 저장
        MemberQuiz memberQuiz = MemberQuiz.of(quiz, member, selectedAnswer, isCorrect);
        memberQuizRepository.save(memberQuiz);

        // 결과 응답 생성 및 반환
        return CheckQuizResponse.from(quiz.getQuizId(), isCorrect, selectedAnswer, quiz.getAnswer(), quiz.getAnswerDescription());
    }

    @Override
    public void resetQuizHistory(long memberId) {
        // memberId로 퀴즈 응답 기록 삭제
        memberQuizRepository.deleteByMember_MemberId(memberId);
    }

    // memberId로 회원 조회
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    //이미 해당 퀴즈에 응답했는지 확인 (중복 응답 방지)
    private void checkDuplicateResponse(Quiz quiz, Member member) {
        if (memberQuizRepository.existsByQuizAndMember(quiz, member)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESPONSE);
        }
    }


    @Override
    @Transactional
    public QuizResultResponse getQuizResult(long memberId) {
        // 퀴즈를 3문제 다 풀었는지 확인
        int solvedCount = memberQuizRepository.countByMember_MemberId(memberId);
        if (solvedCount < QUIZ_COUNT_PER_DAY) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_QUIZ_SOLVED);
        }

        // 정답 개수 조회
        int correctCount = memberQuizRepository.countByMember_MemberIdAndIsCorrectTrue(memberId);

        // 보상 계산
        int coinReward = calculateReward(correctCount); // 예: 3문제 정답 시 30코인

        // 포인트 지급
        Point point = pointRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));

        point.addCoin(coinReward);

        // 퀴즈 응답 조회
        List<MemberQuiz> memberQuizzes = memberQuizRepository.findByMember_MemberId(memberId);
        if (memberQuizzes.isEmpty()) {
            throw new CustomException(ErrorCode.QUIZ_NOT_FOUND);
        }

        // 응답 정보 생성
        List<CheckQuizResponse> responses = memberQuizzes.stream()
                .map(this::buildCheckQuizResponse)
                .collect(toList());
        return QuizResultResponse.from(correctCount, coinReward, responses);
    }

    private CheckQuizResponse buildCheckQuizResponse(Quiz quiz, boolean isCorrect, String selectedAnswer) {
        return CheckQuizResponse.from(
                quiz.getQuizId(),
                isCorrect,
                selectedAnswer,
                quiz.getAnswer(),
                quiz.getAnswerDescription());
    }

    private CheckQuizResponse buildCheckQuizResponse(MemberQuiz memberQuiz) {
        Quiz quiz = memberQuiz.getQuiz();
        return buildCheckQuizResponse(quiz, memberQuiz.isCorrect(), memberQuiz.getSelectedAnswer());
    }

    // 정답 개수에 따라 보상 계산 (예: 3문제 정답 시 30코인)
    private int calculateReward(int correctCount) {
        switch (correctCount) {
            case QUIZ_COUNT_PER_DAY:
                return REWARD_ALL_CORRECT;
            case 2:
                return REWARD_TWO_CORRECT;
            case 1:
                return REWARD_ONE_CORRECT;
            default:
                return 0;
        }
    }


}
