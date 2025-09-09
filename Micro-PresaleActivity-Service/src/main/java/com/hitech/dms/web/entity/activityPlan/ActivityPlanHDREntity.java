/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan;

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
@Table(name = "SA_ACT_PLAN_HDR")
@Entity
@Data
public class ActivityPlanHDREntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7148635874921580002L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_plan_hdr_id")
	private BigInteger activityPlanHdrId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "activity_number")
	private String activityNo;

	@Column(name = "activity_creation_date", updatable = false)
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date activityCreationDate;

	@Column(name = "activity_month")
	private int activityMonth;

	@Column(name = "activity_year")
	private int activityYear;

	@Column(name = "activity_status")
	private String activityStatus;

	@Column(name = "series_name")
	private String seriesName;

	@Column(name = "segment_name")
	private String segmentName;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;
	@Column(name = "created_date", updatable = false)
	private Date createdDate;
	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;
	@Column(name = "last_modified_date")
	private Date modifiedDate;
}
