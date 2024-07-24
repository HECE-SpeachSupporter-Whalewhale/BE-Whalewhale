package com.whalewhale.speachsupporter.Mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final Map<String, String> authCodeStore = new HashMap<>(); // 이메일과 인증번호 저장

    // 랜덤 인증 코드 생성
    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(10));
                    break;
            }
        }
        return key.toString();
    }

    // 메일 양식 작성 및 인증번호 저장
    public void sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        String authCode = createCode(); // 인증 코드 생성
        authCodeStore.put(email, authCode); // 이메일과 인증 코드 저장

        String setFrom = "your-email@example.com"; // 자신의 이메일 주소 (보내는 사람)
        String title = "CODEBOX 회원가입 인증 번호"; // 제목

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email); // 보낼 이메일 설정
        helper.setSubject(title); // 제목 설정
        helper.setFrom(setFrom); // 보내는 이메일
        helper.setText(setContext(authCode), true); // HTML 형식으로 이메일 본문 설정

        emailSender.send(message); // 실제 메일 전송
    }

    // Thymeleaf를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context); // mail.html
    }

    // 인증번호 검증
    public boolean verifyAuthCode(String email, String authCode) {
        return authCode.equals(authCodeStore.get(email));
    }

    // 인증번호 삭제
    public void removeAuthCode(String email) {
        authCodeStore.remove(email);
    }
}

