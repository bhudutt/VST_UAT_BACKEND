/**
 * 
 */
package com.hitech.dms.web.model.pr.grn.dtl.response;

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
public class GrnDtlForPurchaseReturnResponseModel {
	private BigInteger dealerId;
	private String dealerName;
	private String dealerCode;
	private BigInteger grnId;
	private String grnNumber;
	private String grnDate;
	private Integer grnTypeId;
	private String grnType;
	private String grnStatus;
	private BigInteger invoiceId;
	private String invoiceNumber;
	private String invoiceStatus;
	private String invoiceType;
	private String invoiceDate;
	private String displayValue;
	private String partyCode;
	private String partyName;
	private String transporterName;
	private String driverName;
	private String driverMobileNo;
	private String transporterVehicleNo;
	private BigDecimal grossTotalValue;
	private Integer pcId;
	private String pcDesc;
	private List<GrnMachDtlForPurchaseReturnResponseModel> salesMachineGrnDtlList;
	private List<GrnItemDtlForPurchaseReturnResponseModel> salesMachineGrnImplDtlList;
}
