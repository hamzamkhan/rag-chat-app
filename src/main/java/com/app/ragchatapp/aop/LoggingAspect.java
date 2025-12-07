package com.app.ragchatapp.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());
//
//    @Before("execution(* com.yourcompany.service.*.*(..))")
//    public void logBeforeMethod(JoinPoint joinPoint) {
//        logger.info("Entering method: {}", joinPoint.getSignature().getName());
//    }
}
