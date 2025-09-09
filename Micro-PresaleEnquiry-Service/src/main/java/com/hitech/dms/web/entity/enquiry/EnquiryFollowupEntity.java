/**
 * 
 */
package com.hitech.dms.web.entity.enquiry;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ENQ_FOLLOWUP")
@Entity
@Data
public class EnquiryFollowupEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2064280492345441799L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private BigInteger enquiryFallowUpId;
	
	@JoinColumn(name="enquiry_id")
    @ManyToOne(fetch = FetchType.LAZY)
	private EnquiryHdrEntity enquiryHdr;
	
	@Column(name = "enquiry_type_id")
	private BigInteger enquiryTypeId;
	
	@Column(name = "enquiry_stage_id")
	private Integer enquiryStageId;
	
	@Column(name = "follow_up_type_id")
	private BigInteger followupTypeId;
	
	@Column(name = "current_follow_up_date")
	private Date currentFollowupDate;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "expected_purchase_date")
	private Date expectedPurchaseDate;
	
	@Column(name = "next_follow_up_date")
	private Date nextFollowupDate;
	
	@Column(name = "next_followup_activity_id")
	private BigInteger nextFollowupActivityId;
	
	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
