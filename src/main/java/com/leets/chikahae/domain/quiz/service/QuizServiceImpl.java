package com.leets.chikahae.domain.quiz.service;


import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.mission.entity.Mission;
import com.leets.chikahae.domain.mission.service.MissionService;
import com.leets.chikahae.domain.point.entity.UserPointHistory;
import com.leets.chikahae.domain.point.repository.PointRepository;
import com.leets.chikahae.domain.point.repository.UserPointHistoryRepository;
import com.leets.chikahae.domain.point.service.PointService;
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

import static com.leets.chikahae.global.response.ErrorCode.USER_NOT_FOUND;
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
    private final UserPointHistoryRepository userPointHistoryRepository;
    private final PointService pointService;
    private final MissionService missionService;

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
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public QuizResultResponse getQuizResult(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //  포인트 이미 지급된 퀴즈인지 확인
        if (userPointHistoryRepository.existsByMemberAndDateAndTypeAndDescription(member, LocalDate.now(), UserPointHistory.Type.EARN, "퀴즈 보상")) {
            throw new CustomException(ErrorCode.ALREADY_REWARDED);
        }
        // 퀴즈를 3문제 다 풀었는지 확인
        int solvedCount = memberQuizRepository.countTodaySolved(memberId, LocalDate.now());
        if (solvedCount < QUIZ_COUNT_PER_DAY) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_QUIZ_SOLVED);
        }

        // 정답 개수 조회
        int correctCount = memberQuizRepository.countTodayCorrect(memberId, LocalDate.now());

        // 보상 계산
        int coinReward = calculateReward(correctCount);

        // 퀴즈 응답 조회
        List<MemberQuiz> memberQuizzes = memberQuizRepository.findByMember_MemberId(memberId);
        if (memberQuizzes.isEmpty()) {
            throw new CustomException(ErrorCode.QUIZ_NOT_FOUND);
        }

        // 1. 퀴즈 자체 보상
        pointService.earnPoint(memberId, coinReward, "퀴즈 보상");

        // 2. 미션 보상
        missionService.completeRewardedMission(member, Mission.MissionCode.valueOf("DAILY_QUIZ"));

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
