package com.hitech.dms.web.entity.installation;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hitech.dms.web.entity.pdi.PdiDetailsEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SV_INSTALLATION_HDR")
public class InstallationEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "ChassisNumber")
    private String chassisNo;

	@Column(name = "EngineNumber")
    private String engineNo;
	
	@Column(name = "InstallationNumber")
    private String installationNo;
	
	@Column(name = "InstallationDate")
	private Date installationDate;
	
	@Column(name = "InstallationType")
	private Integer installationType;
	
	@Column(name = "MachineModel")
	private Integer machineModel;
	
	@Column(name = "ProfitCenter")
	private Integer profitCenter;
	
	@Column(name = "CustomerName")
	private String customerName;
	
	@Column(name = "CustomerMobileNo")
	private String customerMobileNo;
	
	@Column(name = "CurrentHours")
	private String currentHours;
	
	@Column(name = "InstallationDoneBy")
	private Integer installationDoneBy;
	
	@Column(name = "RepresentativeType")
	private Integer representativeType;
	
	@Column(name = "RepresentativeName")
	private String representativeName;
	
	@Column(name = "FileUpload")
	private String fileupload;
	
	@Column(name = "Remarks")
	private String remarks;
	
	@Column(updatable = false)
	private Date createdDate = new Date();

	@Column(updatable = false)
	private BigInteger createdBy;

	private BigInteger modifiedBy;

	private Date modifiedDate;
	
	 @Transient
	private List<InstallationDetailsEntity> installationDetailList;
}
