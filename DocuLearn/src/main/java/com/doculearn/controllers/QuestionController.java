package com.doculearn.controllers;

import com.doculearn.pojo.Question;
import com.doculearn.pojo.Summary;
import com.doculearn.service.CourseService;
import com.doculearn.service.OptionService;
import com.doculearn.service.QuestionService;
import com.doculearn.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/course/{id}/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private OptionService optionService;

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

}
