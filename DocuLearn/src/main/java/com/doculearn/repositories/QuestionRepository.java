package com.doculearn.repositories;

import com.doculearn.pojo.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {


    List<Question> findBySummary_Id(Integer summaryId);

    Question findAllByContent(String content);

    List<Question> findByContentContaining(String content);

    @EntityGraph(attributePaths = "questionOptions")
    List<Question> findByCourse_Id(Integer courseId);

    List<Question> findByCourse_IdAndSummary_Id(Integer courseId, Integer summaryId);

}
