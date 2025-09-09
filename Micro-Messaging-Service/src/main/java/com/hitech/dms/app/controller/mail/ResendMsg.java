/**
 * 
 */
package com.hitech.dms.app.controller.mail;

import java.util.List;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.common.Constant;
import com.hitech.dms.app.entity.MsgLog;
import com.hitech.dms.app.mq.MessageHelper;
import com.hitech.dms.app.service.MsgLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@Component
@Slf4j
public class ResendMsg {

	@Autowired
	private MsgLogService msgLogService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	// maximum number of deliveries
	private static final int MAX_TRY_COUNT = 3;

	/**
	 * Pull the failed delivery message every 30s and re-deliver
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void resend() {
		log.info("Start executing scheduled task (re-delivery message)");

		List<MsgLog> msgLogs = msgLogService.selectTimeoutMsg();
		msgLogs.forEach(msgLog -> {
			String msgId = msgLog.getMsgId();
			if (msgLog.getTryCount() >= MAX_TRY_COUNT) {
				msgLogService.updateStatus(msgId, Constant.MsgLogStatus.DELIVER_FAIL);
				log.info("Exceeded maximum number of retries, message delivery failed, msgId: {}", msgId);
			} else {
				msgLogService.updateTryCount(msgId, msgLog.getNextTryTime()); // delivery times +1

				CorrelationData correlationData = new CorrelationData(msgId);
				rabbitTemplate.convertAndSend(msgLog.getExchange(), msgLog.getRoutingKey(),
						MessageHelper.objToMsg(msgLog.getMsg()), correlationData); // redeliver

				log.info(" th " + (msgLog.getTryCount() + 1) + " redelivery message");
			}
		});

		log.info("End of scheduled task execution (re-delivery message)");
	}

}