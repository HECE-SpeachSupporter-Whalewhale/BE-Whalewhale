package com.whalewhale.speachsupporter.Users;

import com.whalewhale.speachsupporter.Mail.MailDto;
import com.whalewhale.speachsupporter.Mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password")
@Validated
public class ForgotPasswordController {

    private final MailService mailService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws MessagingException {
        if (!usersRepository.findByUsername(email).isPresent()) {
            return ResponseEntity.badRequest().body("등록되지 않은 이메일입니다.");
        }

        mailService.sendSimpleMessage(email);
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        if (mailService.verifyCode(request.getEmail(), request.getCode())) {
            var userOptional = usersRepository.findByUsername(request.getEmail());
            if (userOptional.isPresent()) {
                var user = userOptional.get();
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                usersRepository.save(user);

                // 인증 코드 초기화
                mailService.invalidateCode(request.getEmail());

                return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
            }
        }
        return ResponseEntity.badRequest().body("인증 실패");
    }
}

class PasswordResetRequest {
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    private String email;
    @NotEmpty(message = "인증 코드를 입력하세요.")
    private String code;
    @Size(min = 8, max = 12, message = "비밀번호는 8자에서 12자 사이여야 합니다.")
    private String newPassword;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}