package com.whalewhale.speachsupporter.Users;

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
                            @RequestParam @Size(min = 8, max = 12, message = "비밀번호는 8자에서 12자 사이어야 합니다.") String password) {
        var hash = passwordEncoder.encode(password);
        Users users = new Users();
        users.setUsername(username);
        users.setNickname(nickname);
        users.setIsAdmin(false);
        users.setPassword(hash);
        usersRepository.save(users);

        System.out.println("User saved: " + users);
        return "redirect:";
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
