/**
 * 
 */
package com.hitech.dms.web.model.jobcard.edit.request;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class EditPartsRequest {
	private int requisitionId;
	private int bileableId;
	private int oem;
	private int customer;
	private int dealer;
	private int insurance;
}
