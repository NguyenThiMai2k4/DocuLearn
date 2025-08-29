package com.doculearn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class OptionForm {

    private List<QuestionOption> options= new ArrayList<>();;


    public OptionForm() {}
}
