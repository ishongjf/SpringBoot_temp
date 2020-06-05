package com.hongjf.common.sms;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com. hongjf.common.enums.exception.GlobalExceptionEnum;
import com. hongjf.common.exception.GlobalException;
import com. hongjf.common.sms.enums.SmsResultEnum;
import com. hongjf.common.sms.enums.SmsSendStatusEnum;
import com. hongjf.common.sms.enums.SmsVerifyResult;
import com. hongjf.common.sms.exception.SmsException;
import com. hongjf.common.sms.init.*;
import com. hongjf.common.sms.manager.MultiSignManager;
import com. hongjf.common.sms.manager.SmsManager;
import com. hongjf.common.utils.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 10:55
 * @Description:sms短信工具类
 */
@Slf4j
public class AliyunSmsManager implements SmsManager {

    @Autowired
    private AliyunSmsProperties aliyunSmsProperties;

    @Autowired
    private MultiSignManager multiSignManager;

    @Autowired
    private SmsInfoMapper smsInfoMapper;

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(String phoneNumber, String templateCode, Map<String, Object> params, String smsCode, Integer messageType, Integer smsSendSource) {
        log.info("开始发送阿里云短信，手机号是：[{}],模板号是：[{}],参数是：[{}]", phoneNumber, templateCode, params);

        //检验参数是否合法
        assertSendSmsParams(phoneNumber, templateCode, params, aliyunSmsProperties);

        //初始化client profile
        IClientProfile profile = initClientProfile();
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsResponse sendSmsResponse = createSmsRequest(phoneNumber, templateCode, params, acsClient);

        //如果返回ok则发送成功
        if (sendSmsResponse.getCode() != null && SmsResultEnum.OK.getCode().equals(sendSmsResponse.getCode())) {
            SendMessageParam param = new SendMessageParam();
            param.setMessageType(messageType);
            param.setPhoneNumbers(phoneNumber);
            param.setTemplateCode(templateCode);
            param.setParams(params);
            param.setSmsSendSource(smsSendSource);
            param.setSmsStatus(SmsSendStatusEnum.SUCCESS.getCode());
            saveSmsInfo(param, smsCode);
            return;
        } else {
            //放回其他状态码根据情况抛出业务异常
            String code = SmsResultEnum.SYSTEM_ERROR.getCode();
            String errorMessage = SmsResultEnum.SYSTEM_ERROR.getMessage();
            for (SmsResultEnum smsResultEnum : SmsResultEnum.values()) {
                if (smsResultEnum.getCode().equals(sendSmsResponse.getCode())) {
                    code = smsResultEnum.getCode();
                    errorMessage = smsResultEnum.getMessage();
                }
            }
            log.error("发送短信异常！错误代码code = [{}],错误信息message = [{}]", code, errorMessage);
            throw new SmsException(code, errorMessage);
        }
    }

    /**
     * 校验验证码是否正确,校验成功更新短信状态为失效
     *
     * @param phone 电话好码
     * @param code  验证码
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer validateSmsInfo(String phone, String code) {
        //校验请求是否为空
        if (ToolUtil.isOneEmpty(phone, code)) {
            log.error("校验短信是否正确失败！有参数为空！");
            throw new SmsException(SmsResultEnum.PARAM_NULL.getCode(), SmsResultEnum.PARAM_NULL.getMessage());
        }

        //查询有没有这条记录
        QueryWrapper<SmsInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_numbers", phone);
        queryWrapper.eq("validate_code", code);
        queryWrapper.eq("status", SmsSendStatusEnum.SUCCESS.getCode());
        queryWrapper.orderByDesc("create_time");
        List<SmsInfoEntity> smsInfos = smsInfoMapper.selectList(queryWrapper);

        //如果找不到记录，提示验证失败
        if (smsInfos == null || smsInfos.isEmpty()) {
            log.info("验证短信Provider接口，找不到验证码记录，响应验证失败！");
            return SmsVerifyResult.ERROR.getCoed();
        } else {
            //获取最近发送的第一条
            SmsInfoEntity smsInfo = smsInfos.get(0);

            //如果验证码和传过来的不一致
            if (!code.equals(smsInfo.getValidateCode())) {
                log.info("验证短信Provider接口，验证手机号和验证码不一致，响应验证失败！");
                return SmsVerifyResult.ERROR.getCoed();
            }

            //判断是否超时
            Date invalidTime = smsInfo.getInvalidTime();
            if (invalidTime == null || new Date().after(invalidTime)) {
                log.info("验证短信Provider接口，验证码超时，响应验证失败！");
                return SmsVerifyResult.EXPIRED.getCoed();
            }

            //验证成功后把短信设置成失效
            smsInfo.setStatus(SmsSendStatusEnum.INVALID.getCode());
            smsInfoMapper.updateById(smsInfo);

            log.info("验证短信Provider接口，验证码验证成功！");
            return SmsVerifyResult.SUCCESS.getCoed();
        }
    }

    /**
     * 校验发送短信的参数是否正确
     *
     * @Date 2018/7/6 下午3:19
     */
    private void assertSendSmsParams(String phoneNumber, String templateCode, Map<String, Object> params,
                                     AliyunSmsProperties aliyunSmsProperties) {
        if (ToolUtil.isOneEmpty(phoneNumber, templateCode, params, aliyunSmsProperties)) {
            log.error("阿里云短信发送异常！请求参数存在空！");
            throw new SmsException(SmsResultEnum.PARAM_NULL.getCode(), SmsResultEnum.PARAM_NULL.getMessage());
        }
    }

