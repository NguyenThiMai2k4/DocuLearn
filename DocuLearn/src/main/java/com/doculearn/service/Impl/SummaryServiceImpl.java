package com.doculearn.service.Impl;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.repositories.SummaryRepository;
import com.doculearn.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryServiceImpl implements SummaryService {
    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SummaryRepository summaryRepo;

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
    public Summary getSummaryById(int id){
        Optional<Summary> summary = this.summaryRepo.findById(id);
        return  summary.orElse(null);
    }

    @Override
    public Summary createSummary(Summary summary){
        return  this.summaryRepo.save(summary);
    }

    @Override
    public void deleteSummary(int id){
        this.summaryRepo.deleteById(id);
    }

}
