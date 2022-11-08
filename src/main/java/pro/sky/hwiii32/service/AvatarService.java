package pro.sky.hwiii32.service;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hwiii32.exceptions.AvatarNotFoundException;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.repository.AvatarRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Avatar findAvatarByStudentIdByIdOrNew(Long id) {
        return avatarRepository.findAvatarByStudent(studentService.findStudentById(id))
                .orElse(new Avatar());
    }

    public void uploadAvatar(Long studentId, MultipartFile fileAvatar) throws IOException {
        Student student = studentService.findStudentById(studentId);

        String fileNameStr = fileAvatar.getOriginalFilename();
        if (fileNameStr == null) {
            throw new InvalidFileNameException(fileAvatar.getName(), "Некорректное имя файла");
        }
        String fileNameExtStr = fileNameStr.substring(fileNameStr.lastIndexOf('.') + 1);

        Path filePath = Path.of(avatarsDir, student + "." + fileNameExtStr);
        if (Files.notExists(filePath.getParent())) {
            Files.createDirectory(filePath.getParent());
        }
        Files.deleteIfExists(filePath);
        try (
                InputStream is = fileAvatar.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatarByStudentIdByIdOrNew(studentId);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(fileAvatar.getSize());
        avatar.setMediaType(fileAvatar.getContentType());
        avatar.setData(fileAvatar.getBytes());
        avatar.setStudent(student);
        avatarRepository.save(avatar);
    }


    public String createAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        uploadAvatar(studentId, avatarFile);
        return "Download done";
    }

    public void readAvatarFromDir(Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = readAvatarDb(id);
        Path path = Path.of(avatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream()
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    public Avatar readAvatarDb(long id) {
        return avatarRepository
                .findAvatarByStudent(studentService.findStudentById(id))
                .orElseThrow(() -> new AvatarNotFoundException("Такого аватара нет"));
    }

    public String deleteAvatar(long id) throws IOException {
        Avatar deleteAvatar = avatarRepository
                .findAvatarByStudent(studentService.findStudentById(id))
                .orElseThrow(() -> new AvatarNotFoundException("Такого аватара нет"));

        Files.deleteIfExists(Path.of(deleteAvatar.getFilePath()));
        avatarRepository.deleteById(deleteAvatar.getId());
        return "Аватар студента " + id + " удалён";
    }
}