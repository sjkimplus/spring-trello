package com.sparta.springtrello.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

//@Service
//public class SlackNotificationService {
//
//    @Value("${slack.webhook.url}")
//    private String slackWebhookUrl;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public void sendSlackMessage(String message) {
//        // Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 메세지 생성
//        Map<String, String> body = new HashMap<>();
//        body.put("text", message);
//
//        // Map
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
//
//        // Slack webhook URL으로 POST 요청 보내기
//        restTemplate.exchange(slackWebhookUrl, HttpMethod.POST, request, String.class);
//    }
//}
