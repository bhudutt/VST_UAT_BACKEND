/**
 * 
 */
package com.hitech.dms.app.config.subscribers.enquiry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitech.dms.app.config.RabbitConfig;
import com.hitech.dms.app.config.subscribers.extract.StringToObjectModel;
import com.hitech.dms.app.config.subscribers.models.PublishModel;
import com.hitech.dms.app.constants.WebConstants;
import com.hitech.dms.app.entity.MailWithTemplate;
import com.hitech.dms.app.entity.MsgLog;
import com.hitech.dms.app.model.mail.SmsMailRequestModel;
import com.hitech.dms.app.repo.MailDao;
import com.hitech.dms.app.repo.SmsMailDao;
import com.hitech.dms.app.service.MsgLogService;
import com.hitech.dms.app.template.mail.CreateMailTemplateModel;
import com.hitech.dms.app.util.MailUtil;
import com.hitech.dms.app.util.RandomUtil;

/**
 * @author dinesh.jakhar
 *
 */
public class FollowupEnqEventSubscriber extends StringToObjectModel {
	private Logger logger = LoggerFactory.getLogger(ValidatedEnqEventSubscriber.class);

	private MsgLogService msgLogService;
	private SmsMailDao smsMailDao;
	private MailUtil mailUtil;
	private CreateMailTemplateModel templateModel;
	private MailDao mailDao;

	public FollowupEnqEventSubscriber(MsgLogService msgLogService, SmsMailDao smsMailDao, MailUtil mailUtil,
			CreateMailTemplateModel templateModel, MailDao mailDao) {
		this.msgLogService = msgLogService;
		this.smsMailDao = smsMailDao;
		this.mailUtil = mailUtil;
		this.templateModel = templateModel;
		this.mailDao = mailDao;
	}

	public void receive(String msg) {
		PublishModel publishModel = objToJson(msg);
		if (publishModel != null) {
			logger.info("Followup Enquiry received message '{}'", publishModel.toString());
			sentMail(publishModel);
		}
	}

	private void sentMail(PublishModel publishModel) {
		SmsMailRequestModel requestModel = smsMailDao.fetchEventMailByMailItemId(publishModel.getId());
		if (requestModel != null) {
			logger.info("requestModel '{}'", requestModel.toString());
			MailWithTemplate mail = templateModel.setMailWithTemplateObject(requestModel);
			boolean success = mailUtil.sendEmailWithTemplate(mail);
			if (success) {
				updateMsgLogData(mail, 3, requestModel, WebConstants.SENT);
			} else {
				updateMsgLogData(mail, 0, requestModel, WebConstants.PENDING);
			}
		}
	}

	private void updateMsgLogData(MailWithTemplate mail, int msgLogStatus, SmsMailRequestModel requestModel,
			String statusForQueue) {
		StringBuilder msgId = new StringBuilder(RandomUtil.UUID32());
		mail.setMsgId(msgId.toString());

		MsgLog msgLog = new MsgLog(msgId.toString(), mail, RabbitConfig.mail_enq_followup_exchange,
				RabbitConfig.mail_enq_followup_routing_key);
		msgLog.setStatus(msgLogStatus);
		boolean isInserted = mailDao.insert(msgLog);
		if (isInserted) {
			smsMailDao.updateStatusForQueue(requestModel.getMailitem_id(), msgId.toString(), statusForQueue);
		}
	}
}
