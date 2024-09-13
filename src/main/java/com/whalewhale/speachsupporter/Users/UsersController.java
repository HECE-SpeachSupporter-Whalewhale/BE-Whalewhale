package com.whalewhale.speachsupporter.Users;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Optional;

@RestController  // @Controller -> @RestController로 변경
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<?> addMember(@RequestParam String username,
                                       @RequestParam @Size(max = 4, message = "닉네임은 4자 이하여야 합니다.") String nickname,
                                       @RequestParam String user_job,
                                       @RequestParam @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$",
                                               message = "비밀번호는 영문자, 숫자를 혼합하여 8자 이상 12자 이하여야 합니다.") String password,
                                       HttpSession session) {

        // 이메일 인증 확인
        Boolean emailVerified = (Boolean) session.getAttribute("emailVerified");
        if (emailVerified == null || !emailVerified) {
            return ResponseEntity.badRequest().body("이메일 인증이 완료되지 않았습니다.");
        }

        // 사용자 존재 여부 확인
        if (usersRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자입니다.");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 저장 중 오류가 발생했습니다.");
        }

        // 인증 상태 세션에서 제거
        session.removeAttribute("emailVerified");

        return ResponseEntity.ok(users);  // 저장된 사용자 객체를 JSON으로 반환
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> handleOAuth2Success(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String email;

        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            email = oauth2User.getAttribute("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;
            email = userDetails.getUsername();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("알 수 없는 principal 타입");
        }

        Users user = usersRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if ("INCOMPLETE_PROFILE".equals(user.getUser_job())) {
            return ResponseEntity.ok("프로필을 완성해주세요.");
        } else {
            return ResponseEntity.ok(user);  // 사용자 정보를 JSON으로 반환
        }
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> saveProfile(@RequestBody Map<String, String> request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String email;

        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            email = oauth2User.getAttribute("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;
            email = userDetails.getUsername();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("알 수 없는 principal 타입");
        }

        Users user = usersRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 요청 본문에서 user_job 값을 추출
        String userJob = request.get("user_job");
        if (userJob == null) {
            return ResponseEntity.badRequest().body("user_job 값이 누락되었습니다.");
        }

        user.setUser_job(userJob);
        usersRepository.save(user);

        return ResponseEntity.ok(user);  // 업데이트된 사용자 정보를 JSON으로 반환
    }


}
