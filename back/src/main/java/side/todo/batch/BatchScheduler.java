package side.todo.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import side.todo.service.MemberBatchService;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final MemberBatchService memberBatchService;

    @Scheduled(cron = "${batch.member.cron}", zone = "${batch.member.zone}")
    public void memberBatch() {
        memberBatchService.deleteRetiredMembers();
    }
}
