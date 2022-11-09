package pro.sky.hwiii32;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.hwiii32.controller.AvatarController;
import pro.sky.hwiii32.controller.StudentController;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.AvatarRepository;
import pro.sky.hwiii32.repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationRestTemplateTest {

    @LocalServerPort
    private int port;
    @Autowired
    private AvatarController avatarController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextsLoad() {
        assertThat(avatarController).isNotNull();
        assertThat(studentController).isNotNull();
    }

    @Test
    public void testCRUDStudentPositive() {
        final String name1 = "qwerty";
        final int age1 = 20;

        Student student1 = new Student();
        student1.setName(name1);
        student1.setAge(age1);

        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setName(name1);
        studentRecord.setAge(age1);

        List<Student> studentList = studentRepository.findAll();
        int studentListCount = studentList.size();

        assertThat(restTemplate
                .postForObject("http://localhost:" + port + "/student",
                        studentRecord,
                        StudentRecord.class))
                .isNotNull();

        studentList = studentRepository.findAll();
        assertThat(studentList.size())
                .isEqualTo(studentListCount + 1);

        Student studentDelete = studentList.stream()
                .filter(student -> student.getAge() == age1
                        & student.getName().equals(name1))
                .findFirst().orElse(null);

        assertThat(studentDelete)
                .isNotNull();
        assertThat(studentDelete.getAge())
                .isEqualTo(student1.getAge());
        assertThat(studentDelete.getName())
                .isEqualTo(student1.getName());


        assertThat(restTemplate
                .getForObject("http://localhost:" + port + "/student" + studentDelete.getId(),
                        JSONObject.class))
                .isNotNull();

        studentRecord.setId(studentDelete.getId());
        studentRecord.setName("123qwe123");
        studentRecord.setAge(100);

        restTemplate
                .put("http://localhost:" + port + "/student",
                        studentRecord);

        studentList = studentRepository.findAll();
        assertThat(studentList.size())
                .isEqualTo(studentListCount + 1);

        studentDelete = studentList.stream()
                .filter(student -> student.getId() == studentRecord.getId())
                .findFirst().orElse(null);

        assertThat(studentDelete)
                .isNotNull();
        assertThat(studentDelete.getAge())
                .isEqualTo(studentRecord.getAge());
        assertThat(studentDelete.getName())
                .isEqualTo(studentRecord.getName());

        restTemplate
                .delete("http://localhost:" + port + "/student/" + studentDelete.getId());

        studentList = studentRepository.findAll();
        assertThat(studentList.size())
                .isEqualTo(studentListCount);
    }


    @Test
    public void getAllStudentTest() {
        int actualCountStudents = 11;


        StudentRecord[] studentList = restTemplate
                .getForObject("http://localhost:" + port + "/student",
                        StudentRecord[].class);

        assertThat(studentList.length)
                .isEqualTo(actualCountStudents);
    }

    @Test
    public void getStudentsWithEqualAgeTest() {
        int actualCountStudentsEqualAge = 4;


        StudentRecord[] studentList = restTemplate
                .getForObject("http://localhost:" + port + "/student/filter?age=10",
                        StudentRecord[].class);

        assertThat(studentList.length)
                .isEqualTo(actualCountStudentsEqualAge);
    }

    @Test
    public void getStudentsWithBetweenAgeTest() {
        int actualCountStudentsWithBetweenAge = 5;

        StudentRecord[] studentList = restTemplate
                .getForObject("http://localhost:" + port + "/student/filter?age_from=10&age_to=15",
                        StudentRecord[].class);

        assertThat(studentList.length)
                .isEqualTo(actualCountStudentsWithBetweenAge);
    }

    @Test
    public void getFacultyByStudentTest() {
        int actualFacultyId = 14;

        FacultyRecord facultyRecord = restTemplate
                .getForObject("http://localhost:" + port + "/student/filter?student_id=3",
                        FacultyRecord.class);

        assertThat(facultyRecord.getId())
                .isEqualTo(actualFacultyId);
    }

//    @Test
//    public void testCRUDAvatarPositive() throws Exception {
//        final String name1 = "qwerty";
//        final int age1 = 20;
//
//        Student student1 = new Student();
//        student1.setId(2L);
//        student1.setName(name1);
//        student1.setAge(age1);
//
//        Avatar avatar = new Avatar();
//        byte[] bytes = {1, 2, 3, 4, 5};
//        avatar.setData(bytes);
//        avatar.setFilePath("/Users/slavz/Desktop/dir/Student{id=2, name='Kolya', age=2}.jpg");
//        avatar.setFileSize(9255);
//        avatar.setMediaType("image/jpeg");
//        avatar.setStudent(student1);
//
//        MultipartFile multipartFile;
//        multipartFile = Files.
//
//        List<Avatar> avatarList = avatarRepository.findAll();
//        int studentListCount = avatarList.size();
//
//        assertThat(restTemplate
//                .postForObject("http://localhost:" + port + "/student/2/avatar",
//                        avatar,
//                        String.class))
//                .isNotNull();
//
//        avatarList = avatarRepository.findAll();
//        assertThat(avatarList.size())
//                .isEqualTo(studentListCount + 1);
//
//        Avatar avatarDelete = avatarList.stream()
//                .filter(student -> student.getStudent().equals(student1))
//                .findFirst().orElse(null);
//
//        assertThat(avatarDelete)
//                .isNotNull();
//        assertThat(avatarDelete.getData())
//                .isEqualTo(avatar.getData());
//        assertThat(avatarDelete.getFilePath())
//                .isEqualTo(avatar.getFilePath());
//        assertThat(avatarDelete.getFileSize())
//                .isEqualTo(avatar.getFileSize());
//        assertThat(avatarDelete.getMediaType())
//                .isEqualTo(avatar.getMediaType());
//
//        assertThat(restTemplate
//                .getForObject("http://localhost:" + port + "/student/2/avatar-db",
//                        byte[].class))
//                .isNotNull();
//
////        assertThat(restTemplate
////                .getForObject("http://localhost:" + port + "/student/2/avatar-db",
////        void));
//
//        restTemplate
//                .delete("http://localhost:" + port + "/student/2/avatar");
//
//        avatarList = avatarRepository.findAll();
//        assertThat(avatarList.size())
//                .isEqualTo(studentListCount);
//    }

}