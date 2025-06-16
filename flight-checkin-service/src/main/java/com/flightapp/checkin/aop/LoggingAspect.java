package com.flightapp.checkin.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Pointcut for all public methods in service and controller packages of checkin
    @Pointcut("execution(public * com.flightapp.checkin.service.*.*(..)) || execution(public * com.flightapp.checkin.controller.*.*(..))")
    public void checkinServiceAndControllerMethods() {}

    @Before("checkinServiceAndControllerMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering method: {} | Args: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "checkinServiceAndControllerMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("Completed method: {} | Result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "checkinServiceAndControllerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception in method: {} | Exception: {}", joinPoint.getSignature(), ex.getMessage());
    }
    public LoggingAspect() {
        logger.info("LoggingAspect initialized");
    }

}
