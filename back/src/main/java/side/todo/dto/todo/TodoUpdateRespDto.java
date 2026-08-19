package side.todo.dto.todo;

import lombok.*;
import side.todo.dto.todoType.ToDoTypeRespDto;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateRespDto {
    private String memberId;
    private Long todoId;
    private String title;
    private String content;
    private boolean completed;
    private ToDoTypeRespDto todoType;
}
