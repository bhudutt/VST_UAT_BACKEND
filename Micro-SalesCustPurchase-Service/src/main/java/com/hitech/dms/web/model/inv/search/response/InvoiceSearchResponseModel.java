/**
 * 
 */
package com.hitech.dms.web.model.inv.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class InvoiceSearchResponseModel {
	private BigInteger id; // salesInvoiceHdrId
	private BigInteger id1; // branchId
	private Integer id2; // pcId
	private String action;
	private String invoiceNumber;
	private String invoiceType;
	private String invoiceDate;
	private String invoiceStatus;
	private String dealerShip;
	private String branchCode;
	private String branchName;
	private String pcDesc;
	private String dcNumber;
	private String poNumber;
	private String customerName;
	private BigDecimal totalBasicAmnt;
	private BigDecimal totalGstAmnt;
	private BigDecimal totalInvoiceAmnt;
	private String invoiceCancelDate;
	private String invoiceCancelRemark;
}
