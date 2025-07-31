package com.leets.chikahae.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.notification.entity.FcmToken;
import com.leets.chikahae.domain.notification.repository.FcmTokenRepository;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;

@Service
public class FcmTokenService {

	private final FcmTokenRepository fcmTokenRepo;
	private final MemberRepository memberRepo;

	public FcmTokenService(FcmTokenRepository fcmTokenRepo, MemberRepository memberRepo) {
		this.fcmTokenRepo = fcmTokenRepo;
		this.memberRepo = memberRepo;
	}

	//fcm 토큰 등록 & 이미 존재한다면 기존의 엔티티 반환
	@Transactional
	public FcmToken upsertToken(Long memberId, String tokenStr) {
		Member member = memberRepo.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return fcmTokenRepo.findByFcmToken(tokenStr)
			.orElseGet(() -> fcmTokenRepo.save(new FcmToken(member, tokenStr)));
	}

	@Transactional
	public void deleteToken(String tokenStr) {
		fcmTokenRepo.deleteByFcmToken(tokenStr);
	}

	//여기도 매개변수때매 조회수정
	public List<FcmToken> getTokens(Long memberId) {
		Member member = memberRepo.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return fcmTokenRepo.findByMember(member);

	}
}
