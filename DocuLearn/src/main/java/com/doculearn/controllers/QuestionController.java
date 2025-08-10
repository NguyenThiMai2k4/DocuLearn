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

        // Thêm 1 Section rỗng để hiển thị trong form
        QuestionDTO dto = new QuestionDTO();
        dto.getOptions().add(new QuestionOption());

        model.addAttribute("questionDTO",  dto);
        model.addAttribute("courseId", courseId);
        model.addAttribute("summaries", this.courseService.getAllSummaryByCourseId(courseId));
        return "createQuestionOption";
    }

    // ============= ADD Summary =================
    @PostMapping("/add")
    public String addQuestion(@ModelAttribute("questionDTO") QuestionDTO dto,
                             @PathVariable("id") int courseId) {
        Question question = new Question();
        question.setContent(dto.getContent());

        Set<QuestionOption> optionSet = new LinkedHashSet<>();
        for (QuestionOption opt : dto.getOptions()) {
            opt.setQuestion(question); // gán quan hệ 2 chiều
            optionSet.add(opt);
        }
        question.setQuestionOptions(optionSet);
        question.setCourse(this.courseService.getCourseById(courseId));
        questionService.createOrUpdateQuestion(question);

        return "redirect:/course/" + courseId + "/question-option";
    }

    // ============= UPDATE Summary =================
//    @GetMapping("/summary/{summaryId}/edit")
//    public String updateSummary(@PathVariable("summaryId") int summaryId,
//                                @PathVariable("id") int courseId, Model model) {
//        model.addAttribute("status", Status.values());
//        model.addAttribute("courseId", courseId);
//        model.addAttribute("summary", this.summaryService.getSummaryById(summaryId));
//        return "createSummary";
//    }
//
//    // ============= Delete Course =================
//    @GetMapping("/summary/delete")
//    public String deleteSummary(@RequestParam("id") int summaryId, @PathVariable("id") int courseId) {
//        this.summaryService.deleteSummary(summaryId);
//        return "redirect:/course/" + courseId + "/detail";
//    }


}
