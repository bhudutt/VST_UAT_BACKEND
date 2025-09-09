/**
 * 
 */
package com.hitech.dms.app.model.mail;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Data
public class SmsMailRequestModel {
	@Id
	private BigInteger mailitem_id;
	private Integer event_id;
	@Column(name = "ReferenceId")
	private BigInteger referenceId;
	@Column(name = "FromMailId")
	private String fromMailId;
	@Column(name = "ToMailId")
	private String toMailId;
	@Column(name = "CCMailId")
	private String ccMailId;
	@Column(name = "BCCMailId")
	private String bccMailId;
	@Column(name = "MailSubject")
	private String mailSubject;
	@Column(name = "MailBodyTxt")
	private String mailBodyTxt;
	@Column(name = "MailStatus")
	private String mailStatus;
	@Column(name = "MailSentDate")
	private Date mailSentDate;
	@Column(name = "Createdby")
	private BigInteger createdBy;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "TemplateName")
	private String templateName;
}
