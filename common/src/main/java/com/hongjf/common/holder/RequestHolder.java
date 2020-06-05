package com.hongjf.common.holder;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 10:52
 * @Description:获取 HttpServletRequest
 */
public class RequestHolder {
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
