/**
 * 
 */
package com.hitech.dms.web.entity.user.accesslog;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_USR_ACTIVITY_LOG")
@Data
public class UserActivityLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7352735975339930084L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	private String userCode;
	private String ip;
	private String method;
	private String event; 
	private String url;
	private String page;
	private String queryString;
	private String refererPage;	
	private String userAgent;
	private LocalDateTime loggedTime;
	@Type(type = "yes_no")
	private boolean uniqueVisit;
	private String latitude;
	private String longitude;
}
