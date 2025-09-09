/**
 * 
 */
package com.hitech.dms.web.model.dc.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class CancelReasonResponseModel {
	private Integer cancelReasonId;
	private String cancelReasonType;
	private String cancelReasonDesc;
}
