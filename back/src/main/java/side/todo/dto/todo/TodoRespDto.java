package side.todo.dto.todo;

import lombok.*;
import side.todo.dto.todoType.ToDoTypeRespDto;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoRespDto {

    private Long todoId;
    private String title;
    private String content;
    private ToDoTypeRespDto todoType;
}
