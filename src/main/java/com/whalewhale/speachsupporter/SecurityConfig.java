package com.whalewhale.speachsupporter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 설정
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        // CSRF 토큰 저장소 설정
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        return repository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/", "/list", "/emailCheck").permitAll() // 인증 없이 접근 허용
                        .requestMatchers("/users").permitAll()
                        .anyRequest().authenticated()) // 나머지 요청은 인증 필요
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // 로그인 페이지 설정
                        .successHandler((request, response, authentication) -> {
                            System.out.println("로그인 성공: " + authentication.getName());
                            response.sendRedirect("/"); // 로그인 성공 후 리다이렉트
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("로그인 실패: " + exception.getMessage());
                            response.sendRedirect("/login?error=true"); // 로그인 실패 시 리다이렉트
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청 URL
                        .logoutSuccessHandler((request, response, authentication) -> {
                            System.out.println("로그아웃 성공: " + (authentication != null ? authentication.getName() : "Anonymous"));
                            response.sendRedirect("/"); // 로그아웃 성공 후 리다이렉트
                        })
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID")); // 쿠키 삭제

        return http.build();
    }

}
