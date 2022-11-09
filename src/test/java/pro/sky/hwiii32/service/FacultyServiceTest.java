package pro.sky.hwiii32.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.exceptions.FacultyNotFoundException;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.FacultyRepository;

import java.util.*;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private RecordMapper recordMapper;

    private FacultyService facultyService;
    private Student student;
    private Faculty faculty;
    private StudentRecord studentRecord;
    private FacultyRecord facultyRecord;

    @BeforeEach
    void set() {
        facultyService = new FacultyService(facultyRepository, studentService, recordMapper);

        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("22222");
        faculty.setColor("blue");

        facultyRecord = new FacultyRecord();
        facultyRecord.setId(1L);
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

        faculty.setStudents(new HashSet<>(Set.of(student)));
//        facultyRecord.setStudents(new HashSet<>(Set.of(student)));
    }


    @Test
    void findFacultyByIdTest() {

        when(facultyRepository.findById(-1L)).thenThrow(FacultyNotFoundException.class);
        Optional<Faculty> optionalFaculty = Optional.of(faculty);
        when(facultyRepository.findById(1L)).thenReturn(optionalFaculty);

        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.findFacultyById(-1L));
        assertEquals(facultyService.findFacultyById(1L), faculty);
    }

    @Test
    void createFacultyTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);
        when(recordMapper.toEntity(facultyRecord)).thenReturn(faculty);
        when(facultyRepository.save(faculty)).thenReturn(faculty);

        assertEquals(facultyService.createFaculty(facultyRecord), facultyRecord);
    }

    @Test
    void readFacultyTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);

        when(facultyRepository.findById(-1L)).thenThrow(FacultyNotFoundException.class);
        Optional<Faculty> optionalFaculty = Optional.of(faculty);
        when(facultyRepository.findById(1L)).thenReturn(optionalFaculty);

        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.readFaculty(-1L));
        assertEquals(facultyService.readFaculty(1L), facultyRecord);
    }

    @Test
    void updateFacultyTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);

        Optional<Faculty> optionalFaculty = Optional.of(faculty);
        when(facultyRepository.findById(1L)).thenReturn(optionalFaculty);
        when(facultyRepository.save(faculty)).thenReturn(faculty);

        assertEquals(facultyService.updateFaculty(facultyRecord), facultyRecord);
    }

    @Test
    void deleteFacultyTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);

        Optional<Faculty> optionalFaculty = Optional.of(faculty);
        when(facultyRepository.findById(1L)).thenReturn(optionalFaculty);

        assertEquals(facultyService.deleteFaculty(1), facultyRecord);
    }

    @Test
    void getAllTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);
        when(facultyRepository.findAll()).thenReturn(new ArrayList<>(List.of(faculty)));
        assertIterableEquals(facultyService.getAll(), new ArrayList<>(List.of(facultyRecord)));
    }

    @Test
    void getFacultiesWithEqualColorTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);
        when(facultyRepository.findFacultiesByColor(anyString()))
                .thenReturn(new ArrayList<>(List.of(faculty)));
        assertIterableEquals(facultyService.getFacultiesWithEqualColor("blue"), new ArrayList<>(List.of(facultyRecord)));
    }

    @Test
    void getFacultiesWithEqualNameOrColorTest() {
        when(recordMapper.toRecord(faculty)).thenReturn(facultyRecord);
        when(facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase("str", "blue"))
                .thenReturn(new ArrayList<>(List.of(faculty)));
        assertIterableEquals(facultyService
                .getFacultiesWithEqualNameOrColor("str", "blue"), new ArrayList<>(List.of(facultyRecord)));
    }

    @Test
    void getStudentsWithEqualFacultyTest() {
        when(recordMapper.toRecord(student)).thenReturn(studentRecord);
        when(studentService.findStudentsByFaculty(1L))
                .thenReturn(new ArrayList<>(List.of(student)));
        assertIterableEquals(facultyService
                .getStudentsWithEqualFaculty(1L), new ArrayList<>(List.of(studentRecord)));
    }
}