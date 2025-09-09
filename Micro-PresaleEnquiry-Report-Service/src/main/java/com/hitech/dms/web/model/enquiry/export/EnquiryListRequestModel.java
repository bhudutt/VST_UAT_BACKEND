package com.hitech.dms.web.model.enquiry.export;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

@Data
public class EnquiryListRequestModel {

	private BigInteger dealerId;
	private BigInteger branchId;
	private String enquiryTypeId;
	private Integer enquiryStage;
	private String enquiryStatusId;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date fromDate;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date toDate;
	String enquiryNumber;
	private BigInteger modelId;
	private BigInteger salesManId;
	private Integer enquirySourceId;
	private String prospectType;
	private Integer profitCenterId;
	private BigInteger stateId;
	private String clusterId;
	private String territoryManagerId;
	private BigInteger orgHierarchyId;
	private Integer page;
	private Integer Size;
	private Integer zone;
	private Integer territory;
	private BigInteger hoLevelId;
	private String fromDate1;
	private String toDate1;
	
}
