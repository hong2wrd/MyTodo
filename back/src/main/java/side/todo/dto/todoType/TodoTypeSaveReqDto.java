package side.todo.dto.todoType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoTypeSaveReqDto {
    @NotBlank(message = "Todo Type 제목을 작성해주세요.")
    @Size(max = 10)
    private String todoTypeTitle;
}
