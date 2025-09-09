/**
 * 
 */
package com.hitech.dms.web.model.allot.enq.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineEnqDtlForAllotRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private int flag;
	private String isFor;
	private String bussinessType;
}
