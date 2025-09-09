/**
 * 
 */
package com.hitech.dms.web.model.opex.template.response;

import java.math.BigDecimal;

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
public class OpexBudgetResponseModel {
	private String glHeadCode;
	private String glHeadName;
	private String finYear;
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
