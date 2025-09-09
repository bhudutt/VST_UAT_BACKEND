/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.response;

import java.util.List;

import com.hitech.dms.web.model.jobcard.response.ActivityPlanModelResponse;
import com.hitech.dms.web.model.jobcard.response.InstallationChecklistResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardDataResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardServiceHistoryModelResponse;
import com.hitech.dms.web.model.jobcard.save.request.CustomberVoiceRequest;
import com.hitech.dms.web.model.jobcard.save.request.DocumentFilesResponse;
import com.hitech.dms.web.model.jobcard.save.request.InspectionCheckPointRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCreateRequest;
import com.hitech.dms.web.model.jobcard.save.request.LabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.OutSiderLabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.PaintRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiChecklistRequest;
import com.hitech.dms.web.model.jobcard.save.request.TyreDetailsRequest;
import com.hitech.dms.web.model.jobcard.search.request.PdiCheckListResponse;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardDetailsResponse {
	private JobCardDataResponse jobCardSearchResponse;
	private TyreDetailsResponse tyreDetailsResponse;
	private List<CustomerServiceResponse> customberVoiceResponse;
	private List<LabourChargeResposne> labourChargeResposne;
	private List<OutSideLabourResponse> outSideLabourResponse;
	private List<InventoryResponse> inventoryResponse;
	private List<PaintScratchedResponse> paintScratchedResponse;
	private List<PartsResponse> partsResponse;
	private List<DocumentFilesResponse> docsResponse; 
	private List<ActivityPlanModelResponse> activityResponse;
	private List<JobCardServiceHistoryModelResponse> serviceHistoryResponse;
	private List<PdiCheckListResponse> pdiCheckListResponse;
	private List<InstallationChecklistResponse>installationChecklistResponse;
}
