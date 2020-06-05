package com.hongjf.basics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Hongjf
 * @Date: 2020/1/10
 * @Time: 17:09
 * @Description:
 */
@Data
public class BaseVo {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 创建人名
     */
    private String createUserName;
    /**
     * 更新id
     */
    private Long updateUserId;
    /**
     * 更新人名
     */
    private String updateUserName;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
