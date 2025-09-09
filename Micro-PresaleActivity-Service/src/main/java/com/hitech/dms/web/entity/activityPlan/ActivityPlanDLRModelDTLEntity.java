/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan;

import java.math.BigDecimal;
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

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_PLAN_DLR_MODL_DTL")
@Entity
@Data
public class ActivityPlanDLRModelDTLEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_plan_dlr_dtl_id")
	private BigInteger activityPlanDlrDtlId;

	@JoinColumn(name = "activity_plan_dlr_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ActivityPlanDLRModelEntity activityPlanDLR;

	@Column(name = "activity_id")
	private BigInteger activityId;

	@Column(name = "no_of_days")
	private Integer noOfDays;

	@Column(name = "budget_value")
	private BigDecimal budgetValue;

	@Column(name = "oem_share")
	private BigDecimal oemShare;

	@Column(name = "oem_cost")
	private BigDecimal oemCost;
}
