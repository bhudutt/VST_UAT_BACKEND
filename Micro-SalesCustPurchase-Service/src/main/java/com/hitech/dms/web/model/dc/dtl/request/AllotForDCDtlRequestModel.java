/**
 * 
 */
package com.hitech.dms.web.model.dc.dtl.request;

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
public class AllotForDCDtlRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private int flag;
	private String isFor;
}
