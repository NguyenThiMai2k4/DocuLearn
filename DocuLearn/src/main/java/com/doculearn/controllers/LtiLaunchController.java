package com.doculearn.controllers;

import com.doculearn.pojo.Course;
import com.doculearn.service.CourseService;
import com.doculearn.service.OpenAiService;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.nimbusds.jwt.JWTClaimsSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//Nhận ID Token (JWT) từ LMS → verify → hiển thị giao diện.

@Controller
@RequestMapping("/lti")
public class LtiLaunchController {


    @Autowired
    private CourseService courseService;

    @GetMapping("/course/{id}/launch")
    public String launchCourse(@PathVariable int id, Model model) {
        model.addAttribute("courseId", id);
        return "uploadFile"; // view
    }

    @PostMapping("/launch")
    public String launch(@RequestParam("id_token") String idToken, Model model) throws Exception {

        // 1. Lấy public key từ LMS JWKS endpoint
        // 2. Verify JWT (issuer, audience, signature, exp...)
        // 3. Parse claim lti_deployment_id, lti_context_id, sub, roles...
        System.out.println("idToken: " + idToken);
        JWTClaimsSet claims = JWTParser.parse(idToken).getJWTClaimsSet();
        System.out.println(claims.toJSONObject());


        //Lấy Title course của moodle
        Map<String, Object> context = (Map<String, Object>) claims.getClaim("https://purl.imsglobal.org/spec/lti/claim/context");
        String courseTitle = (String) context.get("title");
        System.out.println("courseTitle: " + courseTitle);

        // title là độc lập không trùng nhau
        Course course = courseService.createIfNotExists(courseTitle);
        System.out.println("courseTitle: " + course.getTitle());
        System.out.println("courseId: " + course.getId());

        //lấy courseId cho FileCoutroller PathValidate courseId
        model.addAttribute("courseId", course.getId());
        return "uploadFile"; // view Thymeleaf
    }



}

