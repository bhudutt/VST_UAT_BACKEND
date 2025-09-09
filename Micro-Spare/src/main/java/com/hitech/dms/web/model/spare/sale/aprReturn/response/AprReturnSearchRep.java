package com.hitech.dms.web.model.spare.sale.aprReturn.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AprReturnSearchRep {
	
	//private Integer branchId;
	
	private Integer id;
	
	private String action;
	
	private String branchName;
	
	private String aprReturnDocNo;
	
	private String aprReturnDate;
	
	private String aprReturnStatus;
	
	//private Integer partyId;
	
	private String partyName;
	
	private String partCode;
	
	private String partyType;
	
	//private Integer invoiceId;
	
	private String invoiceNo;
	

	
//	private Integer aprReturnQty;
//	
//	private Integer aprReturnedQty;

}
