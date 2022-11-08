package pro.sky.hwiii32.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.model.Student;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findAvatarByStudent(Student student);

}
