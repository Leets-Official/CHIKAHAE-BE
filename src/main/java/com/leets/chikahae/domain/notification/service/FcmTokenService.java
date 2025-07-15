package com.leets.chikahae.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.notification.entity.FcmToken;
import com.leets.chikahae.domain.notification.repository.FcmTokenRepository;
import com.leets.chikahae.domain.member.entity.Member;

@Service
public class FcmTokenService {

	private final FcmTokenRepository fcmTokenRepo;
	//----테스트용으로 만듬 지워야함 !!!
	private final MemberRepository memberRepo;
	//여기도 멤버 레포 지워야함!!!
	public FcmTokenService(FcmTokenRepository fcmTokenRepo, MemberRepository memberRepo) {
		this.fcmTokenRepo = fcmTokenRepo;
		this.memberRepo = memberRepo;
	}

	//fcm 토큰 등록 & 이미 존재한다면 기존의 엔티티 반환
	//07-12 테스트위해 매개변수 Long타입으로 변경 아마 전부 Long으로 바꾸는게 나을듯
	@Transactional
	public FcmToken upsertToken(Long memberId, String tokenStr) {
		Member member = memberRepo.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("Member not found"));
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
			.orElseThrow(() -> new IllegalArgumentException("Member not found"));
		return fcmTokenRepo.findByMember(member);
	}
}
