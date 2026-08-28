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
import side.todo.repository.MemberRepository;
import side.todo.repository.TodoRepository;
import side.todo.repository.TodoTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final TodoTypeRepository todoTypeRepository;

    /**
     * 투두 저장
     * @param todoSaveReqDto
     * @return TodoSaveRespDto
     */
    @Override
    public TodoSaveRespDto saveTodo(TodoSaveReqDto todoSaveReqDto, String memberId) {

        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoSaveReqDto.getTodoType(), findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        Todo saveTodo = todoRepository.save(Todo.create(todoSaveReqDto.getTitle(), todoSaveReqDto.getContent(), findTodoType, findMember));

        return TodoSaveRespDto.builder()
                .memberId(saveTodo.getMember().getMemberId())
                .todoId(saveTodo.getId())
                .title(saveTodo.getTitle())
                .content(saveTodo.getContent())
                .todoType(Optional.ofNullable(todoSaveReqDto.getTodoType()).orElse(0L))
                .completed(saveTodo.isCompleted())
                .build();
    }

    /**
     * 투두 변경
     * @param todoUpdateReqDto
     * @param memberId
     * @return TodoUpdateRespDto
     */
    @Override
    public TodoUpdateRespDto updateTodo(TodoUpdateReqDto todoUpdateReqDto, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        Todo findTodo = todoRepository.findByIdAndMember(todoUpdateReqDto.getTodoId(), findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND));

        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoUpdateReqDto.getTodoType(), findMember)
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
     * @param memberId
     */
    @Override
    public void deleteTodo(Long todoId, String memberId) {

        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        Todo findTodo = todoRepository.findByIdAndMember(todoId, findMember)
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
     * @param memberId
     * @return TodoSearchRespDto
     */
    @Override
    public TodoRespDto searchTodo(Long todoId, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        return todoRepository.findByIdAndMember(todoId, findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND))
                .toDto();
    }

    @Override
    public List<TodoRespDto> searchTodos(Long todoTypeId, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        return todoRepository.findByMemberAndTodoType(findMember, todoTypeId);
    }

    @Override
    public void completeTodo(Long todoId, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        Todo findTodo = todoRepository.findByIdAndMember(todoId, findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND));

        findTodo.completed();
    }

    @Override
    public void changeTodoType(Long todoId, Long todoTypeId, String memberId) {
        Member findMember = memberRepository.findByMemberIdAndRetired(memberId, false)
                .orElseThrow(() -> new ApiException(ErrorCode.MEM_NOT_FOUND));

        Todo findTodo = todoRepository.findByIdAndMember(todoId, findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_NOT_FOUND));

        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoTypeId, findMember)
                .orElseThrow(() -> new ApiException(ErrorCode.TODO_TYPE_NOT_FOUND));

        findTodo.changeTodoType(findTodoType);
    }

}
