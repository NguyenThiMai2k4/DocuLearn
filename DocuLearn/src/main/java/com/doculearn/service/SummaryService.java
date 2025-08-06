package com.doculearn.service;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SummaryService {
    List<Summary> getAllSummaryByCourseId(int courseId);
    Summary getSummaryById(int id);
    Summary createSummary(Summary summary);
    void deleteSummary(int id);
}
