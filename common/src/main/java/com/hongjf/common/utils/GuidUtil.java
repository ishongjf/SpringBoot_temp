package com.hongjf.common.utils;


import java.sql.Timestamp;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright 2019 OnlySilence, Inc. All rights reserved.
 *
 * @Author: MuYa
 * @Date: 2019/9/21
 * @Time: 22:25
 * @Description: 生成指定名称
 */
public class GuidUtil {

    /**
     * 生成一个GUID
     *
     * @return GUID
     */
    public static String generateGUID() {
        //生成正确的UUID
        String guid = UUID.randomUUID().toString();
        //去除自带的特殊字符
        guid = guid.replaceAll(":", "");
        guid = guid.replaceAll("-", "");
        return guid.toUpperCase();
    }

    /**
     * 生成编码
     *
     * @param prefix
     * @return
     */
    public static String generatorIdCode(String prefix) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeStr = time.toString().substring(0, 19).replaceAll("-", "")
                .replaceAll(":", "").replaceAll(" ", "");
        StringBuilder sb = new StringBuilder();
        return sb.append(prefix).append(timeStr).append(generateGUID().substring(0, 4)).toString();
    }

    /**
     * 获取指定字符出现的次数
     * Java正则表达式Pattern和Matcher类
     *
     * @param srcStr
     * @param findStr
     * @return
     */
    public static int count(String srcStr, String findStr) {
        int count = 0;
        // 通过静态方法compile(String regex)方法来创建,将给定的正则表达式编译并赋予给Pattern类
        Pattern pattern = Pattern.compile(findStr);
        Matcher matcher = pattern.matcher(srcStr);
        // boolean find() 对字符串进行匹配,匹配到的字符串可以在任何位置
        while (matcher.find()) {
            count++;
        }
        return count;
    }

}
