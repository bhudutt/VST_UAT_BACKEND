package com.hitech.dms.web.entity.quotation.create.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import lombok.Data;

@Table(name = "SV_QUOTATION_VERBTM")
@Entity
@Data
public class ServiceVerbatimEntity {

	@Id
	@Column(name = "Quotation_Verbtm_Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer quotationVerbtmId;
	
	@ManyToOne
	@JoinColumn(name = "Quotation_Id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ServiceQuotationEntity serviceQuotationEntity;
	
	@Column(name = "Sr_No")
	private Integer srNo;
	
	@Column(name = "Verbatim")
	private String verbatim;
	
	@Column(name = "VerbatimSource")
	private String verbatimSource;
	
	@Column(name = "CustomerVoice")
	private String customerVoice;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private String createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modfiedDate;
	
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	
	@Transient
	private Boolean check;
}
