package side.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomNotFoundException extends ApiException {
    public CustomNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
