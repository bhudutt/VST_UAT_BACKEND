/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Entity
@Table(name = "SV_RO_OUTSIDE_LBR_DTL")
@Data
public class OutSiderLabourChargeEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger Id;
	@Column(name = "RO_Id")
	private BigInteger roId;

	@Column(name = "Labour_Id")
	private BigInteger labourCodeId;

	@Column(name = "Bill_Type_ID")
	private BigInteger billableTypeId;

	@Column(name = "StandardHrs")
	private BigDecimal hour;

	@Column(name = "Rate")
	private BigDecimal rate;

	@Column(name = "TotalAmt")
	private BigDecimal amount;
	
	@Column(name = "InsurancePartyId")
	private Integer insurancePartyId;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;
	
	@Column(name = "bayType_Id")
	private int bayTypeId;

	@Column(name = "mechanic_Id")
	private int mechanicId;

	@Column(name = "start_Date")
	private Date startDate;

	@Column(name = "end_Date")
	private Date endDate;

	@Column(name = "start_Time")
	private String startTime;

	@Column(name = "end_Time")
	private String endTime;
	
	@Column(name = "IGST")
	private BigDecimal igst;
	
	@Column(name = "CGST")
	private BigDecimal cgst;
	
	@Column(name = "SGST")
	private BigDecimal sgst;
	
	@Column(name = "IGSTAMT")
	private BigDecimal igstAmt;
	
	@Column(name = "CGSTAMT")
	private BigDecimal cgstAmt;
	
	@Column(name = "SGSTAMT")
	private BigDecimal sgstAmt;
	
	@Column(name = "TOTALGST")
	private BigDecimal gstAmt;
	
	@Column(name="OEM")
	private Integer oem;
	@Column(name="customer")
	private Integer customer;
	@Column(name="dealer")
	private Integer dealer;
	@Column(name="insurance")
	private Integer insurance;

	// private String deleteFlag;
}
