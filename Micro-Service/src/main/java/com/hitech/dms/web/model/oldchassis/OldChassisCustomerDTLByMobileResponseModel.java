package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author Sunil.Singh
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OldChassisCustomerDTLByMobileResponseModel {
	private BigInteger customerId;
	private String customerCode;
	private String displayValue;
	private String prospectType;
	private String mobileNo;
	private String errorFlag;
	private String errorMsg;
	
}
