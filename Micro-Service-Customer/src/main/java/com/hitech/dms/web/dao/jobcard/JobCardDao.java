/**
 * 
 */
package com.hitech.dms.web.dao.jobcard;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.jobcard.edit.request.EditCustomberVoiceRequest;
import com.hitech.dms.web.model.jobcard.edit.request.EditPartsRequest;
import com.hitech.dms.web.model.jobcard.request.ServiceBookingAndQuoationStatusRequestModel;
import com.hitech.dms.web.model.jobcard.request.jobCardCreateRequest;
import com.hitech.dms.web.model.jobcard.response.ActivityPlanModelResponse;
import com.hitech.dms.web.model.jobcard.response.AutoSelectSrvCategoryAndTypeResponse;
import com.hitech.dms.web.model.jobcard.response.InstallationChecklistResponse;
import com.hitech.dms.web.model.jobcard.response.InvoiceSearchResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardApplicationUsedResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardCategoryResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardCreateResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardDataResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardImplementTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardServiceHistoryModelResponse;
import com.hitech.dms.web.model.jobcard.response.JobChassisDetailsResponse;
import com.hitech.dms.web.model.jobcard.response.JobChassisResponse;
import com.hitech.dms.web.model.jobcard.response.JobLabourMasterResponse;
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
import com.hitech.dms.web.model.jobcard.save.request.CustomberVoiceRequest;
import com.hitech.dms.web.model.jobcard.save.request.DocumentFilesResponse;
import com.hitech.dms.web.model.jobcard.save.request.InspectionCheckPointRequest;
import com.hitech.dms.web.model.jobcard.save.request.InstallationUpdateRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCancelRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCloseRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCreateRequest;
import com.hitech.dms.web.model.jobcard.save.request.LabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.LabourGstCalcDtl;
import com.hitech.dms.web.model.jobcard.save.request.OutSiderLabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.PaintRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiChecklistRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiDataUpdateRequest;
import com.hitech.dms.web.model.jobcard.save.request.ServiceActivityRequest;
import com.hitech.dms.web.model.jobcard.save.request.TyreDetailsRequest;
import com.hitech.dms.web.model.jobcard.search.request.JobCardSearchRequest;
import com.hitech.dms.web.model.jobcard.search.request.PdiCheckListResponse;
import com.hitech.dms.web.model.jobcard.search.request.SearchChassisRequest;
import com.hitech.dms.web.model.jobcard.search.response.CustomerServiceResponse;
import com.hitech.dms.web.model.jobcard.search.response.InventoryResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardAndStausResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchResponse;
import com.hitech.dms.web.model.jobcard.search.response.LabourChargeResposne;
import com.hitech.dms.web.model.jobcard.search.response.OutSideLabourResponse;
import com.hitech.dms.web.model.jobcard.search.response.PaintScratchedResponse;
import com.hitech.dms.web.model.jobcard.search.response.PartsResponse;
import com.hitech.dms.web.model.jobcard.search.response.TyreDetailsResponse;
import com.hitech.dms.web.model.models.response.MasterDataModelResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardServiceHistoryModelResponse;

/**
 * @author santosh.kumar
 *
 */
