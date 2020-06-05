package com.hongjf.common.annotation.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: hongjf
 * @Date: 2020/6/5
 * @Time: 20:50
 * @Description:该注解作用于属性上，表明该注解会在返回前赋值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SetValueField {
    /**
     * 要调用的是哪个bean
     *
     * @return
     */
    Class<?> beanClass();

    /**
     * 要调用该bean的哪个方法
     *
     * @return
     */
    String method();

    /**
     * 传入的参数是哪个字段的值
     *
     * @return
     */
    String param();

    /**
     * 获取哪个属性的值填充
     *
     * @return
     */
    String target();
}
