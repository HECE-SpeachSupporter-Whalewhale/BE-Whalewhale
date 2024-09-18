package com.whalewhale.speachsupporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whalewhale.speachsupporter.Oauth2.OAuth2MemberService;
import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsersRepository usersRepository;

    public SecurityConfig(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UsersRepository usersRepository) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register","/api/**", "/", "/list", "/email/send",
                                "/email/verify", "/Presentation", "/oauth2/**", "/complete-profile", "/password/forgot",
                                "/password/reset", "/bot/**", "/generate/**").permitAll()
                        .requestMatchers("/users").permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 시 JSON 응답
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(200);

                            String username = authentication.getName();
                            Users user = usersRepository.findByUsername(username)
                                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

                            Map<String, Object> data = new HashMap<>();
                            data.put("message", "로그인 성공");
                            data.put("username", user.getUsername());
                            data.put("nickname", user.getNickname());

                            ObjectMapper objectMapper = new ObjectMapper();
                            response.getWriter().write(objectMapper.writeValueAsString(data));
                        }).failureHandler((request, response, exception) -> {
                            // 로그인 실패 시 JSON 응답
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(401);

                            Map<String, Object> data = new HashMap<>();
                            data.put("message", "로그인 실패");
                            data.put("error", exception.getMessage());

                            ObjectMapper objectMapper = new ObjectMapper();
                            response.getWriter().write(objectMapper.writeValueAsString(data));
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            System.out.println("로그아웃 성공: " + (authentication != null ? authentication.getName() : "Anonymous"));
                            response.sendRedirect("/");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService()))
                        .successHandler((request, response, authentication) -> {
                            System.out.println("OAuth2 로그인 성공: " + authentication.getName());
                            response.sendRedirect("/oauth2/success");
                        }));

        return http.build();
    }

    @Bean
    public OAuth2MemberService oAuth2UserService() {
        return new OAuth2MemberService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 출처 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 메서드 설정
        configuration.setAllowedHeaders(Arrays.asList("*")); // 허용할 헤더 설정
        configuration.setAllowCredentials(true); // 자격 증명 허용 여부 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
}
