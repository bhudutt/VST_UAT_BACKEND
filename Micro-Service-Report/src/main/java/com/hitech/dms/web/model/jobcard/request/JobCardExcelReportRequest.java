/**
 * 
 */
package com.hitech.dms.web.model.jobcard.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardExcelReportRequest {
	private BigInteger branchId;
	private String userCode;
	private String roNumber;
	private String chasisNumber;
	private String engineNumber;
	private String vinNo;
	private String customerName;
	private String mobileNo;
	private BigInteger serviceTypeId;
	private String repairTypeId;
	private String jobCardCategoryId;
	private String invoiceNo;
	private String serviceBookingNumber;
	private String quotationNumber;
	private String fromDate;
	private String toDate;
	private int page;
	private int size;
	private int pcId;
	private int hoId;
	private int zoneId;
	private int stateId;
	private int territoryId;
}
