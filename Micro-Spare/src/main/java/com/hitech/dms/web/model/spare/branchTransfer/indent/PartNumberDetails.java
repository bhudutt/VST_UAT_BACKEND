package com.hitech.dms.web.model.spare.branchTransfer.indent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class PartNumberDetails {

	private Integer partId;
	private Integer partBranchId;
	private String partNumber;
	private String partDesc;
	private String pickListNumber;
	private String partSubCategory;
	private Integer totalStock;
	private BigDecimal totalValue;
	private Integer indentQty;
//	private BigDecimal mrp;
	private List<BigDecimal> mrp;
	private BigDecimal basicUnitPrice;
	private String fromStore;
	private BigInteger stockBinId;
	private String storeBinLocation;
	private String binStock;
	private BigDecimal orderQty;
	private BigDecimal balanceQty;
	private BigDecimal sgst;
	private BigDecimal cgst;
	private BigDecimal igst;
	private BigDecimal discount;
	private String hsnCode;
	private String serialNo;
	private BigInteger poDtlId;
	private Integer customerOrderOrDCId;
	private Integer customerDtlId;
	private BigInteger dcId;
	private BigInteger dcDtlId;
	private BigInteger pickListDtlId;
	private String lockForTranscation;
	private String errorMsg;
	private Integer lockPartCount;
	private Integer status;
	private String message;
	
}
