package com.hongjf.common.sms.enums;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 13:43
 * @Description:消息类型
 */
public enum MessageTypeEnum {
    /**
     * 消息类型
     */
    SMS(1, "验证类消息"),

    MESSAGE(2, "纯发送消息");

    private Integer code;

    private String msg;

    MessageTypeEnum(Integer code, String msg) {
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
