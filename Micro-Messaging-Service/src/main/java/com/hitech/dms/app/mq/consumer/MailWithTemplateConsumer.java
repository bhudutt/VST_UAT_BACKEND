/**
 * 
 */
package com.hitech.dms.app.mq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.entity.MailWithTemplate;
import com.hitech.dms.app.exception.ServiceException;
import com.hitech.dms.app.mq.BaseConsumer;
import com.hitech.dms.app.mq.MessageHelper;
import com.hitech.dms.app.util.MailUtil;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@Component
@Slf4j
public class MailWithTemplateConsumer implements BaseConsumer {
	@Autowired
	private MailUtil mailUtil;

	public void consume(Message message, Channel channel) {
		MailWithTemplate mail = MessageHelper.msgToObj(message, MailWithTemplate.class);
		log.info("message received: {}", mail.toString());

		boolean success = mailUtil.sendEmailWithTemplate(mail);
		if (!success) {
			throw new ServiceException("send mail error");
		}
	}
}
