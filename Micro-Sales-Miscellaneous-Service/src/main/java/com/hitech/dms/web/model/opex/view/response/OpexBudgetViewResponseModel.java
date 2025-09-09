/**
 * 
 */
package com.hitech.dms.web.model.opex.view.response;

import java.math.BigInteger;
import java.util.List;

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
public class OpexBudgetViewResponseModel {
	private BigInteger opexId;
	private Integer pcId;
	private BigInteger stateId;
	private String stateCode;
	private String stateName;
	private String pcDesc;
	private String opexNumber;
	private String opexDate;
	private String opexStatus;
	private String opexUpdatedDate;
	private String remarks;
	private String opexFinYr;
	private String action;
	private List<OpexBudgetDtlViewResponseModel> opexBudgetDtlList;
}
