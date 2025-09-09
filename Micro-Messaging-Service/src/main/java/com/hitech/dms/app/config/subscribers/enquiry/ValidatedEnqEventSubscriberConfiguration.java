/**
 * 
 */
package com.hitech.dms.app.config.subscribers.enquiry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hitech.dms.app.repo.MailDao;
import com.hitech.dms.app.repo.SmsMailDao;
import com.hitech.dms.app.service.MsgLogService;
import com.hitech.dms.app.template.mail.CreateMailTemplateModel;
import com.hitech.dms.app.util.MailUtil;

/**
 * @author dinesh.jakhar
 *
 */
@Configuration
public class ValidatedEnqEventSubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {
	private Logger logger = LoggerFactory.getLogger(ValidatedEnqEventSubscriberConfiguration.class);
	
	@Value("${mail_validated_enq.queue}")
	private String validatedEnqQueueName;

	@Value("${mail_validated_enq.routing_key}")
	private String validatedEnqRoutingKey;

	@Value("${mail_validated_enq.exchange}")
	private String validatedEnqExchange;
	
	@Autowired
	private MsgLogService msgLogService;
	@Autowired
	private SmsMailDao smsMailDao;
	@Autowired
	private MailUtil mailUtil;
	@Autowired
	private CreateMailTemplateModel templateModel;
	@Autowired
	private MailDao mailDao;

	@Bean(name = "receiverExchangeForvalidatedEnq")
	public TopicExchange receiverExchangeForvalidatedEnq() {
		return new TopicExchange(validatedEnqExchange);
	}

	@Bean(name = "validatedEnqQueueName")
	public Queue validatedEnqQueueName() {
		if (validatedEnqQueueName == null) {
			throw new IllegalStateException(
					"No queue to listen to! Please specify the name of the queue to listen to with the property 'subscriber.queue'");
		}
		return new Queue(validatedEnqQueueName);
	}

	@Bean(name = "validatedEnqBinding")
	public Binding validatedEnqBinding(Queue validatedEnqQueueName,
			TopicExchange receiverExchangeForvalidatedEnq) {
		if (validatedEnqRoutingKey == null) {
			throw new IllegalStateException(
					"No events to listen to! Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EventPublisher for available routing keys).");
		}
		return BindingBuilder.bind(validatedEnqQueueName).to(receiverExchangeForvalidatedEnq)
				.with(validatedEnqRoutingKey);
	}

	@Bean(name = "validatedEnqContainer")
	public SimpleMessageListenerContainer validatedEnqContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter validatedEnqListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(validatedEnqQueueName);
		container.setMessageListener(validatedEnqListenerAdapter);

		return container;
	}

	@Bean(name = "validatedEnqListenerAdapter")
	public MessageListenerAdapter validatedEnqListenerAdapter(ValidatedEnqEventSubscriber validatedEnqEventReceiver) {
		return new MessageListenerAdapter(validatedEnqEventReceiver, "receive");
	}

	@Bean(name = "validatedEnqEventReceiver")
	public ValidatedEnqEventSubscriber validatedEnqEventReceiver() {
		return new ValidatedEnqEventSubscriber(msgLogService, smsMailDao, mailUtil, templateModel, mailDao);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", validatedEnqRoutingKey,
				validatedEnqQueueName);
	}
}
