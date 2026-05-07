package com.tatvasoft.course_management.repository;

import com.tatvasoft.course_management.entity.Enrollment;
import com.tatvasoft.course_management.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourseAndIsDeletedFalse(User student, Course course);
    List<Enrollment> findByUserAndIsDeletedFalse(User student);
    List<Enrollment> findByCourseAndIsDeletedFalse(Course course);
}
