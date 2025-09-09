/**
 * 
 */
package com.hitech.dms.web.model.dc.view.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcCancellationApprovalResponseModel {

	private String cancelRequestDate;
	private String dcCancelDate;
	private String cancelRemark;
	private String cancelReasonDesc;
	private String employeeName;
	private String desginationDesc;
	private String approvalStaus;
	private String approvedDate;
}
