/**
 * 
 */
package com.hitech.dms.web.service.jobcard;

import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.jobcard.response.AutoSelectSrvCategoryAndTypeResponse;
import com.hitech.dms.web.model.jobcard.response.ChassisDetailsWithFlagResponse;
import com.hitech.dms.web.model.jobcard.edit.request.EditFinalModel;
import com.hitech.dms.web.model.jobcard.request.ServiceBookingAndQuoationStatusRequestModel;
import com.hitech.dms.web.model.jobcard.request.jobCardCreateRequest;
import com.hitech.dms.web.model.jobcard.response.InvoiceSearchResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardApplicationUsedResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardCategoryResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardCreateResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardImplementTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardStatusResponse;
import com.hitech.dms.web.model.jobcard.response.JobChassisDetailsResponse;
import com.hitech.dms.web.model.jobcard.response.JobRepairCatgResponse;
import com.hitech.dms.web.model.jobcard.response.JobRepresentativeResponse;
import com.hitech.dms.web.model.jobcard.response.JobServiceActivityTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobServiceBookingResponse;
import com.hitech.dms.web.model.jobcard.response.JobServiceTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobStatusResponse;
import com.hitech.dms.web.model.jobcard.response.JobTechnicianListResponse;
import com.hitech.dms.web.model.jobcard.response.jobBillableTypeResponse;
import com.hitech.dms.web.model.jobcard.response.jobInventryChecklistReponse;
import com.hitech.dms.web.model.jobcard.response.jobpainscratchedResponse;
import com.hitech.dms.web.model.jobcard.save.request.InstallationUpdateRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCancelRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCloseRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardFinalRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiDataUpdateRequest;
import com.hitech.dms.web.model.jobcard.search.request.JobCardSearchRequest;
import com.hitech.dms.web.model.jobcard.search.request.SearchChassisRequest;
import com.hitech.dms.web.model.jobcard.search.response.JobCardDetailsResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchTypeWiseResponse;
import com.hitech.dms.web.model.models.response.MasterDataModelResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import com.hitech.dms.web.model.jobcard.response.JobChassisResponse;
import com.hitech.dms.web.model.jobcard.response.JobLabourMasterResponse;

/**
 * @author santosh.kumar
 *
 */
