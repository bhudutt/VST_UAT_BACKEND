/**
 * 
 */
package com.hitech.dms.web.model.inv.inv.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.inv.customer.response.CustDtlForInvResponseModel;
import com.hitech.dms.web.model.inv.dc.list.response.DcListForInvResponseModel;

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
public class InvViewResponseModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dealerCode;
	private String dealerName;
	private String branchCode;
	private String branchName;
	private Integer pcId;
	private String pcDesc;

	private String action;
	private BigInteger salesInvoiceHdrId;
	private String invoiceNumber;
	private Integer invoiceTypeId;
	private String invoiceType;
	private String invoiceStatus;
	private String invoiceDate;
	private BigInteger customerId;
	private BigInteger toDealerId;
	private String toDealerCode;
	private String toDealerName;	
	private BigInteger toPoHdrId;
	private String poNumber;
	private BigDecimal totalDicountAmnt;
	private BigDecimal insuranceCharges;
	private BigDecimal rtoCharges;
	private BigDecimal hsrpCharges;
	private BigDecimal otherCharges;
	private BigDecimal totalBasicAmnt;
	private BigDecimal totalGstAmnt;
	private BigDecimal totalInvoiceAmnt;
	private BigInteger financerId;
	private String financerName;
	private BigInteger insuranceMasterId;
	private String policyStartDate;
	private String policyExpiryDate;
	private String remarks;

	private BigInteger invCancelRequestId;
	private Integer invCancelReasonId;
	private String invCancelReason;
	private String invCancelDate;
	private String invCancelRemark;
	private String invCancelType;
	
	private String partyCode;
	private String partyName;
	private String partyLocation;
	private String partyAddress1;
	private String partyAddress2;
	private String partyAddress3;
	private String mobileNumber;
	private String emailId;
	private String gstIN;
	private String panNo;
	private BigInteger pinId;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;

	private CustDtlForInvResponseModel customerDetail;
	private List<DcListForInvResponseModel> dcList;
	private List<InvMachDtlForInvResponseModel> machineList;
	private List<InvItemDtlForInvResponseModel> itemList;
	private List<InvCancelAppViewResponseModel> cancelApprovalList;
}
