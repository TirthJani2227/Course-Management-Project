package com.tatvasoft.course_management.repository;

import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.enums.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findByNameAndIsDeletedFalse(String name);

    Optional<Course> findByIdAndIsDeletedFalse(Long id);

    @Query("""
                SELECT c FROM Course c
                WHERE c.isDeleted = false
                AND LOWER(c.name) LIKE :name
                AND (:department IS NULL OR c.department = :department)
            """)
    Page<Course> findWithName(
            @Param("name") String name,
            @Param("department") Department department,
            Pageable pageable);

    @Query("""
                SELECT c FROM Course c
                WHERE c.isDeleted = false
                AND (:department IS NULL OR c.department = :department)
            """)
    Page<Course> findWithoutName(
            @Param("department") Department department,
            Pageable pageable);
}