    /**
     * 初始化短信发送的环境
     *
     * @Date 2018/7/6 下午3:57
     */
    private IClientProfile initClientProfile() {
        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化ascClient需要的几个参数
        //短信API产品名称（短信产品名固定，无需修改）
        final String product = "Dysmsapi";
        //短信API产品域名（接口地址固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";
        final String accessKeyId = aliyunSmsProperties.getAccessKeyId();
        final String accessKeySecret = aliyunSmsProperties.getAccessKeySecret();

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            log.error("初始化阿里云sms异常！", e);
        }
        return profile;
    }

    /**
     * 组装请求对象
     *
     * @Date 2018/7/6 下午3:00
     */
    private SendSmsResponse createSmsRequest(String phoneNumber, String templateCode, Map<String, Object> params, IAcsClient acsClient) {

        SendSmsRequest request = new SendSmsRequest();

        request.setMethod(MethodType.POST);

        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，
        //批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(phoneNumber);

        //必填:短信签名-可在短信控制台中找到
        request.setSignName(this.getSmsSign(phoneNumber));

        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);

        //可选:模板中的变量替换JSON串
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(JSON.toJSONString(params));

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("初始化阿里云sms异常！可能是accessKey和secret错误！", e);
            throw new SmsException(SmsResultEnum.INIT_SMS_CLIENT_ERROR.getCode(),
                    SmsResultEnum.INIT_SMS_CLIENT_ERROR.getMessage());
        }
        return sendSmsResponse;
    }

    /**
     * 获取sms发送的sign标识，参数phone是发送的手机号码
     *
     * @Date 2018/8/13 21:23
     */
    private String getSmsSign(String phone) {
        String signName = this.aliyunSmsProperties.getSignName();

        //如果是单个签名就用一个签名发
        if (!signName.contains(",")) {
            log.info("发送短信，签名为：" + signName + ",电话为：" + phone);
            return signName;
        } else {
            return multiSignManager.getSign(phone, signName);
        }
    }

    /**
     * 存储短信验证信息
     *
     * @param sendMessageParam
     * @param validateCode
     * @return
     */
    public Integer saveSmsInfo(SendMessageParam sendMessageParam, String validateCode) {

        if (ToolUtil.isOneEmpty(sendMessageParam.getPhoneNumbers(), validateCode, sendMessageParam.getTemplateCode())) {
            log.error("存储短信到数据库失败！有参数为空！手机号：[{}],验证码:[{}]", sendMessageParam.getPhoneNumbers(), validateCode);
            throw new GlobalException(GlobalExceptionEnum.SMS_SAVE_ERROR.getCode(), GlobalExceptionEnum.SMS_SAVE_ERROR.getMsg());
        }

        //当前时间
        Date nowDate = new Date();

        //短信失效时间
        long invalidateTime = nowDate.getTime() + aliyunSmsProperties.getInvalidateMinutes() * 60 * 1000;
        Date invalidate = new Date(invalidateTime);

        SmsInfoEntity smsInfo = new SmsInfoEntity();
        smsInfo.setCreateTime(nowDate);
        smsInfo.setInvalidTime(invalidate);
        smsInfo.setPhoneNumbers(sendMessageParam.getPhoneNumbers());
        smsInfo.setStatus(sendMessageParam.getSmsStatus());
        smsInfo.setSource(sendMessageParam.getSmsSendSource());
        smsInfo.setTemplateCode(sendMessageParam.getTemplateCode());
        smsInfo.setValidateCode(validateCode);
        smsInfoMapper.insert(smsInfo);

        log.info("发送短信，存储短信到数据库，数据为：[{}]", smsInfo);

        return smsInfo.getId();
    }
}
