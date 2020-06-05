package com.hongjf.common.sms.enums;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 14:17
 * @Description:短信验证结果
 */
public enum SmsVerifyResult {
    /**
     * 短信验证结果
     */
    SUCCESS(0, "验证成功"),

    ERROR(1, "验证码错误"),

    EXPIRED(2, "验证码超时"),

    FAILURE(3, "验证码失效"),

    TIMES_UP(4, "超过验证次数");

    private Integer coed;

    private String msg;

    SmsVerifyResult(Integer coed, String msg) {
        this.coed = coed;
        this.msg = msg;
    }

    public Integer getCoed() {
        return coed;
    }

    public void setCoed(Integer coed) {
        this.coed = coed;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
