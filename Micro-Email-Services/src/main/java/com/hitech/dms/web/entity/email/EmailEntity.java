/**
 * 
 */
package com.hitech.dms.web.entity.email;

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
 * @author santosh.kumar
 *
 */
@Entity
@Table(name = "CM_SMSMAIL_MAIL")
@Data
public class EmailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mailitem_id")
    private BigInteger mailItemId;

    @Column(name = "event_id")
    private BigInteger eventId;

    @Column(name = "ReferenceId")
    private String referenceId;

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

    @Column(name = "msg_id")
    private String msgId;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "Createdby")
    private String createdBy;

}
