/**
 * 
 */
package com.hitech.dms.web.model.quotation.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoSearchResponse {
	private BigInteger quotationId;
	private String quotationNumber;
	private String quotationDate;
	private String customerName;
	private String customerMobileNumber;
	private String customerCode;
	private String address1;
	private String address2;
	private String address3;
	private String district;
	private String tehsil;
	private String village;
	private String pinCode;
	private String state;
	private String country;
	private BigDecimal totalBasicValue;
	private BigDecimal totalDiscount;
	private BigDecimal totalTaxableValue;
	private BigDecimal totalGstAmount;
	private BigDecimal totalCharges;
	private BigDecimal totalAmount;
}
