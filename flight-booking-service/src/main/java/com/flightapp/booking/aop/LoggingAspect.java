package com.flightapp.booking.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = Logger.getLogger(LoggingAspect.class.getName());

    
    @Pointcut("execution(* com.flightapp.booking.controller..*(..)) || execution(* com.flightapp.booking.service..*(..))")
    public void appMethods() {}

    // Log method entry
    @Before("appMethods()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Entering method: " + joinPoint.getSignature().toShortString() +
                    " | Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    // Log method return
    @AfterReturning(pointcut = "appMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        LOGGER.info("Completed method: " + joinPoint.getSignature().toShortString() +
                    " | Result: " + result);
    }

    // Log exceptions
    @AfterThrowing(pointcut = "appMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        LOGGER.severe("Exception in method: " + joinPoint.getSignature().toShortString() +
                      " | Message: " + ex.getMessage());
    }
}
