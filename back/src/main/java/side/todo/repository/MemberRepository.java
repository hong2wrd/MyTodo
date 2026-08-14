package side.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.todo.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserIdAndRetired(String memberId, boolean retired);
}
