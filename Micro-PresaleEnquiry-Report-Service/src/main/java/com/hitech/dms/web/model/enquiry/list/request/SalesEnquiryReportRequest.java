/**
 * 
 */
package com.hitech.dms.web.model.enquiry.list.request;

import java.math.BigInteger;
import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SalesEnquiryReportRequest {
	private String fromDate;
	private String toDate;
	private Integer profitCenterId;
	private Integer stateId;
	private Integer clusterId;
	private Integer territoryManagerId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String modelIds;
	private Integer levelId;
}
