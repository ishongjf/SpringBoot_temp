package com.hongjf.common.sms.manager;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 11:01
 * @Description:阿里云短信发送服务
 */
public interface MultiSignManager {
    /**
     * 获取签名
     *
     * @param phone    电话
     * @param signName 发送短信用的签名，是一个以逗号隔开的字符串
     * @return
     */
    String getSign(String phone, String signName);
}
