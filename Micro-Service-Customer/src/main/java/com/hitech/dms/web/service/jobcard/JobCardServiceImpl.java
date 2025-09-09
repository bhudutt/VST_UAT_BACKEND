/**
 * 
 */
package com.hitech.dms.web.service.jobcard;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.app.exceptions.RecordNotFoundException;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.jobcard.JobCardDao;
import com.hitech.dms.web.model.jobcard.edit.request.EditCustomberVoiceRequest;
import com.hitech.dms.web.model.jobcard.edit.request.EditFinalModel;
import com.hitech.dms.web.model.jobcard.edit.request.EditPartsRequest;
import com.hitech.dms.web.model.jobcard.edit.request.installationCheckPointUpdateRequest;
import com.hitech.dms.web.model.jobcard.request.ServiceBookingAndQuoationStatusRequestModel;
import com.hitech.dms.web.model.jobcard.request.jobCardCreateRequest;
import com.hitech.dms.web.model.jobcard.response.ActivityPlanModelResponse;
import com.hitech.dms.web.model.jobcard.response.AutoSelectSrvCategoryAndTypeResponse;
import com.hitech.dms.web.model.jobcard.response.ChassisDetailsWithFlagResponse;
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
import com.hitech.dms.web.model.jobcard.save.request.JobCardFinalRequest;
import com.hitech.dms.web.model.jobcard.save.request.LabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.LabourGstCalcDtl;
import com.hitech.dms.web.model.jobcard.save.request.OutSiderLabourChargeRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiChecklistRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiDataUpdateRequest;
import com.hitech.dms.web.model.jobcard.search.request.JobCardSearchRequest;
import com.hitech.dms.web.model.jobcard.search.request.PdiCheckListResponse;
import com.hitech.dms.web.model.jobcard.search.request.SearchChassisRequest;
import com.hitech.dms.web.model.jobcard.search.response.CustomerServiceResponse;
import com.hitech.dms.web.model.jobcard.search.response.InventoryResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardAndStausResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardDetailsResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchTypeWiseResponse;
import com.hitech.dms.web.model.jobcard.search.response.LabourChargeResposne;
import com.hitech.dms.web.model.jobcard.search.response.OutSideLabourResponse;
import com.hitech.dms.web.model.jobcard.search.response.PaintScratchedResponse;
import com.hitech.dms.web.model.jobcard.search.response.PartsResponse;
import com.hitech.dms.web.model.jobcard.search.response.TyreDetailsResponse;
import com.hitech.dms.web.model.models.response.MasterDataModelResponse;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author santosh.kumar
 *
 */
@Service
public class JobCardServiceImpl implements JobCardService {
	@Autowired
	private JobCardDao jobCardDao;

	@Autowired
	private ConnectionConfiguration dataSourceConnection;

	@Value("${file.upload-dir.JobCardReport}")
	private String downloadPathPreInvoice;

	@Value("${file.upload-dir.JobCardOpenReport}")
	private String downloadPathOpenJobCard;

