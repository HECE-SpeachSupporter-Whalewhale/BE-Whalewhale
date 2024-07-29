package com.whalewhale.speachsupporter;

import com.whalewhale.speachsupporter.Oauth2.OAuth2MemberService;
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
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/", "/list", "/email/send",
                                "/email/verify", "/Presentation", "/writePresentation", "/remember",
                                "/oauth2/**", "/complete-profile", "/password/forgot",
                                    "/password/reset").permitAll()
                        .requestMatchers("/users").permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            System.out.println("로그인 성공: " + authentication.getName());
                            response.sendRedirect("/");
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("로그인 실패: " + exception.getMessage());
                            response.sendRedirect("/login?error=true");
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
}