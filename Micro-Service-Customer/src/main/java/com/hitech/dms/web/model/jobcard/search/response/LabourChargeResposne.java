/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class LabourChargeResposne {
	private Integer labourId;
	private String labourCode;
	private String labourDesc;
	private String billableTypeCode;
	private BigDecimal labourCharge;
	private BigDecimal standardHrs;
	private BigDecimal rate;
	private BigDecimal totalAmt;
	private String insurancePartyName;
	private String bayType;
	private String mechanicName;
	private Date startDate;
	private Date endDate;
	private String startTime;
	private String endTime;
	private Integer oem;
	private Integer customer;
	private Integer dealer;
	private Integer insurance;
	
	
}
