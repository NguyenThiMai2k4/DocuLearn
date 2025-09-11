package com.doculearn.controllers;

import com.doculearn.service.FileService;
import com.doculearn.service.OpenAiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@RestController
@RequestMapping("/course/{id}")
public class FileController {
    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private FileService fileService;

    @PostMapping("/add-file")
    public ResponseEntity<?> handleUpload(@PathVariable ("id") int courseId, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File không được để trống"));
        }

        // Chỉ cho phép PDF
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!originalFilename.toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Chỉ hỗ trợ file PDF"));
        }

        String fileId = UUID.randomUUID().toString();

        // Lưu file tạm
        File tempFile = File.createTempFile("temp_", ".pdf");
        file.transferTo(tempFile);


        String content = openAiService.extractTextFromPDF(tempFile);
        String prompt = openAiService.buildPrompt(content);
        String result = openAiService.callOpenAI(prompt);
        openAiService.generateJsonFiles(result, fileId);
        System.out.println("================== RESULT GENNERATED: \n"+result);

        Path generatedFilePath = Paths.get("src/main/resources/static/generated", fileId + ".json");

        //Path generatedFilePath = Paths.get("src/main/resources/static/generated/3afc7b52-9d5d-4dea-979d-d2dc12d3bf32.json");


        int summaryId = fileService.importFromJson(generatedFilePath,courseId);

        session.setAttribute("summaryId", summaryId);
        // Trả kết quả JSON
        String baseUrl = "/generated/";
        return ResponseEntity.ok(Map.of(
                "json", baseUrl + fileId + ".json",
                "fileName", originalFilename,
                "summaryId", summaryId
//                "contentLength", result.length()
        ));


    }

}

