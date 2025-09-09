/**
 * 
 */
package com.hitech.dms.web.model.pr.view.response;

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
public class PurchaseReturnViewResponseModel {
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private String purchaseReturnStatus;
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private String purchaseReturnDate;
	private BigInteger grnId;
	private String grnNumber;
	private String grnDate;
	private Integer grnTypeId;
	private String grnType;
	private String grnStatus;
	private BigInteger invoiceId;
	private String invoiceNumber;
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
	private List<PurchaseReturnViewAppResponseModel> salesMachinePurchaseReturnAppList;
	private List<PurchaseReturnMachDtlViewResponseModel> salesMachineGrnDtlList;
	private List<PurchaseReturnItemDtlViewResponseModel> salesMachineGrnImplDtlList;
}
