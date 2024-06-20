package com.inc1440068.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAiService {
    private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Deque<Map<String, String>>> conversationHistories = new HashMap<>();
    private static final int MESSAGE_LIMIT = 10;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public Object getAnswer(String conversationId, String question) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Deque<Map<String, String>> conversationHistory = conversationHistories.computeIfAbsent(conversationId, k -> new LinkedList<>());
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", question);
        conversationHistory.add(userMessage);

        while (conversationHistory.size() > MESSAGE_LIMIT) {
            conversationHistory.pollFirst();
        }

        // Prepare request body
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4o");
        request.put("messages", new ArrayList<>(conversationHistory));
        request.put("max_tokens", 2000);
        request.put("temperature", 0.1);

        // Create HttpEntity with the headers and request body
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        String content = "";
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode choices = root.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode messageNode = choices.get(0).path("message");
                content = messageNode.path("content").asText();

                Map<String, String> assistantMessage = new HashMap<>();
                assistantMessage.put("role", "assistant");
                assistantMessage.put("content", content);
                conversationHistory.add(assistantMessage);

                // Trim conversation history to the last MESSAGE_LIMIT messages
                while (conversationHistory.size() > MESSAGE_LIMIT) {
                    conversationHistory.pollFirst();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        Map<String, String> rtn = new HashMap<>();
        rtn.put("answer", content);

        return rtn;
    }
}
