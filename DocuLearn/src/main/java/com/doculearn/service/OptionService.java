package com.doculearn.service;

import com.doculearn.pojo.QuestionOption;

import java.util.List;

public interface OptionService {
    List<QuestionOption> getAllOptions();
    QuestionOption getOptionById(int id);
    List<QuestionOption> getOptionByQuestionId(int questionId);
    QuestionOption createOrUpdateOption(QuestionOption questionOption);
    void deleteOption(int id);
}
