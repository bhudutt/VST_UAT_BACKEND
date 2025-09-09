/**
 * 
 */
package com.hitech.dms.web.model.machinepo.approval.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePoApprovalResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
