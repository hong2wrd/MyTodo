package side.todo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import side.todo.domain.BatchHistory;
import side.todo.domain.enums.BatchStatus;
import side.todo.repository.BatchHistoryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchHistoryServiceImplTest {

    @Mock private BatchHistoryRepository batchHistoryRepository;
    @InjectMocks private BatchHistoryServiceImpl batchHistoryService;

    @Test
    void createsRunningHistory() {
        when(batchHistoryRepository.save(any(BatchHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BatchHistory history = batchHistoryService.start("DELETE_RETIRED_MEMBERS");

        assertThat(history.getBatchName()).isEqualTo("DELETE_RETIRED_MEMBERS");
        assertThat(history.getStatus()).isEqualTo(BatchStatus.RUNNING);
        assertThat(history.getProcessedCount()).isZero();
        assertThat(history.getStartedAt()).isNotNull();
    }

    @Test
    void updatesHistoryToSuccess() {
        BatchHistory history = mock(BatchHistory.class);
        when(batchHistoryRepository.findById(1L)).thenReturn(Optional.of(history));

        batchHistoryService.success(1L, 4);

        verify(history).success(4);
    }

    @Test
    void updatesHistoryToFailure() {
        BatchHistory history = mock(BatchHistory.class);
        when(batchHistoryRepository.findById(1L)).thenReturn(Optional.of(history));

        batchHistoryService.fail(1L, "delete failed");

        verify(history).fail("delete failed");
    }
}
