/**
 * 
 */
package com.hitech.dms.app.controller.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.common.ServerResponse;
import com.hitech.dms.app.constants.WebConstants;
import com.hitech.dms.app.entity.MailWithTemplate;
import com.hitech.dms.app.model.mail.SmsMailRequestModel;
import com.hitech.dms.app.repo.SmsMailDao;
import com.hitech.dms.app.service.mail.MailService;
import com.hitech.dms.app.template.mail.CreateMailTemplateModel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@Component
@Slf4j
public class SmsMailEvents {
	private static final Logger logger = LoggerFactory.getLogger(SmsMailEvents.class);

	@Autowired
	private SmsMailDao smsMailDao;
	@Autowired
	private MailService mailService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private CreateMailTemplateModel templateModel;

	/**
	 * Pull the Pending Status Mail For Forgot Password Mails
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void sendForgotPasswords() {
		log.info("Start executing scheduled task (forgot password mails)");

		List<SmsMailRequestModel> msgLogs = smsMailDao.fetchEventMailsForTrigger(WebConstants.FORGOT_PASSWORD,
				WebConstants.PENDING);
		logger.debug(msgLogs != null ? msgLogs.toString() : null);
		if (msgLogs != null && !msgLogs.isEmpty()) {
			msgLogs.forEach(msgLog -> {
				MailWithTemplate mail = setMailWithTemplateObject(msgLog);
				ServerResponse response = mailService.sendPasswordWithTemplate(mail, msgLog.getMailitem_id());
				if (response != null && response.getStatus().compareTo(1) == 0) {
					smsMailDao.updateStatus(msgLog.getMailitem_id(), WebConstants.SENT);
				} else {
					smsMailDao.updateStatusBasedOnMsgLLogStatus(msgLog.getMailitem_id(), WebConstants.NOTSURE, 3);
				}
			});
		}
		log.info("End of scheduled task execution (forgot password mails)");
	}

	@Scheduled(cron = "0 0/5 * * * ?")
	public void sendChangedPasswords() {
		log.info("Start executing scheduled task (change password mails)");

		List<SmsMailRequestModel> msgLogs = smsMailDao.fetchEventMailsForTrigger(WebConstants.CHANGED_PASSWORD,
				WebConstants.PENDING);
		logger.debug(msgLogs != null ? msgLogs.toString() : null);
		if (msgLogs != null && !msgLogs.isEmpty()) {
			msgLogs.forEach(msgLog -> {
				MailWithTemplate mail = setMailWithTemplateObject(msgLog);
				ServerResponse response = mailService.sendChangeWithTemplate(mail, msgLog.getMailitem_id());
				if (response != null && response.getStatus().compareTo(1) == 0) {
					smsMailDao.updateStatus(msgLog.getMailitem_id(), WebConstants.SENT);
				} else {
					smsMailDao.updateStatusBasedOnMsgLLogStatus(msgLog.getMailitem_id(), WebConstants.NOTSURE, 3);
				}
			});
		}
		log.info("End of scheduled task execution (change password mails)");
	}

	@Scheduled(cron = "0 0/10 * * * ?")
	public void sendCreateEnquiryMails() {
		log.info("Start executing scheduled task (Create Enquiry mails)");

		List<SmsMailRequestModel> msgLogs = smsMailDao.fetchEventMailsForTrigger(WebConstants.CREATE_ENQ,
				WebConstants.PENDING);
		logger.debug(msgLogs != null ? msgLogs.toString() : null);
		if (msgLogs != null && !msgLogs.isEmpty()) {
			msgLogs.forEach(msgLog -> {
				MailWithTemplate mail = setMailWithTemplateObject(msgLog);
				ServerResponse response = mailService.sendCreateEnqWithTemplate(mail, msgLog.getMailitem_id());
				if (response != null && response.getStatus().compareTo(1) == 0) {
					smsMailDao.updateStatus(msgLog.getMailitem_id(), WebConstants.SENT);
				} else {
					smsMailDao.updateStatusBasedOnMsgLLogStatus(msgLog.getMailitem_id(), WebConstants.NOTSURE, 3);
				}
			});
		}
		log.info("End of scheduled task execution (Create Enquiry mails)");
	}

	@Scheduled(cron = "0 0/15 * * * ?")
	public void sendFollowupEnquiryMails() {
		log.info("Start executing scheduled task (Followup Enquiry mails)");
//
		List<SmsMailRequestModel> msgLogs = smsMailDao.fetchEventMailsForTrigger(WebConstants.FOLLOWUP_ENQ,
				WebConstants.PENDING);
		logger.debug(msgLogs != null ? msgLogs.toString() : null);
		if (msgLogs != null && !msgLogs.isEmpty()) {
			msgLogs.forEach(msgLog -> {
				MailWithTemplate mail = setMailWithTemplateObject(msgLog);
				ServerResponse response = mailService.sendEnqFollowupWithTemplate(mail, msgLog.getMailitem_id());
				if (response != null && response.getStatus().compareTo(1) == 0) {
					smsMailDao.updateStatus(msgLog.getMailitem_id(), WebConstants.SENT);
				} else {
					smsMailDao.updateStatusBasedOnMsgLLogStatus(msgLog.getMailitem_id(), WebConstants.NOTSURE, 3);
				}
			});
		}
		log.info("End of scheduled task execution (Followup Enquiry mails)");
	}

	@Scheduled(cron = "0 0/15 * * * ?")
	public void sendValidatedEnquiryMails() {
		log.info("Start executing scheduled task (Validated Enquiry mails)");

		List<SmsMailRequestModel> msgLogs = smsMailDao.fetchEventMailsForTrigger(WebConstants.VALIDATED_ENQ,
				WebConstants.PENDING);
		logger.debug(msgLogs != null ? msgLogs.toString() : null);
		if (msgLogs != null && !msgLogs.isEmpty()) {
			msgLogs.forEach(msgLog -> {
				MailWithTemplate mail = setMailWithTemplateObject(msgLog);
				ServerResponse response = mailService.sendValidatedEnqWithTemplate(mail, msgLog.getMailitem_id());
				if (response != null && response.getStatus().compareTo(1) == 0) {
					smsMailDao.updateStatus(msgLog.getMailitem_id(), WebConstants.SENT);
				} else {
					smsMailDao.updateStatusBasedOnMsgLLogStatus(msgLog.getMailitem_id(), WebConstants.NOTSURE, 3);
				}
			});
		}
		log.info("End of scheduled task execution (Validated Enquiry mails)");
	}

//	@Scheduled(cron = "0 0/7 * * * ?")
	public void sendRetailFollowupMails() {
//		log.info("Start executing scheduled task (Retail Followup mails)");
//
//		List<SmsMailRequestModel> msgLogs = smsMailDao.fetchEventMailsForTrigger(WebConstants.RETAIL_FOLLOWUP,
//				WebConstants.PENDING);
//		logger.debug(msgLogs != null ? msgLogs.toString() : null);
//		if (msgLogs != null && !msgLogs.isEmpty()) {
//			msgLogs.forEach(msgLog -> {
//				MailWithTemplate mail = setMailWithTemplateObject(msgLog);
//				ServerResponse response = mailService.sendWithTemplate(mail, msgLog.getMailitem_id());
//				if (response != null && response.getStatus().compareTo(1) == 0) {
//					smsMailDao.updateStatus(msgLog.getMailitem_id(), WebConstants.SENT);
//				}else {
//					smsMailDao.updateStatusBasedOnMsgLLogStatus(msgLog.getMailitem_id(), WebConstants.NOTSURE, 3);
//				}
//			});
//		}
//		log.info("End of scheduled task execution (Retail Followup mails)");
	}

	private MailWithTemplate setMailWithTemplateObject(SmsMailRequestModel msgLog) {
		MailWithTemplate mail = new MailWithTemplate();
		mail.setContent(msgLog.getMailBodyTxt());
		mail.setTitle(msgLog.getMailSubject());
		mail.setToEmailIDs(msgLog.getToMailId());
		mail.setTemplateName(msgLog.getTemplateName());
		if (msgLog.getCcMailId() != null) {
			mail.setCcEmailIDs(msgLog.getCcMailId());
		}
		if (msgLog.getBccMailId() != null) {
			mail.setBccEmailIDs(msgLog.getBccMailId());
		}
		Map<String, Object> model = new HashMap<>();
		model.put("content", mail.getContent());
		return mail;
	}
}
