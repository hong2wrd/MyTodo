package side.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.todo.domain.BatchHistory;

public interface BatchHistoryRepository extends JpaRepository<BatchHistory, Long> {
}
