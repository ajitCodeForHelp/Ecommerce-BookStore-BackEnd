package com.bt.ecommerce.annotation;

import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.configuration.TranslationUtility;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TranslateResponseMessageAspect {
    @Around("@annotation(translate)")
    public Object translateMessage(ProceedingJoinPoint joinPoint, TranslateResponseMessage translate) throws Throwable {
        Object response = joinPoint.proceed();

        if (response instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) response;
            if (responseEntity.getBody() instanceof ResponsePacket) {
                ResponsePacket responsePacket = (ResponsePacket) responseEntity.getBody();
                // Translate message
                responsePacket.setMessage(SpringBeanContext.getBean(TranslationUtility.class).getMessage(responsePacket.getMessage()));
            }
        }
        return response;
    }
}