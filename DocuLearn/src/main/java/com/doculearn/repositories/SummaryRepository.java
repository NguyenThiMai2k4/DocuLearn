package com.doculearn.repositories;

import com.doculearn.pojo.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {
    List<Summary> findByCourse_Id(Integer courseId);
}
