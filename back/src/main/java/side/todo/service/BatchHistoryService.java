package side.todo.service;

import side.todo.domain.BatchHistory;

public interface BatchHistoryService {
    BatchHistory start(String batchName);

    void success(Long batchHistoryId, int processedCount);

    void fail(Long batchHistoryId, String errorMsg);
}
