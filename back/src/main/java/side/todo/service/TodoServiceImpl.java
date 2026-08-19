package side.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.dto.todo.*;
import side.todo.exception.CustomNotFoundException;
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
    public TodoSaveRespDto todoSave(TodoSaveReqDto todoSaveReqDto, Member member) {

        TodoType findTodoType = todoTypeRepository.findByMemberAndTitle(member, todoSaveReqDto.getTodoType())
                .orElseThrow(() -> new CustomNotFoundException("Todo 타입을 확인해주세요."));

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
    public TodoUpdateRespDto todoUpdate(TodoUpdateReqDto todoUpdateReqDto, Member member) {
        Todo findTodo = todoRepository.findByIdAndMember(todoUpdateReqDto.getTodoId(), member)
                .orElseThrow(() -> new CustomNotFoundException("Todo를 찾을 수 없습니다."));

        TodoType findTodoType = todoTypeRepository.findByMemberAndTitle(member, todoUpdateReqDto.getTodoType())
                    .orElseThrow(() -> new CustomNotFoundException("Todo 타입을 확인해주세요."));

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
    public void todoDelete(Long todoId, Member member) {
        Todo findTodo = todoRepository.findByIdAndMember(todoId, member)
                .orElseThrow(() -> new CustomNotFoundException("Todo를 찾을 수 없습니다."));

        todoRepository.delete(findTodo);
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
                .orElseThrow(() -> new CustomNotFoundException("Todo를 찾을 수 없습니다."))
                .toDto();
    }

    @Override
    public List<TodoRespDto> searchTodos(Long todoTypeId, Member member) {
        TodoType findTodoType = todoTypeRepository.findByIdAndMember(todoTypeId, member)
                .orElseThrow(()-> new CustomNotFoundException("TodoType을 찾을 수 없습니다."));

        return todoRepository.findByMemberAndTodoType(member, findTodoType)
                .stream()
                .map(Todo::toDto)
                .collect(Collectors.toList());
    }

}
