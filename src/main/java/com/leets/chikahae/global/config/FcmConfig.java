package com.leets.chikahae.global.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FcmConfig {
	/**
	 * FirebaseMessaging 빈으로 등록
	 * - 클래스패스에서 서비스 계정 키(json) 파일을 로드
	 * - 이미 초기화된 FirebaseApp(default) 이 있으면 재사용하고,
	 *   없으면 새로 초기화.
	 */
	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		ClassPathResource resource = new ClassPathResource("firebase/team-chikahae-firebase-adminsdk-fbsvc-c6b2c76cbb.json");
		InputStream serviceAccountStream = resource.getInputStream();

		FirebaseApp firebaseApp = null;
		List<FirebaseApp> apps = FirebaseApp.getApps();
		if (apps != null && !apps.isEmpty()) {
			for (FirebaseApp app : apps) {
				if (FirebaseApp.DEFAULT_APP_NAME.equals(app.getName())) {
					firebaseApp = app;
					break;
				}
			}
		}
		if (firebaseApp == null) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
				.build();
			firebaseApp = FirebaseApp.initializeApp(options);
		}
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}

