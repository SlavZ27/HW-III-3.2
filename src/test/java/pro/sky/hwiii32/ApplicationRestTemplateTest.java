package pro.sky.hwiii32;

import com.github.javafaker.Faker;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.controller.AvatarController;
import pro.sky.hwiii32.controller.StudentController;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.AvatarRepository;
import pro.sky.hwiii32.repository.FacultyRepository;
import pro.sky.hwiii32.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:resources/application.properties")
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
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void generateData() {
        int need = 100;
        int count = 0;
        while (count < need / 10) {
            facultyRepository.save(generateFaculty());
            count++;
        }

        List<Faculty> faculties = facultyRepository.findAll();
        Random random = new Random();

        count = 0;
        while (count < need) {
            studentRepository.save(generateStudent(faculties.get(random.nextInt(faculties.size()))));
            count++;
        }
    }

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
                .getForObject("http://localhost:" + port + "/student/" + studentDelete.getId(),
                        JSONObject.class))
                .isNotNull();

        studentRecord.setId(studentDelete.getId());
        studentRecord.setName("123qwe123");
        studentRecord.setAge(21);

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

        final long actualCountStudents = studentRepository.findAll().size();

        StudentRecord[] studentList = restTemplate
                .getForObject("http://localhost:" + port + "/student",
                        StudentRecord[].class);

        assertThat(studentList.length)
                .isEqualTo(actualCountStudents);
    }

    @Test
    public void getStudentsWithEqualAgeTest() {
        final int testAge = 10;
        final long actualCountStudentsEqualAge = studentRepository.findAll().stream()
                .filter(student -> student.getAge() == testAge)
                .count();

        StudentRecord[] studentList = restTemplate
                .getForObject("http://localhost:" + port + "/student?age=" + testAge,
                        StudentRecord[].class);

        assertThat(studentList.length)
                .isEqualTo(actualCountStudentsEqualAge);
    }

    @Test
    public void getStudentsWithBetweenAgeTest() {
        final int age_from = 10;
        final int age_to = 15;

        final long actualCountStudentsWithBetweenAge = studentRepository.findAll().stream()
                .filter(student -> student.getAge() >= age_from & student.getAge() <= age_to)
                .count();

        StudentRecord[] studentList = restTemplate
                .getForObject("http://localhost:" + port + "/student?age_from=" + age_from + "&age_to=" + age_to,
                        StudentRecord[].class);

        assertThat(studentList.length)
                .isEqualTo(actualCountStudentsWithBetweenAge);
    }

    @Test
    public void getFacultyByStudentTest() {


        final Student actualStudent = studentRepository.findAll().stream().findAny().orElse(null);
        assert actualStudent != null;
        final int testStudentId = Math.toIntExact(actualStudent.getId());

        FacultyRecord facultyRecord = restTemplate
                .getForObject("http://localhost:" + port + "/student/" + testStudentId + "/faculty",
                        FacultyRecord.class);


        assertThat(facultyRecord.getId())
                .isEqualTo(actualStudent.getFaculty().getId());
    }

    @Test
    public void getCountStudentTest() {
        final Long actualCountStudent = (long) studentRepository.findAll().size();
        Long expectedCountStudent = restTemplate
                .getForObject("http://localhost:" + port + "/student/count",
                        Long.class);
        assertThat(actualCountStudent)
                .isEqualTo(expectedCountStudent);
    }

    @Test
    public void getMidAgeOfStudentTest() {
        final double actualMidAgeOfStudent = studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average().orElse(0);

        float expectedMidAgeOfStudent = restTemplate
                .getForObject("http://localhost:" + port + "/student/mid-age",
                        Long.class);
        assertThat((int) actualMidAgeOfStudent)
                .isEqualTo((int) expectedMidAgeOfStudent);
    }

    @Test
    public void get5StudentWithBiggerIdTest() {
        List<Student> allStudentRecord = studentRepository.findAll().
                stream()
                .sorted(Comparator.comparing(Student::getId)).collect(Collectors.toList());

        List<StudentRecord> actualStudentRecord = new ArrayList<>();
        actualStudentRecord.add(recordMapper.toRecord(allStudentRecord.get(allStudentRecord.size() - 1)));
        actualStudentRecord.add(recordMapper.toRecord(allStudentRecord.get(allStudentRecord.size() - 2)));
        actualStudentRecord.add(recordMapper.toRecord(allStudentRecord.get(allStudentRecord.size() - 3)));
        actualStudentRecord.add(recordMapper.toRecord(allStudentRecord.get(allStudentRecord.size() - 4)));
        actualStudentRecord.add(recordMapper.toRecord(allStudentRecord.get(allStudentRecord.size() - 5)));

        StudentRecord[] requestStudentRecord = restTemplate
                .getForObject("http://localhost:" + port + "/student/5BiggerId",
                        StudentRecord[].class);

        List<StudentRecord> expectedListRecord = new ArrayList<>(List.of(requestStudentRecord));

        assertEquals(actualStudentRecord.size(), expectedListRecord.size());

        assertTrue(actualStudentRecord.stream()
                .map(StudentRecord::getName)
                .collect(Collectors.toList())
                .containsAll(
                        expectedListRecord.stream()
                                .map(StudentRecord::getName)
                                .collect(Collectors.toList())));

        assertTrue(actualStudentRecord.stream()
                .map(StudentRecord::getAge)
                .collect(Collectors.toList())
                .containsAll(
                        expectedListRecord.stream()
                                .map(StudentRecord::getAge)
                                .collect(Collectors.toList())));

        assertTrue(actualStudentRecord.stream()
                .map(StudentRecord::getFacultyId)
                .collect(Collectors.toList())
                .containsAll(
                        expectedListRecord.stream()
                                .map(StudentRecord::getFacultyId)
                                .collect(Collectors.toList())));

        assertTrue(actualStudentRecord.stream()
                .map(StudentRecord::getId)
                .collect(Collectors.toList())
                .containsAll(
                        expectedListRecord.stream()
                                .map(StudentRecord::getId)
                                .collect(Collectors.toList())));
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

    private Faculty generateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

}