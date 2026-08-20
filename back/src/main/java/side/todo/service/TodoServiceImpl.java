package side.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.dto.todo.*;
import side.todo.exception.ApiException;
import side.todo.exception.ErrorCode;
import side.todo.repository.TodoRepository;
import side.todo.repository.TodoTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoTypeRepository todoTypeRepository;

    /**
     * 투두 저장
     * @param todoSaveReqDto
     * @param member
     * @return TodoSaveRespDto
     */
    @Override
    public TodoSaveRespDto saveTodo(TodoSaveReqDto todoSaveReqDto, Member member) {

        TodoType findTodoType = todoTypeRepository.findByMemberAndTitle(member, todoSaveReqDto.getTodoType())
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        Todo saveTodo = todoRepository.save(Todo.create(todoSaveReqDto.getTitle(), todoSaveReqDto.getContent(), findTodoType, member));

        return TodoSaveRespDto.builder()
                .memberId(saveTodo.getMember().getMemberId())
                .todoId(saveTodo.getId())
                .title(saveTodo.getTitle())
                .content(saveTodo.getContent())
                .todoType(Optional.ofNullable(todoSaveReqDto.getTodoType()).orElse(""))
                .completed(saveTodo.isCompleted())
                .build();
    }

    /**
     * 투두 변경
     * @param todoUpdateReqDto
     * @param member
     * @return TodoUpdateRespDto
     */
    @Override
    public TodoUpdateRespDto updateTodo(TodoUpdateReqDto todoUpdateReqDto, Member member) {
        Todo findTodo = todoRepository.findByIdAndMember(todoUpdateReqDto.getTodoId(), member)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND));

        TodoType findTodoType = todoTypeRepository.findByMemberAndTitle(member, todoUpdateReqDto.getTodoType())
                    .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        findTodo.update(todoUpdateReqDto.getTitle(), todoUpdateReqDto.getContent(), findTodoType);

        return TodoUpdateRespDto.builder()
                .todoId(findTodo.getId())
                .title(findTodo.getTitle())
                .content(findTodo.getContent())
                .todoType(findTodoType.toDto())
                .build();
    }

    /**
     * 투두 삭제
     * @param todoId
     * @param member
     */
    @Override
    public void deleteTodo(Long todoId, Member member) {
        Todo findTodo = todoRepository.findByIdAndMember(todoId, member)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND));

        todoRepository.delete(findTodo);
    }

    @Override
    public void deleteTodoByTodoTypeAndMember(TodoType todoType, Member member) {
        List<Long> findTodoIds = todoRepository.findByTodoTypeAndMember(todoType, member)
                .stream()
                .map(Todo::getId)
                .collect(Collectors.toList());

        todoRepository.deleteTodo(findTodoIds);
    }


    /**
     * 단건 조회
     * @param todoId
     * @param member
     * @return TodoSearchRespDto
     */
    @Override
    public TodoRespDto searchTodo(Long todoId, Member member) {
        return todoRepository.findByIdAndMember(todoId, member)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND))
                .toDto();
    }

    @Override
    public List<TodoRespDto> searchTodos(Long todoTypeId, Member member) {
        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoTypeId, member)
                .orElseThrow(()-> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        return todoRepository.findByMemberAndTodoType(member, findTodoType)
                .stream()
                .map(Todo::toDto)
                .collect(Collectors.toList());
    }

}
