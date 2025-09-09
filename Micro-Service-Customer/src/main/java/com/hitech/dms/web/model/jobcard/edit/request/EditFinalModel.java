/**
 * 
 */
package com.hitech.dms.web.model.jobcard.edit.request;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.jobcard.save.request.LabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.OutSiderLabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiChecklistRequest;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class EditFinalModel {
	private Integer jobCardId;
	private Integer categoryId;
	private Integer branchId;
	private BigInteger customerId;
	private List<EditCustomberVoiceRequest> customberVoiceRequest;
	private List<OutSiderLabourChargeRequest> outSiderLabourChargeRequest;
	private List<LabourChargeRequest> labourChargeRequest;
	private List<EditPartsRequest> parts;
	private List<PdiChecklistRequest> pdiChecklistRequest;
	private List<installationCheckPointUpdateRequest> inspectionCheckPointRequest;

}
