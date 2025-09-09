package com.hitech.dms.web.entity.spare.picklist;

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
@Entity
@Table(name = "PA_PICKLIST_DTL")
public class PickListDtlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Pick_List_Dtl_Id")
	private BigInteger pickListDtlId;
	
	@Column(name="Pick_List_Hdr_Id")
	private BigInteger pickListHdrId;
	
	@Column(name="Po_Dtl_Id")
	private BigInteger poDtlId;

	@Column(name="Co_Dtl_Id")
	private BigInteger coDtlId;
	
	@Column(name="Part_Branch_Id")
	private BigInteger partBranchId;

	@Column(name="Part_Id")
	private BigInteger partId;

	@Column(name="Stock_Bin_Id")
	private BigInteger StockBinId;

	@Column(name="MRP")
	private BigDecimal MRP;

	@Column(name="Order_Qty")
	private BigDecimal OrderQty;

	@Column(name="Balance_Qty")
	private BigDecimal BalanceQty;

	@Column(name="Issue_Qty")
	private BigDecimal IssueQty;
	
	@Column(name="Is_Individual_Bin")
	private Character IsIndividualBin;

	@Column(name="CreatedDate")
	private Date CreatedDate;

	@Column(name="CreatedBy")
	private String CreatedBy;
	
	@Column(name="ModifiedDate")
	private Date ModifiedDate;
	
	@Column(name="ModifiedBy")
	private String ModifiedBy;
	
	@Column(name="Dc_Issue_Qty")
	private Integer dcIssueQty;
	
	
	}