package com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SearchBinToBinTransferResponseDto {
	
	private BigInteger id;
	
	private String issueNo;
	
	private String issueDate;
	
	private String receiptNo;
	
	private String remarks;
	
	private String transferDoneBy;
	
//	private String partNo;
	
//	private String partDesc;
//	
//	private String fromStore;
//	
//	private String fromBinLocation;
//	
//	private BigDecimal fromBinStock;
//	
//	private String toStore;
//	
//	private String toBinLocation;
//	
//	private BigDecimal toBinStock;
//	
//	private BigDecimal transferQty;

	@JsonIgnore
    private Long totalCount;

//    private String dealerCode;
//
//    private String dealerName;
//
//    private String dealerBranch;
//    
//    private String dealerLocation;

}
