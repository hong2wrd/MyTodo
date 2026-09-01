package side.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import side.todo.domain.BatchHistory;
import side.todo.repository.BatchHistoryRepository;

@Service
@RequiredArgsConstructor
public class BatchHistoryServiceImpl implements BatchHistoryService {

    private final BatchHistoryRepository batchHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public BatchHistory start(String batchName) {
        BatchHistory batchHistory = BatchHistory.of(batchName);
        return batchHistoryRepository.save(batchHistory);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void success(Long batchHistoryId, int processedCount) {
        BatchHistory batchHistory = batchHistoryRepository.findById(batchHistoryId)
                .orElseThrow();

        batchHistory.success(processedCount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void fail(Long batchHistoryId, String errorMsg) {
        BatchHistory batchHistory = batchHistoryRepository.findById(batchHistoryId)
                .orElseThrow();
        batchHistory.fail(errorMsg);
    }

}
