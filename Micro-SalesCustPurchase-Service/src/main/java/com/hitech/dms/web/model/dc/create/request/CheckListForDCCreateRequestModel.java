/**
 * 
 */
package com.hitech.dms.web.model.dc.create.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class CheckListForDCCreateRequestModel {
	private Integer checkListId;
	private Integer pcId;
	private String deliverableChecklist;
	private Boolean isDelivered;
}
