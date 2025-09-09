package com.hitech.dms.web.entity.partrequisition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
@Table(name = "PA_WK_REQ")
@Data
public class PartRequisitionEntity implements Serializable {
	
	
	private static final long serialVersionUID = 1867514944801926156L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Requisition_Id")
	private BigInteger requisitionId;
	@Column(name = "RequisitionNumber")
	private String requisitionNo;
	@Column(name = "Requisition_Date")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date requisitionDate =new Date();
	@Column(name="Branch_Id")
	private Integer branch;
	@Column(name="Ro_Id")
	private BigInteger roId;
	@Column(name="RequisitionStatus")
	private String requisitionStatus;
	@Column(name="CancelDate")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date cancelDate;
	@Column(name = "Remarks")
	private String remarks;
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate =new Date();
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "ModifiedDate")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date modfiedDate= new Date();
	@Column(name="View_Doc_No")
	private String viewDocNo;
	@Column(name="Version_No")
	private Integer versionNo;
	@Column(name="RequisitionType")
	private Integer requisitionType;
	@Column(name="Vin_Id")
	private BigInteger vinId;
	@Column(name="Customer_Id")
	private Integer customerId;
	@Column(name="RequestedBy")
	private Integer requestedBy;

}
