package com.hongjf.common.enums.pay;

/**
 *
 *
 * @Author: hongjf
 * @Date: 2020/2/13
 * @Time: 17:16
 * @Description:支付状态枚举
 */
public enum PayStatusEnum {

    /**
     * 支付状态枚举
     */
    TO_DO_PAY(0, "待支付"),

    SUCCEED(1, "成功"),

    FAIL(2, "失败");

    private Integer code;

    private String msg;

    PayStatusEnum(Integer code, String msg) {
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
