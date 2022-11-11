package pro.sky.hwiii32.service;

import org.springframework.stereotype.Service;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.exceptions.FacultyNotFoundException;
import pro.sky.hwiii32.exceptions.StudentNotFoundException;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final RecordMapper recordMapper;


    public StudentService(StudentRepository studentRepository, RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.recordMapper = recordMapper;
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(String.valueOf(id)));
    }

    public StudentRecord createStudent(StudentRecord studentRecord) {
        return recordMapper.toRecord(
                studentRepository.save(
                        recordMapper.toEntity(studentRecord)));
    }

    public StudentRecord readStudent(long id) {
        return recordMapper.toRecord(findStudentById(id));
    }

    public StudentRecord updateStudent(StudentRecord studentRecord) {
        Student updateStudent = findStudentById(studentRecord.getId());
        updateStudent.setName(studentRecord.getName());
        updateStudent.setAge(studentRecord.getAge());
        return recordMapper.toRecord(studentRepository.save(updateStudent));
    }

    public StudentRecord deleteStudent(long id) {
        Student deleteStudent = findStudentById(id);
        studentRepository.deleteById(id);
        return recordMapper.toRecord(deleteStudent);
    }

    public Collection<StudentRecord> getAll() {
        return studentRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsWithEqualAge(int age) {
        return studentRepository.findStudentsByAge(age).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsWithBetweenAge(Integer ageFrom, Integer ageTo) {
        return studentRepository.findStudentsByAgeBetween(ageFrom, ageTo).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord getFacultyByStudent(Long studentId) {
        return recordMapper.toRecord(Optional.ofNullable(
                findStudentById(studentId).getFaculty()).orElseThrow(() -> new FacultyNotFoundException("")));
    }

    public Collection<Student> findStudentsByFaculty(Long facultyId) {
        return studentRepository.findStudentsByFacultyId(facultyId);
    }

    public Long getCountStudent() {
        return studentRepository.getCountStudent();
    }

    public Float getMidAgeOfStudent() {
        return studentRepository.getMidAgeOfStudent();
    }

    public List<StudentRecord> get5StudentWithBiggerId() {
        return studentRepository.get5StudentWithBiggerId().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}
