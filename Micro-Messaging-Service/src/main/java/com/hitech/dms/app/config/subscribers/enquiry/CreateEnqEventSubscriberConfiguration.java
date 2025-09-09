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

import com.hitech.dms.app.config.subscribers.password.ChangePwdEventSubscriber;
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
public class CreateEnqEventSubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {
	private Logger logger = LoggerFactory.getLogger(CreateEnqEventSubscriberConfiguration.class);
	
	@Value("${mail_create_enq.queue}")
	private String createEnqQueueName;

	@Value("${mail_create_enq.routing_key}")
	private String createEnqRoutingKey;

	@Value("${mail_create_enq.exchange}")
	private String createEnqExchange;
	
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

	@Bean(name = "receiverExchangeForcreateEnq")
	public TopicExchange receiverExchangeForcreateEnq() {
		return new TopicExchange(createEnqExchange);
	}

	@Bean(name = "createEnqQueueName")
	public Queue createEnqQueueName() {
		if (createEnqQueueName == null) {
			throw new IllegalStateException(
					"No queue to listen to! Please specify the name of the queue to listen to with the property 'subscriber.queue'");
		}
		return new Queue(createEnqQueueName);
	}

	@Bean(name = "createEnqBinding")
	public Binding createEnqBinding(Queue createEnqQueueName,
			TopicExchange receiverExchangeForcreateEnq) {
		if (createEnqRoutingKey == null) {
			throw new IllegalStateException(
					"No events to listen to! Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EventPublisher for available routing keys).");
		}
		return BindingBuilder.bind(createEnqQueueName).to(receiverExchangeForcreateEnq)
				.with(createEnqRoutingKey);
	}

	@Bean(name = "createEnqContainer")
	public SimpleMessageListenerContainer createEnqContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter createEnqListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(createEnqQueueName);
		container.setMessageListener(createEnqListenerAdapter);

		return container;
	}

	@Bean(name = "createEnqListenerAdapter")
	public MessageListenerAdapter createEnqListenerAdapter(CreateEnqEventSubscriber createEnqEventReceiver) {
		return new MessageListenerAdapter(createEnqEventReceiver, "receive");
	}

	@Bean(name = "createEnqEventReceiver")
	public CreateEnqEventSubscriber createEnqEventReceiver() {
		return new CreateEnqEventSubscriber(msgLogService, smsMailDao, mailUtil, templateModel, mailDao);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", createEnqRoutingKey,
				createEnqQueueName);
	}
}
