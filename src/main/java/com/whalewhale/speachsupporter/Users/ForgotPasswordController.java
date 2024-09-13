package com.whalewhale.speachsupporter.Users;

import com.whalewhale.speachsupporter.Mail.MailDto;
import com.whalewhale.speachsupporter.Mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password")
public class ForgotPasswordController {

    private final MailService mailService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/forgot")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) throws MessagingException {
        String email = request.get("email");
        var userOptional = usersRepository.findByUsername(email);

        if (userOptional.isEmpty() || userOptional.get().getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "등록되지 않은 이메일입니다."));
        }

        mailService.sendSimpleMessage(email);
        return ResponseEntity.ok(Map.of("message", "인증 코드가 이메일로 전송되었습니다."));
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody PasswordResetRequest request) {
        var userOptional = usersRepository.findByUsername(request.getEmail());

        if (userOptional.isEmpty() || userOptional.get().getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "등록되지 않은 이메일입니다."));
        }

        var user = userOptional.get();

        if (mailService.verifyCode(request.getEmail(), request.getCode())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            usersRepository.save(user);

            // 인증 코드 초기화
            mailService.invalidateCode(request.getEmail());

            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 재설정되었습니다."));
        }

        return ResponseEntity.badRequest().body(Map.of("message", "인증 실패"));
    }
}

class PasswordResetRequest {
    private String email;
    private String code;
    private String newPassword;

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}