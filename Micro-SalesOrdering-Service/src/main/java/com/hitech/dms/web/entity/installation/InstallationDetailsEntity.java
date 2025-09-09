package com.hitech.dms.web.entity.installation;

import java.math.BigInteger;
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
@Table(name = "SV_INSTALLATION_DTL")
public class InstallationDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	
	@Column(name = "InstallationHdrId")
	private Integer installationId;
	
	@Column(name = "OkFlag")
	private Boolean okFlag = false;
	
	@Column(name = "CheckPointId")
	private Integer checkPoint;
	
	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate = new Date();

	private BigInteger modifiedBy;

	private Date modifiedDate;
}
