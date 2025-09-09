/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrForInvoiceSearchResponseModel {
	private BigInteger id; // purchaseReturnInvId
	private String purchaseReturnInvNumber;
	private String purchaseReturnInvDate;
	private BigInteger id1; // purchaseReturnId
	private String purchaseReturnNumber;
	private String purchaseReturnStatus;
	private BigInteger id2; // dealerId
	private String dealerCode;
	private String dealerName;
	private BigInteger id3; // toDealerId
	private String toDealerCode;
	private String toDealerName;
	private String purchaseReturnDate;
	private BigInteger id4; // grnId
	private String grnNumber;
	private String grnDate;
//	private String grnType;
	private String grnStatus;
	private String invoiceNumber;
//	private String invoiceType;
	private Date invoiceDate;
//	private String displayValue;
	private String partyCode;
	private String partyName;
	private String transporterName;
	private String driverName;
	private String driverMobileNo;
	private String transporterVehicleNo;
	private BigDecimal grossTotalReturnValue;
	private Integer id5; // pcId
	private String pcDesc;
}
