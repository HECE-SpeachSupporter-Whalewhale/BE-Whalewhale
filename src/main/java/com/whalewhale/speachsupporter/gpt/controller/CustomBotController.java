package com.whalewhale.speachsupporter.gpt.controller;

import com.whalewhale.speachsupporter.gpt.DTO.ChatGPTRequest;
import com.whalewhale.speachsupporter.gpt.DTO.ChatGPTResponse;
import com.whalewhale.speachsupporter.gpt.DTO.PresentationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @PostMapping("/generate")
    public String generatePresentation(@RequestBody PresentationRequest presentationRequest) {
        String prompt = createPromptFromRequest(presentationRequest);
        ChatGPTRequest request = new ChatGPTRequest(this.model, prompt);
        ChatGPTResponse chatGPTResponse = null;

        try {
            chatGPTResponse = this.template.postForObject(this.apiURL, request, ChatGPTResponse.class);
        } catch (HttpClientErrorException.TooManyRequests e) {
            int retryCount = 0;
            int maxRetries = 3;
            int waitTime = 2000;

            while (retryCount < maxRetries) {
                try {
                    Thread.sleep(waitTime);
                    chatGPTResponse = this.template.postForObject(this.apiURL, request, ChatGPTResponse.class);
                    break;
                } catch (HttpClientErrorException.TooManyRequests ex) {
                    retryCount++;
                    waitTime *= 2;
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }

            if (chatGPTResponse == null) {
                return "Error: Too many requests. Please try again later.";
            }
        } catch (HttpClientErrorException e) {
            return "Error: " + e.getMessage();
        }

        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    private String createPromptFromRequest(PresentationRequest presentationRequest) {
        String keywords = (presentationRequest.getKeywordName() != null) ?
                String.join(", ", presentationRequest.getKeywordName()) :
                "No keywords provided";

        System.out.println(String.format(
                "제목: \"%s\"\n본문: \"%s\"\n키워드: \"%s\"\n제한시간: \"%d분 %d초\"\n사용자 유형: \"%s\"",
                presentationRequest.getTitle(),
                presentationRequest.getBody(),
                keywords,
                presentationRequest.getSpeedMinute(),
                presentationRequest.getSpeedSecond(),
                presentationRequest.getUserJob()
        ));

        return String.format(
                "제목: \"%s\"\n본문: \"%s\"\n키워드: \"%s\"\n제한시간: \"%d분 %d초\"\n사용자 유형: \"%s\"",
                presentationRequest.getTitle(),
                presentationRequest.getBody(),
                keywords,
                presentationRequest.getSpeedMinute(),
                presentationRequest.getSpeedSecond(),
                presentationRequest.getUserJob()
        );
    }
}
