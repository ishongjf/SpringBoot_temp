package com.hongjf.basics.param.base;

import com.hongjf.basics.entity.base.UserEntity;
import com.hongjf.basics.param.ITransferEntity;
import com.hongjf.common.constant.GlobalConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hongjf
 * @Date: 2020/5/15
 * @Time: 17:32
 * @Description:
 */
@Data
public class UserParam implements Serializable, ITransferEntity<UserEntity> {
    /**
     * 主键id
     */
    private Long id;
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

    @Override
    public UserEntity transferToEntity() {
        return GlobalConstant.modelMapper().map(this, UserEntity.class);
    }
}
