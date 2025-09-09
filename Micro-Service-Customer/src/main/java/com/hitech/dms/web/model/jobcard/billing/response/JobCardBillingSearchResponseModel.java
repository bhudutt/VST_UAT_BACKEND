package com.hitech.dms.web.model.jobcard.billing.response;
import java.math.BigInteger;

import lombok.Data;

@Data
public class JobCardBillingSearchResponseModel {

	private String JobCardNo;
	private BigInteger roId;
	private String openDate;
	private String vehSrNo;
	private String JobCardStatus;
	private String closeDate;
	private String saleDate;
}
