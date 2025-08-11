package com.doculearn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QuestionDTO {
    private String content;
    private String responseType;

    private List<QuestionOption> options = new ArrayList<>();
    private int summaryId;
    public QuestionDTO() {}

}
