package com.skypro.course_03.controller;

import com.github.javafaker.Faker;
import com.skypro.course_03.entity.Faculty;
import com.skypro.course_03.entity.Student;
import com.skypro.course_03.repositories.FacultyRepository;
import com.skypro.course_03.repositories.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private Faculty generateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

    private Faculty addFaculty(Faculty faculty) {
        ResponseEntity<Faculty> facultyResponseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );
        assertThat(facultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyResponseEntity.getBody()).isNotNull();
        assertThat(facultyResponseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(faculty);
        assertThat(facultyResponseEntity.getBody().getId()).isNotNull();

        return facultyResponseEntity.getBody();
    }

    private Student generateStudent(Faculty faculty) {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(11, 18));
        if (faculty != null) {
            student.setFaculty(faculty);
        }
        return student;
    }

    private Student addStudent(Student student) {
        ResponseEntity<Student> studentResponseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );
        assertThat(studentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponseEntity.getBody()).isNotNull();
        assertThat(studentResponseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(student);
        assertThat(studentResponseEntity.getBody().getId()).isNotNull();

        return studentResponseEntity.getBody();
    }

    @Test void createTest() {
        addStudent(generateStudent(addFaculty(generateFaculty())));
    }

    @Test
    public void putTest() {
        Faculty f1 = addFaculty(generateFaculty());
        Faculty f2 = addFaculty(generateFaculty());
        Student student = addStudent(generateStudent(f1));

        ResponseEntity<Student> getForEntityResponse = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student.getId(),
                Student.class
        );
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody()).isNotNull();
        assertThat(getForEntityResponse.getBody()).usingRecursiveComparison().isEqualTo(student);
        assertThat(getForEntityResponse.getBody().getFaculty()).usingRecursiveComparison()
                .isEqualTo(f1);

        student.setFaculty(f2);

        ResponseEntity<Student> recordForEntityResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/" + student.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class
        );
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordForEntityResponse.getBody()).isNotNull();
        assertThat(recordForEntityResponse.getBody()).usingRecursiveComparison().isEqualTo(student);
        assertThat(recordForEntityResponse.getBody().getFaculty()).usingRecursiveComparison()
                .isEqualTo(f2);

    }

    @Test
    public void findByAgeBetweenTest() {
        List<Faculty> faculties = Stream.generate(this::generateFaculty)
                .limit(5)
                .map(this::addFaculty)
                .toList();
        List<Student> students = Stream.generate(
                        () -> generateStudent(faculties.get(faker.random().nextInt(faculties.size())))
                )
                .limit(50)
                .map(this::addStudent)
                .toList();

        int minAge = 14;
        int maxAge = 17;

        List<Student> expectedStudents = students.stream()
                .filter(
                        student -> student.getAge() >= minAge && student.getAge() <= maxAge
                )
                .toList();

        //http://localhost:8080/student?minAge=12&maxAge=13
        ResponseEntity<List<Student>> getForEntityResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/student?min={minAge}&max={maxAge}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                },
                minAge,
                maxAge
        );

        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody())
                .hasSize(expectedStudents.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedStudents);

    }
}
