package com.hitech.dms.web.entity.partrequisition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

@Entity
@Table(name = "PA_WK_REQ_DTL")
@Data
public class PartDetailsEntity implements Serializable{
	
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
	private Date createdDate = new Date();
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "ModifiedDate")
	private Date modfiedDate= new Date();
	@Column(name="IsTag")
	private Boolean isTag;
	@Column(name="Version_No")
	private Integer versionNo;
	@Column(name="partId")
	private Integer partId;
	
	
}
