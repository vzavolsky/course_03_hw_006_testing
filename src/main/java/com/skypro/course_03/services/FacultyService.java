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
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty add(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> update(Long id, Faculty faculty) {
        return facultyRepository.findById(id)
                .map(f -> {
                    f.setName(faculty.getName());
                    f.setColor(faculty.getColor());
                    return facultyRepository.save(f);
                });
    }

    public Optional<Faculty> getById(Long id) {
        return facultyRepository.findById(id);
    }

    public Collection<Faculty> getAll() {
        return Collections.unmodifiableCollection(facultyRepository.findAll());
    }

    public Optional<Faculty> deleteById(Long id) {
        return facultyRepository.findById(id)
                .map(f -> {
                    facultyRepository.deleteById(id);
                    return f;
                });
    }

    public Collection<Faculty> findAllByNameOrColor(String name, String color) {
        return facultyRepository.findAllByNameContainsIgnoreCaseOrColorContainsIgnoreCase(name, color);
    }

    public Collection<Student> getStudentsByFacultyId(Long id) {
        return studentRepository.findAllByFaculty_Id(id);
    }
}
