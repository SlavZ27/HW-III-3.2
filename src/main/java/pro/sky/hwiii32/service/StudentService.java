package pro.sky.hwiii32.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
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
    private final static Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final Object flag = new Object();
    private int count;


    public StudentService(StudentRepository studentRepository, RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.recordMapper = recordMapper;
    }

    public Student findStudentById(Long id) {
        logger.info("Was invoked method findStudentById for find student by id");

        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isPresent()) {
            logger.debug("Student with id = {} was found", id);
        }

        return studentOptional.orElseThrow(() -> new StudentNotFoundException(String.valueOf(id)));
    }

    public StudentRecord createStudent(StudentRecord studentRecord) {
        logger.info("Was invoked method createStudent for create student");
        Student studentEntity = recordMapper.toEntity(studentRecord);
        Student studentCreate = new Student();

        studentCreate.setName(studentEntity.getName());
        studentCreate.setAge(studentEntity.getAge());
        if (studentEntity.getFaculty() != null) {
            studentCreate.setFaculty(studentEntity.getFaculty());
        }
        return recordMapper.toRecord(
                studentRepository.save(studentCreate));
    }

    public StudentRecord readStudent(long id) {
        logger.info("Was invoked method readStudent for read student by id = {}", id);
        return recordMapper.toRecord(findStudentById(id));
    }

    public StudentRecord updateStudent(StudentRecord studentRecord) {
        logger.info("Was invoked method updateStudent for update student");
        Student updateStudent = findStudentById(studentRecord.getId());
        updateStudent.setName(studentRecord.getName());
        updateStudent.setAge(studentRecord.getAge());
        return recordMapper.toRecord(studentRepository.save(updateStudent));
    }

    public StudentRecord deleteStudent(long id) {
        logger.info("Was invoked method deleteStudent for delete student by id = {}", id);
        Student deleteStudent = findStudentById(id);
        studentRepository.deleteById(id);
        return recordMapper.toRecord(deleteStudent);
    }

    public Collection<StudentRecord> getAll() {
        logger.info("Was invoked method getAll for send all students");
        return studentRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsWithEqualAge(int age) {
        logger.info("Was invoked method getStudentsWithEqualAge " +
                "for send all students with the required age = {}", age);
        return studentRepository.findStudentsByAge(age).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsWithBetweenAge(Integer ageFrom, Integer ageTo) {
        logger.info("Was invoked method getStudentsWithEqualAge " +
                "for send all students with ages from = {} and to = {}", ageFrom, ageTo);
        return studentRepository.findStudentsByAgeBetween(ageFrom, ageTo).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord getFacultyByStudent(Long studentId) {
        logger.info("Was invoked method getFacultyByStudent " +
                "for send faculty of students with id = {}", studentId);
        return recordMapper.toRecord(Optional.ofNullable(
                findStudentById(studentId).getFaculty()).orElseThrow(() -> new FacultyNotFoundException("")));
    }

    public Long getCountStudent() {
        logger.info("Was invoked method getCountStudent for send the amount of students");
        return studentRepository.getCountStudent();
    }

    public Float getMidAgeOfStudent() {
        logger.info("Was invoked method getMidAgeOfStudent for send the middle age of students");
        return studentRepository.getMidAgeOfStudent();
    }

    public List<StudentRecord> get5StudentWithBiggerId() {
        logger.info("Was invoked method get5StudentWithBiggerId for send five students with bigger id");
        return studentRepository.get5StudentWithBiggerId().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public List<String> getStudentWithFirstCharOfName(Character firstChar) {
        logger.info("Was invoked method getStudentWithFirstCharOfName " +
                "for send list of students with first char = {}", firstChar);
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.substring(0, 1).equalsIgnoreCase(String.valueOf(firstChar)))
                .map(s -> StringUtils.capitalize(s.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Double getMidAgeOfAllStudents() {
        logger.info("Was invoked method getMidAgeOfAllStudents " +
                "for send average age of students");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow(() -> new NotFoundException("List is empty"));
    }

    public void sout() {
        logger.info("Was invoked method sout " +
                "for print names of students to console in different threads");
        List<Student> studentList = studentRepository.findAll();
        count = 0;
        printName(studentList, 0);
        new Thread(() -> {
            printName(studentList, 1);
            printName(studentList, 2);
        }).start();
        new Thread(() -> {
            printName(studentList, 3);
            printName(studentList, 4);
        }).start();
        printName(studentList, 5);
    }

    public void sout2() {
        logger.info("Was invoked method sout2 " +
                "for print names of students to console in different and synchronized threads");
        List<Student> studentList = studentRepository.findAll();
        count = 0;
        printName2(studentList);
        printName2(studentList);
        new Thread(() -> {
            printName2(studentList);
            printName2(studentList);
        }).start();
        new Thread(() -> {
            printName2(studentList);
            printName2(studentList);
        }).start();
    }

    private void printName2(List<Student> studentList) {
        if (studentList.size() - 1 < count) {
            throw new StudentNotFoundException(String.valueOf(count));
        }
        synchronized (flag) {
            System.out.println("studentList.get(" + count + ") = " + studentList.get(count).getName());
            count++;
        }
    }

    private void printName(List<Student> studentList, int id) {
        if (studentList.size() - 1 < id) {
            throw new StudentNotFoundException(String.valueOf(id));
        }
        System.out.println("studentList.get(" + id + ") = " + studentList.get(id).getName());
    }

}
