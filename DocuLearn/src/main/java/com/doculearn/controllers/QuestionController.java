package com.doculearn.controllers;

import com.doculearn.enums.Status;
import com.doculearn.pojo.*;
import com.doculearn.service.CourseService;
import com.doculearn.service.OptionService;
import com.doculearn.service.QuestionService;
import com.doculearn.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.doculearn.enums.ResponseType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/course/{id}/question-option")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private CourseService courseService;

    @GetMapping("")
    public String QuestionView(Model model, @PathVariable("id") int courseId){
        List<Question> questions = this.questionService.getAllQuestionsByCourseId(courseId);

        if (questions.isEmpty()) {
            System.out.println("ERROR QUERY Questions");
        } else {
            System.out.println("==================== questions: \n" + questions);
        }

        model.addAttribute("courseId", courseId);
        model.addAttribute("questions", questions);
        return "question"; // trỏ tới file question.html
    }


    @GetMapping("/add")
    public String createSummary(@PathVariable("id") int courseId, Model model) {

        QuestionListWrapper wrapper = new QuestionListWrapper();
        QuestionDTO dto = new QuestionDTO();
        dto.getOptions().add(new QuestionOption());
        wrapper.getQuestions().add(dto);


        model.addAttribute("responseType", ResponseType.values());
        model.addAttribute("questionWrapper", wrapper);
        model.addAttribute("courseId", courseId);
        model.addAttribute("summaries", courseService.getAllSummaryByCourseId(courseId));
        return "createQuestionOption";
    }

    // ============= ADD question =================
    @PostMapping("/add")
    public String addQuestion(@ModelAttribute("questionWrapper") QuestionListWrapper wrapper,
                              @PathVariable("id") int courseId) {
        for (QuestionDTO dto : wrapper.getQuestions()) {
            System.out.println("OPTIONS SIZE = " + dto.getOptions().size());
            Question question = new Question();
            question.setContent(dto.getContent());
            question.setResponseType(ResponseType.valueOf(dto.getResponseType()));
            question.setCourse(courseService.getCourseById(courseId));

            Set<QuestionOption> optionSet = new LinkedHashSet<>();
            for (QuestionOption opt : dto.getOptions()) {
                opt.setQuestion(question);
                optionSet.add(opt);

            }
            System.out.println("OptionSet = " + optionSet);
            question.setQuestionOptions(optionSet);
            this.questionService.createOrUpdateQuestion(question);

        }
        return "redirect:/course/" + courseId + "/question-option";
    }
//
//     ============= UPDATE question - option =================
@GetMapping("/{qoId}/edit")
public String updateQo(@PathVariable("qoId") int qoId,
                       @PathVariable("id") int courseId,
                       Model model) {

    Question question = questionService.getQuestionById(qoId);

    // Chuyển Question -> DTO
    QuestionDTO dto = new QuestionDTO();
    dto.setContent(question.getContent());
    dto.setResponseType(question.getResponseType().name());
    dto.setOptions(new ArrayList<>(question.getQuestionOptions())); // copy list option

    QuestionListWrapper wrapper = new QuestionListWrapper();
    wrapper.getQuestions().add(dto);

    model.addAttribute("responseType", ResponseType.values());
    model.addAttribute("questionWrapper", wrapper);
    model.addAttribute("courseId", courseId);
    model.addAttribute("summaries", courseService.getAllSummaryByCourseId(courseId));
    model.addAttribute("questionId", qoId);

    return "createQuestionOption"; // vẫn dùng template này
}

    // ============= Delete question - option =================
    @GetMapping("/delete")
    public String deleteSummary(@RequestParam("questionId") int questionId, @PathVariable("id") int courseId) {
        this.questionService.deleteQuestion(questionId);
        return "redirect:/course/" + courseId + "/question-option";
    }


}
