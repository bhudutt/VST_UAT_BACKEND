package com.hitech.dms.web.model.targetSetting.request;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

@Data
public class TargetSettingRequestModel {

	private BigInteger targetHdrId;

	private String targetNumber;
	
	private Integer branchId;

	private Date targetDate;

	private String targetFor;

	private String productCategory;

	private String outletCategory;
}
