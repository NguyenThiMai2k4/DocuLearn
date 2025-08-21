package com.doculearn.repositories;

import com.doculearn.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    <T> Optional<T> findByTitle(String title);
}
