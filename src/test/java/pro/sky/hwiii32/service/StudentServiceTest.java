package pro.sky.hwiii32.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.exceptions.StudentNotFoundException;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private RecordMapper recordMapper;

    private StudentService studentService;
    private Student student;
    private Faculty faculty;
    private StudentRecord studentRecord;
    private FacultyRecord facultyRecord;

    @BeforeEach
    void set() {
        studentService = new StudentService(studentRepository, recordMapper);

        faculty = new Faculty();
        faculty.setId(2L);
        faculty.setName("22222");
        faculty.setColor("blue");

        facultyRecord = new FacultyRecord();
        facultyRecord.setId(2L);
        facultyRecord.setName("22222");
        facultyRecord.setColor("blue");

        student = new Student();
        student.setId(2L);
        student.setName("11111");
        student.setAge(5);
        student.setFaculty(faculty);

        studentRecord = new StudentRecord();
        studentRecord.setId(2L);
        studentRecord.setName("11111");
        studentRecord.setAge(5);
        studentRecord.setFaculty(faculty);

//        faculty.setStudents(new HashSet<>(Set.of(student)));
//        facultyRecord.setStudents(new HashSet<>(Set.of(student)));
    }


    @Test
    void findStudentByIdTest() {

        when(studentRepository.findById(-2L)).thenThrow(StudentNotFoundException.class);
        Optional<Student> optionalStudent = Optional.of(student);
        when(studentRepository.findById(2L)).thenReturn(optionalStudent);

        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.findStudentById(-2L));
        assertEquals(studentService.findStudentById(2L), student);
    }

    @Test
    void createStudentTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);
        when(recordMapper.toEntity(studentRecord)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);

        assertEquals(studentService.createStudent(studentRecord), studentRecord);
    }

    @Test
    void readFacultyTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);

        when(studentRepository.findById(-2L)).thenThrow(StudentNotFoundException.class);
        Optional<Student> optionalStudent = Optional.of(student);
        when(studentRepository.findById(2L)).thenReturn(optionalStudent);

        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.readStudent(-2L));
        assertEquals(studentService.readStudent(2L), studentRecord);
    }

    @Test
    void updateFacultyTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);

        Optional<Student> optionalStudent = Optional.of(student);
        when(studentRepository.findById(2L)).thenReturn(optionalStudent);
        when(studentRepository.save(student)).thenReturn(student);

        assertEquals(studentService.updateStudent(studentRecord), studentRecord);
    }

    @Test
    void deleteFacultyTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);
        Optional<Student> optionalStudent = Optional.of(student);
        when(studentRepository.findById(2L)).thenReturn(optionalStudent);

        assertEquals(studentService.deleteStudent(2L), studentRecord);
    }

    @Test
    void getAllTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);
        when(studentRepository.findAll()).thenReturn(new ArrayList<>(List.of(student)));
        assertIterableEquals(studentService.getAll(), new ArrayList<>(List.of(studentRecord)));
    }

    @Test
    void getStudentsWithEqualAgeTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);

        when(studentRepository.findStudentsByAge(5))
                .thenReturn(new ArrayList<>(List.of(student)));

        assertIterableEquals(studentService.getStudentsWithEqualAge(5)
                , new ArrayList<>(List.of(studentRecord)));
    }

    @Test
    void getStudentsWithBetweenAgeTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);

        when(studentRepository.findStudentsByAgeBetween(4, 6))
                .thenReturn(new ArrayList<>(List.of(student)));

        assertIterableEquals(studentService.getStudentsWithBetweenAge(4, 6)
                , new ArrayList<>(List.of(studentRecord)));
    }

    @Test
    void getFacultyByStudentTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);

        Optional<Student> optionalStudent = Optional.of(student);
        when(studentRepository.findById(2L)).thenReturn(optionalStudent);

        assertEquals(studentService.getFacultyByStudent(2L)
                , facultyRecord);
    }

    @Test
    void findStudentsByFacultyTest() {
        when(studentRepository.findStudentsByFacultyId(1L))
                .thenReturn(new ArrayList<>(List.of(student)));

        assertIterableEquals(studentService.findStudentsByFaculty(1L)
                , new ArrayList<>(List.of(student)));
    }
}