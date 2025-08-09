package com.doculearn.service.Impl;

import com.doculearn.pojo.QuestionOption;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.repositories.OptionRepository;
import com.doculearn.repositories.QuestionRepository;
import com.doculearn.service.OptionService;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository  questionRepository;

    @Override
    public List<QuestionOption> getAllOptions(){
        List<QuestionOption> questionOptions = this.optionRepository.findAll();
        if(questionOptions.isEmpty()){
            throw new EntityNotFoundException("khong tim thay option nao");
        }
        return questionOptions;
    }

    @Override
    public QuestionOption createOrUpdateOption(QuestionOption questionOption){
        return this.optionRepository.save(questionOption);
    }

    @Override
    public void deleteOption(int id){
        this.optionRepository.deleteById(id);
    }
}
