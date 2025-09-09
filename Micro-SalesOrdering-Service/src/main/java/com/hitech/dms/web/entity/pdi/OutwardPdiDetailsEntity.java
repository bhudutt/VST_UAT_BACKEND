package com.hitech.dms.web.entity.pdi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_PDI_OUTWARD_DTL")
public class OutwardPdiDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "OutwardPdiId")
	private Integer pdiEntity;
	
	@Column(name = "DealerId")
	private Integer dealerId;

	@Column(name = "BranchId")
	private Integer branchId;
	
	@Column(name = "Remarks")
	private String remarks;
	
	@Column(name = "OkFlag")
	private Boolean okFlag = false;

	@Column(name = "CreatedBy", updatable = false)
	private Long createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate = new Date();

	private Long modifiedBy;

	private Date modifiedDate;

	@Column(name = "Attributes")
	private String attributes;

	@Column(name = "CheckPointId")
	private String checkPoint;
}
