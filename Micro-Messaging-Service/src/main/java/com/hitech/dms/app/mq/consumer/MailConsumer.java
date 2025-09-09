package com.hitech.dms.app.mq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.entity.Mail;
import com.hitech.dms.app.exception.ServiceException;
import com.hitech.dms.app.mq.BaseConsumer;
import com.hitech.dms.app.mq.MessageHelper;
import com.hitech.dms.app.util.MailUtil;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailConsumer implements BaseConsumer {

	@Autowired
	private MailUtil mailUtil;

	public void consume(Message message, Channel channel) {
		Mail mail = MessageHelper.msgToObj(message, Mail.class);
		log.info("message received: {}", mail.toString());

		boolean success = mailUtil.send(mail);
		if (!success) {
			throw new ServiceException("send mail error");
		}
	}

}
