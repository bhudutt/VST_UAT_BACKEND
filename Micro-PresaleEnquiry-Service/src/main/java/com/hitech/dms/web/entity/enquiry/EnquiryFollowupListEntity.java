package com.hitech.dms.web.entity.enquiry;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

public class EnquiryFollowupListEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Followup_Id")
	private Integer followupId;
	@Column(name="Followup_ActionName")
	private String followupActionName;
	@Column(name="Followup_Status")
	private String followupStatus;
	@Column(name = "Createddate", updatable = false)
	@JsonDeserialize(using = DateHandler.class)
	private Date createdDate =new Date();
	@Column(name="Createdby")
	private Integer createdby;
	

}
