package com.hitech.dms.web.entity.spare.partymaster.mapping;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_Dealer_Distributor_Mapping_Dtl")
public class DealerDistributorMappingDtlEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private BigInteger id;
	
	@Column(name="hdr_id")
	private Integer hdrId;
	
	@Column(name="Dealer_Code")
	private String dealerCode;
	
	@Column(name="Dealer_id")
	private Integer dealerId;
	
	@Column(name="Dealer_Name")
	private String dealerName;
	
	@Column(name="Dealer_PinCode")
	private String dealerPinCode;
	
	@Column(name="Dealer_District")
	private String dealerDistrict;
	
	@Column(name="Dealer_Tehsil")
	private Integer dealerTehsil;
	
	@Column(name="Dealer_State")
	private String dealerState;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
}
