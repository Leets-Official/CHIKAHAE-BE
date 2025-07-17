package com.leets.chikahae.global.config;


import com.leets.chikahae.security.filter.JwtAuthenticationDeniedHandler;
import com.leets.chikahae.security.filter.JwtAuthenticationFailureHandler;
import com.leets.chikahae.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

            // 인증/토큰 관련
            "/api/v1/auth/reissue",     // 토큰 재발급
            "/api/v1/auth/logout",      // 로그아웃

            // 정적/문서/리소스
            "/docs/**",                 // API 문서
            "/*.ico",                   // 아이콘 파일 (루트)
            "/resources/**",            // 정적 리소스
            "/index.html",              // 인덱스 페이지
            "/error",                   // 에러 페이지

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
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
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
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://chika-hae.site"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList(ACCESS_TOKEN_SUBJECT, REFRESH_TOKEN_SUBJECT));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }
}
