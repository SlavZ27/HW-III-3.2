package pro.sky.hwiii32.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pro.sky.hwiii32.service.AvatarService;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AvatarNotFoundException extends RuntimeException {

    public AvatarNotFoundException(String message) {
        super(message);
        Logger logger = LoggerFactory.getLogger(AvatarService.class);
        logger.error("Avatar with id = " + message + " not found");
    }
}
