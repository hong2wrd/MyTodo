package side.todo.aop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import side.todo.domain.BatchHistory;
import side.todo.service.BatchHistoryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchAopTest {

    @Mock private BatchHistoryService batchHistoryService;
    @Mock private BatchHistory batchHistory;
    @InjectMocks private BatchAop batchAop;

    @Test
    void recordsSuccessAndReturnsOriginalResultThroughSpringProxy() {
        when(batchHistoryService.start("DELETE_RETIRED_MEMBERS")).thenReturn(batchHistory);
        when(batchHistory.getId()).thenReturn(1L);
        TestBatchTarget proxy = createProxy();

        int result = proxy.succeed();

        assertThat(result).isEqualTo(3);
        verify(batchHistoryService).success(1L, 3);
    }

    @Test
    void recordsFailureAndRethrowsOriginalExceptionThroughSpringProxy() {
        when(batchHistoryService.start("DELETE_RETIRED_MEMBERS")).thenReturn(batchHistory);
        when(batchHistory.getId()).thenReturn(1L);
        TestBatchTarget proxy = createProxy();

        assertThatThrownBy(proxy::fail)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("delete failed");

        verify(batchHistoryService).fail(1L, "delete failed");
    }

    private TestBatchTarget createProxy() {
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestBatchTarget());
        factory.addAspect(batchAop);
        return factory.getProxy();
    }

    static class TestBatchTarget {

        @BatchLogging("DELETE_RETIRED_MEMBERS")
        public int succeed() {
            return 3;
        }

        @BatchLogging("DELETE_RETIRED_MEMBERS")
        public int fail() {
            throw new IllegalStateException("delete failed");
        }
    }

}
