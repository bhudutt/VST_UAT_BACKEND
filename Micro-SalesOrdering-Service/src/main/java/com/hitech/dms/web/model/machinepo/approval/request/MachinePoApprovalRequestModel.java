/**
 * 
 */
package com.hitech.dms.web.model.machinepo.approval.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePoApprovalRequestModel {
	private BigInteger poHdrId;
	private String poNumber;
	private String approvalStatus;
	private String remarks;
}
