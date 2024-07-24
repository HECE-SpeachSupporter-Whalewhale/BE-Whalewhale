package com.whalewhale.speachsupporter.Mail;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailAuthRequestDto {

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email(message = "유효한 이메일을 입력해주세요")
    private String email;

    private String authCode; // 인증번호 필드 추가
}