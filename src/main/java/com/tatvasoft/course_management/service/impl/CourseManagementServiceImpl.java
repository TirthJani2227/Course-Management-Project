package com.tatvasoft.course_management.service.impl;

import com.tatvasoft.course_management.dto.request.CourseManagementDTO;
import com.tatvasoft.course_management.dto.response.CourseResponseDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.Enrollment;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Department;
import com.tatvasoft.course_management.enums.Status;
import com.tatvasoft.course_management.exception.BusinessLogicException;
import com.tatvasoft.course_management.exception.CourseNotFoundException;
import com.tatvasoft.course_management.repository.CourseRepository;
import com.tatvasoft.course_management.repository.EnrollmentRepository;
import com.tatvasoft.course_management.service.CourseManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseManagementServiceImpl implements CourseManagementService {

    private final CourseRepository repository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public CourseResponseDTO createCourse(String name, String content, Department department, Integer credits, User admin) {
        Course existingCourse = repository.findByNameAndIsDeletedFalse(name).orElse(null);
        if (existingCourse != null) throw new BusinessLogicException("Course already Exists");
        Course newCourse = new Course();
        newCourse.setName(name);
        newCourse.setCreatedBy(admin);
        newCourse.setContent(content);
        newCourse.setDepartment(department);
        newCourse.setCredits(credits);
        newCourse = repository.save(newCourse);
        return CourseResponseDTO.builder().name(name).id(newCourse.getId()).content(newCourse.getContent()).credits(newCourse.getCredits()).createdAt(newCourse.getCreatedAt()).department(newCourse.getDepartment()).build();
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = repository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CourseNotFoundException("No such course found by id: " + id));
        return new CourseResponseDTO(course.getId(), course.getName(), course.getContent(), course.getCredits(), course.getDepartment(), course.getCreatedAt());
    }

    @Override
    @Transactional
    public CourseResponseDTO updateCourseById(Long id, CourseManagementDTO.Create dto) {
        Course course = repository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CourseNotFoundException("No such course found by id: " + id));


        if (dto.getName() != null) {
            String trimmedName = dto.getName().trim();
            if (trimmedName.length() < 8 || trimmedName.length() > 250)
                throw new IllegalArgumentException("Course name must be between 8 and 250 characters");
            course.setName(trimmedName);
        }

        if (dto.getContent() != null) {
            String trimmedContent = dto.getContent().trim();
            if (trimmedContent.length() < 10 || trimmedContent.length() > 250)
                throw new IllegalArgumentException("Course content must be between 10 and 250 characters");

            course.setContent(trimmedContent);
        }

        if (dto.getCredits() != null) {
            if (dto.getCredits() < 1 || dto.getCredits() > 6) {
                throw new IllegalArgumentException("Course credits must be between 1 to 6");
            }
            course.setCredits(dto.getCredits());
        }

        if (dto.getDepartment() != null) {
            course.setDepartment(dto.getDepartment());
        }
        course = repository.save(course);
        return new CourseResponseDTO(course.getId(), course.getName(), course.getContent(), course.getCredits(), course.getDepartment(), course.getCreatedAt());
    }

    @Override
    public boolean deleteCourseById(Long id) {
        Course existingCourse = repository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CourseNotFoundException("No such course found by id: " + id));
        List<Enrollment> allEnrollmentsInCourse = enrollmentRepository.findByCourseAndIsDeletedFalse(existingCourse);
        List<Enrollment> result = allEnrollmentsInCourse.stream().filter((enrollment -> {
            return enrollment.getStatus() == Status.ENROLLED;
        })).toList();

        if (!result.isEmpty()) throw new BusinessLogicException("Cannot delete a course in which users are enrolled");

        existingCourse.setIsDeleted(true);
        repository.save(existingCourse);
        return true;
    }

    @Override
    public Page<CourseResponseDTO> getAllCourses(String name, Department department, Pageable pageable) {

        Page<Course> courses;

        if (name != null && !name.isBlank()) {
            String nameFilter = "%" + name.trim().toLowerCase() + "%";
            courses = repository.findWithName(nameFilter, department, pageable);
        } else {
            courses = repository.findWithoutName(department, pageable);
        }

        return courses.map(course -> CourseResponseDTO.builder().id(course.getId()).name(course.getName()).content(course.getContent()).credits(course.getCredits()).department(course.getDepartment()).createdAt(course.getCreatedAt()).build());
    }


}
