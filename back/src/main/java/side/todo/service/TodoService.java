package side.todo.service;

import side.todo.domain.Member;
import side.todo.domain.TodoType;
import side.todo.dto.todo.*;
import side.todo.repository.TodoTypeRepository;

import java.util.List;

public interface TodoService {
    TodoSaveRespDto saveTodo(TodoSaveReqDto todoSaveReqDto, Member member);

    TodoUpdateRespDto updateTodo(TodoUpdateReqDto todoUpdateReqDto, Member member);

    void deleteTodo(Long todoId, Member member);

    void deleteTodoByTodoTypeAndMember(TodoType todoType, Member member);

    TodoRespDto searchTodo(Long todoId, Member member);

    List<TodoRespDto> searchTodos(Long todoTypeId, Member member);
}
