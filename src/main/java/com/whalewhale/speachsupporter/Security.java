package com.whalewhale.speachsupporter;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf)->csrf.disable());
        http.authorizeHttpRequests((authorize)->
                authorize.requestMatchers("/**").permitAll()
                );
        http.formLogin((formLogin)->
                formLogin.loginPage("/login")
                .defaultSuccessUrl("/", true) // 로그인 성공 후 리다이렉트될 URL
                .failureUrl("/login?error=true")); // 로그인 실패 시 리다이렉트될 URL
        return http.build();
    }
}
