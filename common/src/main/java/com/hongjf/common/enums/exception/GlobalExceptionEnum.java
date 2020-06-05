package com.hongjf.common.enums.exception;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/1/2
 * @Time: 14:53
 * @Description:全局异常枚举类
 */
public enum GlobalExceptionEnum {
    /**
     * 异常编码信息
     */
    REQUEST_PARAM_ERROR(300, "请求参数异常"),

    LOGIN_ERROR(400, "登录信息异常"),

    SERVER_ERROR(500, "服务器运行异常"),

    NOT_SUPPORTED_ERROR(600, "请求方法错误"),

    DATA_INTEGRITY_ERROR(700, "请求参数中有字段超过数据长度限制"),

    WX_LOGIN_ERROR(800, "微信登录失败"),

    FILE_IMPORT_ERROR(900, "文件导入异常"),

    USER_EXIST_ERROR(1000, "用户不存在"),

    USER_ACCOUNT_NAME_EXIST_ERROR(1010, "用户账号已存在"),

    USER_STATUS_ERROR(1020, "用户状态异常"),

    USER_DISABLED_ERROR(1030, "用户已被禁用，请联系管理人员"),

    FILE_UPLOAD_MAX_ERROR(1100, "文件过大"),

    INIT_TABLE_EMPTY_PARAMS(1200, "初始化数据库，存在为空的字段"),

    FIELD_VALIDATE_ERROR(1300, "数据库字段与实体字段不一致"),

    SMS_SEND_ERROR(1400, "短信发送失败"),

    SMS_SAVE_ERROR(1410, "短信信息保存失败"),
    ;


    private Integer code;

    private String msg;

    GlobalExceptionEnum(Integer code, String msg) {
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
