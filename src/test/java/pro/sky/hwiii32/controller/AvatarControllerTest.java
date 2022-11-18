package pro.sky.hwiii32.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.AvatarRecord;
import pro.sky.hwiii32.repository.AvatarRepository;
import pro.sky.hwiii32.repository.FacultyRepository;
import pro.sky.hwiii32.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:src/test/resources/application.properties")
class AvatarControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private AvatarController avatarController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private TestRestTemplate restTemplate;

    final Faker faker = new Faker();

    @AfterEach
    public void eraseData() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        int need = 100;
        int count = 0;
        while (count < need) {
            avatarRepository.save(generateAvatar(studentRepository.save(generateStudent())));
            count++;
        }
    }

    @Test
    void deleteAvatar() {

        final int countAvatar = avatarRepository.findAll().size();
        Avatar avatar = avatarRepository.findAll().stream().findFirst().orElse(null);

        ResponseEntity<String> responseEntity = restTemplate
                .exchange("http://localhost:" + port + "/avatar/" + avatar.getStudent().getId()
                        , HttpMethod.DELETE
                        , HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                        });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .contains(avatar.getStudent().getId().toString());

        assertEquals(countAvatar - 1, avatarRepository.findAll().size());

        assertThat(avatarRepository.findById(avatar.getId()).orElse(null))
                .isNull();
    }

    @Test
    void getAllAvatarWithPagination() {
        final int pageNumber = 1;
        final int pageSize = 10;

        int offset = pageSize * (pageNumber - 1);

        List<AvatarRecord> actualAvatars = avatarRepository.findAll().stream()
                .skip(offset)
                .limit(pageSize)
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());

        AvatarRecord[] avatarList = restTemplate
                .getForObject("http://localhost:" + port + "/avatar?page-number=" + pageNumber +
                                "&page-size=" + pageSize,
                        AvatarRecord[].class);

        List<AvatarRecord> expectedAvatars = new ArrayList<>(List.of(avatarList));

        assertThat(actualAvatars).usingRecursiveComparison()
                .isEqualTo(expectedAvatars);
    }

    private Student generateStudent() {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(11, 18));
        return student;
    }

    private Avatar generateAvatar(Student student) {
        Avatar avatar = new Avatar();
        avatar.setData(faker.avatar().image().getBytes());
        avatar.setMediaType("image/jpeg");
        avatar.setFileSize(faker.avatar().image().length());
        if (student != null) {
            avatar.setStudent(student);
            avatar.setFilePath("./avatar-dir/Student{id=" + student.getId()
                    + ", name='" + student.getName() + "', age=" + student.getAge() + "}.jpg");
        }
        return avatar;
    }
}