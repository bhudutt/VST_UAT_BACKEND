/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan.stg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "STG_SA_ACT_PLAN_DLR_MODL")
@Entity
@Data
public class StgActivityPlanDLRModelEntity {
	@Id
	@Column(name = "stg_activity_plan_dlr_id", unique=true, nullable = false)
	private String stgActivityPlanDlrId;

	@Column(name = "stg_activity_plan_hdr_id", nullable = false)
	private String stgActivityPlanHdrId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;
	
	private transient String dealerCode;
	private transient String dealerName;
	private transient String location;
	private transient List<StgActivityPlanDLRModelDTLEntity> activityPlanDLRModelDTLList;

	@Column(name = "model_name")
	private String modelName;

	@Column(name = "plan_date")
	private String planDate;

	@Column(name = "delivery_target")
	private Integer deliveryTarget;

	@Column(name = "conv_ratio")
	private Integer convRatio;

	@Column(name = "target_hot_prospect")
	private Integer targetHotProspect;

	@Column(name = "opening_hot_prospect")
	private Integer openingHotProspect;

	@Column(name = "variance_hot_prospect")
	private Integer varianceHotProspect;

	@Column(name = "billing_plan")
	private Integer billingPlan;

	@Column(name = "oem_share")
	private BigDecimal oemShare;
}
