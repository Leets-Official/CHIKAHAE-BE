package com.leets.chikahae.global.config;

import org.springframework.context.annotation.Configuration;


@Configuration
public class JpaAuditingConfig {
	/**
	 JPA 엔티티에 자동으로 createdAt(생성 시각)와 updatedAt(수정 시각)을 채워 주는 기능
	  BaseEntity에 @CreatedDate·@LastModifiedDate를 붙였어도
	  이 설정이 없으면 스프링이 해당 애노테이션을 인식하지 않는다길래 ,,
	 */
}
