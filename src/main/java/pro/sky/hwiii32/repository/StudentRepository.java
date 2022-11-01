package pro.sky.hwiii32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hwiii32.model.Student;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findStudentsByAge(int age);
}
