package com.doculearn.service.Impl;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.service.CourseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Course createIfNotExists(String lmsTitle) {
        return (Course) courseRepo.findByTitle(lmsTitle)
                .orElseGet(() -> {
                    Course course = new Course();
                    course.setTitle(lmsTitle);
                    return courseRepo.save(course);
                });
    }

    @Override
    public void deleteCourse(int id){
        this.courseRepo.deleteById(id);
    }
}
