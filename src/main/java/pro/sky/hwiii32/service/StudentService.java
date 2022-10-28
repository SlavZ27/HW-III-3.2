package pro.sky.hwiii32.service;

import org.springframework.stereotype.Service;
import pro.sky.hwiii32.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    public Student createStudent(Student Student) {
        Student.setId(++lastId);
        students.put(lastId, Student);
        return Student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student Student) {
        if (students.containsKey(Student.getId())) {
            students.put(Student.getId(), Student);
            return Student;
        }
        return null;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> getAll() {
        return new ArrayList<>(students.values());
    }

    public Collection<Student> getStudentsWithEqualAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

}
