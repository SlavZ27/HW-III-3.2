package pro.sky.hwiii32.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AvatarNotFoundException extends RuntimeException{
    public AvatarNotFoundException(String message) {
        super(message);
    }
}
