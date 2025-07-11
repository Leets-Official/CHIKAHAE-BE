//package com.leets.chikahae.global.config.swagger;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.beans.factory.annotation.Value;
//
//@Configuration
//@Profile({"prod", "dev"})
//public class SwaggerConfig {
//
//    @Value("${spring.back.prod}")
//    private String serverUrl;
//
//    @Bean
//    public OpenAPI openAPI() {
//        String jwt = "JWT";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
//        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
//                .name(jwt)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("Bearer")
//                .bearerFormat("JWT")
//        );
//
//        Server server = new Server();
//        server.setUrl(serverUrl);
//        server.setDescription("치카해 API 명세서");
//
//        return new OpenAPI()
//                .components(components)
//                .info(apiInfo())
//                .addSecurityItem(securityRequirement)
//                .addServersItem(server);
//    }
//
//    private Info apiInfo() {
//        return new Info()
//                .version("1.0")
//                .title("치카해 API")
//                .description("치카해 개발 서버의 API 입니다");
//    }
//}
