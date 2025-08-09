package com.doculearn.service.Impl;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Question;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.repositories.QuestionRepository;
import com.doculearn.repositories.SummaryRepository;
import com.doculearn.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Override
    public List<Question> getAllQuestionsByCourseId(int courseId){

        List<Question> questions= this.questionRepo.findByCourse_Id(courseId);
        if(questions.isEmpty()){
            throw new EntityNotFoundException("Not found Question with courseId:"+courseId);
        }
        return questions;

    }
    @Override
    public List<Question> getAllQuestionsBySummaryId(int summaryId){
        List<Question> questions= this.questionRepo.findBySummary_Id(summaryId);
        if(questions.isEmpty()){
            throw new EntityNotFoundException("Not found Question with summaryId:"+summaryId);
        }
        return questions;
    }

    @Override
    public Question getQuestionById(int id) {
        return this.questionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy câu hỏi với id = " + id));
    }

    @Override
    public List<Question> getQuestionsByName(String name) {
        List<Question> questions = this.questionRepo.findByContentContaining(name);

        if (questions.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy câu hỏi nào có nội dung chứa: " + name);
        }

        return questions;
    }

    @Override
    public Question createOrUpdateQuestion(Question question){
        return this.questionRepo.save(question);
    }

    @Override
    public void deleteQuestion(int id){
        this.questionRepo.deleteById(id);
    }
}
