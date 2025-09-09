/**
 * 
 */
package com.hitech.dms.app.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "CM_SMSMAIL_MAIL")
@Data
public class SmsMail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3265517843939631263L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private String createdBy;
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	private transient String templateName;
}
