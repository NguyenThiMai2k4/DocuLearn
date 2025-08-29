package com.doculearn.controllers;

import com.doculearn.pojo.Question;
import com.doculearn.pojo.QuestionOption;
import com.doculearn.pojo.SectionDTO;
import com.doculearn.pojo.Summary;
import com.doculearn.service.QuestionService;
import com.doculearn.service.SummaryService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class PdfExportController {

    private final QuestionService questionService;
    private final SummaryService summaryService;

    public PdfExportController(QuestionService questionService, SummaryService summaryService) {
        this.questionService = questionService;
        this.summaryService = summaryService;
    }

    @GetMapping("/course/{courseId}/download-pdf")
    public void downloadPdf(@PathVariable int courseId,
                            HttpSession session,
                            HttpServletResponse response) throws IOException {


        // === Lấy summaryId từ session (giống trong viewDetail) ===
        Object summaryObj = session.getAttribute("summaryId");
        if (summaryObj == null) {
            throw new RuntimeException("summaryId chưa có trong session");
        }
        int summaryId = (int) summaryObj;

        // === Lấy dữ liệu giống view detail ===
        Summary summary = summaryService.getSummaryById(summaryId);
        List<Question> questions = questionService.findByCourse_IdAndSummary_Id(courseId, summaryId);

        // === Tao ten file
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=tong_quan_kien_thuc" + summary.getTitle() + ".pdf";
        response.setHeader(headerKey, headerValue);


        // === Tạo PDF ===
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        document.open();

        // Nap font unicode
        InputStream fontStream = getClass().getResourceAsStream("/fonts/dejavu-sans/DejaVuSans.ttf");
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, fontStream.readAllBytes(), null);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Font titleFont = new Font(bf, 18, Font.BOLD);
        Font chapterFont = new Font(bf, 17, Font.BOLD);
        Font sectionFont = new Font(bf, 14, Font.BOLD);
        Font textFont = new Font(bf, 12, Font.NORMAL);
        Font qFont = new Font(bf, 12, Font.BOLD);

        // Tiêu đề PDF
        Paragraph title = new Paragraph(summary.getTitle(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        Paragraph chapterSummary = new Paragraph("Summary", chapterFont);
        chapterSummary.setAlignment(Element.ALIGN_CENTER);

        Paragraph chapterQo = new Paragraph("Summary", chapterFont);
        chapterQo.setAlignment(Element.ALIGN_CENTER);

        try {
            document.add(title);
            document.add(new Paragraph("\n"));
            document.add(chapterSummary);
            document.add(new Paragraph("\n"));

            // ==== SESSION
            if (summary != null) {
                if (summary.getSections() != null && !summary.getSections().isEmpty()) {
                    for (SectionDTO section : summary.getSections()) {
                        // In tiêu đề Section (nếu có)
                        if (section.getHeading() != null) {
                            Paragraph heading = new Paragraph(section.getHeading(), sectionFont);
                            heading.setSpacingBefore(10);
                            heading.setSpacingAfter(5);
                            document.add(heading);
                        }

                        // In nội dung Section (nếu có)
                        if (section.getContent() != null) {
                            Paragraph content = new Paragraph(section.getContent(), textFont);
                            content.setLeading(16); // chỉnh khoảng cách dòng
                            content.setSpacingAfter(10); // cách đoạn dưới
                            document.add(content);
                        }
                    }
                }
            } else {
                document.add(new Paragraph("Không có dữ liệu Summary.", textFont));
            }
            document.add(new Paragraph("\n"));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        //== Danh sách Câu hỏi & Option
        try {
            document.add(chapterQo);
            int idx = 1;
            for (Question q : questions) {
                // Câu hỏi
                String qText = (q.getContent() == null) ? "(trống)" : q.getContent();
                document.add(new Paragraph(qText, qFont));

                // Option
                if (q.getQuestionOptions() != null && !q.getQuestionOptions().isEmpty()) {
                    int index = 0;
                    for (QuestionOption opt : q.getQuestionOptions()) {
                        String oText = (opt.getContent() == null) ? "(trống)" : opt.getContent();
                        char optionLabel = (char) ('A' + index); // A, B, C, D,...
                        document.add(new Paragraph("   " + optionLabel + ". " + oText, textFont));
                        index++;
                    }
                } else {
                    document.add(new Paragraph("   - (Đáp án tự luận ...)", textFont));
                }
                document.add(new Paragraph("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        document.close();
    }
}
