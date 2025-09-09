/**
 * 
 */
package com.hitech.dms.web.model.grn.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class GrnSearchResponseModel {
	private BigInteger id; // grnId
	private String grnNumber;
	private String grnType;
	private String grnStatus;
	private String grnDate;
	private BigInteger id1; // dealerId 
	private String dealerCode;
	private String dealerName;
	private String pcDesc;
	private String invoiceNumber;
	private String invoiceDate;
	private String partyCode;
	private String partyName;
	private String transporterName;
	private String driverName;
	private String driverMobileNo;
	private String transporterVehicleNo;
	private BigDecimal grossTotalValue;
}
