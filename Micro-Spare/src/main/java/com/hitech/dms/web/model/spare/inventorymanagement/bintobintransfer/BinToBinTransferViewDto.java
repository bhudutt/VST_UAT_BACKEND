package com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BinToBinTransferViewDto {
	
	private BigInteger id;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String receiptNo;
	
	private String remarks;
	
	private String transferDoneBy;
	
	private List<BinToBinTransferDetailDto> partDetails;

}
