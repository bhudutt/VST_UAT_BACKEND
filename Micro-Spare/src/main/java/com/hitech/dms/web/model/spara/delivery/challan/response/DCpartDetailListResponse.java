package com.hitech.dms.web.model.spara.delivery.challan.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class DCpartDetailListResponse {
	
	
	private BigInteger dChallanDtlId;
	
    private Integer	part_id;
    
    private BigDecimal basicUnitPrice;
    
	private BigDecimal dealerMRP;
	
	private String hsnCode;
	
	private String partNumber;
	
    private Integer currentStock;
    
	private String partDesc;
	
	private String paSubCategoryDesc;
	
	private BigDecimal issuedQty;
	
	private String customerOrderNumber;
	
	private Date customerOrderDate;
	
	private Integer coQty;
	
	private BigDecimal sgstAmt;
	
	private BigDecimal	igstAmt;
	
	private BigDecimal	cgstAmt;
	
	private BigDecimal	sgstPer;
	
	private BigDecimal	igstPer;
	
	private BigDecimal	cgstPer;
	
	private BigDecimal	netAmount;
	
	private String fromStore;
	
	private String binList;
	
	private Integer partBranchId;
	
	private Integer customerDtlId;
	
	private BigInteger stockBinid;
	
	private Integer storeId;
	
	private Integer stockStoreId;
	
	private Integer branchId;
	
	private BigInteger binId;
	
	private Integer balanceQty;
	
	private String picklistnumber;
	
	private Integer pickListDtlId;
	
}
