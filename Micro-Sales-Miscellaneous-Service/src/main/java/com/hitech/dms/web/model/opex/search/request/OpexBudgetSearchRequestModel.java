/**
 * 
 */
package com.hitech.dms.web.model.opex.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

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
public class OpexBudgetSearchRequestModel {
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger stateId;
	private String opexNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private int page;
	private int size;
}
