package com.hongjf.common.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * oss相关配置
 *
 * @author eel
 * @date 2018-06-27-下午1:20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsProperties {

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 签名名称
     */
    private String signName;

    /**
     * 登录验证码的模板
     */
    private String loginTemplateCode;
    /**
     * 身份验证验证码模板
     */
    private String identityTemplateCode;
    /**
     * 登录异常验证码模板
     */
    private String loginExceptionTemplateCode;
    /**
     * 注册验证码模板
     */
    private String registeredTemplateCode;
    /**
     * 修改密码验证码模板
     */
    private String midPasswordTemplateCode;
    /**
     * 修改密码验证码模板
     */
    private String updateInfoTemplateCode;
    /**
     * 短信失效时间（分钟）
     */
    private Integer invalidateMinutes = 5;

    /**
     * 根据参数类型获取对应验证码模板号
     *
     * @param type
     * @return
     */
    public String getCode(Integer type) {
        switch (type) {
            case 1:
                return this.identityTemplateCode;
            case 2:
                return this.loginTemplateCode;
            case 3:
                return this.registeredTemplateCode;
            case 4:
                return this.updateInfoTemplateCode;
            case 5:
                return this.midPasswordTemplateCode;
            case 6:
                return this.loginExceptionTemplateCode;
            default:
                return this.loginTemplateCode;
        }
    }
}
