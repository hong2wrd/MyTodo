package side.todo.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiException extends RuntimeException {

    private Map<String, String> errorMap = new HashMap<>();

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
