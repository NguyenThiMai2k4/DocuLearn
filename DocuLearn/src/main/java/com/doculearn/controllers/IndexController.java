package com.doculearn.controllers;

import com.doculearn.pojo.Course;
import com.doculearn.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller

public class IndexController {
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
        return "index";
    }


}
