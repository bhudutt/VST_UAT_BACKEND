package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DcSearchBean {
	
	private BigInteger id;
	
	private String action;
	
	private String cancelAction;
	
	private String deliveryChallanNumber;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dcDate;
	
	private String branchName;
	
	private BigInteger branchId;
	
	private String productCategory;
	
	private String dcStatus;
	
	private String partyTypeName;
	
	private String partyCode;
	
	private String address;
	
	private String tehsil;
	
	private String postOffice;
	
	private String city;
	
	private String pinCode;
	
	private String state;
	

}
