/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.view.response;

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
public class PrForInvoiceViewResponseModel {
	private BigInteger purchaseReturnInvId;
	private String purchaseReturnInvNumber;
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private String purchaseReturnStatus;
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private BigInteger toDealerId;
	private String toDealerCode;
	private String toDealerName;
	private String purchaseReturnInvDate;
	private String purchaseReturnDate;
	private BigInteger grnId;
	private String grnNumber;
	private String grnDate;
	private String grnType;
	private String grnStatus;
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
	private BigDecimal grossTotalReturnValue;
	private Integer id5; // pcId
	private String pcDesc;
	private List<PrMachDtlForInvoiceViewResponseModel> salesMachineGrnDtlList;
	private List<PrItemDtlForInvoiceViewResponseModel> salesMachineGrnImplDtlList;
}
