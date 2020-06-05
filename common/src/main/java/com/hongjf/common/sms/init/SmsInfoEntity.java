package com.hongjf.common.sms.init;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 10:28
 * @Description:
 */
@Data
@TableName("sys_sms_info")
public class SmsInfoEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 手机号
     */
    @TableField("phone_numbers")
    private String phoneNumbers;
    /**
     * 短信验证码
     */
    @TableField("validate_code")
    private String validateCode;
    /**
     * 短信模板ID
     */
    @TableField("template_code")
    private String templateCode;
    /**
     * 回执ID,可根据该ID查询具体的发送状态
     */
    @TableField("bizid")
    private String bizid;
    /**
     * 0=未发送，1=发送成功，2=发送失败
     */
    @TableField("status")
    private Integer status;
    /**
     * 0=app，1=pc，2=其它
     */
    @TableField("source")
    private Integer source;
    /**
     * 失效时间
     */
    @TableField("invalid_time")
    private Date invalidTime;
    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Date modifyTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
