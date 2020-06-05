package com.hongjf.basics.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongjf.basics.entity.BaseEntity;
import com.hongjf.basics.entity.ITransferVo;
import com.hongjf.basics.vo.system.SystemLogVo;
import com.hongjf.common.constant.GlobalConstant;
import lombok.Data;

/**
 * @Author: Hongjf
 * @Date: 2020/1/6
 * @Time: 17:04
 * @Description:系统日志entity
 */
@Data
@TableName("system_log")
public class SystemLogEntity extends BaseEntity implements ITransferVo<SystemLogVo> {
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
     * 请求耗时时间戳
     */
    private Long elapsedTime;
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

    @Override
    public SystemLogVo transferToVo() {
        SystemLogVo systemLogVo = GlobalConstant.modelMapper().map(this, SystemLogVo.class);
        return systemLogVo;
    }
}
