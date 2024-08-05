package com.whalewhale.speachsupporter.Mail;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping("/send")
    public String sendEmail(@RequestParam String username) throws MessagingException {
        // username 파라미터를 이메일 주소로 사용
        String authCode = mailService.sendSimpleMessage(username);
        return authCode;
    }

    @ResponseBody
    @PostMapping("/verify")
    public String verifyEmail(@RequestBody MailDto mailDto, HttpSession session) {
        boolean isValid = mailService.verifyCode(mailDto.getEmail(), mailDto.getCode());
        // 세션 속성 이름을 emailVerified로 설정
        session.setAttribute("emailVerified", isValid);
        return isValid ? "인증 성공" : "인증 실패";
    }
}
