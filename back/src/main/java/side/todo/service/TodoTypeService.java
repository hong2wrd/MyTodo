package side.todo.service;

import side.todo.domain.Member;
import side.todo.dto.todoType.ToDoTypeRespDto;
import side.todo.dto.todoType.TodoTypeSaveReqDto;
import side.todo.dto.todoType.TodoTypeUpdateReqDto;

import java.util.List;

public interface TodoTypeService {

    List<ToDoTypeRespDto> searchTodoTypes(String memberId);

    ToDoTypeRespDto saveTodoType(TodoTypeSaveReqDto todoTypeSaveReqDto, String memberId);

    ToDoTypeRespDto updateTodoType(TodoTypeUpdateReqDto todotypeUpdateReqDto, String memberId);

    void deleteTodoType(Long todoTypeId, String memberId);
}
