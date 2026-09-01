package side.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.todo.aop.BatchLogging;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.repository.MemberRepository;
import side.todo.repository.TodoRepository;
import side.todo.repository.TodoTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBatchServiceImpl implements MemberBatchService {

    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final TodoTypeRepository todoTypeRepository;

    @Override
    @Transactional
    @BatchLogging("DELETE_RETIRED_MEMBERS")
    public int deleteRetiredMembers() {
        List<Member> findMembers = memberRepository.findByRetiredTrue();

        if(findMembers.isEmpty()) return 0;

        List<Todo> findTodos = todoRepository.findByMembers(findMembers);

        List<TodoType> findTodoType = todoTypeRepository.findByMembers(findMembers);

        todoRepository.deleteAll(findTodos);

        todoTypeRepository.deleteAll(findTodoType);

        memberRepository.deleteAll(findMembers);

        return findMembers.size();
    }
}
