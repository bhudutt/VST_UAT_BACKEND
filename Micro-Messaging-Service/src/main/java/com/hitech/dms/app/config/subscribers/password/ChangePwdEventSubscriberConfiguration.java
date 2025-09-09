/**
 * 
 */
package com.hitech.dms.app.config.subscribers.password;

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
public class ChangePwdEventSubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {
	private Logger logger = LoggerFactory.getLogger(ChangePwdEventSubscriberConfiguration.class);

	@Value("${mail_change_password.queue}")
	private String changePasswordQueueName;

	@Value("${mail_change_password.routing_key}")
	private String changePasswordRoutingKey;

	@Value("${mail_change_password.exchange}")
	private String changePasswordExchange;
	
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

	@Bean(name = "receiverExchangeForchangePassword")
	public TopicExchange receiverExchangeForchangePassword() {
		return new TopicExchange(changePasswordExchange);
	}

	@Bean(name = "changePasswordQueueName")
	public Queue changePasswordQueueName() {
		if (changePasswordQueueName == null) {
			throw new IllegalStateException(
					"No queue to listen to! Please specify the name of the queue to listen to with the property 'subscriber.queue'");
		}
		return new Queue(changePasswordQueueName);
	}

	@Bean(name = "changePasswordBinding")
	public Binding changePasswordBinding(Queue changePasswordQueueName,
			TopicExchange receiverExchangeForchangePassword) {
		if (changePasswordRoutingKey == null) {
			throw new IllegalStateException(
					"No events to listen to! Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EventPublisher for available routing keys).");
		}
		return BindingBuilder.bind(changePasswordQueueName).to(receiverExchangeForchangePassword)
				.with(changePasswordRoutingKey);
	}

	@Bean(name = "changePasswordContainer")
	public SimpleMessageListenerContainer changePasswordContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter changePasswordListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(changePasswordQueueName);
		container.setMessageListener(changePasswordListenerAdapter);

//        container.setMessageListener(new MessageListenerAdapter(new Object() {
//            void handleMessage(Object in) {
//                System.out.println(in.getClass() + "\n" + in);
//            }
//        }, converter));

		return container;
	}

	@Bean(name = "changePasswordListenerAdapter")
	public MessageListenerAdapter changePasswordListenerAdapter(ChangePwdEventSubscriber changePasswordEventReceiver) {
		return new MessageListenerAdapter(changePasswordEventReceiver, "receive");
	}

	@Bean(name = "changePasswordEventReceiver")
	public ChangePwdEventSubscriber changePasswordEventReceiver() {
		return new ChangePwdEventSubscriber(msgLogService, smsMailDao, mailUtil, templateModel, mailDao);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", changePasswordRoutingKey,
				changePasswordQueueName);
	}

}
