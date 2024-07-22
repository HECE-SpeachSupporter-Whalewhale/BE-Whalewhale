package com.whalewhale.speachsupporter.Mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("login/mailConfirm")
    public String mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(emailDto.getEmail());
        return "인증번호가 이메일로 전송되었습니다.";
    }

    @PostMapping("verifyAuthCode")
    public Map<String, Boolean> verifyAuthCode(@RequestBody EmailAuthRequestDto emailDto) {
        boolean isValid = emailService.verifyAuthCode(emailDto.getEmail(), emailDto.getAuthCode());
        if (isValid) {
            emailService.removeAuthCode(emailDto.getEmail()); // 인증 후 인증번호 삭제
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        return response;
    }
}
