package com.doculearn.repositories;

import com.doculearn.pojo.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {
}
