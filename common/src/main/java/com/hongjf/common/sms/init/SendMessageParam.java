package com.hongjf.common.sms.init;


import com. hongjf.common.sms.enums.MessageTypeEnum;
import com. hongjf.common.sms.enums.SmsSendSourceEnum;
import lombok.Data;

import java.util.Map;

/**
 * 发送短信的参数
 *
 * @author eel
 * @date 2018-07-05 21:19
 */
@Data
public class SendMessageParam {

    /**
     * 手机号
     */
    private String phoneNumbers;

    /**
     * 模板号
     */
    private String templateCode;

    /**
     * 模板中的参数
     */
    private Map<String, Object> params;

    /**
     * sms发送源 0app 1pc 2其他
     */
    private Integer smsSendSource = SmsSendSourceEnum.PC.getCode();

    /**
     * 消息类型，1=验证码，2=消息，默认不传为验证码
     */
    private Integer messageType = MessageTypeEnum.SMS.getCode();
    /**
     * 状态 0未发送 1发送成功 2发送失败 3失败
     */
    private Integer smsStatus;


}
