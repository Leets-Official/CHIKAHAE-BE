package com.leets.chikahae.global.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		// 1) ObjectMapper에 JavaTimeModule 등록
		ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// 2) Default Typing 활성화: NON_FINAL 타입(엔티티, DTO 등)에는 JSON에 @class 정보 추가
		mapper.activateDefaultTyping(
			LaissezFaireSubTypeValidator.instance,
			ObjectMapper.DefaultTyping.EVERYTHING,
			JsonTypeInfo.As.PROPERTY
		);

		// 3) JSON 직렬화기 생성 (타입 정보 포함된 ObjectMapper 사용)
		GenericJackson2JsonRedisSerializer jsonSerializer =
			new GenericJackson2JsonRedisSerializer(mapper);

		return RedisCacheConfiguration.defaultCacheConfig()
			.prefixCacheNameWith("chikahae::")
			.entryTtl(Duration.ofHours(6))
			.serializeKeysWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(jsonSerializer));
	}

	@Bean
	public org.springframework.cache.CacheManager cacheManager(
		org.springframework.data.redis.connection.RedisConnectionFactory cf,
		RedisCacheConfiguration config) {

		return org.springframework.data.redis.cache.RedisCacheManager
			.builder(cf)
			.cacheDefaults(config)
			.build();
	}
}
