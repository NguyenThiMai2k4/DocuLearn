//package com.doculearn.controllers.API;
//
//
//import com.doculearn.enums.Status;
//import com.doculearn.pojo.Course;
//import com.doculearn.pojo.Summary;
//import com.doculearn.service.CourseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//
//@RequestMapping("/api/course")
//@RestController
//@CrossOrigin(origins = "http://localhost:3000") // cho phép truy cập nextjs
//public class CourseRestController {
//
//        @Autowired
//        private CourseService courseService;
//
//    @GetMapping("") //_ALL
//    @ResponseBody
//    @CrossOrigin
//    public ResponseEntity<List<Map<String, Object>>> getCourse(Principal principal, @RequestParam Map<String, String> params) {
//        return new ResponseEntity<>(this.courseService.getAllCourses(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Course> getSummaryById(@PathVariable(value = "id")int id) {
//        return courseService.getCourseById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//        //Mapping den createCourse.html
//        @GetMapping("/course/add")
//        public String createCourse(Model model) {
//            model.addAttribute("status", Status.values());
//            model.addAttribute("course", new Course());
//            return "createCourse";
//        }
//
//        // ============= ADD Course =================
//        @PostMapping("/add-course")
//        public String addCourse(@ModelAttribute("course") Course course) {
//            this.courseService.createOrUpdateCourse(course);
//            return "redirect:/";
//        }
//
//        // ============= UPDATE Course =================
//        @GetMapping("/course/{courseId}/edit")
//        public String createCourse(@PathVariable("courseId") int courseId, Model model) {
//            model.addAttribute("status", Status.values());
//            model.addAttribute("course", this.courseService.getCourseById(courseId));
//            return "createCourse";
//        }
//
//        // ============= Delete Course =================
//        @GetMapping("/course/delete")
//        public String deleteCourse(@RequestParam("id") int courseId) {
//            this.courseService.deleteCourse(courseId);
//            return "redirect:/";
//        }
//
//
//
//
//    }
//
//
