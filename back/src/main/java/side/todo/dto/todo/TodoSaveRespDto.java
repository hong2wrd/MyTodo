package side.todo.dto.todo;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoSaveRespDto {
    private String memberId;
    private Long todoId;
    private String title;
    private String content;
    private boolean completed;
    private Long todoType;
}