public interface JobCardDao {

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobCardCategoryResponse> fetchJobCardCategory(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobStatusResponse> fetchJobCardSource(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobStatusResponse> fetchJobCardPlaceOfService(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobServiceTypeResponse> fetchJobServiceTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobRepairCatgResponse> fetchJobRepairTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param bookingNo
	 * @return
	 */
	List<JobServiceBookingResponse> fetchServiceBookingList(String authorizationHeader, String userCode,
			String bookingNo);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param invoiceTxt
	 * @return
	 */
	List<InvoiceSearchResponse> fetchInvoiceList(String authorizationHeader, String userCode, String invoiceTxt);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<JobChassisResponse> fetchChassisOrMobileList(String authorizationHeader, String userCode, SearchChassisRequest requestModel);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param vinId
	 * @return
	 */
	List<JobChassisDetailsResponse> fetchChassisDetailsByVinId(String authorizationHeader, String userCode,
			BigInteger vinId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobCardImplementTypeResponse> fetchJobCardImplementTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobCardApplicationUsedResponse> fetchJobCardApplicationUsedList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobRepresentativeResponse> fetchJobCardRepresentativeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobTechnicianListResponse> fetchTechnicianList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param jobcardRequest
	 * @return
	 */
	JobCardCreateResponse createJobCard(String authorizationHeader, String userCode,
			jobCardCreateRequest jobcardRequest);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobServiceActivityTypeResponse> fetchServiceActvityTypeList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<jobBillableTypeResponse> fetchBillableTypeList(String authorizationHeader, String userCode, int categoryId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<jobInventryChecklistReponse> fetchinventryCheckList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<jobpainscratchedResponse> fetchPaintScracthedList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param serachText
	 * @return
	 */
	List<JobLabourMasterResponse> fetchLobourDetailsList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @return
	 */
	List<JobLabourMasterResponse> fetchoutSiderLobourDetailsList(String authorizationHeader, String userCode);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param jobCardCreateRequest
	 * @return
	 */
	JobCardCreateResponse saveJobCardData(String authorizationHeader, String userCode,
			JobCardCreateRequest jobCardCreateRequest);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param custVoice
	 * @param roId
	 * @return
	 */
	JobCardCreateResponse saveCustVoiceData(String authorizationHeader, String userCode,
			CustomberVoiceRequest custVoice, BigInteger roId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param tyreDetailsRequest
	 * @param roid
	 * @return
	 */
	JobCardCreateResponse saveTyreDetails(String authorizationHeader, String userCode,
			TyreDetailsRequest tyreDetailsRequest, BigInteger roid);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param labourCharge
	 * @param roid
	 * @return
	 */
	JobCardCreateResponse saveLabourChargeData(String authorizationHeader, String userCode,
			LabourChargeRequest labourCharge, BigInteger roid);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param outerLabourCharge
	 * @param roid
	 * @return
	 */
	JobCardCreateResponse saveOuterLabourChargeData(String authorizationHeader, String userCode,
			OutSiderLabourChargeRequest outerLabourCharge, BigInteger roid);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param paintRequest
	 * @param paintRequest2
	 * @param roid
	 * @return
	 */
	JobCardCreateResponse savePaintScrachedData(String authorizationHeader, String userCode, PaintRequest paintRequest,
			Integer paintCheckBox, BigInteger roid);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param inventoryRequestId
	 * @param roid
	 * @return
	 */
	JobCardCreateResponse saveInventoryCodes(String authorizationHeader, String userCode, Integer inventoryRequestId,
			BigInteger roid);

	/**
	 * @param userCode
	 * @param roId
	 * @param chassisNoPic
	 * @param hourMeterPic
	 * @param pic1
	 * @param pic2
	 * @return
	 */
	MessageCodeResponse saveDocuments(String userCode, Integer roId, MultipartFile chassisNoPic,
			MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2);

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
	 * @param pdiChecklistRequest
	 * @param roId
	 * @return
	 */
	JobCardCreateResponse savePdiCheckList(String authorizationHeader, String userCode,
			PdiChecklistRequest pdiChecklistRequest, BigInteger roId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param type
	 * @param engineNumber
	 * @return
	 */
	HashMap<String, Object> fetchJobCardaTypeWise(String authorizationHeader, String userCode, String type,
			String searchText);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param inspectionChecklistRequest
	 * @param roId
	 * @return
	 */
	JobCardCreateResponse saveInspectionCheckList(String authorizationHeader, String userCode,
			InspectionCheckPointRequest inspectionChecklistRequest, BigInteger roId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	JobCardDataResponse fetchJobCardDetails(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	TyreDetailsResponse fetchTyreDetails(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<CustomerServiceResponse> fetchCustDetails(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<LabourChargeResposne> fetchLabourChargeDetails(String authorizationHeader, String userCode, Integer roId,
			int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<InventoryResponse> fetchInventoryDetails(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<PaintScratchedResponse> fetchPaintDetails(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<PartsResponse> fetchPartDetails(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<OutSideLabourResponse> fetchOutSideLabourChargeDetails(String authorizationHeader, String userCode,
			Integer roId, int i);

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
	JobCardCreateResponse upkoadPdiFiles(String userCode, PdiDataUpdateRequest requestModel, MultipartFile files);

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
	 * @param roId
	 * @param i
	 * @return
	 */
	List<DocumentFilesResponse> fetchDocsFiles(String authorizationHeader, String userCode, Integer roId, int i);

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
	 * @param partRequest
	 * @param device
	 * @return
	 */
	JobCardCreateResponse saveEditPartDetails(String authorizationHeader, String userCode, EditPartsRequest partRequest,
			Device device);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param custRequest
	 * @param device
	 * @return
	 */
	JobCardCreateResponse saveEditCustomerDetails(String authorizationHeader, String userCode,
			EditCustomberVoiceRequest custRequest, Device device);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param bigInteger 
	 * @param integer 
	 * @param outSideLabourRequest
	 * @param roId
	 * @param device
	 * @return
	 */
	JobCardCreateResponse saveEditOutSideLabourDetails(String authorizationHeader, String userCode,
			Integer integer, BigInteger bigInteger, List<OutSiderLabourChargeRequest> outSideLabourRequest, Integer roId, Device device);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param customerId 
	 * @param branchId 
	 * @param editLabourChargeRequest
	 * @param jobCardId
	 * @param device
	 * @return
	 */
	JobCardCreateResponse saveEditLabourDetails(String authorizationHeader, String userCode,
			Integer branchId, BigInteger customerId, List<LabourChargeRequest> editLabourChargeRequest, Integer jobCardId, Device device);

	/**
	 * @param userCode
	 * @param roId
	 * @param chassisNoPic
	 * @param hourMeterPic
	 * @param pic1
	 * @param pic2
	 * @return
	 */
	MessageCodeResponse updateDocuments(String userCode, Integer roId, MultipartFile chassisNoPic,
			MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2);

	/**
	 * @param userCode
	 * @param vinId
	 * @return
	 */
	List<JobCardAndStausResponse> getJobCardAndStausList(String userCode, BigInteger vinId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	MessageCodeResponse updateBookingAndQuotationStatus(String authorizationHeader, String userCode,
			ServiceBookingAndQuoationStatusRequestModel requestModel);

	/**
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<JobCardAndStausResponse> getJobCardAndStausListByChassis(String userCode, String chassis);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<JobChassisDetailsResponse> fetchChassisDetailsByChassis(String authorizationHeader, String userCode,
			String chassis);

	/**
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<PdiCheckListResponse> getAllCheckpointOfPDI(String userCode, String chassis);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<JobChassisDetailsResponse> fetchOutWordChassisDetailsByChassis(String authorizationHeader, String userCode,
			String chassis);

	/**
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<PdiCheckListResponse> getAllCheckpointOfOutWord(String userCode, String chassis);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<JobChassisDetailsResponse> fetchInstallationChassisDetailsByChassis(String authorizationHeader,
			String userCode, String chassis);

	/**
	 * @param userCode
	 * @param chassis
	 * @return
	 */
	List<InstallationChecklistResponse> getAllCheckpointOfInstallation(String userCode, String chassis);

	/**
	 * @param userCode
	 * @param vin_Id
	 * @param kMReading
	 * @return
	 */
	List<AutoSelectSrvCategoryAndTypeResponse> getAutoSelectSrvCategoryAndTypeResponse(String userCode, Integer vin_Id,
			Integer kMReading);

	/**
	 * @param roId
	 */
	void updateSrvBookingAndQuatationStatus(Integer roId);

	/**
	 * @param userCode
	 * @param profitcenter 
	 * @param searchText
	 * @return
	 */
	HashMap<String, Object> fetchActivityPlanDetails(String userCode, String profitcenter);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param serviceActivityRequest
	 * @param roId
	 * @return
	 */
	JobCardCreateResponse saveActivityDetails(String authorizationHeader, String userCode,
			ServiceActivityRequest serviceActivityRequest, BigInteger roId);

	/**
	 * @param userCode
	 * @param chassis
	 * @param flag 
	 * @return
	 */
	MasterDataModelResponse getChassisDetailsForVehicle(String userCode, String chassisNo, String flag);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<ActivityPlanModelResponse> fetchActivityList(String authorizationHeader, String userCode, Integer roId, int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<JobCardServiceHistoryModelResponse> fetchServiceHistoryList(String authorizationHeader, String userCode,
			Integer roId, int i);

	/**
	 * @param userCode
	 * @param chassisNumber
	 * @return
	 */
	Map<String, Object> fetchServiceActivityByChassis(String userCode, String chassisNumber);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<PdiCheckListResponse> getAllCheckpointOfPDIByRoId(String authorizationHeader, String userCode, Integer roId,
			int i);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param roId
	 * @param i
	 * @return
	 */
	List<InstallationChecklistResponse> getAllCheckpointOfInstallationByRoId(String authorizationHeader,
			String userCode, Integer roId, int i);

	/**
	 * @param userCode
	 * @param quotationText
	 * @param categoryId 
	 * @return
	 */
	Map<String, Object> fetchQuotationSearchList(String userCode, String quotationText, int categoryId);

	/**
	 * @param pdiDtlId
	 * @param i
	 * @param categoryId 
	 */
	void updateFlagInPdiTableByDtlId(int pdiDtlId, int i, int categoryId);

	/**
	 * @param userCode
	 * @param jobCardId
	 * @return
	 */
	Map<String, Object> getValidatePcrButton(String userCode, String jobCardId);

	/**
	 * @param userCode
	 * @return
	 */
	Map<String, Object> fetchJobCardStatusList(String userCode);

	/**
	 * @param installDtlId
	 * @param isActive
	 * @param categoryId
	 */
	void updateFlagInInstallationDtlById(int installDtlId, int isActive, Integer categoryId);

	List<JobChassisDetailsResponse> fetchOutWordChassisDetailsByChassisSTK(String authorizationHeader, String userCode,
			String chassis);

	LabourGstCalcDtl getLabourGstDetail(int branchId, int coustemberId, int labourCodeId, BigDecimal rate, BigDecimal hour);

	LabourGstCalcDtl getOuterLabourGstDetail(int branchId, int coustemberId, int labourCodeId, BigDecimal rate,BigDecimal hour);

	Integer checkLubricant(BigInteger roId);


}
