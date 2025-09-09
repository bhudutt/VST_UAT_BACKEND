/**
 * 
 */
package com.hitech.dms.web.model.grn.view.response;

import java.math.BigDecimal;
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
public class SalesGrnViewResponseModel {
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private Integer pcId;
	private String pcDesc;
	private Integer grnTypeId;
	private String grnNumber;
	private String grnType;
	private String grnDate;
	private BigInteger invoiceId;
	private BigInteger coDealerInvoiceId;
	private BigInteger erpInvoiceHdrId;
	private BigInteger purchaseReturnInvId;
	private String invoiceNumber;
	private String invoiceDate;
	private String partyCode;
	private String partyName;
	private String transporterName;
	private String driverName;
	private String driverMobileNo;
	private String transporterVehicleNo;
	private BigDecimal grossTotalValue;
	private String plantCode;
	private List<SalesGrnViewMachineDtlResponseModel> salesMachineGrnDtlList;
	private List<SalesGrnViewItemDtlResponseModel> salesMachineGrnImplDtlList;
}
