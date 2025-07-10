package com.leets.chikahae.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;

@Service
public class FcmPushService {
	private final FirebaseMessaging firebaseMessaging;

	public FcmPushService(FirebaseMessaging firebaseMessaging) {
		this.firebaseMessaging = firebaseMessaging;
	}

	//Data 페이로드 방식: key/value 형태로 직접 데이터를 전송
	public BatchResponse sendMulticast(List<String> tokens, String title, String body) throws Exception {
		MulticastMessage message = MulticastMessage.builder()
			.addAllTokens(tokens)
			.putData("title", title)
			.putData("body", body)
			.build();
		return firebaseMessaging.sendMulticast(message);
	}
}
