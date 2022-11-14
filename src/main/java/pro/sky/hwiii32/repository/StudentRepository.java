package pro.sky.hwiii32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.hwiii32.model.Student;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findStudentsByAge(int age);

    List<Student> findStudentsByAgeBetween(Integer ageFrom, Integer ageTo);

    List<Student> findStudentsByFacultyId(Long facultyId);

    @Query(value = "select count(*) from student",nativeQuery = true)
    Long getCountStudent();

    @Query(value = "select avg(age) from student",nativeQuery = true)
    Float getMidAgeOfStudent();

    @Query(value = "select * from student order by id DESC limit 5",nativeQuery = true)
    List<Student> get5StudentWithBiggerId();

}
