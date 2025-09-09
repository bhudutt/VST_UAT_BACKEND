package com.hitech.dms.web.model.gstinvoice.list.request;



import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GstInvoiceFilterRequest {

	private String fromDate;
	private String toDate;
	private String zone;
	private String area;
	private String includeInActive;
	private int page;
	private int size;
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigInteger stateId;
	private String invoiceNumber;
	private String employeeCode;
	private String employeeName;
	private BigInteger deptId;
	private String chassisNo;
}
