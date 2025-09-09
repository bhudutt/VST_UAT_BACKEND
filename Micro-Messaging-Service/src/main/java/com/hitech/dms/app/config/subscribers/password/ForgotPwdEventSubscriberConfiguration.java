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
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
public class ForgotPwdEventSubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {

	private Logger logger = LoggerFactory.getLogger(ForgotPwdEventSubscriberConfiguration.class);

	@Value("${mail_forgot_password.queue}")
	private String forgotPasswordQueueName;

	@Value("${mail_forgot_password.routing_key}")
	private String forgotPasswordRoutingKey;

	@Value("${mail_forgot_password.exchange}")
	private String forgotPasswordExchange;
	
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

	@Bean(name = "receiverExchangeForForgotPassword")
	public TopicExchange receiverExchangeForForgotPassword() {
		return new TopicExchange(forgotPasswordExchange);
	}

	@Bean(name = "forgotPasswordQueueName")
	public Queue forgotPasswordQueueName() {
		if (forgotPasswordQueueName == null) {
			throw new IllegalStateException(
					"No queue to listen to! Please specify the name of the queue to listen to with the property 'subscriber.queue'");
		}
		return new Queue(forgotPasswordQueueName);
	}

	@Bean(name = "forgotPasswordBinding")
	public Binding forgotPasswordBinding(Queue forgotPasswordQueueName, TopicExchange receiverExchangeForForgotPassword) {
		if (forgotPasswordRoutingKey == null) {
			throw new IllegalStateException(
					"No events to listen to! Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EventPublisher for available routing keys).");
		}
		return BindingBuilder.bind(forgotPasswordQueueName).to(receiverExchangeForForgotPassword).with(forgotPasswordRoutingKey);
	}
	
	@Autowired
	private Jackson2JsonMessageConverter converter;

	@Bean(name = "forgotPasswordContainer")
	public SimpleMessageListenerContainer forgotPasswordContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter forgotPasswordListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(forgotPasswordQueueName);
		container.setMessageListener(forgotPasswordListenerAdapter);
		
//        container.setMessageListener(new MessageListenerAdapter(new Object() {
//            void handleMessage(Object in) {
//                System.out.println(in.getClass() + "\n" + in);
//            }
//        }, converter));
		
		return container;
	}

	@Bean(name = "forgotPasswordListenerAdapter")
	public MessageListenerAdapter forgotPasswordListenerAdapter(ForgotPwdEventSubscriber forgotPasswordEventReceiver) {
		return new MessageListenerAdapter(forgotPasswordEventReceiver, "receive");
	}

	@Bean(name = "forgotPasswordEventReceiver")
	public ForgotPwdEventSubscriber forgotPasswordEventReceiver() {
		return new ForgotPwdEventSubscriber(msgLogService, smsMailDao, mailUtil, templateModel, mailDao);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", forgotPasswordRoutingKey, forgotPasswordQueueName);
	}
}
