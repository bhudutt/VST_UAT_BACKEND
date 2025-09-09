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

@Data
@Table(name = "PA_DCHALLAN_HDR")
@Entity
public class DeliveryChallanHdrEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DChallan_Id")
	private BigInteger dchallanId;
	
	@Column(name = "Branch_Id")
	private BigInteger branchId;
	
	@Column(name = "ChallanType")
	private Integer challanType;
	
	@Column(name = "DCNumber")
	private String dcNumber;
	
	@Column(name = "DC_Date")
	private Date dcDate;
	
	@Column(name = "DCStatus")
	private String dcStatus;
	
//	@Column(name = "Emp_Id")
//	private BigInteger empId;
	
	@Column(name = "CategoryCode")
	private String categoryCode;
	
	@Column(name = "PartyCode")
	private String partyCode;
	
	@Column(name = "CustomerName")
	private String customerName;
	
	@Column(name = "PartyType")
	private String partyType;
//	
//	@Column(name = "PartyAddLine2")
//	private String partyAddline2;
//	
//	@Column(name = "PartyAddLine3")
//	private String partyAddline3;
//	
//	@Column(name = "City_Id")
//	private Integer cityId;
//	
//	@Column(name = "Pin_Id")
//	private Integer pinId;
	
	@Column(name = "Remarks")
	private String remarks;
	
	@Column(name = "TotalQty")
	private BigInteger totalIssuedQty;
	
//	@Column(name = "TotalReturnedValue")
//	private BigDecimal totalReturnedValue;
//	
	@Column(name = "TotalPart")
	private BigDecimal totalValue;
	
	@Column(name = "Base_Amount")
	private BigDecimal baseAmount;
	
	@Column(name = "Total_Gst_Amount")
	private BigDecimal totalGstAmount;
	
	@Column(name = "Total_Order_Amount")
	private BigDecimal totalOrderAmount;
	
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	


}
