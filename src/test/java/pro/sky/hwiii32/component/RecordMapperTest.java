package pro.sky.hwiii32.component;

import org.junit.jupiter.api.Test;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecordMapperTest {

    private final RecordMapper recordMapper = new RecordMapper();


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

        faculty.setStudents(new HashSet<>(Set.of(student)));

        assertEquals(recordMapper.toEntity(recordMapper.toRecord(student)), student);
        assertEquals(recordMapper.toEntity(recordMapper.toRecord(faculty)), faculty);
    }
}