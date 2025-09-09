/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class LabourChargeRequest {
	    private String deleteFlag;
	    private int billableTypeId;
	    private int labourCodeId;
	    private BigDecimal hour;
	    private BigDecimal rate;
	    private BigDecimal amount;
	    private int insurancePartyId;
	    private int bayTypeId;
	    private int mechanicId;
	    private Date startDate;
	    private Date endDate;
	    private String startTime;
	    private String endTime;
	    
		private BigDecimal basePrice;
		private BigDecimal igst;
		private BigDecimal sgst;
		private BigDecimal cgst;
		private BigDecimal igstAmt;
		private BigDecimal cgstAmt;
		private BigDecimal sgstAmt;
		private BigDecimal totalGst;
		private BigDecimal totalAmt;
		
		private Integer oem;
		private Integer customer;
		private Integer dealer;
		private Integer insurance;
		
	    
	    
}
