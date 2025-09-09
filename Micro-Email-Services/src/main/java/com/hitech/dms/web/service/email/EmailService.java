/**
 * 
 */
package com.hitech.dms.web.service.email;

import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.email.EmailEntity;

/**
 * @author santosh.kumar
 *
 */
public interface EmailService {

	/**
	 * @param file
	 * @param to
	 * @param cc
	 * @param bcc 
	 * @param subject
	 * @param body
	 * @return
	 */
	String sendMail(MultipartFile[] file,String from, String[] to, String[] cc, String[] bcc, String subject, String body);

	/**
	 * @param userCode
	 * @param targetDate
	 * @return
	 */
	List<EmailEntity> getEmailConfigDetails(String targetDate);

	/**
	 * @param mailItemId
	 * @return 
	 */
	String updateMailStatus(BigInteger mailItemId);

}
