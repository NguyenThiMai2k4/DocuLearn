package com.doculearn.pojo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DataFileDTO {
    private SummaryDTO summary;
    private List<MultipleChoiceQuestionDTO> multipleChoice;
    private List<String> essayQuestions;
    public  DataFileDTO() {
    }



}
