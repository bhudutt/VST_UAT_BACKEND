package com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class BinToBinTransferDetailDto {
	
	private BigInteger issueId;
	
	private String partNo;
	
	private String partDesc;
	
	private String fromStore;
	
	private String fromBinLocation;
	
	private BigDecimal fromBinStock;
	
	private String toStore;
	
	private String toBinLocation;
	
	private BigDecimal toBinStock;
	
	private BigDecimal transferQty;

}
