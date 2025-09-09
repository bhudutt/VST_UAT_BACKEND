/**
 * 
 */
package com.hitech.dms.web.model.enquiry.exchange.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ExchangeVehicleUpdateRequestModel {
	private BigInteger oldVehId;
	private BigInteger enquiryId;
	private BigInteger branchId;
	private String buyerContactNumber;
	private String buyerName;
	private String saleRemarks;
	private BigDecimal sellingPrice;
	private Date saleDate;
}
