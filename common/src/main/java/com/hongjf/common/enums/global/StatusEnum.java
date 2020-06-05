package com.hongjf.common.enums.global;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/4/29
 * @Time: 16:34
 * @Description:状态枚举
 */
public enum StatusEnum {
    /**
     * 启用禁用
     */
    ENABLED(0, "已启用"),

    DISABLED(1, "已禁用");;

    private Integer code;

    private String msg;

    StatusEnum(Integer code, String msg) {
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
