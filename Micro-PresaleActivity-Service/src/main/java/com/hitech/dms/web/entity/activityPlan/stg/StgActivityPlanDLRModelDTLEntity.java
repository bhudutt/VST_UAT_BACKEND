/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan.stg;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "STG_SA_ACT_PLAN_DLR_MODL_DTL")
@Entity
@Data
public class StgActivityPlanDLRModelDTLEntity {
	@Id
	@Column(name = "stg_activity_plan_dlr_dtl_id", nullable = false)
	private String stgActivityPlanDlrDtlId;

	@Column(name = "stg_activity_plan_dlr_id", nullable = false)
	private String stgActivityPlanDlrId;

	@Column(name = "activity_id")
	private Integer activityId;
	private transient String activityDesc;

	@Column(name = "no_of_days")
	private Integer noOfDays;

	@Column(name = "budget_value")
	private BigDecimal budgetValue;

	@Column(name = "oem_share")
	private BigDecimal oemShare;

	@Column(name = "oem_cost")
	private BigDecimal oemCost;
}
