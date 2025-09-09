package com.hitech.dms.web.model.aop.template.submit.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class AopTargetUpdateRequestModel {
	
	private BigInteger aopHrdId;
	private String remark;
	private List<AopTargetUpdateDetail> details;
	
	
	
	

}
