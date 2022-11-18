package pro.sky.hwiii32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.hwiii32.model.Faculty;

import java.util.List;


@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findFacultiesByColor(String needColor);

    List<Faculty> findFacultiesByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}
