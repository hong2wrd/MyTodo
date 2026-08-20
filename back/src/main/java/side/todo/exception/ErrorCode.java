package side.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    MEM_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    MEM_CONFLICT(HttpStatus.CONFLICT, "중복된 회원입니다."),
    MEM_CONFIRM(HttpStatus.BAD_REQUEST, "사용자 정보 확인이 필요합니다."),

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "Todo 내역을 찾을 수 없습니다."),

    TODO_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Todo 타입을 확인바랍니다."),
    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
