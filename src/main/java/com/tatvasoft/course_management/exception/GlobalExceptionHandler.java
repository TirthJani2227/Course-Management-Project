package com.tatvasoft.course_management.exception;

import com.tatvasoft.course_management.dto.response.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponseDTO.error("Request method '" + ex.getMethod() + "' not supported"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(NoHandlerFoundException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.error("The requested resource was not found."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String message;
            if ("typeMismatch".equals(error.getCode())) {
                message = "Invalid input format";
            } else {
                message = error.getDefaultMessage();
            }
            fieldErrors.put(error.getField(), message);
        }
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(false, "Validations failed", fieldErrors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleCourseNotFoundException(CourseNotFoundException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleInvalidCredentialException(InvalidCredentialException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.error("Something went wrong. Please try again."));
    }
}
