package side.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.TodoType;
import side.todo.dto.todoType.ToDoTypeRespDto;
import side.todo.dto.todoType.TodoTypeSaveReqDto;
import side.todo.dto.todoType.TodoTypeUpdateReqDto;
import side.todo.exception.ApiException;
import side.todo.exception.ErrorCode;
import side.todo.repository.TodoTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoTypeServiceImpl implements TodoTypeService {

    private final TodoService todoService;
    private final TodoTypeRepository todoTypeRepository;

    @Override
    public List<ToDoTypeRespDto> searchTodoTypes(Member member) {
        return todoTypeRepository.findByMember(member)
                .stream()
                .map(TodoType::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ToDoTypeRespDto saveTodoType(TodoTypeSaveReqDto todoTypeSaveReqDto, Member member) {
        if(!todoTypeSaveReqDto.getMemberId().equals(member.getMemberId())) {
            throw new ApiException(ErrorCode.MEM_CONFIRM);
        }

        return todoTypeRepository.save(TodoType.create(todoTypeSaveReqDto.getTitle(), member))
                .toDto();
    }

    @Override
    public ToDoTypeRespDto updateTodoType(TodoTypeUpdateReqDto todotypeUpdateReqDto, Member member) {
        if(!todotypeUpdateReqDto.getMemberId().equals(member.getMemberId())) {
            throw new ApiException(ErrorCode.MEM_CONFIRM);
        }

        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todotypeUpdateReqDto.getTodoTypeId(), member)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        findTodoType.update(todotypeUpdateReqDto.getTitle());

        return findTodoType.toDto();
    }

    @Override
    public void deleteTodoType(Long todoTypeId, Member member) {
        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoTypeId, member)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        todoService.deleteTodoByTodoTypeAndMember(findTodoType, member);

        todoTypeRepository.delete(findTodoType);
    }
}
