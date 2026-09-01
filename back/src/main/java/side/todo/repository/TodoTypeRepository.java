package side.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import side.todo.domain.Member;
import side.todo.domain.TodoType;

import java.util.List;
import java.util.Optional;

public interface TodoTypeRepository extends JpaRepository<TodoType, Long> {

    Optional<TodoType> findByIdAndMember(Long todoTypeId, Member member);

    @Query("""
        SELECT tt
        FROM TodoType tt
        WHERE tt.member.retired = false
          AND tt.member.memberId = :memberId
    """)
    List<TodoType> findByMemberId(@Param("memberId") String memberId);

    @Query("""
        SELECT tt
        FROM TodoType tt
        WHERE tt.member IN (:members)
    """)
    List<TodoType> findByMembers(@Param("members") List<Member> members);
}
