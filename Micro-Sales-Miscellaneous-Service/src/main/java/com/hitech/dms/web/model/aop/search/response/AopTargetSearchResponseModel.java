/**
 * 
 */
package com.hitech.dms.web.model.aop.search.response;

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
public class AopTargetSearchResponseModel {
	private BigInteger id; // aopId
	private String action;
	private String aopNumber;
	private String aopDate;
	private String aopStatus;
	private String dealerShip;
	private String profitCenter;
	private String finYear;
	private String remarks;
	private String aopUpdatedDate;
}
