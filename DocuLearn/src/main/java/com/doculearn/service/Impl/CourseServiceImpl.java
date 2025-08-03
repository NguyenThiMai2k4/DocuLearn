package com.doculearn.service.Impl;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Override
    public List<Course> getAllCourses(){
        return  this.courseRepo.findAll();
    }
    @Override
    public List<Summary> getAllSummaryByCourseId(int courseId){
        Optional<Course> courses = this.courseRepo.findById(courseId);
        if(courses.isPresent()){
            Course course = courses.get();
            return  new ArrayList<>(course.getSummaries());

        }
        return new ArrayList<>();
    }

    @Override
    public Course getCourseById(int id){
        Optional<Course> courses = this.courseRepo.findById(id);
        return courses.orElse(null);
    }

    @Override
    public Course createOrUpdateCourse(Course course){
        return  this.courseRepo.save(course);
    }

    @Override
    public void deleteCourse(int id){
        this.courseRepo.deleteById(id);
    }
}
