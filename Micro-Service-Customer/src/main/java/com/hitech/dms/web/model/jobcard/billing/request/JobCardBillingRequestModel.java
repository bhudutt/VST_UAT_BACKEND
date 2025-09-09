package com.hitech.dms.web.model.jobcard.billing.request;


import java.util.List;

import com.hitech.dms.web.entity.jobcard.billing.JobCardBillingHDREntity;

import com.hitech.dms.web.entity.jobcard.billing.JobCardRoBillLabourDTLEntity;
import com.hitech.dms.web.entity.jobcard.billing.JobCardRoBillOutSideLabourDTLEntity;
import com.hitech.dms.web.entity.jobcard.billing.JobCardRoBillPartDetailEntity;

import lombok.Data;

@Data
public class JobCardBillingRequestModel {

	private JobCardBillingHDREntity jobCardBillingHDREntity;
	private List<JobCardRoBillPartDetailEntity> jobCardRoBillPartDetailEntity;
	private List<JobCardRoBillLabourDTLEntity> jobCardRoBillLabourDTLEntity;
	private List<JobCardRoBillOutSideLabourDTLEntity> jobCardRoBillOutSideLabourDTLEntity;

}
