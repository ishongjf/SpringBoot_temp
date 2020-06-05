package com.hongjf.common.sms.enums;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 13:40
 * @Description:sms发送源
 */
public enum  SmsSendSourceEnum {
    APP(0),

    PC(1),

    OTHER(2);;

    private Integer code;

    SmsSendSourceEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
