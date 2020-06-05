package com.hongjf.common.rabbitmq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2019/11/19
 * @Time: 11:20
 * @Description:rabbitMq配置类
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitConfig {

    public String host;

    public String port;

    public String username;

    public String password;

    public String virtualHost;

    /**
     * json转换器
     *
     * @return
     */
    @Bean(name = "messageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, Integer.parseInt(port));
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     * 配置RabbitTemplate，设置messageConverter，序列化消息为json格式
     */
    @Bean(name = "rabbitTemplate")
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(this.jsonMessageConverter());
        template.setMandatory(true);
        return template;
    }

    /**
     * 配置消费者监听的容器
     *
     * @return
     */
    @Bean(name = "simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(10);
        factory.setMaxConcurrentConsumers(20);
        factory.setPrefetchCount(1);
        factory.setAutoStartup(true);
        return factory;
    }

}