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
public class EnqEventSubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {
	private Logger logger = LoggerFactory.getLogger(EnqEventSubscriberConfiguration.class);

	@Value("${mail_followup_enq.queue}")
	private String followupEnqQueueName;

	@Value("${mail_followup_enq.routing_key}")
	private String followupEnqRoutingKey;

	@Value("${mail_followup_enq.exchange}")
	private String followupEnqExchange;

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

	@Bean(name = "receiverExchangeForfollowupEnq")
	public TopicExchange receiverExchangeForfollowupEnq() {
		return new TopicExchange(followupEnqExchange);
	}

	@Bean(name = "followupEnqQueueName")
	public Queue followupEnqQueueName() {
		if (followupEnqQueueName == null) {
			throw new IllegalStateException(
					"No queue to listen to! Please specify the name of the queue to listen to with the property 'subscriber.queue'");
		}
		return new Queue(followupEnqQueueName);
	}

	@Bean(name = "followupEnqBinding")
	public Binding followupEnqBinding(Queue followupEnqQueueName, TopicExchange receiverExchangeForfollowupEnq) {
		if (followupEnqRoutingKey == null) {
			throw new IllegalStateException(
					"No events to listen to! Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EventPublisher for available routing keys).");
		}
		return BindingBuilder.bind(followupEnqQueueName).to(receiverExchangeForfollowupEnq).with(followupEnqRoutingKey);
	}

	@Bean(name = "followupEnqContainer")
	public SimpleMessageListenerContainer followupEnqContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter followupEnqListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(followupEnqQueueName);
		container.setMessageListener(followupEnqListenerAdapter);

		return container;
	}

	@Bean(name = "followupEnqListenerAdapter")
	public MessageListenerAdapter followupEnqListenerAdapter(FollowupEnqEventSubscriber followupEnqEventReceiver) {
		return new MessageListenerAdapter(followupEnqEventReceiver, "receive");
	}

	@Bean(name = "followupEnqEventReceiver")
	public FollowupEnqEventSubscriber followupEnqEventReceiver() {
		return new FollowupEnqEventSubscriber(msgLogService, smsMailDao, mailUtil, templateModel, mailDao);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", followupEnqRoutingKey,
				followupEnqQueueName);
	}
}
