package com.whalewhale.speachsupporter.Mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email") // 공통적인 URL 경로 추가
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping("/send") // 메일 발송 엔드포인트
    public String sendEmail(@RequestBody MailDto mailDto) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(mailDto.getEmail());
        return authCode; // Response body에 값을 반환
    }

    @ResponseBody
    @PostMapping("/verify") // 인증번호 검증 엔드포인트
    public String verifyEmail(@RequestBody MailDto mailDto) {
        boolean isValid = mailService.verifyCode(mailDto.getEmail(), mailDto.getCode());
        return isValid ? "인증 성공" : "인증 실패";
    }
}
