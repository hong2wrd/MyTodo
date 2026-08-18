package side.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiException extends RuntimeException {

    private Map<String, String> errorMap = new HashMap<>();
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ApiException(String message) {
        super(message);
    }
    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message, Map<String, String> errorMap, HttpStatus httpStatus) {
        super(message);
        this.errorMap = errorMap;
        this.httpStatus = httpStatus;
    }
}
