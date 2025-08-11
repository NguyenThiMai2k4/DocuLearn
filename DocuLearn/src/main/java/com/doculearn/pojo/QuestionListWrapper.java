package com.doculearn.pojo;

import java.util.ArrayList;
import java.util.List;

public class QuestionListWrapper {
    private List<QuestionDTO> questions = new ArrayList<>();
    public List<QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDTO> questions) { this.questions = questions; }
}

