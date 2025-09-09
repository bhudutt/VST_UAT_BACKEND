/**
 * 
 */
package com.hitech.dms.web.model.customer.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class CustomerDTLByMobileResponseModel {
	private BigInteger customerId;
	private String customerCode;
	private String displayValue;
	private String prospectType;
	private String mobileNo;
	private String underDealerTerritory;
	private String errorFlag;
	private String errorMsg;
}
