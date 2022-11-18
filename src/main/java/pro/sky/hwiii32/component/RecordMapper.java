package pro.sky.hwiii32.component;

import org.springframework.stereotype.Component;
import pro.sky.hwiii32.exceptions.FacultyNotFoundException;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.AvatarRecord;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.FacultyRepository;

@Component
public class RecordMapper {

    private final FacultyRepository facultyRepository;

    public RecordMapper(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public AvatarRecord toRecord(Avatar avatar) {
        AvatarRecord avatarRecord = new AvatarRecord();
        avatarRecord.setId(avatar.getId());
        avatarRecord.setFileSize(avatar.getFileSize());
        avatarRecord.setMediaType(avatar.getMediaType());
        if (avatar.getStudent() != null) {
            avatarRecord.setStudentId(avatar.getStudent().getId());
        }
        avatarRecord.setUrl("http://Localhost:8080/avatar/"
                + avatar.getStudent().getId() +
                "/avatar-db");
        return avatarRecord;
    }

    public StudentRecord toRecord(Student student) {
        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setId(student.getId());
        studentRecord.setName(student.getName());
        studentRecord.setAge(student.getAge());
        if (student.getFaculty() != null) {
            studentRecord.setFacultyId(student.getFaculty().getId());
        }
        return studentRecord;
    }

    public FacultyRecord toRecord(Faculty faculty) {
        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setId(faculty.getId());
        facultyRecord.setName(faculty.getName());
        facultyRecord.setColor(faculty.getColor());
        return facultyRecord;
    }

    public Student toEntity(StudentRecord studentRecord) {
        Student student = new Student();
        student.setId(studentRecord.getId());
        student.setName(studentRecord.getName());
        student.setAge(studentRecord.getAge());
        if (studentRecord.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(
                            studentRecord.getFacultyId())
                    .orElseThrow(() -> new FacultyNotFoundException(String.valueOf(studentRecord.getFacultyId())));
            student.setFaculty(faculty);
        }
        return student;
    }

    public Faculty toEntity(FacultyRecord facultyRecord) {
        Faculty faculty = new Faculty();
        faculty.setId(facultyRecord.getId());
        faculty.setName(facultyRecord.getName());
        faculty.setColor(facultyRecord.getColor());
        return faculty;
    }


}
