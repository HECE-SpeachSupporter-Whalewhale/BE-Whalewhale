package com.whalewhale.speachsupporter.gpt.DTO;

import java.util.ArrayList;
import java.util.List;

public class PresentationRequest {

    private String title;
    private String body;
    private List<String> keywordName;
    private int speedMinute;
    private int speedSecond;
    private String userJob;

    // 기본 생성자 추가
    public PresentationRequest() {
        this.title = "";
        this.body = "";
        this.keywordName = new ArrayList<>(); // 빈 리스트로 초기화
        this.speedMinute = 0; // 기본값 0분
        this.speedSecond = 0; // 기본값 0초
        this.userJob = "Unknown"; // 기본값 설정
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(List<String> keywordName) {
        this.keywordName = keywordName;
    }

    public int getSpeedMinute() {
        return speedMinute;
    }

    public void setSpeedMinute(int speedMinute) {
        this.speedMinute = speedMinute;
    }

    public int getSpeedSecond() {
        return speedSecond;
    }

    public void setSpeedSecond(int speedSecond) {
        this.speedSecond = speedSecond;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }
}
