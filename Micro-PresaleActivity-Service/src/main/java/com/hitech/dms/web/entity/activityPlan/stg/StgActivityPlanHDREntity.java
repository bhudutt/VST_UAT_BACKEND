/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan.stg;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hitech.dms.web.entity.activityPlan.ActivityPlanHDREntity;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "STG_SA_ACT_PLAN_HDR")
@Entity
@Data
public class StgActivityPlanHDREntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8843078251082005422L;

	@Id
	@Column(name = "stg_activity_plan_hdr_id", unique=true, nullable = false)
	private String stgActivityPlanHdrId;

	@Column(name = "pc_id")
	private Integer pcId;

	@Column(name = "activity_creation_date", updatable = false)
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
	
	private transient List<StgActivityPlanDLRModelEntity> activityPlanDLRModelList;
}
