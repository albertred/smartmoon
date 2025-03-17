package com.inc1440068.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inc1440068.app.service.FilesService;
import com.inc1440068.app.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QnaController.class)
public class QnaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenAiService openAiService;

    @MockBean
    private FilesService filesService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testEndpoint_ShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void getAnswer_WithoutFiles_ShouldReturnAnswer() throws Exception {
        // Prepare test data
        Map<String, Object> payload = new HashMap<>();
        payload.put("question", "test question");
        payload.put("conversationId", "test-conv-id");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("answer", "test answer");

        // Mock service response
        when(openAiService.getAnswer(anyString(), anyString())).thenReturn(expectedResponse);

        // Perform test
        mockMvc.perform(post("/api/qna")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void getAnswer_WithFiles_ShouldReturnAnswer() throws Exception {
        // Prepare test data
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("filename", "test.txt");
        fileData.put("content", "test content");

        List<Map<String, Object>> files = new ArrayList<>();
        files.add(fileData);

        Map<String, Object> payload = new HashMap<>();
        payload.put("question", "test question");
        payload.put("conversationId", "test-conv-id");
        payload.put("files", files);

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("answer", "test answer with files");

        // Mock service responses
        when(filesService.generateAdditionalPrompt(any())).thenReturn("additional prompt");
        when(openAiService.getAnswer(anyString(), anyString())).thenReturn(expectedResponse);

        // Perform test
        mockMvc.perform(post("/api/qna")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void getAnswer_WithInvalidPayload_ShouldReturnBadRequest() throws Exception {
        // Prepare invalid test data
        Map<String, Object> payload = new HashMap<>();
        // Missing required fields

        // Perform test
        mockMvc.perform(post("/api/qna")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
} 