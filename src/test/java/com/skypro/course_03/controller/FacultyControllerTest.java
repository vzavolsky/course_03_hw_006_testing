package com.skypro.course_03.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.course_03.controllers.FacultyController;
import com.skypro.course_03.entity.Faculty;
import com.skypro.course_03.repositories.FacultyRepository;
import com.skypro.course_03.repositories.StudentRepository;
import com.skypro.course_03.services.FacultyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @Test
    public void createTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("red");
        faculty.setName("Griffindor");

        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
        ).andExpect( result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            Faculty facultyResult = objectMapper.readValue(
                    mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8),
                    Faculty.class);

            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyResult).isNotNull();
            assertThat(facultyResult).usingRecursiveComparison().ignoringFields("id")
                    .isEqualTo(faculty);
            assertThat(facultyResult.getId()).isEqualTo(faculty.getId());
        });
    }
}
