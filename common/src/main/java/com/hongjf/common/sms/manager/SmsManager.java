package com.hongjf.common.sms.manager;

import java.util.Map;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 11:11
 * @Description:短信发送服务
 */
public interface SmsManager {

    /**
     * 发送短信
     *
     * @param phoneNumber   电话号码
     * @param templateCode  模板号码
     * @param params        模板里参数的集合
     * @param smsCode       当messageType为1验证类消息时，code为验证码
     * @param messageType   消息类型 1验证类消息 2纯发送消息
     * @param smsSendSource 消息发送源 0app 1pc 2其他
     */
    void sendSms(String phoneNumber, String templateCode, Map<String, Object> params, String smsCode, Integer messageType, Integer smsSendSource);

    /**
     * 校验验证码是否正确,校验成功更新短信状态为失效
     *
     * @param phone 电话好码
     * @param code  验证码
     * @return
     */
    Integer validateSmsInfo(String phone, String code);
}
