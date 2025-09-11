package com.doculearn.controllers.API;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AIBoxController {
    @PostMapping("/ask-ai")
    public Map<String, String> askAI(@RequestBody Map<String, String> body) {
        String question = body.get("question");

        // TODO: Gọi Gemini API thật (qua HttpClient hoặc WebClient)
        // Ví dụ giả lập trả lời
        String fakeAnswer = "Mình là Gemini 🤖. Bạn vừa hỏi: " + question;

        return Map.of("answer", fakeAnswer);
    }
}
