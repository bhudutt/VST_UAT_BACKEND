
package com.hitech.dms.web.service.email;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.email.EmailEntity;
import com.hitech.dms.web.repository.email.EmailRepository;

@Service
public class EmailServiceImpl implements EmailService {
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private String port;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Environment environment;

	@Autowired
	private EmailRepository repo;

	@Override
	public String sendMail(MultipartFile[] files,String from, String[] to, String[] cc, String[] bcc, String subject, String body) {
		try {
//			System.out.println("fromEmail:::::::::::::::" + fromEmail);
//			System.out.println(environment.getProperty("spring.mail.username"));
//			System.out.println(environment.getProperty("spring.mail.password"));
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			StringBuilder sb=new StringBuilder();
			for(String toMail:to) {
				sb.append(toMail+",");
			}
			
			StringBuilder sbcc=new StringBuilder();
			for(String ccMail:cc) {
				sbcc.append(ccMail+",");
			}
			
			StringBuilder sbbcc=new StringBuilder();
			for(String bccMail:bcc) {
				sbbcc.append(bccMail+",");
			}
			

			mimeMessage.setFrom(new InternetAddress(from));
			mimeMessage.setRecipients(MimeMessage.RecipientType.TO, sb.toString());
			System.out.println("to-: "+sb.toString());
			
			if(cc != null)
			 { 
				mimeMessage.setRecipients(MimeMessage.RecipientType.CC, sbcc.toString()+"anand.m@vsttractors.com");
				System.out.println("sbcc-: "+sbcc.toString());
			 }
			
			if (bcc != null) {
				mimeMessage.setRecipients(MimeMessage.RecipientType.BCC, sbbcc.toString());
				System.out.println("sbbcc-: "+sbbcc.toString());
			}
			
			mimeMessage.setSubject(subject);

		   
		    mimeMessage.setContent(body, "text/html; charset=utf-8");
			
			
			
			
			/*
			 * //MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
			 * true); MimeMessageHelper mimeMessageHelper = new
			 * MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
			 * 
			 * mimeMessageHelper.setFrom(from); mimeMessageHelper.setTo(to); if (cc != null)
			 * { mimeMessageHelper.setCc(cc); } if (bcc != null) {
			 * mimeMessageHelper.setBcc(bcc); } mimeMessageHelper.setSubject(subject);
			 * //mimeMessageHelper.setText(body);
			 * //mimeMessageHelper.setText(body,"text/html");
			 * mimeMessageHelper.setText(body,true);
			 * 
			 * if (files != null) { for (MultipartFile file : files) {
			 * mimeMessageHelper.addAttachment(file.getOriginalFilename(), new
			 * ByteArrayResource(file.getBytes())); } }
			 */
		
			javaMailSender.send(mimeMessage);

			return "Mail sent successfully!";

		} catch (Exception e) {
			throw new RuntimeException("Failed to send mail", e);
		}
	}

	@Override
	public List<EmailEntity> getEmailConfigDetails(String targetDate) {

		return repo.getEmailConfigDetails(targetDate);
	}

	@Override
	public String updateMailStatus(BigInteger mailItemId) {
		return repo.updateMailStatus(mailItemId);

	}

}
