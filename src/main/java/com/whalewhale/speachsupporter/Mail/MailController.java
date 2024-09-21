package com.whalewhale.speachsupporter.Mail;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestParam(required = false) String username,
                                       @RequestBody(required = false) Map<String, String> body) throws MessagingException {
        String email = username != null ? username : (body != null ? body.get("email") : null);
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일이 제공되지 않았습니다."));
        }
        String authCode = mailService.sendSimpleMessage(email);
        return ResponseEntity.ok().body(Map.of("message", "인증 코드가 전송되었습니다.", "authCode", authCode));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam(required = false) String email,
                                         @RequestParam(required = false) String code,
                                         @RequestBody(required = false) Map<String, String> body,
                                         HttpSession session) {
        String verifyEmail = email != null ? email : (body != null ? body.get("email") : null);
        String verifyCode = code != null ? code : (body != null ? body.get("code") : null);
        if (verifyEmail == null || verifyCode == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 또는 인증 코드가 제공되지 않았습니다."));
        }
        boolean isValid = mailService.verifyCode(verifyEmail, verifyCode);
        session.setAttribute("emailVerified", isValid);
        return ResponseEntity.ok().body(Map.of("verification", isValid));
    }
}
