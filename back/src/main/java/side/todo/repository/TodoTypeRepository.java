package side.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.todo.domain.Member;
import side.todo.domain.TodoType;

import java.util.Optional;

public interface TodoTypeRepository extends JpaRepository<TodoType, Long> {
    Optional<TodoType> findByMemberAndTitle(Member member, String title);

    Optional<TodoType> findByIdAndMember(Long todoTypeId, Member member);
}
