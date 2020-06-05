package com.hongjf.common.sms.manager;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 11:05
 * @Description:
 */
@Slf4j
public class MapSignManager implements MultiSignManager {

    /**
     * 缓存个数最大值
     */
    public static final int CLEAR_TIME = 1000;

    private Map<String, String> cacheMap = new ConcurrentHashMap<>();

    @Override
    public String getSign(String phone, String signName) {

        clearMap();

        String[] signNames = signName.split(",");

        //获取上次发送的时候用的哪个签名，这次换一个
        Object lastSignName = cacheMap.get(phone);
        if (lastSignName == null) {
            cacheMap.put(phone, signNames[0]);
            log.info("发送短信，签名为：" + signNames[0] + ",电话为：" + phone);
            return signNames[0];
        } else {
            for (String name : signNames) {
                if (!name.equals(lastSignName)) {
                    cacheMap.put(phone, name);
                    log.info("发送短信，签名为：" + name + ",电话为：" + phone);
                    return name;
                }
            }
            cacheMap.put(phone, signNames[0]);
            log.info("发送短信，签名为：" + signNames[0] + ",电话为：" + phone);
            return signNames[0];
        }
    }

    /**
     * 每隔一段时间清除下map
     *
     * @Date 2018/9/21 上午10:57
     */
    private void clearMap() {
        if (cacheMap.size() >= CLEAR_TIME) {
            cacheMap.clear();
        }
    }
}
