package side.todo.batch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;
import side.todo.service.MemberBatchService;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BatchSchedulerTest {

    @Mock
    private MemberBatchService memberBatchService;

    @InjectMocks
    private BatchScheduler batchScheduler;

    @Test
    void memberBatchExecutesMemberBatchService() {
        batchScheduler.memberBatch();

        verify(memberBatchService).deleteRetiredMembers();
    }

    @Test
    void memberBatchUsesConfiguredCronAndTimeZone() throws NoSuchMethodException {
        Method method = BatchScheduler.class.getDeclaredMethod("memberBatch");

        Scheduled scheduled = method.getAnnotation(Scheduled.class);

        assertThat(scheduled).isNotNull();
        assertThat(scheduled.cron()).isEqualTo("${batch.member.cron}");
        assertThat(scheduled.zone()).isEqualTo("${batch.member.zone}");
    }
}
