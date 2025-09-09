/**
 * 
 */
package com.hitech.dms.web.entity.opex;

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
@Table(name = "STG_SA_MIS_OPEX_BUDGET_DTL")
@Entity
@Data
public class StgOpexBudgetDtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -909056828996122762L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stg_opex_Id")
	@JsonIgnore
	private BigInteger stgOpexDtlId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stg_opex_id")
	@JsonIgnore
	private StgOpexHdrEntity opex;

	@Column(name = "gl_id")
	private BigInteger glId;
	private transient String glHeadCode;
	private transient String glHeadName;

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
