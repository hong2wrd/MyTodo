package side.todo.service;

import side.todo.domain.Member;
import side.todo.dto.todoType.ToDoTypeRespDto;
import side.todo.dto.todoType.TodoTypeSaveReqDto;
import side.todo.dto.todoType.TodoTypeUpdateReqDto;

import java.util.List;

public interface TodoTypeService {

    List<ToDoTypeRespDto> searchTodoTypes(Member member);

    ToDoTypeRespDto saveTodoType(TodoTypeSaveReqDto todoTypeSaveReqDto, Member member);

    ToDoTypeRespDto updateTodoType(TodoTypeUpdateReqDto todotypeUpdateReqDto, Member member);

    void deleteTodoType(Long todoTypeId, Member member);
}
