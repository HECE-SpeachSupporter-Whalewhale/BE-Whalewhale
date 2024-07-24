package com.whalewhale.speachsupporter.Mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping("/emailCheck") // 이 부분은 각자 바꿔주시면 됩니다.
    public String emailCheck(@RequestBody MailDto mailDto) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(mailDto.getEmail());
        return authCode; // Response body에 값을 반환
    }
}
