/**
 * 
 */
package com.hitech.dms.web.entity.activity;

import java.io.Serializable;
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
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_ACTUAL_HDR")
@Entity
@Data
public class ActualActivityHDREntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -6075989882964346582L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_actual_hdr_id")
	private BigInteger activityActualHdrId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;
	
	@Column(name = "activity_plan_hdr_id")
	private BigInteger activityPlanHdrId;

	@Column(name = "activity_actual_number")
	private String activityActualNo;

	@Column(name = "activity_actual_date", updatable = false)
	private Date activityActualDate;
	
	@Column(name = "activity_id")
	private BigInteger activityId;

	@Column(name = "activity_location")
	private String activityLocation;
	
	@Column(name = "activity_actual_from_date", updatable = false)
	private Date activityActualFromDate;
	
	@Column(name = "activity_actual_to_date", updatable = false)
	private Date activityActualToDate;
	
	@Column(name = "activity_actual_status")
	private String activityActualStatus;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;
	@Column(name = "created_date", updatable = false)
	private Date createdDate;
	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;
	@Column(name = "last_modified_date")
	private Date modifiedDate;
	
}
