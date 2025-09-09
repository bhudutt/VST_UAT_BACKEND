package com.hitech.dms.app.service;

import java.math.BigInteger;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.common.ResponseCode;
import com.hitech.dms.app.common.ServerResponse;
import com.hitech.dms.app.config.RabbitConfig;
import com.hitech.dms.app.constants.WebConstants;
import com.hitech.dms.app.entity.Mail;
import com.hitech.dms.app.entity.MailWithTemplate;
import com.hitech.dms.app.entity.MsgLog;
import com.hitech.dms.app.mq.MessageHelper;
import com.hitech.dms.app.repo.MailDao;
import com.hitech.dms.app.repo.SmsMailDao;
import com.hitech.dms.app.service.mail.MailService;
import com.hitech.dms.app.util.RandomUtil;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private MailDao mailDao;

	@Autowired
	private SmsMailDao smsMailDao;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public ServerResponse testIdempotence() {
		return ServerResponse.success("testIdempotence: success");
	}

	@Override
	public ServerResponse accessLimit() {
		return ServerResponse.success("accessLimit: success");
	}

	@Override
	public ServerResponse send(Mail mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}

	@Override
	public ServerResponse sendWithTemplate(MailWithTemplate mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}
	
	@Override
	public ServerResponse sendPasswordWithTemplate(MailWithTemplate mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.mail_forgot_password_exchange, RabbitConfig.mail_forgot_password_routing_key);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.mail_forgot_password_exchange, RabbitConfig.mail_forgot_password_routing_key,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}
	
	@Override
	public ServerResponse sendChangeWithTemplate(MailWithTemplate mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.mail_change_password_exchange, RabbitConfig.mail_change_password_routing_key);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.mail_change_password_exchange, RabbitConfig.mail_change_password_routing_key,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}
	
	@Override
	public ServerResponse sendCreateEnqWithTemplate(MailWithTemplate mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.mail_enq_create_exchange, RabbitConfig.mail_enq_create_routing_key);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.mail_enq_create_exchange, RabbitConfig.mail_enq_create_routing_key,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}
	
	@Override
	public ServerResponse sendEnqFollowupWithTemplate(MailWithTemplate mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.mail_enq_followup_exchange, RabbitConfig.mail_enq_followup_routing_key);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.mail_enq_followup_exchange, RabbitConfig.mail_enq_followup_routing_key,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}
	
	@Override
	public ServerResponse sendValidatedEnqWithTemplate(MailWithTemplate mail, BigInteger mailItemId) {
		String msgId = RandomUtil.UUID32();
		mail.setMsgId(msgId);

		MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.mail_validatedenq_exchange, RabbitConfig.mail_validatedenq_routing_key);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			if (mailItemId != null) {
				smsMailDao.updateStatusForQueue(mailItemId, msgId, WebConstants.PENDING_QUEUE);
			}
			CorrelationData correlationData = new CorrelationData(msgId);
			rabbitTemplate.convertAndSend(RabbitConfig.mail_validatedenq_exchange, RabbitConfig.mail_validatedenq_routing_key,
					MessageHelper.objToMsg(mail), correlationData);

			return ServerResponse.success(ResponseCode.MAIL_SEND_SUCCESS.getMsg());
		} else {
			return ServerResponse.success(ResponseCode.MAIL_SEND_ERROR.getMsg());
		}
	}

}
