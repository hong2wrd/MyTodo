package side.todo.service;

import side.todo.domain.Member;
import side.todo.domain.TodoType;
import side.todo.dto.todo.*;

import java.util.List;

public interface TodoService {
    TodoSaveRespDto saveTodo(TodoSaveReqDto todoSaveReqDto);

    TodoUpdateRespDto updateTodo(TodoUpdateReqDto todoUpdateReqDto, Member member);

    void deleteTodo(Long todoId, String memberId);

    void deleteTodoByTodoTypeAndMember(TodoType todoType, Member member);

    TodoRespDto searchTodo(Long todoId, Member member);

    List<TodoRespDto> searchTodos(Long todoTypeId, String memberId);

    void completeTodo(Long todoId, String memberId);

    void changeTodoType(Long todoId, Long todoTypeId, String memberId);

}
