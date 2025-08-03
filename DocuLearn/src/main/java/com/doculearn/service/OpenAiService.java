package com.doculearn.service;


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

        StringBuilder response = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) response.append(line);
            System.err.println("RESPONSE: " + response);

        }

        JSONObject responseObject = new JSONObject(response.toString());
        JSONArray choices = responseObject.getJSONArray("choices");
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        String content = message.getString("content");
        return content;
    }

    public String buildPrompt(String content) {
        return """
                Hãy thực hiện các nhiệm vụ sau với nội dung được cung cấp, và **tuân thủ đúng định dạng đầu ra**:
                
                          📌 Tóm tắt nội dung chính:
                          - Bắt đầu bằng tiêu đề chương duy nhất ở dòng đầu: ví dụ **"Chương 1: Nhập môn ..."**
                          - Sau đó chia nội dung thành các phần nhỏ: **"Phần 1: Tên phần"**, rồi xuống dòng ghi nội dung tóm tắt phần đó (5–10 dòng).
                          - Lặp lại cho các phần tiếp theo: **"Phần 2: ..."**, v.v.
                          - Nội dung cần rõ ràng, dễ hiểu và có tính hệ thống.
                
                          ❓ Câu hỏi trắc nghiệm:
                          - Tạo 5 câu hỏi trắc nghiệm có 4 đáp án (A, B, C, D), mỗi câu 1 dòng.
                
                          🖋️ Câu hỏi tự luận:
                          - Tạo 2 câu hỏi tự luận yêu cầu phân tích nội dung.
                
                          Lưu ý: Giữ đúng các tiêu đề “📌 Tóm tắt:”, “❓ Câu hỏi trắc nghiệm:”, “🖋️ Câu hỏi tự luận:”
                
                          Nội dung:
                          %s
        """.formatted(content.length() > 3000 ? content.substring(0, 3000) + "..." : content);
    }

    public void generateJsonFiles(String resultText, String fileId) throws IOException {
        String summary  = extractSection(resultText, "📌 Tóm tắt:", "❓ Câu hỏi trắc nghiệm:");
        String questionsPart  = extractSectionToEnd(resultText, "❓ Câu hỏi trắc nghiệm:");
        String essaysPart = extractSectionToEnd(resultText, "🖋️ Câu hỏi tự luận:");
        JSONObject json = new JSONObject();

        JSONObject summaryObj = new JSONObject();

        String[] lines = summary.split("\\r?\\n");
        String title = "";
        JSONArray sections = new JSONArray();

        String currentHeading = null;
        StringBuilder currentContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("Chương")) {
                title = line;
            } else if (line.matches("^\\*{2}Phần\\s+\\d+:.*\\*{2}")) {
                // Nếu đã có section trước -> đẩy vào mảng
                if (currentHeading != null) {
                    JSONObject section = new JSONObject();
                    section.put("heading", currentHeading);
                    section.put("content", currentContent.toString().trim());
                    sections.put(section);
                }

                // Bắt đầu phần mới
                currentHeading = line.replace("**", "").trim();
                currentContent.setLength(0);
            } else {
                currentContent.append(line).append(" ");
            }
        }

        // Thêm phần cuối cùng
        if (currentHeading != null) {
            JSONObject section = new JSONObject();
            section.put("heading", currentHeading);
            section.put("content", currentContent.toString().trim());
            sections.put(section);
        }

        summaryObj.put("title", title);
        summaryObj.put("sections", sections);

        json.put("summary", summaryObj);

        // ❓ Trắc nghiệm
        JSONArray mcQuestions = new JSONArray();
        String[] linesQuestion = questionsPart.split("\\r?\\n");
        for (String line : linesQuestion) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("A\\.|B\\.|C\\.|D\\.");
            if (parts.length < 5) continue;

            JSONObject questionObj = new JSONObject();
            questionObj.put("question", parts[0].trim());

            JSONObject options = new JSONObject();
            options.put("A", parts[1].trim());
            options.put("B", parts[2].trim());
            options.put("C", parts[3].trim());
            options.put("D", parts[4].trim());

            questionObj.put("options", options);
            mcQuestions.put(questionObj);
        }
        json.put("multipleChoice", mcQuestions);

        // 🖋️ Tự luận
        JSONArray essayQuestions = new JSONArray();
        String[] essayLines = essaysPart.split("\\r?\\n");
        for (String line : essayLines) {
            if (!line.trim().isEmpty()) {
                essayQuestions.put(line.trim());
            }
        }
        json.put("essayQuestions", essayQuestions);

        writeJsonFile(fileId + ".json", json);
    }



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

//    private void writeHtmlFile(String fileName, String title, String content) throws IOException {
//        String html = """
//        <html><head><meta charset="UTF-8"><title>%s</title></head>
//        <body><h2>%s</h2><pre>%s</pre></body></html>
//        """.formatted(title, title, content);
//
//        try (FileWriter fw = new FileWriter(OUTPUT_FOLDER + fileName)) {
//            fw.write(html);
//        }
//    }

    private void writeJsonFile(String fileName, JSONObject jsonObject) throws IOException {
        try (FileWriter fw = new FileWriter(OUTPUT_FOLDER + fileName)) {
            fw.write(jsonObject.toString(2)); // Pretty print JSON
        }
    }

}
