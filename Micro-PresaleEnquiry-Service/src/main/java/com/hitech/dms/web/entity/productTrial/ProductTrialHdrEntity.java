package com.hitech.dms.web.entity.productTrial;



import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Entity
@Table(name = "SA_PRODUCT_TRIAL_HDR")
@Data
public class ProductTrialHdrEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -108024506090247623L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Product_Trial_ID")
	private BigInteger productTrialId;


	@Column(name = "Product_Trial_No")
	private String productTrialNo;
	
	@JsonProperty(value = "enquiryId", required = true)
	@NotNull(message = "Enquiry No Is Required")
	@Column(name = "enquiry_id")
	private BigInteger enquiryId;
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "trialDate", required = true)
	@NotNull(message = "Product Trial Date Is Required")
	@Column(name = "Product_Trial_Date")
	private Date trialDate;
	
	
	//@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "startTime", required = true)
	@NotNull(message = "Start Time Is Required")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Column(name = "Start_time")
	private String startTime;
	
	//@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "endTime", required = true)
	@NotNull(message = "End Time Is Required")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Column(name = "End_Time")
	private String endTime;
	
	@JsonProperty(value = "trialModelId", required = true)
	@NotNull(message = "Trial Model No Is Required")
	@Column(name = "Trial_Model_Id")
	private BigInteger trialModelId;
	
	@JsonProperty(value = "startKm", required = true)
	@NotNull(message = "Start KM Is Required")
	@Column(name = "Start_Km")
	private String startKm;
	
	@JsonProperty(value = "endKm", required = true)
	@NotNull(message = "End KM Is Required")
	@Column(name = "End_Km")
	private String endKm;
	
	
	@JsonProperty(value = "chassisNo", required = true)
	@NotNull(message = "Chassis No Is Required")
	@Column(name = "Chassis_no")
	private String chassisNo;
	
	@JsonProperty(value = "trialGivenById", required = true)
	@NotNull(message = "Trial Given By Is Required")
	@Column(name = "Trial_Given_By")
	private BigInteger trialGivenById;
	

	@Column(name = "Overall_Rating")
	private BigDecimal overallRating;
	
	@JsonProperty(value = "tempRegNo", required = true)
	@NotNull(message = "Temp Reg No Is Required")
	@Column(name = "Temp_Reg_No")
	private String tempRegNo;
	
	@JsonProperty(value = "overallRemarks", required = true)
	@NotNull(message = "Remarks Is Required")
	@Column(name = "Remarks")
	private String overallRemarks;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "productTrialHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<ProductTrialFeedbackEntity> feedback;
}
