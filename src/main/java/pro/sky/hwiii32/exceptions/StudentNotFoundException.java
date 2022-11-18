package pro.sky.hwiii32.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pro.sky.hwiii32.service.StudentService;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(String message) {
        super(message);
        Logger logger = LoggerFactory.getLogger(StudentService.class);
        logger.error("Student with id = " + message + " not found");

    }
}
