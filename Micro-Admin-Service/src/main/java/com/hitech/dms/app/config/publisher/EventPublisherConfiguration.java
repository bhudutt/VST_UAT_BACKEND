/**
 * 
 */
package com.hitech.dms.app.config.publisher;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author dinesh.jakhar
 *
 */
@EnableScheduling
@Configuration
public class EventPublisherConfiguration {
	
	@Value("${mail_forgot_password.exchange}")
	private String forgotPasswordExchange;

	@Bean(name = "senderForgotPasswordTopicExchange")
	public TopicExchange senderForgotPasswordTopicExchange() {
		return new TopicExchange(forgotPasswordExchange);
	}
	
	@Value("${mail_change_password.exchange}")
	private String changePasswordExchange;
	
	@Bean(name = "senderChangePasswordTopicExchange")
	public TopicExchange senderChangePasswordTopicExchange() {
		return new TopicExchange(changePasswordExchange);
	}

	// uncommented
	@Bean
	public ForgotPasswordEventPublisher forgotPasswordEventPublisher(RabbitTemplate rabbitTemplate,
			TopicExchange senderForgotPasswordTopicExchange) {
		return new ForgotPasswordEventPublisher(rabbitTemplate, senderForgotPasswordTopicExchange);
	}
}
