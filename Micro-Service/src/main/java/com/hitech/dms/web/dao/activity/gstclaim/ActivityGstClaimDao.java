/**
 * 
 */
package com.hitech.dms.web.dao.activity.gstclaim;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimGstInvSearchRequest;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvoiceRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvSearchResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvoiceResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimInvApprovalResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityCreditNoteResponseModel;

/**
 * @author santosh.kumar
 *
 */
public interface ActivityGstClaimDao {

	/**
	 * @param userCode
	 * @return
	 */
	Map<String, Object> fetchActivityGstClaimList(String userCode);

	/**
	 * @param userCode
	 * @param activityClaimId
	 * @return
	 */
	Map<String, Object> fetchActivityHeader(String userCode, Integer activityClaimId);

	/**
	 * @param userCode
	 * @param activityClaimId
	 * @return
	 */
	Map<String, Object> fetchActivityClaimDetails(String userCode, Integer activityClaimId);

	/**
	 * @param userCode
	 * @param requestedData
	 * @param device
	 * @return
	 */
	ActivityClaimGstInvoiceResponseModel createActivityGstClaimInvoice(String userCode,
			@Valid ActivityClaimInvoiceRequestModel requestedData, Device device);
	
	ActivityClaimGstInvoiceResponseModel updateActivityGstClaimInvoice(String userCode,
			@Valid ActivityClaimInvoiceRequestModel requestedData, Device device);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<ActivityClaimGstInvSearchResponseModel> fetchActivityClaimGstInvSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel);

	/**
	 * @param userCode
	 * @param activityClaimGstinvoiceId
	 * @return
	 */
	Map<String, Object> fetchActivityClaimGstHeader(String userCode, Integer activityClaimGstinvoiceId);

	/**
	 * @param userCode
	 * @param activityClaimGstinvoiceId
	 * @return
	 */
	Map<String, Object> fetchActivityClaimGstDetails(String userCode, Integer activityClaimGstinvoiceId);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	ActivityClaimInvApprovalResponseModel approveRejectActivityClaimGstInvoice(String userCode,
			ActivityClaimInvApprovalRequestModel requestModel);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<ActivityCreditNoteResponseModel> fetchActivityCreditNoteSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel);

	/**
	 * @param userCode
	 * @return
	 */
	Map<String, Object> fetchActivityClaimGstInvoiceList(String userCode);

	/**
	 * @param activityPlanHdrId
	 * @return
	 */
	Map<String, Object> fetchActivityClaimGstInvByPlanId(BigInteger activityPlanHdrId);
	
	Map<String, Object> getPlantCodeList(String userCode);

}
