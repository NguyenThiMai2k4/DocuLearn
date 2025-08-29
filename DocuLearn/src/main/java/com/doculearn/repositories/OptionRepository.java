package com.doculearn.repositories;

import com.doculearn.pojo.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<QuestionOption, Integer> {
    List<QuestionOption> findByQuestion_Id(Integer questionId);
}
