package com.hitech.dms.web.model.targetSetting.response;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class TargetSettingDtlResponse {

//	private BigInteger targetDtlId;

//	private BigInteger targetHdrId;

	private BigInteger partyId;

	private String partyCode;
	
	private String parentDealerName;
	
	private String parentDealerLocation;
	
	private String dealerPincode;

	private Double apr;

	private Double may;

	private Double jun;

	private Double jul;

	private Double aug;

	private Double sep;

	private Double oct;

	private Double nov;

	private Double dec;

	private Double jan;

	private Double feb;

	private Double mar;
	
	private boolean isMonthDataExist;
	
}
