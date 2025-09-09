/**
 * 
 */
package com.hitech.dms.app.mq.listener.password;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.config.RabbitConfig;
import com.hitech.dms.app.mq.BaseConsumer;
import com.hitech.dms.app.mq.BaseConsumerProxy;
import com.hitech.dms.app.mq.consumer.MailWithTemplateConsumer;
import com.hitech.dms.app.service.MsgLogService;
import com.rabbitmq.client.Channel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class ChangePasswordListner {
	@Autowired
	private MailWithTemplateConsumer mailWithTemplateConsumer;

	@Autowired
	private MsgLogService msgLogService;

	@RabbitListener(queues = RabbitConfig.mail_change_password_queue)
	public void consume(Message message, Channel channel) throws IOException {
		BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(mailWithTemplateConsumer, msgLogService);
		BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
		if (null != proxy) {
			proxy.consume(message, channel);
		}
	}
}
