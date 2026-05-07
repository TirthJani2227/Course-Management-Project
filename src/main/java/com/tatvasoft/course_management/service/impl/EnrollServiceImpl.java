package com.tatvasoft.course_management.service.impl;

import com.tatvasoft.course_management.dto.response.EnrolledCourseDetailsDTO;
import com.tatvasoft.course_management.dto.response.EnrollmentResponseDTO;
import com.tatvasoft.course_management.dto.response.MyProfileResponseDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.Enrollment;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Status;
import com.tatvasoft.course_management.exception.BusinessLogicException;
import com.tatvasoft.course_management.exception.CourseNotFoundException;
import com.tatvasoft.course_management.repository.CourseRepository;
import com.tatvasoft.course_management.repository.EnrollmentRepository;
import com.tatvasoft.course_management.repository.UserRepository;
import com.tatvasoft.course_management.service.EnrollService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EnrollServiceImpl implements EnrollService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public EnrollmentResponseDTO enrollStudent(User student, Long courseId) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseId).orElseThrow(() ->
                new CourseNotFoundException("No such course found with id: " + courseId)
        );

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserAndCourseAndIsDeletedFalse(student, course);

        if (existingEnrollment.isPresent())
            throw new BusinessLogicException("Student already enrolled in the course");

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(student);
        enrollment.setCourse(course);
        enrollment.setStatus(Status.ENROLLED);
//        For now, we used set default time but in future we can change as we want
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setCreatedBy(student);

        enrollment = enrollmentRepository.save(enrollment);

        return new EnrollmentResponseDTO(enrollment);

    }

    @Override
    @Transactional
    public void markCourseCompleted(User student, Long courseId) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseId).orElseThrow(() ->
                new CourseNotFoundException("No such course found with id: " + courseId)
        );

        Enrollment existingEnrollment = enrollmentRepository.findByUserAndCourseAndIsDeletedFalse(student, course).orElseThrow(() -> new BusinessLogicException("No such student enrolled in this course"));

        if(existingEnrollment.getStatus() == Status.COMPLETED) throw new BusinessLogicException("Student has already completed the course");

        existingEnrollment.setStatus(Status.COMPLETED);
        existingEnrollment.setCompletedAt(LocalDateTime.now());
        existingEnrollment.setModifiedBy(student);

        enrollmentRepository.save(existingEnrollment);
    }

    @Override
    public MyProfileResponseDTO getStudentProfile(User student) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserAndIsDeletedFalse(student);
        return new MyProfileResponseDTO(enrollments, student);
    }

    @Override
    public EnrolledCourseDetailsDTO getStudentsEnrolledInCourse(Long courseId) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseId).orElseThrow(() ->
                new CourseNotFoundException("No such course found with id: " + courseId)
        );
        List<Enrollment> enrollments = enrollmentRepository.findByCourseAndIsDeletedFalse(course);
        return new EnrolledCourseDetailsDTO(course,enrollments);
    }
}
