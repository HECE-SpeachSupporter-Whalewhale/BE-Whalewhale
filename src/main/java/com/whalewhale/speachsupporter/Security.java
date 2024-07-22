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
public class Security {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf
                -> csrf.csrfTokenRepository(csrfTokenRepository())
                        .ignoringRequestMatchers("/login"))
        ;

                http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/login").permitAll() // 로그인 페이지 접근 허용
                                .requestMatchers("/").permitAll() // 홈 페이지 접근 허용
                                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 로그인 페이지 설정
                                .defaultSuccessUrl("/", true) // 로그인 성공 후 홈 페이지로 리디렉션
                                .failureUrl("/login?error=true") // 로그인 실패 시 오류 표시
                );
        http.logout(logout->logout.logoutUrl("/logout"));
        return http.build();
    }
}
