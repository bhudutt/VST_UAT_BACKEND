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

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name="SA_MIS_AOP_TARGET_DTL")
@Entity
@Data
public class AopTargetDtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1589796789943056289L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="aop_dtl_id")
	private BigInteger aopDtlId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aop_id")
	private AopHdrEntity aop;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	
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
