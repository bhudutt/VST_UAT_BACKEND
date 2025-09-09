package com.hitech.dms.app.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hitech.dms.app.common.Constant;
import com.hitech.dms.app.service.MsgLogService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitConfig {

	@Autowired
	private CachingConnectionFactory connectionFactory;

	@Autowired
	private MsgLogService msgLogService;

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());

		// Whether the message was successfully sent to Exchange
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack) {
				log.info(" Message successfully sent to Exchange");
				String msgId = correlationData.getId();
				msgLogService.updateStatus(msgId, Constant.MsgLogStatus.DELIVER_SUCCESS);
			} else {
				log.info("Failed to send message to Exchange, {}, cause: {}", correlationData, cause);
			}
		});

		// To trigger the setReturnCallback callback, mandatory=true must be set,
		// otherwise the Exchange will discard the message if it does not find the Queue
		// without triggering the callback
		rabbitTemplate.setMandatory(true);
		// Whether the message is routed from the Exchange to the Queue, note: this is a
		// failure callback, this method will be called back only if the message fails
		// to be routed from the Exchange to the Queue
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			log.info(
					"Failed to route message from Exchange to Queue: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}",
					exchange, routingKey, replyCode, replyText, message);
		});

		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	// login log
	public static final String LOGIN_LOG_QUEUE_NAME = "login.log.queue";
	public static final String LOGIN_LOG_EXCHANGE_NAME = "login.log.exchange";
	public static final String LOGIN_LOG_ROUTING_KEY_NAME = "login.log.routing.key";

	@Bean
	public Queue logUserQueue() {
		return new Queue(LOGIN_LOG_QUEUE_NAME, true);
	}

	@Bean
	public DirectExchange logUserExchange() {
		return new DirectExchange(LOGIN_LOG_EXCHANGE_NAME, true, false);
	}

	@Bean
	public Binding logUserBinding() {
		return BindingBuilder.bind(logUserQueue()).to(logUserExchange()).with(LOGIN_LOG_ROUTING_KEY_NAME);
	}

	// send email
	public static final String MAIL_QUEUE_NAME = "mail.queue";
	public static final String MAIL_EXCHANGE_NAME = "mail.exchange";
	public static final String MAIL_ROUTING_KEY_NAME = "mail.routing.key";

	@Bean
	public Queue mailQueue() {
		return new Queue(MAIL_QUEUE_NAME, true);
	}

	@Bean
	public DirectExchange mailExchange() {
		return new DirectExchange(MAIL_EXCHANGE_NAME, true, false);
	}

	@Bean
	public Binding mailBinding() {
		return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTING_KEY_NAME);
	}

	public static final String mail_forgot_password_queue = "forgotpassword.mail.queue";
	public static final String mail_forgot_password_exchange = "forgotpassword.mail.exchange";
	public static final String mail_forgot_password_routing_key = "forgotpassword.mail.routing.key";

	@Bean
	public Queue forgotPasswordQueue() {
		return new Queue(mail_forgot_password_queue, true);
	}

	@Bean
	public DirectExchange forgotPasswordExchange() {
		return new DirectExchange(mail_forgot_password_exchange, true, false);
	}

	@Bean
	public Binding forgotPasswordRoutingKey() {
		return BindingBuilder.bind(forgotPasswordQueue()).to(forgotPasswordExchange())
				.with(mail_forgot_password_routing_key);
	}

	public static final String mail_change_password_queue = "changepassword.mail.queue";
	public static final String mail_change_password_exchange = "changepassword.mail.exchange";
	public static final String mail_change_password_routing_key = "changepassword.mail.routing.key";

	@Bean
	public Queue changePasswordQueue() {
		return new Queue(mail_change_password_queue, true);
	}

	@Bean
	public DirectExchange changePasswordExchange() {
		return new DirectExchange(mail_change_password_exchange, true, false);
	}

	@Bean
	public Binding changePasswordRoutingKey() {
		return BindingBuilder.bind(changePasswordQueue()).to(changePasswordExchange())
				.with(mail_change_password_routing_key);
	}
	
	public static final String mail_enq_create_queue = "createenq.mail.queue";
	public static final String mail_enq_create_exchange = "createenq.mail.exchange";
	public static final String mail_enq_create_routing_key = "createenq.mail.routing.key";

	@Bean
	public Queue createenqQueue() {
		return new Queue(mail_enq_create_queue, true);
	}

	@Bean
	public DirectExchange createenqExchange() {
		return new DirectExchange(mail_enq_create_exchange, true, false);
	}

	@Bean
	public Binding createenqRoutingKey() {
		return BindingBuilder.bind(createenqQueue()).to(createenqExchange()).with(mail_enq_create_routing_key);
	}
	
	public static final String mail_enq_followup_queue = "enqfollowup.mail.queue";
	public static final String mail_enq_followup_exchange = "enqfollowup.mail.exchange";
	public static final String mail_enq_followup_routing_key = "enqfollowup.mail.routing.key";

	@Bean
	public Queue enqFollowupQueue() {
		return new Queue(mail_enq_followup_queue, true);
	}

	@Bean
	public DirectExchange enqFollowupExchange() {
		return new DirectExchange(mail_enq_followup_exchange, true, false);
	}

	@Bean
	public Binding enqFollowupRoutingKey() {
		return BindingBuilder.bind(enqFollowupQueue()).to(enqFollowupExchange()).with(mail_enq_followup_routing_key);
	}
	
	public static final String mail_validatedenq_queue = "validatedenq.mail.queue";
	public static final String mail_validatedenq_exchange = "validatedenq.mail.exchange";
	public static final String mail_validatedenq_routing_key = "validatedenq.mail.routing.key";

	@Bean
	public Queue validateenqQueue() {
		return new Queue(mail_validatedenq_queue, true);
	}

	@Bean
	public DirectExchange validateenqExchange() {
		return new DirectExchange(mail_validatedenq_exchange, true, false);
	}

	@Bean
	public Binding validateenqRoutingKey() {
		return BindingBuilder.bind(validateenqQueue()).to(validateenqExchange()).with(mail_validatedenq_routing_key);
	}

}
