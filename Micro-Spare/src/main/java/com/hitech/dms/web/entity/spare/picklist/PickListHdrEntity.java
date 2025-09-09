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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingDtlEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_PICKLIST_HDR")
public class PickListHdrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Pick_List_Hdr_Id")
	private BigInteger pickListHdrId;
	
	@Column(name="Pick_List_Number")
	private String pickListNumber;
	
	@Column(name="Pick_List_Status")
	private String pickListStatus;

	@Column(name="Branch_Id")
	private BigInteger branchId;

	@Column(name="Counter_Sale_Id")
	private BigInteger counterSaleId;
	
	@Column(name="Po_Hdr_Id")
	private BigInteger poHdrId;

	@Column(name="Co_Hdr_Id")
	private BigInteger coHdrId;
	
	@Column(name="Pin_id")
	private BigInteger pinId;
	
	@Column(name="Ref_Doc")
	private BigInteger refDoc;

	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;	
	
	}