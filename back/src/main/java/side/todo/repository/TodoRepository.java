package side.todo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import side.todo.domain.Member;
import side.todo.domain.Todo;
import side.todo.domain.TodoType;
import side.todo.dto.todo.TodoRespDto;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByIdAndMember(Long id, Member member);

    @Query("""
        SELECT new side.todo.dto.todo.TodoRespDto(
                t.id,
                t.title,
                t.content,
                t.completed,
                new side.todo.dto.todoType.ToDoTypeRespDto(
                        t.todoType.id,
                        t.todoType.title
                        )
                )
          FROM Todo t
         WHERE t.member = :member
           AND (:todoTypeId = 0 OR t.todoType.id = :todoTypeId)
         ORDER BY t.completed
        """)
    List<TodoRespDto> findByMemberAndTodoType(@Param("member") Member member, @Param("todoTypeId") Long todoTypeId);

    List<Todo> findByTodoTypeAndMember(TodoType todoType, Member member);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Todo t WHERE t.id IN :findTodoIds")
    void deleteTodo(@Param("ids") List<Long> findTodoIds);
}
