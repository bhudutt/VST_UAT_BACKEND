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
public class EditCustomberVoiceRequest {
	private boolean deleteFlag;
    private int customerId;
    private String customerConcern;
    private String activityToBeDone;
}
