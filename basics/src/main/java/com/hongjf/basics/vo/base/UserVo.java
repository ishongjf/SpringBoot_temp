package com.hongjf.basics.vo.base;

import com.hongjf.basics.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hongjf
 * @Date: 2020/5/15
 * @Time: 17:29
 * @Description:
 */
@Data
public class UserVo extends BaseVo implements Serializable {
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
}
