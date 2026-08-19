package side.todo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByIdAndMember(Long id, Member member);

    List<Todo> findByMemberAndTodoType(Member member, TodoType todoType);

    List<Todo> findByTodoTypeAndMember(TodoType todoType, Member member);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Todo t WHERE t.id IN :findTodoIds")
    void deleteTodo(@Param("ids") List<Long> findTodoIds);
}
