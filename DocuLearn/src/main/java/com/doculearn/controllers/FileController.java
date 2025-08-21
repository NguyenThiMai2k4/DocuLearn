package com.doculearn.controllers;

import com.doculearn.service.FileService;
import com.doculearn.service.OpenAiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        Path generatedFilePath = Paths.get("src/main/resources/static/generated", fileId + ".json");

        fileService.importFromJson(generatedFilePath,courseId);

        // Trả kết quả JSON
        String baseUrl = "/generated/";
        return ResponseEntity.ok(Map.of(
                "json", baseUrl + fileId + ".json",
                "fileName", originalFilename,
                "contentLength", result.length()
        ));


    }

}

//        String result = """
//        {
//          "summary": {
//            "title": "**Chương 1: Nhập môn React Native**",
//            "sections": [
//              {
//                "heading": "Phần 1: Giới thiệu về React Native",
//                "content": "React Native là một framework JavaScript dùng để phát triển ứng dụng di động cho iOS và Android. Nó cho phép sử dụng các native component thay vì web component, giúp mã nguồn viết trong React Native được biên dịch thành native code cho cả hai hệ điều hành. Framework này chỉ cần sử dụng JavaScript để phát triển ứng dụng, và có một cộng đồng sử dụng lớn."
//              },
//              {
//                "heading": "Phần 5: State",
//                "content": "State là đối tượng lưu giá trị các thuộc tính của component và chỉ có hiệu lực trong phạm vi component đó. State có thể thay đổi bất cứ khi nào bằng phương thức this.setState()."
//              },
//              {
//                "heading": "Phần 6: Vòng đời React Component",
//                "content": "Mỗi component có 3 giai đoạn chính: Mounting (lúc đưa component vào DOM), Updating (khi props hoặc state thay đổi), và Unmounting (khi component được gỡ bỏ khỏi DOM)."
//              }
//            ]
//          },
//          "multipleChoice": [
//            {
//              "question": "1. React Native là gì?",
//              "options": [
//                "Một ngôn ngữ lập trình mới",
//                "Một framework JavaScript cho phát triển ứng dụng di động",
//                "Một công cụ phát triển web",
//                "Một hệ điều hành mới"
//              ]
//            },
//             {
//              "question": "4. Khi state thay đổi trong một component, điều gì xảy ra?",
//              "options": [
//                "Component sẽ bị xóa",
//                "Component sẽ không thay đổi",
//                "Component sẽ nạp lại (re-render)",
//                "Không có gì xảy ra"
//              ]
//            }
//          ],
//          "essayQuestions": [
//            "🖋️ Câu hỏi tự luận:",
//            "1. Hãy phân tích vai trò của Props và State trong React Native và cách chúng ảnh hưởng đến việc xây dựng giao diện người dùng.",
//            "2. Trình bày và phân tích các giai đoạn trong vòng đời của một React component, giải thích tại sao việc hiểu rõ các giai đoạn này lại quan trọng trong phát triển ứng dụng."
//          ]
//        }
//        """;
