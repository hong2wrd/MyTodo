package side.todo.service;

import side.todo.domain.Member;
import side.todo.dto.todo.*;

import java.util.List;

public interface TodoService {
    TodoSaveRespDto todoSave(TodoSaveReqDto todoSaveReqDto, Member member);

    TodoUpdateRespDto todoUpdate(TodoUpdateReqDto todoUpdateReqDto, Member member);

    void todoDelete(Long todoId, Member member);

    TodoRespDto searchTodo(Long todoId, Member member);

    List<TodoRespDto> searchTodos(Long todoTypeId, Member member);
}
