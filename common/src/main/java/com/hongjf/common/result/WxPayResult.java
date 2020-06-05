package com.hongjf.common.result;

import lombok.Data;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2020/3/12
 * @Time: 14:40
 * @Description:微信支付通知返回类
 */
@Data
public class WxPayResult {
    /**
     * 状态码
     * SUCCESS/FAIL
     *
     * SUCCESS表示商户接收通知成功并校验成功
     */
    private String return_code;
    /**
     * 返回信息，如非空，为错误原因：
     *
     * 签名失败
     *
     * 参数格式校验错误
     */
    private String return_msg;

    public WxPayResult(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }
}
