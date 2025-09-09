/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */

@Entity
@Table(name = "SV_CUSTOMER_VOICE")
@Data
public class CustomberVoiceEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "RO_ID")
	private Integer roId;
	@Column(name = "CUSTOMER_CONCERN")
	private String customerConcern;
	@Column(name = "Activity_To_Done")
	private String activityToBeDone;
	@Column(name = "createdate")
	private Date createdDate;
	@Column(name = "creatredby")
	private String createdBy;
	//private String deleteFlag;
}
