package side.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByIdAndMember(Long id, Member member);

    List<Todo> findByMemberAndTodoType(Member member, TodoType todoType);
}
