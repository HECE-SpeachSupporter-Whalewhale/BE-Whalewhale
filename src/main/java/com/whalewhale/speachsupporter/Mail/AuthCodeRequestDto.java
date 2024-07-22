package com.whalewhale.speachsupporter.Mail;

import lombok.Data;

@Data
public class AuthCodeRequestDto {
    private String email;
    private String authCode;
}