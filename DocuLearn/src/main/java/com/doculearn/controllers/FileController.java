package com.doculearn.controllers;

import com.doculearn.service.FileService;
import com.doculearn.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/course/{id}")
public class FileController {
    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private FileService fileService;
    @PostMapping("/add-file")
    public ResponseEntity<?> handleUpload(@PathVariable ("id") int courseId, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File không được để trống"));
        }

        // Chỉ cho phép PDF
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (!originalFilename.toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Chỉ hỗ trợ file PDF"));
        }

        String fileId = UUID.randomUUID().toString();

        // Lưu file tạm
        File tempFile = File.createTempFile("temp_", ".pdf");
        file.transferTo(tempFile);

        // Gọi OpenAiService
        String content = openAiService.extractTextFromPDF(tempFile);
        String prompt = openAiService.buildPrompt(content);
        String result = openAiService.callOpenAI(prompt);
        openAiService.generateJsonFiles(result, fileId);

        System.out.println("================== RESULT GENNERATED: \n"+result);
        Path generatedFilePath = Paths.get("generated", fileId + ".json");
        fileService.importFromJson(generatedFilePath, courseId);

        // Trả kết quả JSON
        String baseUrl = "/generated/";
        return ResponseEntity.ok(Map.of(
                "json", baseUrl + fileId + ".json",
                "fileName", originalFilename,
                "contentLength", content.length()
        ));
    }


//    @PostMapping("/upload")
//
//    public String handleUpload(@RequestParam("file") MultipartFile file) throws IOException {
//        String fileId = UUID.randomUUID().toString();
//
//
//        // Lưu tạm file
//        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
//        File tempFile = File.createTempFile("temp_", ".pdf");
//        file.transferTo(tempFile);// chuyển file PDF vừa upload vào file tạm
//
//        String content = openAiService.extractTextFromPDF(tempFile);
//        System.out.println("PDF Content length: " + content.length());
//        System.out.println("PDF Preview: " + content.substring(0, Math.min(300, content.length())));
//        String prompt = openAiService.buildPrompt(content);
//        String result = openAiService.callOpenAI(prompt);
//
//
//        System.out.println("Prompt:\n" + prompt);
//        System.out.println("================RESULT=====================\n" + result);
//
//        openAiService.generateJsonFiles(result, fileId);
//
//        // Trả về link
//        String baseUrl = "/generated/";
//        return """
//        {
//          "json": "%s%s.json"
//        }
//        """.formatted(baseUrl, fileId, baseUrl, fileId);
//    }
}
