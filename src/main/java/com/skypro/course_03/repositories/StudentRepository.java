package com.skypro.course_03.repositories;

import com.skypro.course_03.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> getStudentsByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    //@Query(value = "SELECT s.id, s.name, s.age FROM students as s, faculties as f WHERE s.faculty_id = f.id AND s.faculty_id = :id", nativeQuery = true)
    Collection<Student> findAllByFaculty_Id(Long id);

}
