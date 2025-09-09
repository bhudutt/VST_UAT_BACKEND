package com.hitech.dms.web.entity.digitalUpload;



import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Table(name = "SA_ENQ_DIGITAL_HDR")
@Entity
@Data
public class DigitalUploadHdrEntity implements Serializable {
	private static final long serialVersionUID = -3269854679106073960L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Digital_Enq_HDR_ID")
	private BigInteger digitalEnqHdrId;
	
	@Column(name = "Digital_Enq_No")
	private String digitalEnqNo;
	
	@Column(name = "Digital_Source_ID")
	private BigInteger digitalSourceId;
	
	@Column(name = "PC_ID")
	private BigInteger pcId;
	
	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "digitalEnqHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<DigitalUploadDtlEntity> digitalUploadDtl;

}
