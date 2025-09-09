/**
 * 
 */
package com.hitech.dms.web.model.dc.view.response;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcViewResponseModel {
	private BigInteger dcId;
	private String dcNumber;
	private String dcDate;
	private Date dcDate1;
	private String dcStatus;
	private Integer dcTypeId;
	private String dcType;
	
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dealerCode;
	private String dealerName;
	private String branchCode;
	private String branchName;
	private Integer pcId;
	private String pcDesc;
	
	private String action;
	
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private String allotDate;
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String enquiryType;
	private String enquiryDate;
	private String gatePassNumber;
	private String gatePassDate;
	
	private String dcCancelDate;
	private Integer dcCancelReasonId;
	private String dcCancelReason;
	private String dcCancelRemark;
	private String dcCancellationType;
	
	private BigInteger dcCancelRequestId;
	
	private BigInteger insuranceMasterId;
	private String policyStartDate;
	private String policyEndDate;
	private String dcRemarks;
	private String vehicleRegistrationNumber;
	private String partyCode;
	private String partyName;
	private String partyLocation;
	private String partyAddress1;
	private String partyAddress2;
	private String partyAddress3;
	private String mobileNumber;
	private String emailId;
	private String displayValue;
	private BigInteger pinId;
	private String pincode;
	private String village;
	private String tehsil;
	private String district;
	private String state;
	private String country;

	private DcCustViewResponseModel customerDetail;
	private List<DcMachDtlViewResponseModel> dcDtlList;
	private List<DcCheckListViewResponseModel> dcCheckList;
	private List<DcItemDtlViewResponseModel> dcItemList;
	private List<DcCancellationApprovalResponseModel> cancellationApprovalList;
}
