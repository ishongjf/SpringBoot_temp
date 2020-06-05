package com.hongjf.rabbitmq.consumers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright 2020 hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/19
 * @Time: 14:13
 * @Description:
 */
@Slf4j
@Configuration
public class MsgConsumer {

    /**
     * 队列消费 queues是队列名称
     *
     * @param message 消息
     */
    /*@RabbitHandler
    @RabbitListener(queues = "hongTempQueue")
    public void processClean(@Payload Message message, Channel channel) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = objectMapper.readValue(message.getBody(), JSONObject.class);
        try {
            // false只确认当前一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("接受到的消息为[{}]", new String(message.getBody(), "UTF-8"));
        } catch (Exception e) {
            //最后一个参数是：是否重回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            //拒绝消息
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            //消息被丢失
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            //消息被重新发送
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            //多条消息被重新发送
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }*/

}
