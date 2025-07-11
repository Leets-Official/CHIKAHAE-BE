// package com.leets.chikahae.global.config;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.web.config.EnableSpringDataWebSupport;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// @Configuration
// @EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
// public class WebConfig implements WebMvcConfigurer {
//
//     @Value("${spring.front.host}")
//     private String localHost;
//
//     @Value("${spring.front.prod}")
//     private String frontProd;
//
//     @Value("${spring.back.prod}")
//     private String backProd;
//
//
//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//         return new WebMvcConfigurer() {
//             @Override
//             public void addCorsMappings(CorsRegistry registry) {
//                 registry.addMapping("/**")
//                         .allowedOrigins(localHost, frontProd, backProd)
//                         .allowedMethods("*")
//                         .allowCredentials(true);
//             }
//         };
//     }}
