package com.example.productmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("配置 SecurityFilterChain");
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 禁用 Session
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/auth/register", "/api/auth/login").permitAll() // 允許無需認證的路徑
                        .requestMatchers("/api/products/**").permitAll() // 允許 /api/products 路徑下的所有端點
                        .requestMatchers("/api/user-info/**").permitAll() // 允許 /api/user-info 路徑下的所有端點
                        .requestMatchers("/api/logs/**").permitAll() // 允許 /api/logs 路徑下的所有端點
                        .anyRequest().authenticated() // 其他請求需要認證
                )
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:8081")); // 允許的前端域名
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // 允許的 HTTP 方法
                    corsConfig.setAllowedHeaders(List.of("*")); // 允許的請求頭
                    corsConfig.setAllowCredentials(true); // 允許攜帶憑證
                    return corsConfig;
                }));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}