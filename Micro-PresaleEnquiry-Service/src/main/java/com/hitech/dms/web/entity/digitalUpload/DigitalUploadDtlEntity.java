package com.hitech.dms.web.entity.digitalUpload;

import java.io.Serializable;
import java.math.BigInteger;

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

/**
 * @author vinay.gautam
 *
 */
@Table(name = "SA_ENQ_DIGITAL_DTL")
@Entity
@Data
public class DigitalUploadDtlEntity implements Serializable {

	private static final long serialVersionUID = -3687604116480160795L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger Digital_Enq_DTL_ID;
	
	@JoinColumn(name="Digital_Enq_HDR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private DigitalUploadHdrEntity digitalEnqHdr;
	
	@Column(name = "Customer_Name")
	private String customerName;
	
	@Column(name = "Customer_Mobile_No")
	private String customerMobileNo;
	
	@Column(name = "Customer_Email_ID")
	private String customerEmailI;
	
	@Column(name = "Model")
	private String model;
	
	@Column(name = "Segment")
	private String segment;
	
	@Column(name = "Customer_District")
	private String customerDistrict;
	
	@Column(name = "Customer_Tehsil")
	private String customerTehsil;
	
	@Column(name = "Customer_State")
	private String customerState;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "Error_Detail")
	private String ecrrorDetail;

}
