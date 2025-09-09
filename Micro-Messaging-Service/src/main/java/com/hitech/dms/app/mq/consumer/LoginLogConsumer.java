package com.hitech.dms.app.mq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.entity.LoginLog;
import com.hitech.dms.app.mq.BaseConsumer;
import com.hitech.dms.app.mq.MessageHelper;
import com.hitech.dms.app.service.LoginLogService;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginLogConsumer implements BaseConsumer {

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void consume(Message message, Channel channel) {
		log.info("Received message: {}", message.toString());
		LoginLog loginLog = MessageHelper.msgToObj(message, LoginLog.class);
		loginLogService.insert(loginLog);
	}
}
