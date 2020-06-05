package com.hongjf.common.enums.global;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 9:43
 * @Description:请求是否成功枚举
 */
public enum ResultStatusEnum {
    /**
     * 请求成功
     */
    SUCCEED(0, "成功"),
    /**
     * 失败
     */
    FAIL(1, "失败");

    private Integer code;

    private String msg;

    ResultStatusEnum(Integer code, String msg) {
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
