package side.todo.dto.todo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TodoDeleteReqDto {
    private String memberId;
    private List<Long> todoIds;
}
