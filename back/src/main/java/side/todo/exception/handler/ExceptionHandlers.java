package side.todo.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import side.todo.dto.ResponseDto;
import side.todo.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> bindExceptionHandler(BindException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : e.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        ResponseDto<?> responseDto = new ResponseDto<>(-1, errorMap.isEmpty() ? "알 수 없는 에러" : "유효성검사 실패", errorMap);

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> apiExceptionHandler(ApiException e) {
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), e.getHttpStatus());
    }

}
