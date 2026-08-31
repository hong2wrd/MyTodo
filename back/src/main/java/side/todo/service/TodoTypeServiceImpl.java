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
import side.todo.repository.MemberRepository;
import side.todo.repository.TodoTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoTypeServiceImpl implements TodoTypeService {

    private final TodoService todoService;
    private final TodoTypeRepository todoTypeRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ToDoTypeRespDto> searchTodoTypes(String memberId) {
        return todoTypeRepository.findByMemberId(memberId)
                .stream()
                .map(TodoType::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ToDoTypeRespDto saveTodoType(TodoTypeSaveReqDto todoTypeSaveReqDto, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        return todoTypeRepository.save(TodoType.create(todoTypeSaveReqDto.getTodoTypeTitle(), findMember))
                .toDto();
    }

    @Override
    public ToDoTypeRespDto updateTodoType(TodoTypeUpdateReqDto todotypeUpdateReqDto, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todotypeUpdateReqDto.getTodoTypeId(), findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        findTodoType.update(todotypeUpdateReqDto.getTodoTypeTitle());

        return findTodoType.toDto();
    }

    @Override
    public void deleteTodoType(Long todoTypeId, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoTypeId, findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        todoService.deleteTodoByTodoTypeAndMember(findTodoType, findMember);

        todoTypeRepository.delete(findTodoType);
    }
}
