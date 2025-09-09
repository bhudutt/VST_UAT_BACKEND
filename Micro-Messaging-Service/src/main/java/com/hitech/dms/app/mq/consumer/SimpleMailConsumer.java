package com.hitech.dms.app.mq.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.common.Constant;
import com.hitech.dms.app.config.RabbitConfig;
import com.hitech.dms.app.entity.Mail;
import com.hitech.dms.app.entity.MailWithTemplate;
import com.hitech.dms.app.entity.MsgLog;
import com.hitech.dms.app.mq.MessageHelper;
import com.hitech.dms.app.service.MsgLogService;
import com.hitech.dms.app.util.MailUtil;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleMailConsumer {

	@Autowired
	private MsgLogService msgLogService;

	@Autowired
	private MailUtil mailUtil;

//	@RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
//	public void consume(Message message, Channel channel) throws IOException {
//		Mail mail = MessageHelper.msgToObj(message, Mail.class);
//		log.info("message received: {}", mail.toString());
//
//		String msgId = mail.getMsgId();
//
//		MsgLog msgLog = msgLogService.selectByMsgId(msgId);
//		if (null == msgLog || msgLog.getStatus().equals(Constant.MsgLogStatus.CONSUMED_SUCCESS)) { // consumption
//																									// idempotency
//			log.info("Repeated consumption, msgId: {}", msgId);
//			return;
//		}
//
//		MessageProperties properties = message.getMessageProperties();
//		long tag = properties.getDeliveryTag();
//
//		boolean success = mailUtil.send(mail);
//		if (success) {
//			msgLogService.updateStatus(msgId, Constant.MsgLogStatus.CONSUMED_SUCCESS);
//			channel.basicAck(tag, false); // consumption confirmation
//		} else {
//			channel.basicNack(tag, false, true);
//		}
//	}
	
	@RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
	public void consume(Message message, Channel channel) throws IOException {
		MailWithTemplate mail = MessageHelper.msgToObj(message, MailWithTemplate.class);
		log.info("message received: {}", mail.toString());

		String msgId = mail.getMsgId();

		MsgLog msgLog = msgLogService.selectByMsgId(msgId);
		if (null == msgLog || msgLog.getStatus().equals(Constant.MsgLogStatus.CONSUMED_SUCCESS)) { // consumption
																									// idempotency
			log.info("Repeated consumption, msgId: {}", msgId);
			return;
		}

		MessageProperties properties = message.getMessageProperties();
		long tag = properties.getDeliveryTag();

		boolean success = mailUtil.sendEmailWithTemplate(mail);
		if (success) {
			msgLogService.updateStatus(msgId, Constant.MsgLogStatus.CONSUMED_SUCCESS);
			channel.basicAck(tag, false); // consumption confirmation
		} else {
			channel.basicNack(tag, false, true);
		}
	}
	
}
