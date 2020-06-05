package com.hongjf.common.enums.global;

/**
 *
 * @Author: MuYa
 * @Date: 2019/10/4
 * @Time: 21:28
 * @Description: 数值比较类型
 */
public enum ConditionContentEnum {
    /**
     * 大于
     */
    GREATER_THAN(0, "大于"),

    /**
     * 等于
     */
    EQUAL_TO(1, "等于"),

    /**
     * 小于
     */
    LESS_THAN(2, "小于"),

    /**
     * 大于等于
     */
    GREATER_EQUAL(3, "大于等于"),

    /**
     * 小于等于
     */
    LESS_EQUAL(4, "小于等于"),

    ;

    ConditionContentEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private int code;
    private String msg;

    public static ConditionContentEnum codeOf(int code) {
        for (ConditionContentEnum conditionContentEnum : values()) {
            if (code == conditionContentEnum.getCode()) {
                return conditionContentEnum;
            }
        }
        return null;
    }

}
