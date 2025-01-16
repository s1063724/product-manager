package com.example.productmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 允許 /api 路徑下的所有端點
                .allowedOrigins("http://localhost:8081") // 允許的來源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許的請求頭
                .allowCredentials(true); // 允許攜帶憑證（如 cookies）
    }
}