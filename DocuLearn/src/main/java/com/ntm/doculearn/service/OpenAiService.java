package com.ntm.doculearn.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAiService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    private final String OUTPUT_FOLDER = "src/main/resources/static/generated/";

    public String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            return new PDFTextStripper().getText(document);
        }
    }

    public String callOpenAI(String prompt) throws IOException {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o");

        JSONArray messages = new JSONArray();
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.put(userMsg);

        json.put("messages", messages);
        json.put("temperature", 0.7);
        json.put("max_tokens", 3000);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }


        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();


        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            System.err.println("RESPONSE: " + response); // in để debug
            return response.toString();
        }
    }

    public String buildPrompt(String content) {
        return """
                Hãy thực hiện các nhiệm vụ sau với nội dung được cung cấp, và **tuân thủ đúng định dạng đầu ra**:
                
                          📌 Tóm tắt:
                          - Viết tóm tắt nội dung chính (khoảng 200 từ), trình bày rõ ràng.
                
                          ❓ Câu hỏi trắc nghiệm:
                          - Tạo 5 câu hỏi trắc nghiệm có 4 đáp án (A, B, C, D), mỗi câu 1 dòng.
                
                          🖋️ Câu hỏi tự luận:
                          - Tạo 2 câu hỏi tự luận yêu cầu phân tích nội dung.
                
                          Lưu ý: Giữ đúng các tiêu đề “📌 Tóm tắt:”, “❓ Câu hỏi trắc nghiệm:”, “🖋️ Câu hỏi tự luận:”
                
                          Nội dung:
                          %s
        """.formatted(content.length() > 3000 ? content.substring(0, 3000) + "..." : content);
    }

    public void generateHTMLFiles(String resultText, String fileId) throws IOException {
        String summary = extractSection(resultText, "📌 Tóm tắt:", "❓ Câu hỏi trắc nghiệm:");
        String questions = extractSectionToEnd(resultText, "❓ Câu hỏi trắc nghiệm:");

        writeHtmlFile(fileId + "_summary.html", "Tóm tắt", summary);
        writeHtmlFile(fileId + "_questions.html", "Câu hỏi", questions);
    }

//    private String extractSection(String text, String startPattern, String endPattern) {
//        return text.replaceAll(".*" + startPattern, "")
//                .replaceAll(endPattern + ".*", "").trim();
//    }

    private String extractSection(String text, String startPattern, String endPattern) {
        int startIndex = text.indexOf(startPattern);
        if (startIndex == -1) return "";

        int endIndex = endPattern.isEmpty() ? text.length() : text.indexOf(endPattern, startIndex);
        if (endIndex == -1) endIndex = text.length();

        return text.substring(startIndex, endIndex).trim();
    }

    private String extractSectionToEnd(String text, String startPattern) {
        int startIndex = text.indexOf(startPattern);
        if (startIndex == -1) return "";

        return text.substring(startIndex).trim();
    }

    private void writeHtmlFile(String fileName, String title, String content) throws IOException {
        String html = """
        <html><head><meta charset="UTF-8"><title>%s</title></head>
        <body><h2>%s</h2><pre>%s</pre></body></html>
        """.formatted(title, title, content);

        try (FileWriter fw = new FileWriter(OUTPUT_FOLDER + fileName)) {
            fw.write(html);
        }
    }
}
