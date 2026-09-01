package side.todo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import side.todo.service.BatchHistoryService;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BatchAop {

    private final BatchHistoryService batchHistoryService;

    @Around("@annotation(side.todo.aop.BatchLogging)")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        var method = AopUtils.getMostSpecificMethod(
                signature.getMethod(),
                joinPoint.getTarget().getClass()
        );
        BatchLogging batchLogging = AnnotatedElementUtils.findMergedAnnotation(
                method,
                BatchLogging.class
        );

        if (batchLogging == null) {
            throw new IllegalStateException("BatchLogging annotation was not found");
        }

        return logging(joinPoint, batchLogging);
    }

    public Object logging(ProceedingJoinPoint joinPoint, BatchLogging batchLogging) throws Throwable {

        Object result;

        String batchName = batchLogging.value();

        Long batchHistoryId = batchHistoryService.start(batchName).getId();

        try {
             result = joinPoint.proceed();

            int processedCount = extractProcessedCount(result);

            batchHistoryService.success(batchHistoryId, processedCount);

        }  catch (Throwable e) {
            batchHistoryService.fail(batchHistoryId, e.getMessage());
            throw e;
        }

        return result;
    }

    private int extractProcessedCount(Object result) {
        if (result instanceof Number number) {
            return number.intValue();
        }

        return 0;
    }
}
