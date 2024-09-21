package com.whalewhale.speachsupporter.Users;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsersController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<?> addMember(@RequestBody UserRegistrationDto userRegistrationDto,
                                       HttpSession session) {
        Boolean emailVerified = (Boolean) session.getAttribute("emailVerified");
        if (emailVerified == null || !emailVerified) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 인증이 필요합니다."));
        }

        if (usersRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 존재하는 사용자입니다."));
        }

        var hash = passwordEncoder.encode(userRegistrationDto.getPassword());

        Users users = new Users();
        users.setUsername(userRegistrationDto.getUsername());
        users.setNickname(userRegistrationDto.getNickname());
        users.setUser_job(userRegistrationDto.getUser_job());
        users.setIsAdmin(false);
        users.setPassword(hash);

        try {
            usersRepository.save(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "사용자 저장에 실패했습니다."));
        }

        session.removeAttribute("emailVerified");

        return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> handleOAuth2Success(Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        Users user = usersRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("INCOMPLETE_PROFILE".equals(user.getUser_job())) {
            return ResponseEntity.ok().body(Map.of("message", "프로필 완성이 필요합니다."));
        } else {
            return ResponseEntity.ok().body(Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "nickname", user.getNickname(),
                    "user_job", user.getUser_job(),
                    "isAdmin", user.getIsAdmin()
            ));
        }
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> saveProfile(@RequestBody Map<String, String> profileRequest, Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String user_job = profileRequest.get("user_job");

        Users user = usersRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUser_job(user_job);
        usersRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "nickname", user.getNickname(),
                "user_job", user.getUser_job(),
                "isAdmin", user.getIsAdmin()
        ));
    }
}

class UserRegistrationDto {
    private String username;
    private String nickname;
    private String user_job;
    private String password;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getUser_job() { return user_job; }
    public void setUser_job(String user_job) { this.user_job = user_job; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
