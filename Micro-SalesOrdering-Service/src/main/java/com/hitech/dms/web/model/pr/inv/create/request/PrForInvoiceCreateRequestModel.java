/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrForInvoiceCreateRequestModel {
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private String purchaseReturnStatus;
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private BigInteger toDealerId;
	@JsonDeserialize(using = DateHandler.class)
	private Date purchaseReturnInvDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date purchaseReturnDate;
	private BigInteger grnId;
	private String grnNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date grnDate;
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
	private BigDecimal grossTotalReturnValue;
	private Integer pcId;
	private String pcDesc;
	private List<PrMachDtlForInvoiceCreateRequestModel> salesMachineGrnDtlList;
	private List<PrItemDtlForInvoiceCreateRequestModel> salesMachineGrnImplDtlList;
}
