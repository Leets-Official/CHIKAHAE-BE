package com.leets.chikahae.global.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.ServiceAccountCredentials;
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

	@Value("${fcm.file-path}")
	private String filePath;  // 환경변수 값 주입

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		// 1) 서비스 계정 JSON 파일 로드
		ClassPathResource resource = new ClassPathResource("firebase/" + filePath);
		InputStream serviceAccountStream = resource.getInputStream();

		// 2) ServiceAccountCredentials 로 읽어서 projectId 를 꺼내올 수 있게
		ServiceAccountCredentials credentials =
				ServiceAccountCredentials.fromStream(serviceAccountStream);

		// 3) FirebaseOptions 에 credentials + projectId 를 모두 설정
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(credentials)
				.setProjectId(credentials.getProjectId())   // ★ 프로젝트 ID 추가
				.build();

		// 4) 이미 초기화된 앱이 있으면 재사용, 없으면 초기화
		List<FirebaseApp> apps = FirebaseApp.getApps();
		FirebaseApp app = apps.stream()
				.filter(a -> FirebaseApp.DEFAULT_APP_NAME.equals(a.getName()))
				.findFirst()
				.orElseGet(() -> FirebaseApp.initializeApp(options));

		return FirebaseMessaging.getInstance(app);
	}
}

