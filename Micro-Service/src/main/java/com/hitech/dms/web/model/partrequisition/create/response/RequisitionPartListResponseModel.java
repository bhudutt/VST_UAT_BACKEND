package com.hitech.dms.web.model.partrequisition.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class RequisitionPartListResponseModel {
    
	private Integer partBranchId;
	private Integer partId;
	private String partNo;
	private String partDesc;
	private BigInteger requisitionId;
}
