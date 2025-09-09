/**
 * 
 */
package com.hitech.dms.web.model.grn.invoice.dtl.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class InvoicesForGrnDtlResponseModel {
	private BigInteger invoiceId;
	private String invoiceNo;
	private String invoiceType;
	private String invoiceDate;
	private String displayValue;
	private String partyCode;
	private String partyName;
	private String plantCode;
	private List<InvoicesForGrnDtlForMachineResponseModel> salesMachineGrnDtlList;
	private List<InvoicesForGrnDtlForItemResponseModel> salesMachineGrnImplDtlList;
}
