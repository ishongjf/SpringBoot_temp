package com.hongjf.basics.entity.base;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongjf.basics.entity.BaseEntity;
import com.hongjf.basics.entity.ITransferVo;
import com.hongjf.basics.vo.base.UserVo;
import com.hongjf.common.constant.GlobalConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: hongjf
 * @Date: 2020/2/10
 * @Time: 17:28
 * @Description:业主用户表
 */
@Data
@TableName(value = "user")
public class UserEntity extends BaseEntity implements Serializable, ITransferVo<UserVo> {
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 用户token
     */
    private String token;
    /**
     * 状态 默认为0已启用 1已禁用
     */
    private Integer status;
    /**
     * 删除状态 0未删除 1已删除
     */
    @TableLogic
    private Integer deleteStatus;

    @Override
    public UserVo transferToVo() {
        return GlobalConstant.modelMapper().map(this, UserVo.class);
    }
}
