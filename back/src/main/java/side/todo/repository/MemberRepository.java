package side.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.todo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberIdAndRetired(String memberId, boolean retired);

    List<Member> findByRetiredTrue();
}
