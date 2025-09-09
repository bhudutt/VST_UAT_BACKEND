/**
 * 
 */
package com.hitech.dms.web.model.jobcard.save.request;

import java.util.List;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class JobCardFinalRequest {
	    private JobCardCreateRequest jobCardCreateRequest;
	    private TyreDetailsRequest tyreDetailsRequest;
	    private List<CustomberVoiceRequest> customberVoiceRequest;
	    private List<LabourChargeRequest> labourChargeRequest;
	    private List<OutSiderLabourChargeRequest> outSiderLabourChargeRequest;
	    private List<PdiChecklistRequest> pdiChecklistRequest;
	    private List<InspectionCheckPointRequest> inspectionCheckPointRequest;
	    private PaintRequest paintRequest;
	    private List<Integer> selectedInventoryCodes;
	    private List<Integer> selectedPaintScratched;
	    private ServiceActivityRequest serviceActivityRequest;
}
