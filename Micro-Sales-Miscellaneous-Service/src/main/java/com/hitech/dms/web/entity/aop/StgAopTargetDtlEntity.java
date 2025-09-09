/**
 * 
 */
package com.hitech.dms.web.entity.aop;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name="STG_SA_MIS_AOP_TARGET_DTL")
@Entity
@Data
public class StgAopTargetDtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2198018683818376758L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="stg_aop_dtl_id")
	@JsonIgnore
	private BigInteger aopDtlId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stg_aop_id")
	@JsonIgnore
	private StgAopHdrEntity aop;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	private transient String series;
	private transient String segment;
	private transient String variant;
	private transient String model;
	private transient String item;
	private transient String itemDesc;
	
	@Column(name = "month_1")
	private BigDecimal month1;
	
	@Column(name = "month_2")
	private BigDecimal month2;
	
	@Column(name = "month_3")
	private BigDecimal month3;
	
	@Column(name = "month_4")
	private BigDecimal month4;
	
	@Column(name = "month_5")
	private BigDecimal month5;
	
	@Column(name = "month_6")
	private BigDecimal month6;
	
	@Column(name = "month_7")
	private BigDecimal month7;
	
	@Column(name = "month_8")
	private BigDecimal month8;
	
	@Column(name = "month_9")
	private BigDecimal month9;
	
	@Column(name = "month_10")
	private BigDecimal month10;
	
	@Column(name = "month_11")
	private BigDecimal month11;
	
	@Column(name = "month_12")
	private BigDecimal month12;
}
