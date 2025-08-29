package com.doculearn.service;

import com.doculearn.pojo.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestionsByCourseId(int courseId);
    List<Question> getAllQuestionsBySummaryId(int summaryId);
    List<Question> findByCourse_IdAndSummary_Id (int courseId, int summaryId);
    Question getQuestionById(int id);
    List <Question> getQuestionsByName(String name);
    Question createOrUpdateQuestion(Question question);
    void deleteQuestion(int id);
}
