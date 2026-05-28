package re.exam.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class BookServiceAspect {

    @Pointcut("execution(* re.exam.service.*.*(..))")
    public void serviceLayerPointcut() {}

    @Before("serviceLayerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[AOP - BEFORE] Method: {} | Args: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceLayerPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AOP - AFTER RETURNING] Method: {} | Result: {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "serviceLayerPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("[AOP - AFTER THROWING] Method: {} | Exception: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}

