package com.doculearn.controllers;

import com.doculearn.enums.Status;
import com.doculearn.pojo.*;
import com.doculearn.service.CourseService;
import com.doculearn.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/course/{id}")
public class SummaryController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private SummaryService summaryService;


    @GetMapping("/detail")
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

    //Mapping den createSummary.html
    @GetMapping("/summary/add")
    public String createSummary(@PathVariable("id") int courseId, Model model) {
        DataFileDTO data= new DataFileDTO();
        // Khởi tạo SummaryDTO bên trong
        SummaryDTO summaryDTO = new SummaryDTO();
        summaryDTO.setSections(new ArrayList<>());

        // Thêm 1 Section rỗng để hiển thị trong form
        summaryDTO.getSections().add(new SectionDTO());
        data.setSummary(summaryDTO);
        model.addAttribute("summary", new Summary());
        model.addAttribute("courseId", courseId);
        model.addAttribute("dataFile",data);
        model.addAttribute("status", Status.values());
        return "createSummary";
    }

    // ============= ADD Summary =================
    @PostMapping("/add-summary")
    public String addSummary(@ModelAttribute("summary") Summary summary,
                             @PathVariable("id") int courseId) {
        System.out.println("course id : "+courseId);
        summary.setCourse(this.courseService.getCourseById(courseId));
        this.summaryService.createSummary(summary);
        return "redirect:/course/" + courseId + "/upload-file/details";
    }

    // ============= UPDATE Summary =================
    @GetMapping("/summary/{summaryId}/edit")
    public String updateSummary(@PathVariable("summaryId") int summaryId,
                                @PathVariable("id") int courseId, Model model) {
        model.addAttribute("status", Status.values());
        model.addAttribute("courseId", courseId);
        model.addAttribute("summary", this.summaryService.getSummaryById(summaryId));
        return "createSummary";
    }



    // ============= Delete summary =================
    @GetMapping("/summary/delete")
    public String deleteSummary(@RequestParam("id") int summaryId, @PathVariable("id") int courseId) {
        this.summaryService.deleteSummary(summaryId);
        return "redirect:/course/" + courseId + "/detail";
    }



}
