package com.hitech.dms.web.entity.spare.delivery.challan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;



/**
 * 
 * @author Vivek.Gupta
 *
 */
@Entity
@Table(name = "PA_DCHALLAN_ISSUE_DTL")
@Data
public class DeliveryChallanIssueDetailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DChallan_Dtl_Id")
	private BigInteger dChallanDtlId;
	
	@Column(name = "DChallan_Id")
	private BigInteger dChallanId;
	
	@Column(name = "PartBranch_Id")
	private BigInteger partBranchId;
	
	@Column(name = "Stock_Bin_Id")
	private BigInteger stockBinId;
	
	@Column(name = "MRP")
	private BigDecimal mrp;
	
	@Column(name = "IssuedQty")
	private BigInteger issuedQty;
	
	@Column(name = "Value")
	private BigDecimal value;
	
	@Column(name = "ReturnedQty")
	private BigInteger returnedQty;
	
	@Column(name = "InvoiceQty")
	private BigInteger invoiceQty;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	
	@Column(name = "Part_Id")
	private BigInteger partId;
	
	@Column(name = "Version_No")
	private Integer versionNo;
	
	@Column(name = "From_Store")
	private String fromStore;
	
	@Column(name = "Bin_List")    
	private String binList;
	
	@Column(name = "Customer_dtl_Id")
	private BigInteger customerDtlId;
	
	@Column(name = "Pick_list_dtl")
	private String pickListNumber;
	
	@Column(name = "Pick_dtl_id")
	private Integer pickListDtlId;
	
}
