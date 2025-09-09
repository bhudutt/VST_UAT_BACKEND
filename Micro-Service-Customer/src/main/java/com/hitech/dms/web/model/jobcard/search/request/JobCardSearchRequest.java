/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardSearchRequest {
	private BigInteger branchId;
	private String jobCardNumber;
	private String chassisNumber;
	private String engineNumber;
	private String vinNumber;
	private String customerName;
	private String mobileNo;
	private BigInteger serviceTypeId;
	private String repairTypeId;
	private String jobCardCategoryId;
	private String invoiceNumber;
	private String serviceBookingNumber;
	private String quotationNumber;
	private String jobCardStatus;
	private String fromDate;
	private String toDate;
	private Integer page;
	private Integer size;
	private Integer pcId;
	private Integer hoId;
	private Integer zoneId;
	private Integer stateId;
	private Integer territoryId;
	
	

}
