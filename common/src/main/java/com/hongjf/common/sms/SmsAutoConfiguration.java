package com.hongjf.common.sms;

import com. hongjf.common.sms.manager.MapSignManager;
import com. hongjf.common.sms.manager.SmsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 10:59
 * @Description:aliyun短信发送的配置
 */
@Configuration
public class SmsAutoConfiguration {

    @Bean
    public SmsManager smsManager() {
        return new AliyunSmsManager();
    }

    @Bean
    public MapSignManager mapSignManager() {
        return new MapSignManager();
    }

}
