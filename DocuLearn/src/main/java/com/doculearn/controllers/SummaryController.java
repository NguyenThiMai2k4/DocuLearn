package com.doculearn.controllers;

import com.doculearn.enums.Status;
import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;
import com.doculearn.service.CourseService;
import com.doculearn.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/course/{id}/detail")
public class SummaryController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private SummaryService summaryService;

    @GetMapping("")
    public String summaryView(@PathVariable("id") int courseId, Model model) {
        List<Summary> summaries = this.summaryService.getAllSummaryByCourseId(courseId);

        if(summaries.isEmpty()){
            System.out.println("ERROR QUERY COURSES");
        }
        else
            System.out.println("==================== summaries: \n"+summaries);

        System.out.println("==================== courseId: \n"+courseId);
        model.addAttribute("courseId", courseId); // use in base.html
        model.addAttribute("summaries",summaries);
        return "summary";
    }

    //Mapping den createCourse.html
    @GetMapping("/summary/add")
    public String createSummary(Model model) {

        model.addAttribute("summary", new Summary());
        return "create_summary";
    }

    // ============= ADD Course =================
    @PostMapping("/add-summary")
    public String addSummary(@ModelAttribute("summary") Summary summary) {
        this.summaryService.createSummary(summary);
        return "redirect:/";
    }

    // ============= UPDATE Course =================
    @GetMapping("/course/edit/{courseId}")
    public String updateSummary(@PathVariable("courseId") int courseId, Model model) {
        model.addAttribute("status", Status.values());
        model.addAttribute("course", this.courseService.getCourseById(courseId));
        return "create_summary";
    }

    // ============= Delete Course =================
    @GetMapping("/course/delete")
    public String deleteCourse(@RequestParam("id") int courseId) {
        this.courseService.deleteCourse(courseId);
        return "redirect:/";
    }



}
