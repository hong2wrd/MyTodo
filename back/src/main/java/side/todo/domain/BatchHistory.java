package side.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import side.todo.domain.enums.BatchStatus;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String batchName;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BatchStatus status = BatchStatus.RUNNING;

    @Builder.Default
    private Integer processedCount = 0;

    @Column(length = 1000)
    private String errorMessage;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    private LocalDateTime finishedAt;

    public static BatchHistory of(String batchName) {
        return BatchHistory.builder()
                .batchName(batchName)
                .build();
    }

    public void success(int processedCount) {
        this.status = BatchStatus.SUCCESS;
        this.processedCount = processedCount;
        this.finishedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        this.status = BatchStatus.FAILED;
        this.errorMessage = errorMessage;
        this.finishedAt = LocalDateTime.now();
    }
}
