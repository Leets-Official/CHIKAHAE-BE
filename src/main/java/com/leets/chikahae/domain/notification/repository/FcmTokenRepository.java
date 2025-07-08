package com.leets.chikahae.domain.notification.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.leets.chikahae.domain.notification.entity.FcmToken;

@Repository
public interface FcmTokenRepository {

	//스케줄러에서 푸시 전ㄴ송 대상 토큰 수집 시에 사용
	List<FcmToken> findByMember(Member member);

	Optional<FcmToken> findByFcmToken(String fcmToken);


	void deleteByFcmToken(String fcmToken);
}
