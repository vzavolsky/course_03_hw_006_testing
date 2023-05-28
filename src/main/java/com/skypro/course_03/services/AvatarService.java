package com.skypro.course_03.services;

import com.skypro.course_03.entity.Avatar;
import com.skypro.course_03.entity.Student;
import com.skypro.course_03.exceptions.AvatarNotFoundException;
import com.skypro.course_03.exceptions.AvatarProcessingException;
import com.skypro.course_03.exceptions.StudentNotFoundException;
import com.skypro.course_03.repositories.AvatarRepository;
import com.skypro.course_03.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Transactional
public class AvatarService {

    private String avatarDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository,
                         @Value("${students.avatar.dir.path}") String avatarDir,
                         StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.avatarDir = avatarDir;
        this.studentRepository = studentRepository;
    }

    public Optional<Avatar> findById(Long id) {
        return avatarRepository.findById(id);
    }

    public void uploadAvatar(Long studentId, MultipartFile multipartFile) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(StudentNotFoundException::new);
            String fileName = String.format(
                    "%d.%s",
                    student.getId(),
                    StringUtils.getFilenameExtension(multipartFile.getOriginalFilename())
            );
            byte[] data = multipartFile.getBytes();
            Path path = Paths.get(avatarDir, fileName);
            Files.write(path, data);

            Avatar avatar = new Avatar();
            avatar.setData(data);
            avatar.setFilePath(path.toString());
            avatar.setFileSize(data.length);
            avatar.setStudent(student);
            avatar.setMediaType(multipartFile.getContentType());
            avatarRepository.save(avatar);

        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    public Pair<byte[], String> getFromFs(Long studentId) {
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseThrow(AvatarNotFoundException::new);
            Path path = Paths.get(avatar.getFilePath());
            return Pair.of(Files.readAllBytes(path), avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    public Pair<byte[], String> getFromDb(Long studentId) {
        Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                .orElseThrow(AvatarNotFoundException::new);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }
}
