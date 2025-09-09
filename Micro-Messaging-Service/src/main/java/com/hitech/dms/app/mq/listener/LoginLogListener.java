package com.hitech.dms.app.mq.listener;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.config.RabbitConfig;
import com.hitech.dms.app.mq.BaseConsumer;
import com.hitech.dms.app.mq.BaseConsumerProxy;
import com.hitech.dms.app.mq.consumer.LoginLogConsumer;
import com.hitech.dms.app.service.MsgLogService;
import com.rabbitmq.client.Channel;

@Component
public class LoginLogListener {

    @Autowired
    private LoginLogConsumer loginLogConsumer;

    @Autowired
    private MsgLogService msgLogService;

    @RabbitListener(queues = RabbitConfig.LOGIN_LOG_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(loginLogConsumer, msgLogService);
        BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
        if (null != proxy) {
            proxy.consume(message, channel);
        }
    }

}
