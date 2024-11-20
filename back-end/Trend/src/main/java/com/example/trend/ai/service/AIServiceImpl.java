package com.example.trend.ai.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Service
public class AIServiceImpl implements AIService{

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Override
    public String getChatResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String requestBody = "{\"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

}
