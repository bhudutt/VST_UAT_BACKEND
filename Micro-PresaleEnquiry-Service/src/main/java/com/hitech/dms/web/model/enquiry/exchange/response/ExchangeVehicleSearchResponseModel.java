/**
 * 
 */
package com.hitech.dms.web.model.enquiry.exchange.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ExchangeVehicleSearchResponseModel {
	private BigInteger srNo;
	private BigInteger oldVehId;
	private BigInteger branchId;
	private BigInteger enquiryId;
	private String action;
	private String enqNumber;
	private String vehStatus;
	private String brandName;
	private String modelName;
	private Integer modelYear;
	private String invInDate;
	private BigDecimal estimatedExchangePrice;
	private String buyerName;
	private String buyerContactNumber;
	private String saleDate;
	private BigDecimal sellingPrice;
	String saleRemarks;
}
