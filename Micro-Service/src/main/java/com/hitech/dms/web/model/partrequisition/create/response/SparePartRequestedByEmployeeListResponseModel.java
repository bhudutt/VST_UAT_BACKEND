package com.hitech.dms.web.model.partrequisition.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SparePartRequestedByEmployeeListResponseModel {

	private BigInteger empId;
	private BigInteger  dealerId;
	private String customerName;
	private String EmpCode;
	private BigInteger branchId;
	
}
