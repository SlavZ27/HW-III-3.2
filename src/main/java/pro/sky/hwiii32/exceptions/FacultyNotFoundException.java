package pro.sky.hwiii32.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pro.sky.hwiii32.service.FacultyService;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FacultyNotFoundException extends RuntimeException{
    public FacultyNotFoundException(String message) {
        super(message);
        Logger logger = LoggerFactory.getLogger(FacultyService.class);
        logger.error("Faculty with id = " + message + " not found");
    }
}
