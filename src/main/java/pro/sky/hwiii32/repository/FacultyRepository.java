package pro.sky.hwiii32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hwiii32.model.Faculty;


public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
