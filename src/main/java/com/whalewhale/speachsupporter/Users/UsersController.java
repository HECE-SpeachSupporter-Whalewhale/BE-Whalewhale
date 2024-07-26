package com.whalewhale.speachsupporter.Users;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Size;

@Controller
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    String register(){
        return "register.html";
    }

    @PostMapping("/users")
    public String addMember(@RequestParam String username,
                            @RequestParam String nickname,
                            @RequestParam String user_job,
                            @RequestParam @Size(min = 8, max = 12, message = "비밀번호는 8자에서 12자 사이어야 합니다.") String password,
                            HttpSession session) {
        // 이메일 인증 확인
        Boolean emailVerified = (Boolean) session.getAttribute("emailVerified");
        if (emailVerified == null || !emailVerified) {
            return "redirect:/register?error=emailNotVerified";
        }

        // 비밀번호 해싱
        var hash = passwordEncoder.encode(password);

        // 사용자 객체 생성 및 설정
        Users users = new Users();
        users.setUsername(username);
        users.setNickname(nickname);
        users.setUser_job(user_job);
        users.setIsAdmin(false);
        users.setPassword(hash);

        // DB에 저장
        try {
            usersRepository.save(users);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register?error=saveFailed";
        }

        // 인증 상태 세션에서 제거
        session.removeAttribute("emailVerified");

        System.out.println("User saved: " + users);
        return "redirect:/";
    }



    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth){
        System.out.println(auth.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_USER")
        ));
        System.out.println(auth);
        return "myPage.html";
    }

}
