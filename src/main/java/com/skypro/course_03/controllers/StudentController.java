package com.skypro.course_03.controllers;

import com.skypro.course_03.entity.Faculty;
import com.skypro.course_03.entity.Student;
import com.skypro.course_03.services.AvatarService;
import com.skypro.course_03.services.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/student")
@Tag(name = "Student UI", description = "Check your student methods.")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Student>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Student> add(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.add(student));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Optional<Student>> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.update(id, student));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Optional<Student>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteById(id));
    }

    @GetMapping(params = "age")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping(params = {"minAge","maxAge"})
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
    }

    @GetMapping(value = "/{id}/faculty")
    public ResponseEntity<Optional<Faculty>> getStudentFaculties(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentFaculties(id));
    }
}
