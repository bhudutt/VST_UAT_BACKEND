/**
 * 
 */
package com.hitech.dms.web.model.pr.search.response;

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
public class PurchaseReturnSearchResponseModel {
	private BigInteger id; // purchaseReturnId
	private BigInteger id1; // dealerId
	private Integer id2; // pcId
	private String action;
	private String purchaseReturnNumber;
	private String purchaseReturnDate;
	private String grnNumber;
	private String grnType;
	private String grnDate;
	private String dealerCode;
	private String dealerName;
	private String pcDesc;
	private String invoiceNumber;
	private String invoiceDate;
	private String partyCode;
	private String partyName;
	private BigDecimal grossTotalValue;
}
