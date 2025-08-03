package com.doculearn.service;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    List<Summary> getAllSummaryByCourseId(int courseId);
    Course getCourseById(int id);
    Course createOrUpdateCourse(Course course);
    void deleteCourse(int id);
}
