package com.tatvasoft.course_management.service.impl;

import com.tatvasoft.course_management.dto.request.CourseManagementDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Department;
import com.tatvasoft.course_management.exception.BusinessLogicException;
import com.tatvasoft.course_management.exception.CourseNotFoundException;
import com.tatvasoft.course_management.repository.CourseRepository;
import com.tatvasoft.course_management.service.CourseManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CourseManagementServiceImpl implements CourseManagementService {

    private final CourseRepository repository;

    @Override
    @Transactional
    public Course createCourse(String name, String content, Department department, Integer credits, User admin) {
        Course existingCourse = repository.findByNameAndIsDeletedFalse(name).orElse(null);
        if (existingCourse != null) throw new BusinessLogicException("Course already Exists");
        Course newCourse = new Course();
        newCourse.setName(name);
        newCourse.setCreatedBy(admin);
        newCourse.setContent(content);
        newCourse.setDepartment(department);
        newCourse.setCredits(credits);
        repository.save(newCourse);
        return newCourse;
    }

    @Override
    public Course getCourseById(Long id) {
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow(()->new CourseNotFoundException("No such course found by id: " + id));
    }

    @Override
    @Transactional
    public Course updateCourseById(Long id, CourseManagementDTO.Create dto) {
        Course existingCourse = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CourseNotFoundException("No such course found by id: " + id));

        if (dto.getName() != null) {
            String trimmedName = dto.getName().trim();
            if (trimmedName.length() < 8 || trimmedName.length() > 250)
                throw new IllegalArgumentException("Course name must be between 8 and 250 characters");
            existingCourse.setName(trimmedName);
        }

        if (dto.getContent() != null) {
            String trimmedContent = dto.getContent().trim();
            if (trimmedContent.length() < 10 || trimmedContent.length() > 250)
                throw new IllegalArgumentException("Course content must be between 10 and 250 characters");

            existingCourse.setContent(trimmedContent);
        }

        if (dto.getCredits() != null) {
            if (dto.getCredits() < 1 || dto.getCredits() > 6) {
                throw new IllegalArgumentException("Course credits must be between 1 to 6");
            }
            existingCourse.setCredits(dto.getCredits());
        }

        if (dto.getDepartment() != null) {
            existingCourse.setDepartment(dto.getDepartment());
        }

        return repository.save(existingCourse);
    }

    @Override
    @Transactional
    public boolean deleteCourseById(Long id){
        Course existingCourse = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CourseNotFoundException("No such course found by id: " + id));
        existingCourse.setIsDeleted(true);
        repository.save(existingCourse);
        return true;
    }

}
