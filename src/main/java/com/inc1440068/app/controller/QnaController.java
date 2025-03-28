package com.inc1440068.app.controller;

import com.inc1440068.app.model.FileInfo;
import com.inc1440068.app.service.FilesService;
import com.inc1440068.app.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class QnaController {
    private static final Logger logger = LoggerFactory.getLogger(QnaController.class);

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private FilesService filesService;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping("/qna")
    public ResponseEntity<Object> getAnswer(@RequestBody Map<String, Object> payload) throws IOException {
        logger.info("Received request with payload: {}", payload);
        
        // Validate required fields
        if (!payload.containsKey("question")) {
            return ResponseEntity.badRequest().body("Missing required field: question");
        }

        String question = (String) payload.get("question");
        String conversationId = (String) payload.get("conversationId");

        if (payload.get("files") != null) {
            List<FileInfo> files = ((List<Map>) payload.get("files"))
                    .stream()
                    .map(e -> new FileInfo()
                            .setFilename((String) e.get("filename"))
                            .setContent((String) e.get("content")))
                    .collect(Collectors.toList());

            if (!files.isEmpty()) {
                String additionalPrompt = filesService.generateAdditionalPrompt(files);
                question += "\nFYI - See below attachment(s):\n" + additionalPrompt;
            }
        }

        return ResponseEntity.ok(openAiService.getAnswer(conversationId, question));
    }
}
