package com.hongjf.common.enums.global;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/4/30
 * @Time: 10:24
 * @Description:性别枚举
 */
public enum SexEnum {
    /**
     * 性别
     */
    MAN(1, "男"),

    WOMAN(2, "女");

    private Integer code;

    private String msg;

    SexEnum(Integer code, String msg) {
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

    public static SexEnum getSexEnum(Integer code) {
        for (SexEnum sexEnum : values()) {
            if (sexEnum.getCode().equals(code)) {
                return sexEnum;
            }
        }
        return null;
    }
}
