package com.doculearn.repositories;

import com.doculearn.pojo.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<QuestionOption, Integer> {
}
