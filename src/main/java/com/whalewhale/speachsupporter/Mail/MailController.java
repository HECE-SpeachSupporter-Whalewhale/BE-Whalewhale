package com.whalewhale.speachsupporter.Mail;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {

    private final MailService mailService;

    // 이메일 전송
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody MailDto mailDto) throws MessagingException {
        // username 파라미터를 이메일 주소로 사용
        String authCode = mailService.sendSimpleMessage(mailDto.getEmail());
        return ResponseEntity.ok().body("{\"authCode\": \"" + authCode + "\"}");
    }

    // 이메일 인증 확인
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody MailDto mailDto, HttpSession session) {
        boolean isValid = mailService.verifyCode(mailDto.getEmail(), mailDto.getCode());
        // 세션에 인증 결과 저장
        session.setAttribute("emailVerified", isValid);
        return ResponseEntity.ok().body("{\"verification\": " + isValid + "}");
    }
}
