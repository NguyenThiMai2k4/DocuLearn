package com.doculearn.service;


import com.doculearn.enums.ResponseType;
import com.doculearn.pojo.*;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.repositories.OptionRepository;
import com.doculearn.repositories.QuestionRepository;
import com.doculearn.repositories.SummaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@RequiredArgsConstructor
@Service
// Lưu tất cả thuộc tính trong json vào bảng tương ứng
public class FileService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private final ObjectMapper objectMapper;

    private SummaryDTO summaryDTO;

    public void importFromJson(Path filePath, Integer courseId) throws IOException {
        DataFileDTO data = objectMapper.readValue(filePath.toFile(), DataFileDTO.class);
        System.out.println("Nội dung JSON vừa tạo: " +data.toString());



        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<SectionDTO> sections= data.getSummary().getSections();
//        // Lưu summary
        Summary summary = new Summary();
        summary.setTitle(data.getSummary().getTitle());
        summary.setCourse(course);
        summary.setSections(sections);
        summary.setStatus("PUBLISH");
        this.summaryRepository.save(summary);

//        // Lưu câu hỏi trắc nghiệm
        for (MultipleChoiceQuestionDTO mc : data.getMultipleChoice()) {
            Question question = new Question();
            question.setCourse(course);
            question.setSummary(summary);
            question.setContent(mc.getQuestion());
            question.setResponseType(ResponseType.SINGLE_CHOICE);
            this.questionRepository.save(question);

//            // options có thể là List hoặc Map
            if (mc.getOptions() instanceof List) {
                List<?> opts = (List<?>) mc.getOptions();
                for (Object opt : opts) {
                    QuestionOption qo = new QuestionOption();
                    qo.setQuestion(question);
                    qo.setContent(opt.toString().replaceFirst("^-?\\s*[A-D]\\.?\\s*", "").trim());
                    qo.setIsCorrect(false);
                    this.optionRepository.save(qo);
                }
            } else if (mc.getOptions() instanceof Map) {
                Map<?, ?> opts = (Map<?, ?>) mc.getOptions();
                for (Object val : opts.values()) {
                    QuestionOption qo = new QuestionOption();
                    qo.setQuestion(question);
                    qo.setContent(val.toString().replaceFirst("^-?\\s*[A-D]\\.?\\s*", "").trim());
                    qo.setIsCorrect(false);
                    this.optionRepository.save(qo);
                }
            }
        }
//
//        // Lưu câu hỏi tự luận
        if (data.getEssayQuestions() != null) {
            for (String essay : data.getEssayQuestions()) {
                if (essay.trim().isEmpty() || essay.startsWith("🖋️")) continue;
                Question question = new Question();
                question.setCourse(course);
                question.setSummary(summary);
                question.setContent(essay);
                question.setResponseType(ResponseType.TEXT);
                this.questionRepository.save(question);
            }
        }
      }

}
