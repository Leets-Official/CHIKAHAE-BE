package com.leets.chikahae.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.notification.entity.FcmToken;
import com.leets.chikahae.domain.notification.repository.FcmTokenRepository;

@Service
public class FcmTokenService {

	private final FcmTokenRepository fcmTokenRepo;

	public FcmTokenService(FcmTokenRepository fcmTokenRepo) {
		this.fcmTokenRepo = fcmTokenRepo;
	}

	//fcm 토큰 등록 & 이미 존재한다면 기존의 엔티티 반환
	@Transactional
	public FcmToken upsertToken(Member member, String tokenStr) {
		return fcmTokenRepo.findByFcmToken(tokenStr)
			.orElseGet(() -> fcmTokenRepo.save(new FcmToken(member, tokenStr)));
	}

	@Transactional
	public void deleteToken(String tokenStr) {
		fcmTokenRepo.deleteByFcmToken(tokenStr);
	}

	public List<FcmToken> getTokens(Member member) {
		return fcmTokenRepo.findByMember(member);
	}
}
