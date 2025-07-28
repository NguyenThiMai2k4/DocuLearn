package com.ntm.doculearn.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testDemo {
    @GetMapping("/")
    public String hello(Model model) {
        return "uploadFile";
    }
}
