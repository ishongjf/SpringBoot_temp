package com.hongjf.common.constant;

/**
 *
 * @Author: Hongjf
 * @Date: 2020/1/15
 * @Time: 10:52
 * @Description:jwt常量信息
 */
public interface JwtConstants {
    /**
     * 请求头中的字段名
     */
    String AUTH_HEADER = "token";
    /**
     * 加密字符串
     */
    String SECRET = "defaultSecret";
    /**
     * 有效期
     */
    Long EXPIRATION = 6048000L;
}
