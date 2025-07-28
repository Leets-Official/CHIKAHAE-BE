package com.leets.chikahae.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.chikahae.domain.member.dto.request.UpdateProfileRequest;
import com.leets.chikahae.domain.member.dto.response.MemberProfileResponse;
import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.member.repository.MemberRepository;
import com.leets.chikahae.domain.notification.service.NotificationSlotService;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final MemberRepository memberRepository;
	private final NotificationSlotService notificationSlotService;

	//마이페이지 프로필 조회 메소드
	@Transactional(readOnly = true)
	public MemberProfileResponse getProfile(Long memberId) {
		Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		return new MemberProfileResponse(
			member.getNickname(),
			member.getName(),
			member.getGender(),
			member.getBirth()
		);
	}

	// 프로필 수정 매소드
	@Transactional
	public void updateProfile(Long memberId, UpdateProfileRequest request) {
		Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String nickname = request.nickname();
		String profileImage = request.profileImage();

		if (nickname == null || nickname.isBlank()) {
			throw new CustomException(ErrorCode.INVALID_NICKNAME);
		}

		if (!member.getNickname().equals(nickname) &&
			memberRepository.existsByNickname(nickname)) {
			throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
		}

		member.changeNickname(nickname);

		if (profileImage != null && !profileImage.isBlank()) {
			member.changeProfileImage(profileImage);
		}
	}


}
