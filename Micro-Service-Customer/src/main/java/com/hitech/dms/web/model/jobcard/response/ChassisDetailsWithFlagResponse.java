/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.util.List;

import com.hitech.dms.web.model.jobcard.search.request.PdiCheckListResponse;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ChassisDetailsWithFlagResponse {
	private String jobCardNumber;
	private String Status;
	private String code;
	private String message;
	List<JobChassisDetailsResponse> JobChassisDetailsResponse;
	List<PdiCheckListResponse> pdiCheckListResponse;
	List<InstallationChecklistResponse>installationChecklistResponse;

}
