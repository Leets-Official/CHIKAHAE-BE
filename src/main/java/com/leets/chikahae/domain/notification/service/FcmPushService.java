package com.leets.chikahae.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.leets.chikahae.global.response.CustomException;
import com.leets.chikahae.global.response.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FcmPushService {
	private final FirebaseMessaging firebaseMessaging;

	public FcmPushService(FirebaseMessaging firebaseMessaging) {
		this.firebaseMessaging = firebaseMessaging;
	}

	public void sendToEach(List<String> tokens, String title, String body) {
		for (String token : tokens) {
			Message message = Message.builder()
				.setToken(token)
				// data 페이로드 방식
				.putData("title", title)
				.putData("body", body)
				.build();

			log.info("Sending FCM | token={} | title='{}' | body='{}'", token, title, body);

			try {
				String messageId = firebaseMessaging.send(message);
				// 성공 로그
				log.info("Sent FCM       | token={} | messageId={}", token, messageId);
			} catch (FirebaseMessagingException ex) {
				// 실패 로그
				log.error(" Failed FCM     | token={} | error={}", token, ex.getMessage());
			}
		}

	/*//Data 페이로드 방식: key/value 형태로 직접 데이터를 전송
	public BatchResponse sendMulticast(List<String> tokens, String title, String body) {
		try {
			MulticastMessage message = MulticastMessage.builder()
				.addAllTokens(tokens)
				.putData("title", title)
				.putData("body", body)
				.build();
			return firebaseMessaging.sendMulticast(message);
		} catch (Exception ex) {
			throw new CustomException(
				ErrorCode.FCM_PUSH_ERROR,
				"FCM 푸시 전송 실패: " + ex.getMessage()
			);
		}
	}

	 */
	}
}