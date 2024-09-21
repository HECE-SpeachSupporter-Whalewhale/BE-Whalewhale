package com.whalewhale.speachsupporter.Users;

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
    public ResponseEntity<?> forgotPassword(@RequestParam(required = false) String email,
                                            @RequestBody(required = false) Map<String, String> body) throws MessagingException {
        String userEmail = email != null ? email : (body != null ? body.get("email") : null);
        if (userEmail == null || userEmail.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일이 제공되지 않았습니다."));
        }

        var userOptional = usersRepository.findByUsername(userEmail);

        if (userOptional.isEmpty() || userOptional.get().getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "등록되지 않은 이메일입니다."));
        }

        String authCode = mailService.sendSimpleMessage(userEmail);
        return ResponseEntity.ok(Map.of("message", "인증 코드가 이메일로 전송되었습니다."));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam(required = false) String email,
                                           @RequestParam(required = false) String code,
                                           @RequestParam(required = false) String newPassword,
                                           @RequestBody(required = false) Map<String, String> body) {
        String resetEmail = email != null ? email : (body != null ? body.get("email") : null);
        String resetCode = code != null ? code : (body != null ? body.get("code") : null);
        String resetNewPassword = newPassword != null ? newPassword : (body != null ? body.get("newPassword") : null);

        if (resetEmail == null || resetCode == null || resetNewPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "필요한 정보가 모두 제공되지 않았습니다."));
        }

        var userOptional = usersRepository.findByUsername(resetEmail);

        if (userOptional.isEmpty() || userOptional.get().getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "등록되지 않은 이메일입니다."));
        }

        var user = userOptional.get();

        if (mailService.verifyCode(resetEmail, resetCode)) {
            user.setPassword(passwordEncoder.encode(resetNewPassword));
            usersRepository.save(user);

            // 인증 코드 초기화
            mailService.invalidateCode(resetEmail);

            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 재설정되었습니다."));
        }

        return ResponseEntity.badRequest().body(Map.of("message", "인증 실패"));
    }
}
