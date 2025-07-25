package com.leets.chikahae.global.config;


import com.leets.chikahae.security.filter.JwtAuthenticationDeniedHandler;
import com.leets.chikahae.security.filter.JwtAuthenticationFailureHandler;
import com.leets.chikahae.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.leets.chikahae.security.util.TokenNameUtil.ACCESS_TOKEN_SUBJECT;
import static com.leets.chikahae.security.util.TokenNameUtil.REFRESH_TOKEN_SUBJECT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final JwtAuthenticationDeniedHandler jwtAuthenticationDeniedHandler;

    public static final String[] PERMIT_ALL_PATTERNS = {
            // 기본/홈/인증 관련
            "/",                        // 홈
            "/api/login/**",                // 로그인 관련
            "/api/dev-login",               // 개발자 로그인
            "/api/signup/**",               // 회원가입 관련
            "/api/auth/**",                 // 인증 관련 (ex: 카카오 콜백 등)
            "/login/kakao/callback",        // 카카오 로그인 콜백


            // 정적/문서/리소스
            "/docs/**",                 // API 문서
            "/*.ico",                   // 아이콘 파일 (루트)
            "/resources/**",            // 정적 리소스
            "/index.html",              // 인덱스 페이지
            "/error",                   // 에러 페이지
            "/static/*.png",               // 모든 PNG 정적 리소스 허용
            "/static/*.jpg",               // 모든 JPG 이미지 허용


            // Swagger/OpenAPI 관련
            "/v3/api-docs/**",          // OpenAPI 문서
            "/swagger-ui/**",           // Swagger UI
            "/swagger-resources/**",    // Swagger 리소스
            "/webjars/**",              // Swagger의 정적 리소스
            "/swagger-ui.html",         // Swagger UI HTML

    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(jwtAuthenticationFailureHandler)
                .accessDeniedHandler(jwtAuthenticationDeniedHandler)
        );


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        /// CORS 추가
        configuration.addAllowedOriginPattern("http://localhost:3000");
        configuration.addAllowedOriginPattern("https://api.chika-hae.site");
        configuration.addAllowedOriginPattern("https://chika-hae.site");

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of(ACCESS_TOKEN_SUBJECT, REFRESH_TOKEN_SUBJECT));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
