package com.ntm.doculearn.controller;

import com.ntm.doculearn.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/upload")

    public String handleUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();


        // Lưu tạm file
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        File tempFile = File.createTempFile("temp_ update", ".pdf");
        file.transferTo(tempFile);// chuyển file PDF vừa upload vào file tạm

        String content = openAiService.extractTextFromPDF(tempFile);
        System.out.println("PDF Content length: " + content.length());
        System.out.println("PDF Preview: " + content.substring(0, Math.min(300, content.length())));
        String prompt = openAiService.buildPrompt(content);
        String result = openAiService.callOpenAI(prompt);


        System.out.println("Prompt:\n" + prompt);
        System.out.println("================RESULT=====================\n" + result);

        openAiService.generateHTMLFiles(result, fileId);

        // Trả về link
        String baseUrl = "/generated/";
        return """
        {
          "summary": "%s%s_summary.html",
          "questions": "%s%s_questions.html"
        }
        """.formatted(baseUrl, fileId, baseUrl, fileId);
    }
}
