package com.hongjf.common.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2019/10/25
 * @Time: 14:15
 * @Description:
 */
@Slf4j
@Configuration
public class MsgProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 存放消息
     *
     * @param content    消息字符串
     * @param exchange   exchange
     * @param routingKey routingKey
     */
    public void sendMsg(String content, String exchange, String routingKey) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入对应的队列当中去
        rabbitTemplate.convertAndSend(exchange, routingKey, content, correlationId);
    }

    /**
     * 存放消息
     *
     * @param content    消息对象
     * @param exchange   exchange
     * @param routingKey routingKey
     */
    public void sendObjectMsg(Object content, String exchange, String routingKey) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Message message = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(content)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        message.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MessageProperties.CONTENT_TYPE_JSON);
        //把消息放入对应的队列当中去
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationId);
    }

    /**
     * exchange失败回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData.getId());
        if (ack) {
            log.info("发送消息成功");
        } else {
            log.info("发送消息失败:" + cause);
        }
    }

    /**
     * 发送给queue失败回调
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("return exchange: [{}], routingKey: [{}], replyCode: [{}], replyText: [{}]" + replyText, exchange, routingKey, replyCode, replyCode);
    }
}
