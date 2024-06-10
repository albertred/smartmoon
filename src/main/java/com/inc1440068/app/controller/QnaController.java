package com.inc1440068.app.controller;

import com.inc1440068.app.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class QnaController {
    private static final Logger logger = LoggerFactory.getLogger(QnaController.class);

    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping("/qna")
    public Object getAnswer(@RequestBody Map<String, String> payload) {
        logger.info("Received request with payload: {}", payload);
        String question = payload.get("question");
        return openAiService.getAnswer(question);
    }
}
