package com.skypro.course_03.services;

import com.skypro.course_03.entity.Faculty;
import com.skypro.course_03.entity.Student;
import com.skypro.course_03.repositories.FacultyRepository;
import com.skypro.course_03.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student add(Student student) {
        student.setId(null);
        student.setFaculty(
                Optional.ofNullable(student.getFaculty())
                        .filter(f -> f.getId() != null)
                        .flatMap(f -> facultyRepository.findById(f.getId()))
                        .orElse(null));
        return studentRepository.save(student);
    }

    public Optional<Student> update(Long id, Student student) {
        return studentRepository.findById(id)
                .map(s -> {
                    s.setName(student.getName());
                    s.setAge(student.getAge());
                    s.setFaculty(
                            Optional.ofNullable(student.getFaculty())
                                    .filter(f -> f.getId() != null)
                                    .flatMap(f -> facultyRepository.findById(f.getId()))
                                    .orElse(null));
                    return studentRepository.save(s);
                });
    }

    public Optional<Student> getById(Long id) {
        return studentRepository.findById(id);
    }

    public Collection<Student> getAll() {
        return Collections.unmodifiableCollection(studentRepository.findAll());
    }

    public Optional<Student> deleteById(Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.deleteById(id);
                    return student;
                });
    }

    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.getStudentsByAge(age);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Optional<Faculty> getStudentFaculties(Long id) {
        return studentRepository.findById(id)
                .map(student -> student.getFaculty());
    }
}
