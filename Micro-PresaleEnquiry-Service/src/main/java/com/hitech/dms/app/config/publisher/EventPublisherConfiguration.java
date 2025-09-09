///**
// * 
// */
//package com.hitech.dms.app.config.publisher;
//
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//
////@EnableScheduling
////@Configuration
//public class EventPublisherConfiguration {
//	
//	@Value("${mail_create_enq.exchange}")
//	private String createEnqExchange;
//
//	@Bean(name = "senderCreateEnqTopicExchange")
//	public TopicExchange senderCreateEnqTopicExchange() {
//		return new TopicExchange(createEnqExchange);
//	}
//	
//	@Value("${mail_validated_enq.exchange}")
//	private String validatedEnqExchange;
//	
//	@Bean(name = "senderValidatedEnqTopicExchange")
//	public TopicExchange senderValidatedEnqTopicExchange() {
//		return new TopicExchange(validatedEnqExchange);
//	}
//	
//	@Value("${mail_followup_enq.exchange}")
//	private String followupEnqExchange;
//	
//	@Bean(name = "senderfollowupEnqTopicExchange")
//	public TopicExchange senderfollowupEnqTopicExchange() {
//		return new TopicExchange(followupEnqExchange);
//	}
//	
//	@Value("${mail_retailfollowup_enq.exchange}")
//	private String retailFollowupEnqExchange;
//	
//	@Bean(name = "senderRetailFollowupEnqTopicExchange")
//	public TopicExchange senderRetailFollowupEnqTopicExchange() {
//		return new TopicExchange(retailFollowupEnqExchange);
//	}
//}
