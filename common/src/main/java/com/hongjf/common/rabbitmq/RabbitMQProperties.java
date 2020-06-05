package com.hongjf.common.rabbitmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/23
 * @Time: 16:24
 * @Description:
 */
@Data
@Configuration
public class RabbitMQProperties {

    /**
     * driectKey
     */
    @Value("${spring.rabbitmq.key.driectKey}")
    private String driectKey;
    /**
     * driectExchange
     */
    @Value("${spring.rabbitmq.exchange.driectExchange}")
    private String driectExchange;

}
