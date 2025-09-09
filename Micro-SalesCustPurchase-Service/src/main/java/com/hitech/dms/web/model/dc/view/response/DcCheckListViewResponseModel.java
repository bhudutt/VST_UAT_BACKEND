/**
 * 
 */
package com.hitech.dms.web.model.dc.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcCheckListViewResponseModel {
	private BigInteger dcCheckListId;
	private Integer checkListId;
	private Integer pcId;
	private String deliverableChecklist;
	private Boolean isDelivered;
}
