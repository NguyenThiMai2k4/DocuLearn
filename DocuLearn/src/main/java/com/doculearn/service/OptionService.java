package com.doculearn.service;

import com.doculearn.pojo.QuestionOption;

import java.util.List;

public interface OptionService {
    List<QuestionOption> getAllOptions();
    QuestionOption createOrUpdateOption(QuestionOption questionOption);
    void deleteOption(int id);
}
