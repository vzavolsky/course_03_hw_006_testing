package com.skypro.course_03.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {AvatarProcessingException.class})
    public ResponseEntity<?> handleAvatarNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {StudentNotFoundException.class, AvatarNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
