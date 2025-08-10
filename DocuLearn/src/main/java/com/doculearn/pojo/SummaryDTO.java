package com.doculearn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SummaryDTO {
    private String title;
    private List<SectionDTO> sections;
    public SummaryDTO(){}

}
