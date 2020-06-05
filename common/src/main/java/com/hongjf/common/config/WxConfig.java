package com.hongjf.common.config;

import com.hongjf.common.constant.GlobalConstant;
import com.hongjf.common.utils.MD5Util;
import com.hongjf.common.utils.ToolUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2020/1/9
 * @Time: 17:40
 * @Description:微信的配置信息
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx")
public class WxConfig {
    /**
     * 工匠小程序appId
     */
    private String appletAppId;
    /**
     * 业主小程序appId
     */
    private String ownerAppId;
    /**
     * 小程序secret
     */
    private String secret;
    /**
     * 商户id
     */
    private String mchId;
    /**
     * 商户key
     */
    private String mchKey;
    /**
     * 微信下单url
     */
    private String payUrl;
    /**
     * 支付回调接口
     */
    private String notifyUrl;
    /**
     * 支付文件路径
     */
    private String payFileUrl;
    /**
     * 微信小程序企业付款请求url
     */
    private String wxPayUrl;

    public String getAuthorizationUrl(String code, String appId) {
        return GlobalConstant.WX_AUTHORIZATION_URL + "?appid=" + appId + "&secret=" + this.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";
    }

    /**
     * 微信获取用户信息
     *
     * @param accessToken 微信token
     * @param openId      openId
     * @return
     */
    public String getWxUserInfoUrl(String accessToken, String openId) {
        return GlobalConstant.WX_USER_URL + "?access_token=" + accessToken + "&openid=" + openId;
    }

    /**
     * 生成MD5方式的微信支付签名
     *
     * @param params 请求参数
     * @param mehKey 商户密钥
     * @return
     */
    public String getSign(Map<String, String> params, String mehKey) {
        // 生成签名前先去除sign
        params.remove("sign");
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder signA = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            String value = param.getValue();
            if (ToolUtil.isEmpty(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                signA.append("&");
            }
            signA.append(param.getKey()).append("=").append(value);
        }
        String sign = MD5Util.encrypt(signA.append("&key=").append(mehKey).toString());
        return sign;
    }
}
