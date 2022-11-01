package pro.sky.hwiii32.service;

import org.springframework.stereotype.Service;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsWithEqualAge(int age) {
//        return studentRepository.findAll().stream()
//                .filter(student -> student.getAge() == age)
//                .collect(Collectors.toList());
        return studentRepository.findStudentsByAge(age);
    }

}
