package com.hongjf.controller.handler;

import com.hongjf.common.enums.exception.GlobalExceptionEnum;
import com.hongjf.common.exception.GlobalException;
import com.hongjf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @Author: Hongjf
 * @Date: 2020/1/2
 * @Time: 15:32
 * @Description:全局异常拦截器(拦截所有的控制器,带有@RequestMapping注解的方法上都会拦截)
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result bussiness(Exception e) {
        log.error(">>>>>>系统异常[{}]", e.getMessage(), e);
        return Result.fail(GlobalExceptionEnum.SERVER_ERROR.getCode(), GlobalExceptionEnum.SERVER_ERROR.getMsg());
    }

    /**
     * 请求方法错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result notSupportedMethod(HttpRequestMethodNotSupportedException e) {
        log.error(">>>>>>请求方法错误[{}]", e.getMessage(), e);
        return Result.fail(GlobalExceptionEnum.NOT_SUPPORTED_ERROR.getCode().intValue(), GlobalExceptionEnum.NOT_SUPPORTED_ERROR.getMsg());
    }

    /**
     * 拦截运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result requestParameterError(RuntimeException e) {
        log.error(">>>>>>系统运行异常[{}]", e.getMessage(), e);
        if (e instanceof DataIntegrityViolationException) {
            return Result.fail(GlobalExceptionEnum.DATA_INTEGRITY_ERROR.getCode(), GlobalExceptionEnum.DATA_INTEGRITY_ERROR.getMsg());
        }
        return Result.fail(GlobalExceptionEnum.SERVER_ERROR.getCode(), GlobalExceptionEnum.SERVER_ERROR.getMsg());
    }

    /**
     * 拦截自定义异常
     */
    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result requestParameterError(GlobalException e) {
        log.error(">>>>>>系统业务异常[{}]", e.getMsg(), e);
        return Result.fail(e.getCode(), e.getMsg());
    }

    /**
     * 参数校验异常捕获
     *
     * @param e @Valid注解异常设置
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result exception(MethodArgumentNotValidException e) {
        log.error(">>>>>>请求参数不合法[{}]", e.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), e);
        return Result.fail(GlobalExceptionEnum.REQUEST_PARAM_ERROR.getCode(), e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    /**
     * 文件上传过大异常拦截
     *
     * @param e 异常信息
     * @return
     */
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result MaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(">>>>>>文件上传过大异常[{}]", e.getMessage(), e);
        return Result.fail(GlobalExceptionEnum.FILE_UPLOAD_MAX_ERROR.getCode(), GlobalExceptionEnum.FILE_UPLOAD_MAX_ERROR.getMsg());
    }
}
