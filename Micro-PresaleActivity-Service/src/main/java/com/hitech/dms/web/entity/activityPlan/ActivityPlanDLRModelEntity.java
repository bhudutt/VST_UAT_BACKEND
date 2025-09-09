/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan;

import java.math.BigDecimal;
import java.math.BigInteger;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_PLAN_DLR_MODL")
@Entity
@Data
public class ActivityPlanDLRModelEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_plan_dlr_id")
	private BigInteger activityPlanDlrId;

	@Column(name = "activity_plan_hdr_id")
	private BigInteger activityPlanHdrId;

	@Column(name = "dealer_id")
	private BigInteger dealerId;

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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "activityPlanDLR", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<ActivityPlanDLRModelDTLEntity> activityPlanDLRModelDTLList;
}
