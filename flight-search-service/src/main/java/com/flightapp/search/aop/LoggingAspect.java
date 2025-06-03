package com.flightapp.search.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = Logger.getLogger(LoggingAspect.class.getName());

    @Pointcut("execution(* com.flightapp.search.service..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Executing method: " + joinPoint.getSignature().toShortString() +
                " | Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        LOGGER.info("Completed method: " + joinPoint.getSignature().toShortString() +
                " | Result: " + result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        LOGGER.severe("Exception in method: " + joinPoint.getSignature().toShortString() +
                " | Exception: " + ex.getMessage());
    }
}
