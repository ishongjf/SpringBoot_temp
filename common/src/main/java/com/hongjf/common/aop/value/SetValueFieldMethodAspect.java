package com.hongjf.common.aop.value;

import com.hongjf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @Author: hongjf
 * @Date: 2020/3/1
 * @Time: 14:57
 * @Description:赋值切面
 */
@Slf4j
@Aspect
@Component
public class SetValueFieldMethodAspect {

    @Autowired
    private BeanUtil beanUtil;

    @Around("@annotation(com.hongjf.common.annotation.value.SetValueFieldMethod)")
    public Object SetValue(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        if (result instanceof Result) {
            beanUtil.setValueField(((Result) result).getData());
        }
        return result;
    }

}
