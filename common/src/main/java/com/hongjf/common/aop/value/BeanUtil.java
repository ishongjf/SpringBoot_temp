package com.hongjf.common.aop.value;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongjf.common.annotation.value.SetValueField;
import com.hongjf.common.utils.ToolUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: hongjf
 * @Date: 2020/6/5
 * @Time: 20:53
 * @Description:@SetValueField赋值工具类
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>(16);

    public void setValueField(Object data) throws Exception {
        if (data instanceof Page) {
            for (Object object : ((Page) data).getRecords()) {
                setValue(object);
            }
        } else {
            setValue(data);
        }
    }

    public void setValue(Object object) throws Exception {
        //1.先获取需要进行赋值的对象类型
        Class<?> clazz = object.getClass();
        //2.获取对象类型上的属性并进行遍历
        for (Field field : clazz.getDeclaredFields()) {
            //3.获取属性上的注解
            SetValueField setValueField = field.getAnnotation(SetValueField.class);
            if (ToolUtil.isEmpty(setValueField)) {
                continue;
            }
            //4.设置属性可见
            field.setAccessible(true);
            //5.获取要执行的bean,判断是否为枚举
            Object bean;
            if (setValueField.beanClass().isEnum()) {
                bean = setValueField.beanClass();
            } else {
                bean = applicationContext.getBean(setValueField.beanClass());
            }
            //6.获取bean要执行方法，传入方法名和参数值
            Method method = setValueField.beanClass().getMethod(setValueField.method(), clazz.getDeclaredField(setValueField.param()).getType());
            //7.获取方法要执行的参数,并设置属性可见
            Field param = clazz.getDeclaredField(setValueField.param());
            param.setAccessible(true);
            //8.获取要进行赋值的属性
            Field targetField;
            Boolean innerTargetField = ToolUtil.isNotEmpty(setValueField.target());
            //9.设置缓存key
            String keyPrefix = setValueField.beanClass() + "-" + setValueField.method() + "-" + setValueField.target() + "-";
            //10.获取参数字段的值
            Object paramValue = param.get(object);
            //11.paramValue为空时不去执行方法
            if (ToolUtil.isNotEmpty(paramValue)) {
                //12.设置缓存key
                String key = keyPrefix + paramValue;
                if (cache.containsKey(key)) {
                    field.set(object, cache.get(key));
                } else {
                    //13.执行方法
                    Object methodValue = method.invoke(bean, paramValue);
                    //14.判断target和方法执行结果是否存在
                    if (innerTargetField && ToolUtil.isNotEmpty(methodValue)) {
                        //15.获取targetField,并设置可见
                        targetField = methodValue.getClass().getDeclaredField(setValueField.target());
                        targetField.setAccessible(true);
                        //16.targetField存在就赋值
                        if (ToolUtil.isNotEmpty(targetField.get(methodValue))) {
                            field.set(object, targetField.get(methodValue));
                            cache.put(key, targetField.get(methodValue));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
