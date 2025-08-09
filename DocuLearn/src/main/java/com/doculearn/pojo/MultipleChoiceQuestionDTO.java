package com.doculearn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class MultipleChoiceQuestionDTO {
    private String question;
    private List<String> options;
}
