package com.hongjf.controller.config;


import com.hongjf.common.constant.GlobalConstant;
import com.hongjf.controller.interceptor.RestApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Hongjf
 * @Date: 2020/1/3
 * @Time: 15:53
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RestApiInterceptor restApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restApiInterceptor).excludePathPatterns(GlobalConstant.APPLET_NONE_PERMISSION_API).addPathPatterns("/api/**");
    }
}
