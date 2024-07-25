package com.whalewhale.speachsupporter.Mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email") // 공통적인 URL 경로 추가
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
    @PostMapping("/verify") // 인증번호 검증 엔드포인트
    public String verifyEmail(@RequestBody MailDto mailDto) {
        boolean isValid = mailService.verifyCode(mailDto.getEmail(), mailDto.getCode());
        return isValid ? "인증 성공" : "인증 실패";
    }
}
