package com.hitech.dms.app.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.hitech.dms.app.entity.Mail;
import com.hitech.dms.app.entity.MailToMultiple;
import com.hitech.dms.app.entity.MailWithAttachment;
import com.hitech.dms.app.entity.MailWithTemplate;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailUtil {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
	@Value("${spring.mail.from}")
	private String from;
	@Qualifier("getFreeMarkerConfiguration")
	@Autowired
	Configuration fmConfiguration;
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Send simple mail
	 *
	 * @param mail
	 */
	public boolean send(Mail mail) {
		String to = mail.getTo(); // target mailbox
		String title = mail.getTitle(); // mail title
		String content = mail.getContent(); // mail body
		log.debug(from + " : " + to + " : " + title + " : " + content);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(title);
		message.setText(content);

		try {
			mailSender.send(message);
			log.info(" Mail sent successfully");
			return true;
		} catch (MailException e) {
			log.error("Failed to send email, to: {}, title: {}", to, title, e);
			return false;
		}
	}

	/**
	 * Send simple mail
	 *
	 * @param mail
	 */
	public boolean sendToMultiple(MailToMultiple mail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		String[] mailAddress_TO = null;
		String[] mailAddress_CC = null;
		String[] mailAddress_BCC = null;
		if (mail.getToEmailIDs() != null) {
			mailAddress_TO = mail.getToEmailIDs().split(",");
			message.setTo(mailAddress_TO);
		}
		if (mail.getCcEmailIDs() != null) {
			mailAddress_CC = mail.getCcEmailIDs().split(",");
			message.setCc(mailAddress_CC);
		}
		if (mail.getBccEmailIDs() != null) {
			mailAddress_BCC = mail.getBccEmailIDs().split(",");
			message.setBcc(mailAddress_BCC);
		}
		message.setSubject(mail.getTitle());
		message.setText(mail.getContent());
		try {
			mailSender.send(message);
			log.info(" Mail sent successfully");
			return true;
		} catch (MailException e) {
			log.error("Failed to send email, to: {}, title: {}", mail.getToEmailIDs(), mail.getTitle(), e);
			return false;
		}
	}

	/**
	 * Send attachment email
	 *
	 * @param mail mail
	 * @param file attachment
	 */
	public boolean sendAttachment(Mail mail, File file) {
		String to = mail.getTo();
		String title = mail.getTitle();
		String content = mail.getContent();

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(title);
			helper.setText(content);
			FileSystemResource resource = new FileSystemResource(file);
			String fileName = file.getName();
			helper.addAttachment(fileName, resource);
			mailSender.send(message);
			log.info(" Attachment email sent successfully");
			return true;
		} catch (Exception e) {
			log.error("Failed to send attachment email, to: {}, title: {}", to, title, e);
			return false;
		}
	}

	public boolean sendToMultipleWithAttachment(MailWithAttachment mail) {
		String to = mail.getToEmailIDs();
		String title = mail.getTitle();
		String content = mail.getContent();
		try {
			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(from);
			
			message.setSubject(title);
			message.setText(content);

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			InternetAddress[] mailAddress_TO = null;
			InternetAddress[] mailAddress_CC = null;
			InternetAddress[] mailAddress_BCC = null;
			int i = 0;
			if (mail.getToEmailIDs() != null) {
				String[] mailAddress_TOArr = to.split(",");
				mailAddress_TO = new InternetAddress[mailAddress_TOArr.length];
				for (String toAdd : mailAddress_TOArr) {
					mailAddress_TO[i] = new InternetAddress(toAdd);
					i++;
				}
				helper.setTo(mailAddress_TO);
			}
			if (mail.getCcEmailIDs() != null) {
				String[] mailAddress_CCArr = mail.getCcEmailIDs().split(",");
				i = 0;
				mailAddress_CC = new InternetAddress[mailAddress_CCArr.length];
				for (String cc : mailAddress_CCArr) {
					mailAddress_CC[i] = new InternetAddress(cc);
					i++;
				}
				helper.setCc(mailAddress_CC);
			}
			if (mail.getBccEmailIDs() != null) {
				String[] mailAddress_BCCArr = mail.getBccEmailIDs().split(",");
				i = 0;
				mailAddress_BCC = new InternetAddress[mailAddress_BCCArr.length];
				for (String bcc : mailAddress_BCCArr) {
					mailAddress_BCC[i] = new InternetAddress(bcc);
					i++;
				}
				helper.setBcc(mailAddress_BCC);
			}
			helper.setCc(mailAddress_CC);
			helper.setBcc(mailAddress_BCC);
			if (mail.getAttachment() != null) {
				FileSystemResource resource = new FileSystemResource(mail.getAttachment());
				String fileName = mail.getAttachment().getName();
				helper.addAttachment(fileName, resource);
			}
			mailSender.send(message);
			log.info(" Attachment email sent successfully");
			return true;
		} catch (Exception e) {
			log.error("Failed to send attachment email, to: {}, title: {}", to, title, e);
			return false;
		}
	}

	public boolean sendEmailWithTemplate(MailWithTemplate mail) {
		String to = mail.getToEmailIDs();
		String title = mail.getTitle();
		boolean isSent = false;
//		logger.info("MailWithTemplate '{}'", mail.toString());
		try {
			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(from);
			InternetAddress[] mailAddress_TO = null;
			InternetAddress[] mailAddress_CC = null;
			InternetAddress[] mailAddress_BCC = null;
			message.setSubject(title);

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
			int i = 0;
			if (mail.getToEmailIDs() != null) {
				String[] mailAddress_TOArr = to.split(",");
				mailAddress_TO = new InternetAddress[mailAddress_TOArr.length];
				for (String toAdd : mailAddress_TOArr) {
					mailAddress_TO[i] = new InternetAddress(toAdd);
					i++;
				}
				mimeMessageHelper.setTo(mailAddress_TO);
			}
			if (mail.getCcEmailIDs() != null) {
				String[] mailAddress_CCArr = mail.getCcEmailIDs().split(",");
				i = 0;
				mailAddress_CC = new InternetAddress[mailAddress_CCArr.length];
				for (String cc : mailAddress_CCArr) {
					mailAddress_CC[i] = new InternetAddress(cc);
					i++;
				}
				mimeMessageHelper.setCc(mailAddress_CC);
			}
			if (mail.getBccEmailIDs() != null) {
				String[] mailAddress_BCCArr = mail.getBccEmailIDs().split(",");
				i = 0;
				mailAddress_BCC = new InternetAddress[mailAddress_BCCArr.length];
				for (String bcc : mailAddress_BCCArr) {
					mailAddress_BCC[i] = new InternetAddress(bcc);
					i++;
				}
				mimeMessageHelper.setBcc(mailAddress_BCC);
			}
			if (mail.getTemplateName() != null) {
				if (mail.getModel() == null || mail.getModel().get("content") == null) {
					Map<String, Object> model = new HashMap<>();
					model.put("content", mail.getContent());
					mail.setModel(model);
				}
				mail.setContent(geContentFromTemplate(mail.getModel(), mail.getTemplateName()));
			}
			mimeMessageHelper.setText(mail.getContent(), true);
			if (mail.getAttachment() != null) {
				FileSystemResource resource = new FileSystemResource(mail.getAttachment());
				String fileName = mail.getAttachment().getName();
				mimeMessageHelper.addAttachment(fileName, resource);
			}
			log.info(mail != null ? mail.toString() : null);
			mailSender.send(mimeMessageHelper.getMimeMessage());
			isSent = true;
		} catch (MessagingException e) {
			e.printStackTrace();
			isSent = false;
		}
		return isSent;
	}

	public String geContentFromTemplate(Map<String, Object> model, String templateName) {
		StringBuffer content = new StringBuffer();

		try {
			content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName),
					model));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

}
