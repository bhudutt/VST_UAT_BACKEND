package com.hitech.dms.web.entity.pcr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "PA_WK_REQ_DTL")
@Data
public class PartRequisitionDtlEntity implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Requisition_Dtl_Id")
	private BigInteger requisitionDtlId;
	@Column(name="Requisition_Id")
	private BigInteger requisitionId;
	@Column(name="Sr_No")
	private String srNo;
	@Column(name="PartBranch_Id")
	private Integer partBranchId;
	@Column(name = "RequestedQty")
	private Double requisitionQty;
	@Column(name = "IssuedQty")
	private Double issuedQty;
	@Column(name = "ReceivedQty")
	private String receivedQty;
	@Column(name = "MRP")
	private BigDecimal mrp;
	@Column(name="IsCancelled")
	private Boolean isCancelled;
	@Column(name="Remarks")
	private String remarks;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name="CancelDate")
	private Date cancelDate;
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "ModifiedDate")
	private Date modfiedDate= new Date();
	@Column(name="IsTag")
	private Boolean isTag;
	@Column(name="Version_No")
	private Integer versionNo;
	
	@Column(name = "Failure_Type_Id")
	private BigInteger FailureTypeId;
	
//	@ManyToOne
//    @JsonBackReference
//    @JoinColumn(name = "PartBranch_Id", referencedColumnName = "branch_id")
//    private ServiceWarrantyPcr serviceWarrantyPcr;

}