public interface JobCardService {

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobCardCategoryResponse> getJobCardCategoryList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobStatusResponse> getJobSourceList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobStatusResponse> getJobPlaceOfService(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobServiceTypeResponse> getJobServiceTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobRepairCatgResponse> getJobRepairType(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param bookingNo
	 * @return
	 */
	List<JobServiceBookingResponse> getJobServiceBookingSearch(String authorizationHeader, String userCode,
			String bookingNo);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param invoiceTxt
	 * @return
	 */
	List<InvoiceSearchResponse> getSearchInvoice(String authorizationHeader, String userCode, String invoiceTxt);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<JobChassisResponse> getSearchChassisOrMobileNo(String authorizationHeader, String userCode,
			SearchChassisRequest requestModel);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param vinId
	 * @return
	 */
	ChassisDetailsWithFlagResponse getSearchChassisDetailsByVinId(String authorizationHeader, String userCode,
			BigInteger vinId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobCardImplementTypeResponse> getJobCardImplementTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobCardApplicationUsedResponse> getJobCardApplicationUsedList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobRepresentativeResponse> getRepresentativeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobTechnicianListResponse> getTechnicianList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param jobcardRequest
	 * @return
	 */
	JobCardCreateResponse getJobCardCreation(String authorizationHeader, String userCode,
			jobCardCreateRequest jobcardRequest);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobServiceActivityTypeResponse> getServiceActvityTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param categoryId
	 * @return
	 */
	List<jobBillableTypeResponse> getBillableTypeList(String authorizationHeader, String userCode, int categoryId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<jobInventryChecklistReponse> getinventryCheckList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<jobpainscratchedResponse> getPaintScracthedList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobLabourMasterResponse> getLobourDetails(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobLabourMasterResponse> getOutSiderLobourDetails(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param jobCardData
	 * @return
	 */
	JobCardCreateResponse saveJobCardData(String authorizationHeader, String userCode, JobCardFinalRequest jobCardData);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param chassisNoPic
	 * @param hourMeterPic
	 * @param pic1
	 * @param pic2
	 * @return
	 */
	MessageCodeResponse uploadDocuments(String authorizationHeader, String userCode, Integer roId,
			MultipartFile chassisNoPic, MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param jobCardRequest
	 * @return
	 */
	List<JobCardSearchResponse> getsearchJobCardDetails(String authorizationHeader, String userCode,
			JobCardSearchRequest jobCardRequest);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param type
	 * @param engineNumber
	 * @param vinNumber
	 * @param invoiceNumber
	 * @param quotatioNumber
	 * @param bookingNumber
	 * @param customerName
	 * @param mobileNumber
	 * @return
	 */
	JobCardSearchTypeWiseResponse getsearchJobCardaTypeWise(String authorizationHeader, String userCode, String type,
			String chassisNo,String engineNumber, String vinNumber, String mobileNumber, String customerName, String bookingNumber,
			String quotatioNumber, String invoiceNumber);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @return
	 */
	JobCardDetailsResponse getJobCardDetailsByRoId(String authorizationHeader, String userCode, Integer roId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	JobCardCreateResponse closeJobCard(String authorizationHeader, String userCode, JobCardCloseRequest requestModel,
			Device device);

	/**
	 * @param userCode
	 * @param requestModel
	 * @param files
	 * @return
	 */
	JobCardCreateResponse uploadPdiFiles(String userCode, PdiDataUpdateRequest requestModel, MultipartFile files);

	/**
	 * @param userCode
	 * @param requestModel
	 * @param files
	 * @return
	 */
	JobCardCreateResponse uploadInstallationFiles(String userCode, InstallationUpdateRequest requestModel,
			MultipartFile files);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	JobCardCreateResponse cancelJobCard(String authorizationHeader, String userCode, JobCardCancelRequest requestModel,
			Device device);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	JobCardCreateResponse editJobCard(String authorizationHeader, String userCode, EditFinalModel requestModel,
			Device device);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param chassisNoPic
	 * @param hourMeterPic
	 * @param pic1
	 * @param pic2
	 * @return
	 */
	MessageCodeResponse editUploadImage(String authorizationHeader, String userCode, Integer roId,
			MultipartFile chassisNoPic, MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	MessageCodeResponse updateServieAndQuoationStatus(String authorizationHeader, String userCode, ServiceBookingAndQuoationStatusRequestModel requestModel);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param chassis
	 * @param categoryId
	 * @return
	 */
	ChassisDetailsWithFlagResponse getSearchChassisDetailsByChassis(String authorizationHeader, String userCode,
			String chassis, Integer categoryId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param vin_Id
	 * @param kMReading
	 * @return
	 */
	List<AutoSelectSrvCategoryAndTypeResponse> getAutoSelectSrvCategoryAndType(String authorizationHeader, String userCode,
			Integer vin_Id, Integer kMReading);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param profitcenter 
	 * @param searchText
	 * @return
	 */
	HashMap<String, Object> getActivityDetails(String authorizationHeader, String userCode, String profitcenter);

	/**
	 * @param userCode
	 * @param chassis
	 * @param flag 
	 * @return
	 */
	MasterDataModelResponse getChassisDetails(String userCode, String chassisNo, String flag);

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

	/**
	 * @param userCode
	 * @param device
	 * @param chassisNumber
	 * @return
	 */
	Map<String, Object> getServiceHistoryByChassis(String userCode, Device device, String chassisNumber);

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	JasperPrint PdfGeneratorReportForJobCardOpen(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath);

	/**
	 * @param userCode
	 * @param device
	 * @param quotationText
	 * @param categoryId 
	 * @return
	 */
	Map<String, Object> getQuotationSearchList(String userCode, Device device, String quotationText, int categoryId);

	/**
	 * @param userCode
	 * @param device
	 * @param jobCardId
	 * @return
	 */
	Boolean getValidatePcrButton(String userCode, Device device, String jobCardId);

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	JasperPrint jobcasexlsxGeneratorReport(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath);

	/**
	 * @param jasperPrint
	 * @param format
	 * @param printStatusv
	 * @param outputStream
	 * @throws JRException 
	 */
	void printReportExcel(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream) throws JRException;

	/**
	 * @param userCode
	 * @return
	 */
	Map<String, Object> getJobCardStatusList(String userCode);
	
	
	public Resource loadFileAsResource(String fileName, String docPath, Long id,String path) throws MalformedURLException;

	Integer checkLubricant(String userCode, Device device, BigInteger roId);

}
