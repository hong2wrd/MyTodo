package side.todo.dto.todoType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoTypeSaveReqDto {
    @NotBlank(message = "사용자 Id를 확인해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "사용자 Id를 확인해 주세요.")
    private String memberId;

    @NotBlank(message = "Todo Type 제목을 작성해주세요.")
    @Size(max = 10)
    private String title;
}
