package com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

@Data
public class SearchBinToBinTransferRequestDto {
	
	private String issueNo;
	
	private String receiptNo;
	
	private BigInteger transferDoneBy;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer page;
	
	private Integer size;

}
