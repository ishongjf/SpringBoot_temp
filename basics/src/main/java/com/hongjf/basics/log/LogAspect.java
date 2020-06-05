package com.hongjf.basics.log;

import com.hongjf.basics.entity.system.SystemLogEntity;
import com.hongjf.basics.manager.system.SystemLogManager;
import com.hongjf.common.constant.GlobalConstant;
import com.hongjf.common.enums.global.ResultStatusEnum;
import com.hongjf.common.holder.RequestHolder;
import com.hongjf.common.result.Result;
import com.hongjf.common.utils.RequestUtil;
import com.hongjf.common.utils.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hongjf
 * @Date: 2020/1/6
 * @Time: 17:32
 * @Description:日志记录切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SystemLogManager systemLogManager;

    @Pointcut("execution(public * com.hongjf..controller..*.*(..))")
    public void log() {
    }

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("log()")
    public Object recordSysLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //1.先执行业务
        Object result = proceedingJoinPoint.proceed();
        if (null == result) {
            return null;
        }
        if (!(result instanceof Result)) {
            return result;
        }
        SystemLogEntity systemLogEntity = new SystemLogEntity();
        systemLogEntity.setElapsedTime(System.currentTimeMillis() - startTime);
        //2.执行日志记录
        try {
            handler(proceedingJoinPoint, result, systemLogEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("日志记录出错");
        }
        return result;
    }

    /**
     * 日志处理
     *
     * @param proceedingJoinPoint
     */
    public void handler(ProceedingJoinPoint proceedingJoinPoint, Object result, SystemLogEntity systemLogEntity) {
        //1.获得request对象
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        //2.获取请求数据
        String uri = request.getRequestURI();
        systemLogEntity.setRequestUrl(uri);
        Long userId = GlobalConstant.getUserId();
        if (ToolUtil.isNotEmpty(userId)) {
            systemLogEntity.setUserId(userId);
        }
        systemLogEntity.setRequestIp(RequestUtil.getIp(request));
        systemLogEntity.setRequestType(request.getMethod());
        String classMethod = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName() + "()";
        systemLogEntity.setMethodName(classMethod);
        Map<String, Object> params = getFieldsName(proceedingJoinPoint);
        systemLogEntity.setRequestParams(params.toString());
        String browserInfo = RequestUtil.getAppBrowserInfo(request);
        systemLogEntity.setRequestEquip(browserInfo);
        Result logResult = (Result) result;
        if (GlobalConstant.SUCCEED_CODE.equals(logResult.getCode())) {
            systemLogEntity.setResultStatus(ResultStatusEnum.SUCCEED.getCode());
        } else {
            systemLogEntity.setResultStatus(ResultStatusEnum.FAIL.getCode());
        }
        systemLogEntity.setResult(result.toString());
        //3.保存日志记录
        systemLogManager.insert(systemLogEntity);
    }

    /**
     * 获取参数列表
     *
     * @param joinPoint
     * @return
     */
    private static Map<String, Object> getFieldsName(ProceedingJoinPoint joinPoint) {
        // 参数值
        Object[] args = joinPoint.getArgs();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = pnd.getParameterNames(method);
        Map<String, Object> paramMap = new HashMap<>(16);
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }

}
