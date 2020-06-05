package com.hongjf.common.rabbitmq;

import com. hongjf.common.utils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/19
 * @Time: 14:24
 * @Description:
 */
@Slf4j
@Configuration
public class BindingConfig {

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    /**
     * driectQueue初始化
     *
     * @return
     */
    @Bean
    public List<Queue> driectQueue(RabbitAdmin rabbitAdmin) {
        List<Queue> queuesList = null;
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        List<String> queueList = ListUtil.strToListStr(rabbitMQProperties.getDriectKey(), ",");
        queuesList = queueList.stream().map(new Function<String, Queue>() {
            @Override
            public Queue apply(String name) {
                Queue queue = new Queue(name, durable, exclusive, autoDelete);
                rabbitAdmin.declareQueue(queue);
                return queue;
            }
        }).collect(toList());
        return queuesList;
    }

    /**
     * defaultExchange初始化
     *
     * @return
     */
    @Bean
    public DirectExchange defaultExchange(RabbitAdmin rabbitAdmin) {
        DirectExchange directExchange = new DirectExchange(rabbitMQProperties.getDriectExchange(), true, false);
        rabbitAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public List<Binding> binding(RabbitAdmin rabbitAdmin) {
        List<Queue> queueList = driectQueue(rabbitAdmin);
        List<Binding> bindingList = new ArrayList<>(queueList.size());

        if (rabbitMQProperties.getDriectKey() == null || "".equals(rabbitMQProperties.getDriectKey())) {
            log.error("rabbitmq配置错误,缺少对应的routingKey.");
            return null;
        }
        List<String> queueKeyList = ListUtil.strToListStr(rabbitMQProperties.getDriectKey(), ",");

        for (int i = 0; i < queueList.size(); i++) {
            if (queueList.get(i) == null || "".equals(queueList.get(i))) {
                continue;
            }
            Binding binding = BindingBuilder.bind(queueList.get(i)).to(defaultExchange(rabbitAdmin)).with(queueKeyList.get(i));
            rabbitAdmin.declareBinding(binding);
            bindingList.add(BindingBuilder.bind(queueList.get(i)).to(defaultExchange(rabbitAdmin)).with(queueKeyList.get(i)));
        }
        return bindingList;
    }
}
