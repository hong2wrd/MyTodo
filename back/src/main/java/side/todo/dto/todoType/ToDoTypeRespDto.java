package side.todo.dto.todoType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoTypeRespDto {
    private Long todoTypeId;
    private String todoTypeTitle;
}
