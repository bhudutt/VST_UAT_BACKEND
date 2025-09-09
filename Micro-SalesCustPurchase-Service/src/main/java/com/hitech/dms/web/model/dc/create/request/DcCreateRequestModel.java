/**
 * 
 */
package com.hitech.dms.web.model.dc.create.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class DcCreateRequestModel {
	private BigInteger dealerId;
	@JsonProperty(value = "branchId", required = true)
	private BigInteger branchId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "dcTypeId", required = true)
	private Integer dcTypeId;
	//@JsonDeserialize(using = DateHandler.class)
	private Date dcDate;
	private BigInteger machineAllotmentId;
	private BigInteger enquiryId;
	
//	private BigInteger customerId;
	
	private BigInteger insuranceMasterId;
	@JsonDeserialize(using = DateHandler.class)
	private Date policyStartDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date policyEndDate;
	private String dcRemarks;
	private String vehicleRegistrationNumber;
	
	private CustDtlCreateRequestModel customerDetail;
	private List<AllotMachDtlForDCCreateRequestModel> dcDtlList;
	private List<CheckListForDCCreateRequestModel> dcCheckList;
	private List<ItemListForDCCreateRequestModel> dcItemList;
}
