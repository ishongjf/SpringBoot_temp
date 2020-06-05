package com.hongjf.common.enums.pay;

/**
 *
 *
 * @Author: hongjf
 * @Date: 2020/2/13
 * @Time: 17:22
 * @Description:支付类型枚举
 */
public enum PayTypeEnum {

    /**
     * 支付类型枚举
     */
    PROCEEDS(0, "收款"),

    REFUND(1, "支出");

    private Integer code;

    private String msg;

    PayTypeEnum(Integer code, String msg) {
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
