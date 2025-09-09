package com.hitech.dms.web.entity.activityClaim;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Table(name = "SA_ACT_CLAIM_HDR")
@Entity
@Data
public class ActivityClaimHdrEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4810699861512501601L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_claim_hdr_id")
	private BigInteger activityClaimHdrId;
	
	
	@JsonProperty(value = "dealerId", required = true)
	@NotNull(message = "Dealer is Required")
	@Column(name = "dealer_id")
	private BigInteger dealerId;
	
	@JsonProperty(value = "activityPlanHdrId", required = true)
	@NotNull(message = "Activity Plan is Required")
	@Column(name = "activity_plan_hdr_id")
	private BigInteger activityPlanHdrId;
	
//	@JsonProperty(value = "activityClaimNumber", required = true)
//	@NotNull(message = "Activity Claim Number is Required")
	@Column(name = "activity_claim_number")
	private String activityClaimNumber;
	
	
	@JsonProperty(value = "activityClaimDate", required = true)
	@NotNull(message = "Activity Claim Date is Required")
	@JsonDeserialize(using = DateHandler.class)
	@Column(name = "activity_claim_date")
	private Date activityClaimDate;
	
//	@JsonProperty(value = "activityClaimStatus", required = true)
//	@NotNull(message = "Activity Claim Status is Required")
	@Column(name = "activity_claim_status")
	private String activityClaimStatus;
	
	
	@JsonProperty(value = "totalClaimAmount", required = true)
	@NotNull(message = "Total Claim Amount is Required")
	@Column(name = "total_claim_amount")
	private BigDecimal totalClaimAmount;
	
	@Column(name = "total_approved_amount")
	private BigDecimal totalApprovedAmount;
	
	@Column(name = "approver_final_remarks")
	private String approverFinalRemarks;
	
	

	@Column(name = "created_by")
	private BigInteger createdBy;
	

	@Column(name = "created_date")
	private Date createdDate;
	
	
	@Column(name = "last_modified_by")
	private BigInteger lastModifiedBy;
	
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "acHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<ActivityClaimDtlEntity> activityClaimDtl;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "acHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	private List<ActivityClaimApprovalEntity> activityClaimApprove;
	
	
	
	
    @Transient
	private BigInteger activityActualHdrId;
    
    

}
