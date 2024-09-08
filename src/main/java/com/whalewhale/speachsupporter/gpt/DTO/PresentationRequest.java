package com.whalewhale.speachsupporter.gpt.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PresentationRequest {

    private String title;
    private String body;

    @JsonProperty("keyword_name")
    private List<String> keywordName;

    @JsonProperty("speed_minute")
    private int speedMinute;

    @JsonProperty("speed_second")
    private int speedSecond;

    @JsonProperty("user_job")
    private String userJob;

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
