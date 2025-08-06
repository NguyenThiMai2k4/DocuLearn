package com.doculearn.controllers;

import com.doculearn.enums.Status;
import com.doculearn.pojo.Course;
import com.doculearn.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("/")
    public String courseView(Model model) {
        List<Course> courses= this.courseService.getAllCourses();

        if(courses.isEmpty()){
            System.out.println("ERROR QUERY COURSES");
        }
        else
            System.out.println("==================== courses: \n"+courses);
        model.addAttribute("courses",courses);
        return "course";
    }

    //Mapping den createCourse.html
    @GetMapping("/course/add")
    public String createCourse(Model model) {
        model.addAttribute("status", Status.values());
        model.addAttribute("course", new Course());
        return "createCourse";
    }

    // ============= ADD Course =================
    @PostMapping("/add-course")
    public String addCourse(@ModelAttribute("course") Course course) {
        this.courseService.createOrUpdateCourse(course);
        return "redirect:/";
    }

    // ============= UPDATE Course =================
    @GetMapping("/course/edit/{courseId}")
    public String createCourse(@PathVariable("courseId") int courseId, Model model) {
        model.addAttribute("status", Status.values());
        model.addAttribute("course", this.courseService.getCourseById(courseId));
        return "createCourse";
    }

    // ============= Delete Course =================
    @GetMapping("/course/delete")
    public String deleteCourse(@RequestParam("id") int courseId) {
        this.courseService.deleteCourse(courseId);
        return "redirect:/";
    }




}
