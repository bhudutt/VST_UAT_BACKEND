/**
 * 
 */
package com.hitech.dms.web.model.aop.view.response;

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
public class AopTargetDtlViewResponseModel {
	private BigInteger aopDtlId;
	private BigInteger machineItemId;
	private transient String series;
	private transient String segment;
	private transient String variant;
	private transient String model;
	private transient String item;
	private transient String itemDesc;
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
