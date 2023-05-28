package com.skypro.course_03.repositories;

import com.skypro.course_03.entity.Avatar;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

        Optional<Avatar> findByStudent_Id(Long studentId);
}
