package pro.sky.hwiii32.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.repository.FacultyRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordMapperTest {

    @Mock
    private FacultyRepository facultyRepository;


    @InjectMocks
    private RecordMapper recordMapper;


    @Test
    void toRecordToEntityTest() {


        Faculty faculty = new Faculty();
        faculty.setId(2L);
        faculty.setName("22222");
        faculty.setColor("blue");

        Student student = new Student();
        student.setId(1L);
        student.setName("11111");
        student.setAge(5);
        student.setFaculty(faculty);

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));

        faculty.setStudents(new HashSet<>(Set.of(student)));

        assertEquals(recordMapper.toEntity(recordMapper.toRecord(student)), student);
        assertEquals(recordMapper.toEntity(recordMapper.toRecord(faculty)), faculty);
    }
}