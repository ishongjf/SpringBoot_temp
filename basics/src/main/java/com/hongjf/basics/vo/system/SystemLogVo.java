package com.hongjf.basics.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 17:16
 * @Description:系统日志vo
 */
public class SystemLogVo {
    private Long id;

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

    /**
     * 操作用户id
     */
    private Long userId;
    /**
     * 请求路径
     */
    private String requestUrl;
    /**
     * 请求的ip地址
     */
    private String requestIp;
    /**
     * 请求的设备
     */
    private String requestEquip;
    /**
     * 请求接口类型
     */
    private String requestType;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 操作类型 0查询 1新增 2编辑 3删除
     */
    private Integer operationType;
    /**
     * 请求是否成功 0成功 1失败
     */
    private Integer resultStatus;
    /**
     * 返回数据
     */
    private String result;
    /**
     * 请求哪个类的哪个方法地址
     */
    private String methodName;
}
