/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobLabourMasterResponse {
	private BigInteger labourId;
	private String labourCode;
	private String labourDesc;
	private BigDecimal labourCharge;
}
