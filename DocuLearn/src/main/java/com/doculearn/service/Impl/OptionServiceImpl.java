package com.doculearn.service.Impl;

import com.doculearn.pojo.Question;
import com.doculearn.pojo.QuestionOption;
import com.doculearn.pojo.Summary;
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
import java.util.Optional;

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
    public List<QuestionOption> getOptionByQuestionId(int questionId) {
        List<QuestionOption> questionOptions = this.optionRepository.findByQuestion_Id(questionId);
//        if(questionOptions.isEmpty()){
//            throw new EntityNotFoundException("khong tim thay option nao");
//        }
        return questionOptions;
    }

    @Override
    public QuestionOption getOptionById(int id){
        Optional<QuestionOption> option = this.optionRepository.findById(id);
        return  option.orElse(null);

    }

    @Override
    public QuestionOption createOrUpdateOption(QuestionOption option){
        if (option.getQuestion() != null && option.getQuestion().getId() != null) {
            Question question = questionRepository.findById(option.getQuestion().getId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            option.setQuestion(question); // gán lại Question đã được quản lý
        }
        return this.optionRepository.save(option);
    }

    @Override
    public void deleteOption(int id){
        this.optionRepository.deleteById(id);
    }
}
