package com.hongjf.common.enums.global;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/1/6
 * @Time: 16:25
 * @Description:删除状态枚举
 */
public enum DeleteStatusEnum {
    /**
     * 未删除
     */
    NOT_DELETE(0, "未删除"),

    /**
     * 已删除
     */
    ALREADY_DELETE(1, "已删除");

    private Integer code;

    private String msg;

    DeleteStatusEnum(Integer code, String msg) {
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
