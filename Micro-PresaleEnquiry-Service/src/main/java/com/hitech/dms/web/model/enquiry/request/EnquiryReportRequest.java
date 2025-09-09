/**
 * 
 */
package com.hitech.dms.web.model.enquiry.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class EnquiryReportRequest {
	private String fromDate;
	private String toDate;
	private Integer profitCenterId;
	private Integer stateId;
	private Integer clusterId;
	private Integer territoryManagerId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String modelIds;
	private Integer enquiryTypeId;
	private Integer enquiryStatusId;
	private Integer enquirySourceId;
}
