package com.hitech.dms.web.entity.activityplan;

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

@Entity
@Table(name="SV_Activity_Plan_Dtl")
@Data
public class ActivityPlanDTLEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;
	@Column(name="ActivityPlanDtlId")
	private Integer activityPlanDtlId;
	@Column(name="DealerId")
	private Integer dealerId;
	@Column(name="ServiceActivityType")
	private Integer serviceActivityType;
	@Column(name="FromDate")
	private Date fromDate;
	@Column(name="ToDate")
	private Date toDate;
	@Column(name="TotalNo_OfDays")
	private Integer totalNoOfDays;
	@Column(name="TotalActivityCost")
	private BigDecimal totalActivityCost;
	@Column(name="VSTShare")
	private BigInteger vstShare;
	@Column(name="NO_OfDay_JobCardTarget")
	private BigInteger noofDayJobCardTarget;
	@Column(name="Service_Revenue_Target")
	private BigInteger serviceRevenueTarget;
	@Column(name="No_Of_Eqquiry")
	private BigInteger noofEqquiry; 
	@Column(name="No_Of_Delivery")
	private BigInteger noofDelivery;
	@Column(name="CreatedDate", updatable = false)
	private Date createdDate=new Date();
	@Column(name="CreatedBy")
	private BigInteger createdBy;
	@Column(name="ModifiedBy", updatable = false)
	private BigInteger modifiedBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	@Column(name="Branch_id")
	private BigInteger branchId;
}
