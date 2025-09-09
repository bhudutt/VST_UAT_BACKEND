package com.hitech.dms.web.entity.pcr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Data
@Table(name = "SV_WA_PCR_DTL")
public class ServiceWarrantyPcrDtlEntity implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Integer id;
	
//	@Column(name = "Pcr_ID")
//	private Long pcrId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "Pcr_ID", referencedColumnName = "id")
    private ServiceWarrantyPcr serviceWarrantyPcr;
	
	@Column(name = "Part_Id")
	private Integer partId;
	
	@Column(name = "Failure_Type_ID")
	private Integer failureTypeId;
	
	@Column(name = "failure_code_id")
	private Integer failureCodeId;
	
	
}
