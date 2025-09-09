package com.hitech.dms.web.model.serviceclaim;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class ServiceCLaimApprovalRequestDto {
	
	private BigInteger serviceClaimId;
	private String serviceClaimNo;
	private String approvalStatus;
	private String remarks;
	private String rejectReason;
	private List<InstaillationStatusBean> instaillationStatus;
	private List<PDIStatusBean> pdiStatus;
	private List<JobCardStatusBean> jobCardStatus;
	

}
