package com.hitech.dms.web.entity.plantMaster;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "SA_MST_PO_PLANT")
@Data
public class PoPlantMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name="po_plant_id")
	private BigInteger poPlantId;

	@Column(name="Plant_code")
	private String plantCode;

	@Column(name="Plant_name")
	private String plantName;
	
	@Column(name="Location")
	private String location;
	
	@Column(name="isActive")
	private Character isActive;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="CreatedDate")
	private Date createdDate;

	@Column(name="ModifiedBy")
	private String modifiedBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
}
