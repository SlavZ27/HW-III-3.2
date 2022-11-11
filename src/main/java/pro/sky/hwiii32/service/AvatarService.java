package pro.sky.hwiii32.service;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.exceptions.AvatarNotFoundException;
import pro.sky.hwiii32.exceptions.StudentNotFoundException;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.AvatarRecord;
import pro.sky.hwiii32.repository.AvatarRepository;
import pro.sky.hwiii32.repository.StudentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final RecordMapper recordMapper;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;


    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, RecordMapper recordMapper) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.recordMapper = recordMapper;
    }

    public Avatar findAvatarByStudentIdByIdOrNew(Long studentId) {
        return avatarRepository
                .findAvatarByStudent(
                        studentRepository.findById(studentId).
                                orElseThrow(() -> new StudentNotFoundException(String.valueOf(studentId))))
                .orElse(new Avatar());
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile fileAvatar) throws IOException {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(String.valueOf(studentId)));

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
        return avatarRepository.save(avatar);
    }


    public AvatarRecord createAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        return recordMapper.toRecord(uploadAvatar(studentId, avatarFile));
    }

    public void readAvatarFromDir(Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = readAvatarDb(studentId);
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

    public Avatar readAvatarDb(long studentId) {
        return avatarRepository
                .findAvatarByStudent(
                        studentRepository.findById(studentId).
                                orElseThrow(() -> new StudentNotFoundException(String.valueOf(studentId))))
                .orElseThrow(() -> new AvatarNotFoundException(
                        studentRepository.findById(studentId).get().getId().toString()));
    }

    public String deleteAvatar(long studentId) throws IOException {
        Avatar deleteAvatar = avatarRepository
                .findAvatarByStudent(
                        studentRepository.findById(studentId).
                                orElseThrow(() -> new StudentNotFoundException(String.valueOf(studentId))))
                .orElseThrow(() -> new AvatarNotFoundException(
                        studentRepository.findById(studentId).orElse(null).getId().toString()));

        Files.deleteIfExists(Path.of(deleteAvatar.getFilePath()));
        avatarRepository.deleteById(deleteAvatar.getId());
        return "Аватар студента " + studentId + " удалён";
    }

    public List<AvatarRecord> getAllAvatarWithPagination(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1,pageSize);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}