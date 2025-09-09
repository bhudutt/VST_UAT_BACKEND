/**
 * 
 */
package com.hitech.dms.web.service.activity.gstclaim;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimGstInvSearchRequest;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvoiceRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvSearchResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvoiceResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimInvApprovalResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityCreditNoteResponseModel;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
public interface ActivityGstClaimService {

	/**
	 * @param userCode
	 * @param device
	 * @return
	 */
	Map<String, Object> getActivityClaimNoList(String userCode, Device device);

	/**
	 * @param userCode
	 * @param device
	 * @param activityClaimId
	 * @return
	 */
	Map<String, Object> getActivityClaimDetailsById(String userCode, Device device, Integer activityClaimId);

	/**
	 * @param userCode
	 * @param requestedData
	 * @param device
	 * @return
	 */
	ActivityClaimGstInvoiceResponseModel createActivityGstClaimInvoice(String userCode,
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
	 * @param device
	 * @param activityClaimGstinvoiceId
	 * @return
	 */
	Map<String, Object> getActivityGstClaimInvoiceDetailsById(String userCode, Device device,
			Integer activityClaimGstinvoiceId);

	/**
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	ActivityClaimInvApprovalResponseModel approveRejectActivityClaimGstInvoice(String userCode,
			ActivityClaimInvApprovalRequestModel requestModel, Device device);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<ActivityCreditNoteResponseModel> fetchActivityCreditNoteSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel);

	/**
	 * @param userCode
	 * @param device
	 * @return
	 */
	Map<String, Object> getActivityClaimGstInvoiceList(String userCode, Device device);

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	JasperPrint PdfGeneratorReport(HttpServletRequest request, String string, HashMap<String, Object> jasperParameter,
			String filePath);

	/**
	 * @param jasperPrint
	 * @param format
	 * @param printStatus
	 * @param outputStream
	 * @param reportName
	 * @throws Exception 
	 */
	void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception;
	
	
	Map<String, Object> getPlantCodeList(String userCode, Device device);

}
