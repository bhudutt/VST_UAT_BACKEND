/**
 * 
 */
package com.hitech.dms.app.template.mail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hitech.dms.app.entity.MailWithTemplate;
import com.hitech.dms.app.model.mail.SmsMailRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class CreateMailTemplateModel {
	
	public MailWithTemplate setMailWithTemplateObject(SmsMailRequestModel msgLog) {
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
