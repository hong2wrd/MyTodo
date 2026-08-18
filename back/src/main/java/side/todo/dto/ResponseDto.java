package side.todo.dto;

/**
 * @param code 1 : 성공, -1 실패
 */
public record ResponseDto<T>(Integer code, String msg, T data) {
    public ResponseDto(Integer code, String msg) {
        this(code, msg, null);
    }
}
