package com.hongjf.common.constant;

import cn.hutool.core.collection.CollectionUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/1/3
 * @Time: 15:26
 * @Description:全局常量类
 */
public class GlobalConstant {
    /**
     * 主键分隔符
     */
    public static final String IDS_SPLIT = ",";
    /**
     * 请求成功的状态码
     */
    public static final Integer SUCCEED_CODE = 200;
    /**
     * 主键分隔符
     */
    public static final String ORDER_CODE_PREFIX = "GJ";
    /**
     * 用于存放当前用户的id
     */
    private static ThreadLocal<Long> local = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        local.remove();
        local.set(userId);
    }

    public static Long getUserId() {
        return local.get();
    }

    public static volatile ModelMapper modelMapper = modelMapper();

    public static ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    /**
     * 不需要校验token的请求
     */
    public static List<String> APPLET_NONE_PERMISSION_API = CollectionUtil.newLinkedList("/api/index/**", "/api/test/**", "/api/login/**");
    /**
     * 微信小程序登录url
     */
    public static final String WX_AUTHORIZATION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 微信获取用户信息URL
     */
    public static final String WX_USER_URL = "https://api.weixin.qq.com/sns/userinfo";
    /**
     * 获取当前登录人id的key
     */
    public static final String USER_ACCOUNT_ID = "userAccountId";

}