	@Override
	public List<JobCardCategoryResponse> getJobCardCategoryList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchJobCardCategory(authorizationHeader, userCode);
	}

	@Override
	public List<JobStatusResponse> getJobSourceList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchJobCardSource(authorizationHeader, userCode);
	}

	@Override
	public List<JobStatusResponse> getJobPlaceOfService(String authorizationHeader, String userCode) {
		return jobCardDao.fetchJobCardPlaceOfService(authorizationHeader, userCode);
	}

	@Override
	public List<JobServiceTypeResponse> getJobServiceTypeList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchJobServiceTypeList(authorizationHeader, userCode);
	}

	@Override
	public List<JobRepairCatgResponse> getJobRepairType(String authorizationHeader, String userCode) {
		return jobCardDao.fetchJobRepairTypeList(authorizationHeader, userCode);
	}

	@Override
	public List<JobServiceBookingResponse> getJobServiceBookingSearch(String authorizationHeader, String userCode,
			String bookingNo) {

		return jobCardDao.fetchServiceBookingList(authorizationHeader, userCode, bookingNo);
	}

	@Override
	public List<InvoiceSearchResponse> getSearchInvoice(String authorizationHeader, String userCode,
			String invoiceTxt) {

		return jobCardDao.fetchInvoiceList(authorizationHeader, userCode, invoiceTxt);
	}

	@Override
	public List<JobChassisResponse> getSearchChassisOrMobileNo(String authorizationHeader, String userCode,
			SearchChassisRequest requestModel) {
		return jobCardDao.fetchChassisOrMobileList(authorizationHeader, userCode, requestModel);
	}

	@Override
	public ChassisDetailsWithFlagResponse getSearchChassisDetailsByVinId(String authorizationHeader, String userCode,
			BigInteger vinId) {
		ChassisDetailsWithFlagResponse response = new ChassisDetailsWithFlagResponse();
		List<JobChassisDetailsResponse> JobChassisDetailsResponse = null;
		boolean flag = true;
		try {
			List<JobCardAndStausResponse> list = jobCardDao.getJobCardAndStausList(userCode, vinId);
			if (list != null && !list.isEmpty()) {

//				if (list.stream().anyMatch(s -> s.getStatus().equals("Closed"))) {
//					flag = true;
//				} else {
//					flag = false;
//					response.setJobCardNumber(list.get(0).getRoNumber());
//					response.setStatus(list.get(0).getStatus());
//					response.setCode("500");
//					response.setMessage(
//							"Job Card (" + list.get(0).getRoNumber() + ") already open for the chassis number !!!");
//				}
				if (list.stream().anyMatch(s -> s.getStatus().equals("Open"))) {
					flag = false;
					response.setJobCardNumber(list.get(0).getRoNumber());
					response.setStatus(list.get(0).getStatus());
					response.setCode("500");
					response.setMessage(
							"Job Card (" + list.get(0).getRoNumber() + ") already open for the chassis number !!!");
				} else {
					flag = true;
				}

			}
			if (list==null || flag) {
				JobChassisDetailsResponse = jobCardDao.fetchChassisDetailsByVinId(authorizationHeader, userCode, vinId);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (flag) {
				JobChassisDetailsResponse = jobCardDao.fetchChassisDetailsByVinId(authorizationHeader, userCode, vinId);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
			}
		}
		return response;
	}

	@Override
	public List<JobCardImplementTypeResponse> getJobCardImplementTypeList(String authorizationHeader, String userCode) {

		return jobCardDao.fetchJobCardImplementTypeList(authorizationHeader, userCode);
	}

	@Override
	public List<JobCardApplicationUsedResponse> getJobCardApplicationUsedList(String authorizationHeader,
			String userCode) {
		return jobCardDao.fetchJobCardApplicationUsedList(authorizationHeader, userCode);
	}

	@Override
	public List<JobRepresentativeResponse> getRepresentativeList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchJobCardRepresentativeList(authorizationHeader, userCode);
	}

	@Override
	public List<JobTechnicianListResponse> getTechnicianList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchTechnicianList(authorizationHeader, userCode);
	}

	@Override
	public JobCardCreateResponse getJobCardCreation(String authorizationHeader, String userCode,
			jobCardCreateRequest jobcardRequest) {
		return jobCardDao.createJobCard(authorizationHeader, userCode, jobcardRequest);
	}

	@Override
	public List<JobServiceActivityTypeResponse> getServiceActvityTypeList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchServiceActvityTypeList(authorizationHeader, userCode);
	}

	@Override
	public List<jobBillableTypeResponse> getBillableTypeList(String authorizationHeader, String userCode,
			int categoryId) {
		return jobCardDao.fetchBillableTypeList(authorizationHeader, userCode, categoryId);
	}

	@Override
	public List<jobInventryChecklistReponse> getinventryCheckList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchinventryCheckList(authorizationHeader, userCode);
	}

	@Override
	public List<jobpainscratchedResponse> getPaintScracthedList(String authorizationHeader, String userCode) {
		return jobCardDao.fetchPaintScracthedList(authorizationHeader, userCode);
	}

	@Override
	public List<JobLabourMasterResponse> getLobourDetails(String authorizationHeader, String userCode) {
		return jobCardDao.fetchLobourDetailsList(authorizationHeader, userCode);
	}

	@Override
	public List<JobLabourMasterResponse> getOutSiderLobourDetails(String authorizationHeader, String userCode) {
		return jobCardDao.fetchoutSiderLobourDetailsList(authorizationHeader, userCode);
	}

	@Override
	public JobCardCreateResponse saveJobCardData(String authorizationHeader, String userCode,
			JobCardFinalRequest jobCardData) {

		JobCardCreateResponse jobCardResponse = jobCardDao.saveJobCardData(authorizationHeader, userCode,
				jobCardData.getJobCardCreateRequest());
		try {
			if (jobCardResponse.getRoId() != null) {

				if (jobCardData.getCustomberVoiceRequest() != null
						|| !jobCardData.getCustomberVoiceRequest().isEmpty()) {
					for (CustomberVoiceRequest custVoice : jobCardData.getCustomberVoiceRequest()) {
						JobCardCreateResponse custResponse = jobCardDao.saveCustVoiceData(authorizationHeader, userCode,
								custVoice, jobCardResponse.getRoId());
					}

				}

				if (jobCardData.getTyreDetailsRequest() != null) {
					JobCardCreateResponse tyreResponse = jobCardDao.saveTyreDetails(authorizationHeader, userCode,
							jobCardData.getTyreDetailsRequest(), jobCardResponse.getRoId());
				}

				if (jobCardData.getLabourChargeRequest() != null || !jobCardData.getLabourChargeRequest().isEmpty()) {
					for (LabourChargeRequest labourCharge : jobCardData.getLabourChargeRequest()) {
						if(labourCharge.getRate().compareTo(BigDecimal.ZERO) > 0) {
						LabourGstCalcDtl bean = jobCardDao.getLabourGstDetail(jobCardData.getJobCardCreateRequest().getBranchId() ,jobCardData.getJobCardCreateRequest().getCoustemberId() ,labourCharge.getLabourCodeId(),labourCharge.getRate(), labourCharge.getHour());
						if(bean!=null) {
						labourCharge.setCgst(bean.getCgst());
						labourCharge.setIgst(bean.getIgst());
						labourCharge.setSgst(bean.getSgst());
						labourCharge.setCgstAmt(bean.getCgstAmt());
						labourCharge.setIgstAmt(bean.getIgstAmt());
						labourCharge.setSgstAmt(bean.getSgstAmt());
						labourCharge.setTotalGst(bean.getTotalGst());
						labourCharge.setTotalAmt(bean.getTotalAmt());
						}
						JobCardCreateResponse labourResponse = jobCardDao.saveLabourChargeData(authorizationHeader,
								userCode, labourCharge, jobCardResponse.getRoId());
						}
						
					}
				}
				if (jobCardData.getOutSiderLabourChargeRequest() != null
						|| !jobCardData.getOutSiderLabourChargeRequest().isEmpty()) {
					for (OutSiderLabourChargeRequest outerLabourCharge : jobCardData.getOutSiderLabourChargeRequest())  {
						if(outerLabourCharge.getRate().compareTo(BigDecimal.ZERO) > 0) {
							
							LabourGstCalcDtl bean = jobCardDao.getOuterLabourGstDetail(jobCardData.getJobCardCreateRequest().getBranchId() ,jobCardData.getJobCardCreateRequest().getCoustemberId() ,outerLabourCharge.getLabourCodeId() ,outerLabourCharge.getRate(), outerLabourCharge.getHour());
							
							if(bean!=null) {
								outerLabourCharge.setCgst(bean.getCgst());
								outerLabourCharge.setIgst(bean.getIgst());
								outerLabourCharge.setSgst(bean.getSgst());
								outerLabourCharge.setCgstAmt(bean.getCgstAmt());
								outerLabourCharge.setIgstAmt(bean.getIgstAmt());
								outerLabourCharge.setSgstAmt(bean.getSgstAmt());
								outerLabourCharge.setTotalGst(bean.getTotalGst());
								outerLabourCharge.setTotalAmt(bean.getTotalAmt());
							}	
							
						JobCardCreateResponse OuterlabourResponse = jobCardDao.saveOuterLabourChargeData(
								authorizationHeader, userCode, outerLabourCharge, jobCardResponse.getRoId());
					}
						
					}
				}

				if (jobCardData.getSelectedPaintScratched() != null
						&& !jobCardData.getSelectedPaintScratched().isEmpty()
						&& jobCardData.getPaintRequest() != null) {
					for (Integer paintRequest : jobCardData.getSelectedPaintScratched()) {
						JobCardCreateResponse paintResponse = jobCardDao.savePaintScrachedData(authorizationHeader,
								userCode, jobCardData.getPaintRequest(), paintRequest, jobCardResponse.getRoId());
					}
				} else {
					JobCardCreateResponse paintResponse = jobCardDao.savePaintScrachedData(authorizationHeader,
							userCode, jobCardData.getPaintRequest(), 0, jobCardResponse.getRoId());
				}
				if (jobCardData.getSelectedInventoryCodes() != null
						|| !jobCardData.getSelectedInventoryCodes().isEmpty()) {
					for (Integer InventoryRequestId : jobCardData.getSelectedInventoryCodes()) {
						JobCardCreateResponse inventoryResponse = jobCardDao.saveInventoryCodes(authorizationHeader,
								userCode, InventoryRequestId, jobCardResponse.getRoId());
					}
				}

				if (jobCardData.getPdiChecklistRequest() != null) {
					int i = 0;
					for (PdiChecklistRequest pdiChecklistRequest : jobCardData.getPdiChecklistRequest()) {
						if (pdiChecklistRequest != null) {
							JobCardCreateResponse pdicheckRespose = jobCardDao.savePdiCheckList(authorizationHeader,
									userCode, pdiChecklistRequest, jobCardResponse.getRoId());
							if (pdiChecklistRequest.getDefaultTick().equalsIgnoreCase("true")) {
								i = 1;

							} else {
								i = 0;
							}
//							if (jobCardData.getJobCardCreateRequest().getJobcardCatgId() == 2) {
//								jobCardDao.updateFlagInPdiTableByDtlId(pdiChecklistRequest.getPdiDtlId(), i,
//										jobCardData.getJobCardCreateRequest().getJobcardCatgId());
//							} else if (jobCardData.getJobCardCreateRequest().getJobcardCatgId() == 8) {
//								jobCardDao.updateFlagInPdiTableByDtlId(pdiChecklistRequest.getPdiDtlId(), i,
//										jobCardData.getJobCardCreateRequest().getJobcardCatgId());
//							}
							if (jobCardData.getJobCardCreateRequest().getJobcardCatgId() == 2
									|| jobCardData.getJobCardCreateRequest().getJobcardCatgId() == 8) {
								// jobCardDao.updateFlagInPdiTableByDtlId(pdiChecklistRequest.getPdiDtlId(), i,
								// jobCardData.getJobCardCreateRequest().getJobcardCatgId());
							}

						}
					}
				}
				if (jobCardData.getInspectionCheckPointRequest() != null) {
					for (InspectionCheckPointRequest inspectionChecklistRequest : jobCardData
							.getInspectionCheckPointRequest()) {
						JobCardCreateResponse inventoryResponse = jobCardDao.saveInspectionCheckList(
								authorizationHeader, userCode, inspectionChecklistRequest, jobCardResponse.getRoId());
					}
				}
				if (jobCardData.getServiceActivityRequest() != null) {

					JobCardCreateResponse activityResponse = jobCardDao.saveActivityDetails(authorizationHeader,
							userCode, jobCardData.getServiceActivityRequest(), jobCardResponse.getRoId());

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobCardResponse;
	}

	@Override
	public MessageCodeResponse uploadDocuments(String authorizationHeader, String userCode, Integer roId,
			MultipartFile chassisNoPic, MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2) {

		return jobCardDao.saveDocuments(userCode, roId, chassisNoPic, hourMeterPic, pic1, pic2);
	}

	@Override
	public List<JobCardSearchResponse> getsearchJobCardDetails(String authorizationHeader, String userCode,
			JobCardSearchRequest jobCardSearchRequest) {
		return jobCardDao.getsearchJobCardDetails(authorizationHeader, userCode, jobCardSearchRequest);
	}

	@Override
	public JobCardSearchTypeWiseResponse getsearchJobCardaTypeWise(String authorizationHeader, String userCode,
			String type, String chassisNo, String engineNumber, String vinNumber, String mobileNumber,
			String customerName, String bookingNumber, String quotatioNumber, String invoiceNumber) {
		JobCardSearchTypeWiseResponse jobCardSearchTypeWiseRequest = new JobCardSearchTypeWiseResponse();
		HashMap<String, Object> request = null;
		if (type.equalsIgnoreCase("chassisNo")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, chassisNo);
		} else if (type.equalsIgnoreCase("engineNumber")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, engineNumber);
		} else if (type.equalsIgnoreCase("vinNumber")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, vinNumber);
		} else if (type.equalsIgnoreCase("mobileNumber")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, mobileNumber);
		} else if (type.equalsIgnoreCase("customerName")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, customerName);
		} else if (type.equalsIgnoreCase("bookingNumber")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, bookingNumber);
		} else if (type.equalsIgnoreCase("quotatioNumber")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, quotatioNumber);
		} else if (type.equalsIgnoreCase("invoiceNumber")) {
			request = jobCardDao.fetchJobCardaTypeWise(authorizationHeader, userCode, type, invoiceNumber);
		}
		jobCardSearchTypeWiseRequest.setObject(request);
		return jobCardSearchTypeWiseRequest;
	}

	@Override
	public JobCardDetailsResponse getJobCardDetailsByRoId(String authorizationHeader, String userCode, Integer roId) {

		JobCardDetailsResponse response = new JobCardDetailsResponse();

		for (int i = 1; i < 15; i++) {

			if (i == 1) {
				JobCardDataResponse jobCardresponse = jobCardDao.fetchJobCardDetails(authorizationHeader, userCode,
						roId, i);
				response.setJobCardSearchResponse(jobCardresponse);
			}
			if (i == 2) {
				TyreDetailsResponse tyreResponse = jobCardDao.fetchTyreDetails(authorizationHeader, userCode, roId, i);
				response.setTyreDetailsResponse(tyreResponse);
			}
			if (i == 3) {
				List<CustomerServiceResponse> custResponse = jobCardDao.fetchCustDetails(authorizationHeader, userCode,
						roId, i);
				response.setCustomberVoiceResponse(custResponse);
			}
			if (i == 4) {
				List<LabourChargeResposne> labourChargeResponse = jobCardDao
						.fetchLabourChargeDetails(authorizationHeader, userCode, roId, i);
				response.setLabourChargeResposne(labourChargeResponse);
			}

			if (i == 5) {
				List<OutSideLabourResponse> outSideLabourChargeResponse = jobCardDao
						.fetchOutSideLabourChargeDetails(authorizationHeader, userCode, roId, i);
				response.setOutSideLabourResponse(outSideLabourChargeResponse);
			}

			if (i == 6) {
				List<InventoryResponse> inventoryResponse = jobCardDao.fetchInventoryDetails(authorizationHeader,
						userCode, roId, i);
				response.setInventoryResponse(inventoryResponse);
			}
			if (i == 7) {
				List<PaintScratchedResponse> paintResponse = jobCardDao.fetchPaintDetails(authorizationHeader, userCode,
						roId, i);
				response.setPaintScratchedResponse(paintResponse);
			}
			if (i == 8) {
				List<PartsResponse> partResponse = jobCardDao.fetchPartDetails(authorizationHeader, userCode, roId, i);
				response.setPartsResponse(partResponse);
			}
			if (i == 9) {
				List<DocumentFilesResponse> docsResponse = jobCardDao.fetchDocsFiles(authorizationHeader, userCode,
						roId, i);
				response.setDocsResponse(docsResponse);
			}
			if (i == 10) {
				List<ActivityPlanModelResponse> activityResponse = jobCardDao.fetchActivityList(authorizationHeader,
						userCode, roId, i);
				response.setActivityResponse(activityResponse);

			}
			if (i == 11) {
				List<JobCardServiceHistoryModelResponse> serviceHistoryResponse = jobCardDao
						.fetchServiceHistoryList(authorizationHeader, userCode, roId, i);
				response.setServiceHistoryResponse(serviceHistoryResponse);

			}

			if (i == 12) {
				List<InstallationChecklistResponse> installationCheckListResponse = jobCardDao
						.getAllCheckpointOfInstallationByRoId(authorizationHeader, userCode, roId, i);
				response.setInstallationChecklistResponse(installationCheckListResponse);

			}

			if (i == 13 && response.getJobCardSearchResponse().getCategoryId() == 2) {
				List<PdiCheckListResponse> pdiCheckListResponse = jobCardDao
						.getAllCheckpointOfPDIByRoId(authorizationHeader, userCode, roId, i);
				response.setPdiCheckListResponse(pdiCheckListResponse);
			}
			if (i == 14 && response.getJobCardSearchResponse().getCategoryId() == 8) {
				List<PdiCheckListResponse> outWordpdiCheckListResponse = jobCardDao
						.getAllCheckpointOfPDIByRoId(authorizationHeader, userCode, roId, i);
				response.setPdiCheckListResponse(outWordpdiCheckListResponse);
			}
		}

		return response;
	}

	@Override
	public JobCardCreateResponse closeJobCard(String authorizationHeader, String userCode,
			JobCardCloseRequest requestModel, Device device) {
		JobCardCreateResponse response = null;

		response = jobCardDao.closeJobCard(authorizationHeader, userCode, requestModel, device);
		if (response.getStatusCode() == 201 && requestModel.getRoId() != null) {
			jobCardDao.updateSrvBookingAndQuatationStatus(requestModel.getRoId());
		}

		return response;
	}

	@Override
	public JobCardCreateResponse uploadPdiFiles(String userCode, PdiDataUpdateRequest requestModel,
			MultipartFile files) {

		return jobCardDao.upkoadPdiFiles(userCode, requestModel, files);
	}

	@Override
	public JobCardCreateResponse uploadInstallationFiles(String userCode, InstallationUpdateRequest requestModel,
			MultipartFile files) {
		return jobCardDao.uploadInstallationFiles(userCode, requestModel, files);
	}

	@Override
	public JobCardCreateResponse cancelJobCard(String authorizationHeader, String userCode,
			JobCardCancelRequest requestModel, Device device) {
		return jobCardDao.cancelJobCard(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public JobCardCreateResponse editJobCard(String authorizationHeader, String userCode, EditFinalModel requestModel,
			Device device) {

		JobCardCreateResponse response = new JobCardCreateResponse();
		if (requestModel.getJobCardId() > 0 && requestModel.getParts().get(0).getRequisitionId() > 0) {

			for (EditPartsRequest partRequest : requestModel.getParts()) {
				response = jobCardDao.saveEditPartDetails(authorizationHeader, userCode, partRequest, device);
				if (response.getStatusCode() != 200) {
					return response;
				}

			}
		}
		if (requestModel.getJobCardId() > 0 && requestModel.getCustomberVoiceRequest().get(0).getCustomerId() > 0) {

			for (EditCustomberVoiceRequest custRequest : requestModel.getCustomberVoiceRequest()) {
				response = jobCardDao.saveEditCustomerDetails(authorizationHeader, userCode, custRequest, device);
				if (response.getStatusCode() != 200) {
					return response;
				}
			}

		}
		if (requestModel.getJobCardId() > 0
				&& requestModel.getOutSiderLabourChargeRequest().get(0).getLabourCodeId() > 0) {
			response = jobCardDao.saveEditOutSideLabourDetails(authorizationHeader, userCode,requestModel.getBranchId(), requestModel.getCustomerId(),
					requestModel.getOutSiderLabourChargeRequest(), requestModel.getJobCardId(), device);
			if (response.getStatusCode() != 200) {
				return response;
			}
		}
		if (requestModel.getJobCardId() > 0 && requestModel.getLabourChargeRequest().get(0).getLabourCodeId() > 0) {
			
				response = jobCardDao.saveEditLabourDetails(authorizationHeader, userCode, requestModel.getBranchId(), requestModel.getCustomerId(),
						requestModel.getLabourChargeRequest(), requestModel.getJobCardId(), device);
				
			
			
			if (response.getStatusCode() != 200) {
				return response;
			}
		}

		if (requestModel.getPdiChecklistRequest() != null) {
			int i = 0;
			for (PdiChecklistRequest pdiChecklistRequest : requestModel.getPdiChecklistRequest()) {
				if (pdiChecklistRequest != null) {

					if (pdiChecklistRequest.getDefaultTick().equalsIgnoreCase("true")) {
						i = 1;

					} else {
						i = 0;
					}
					if (requestModel.getCategoryId() == 2 || requestModel.getCategoryId() == 8) {
						jobCardDao.updateFlagInPdiTableByDtlId(pdiChecklistRequest.getPdiDtlId(), i,
								requestModel.getCategoryId());
					}

				}
			}
		}

		if (requestModel.getInspectionCheckPointRequest() != null
				&& !requestModel.getInspectionCheckPointRequest().isEmpty()) {
			if (requestModel.getCategoryId() == 9) {
				for (installationCheckPointUpdateRequest request : requestModel.getInspectionCheckPointRequest()) {
					if (request != null) {
						jobCardDao.updateFlagInInstallationDtlById(request.getInstallDtlId(), request.getIsActive(),
								requestModel.getCategoryId());
					}
				}
			}
		}
		response.setStatusCode(WebConstants.STATUS_OK_200);
		response.setMsg("Job Card Updated Successfully.");
		return response;
	}

	@Override
	public MessageCodeResponse editUploadImage(String authorizationHeader, String userCode, Integer roId,
			MultipartFile chassisNoPic, MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2) {
		return jobCardDao.updateDocuments(userCode, roId, chassisNoPic, hourMeterPic, pic1, pic2);
	}

	@Override
	public MessageCodeResponse updateServieAndQuoationStatus(String authorizationHeader, String userCode,
			ServiceBookingAndQuoationStatusRequestModel requestModel) {

		return jobCardDao.updateBookingAndQuotationStatus(authorizationHeader, userCode, requestModel);
	}

	@Override
	public ChassisDetailsWithFlagResponse getSearchChassisDetailsByChassis(String authorizationHeader, String userCode,
			String chassis, Integer categoryId) {
		ChassisDetailsWithFlagResponse response = new ChassisDetailsWithFlagResponse();
		List<JobChassisDetailsResponse> JobChassisDetailsResponse = null;
		boolean flag = true;
		try {
			List<JobCardAndStausResponse> list = jobCardDao.getJobCardAndStausListByChassis(userCode, chassis);
			System.out.println("list::::::::::" + list);
			if (list != null && !list.isEmpty()) {
//				if (list.stream().anyMatch(s -> (s.getStatus().equals("Closed") || s.getStatus().equals("Cancelled")) && !s.getStatus().equals("Open"))) {
				if (list.stream().anyMatch(s -> (s.getStatus().equalsIgnoreCase("Open")))) {

					flag = false;
					response.setJobCardNumber(list.get(0).getRoNumber());
					response.setStatus(list.get(0).getStatus());
					response.setCode("500");
					response.setMessage(
							"Job Card (" + list.get(0).getRoNumber() + ") already open for the chassis number !!!");

				} else {

					flag = true;
//					flag = false;
//					response.setJobCardNumber(list.get(0).getRoNumber());
//					response.setStatus(list.get(0).getStatus());
//					response.setCode("500");
//					response.setMessage(
//							"Job Card (" + list.get(0).getRoNumber() + ") already open for the chassis number !!!");
//			
				}
			}
			if ((list != null && list.isEmpty()) || (flag && categoryId == 2)) {
				JobChassisDetailsResponse = jobCardDao.fetchChassisDetailsByChassis(authorizationHeader, userCode,
						chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);

				List<PdiCheckListResponse> pdiCheckListResponse = jobCardDao.getAllCheckpointOfPDI(userCode, chassis);
				response.setPdiCheckListResponse(pdiCheckListResponse);
			} else if ((list != null && list.isEmpty()) || (flag && categoryId == 8)) {
				JobChassisDetailsResponse = jobCardDao.fetchOutWordChassisDetailsByChassis(authorizationHeader,
						userCode, chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
				List<PdiCheckListResponse> pdiCheckListResponse = jobCardDao.getAllCheckpointOfOutWord(userCode,
						chassis);
				response.setPdiCheckListResponse(pdiCheckListResponse);
			} else if ((list != null && list.isEmpty()) || (flag && categoryId == 11)) {
				JobChassisDetailsResponse = jobCardDao.fetchOutWordChassisDetailsByChassisSTK(authorizationHeader,
						userCode, chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
			} else if ((list != null && list.isEmpty()) ||( flag && categoryId == 9)) {

				JobChassisDetailsResponse = jobCardDao.fetchInstallationChassisDetailsByChassis(authorizationHeader,
						userCode, chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
				List<InstallationChecklistResponse> installationCheckListResponse = jobCardDao
						.getAllCheckpointOfInstallation(userCode, chassis);
				response.setInstallationChecklistResponse(installationCheckListResponse);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (categoryId == 2) {
				JobChassisDetailsResponse = jobCardDao.fetchChassisDetailsByChassis(authorizationHeader, userCode,
						chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);

				List<PdiCheckListResponse> pdiCheckListResponse = jobCardDao.getAllCheckpointOfPDI(userCode, chassis);
				response.setPdiCheckListResponse(pdiCheckListResponse);
			} else if (categoryId == 8) {
				JobChassisDetailsResponse = jobCardDao.fetchOutWordChassisDetailsByChassis(authorizationHeader,
						userCode, chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
				List<PdiCheckListResponse> pdiCheckListResponse = jobCardDao.getAllCheckpointOfOutWord(userCode,
						chassis);
				response.setPdiCheckListResponse(pdiCheckListResponse);

			} else if (categoryId == 9) {

				JobChassisDetailsResponse = jobCardDao.fetchInstallationChassisDetailsByChassis(authorizationHeader,
						userCode, chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
				List<InstallationChecklistResponse> installationCheckListResponse = jobCardDao
						.getAllCheckpointOfInstallation(userCode, chassis);
				response.setInstallationChecklistResponse(installationCheckListResponse);

			} else if (categoryId == 11) {
				JobChassisDetailsResponse = jobCardDao.fetchOutWordChassisDetailsByChassisSTK(authorizationHeader,
						userCode, chassis);
				response.setJobChassisDetailsResponse(JobChassisDetailsResponse);
			}
		}

		return response;
	}

	@Override
	public List<AutoSelectSrvCategoryAndTypeResponse> getAutoSelectSrvCategoryAndType(String authorizationHeader,
			String userCode, Integer vin_Id, Integer kMReading) {

		return jobCardDao.getAutoSelectSrvCategoryAndTypeResponse(userCode, vin_Id, kMReading);
	}

	@Override
	public HashMap<String, Object> getActivityDetails(String authorizationHeader, String userCode,String profitcenter) {

		return jobCardDao.fetchActivityPlanDetails(userCode,profitcenter);
	}

	@Override
	public MasterDataModelResponse getChassisDetails(String userCode, String chassisNo, String flag) {

		return jobCardDao.getChassisDetailsForVehicle(userCode, chassisNo, flag);
	}

	@Override
	public JasperPrint PdfGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String filePath) {
		// String filePath = request.getServletContext().getRealPath("/reports/" +
		// jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			// String filePath =
			// ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();

			String filePaths = filePath + jaspername;
			System.out.println("filePath  " + filePaths);
			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePaths, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception {

		if (format != null && format.equalsIgnoreCase("pdf")) {
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			if (printStatus != null && printStatus.equals("true")) {
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
				configuration.setPdfJavaScript("this.print();");
			}
			exporter.setConfiguration(configuration);
			exporter.exportReport();

		} else if (format != null && format.equalsIgnoreCase("xls")) {

			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

			exporter.exportReport();
		}
	}

	@Override
	public Map<String, Object> getServiceHistoryByChassis(String userCode, Device device, String chassisNumber) {

		return jobCardDao.fetchServiceActivityByChassis(userCode, chassisNumber);
	}

	@Override
	public JasperPrint PdfGeneratorReportForJobCardOpen(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			// String filePath =
			// ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();

			String filePathsVariable = filePath + jasperName;
			System.out.println("filePath  " + filePathsVariable);
			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePathsVariable, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	@Override
	public Map<String, Object> getQuotationSearchList(String userCode, Device device, String quotationText,
			int categoryId) {
		return jobCardDao.fetchQuotationSearchList(userCode, quotationText, categoryId);
	}

	@Override
	public Boolean getValidatePcrButton(String userCode, Device device, String jobCardId) {
		Map<String, Object> response = jobCardDao.getValidatePcrButton(userCode, jobCardId);
		Boolean flag = false;
		Collection<Object> values = response.values();
		// Convert the collection to a List
		List<Object> valueList = new ArrayList<>(values);

		if (valueList != null && !valueList.isEmpty()) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public JasperPrint jobcasexlsxGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String downloadPaths) {

//		String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			String filePath = downloadPaths + jaspername;
			System.out.println("filePath  " + filePath);
			connection = dataSourceConnection.getConnection();
			System.out.println("jasperParameter " + jasperParameter);

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePath, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	@Override
	public void printReportExcel(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream)
			throws JRException {
		JRXlsxExporter exporter = new JRXlsxExporter();
		SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
		reportConfigXLS.setSheetNames(new String[] { "sheet1" });
		exporter.setConfiguration(reportConfigXLS);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		exporter.exportReport();
	}

	@Override
	public Map<String, Object> getJobCardStatusList(String userCode) {
		return jobCardDao.fetchJobCardStatusList(userCode);
	}
	
	public Resource loadFileAsResource(String fileName, String docPath, Long id,String downloadPath) throws MalformedURLException {
		
			
				if(id != null) {
					downloadPath = downloadPath  + id ;
				}
			
			Path path = Paths.get(downloadPath).toAbsolutePath().normalize();
//			System.out.println(this.fileStorageLocation);
			Path filePath = path.resolve(fileName).normalize();
			Resource resource = null;
			try {
				resource = new UrlResource(filePath.toUri());
				if (resource.exists()) {
					return resource;
				}else {
					throw new RecordNotFoundException("File not found " + fileName);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new MalformedURLException("File not found " + fileName);
			}
			//return resource;
			
		
	}

	@Override
	public Integer checkLubricant(String userCode, Device device, BigInteger roId) {
		return jobCardDao.checkLubricant(roId);
	}	

}
