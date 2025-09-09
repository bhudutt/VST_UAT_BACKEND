package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeliveryChallanPartDetailRequest {
	

	private BigInteger customerId;
	
    private BigInteger dChallanDtlId;
	
	private BigInteger partBranchId;
	
	private String stockBinId;
	
	private BigDecimal mrp;
	
	private BigInteger issuedQty;
	
	private BigDecimal value;
	
	private BigInteger returnedQty;
	
	private BigInteger billedQty;
	
    private BigInteger partId; 
     
    private BigInteger balanceQty; 
     
    private BigInteger dcQty; 
    
    private String fromStore;
    
    private String binList;
	
	private Integer versionNo;
		
	private Date createdDate;
	
	private BigInteger createdBy;
	
	private Date modifiedDate;
	
	private BigInteger modifiedBy;
	
	
    private BigInteger stockStoreId;
	
	private BigInteger branchId;
	
	private BigDecimal basicUnitPrice;
	
	private BigInteger branchStoreId;
	
	private BigInteger storeId;
	
	private BigInteger issueId;
	
	private String pickListNumber;
	
	private BigInteger pickListDtlId;
	
	private Integer totalSumOfIssue;
	
	@JsonProperty(value="binPayload")
	private Map<String, BinDetailRequest> binDetailList = new HashMap<>();
}
