/**
 * 
 */
package com.hitech.dms.web.model.aop.view.response;

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
public class AopTargetViewResponseModel {
	private BigInteger aopId;
	private Integer pcId;
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private String pcDesc;
	private String aopNumber;
	private String aopDate;
	private String aopStatus;
	private String aopUpdatedDate;
	private String remarks;
	private String aopFinYr;
	private String action;
	private List<AopTargetDtlViewResponseModel> aopTargetDtlList;
}
