package com.hongjf.common.sms.enums;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 13:44
 * @Description:短信发送状态枚举
 */
public enum SmsSendStatusEnum {

    /**
     * 验证码未发送
     */
    WAITING(0, "未发送"),
    /**
     * 验证码发送成功
     */
    SUCCESS(1, "发送成功"),
    /**
     * 验证码发送失败
     */
    FAILED(2, "发送失败"),
    /**
     * 验证码失效
     */
    INVALID(3, "失效");

    private Integer code;

    private String msg;

    SmsSendStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
