package com.hongjf.common.annotation.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: hongjf
 * @Date: 2020/6/5
 * @Time: 20:51
 * @Description:对被该注解修饰的方法的返回值在返回之前,对返回值中被@SetValueFIeld注解修饰的属性赋值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SetValueFieldMethod {
}
