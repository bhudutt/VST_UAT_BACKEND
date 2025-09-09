package com.hitech.dms.web.model.delayreason.create.request;

import java.util.Date;

import lombok.Data;
@Data
public class DelayReasonRequestName {

	private Integer delayReasonId;
	
	private String delayReasonCode;

	private String delayReasonDescription;

	private boolean activeStatus;

	private Date createdDate;

	private String createdBy;

	private Date modifiedDate;

	private String modifiedBy;
	
}
