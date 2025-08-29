package com.doculearn.controllers;

import com.doculearn.pojo.OptionForm;
import com.doculearn.pojo.Question;
import com.doculearn.pojo.QuestionOption;
import com.doculearn.service.FileService;
import com.doculearn.service.OptionService;
import com.doculearn.service.QuestionService;
import com.doculearn.service.SummaryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/course/{id}/upload-file")
public class FileViewController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private OptionService optionService;


    @GetMapping("")
    public String viewFile(@PathVariable("id") int courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "uploadFile";
    }

    //view link summary & form question
    @GetMapping("/details")
    public String viewDetail(@PathVariable("id") int courseId, HttpSession session, Model model) {

        Object summaryObj = session.getAttribute("summaryId");
        if (summaryObj == null) {
            throw new RuntimeException("summaryId chưa có trong session");
        }
        int summaryId = (int) summaryObj;
        List<Question> questions = this.questionService.findByCourse_IdAndSummary_Id(courseId, summaryId);

        if (questions.isEmpty()) {
            System.out.println("ERROR QUERY Questions");
        } else {
            System.out.println("==================== questions: \n" + questions);
        }
        OptionForm optionForm=  new OptionForm();
        optionForm.setOptions(this.optionService.getAllOptions());
        System.out.println("==================== options: \n" + optionForm);

        model.addAttribute("summaryId", summaryId);
        model.addAttribute("summary", this.summaryService.getSummaryById(summaryId));
        model.addAttribute("courseId", courseId);
        model.addAttribute("questions", questions);
//        model.addAttribute("options", this.optionService.getAllOptions());
        model.addAttribute("optionForm", optionForm);

        return "editContentFile";
    }

    @PostMapping("/details")
    public String editDetail(@PathVariable("id") int courseId,
                             @ModelAttribute("optionForm") OptionForm optionForm) {
        System.out.println("==================== optionFormPost: \n" + optionForm.getOptions());
//
        // Nhuoc diem truy van nhieu hon (lay tat ca option roi fill)
        List<QuestionOption> validOptions = optionForm.getOptions()
                .stream()
                .filter(opt -> opt.getId() != null) // loại null
                .collect(Collectors.toList());

        for (QuestionOption opt : validOptions) {
            optionService.createOrUpdateOption(opt);
        }

        return "redirect:/course/" + courseId + "/upload-file/details";
    }

}