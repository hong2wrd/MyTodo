package side.todo.dto.todo;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoSaveReqDto {

    @Size(max = 20, message = "제목은 20자리까지 가능합니다.")
    private String title;

    @Size(max = 100, message = "본문은 100자까지 가능합니다.")
    private String content;

    private Long todoType;
}
