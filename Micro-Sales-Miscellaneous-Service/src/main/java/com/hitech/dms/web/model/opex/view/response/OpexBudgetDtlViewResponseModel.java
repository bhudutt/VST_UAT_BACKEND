/**
 * 
 */
package com.hitech.dms.web.model.opex.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class OpexBudgetDtlViewResponseModel {
	private BigInteger opexDtlId;
	private BigInteger glId;
	private String glHeadCode;
	private String glHeadName;
	private BigDecimal month1;
	private BigDecimal month2;
	private BigDecimal month3;
	private BigDecimal month4;
	private BigDecimal month5;
	private BigDecimal month6;
	private BigDecimal month7;
	private BigDecimal month8;
	private BigDecimal month9;
	private BigDecimal month10;
	private BigDecimal month11;
	private BigDecimal month12;
}
