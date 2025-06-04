package com.flightapp.fare.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = Logger.getLogger(LoggingAspect.class.getName());

    
    @Pointcut("execution(* com.flightapp.fare.controller..*(..))")
    public void controllerMethods() {}

    
    @Pointcut("execution(* com.flightapp.fare.repository..*(..))")
    public void repositoryMethods() {}

    @Before("controllerMethods() || repositoryMethods()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Entering method: " + joinPoint.getSignature().toShortString() +
                    " | Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods() || repositoryMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        LOGGER.info("Exiting method: " + joinPoint.getSignature().toShortString() +
                    " | Result: " + result);
    }

    @AfterThrowing(pointcut = "controllerMethods() || repositoryMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        LOGGER.severe("Exception in method: " + joinPoint.getSignature().toShortString() +
                      " | Exception: " + ex.getMessage());
    }
}
