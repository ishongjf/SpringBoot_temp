package com.hongjf.common.utils;

import com. hongjf.common.constant.JwtConstants;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2019  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/1/15
 * @Time: 10:45
 * @Description:jwt工具类 jwt的claim里一般包含以下几种数据:
 * 1. iss -- token的发行者
 * 2. sub -- 该JWT所面向的用户
 * 3. aud -- 接收该JWT的一方
 * 4. exp -- token的失效时间
 * 5. nbf -- 在此时间段之前,不会被处理
 * 6. iat -- jwt发布时间
 * 7. jti -- jwt唯一标识,防止重复使用
 */
public class JwtUtil {
    /**
     * 获取用户信息从token中
     */
    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt接收者
     */
    public static String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public static String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取jwt的payload部分
     */
    public static Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token是否正确,不正确会报异常
     */
    public static void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(JwtConstants.SECRET).parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否失效
     * true:过期   false:没过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public static String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userId);
    }

    /**
     * 生成token
     */
    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + JwtConstants.EXPIRATION * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JwtConstants.SECRET)
                .compact();
    }
}
