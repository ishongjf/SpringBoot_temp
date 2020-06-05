package com.hongjf.controller.interceptor;

import com.hongjf.basics.entity.base.UserEntity;
import com.hongjf.basics.manager.base.UserManager;
import com.hongjf.common.constant.GlobalConstant;
import com.hongjf.common.constant.JwtConstants;
import com.hongjf.common.enums.exception.GlobalExceptionEnum;
import com.hongjf.common.exception.GlobalException;
import com.hongjf.common.utils.JwtUtil;
import com.hongjf.common.utils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Hongjf
 * @Date: 2020/1/3
 * @Time: 15:40
 * @Description:鉴权拦截器
 */
@Component
public class RestApiInterceptor implements HandlerInterceptor {

    @Autowired
    private UserManager userManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!check(request, response)) {
            throw new GlobalException(GlobalExceptionEnum.LOGIN_ERROR.getCode(), GlobalExceptionEnum.LOGIN_ERROR.getMsg());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //请求处理之后进行调用，但是在视图被渲染之前(Controller方法调用之后)
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行(主要是用于进行资源清理工作)
    }

    public boolean check(HttpServletRequest request, HttpServletResponse response) {
        final String authToken = request.getHeader(JwtConstants.AUTH_HEADER);
        //判断是否存在
        if (ToolUtil.isEmpty(authToken)) {
            return false;
        }
        //判断是否过期
        if (JwtUtil.isTokenExpired(authToken)) {
            return false;
        }
        Long userId = Long.parseLong(JwtUtil.getUsernameFromToken(authToken));
        UserEntity userEntity = userManager.selectByPrimaryKey(userId);
        //判断用户是否存在
        if (ToolUtil.isEmpty(userEntity)) {
            return false;
        }
        //判断token是否正确
        if (!authToken.equals(userEntity.getToken())) {
            return false;
        }
        GlobalConstant.setUserId(userId);
        return true;
    }
}
