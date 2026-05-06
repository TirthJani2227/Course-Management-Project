package com.tatvasoft.course_management.repository;

import com.tatvasoft.course_management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByNameAndIsDeletedFalse(String name);
    Optional<Course> findByIdAndIsDeletedFalse(Long id);

}
