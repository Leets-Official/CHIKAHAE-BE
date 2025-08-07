package com.leets.chikahae.domain.point.service;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.point.dto.response.PointHistoryResponseDto;
import com.leets.chikahae.domain.point.entity.Point;
import com.leets.chikahae.domain.point.entity.UserPointHistory;
import com.leets.chikahae.domain.point.repository.PointRepository;
import com.leets.chikahae.domain.point.repository.UserPointHistoryRepository;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final UserPointHistoryRepository userPointHistoryRepository;
    private final MemberRepository memberRepository;

    /**
     * 현재 잔액 반환
     */
    @Transactional(readOnly = true)
    public int getPoint(Long memberId) {
        Point point = pointRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));
        return point.getCoin();
    }

    /**
     * 포인트 적립
     */
    @Transactional
    public int earnPoint(Long memberId, int amount, String description) {
        Point point = getOrCreatePoint(memberId);
        point.increase(amount);

        saveHistory(memberId, amount, UserPointHistory.Type.EARN, description);
        log.info("[PointService] memberId={} {}포인트 적립 완료", memberId, amount);

        return point.getCoin();
    }

    /**
     * 포인트 소비
     */
    @Transactional
    public void consumePoint(Long memberId, int amount, String description) {
        Point point = getOrCreatePoint(memberId);

        if (point.getCoin() < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COIN);
        }

        point.decrease(amount);

        saveHistory(memberId, -amount, UserPointHistory.Type.CONSUME, description);

        log.info("[PointService] memberId={} {}포인트 소비 완료", memberId, amount);
    }

    /**
     * 포인트 이력 조회
     */
    @Transactional(readOnly = true)
    public List<PointHistoryResponseDto> getHistory(Long memberId) {
        List<UserPointHistory> history = userPointHistoryRepository.findByMember_MemberId(memberId);
        return history.stream()
                .map(PointHistoryResponseDto::from)
                .collect(Collectors.toList());
    }

    // --- 내부 유틸 메서드 ---

    private Point getOrCreatePoint(Long memberId) {
        return pointRepository.findById(memberId).orElseGet(() -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            Point newPoint = Point.builder()
                    .member(member)
                    .coin(0)
                    .build();
            return pointRepository.save(newPoint);
        });
    }

    private void saveHistory(Long memberId, int amount, UserPointHistory.Type type, String description) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserPointHistory history = UserPointHistory.builder()
                .member(member)
                .amount(amount)
                .type(type)
                .description(description)
                .build();

        userPointHistoryRepository.save(history);
    }
}
