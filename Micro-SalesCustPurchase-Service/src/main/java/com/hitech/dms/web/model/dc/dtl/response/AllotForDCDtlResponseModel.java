/**
 * 
 */
package com.hitech.dms.web.model.dc.dtl.response;

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
public class AllotForDCDtlResponseModel {
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private String allotDate;
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String enquiryType;
	private String enquiryDate;
	private CustDtlResponseModel customerDetail;
	private List<AllotMachDtlForDCResponseModel> dcDtlList;
	private List<CheckListForDCResponseModel> dcCheckList;
}
