package com.kgisl.sbunittest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Pointcut - any class or method annotated with @Loggable
    @Pointcut("@within(com.kgisl.customrepo.Loggable) || @annotation(com.kgisl.customrepo.Loggable)")
    public void loggableMethods() {}

    // Before advice
    @Before("loggableMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("Before method: {}", joinPoint.getSignature().toShortString());
    }

    // After advice
    @After("loggableMethods()")
    public void logAfter(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("After method: {}", joinPoint.getSignature().toShortString());
    }

    // After returning advice
    @AfterReturning(pointcut = "loggableMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info("Method returned from {} -> {}", joinPoint.getSignature().toShortString(), result);
    }

    // After throwing advice
    @AfterThrowing(pointcut = "loggableMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.error("Exception in method: {}", joinPoint.getSignature().toShortString(), ex);
    }
}

