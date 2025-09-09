/**
 * 
 */
package com.hitech.dms.web.dao.jobcard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.utils.FileUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.jobcard.CustomberVoiceEntity;
import com.hitech.dms.web.entity.jobcard.InspectionCheckListEntity;
import com.hitech.dms.web.entity.jobcard.JobCardDataEntity;
import com.hitech.dms.web.entity.jobcard.LabourChargeEntity;
import com.hitech.dms.web.entity.jobcard.OutSiderLabourChargeEntity;
import com.hitech.dms.web.entity.jobcard.PaintEntity;
import com.hitech.dms.web.entity.jobcard.PartDetailsEntity;
import com.hitech.dms.web.entity.jobcard.PdiCheckListEntity;
import com.hitech.dms.web.entity.jobcard.jobCardDetailsEntity;
import com.hitech.dms.web.entity.jobcard.jobCardEntity;
import com.hitech.dms.web.entity.jobcard.jobInventoryCheckListEntity;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
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

/**
 * @author santosh.kumar
 *
 */
@Repository
public class JobCardDaoImpl implements JobCardDao {
	private static final Logger logger = LoggerFactory.getLogger(JobCardDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;
	private FileUtils utils = new FileUtils();
	// private final String uploadDir =
	// "C:\\THRSL\\VST-DMS-MICROAPP\\projects\\dms-service-customer\\src\\assets\\images";
	private final String uploadDir = "C:\\VST-DMS-APPS\\FILES\\dms-service-customer\\images\\";
	private final String uploadDirForProd = "/var/VST-DMS-APPS/FILES/dms-service-customer/images/";
	
	

	@Override
	public List<JobCardCategoryResponse> fetchJobCardCategory(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardCategory invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobCardCategoryResponse responseListModel = null;
		List<JobCardCategoryResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SV_JOB_CATEGORY]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardCategoryResponse();
				responseModelList = new ArrayList<JobCardCategoryResponse>();
				JobCardCategoryResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardCategoryResponse();
					responseModel.setServiceCategoryId((Integer) row.get("Service_Category_ID"));
					responseModel.setCategoryCode((String) row.get("CategoryCode"));
					responseModel.setCategoryDesc((String) row.get("CategoryDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobStatusResponse> fetchJobCardSource(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardStatus invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobStatusResponse responseListModel = null;
		List<JobStatusResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from [SYS_LOOKUP]  where [LookupTypeCode]= 'JOB_SOURCE' and ISACTIVE = 'Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobStatusResponse();
				responseModelList = new ArrayList<JobStatusResponse>();
				JobStatusResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobStatusResponse();
					responseModel.setLookupId((BigInteger) row.get("lookup_id"));
					responseModel.setLookupTypeCode((String) row.get("LookupTypeCode"));
					responseModel.setLookupVal((String) row.get("LookupVal"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobStatusResponse> fetchJobCardPlaceOfService(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardPlaceOfService invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobStatusResponse responseListModel = null;
		List<JobStatusResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from [SYS_LOOKUP]  where [LookupTypeCode]= 'JOB_PlACE_SERVICE' and ISACTIVE = 'Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobStatusResponse();
				responseModelList = new ArrayList<JobStatusResponse>();
				JobStatusResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobStatusResponse();
					responseModel.setLookupId((BigInteger) row.get("lookup_id"));
					responseModel.setLookupTypeCode((String) row.get("LookupTypeCode"));
					responseModel.setLookupVal((String) row.get("LookupVal"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobServiceTypeResponse> fetchJobServiceTypeList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobServiceTypeList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobServiceTypeResponse responseListModel = null;
		List<JobServiceTypeResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select Service_Type_oem_ID,Service_Category_ID,SrvTypeCode,SrvTypeDesc from [SV_OEM_SRV_TYPE] where IsActive='Y'";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobServiceTypeResponse();
				responseModelList = new ArrayList<JobServiceTypeResponse>();
				JobServiceTypeResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobServiceTypeResponse();
					responseModel.setServiceTypeId((Integer) row.get("Service_Type_oem_ID"));
					responseModel.setServiceCategoryId((Integer) row.get("Service_Category_ID"));
					responseModel.setServiceTypeCode((String) row.get("SrvTypeCode"));
					responseModel.setServiceTypeDesc((String) row.get("SrvTypeDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobRepairCatgResponse> fetchJobRepairTypeList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobRepairCatgList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobRepairCatgResponse responseListModel = null;
		List<JobRepairCatgResponse> responseModelList = null;
		Integer recordCount = 0;
		// where IsActive='Y'
		String sqlQuery = "select repair_catg_id,RepairCatgDesc from SV_REPAIR_CATG where IsActive='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobRepairCatgResponse();
				responseModelList = new ArrayList<JobRepairCatgResponse>();
				JobRepairCatgResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobRepairCatgResponse();
					responseModel.setRepairCatgId((Integer) row.get("repair_catg_id"));
					responseModel.setRepairCatgDesc((String) row.get("RepairCatgDesc"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobServiceBookingResponse> fetchServiceBookingList(String authorizationHeader, String userCode,
			String bookingNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchServiceBookingList invoked.." + bookingNo);
		}
		Query query = null;
		Session session = null;
		JobServiceBookingResponse responseListModel = null;
		List<JobServiceBookingResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec SP_SV_SERVICE_BOOKING_SEARCH :userCode ,:searchText";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", bookingNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobServiceBookingResponse();
				responseModelList = new ArrayList<JobServiceBookingResponse>();
				JobServiceBookingResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobServiceBookingResponse();
					responseModel.setId((BigInteger) row.get("id"));
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setBookingno((String) row.get("bookingno"));
					responseModel.setBookingDate((Date) row.get("booking_date"));
					responseModel.setStatus((String) row.get("status"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<InvoiceSearchResponse> fetchInvoiceList(String authorizationHeader, String userCode,
			String invoiceTxt) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceList invoked.." + invoiceTxt);
		}
		Query query = null;
		Session session = null;
		InvoiceSearchResponse responseListModel = null;
		List<InvoiceSearchResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SV_SERVICE_SEARCHINVOICE] :userCode ,:invoiceTxt";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("invoiceTxt", invoiceTxt);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new InvoiceSearchResponse();
				responseModelList = new ArrayList<InvoiceSearchResponse>();
				InvoiceSearchResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InvoiceSearchResponse();
					responseModel.setInvoiceHdrId((BigInteger) row.get("erp_invoice_hdr_id"));
					responseModel.setInvoiceNumber((String) row.get("invoice_number"));
					responseModel.setInvoiceDate((Date) row.get("invoice_date"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobChassisResponse> fetchChassisOrMobileList(String authorizationHeader, String userCode,
			SearchChassisRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchChassisOrMobileList invoked.." + requestModel);
		}
		Query query = null;
		Session session = null;
		JobChassisResponse responseListModel = null;
		List<JobChassisResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [sp_sv_service_search_chassisno] :UserCode, :ChassisText,:CatergoryId,:ServicebookingNumber,:InvoiceNumber,:QuotationNumber";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("ChassisText", requestModel.getChassisNumber());
			query.setParameter("CatergoryId", requestModel.getCategoryId());
			query.setParameter("ServicebookingNumber", requestModel.getServiceBooking());
			query.setParameter("InvoiceNumber", requestModel.getInvoiceNumber());
			query.setParameter("QuotationNumber", requestModel.getQuotationNumber());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobChassisResponse();
				responseModelList = new ArrayList<JobChassisResponse>();
				JobChassisResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobChassisResponse();
					responseModel.setVinId((BigInteger) row.get("vin_id"));
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setFirstname((String) row.get("FirstName"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobChassisDetailsResponse> fetchChassisDetailsByVinId(String authorizationHeader, String userCode,
			BigInteger vinId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchChassisDetailsByVinId invoked.." + vinId);
		}
		Query query = null;
		Session session = null;
		JobChassisDetailsResponse responseListModel = null;
		List<JobChassisDetailsResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SV_SERVICE_GETOLDCHASSIS_DETAILS] :userCode, :vinId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("vinId", vinId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobChassisDetailsResponse();
				responseModelList = new ArrayList<JobChassisDetailsResponse>();
				JobChassisDetailsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobChassisDetailsResponse();
					responseModel.setVinId((BigInteger) row.get("vinId"));
					responseModel.setProfitcenter((String) row.get("profitcenter"));
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setInvoiceId((BigInteger) row.get("invoiceId"));
					responseModel.setInvoiceNumber((String) row.get("invoice_number"));
					responseModel.setInvoiceDate((Date) row.get("invoice_date"));
					responseModel.setBookingId((BigInteger) row.get("bookingId"));
					responseModel.setServiceTypeId((BigInteger) row.get("service_type_id"));
					responseModel.setServiceRepairTypeId((BigInteger) row.get("service_repair_type_id"));
					responseModel.setCoustomerId((BigInteger) row.get("customerId"));
					responseModel.getServiceRepairTypeId();
					responseModel.setBookingNo((String) row.get("bookingno"));
					responseModel.setBookingDate((Date) row.get("booking_date"));
					responseModel.setPreviousHour((BigInteger) row.get("previous_hour"));
					responseModel.setLrDate((Date) row.get("LRDate"));
					responseModel.setLrNumber((String) row.get("LRNumber"));
					responseModel.setPdiDoneBy((String) row.get("pdiDoneBy"));
					responseModel.setQuotationId((Integer) row.get("Quotation_Id"));
					responseModel.setQuotationDate((Date) row.get("Quotation_Date"));
					responseModel.setQuotationNumber((String) row.get("QuotationNumber"));
					responseModel.setCustomerName((String) row.get("customername"));
					responseModel.setMobileNo((String) row.get("customermobile"));
					responseModel.setRepairOrderType((String) row.get("repairOrderType"));
					responseModel.setServiceType((String) row.get("serviceType"));
					responseModel.setInwardId((Integer) row.get("inwardId"));
					responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));
					responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
					responseModel.setFlipMakeNumber((String) row.get("FIPMakeNumber"));
					responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));
					responseModel.setFrontTyerMakeRHNumber((String) row.get("FrontTyerMakeRHNumber"));
					responseModel.setFrontTyerMakeLHNumber((String) row.get("FrontTyerMakeLHNumber"));
					responseModel.setRearTyerMakeRHNumber((String) row.get("RearTyerMakeRHNumber"));
					responseModel.setRearTypeMakeLHNumber((String) row.get("RearTyerMakeLHNumber"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobCardImplementTypeResponse> fetchJobCardImplementTypeList(String authorizationHeader,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("JobCardImplementTypeResponse invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobCardImplementTypeResponse responseListModel = null;
		List<JobCardImplementTypeResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from SV_JOBCARD_IMPLEMENT_TYPE where IsActive='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardImplementTypeResponse();
				responseModelList = new ArrayList<JobCardImplementTypeResponse>();
				JobCardImplementTypeResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardImplementTypeResponse();
					responseModel.setId((Integer) row.get("ID"));
					responseModel.setImplementType((String) row.get("ImplementType"));
					responseModel.setImplementCode((String) row.get("ImplementCode"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobCardApplicationUsedResponse> fetchJobCardApplicationUsedList(String authorizationHeader,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardApplicationUsedList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobCardApplicationUsedResponse responseListModel = null;
		List<JobCardApplicationUsedResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from [SV_JOBCARD_APPLICATION_USED] where isActive='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardApplicationUsedResponse();
				responseModelList = new ArrayList<JobCardApplicationUsedResponse>();
				JobCardApplicationUsedResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardApplicationUsedResponse();
					responseModel.setId((Integer) row.get("ID"));
					responseModel.setApplicationUsed((String) row.get("ApplicationUsed"));
					responseModel.setApplicationCode((String) row.get("ApplicationCode"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobRepresentativeResponse> fetchJobCardRepresentativeList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardRepresentativeList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobStatusResponse responseListModel = null;
		List<JobRepresentativeResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from [SYS_LOOKUP]  where [LookupTypeCode]= 'REPRESENTATIVE_TYPE' and ISACTIVE = 'Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobStatusResponse();
				responseModelList = new ArrayList<JobRepresentativeResponse>();
				JobRepresentativeResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobRepresentativeResponse();
					responseModel.setLookupId((BigInteger) row.get("lookup_id"));
					responseModel.setLookupTypeCode((String) row.get("LookupTypeCode"));
					responseModel.setLookupVal((String) row.get("LookupVal"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobTechnicianListResponse> fetchTechnicianList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchTechnicianList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobStatusResponse responseListModel = null;
		List<JobTechnicianListResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec SP_SV_SERVICE_TECHNICIAN_DTL :usercode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
		    query.setParameter("usercode", userCode);
			// query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobStatusResponse();
				responseModelList = new ArrayList<JobTechnicianListResponse>();
				JobTechnicianListResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobTechnicianListResponse();
					responseModel.setEmpId((BigInteger) row.get("emp_Id"));
					responseModel.setEmpCode((String) row.get("EmpCode"));
					responseModel.setFirstName((String) row.get("FirstName"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					;
					responseModel.setDesignationDesc((String) row.get("DesignationDesc"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public JobCardCreateResponse createJobCard(String authorizationHeader, String userCode,
			jobCardCreateRequest jobcardRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("createJobCard invoked.." + userCode);
			logger.debug(jobcardRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		jobCardEntity jobHdrEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobHdrEntity = mapper.map(jobcardRequest, jobCardEntity.class, "jobCardCreate");
			// System.out.println("jobcardRequest:::::::::::::::::::::" + jobcardRequest);
			// System.out.println("jobHdrEntity:::::::::::::::::::::" + jobHdrEntity);
			// System.out.println("isSuccess::::::::::::" + isSuccess);
			logger.debug(jobHdrEntity.toString());
			if (jobHdrEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				BigInteger branchCode = null;
				BigInteger branchId = BigInteger.valueOf(jobHdrEntity.getBranchId());
				BranchDTLResponseModel branchDtl = commonDao.fetchBranchDtlByBranchId(authorizationHeader, branchId);
				branchId = branchDtl.getBranchId();
				BigInteger bi = BigInteger.valueOf(199);
				jobHdrEntity.setRoId(bi);
				String jobcardNumber = commonDao.getDocumentNumberById("JC", branchId, session);
				commonDao.updateDocumentNumber("JOB CARD OPEN", branchId, jobcardNumber, session);
				jobHdrEntity.setRoNumber(jobcardNumber);
				session.save(jobHdrEntity);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setVinId(jobHdrEntity.getVinId());
				responseModel.setJobCardNumber(jobHdrEntity.getRoNumber());
				responseModel.setMsg("Job Card Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public List<JobServiceActivityTypeResponse> fetchServiceActvityTypeList(String authorizationHeader,
			String userCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchServiceActvityTypeList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobStatusResponse responseListModel = null;
		List<JobServiceActivityTypeResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select *  from SYS_LOOKUP where LookupTypeCode= 'ACTIVITY_TYPE' and ISACTIVE='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobStatusResponse();
				responseModelList = new ArrayList<JobServiceActivityTypeResponse>();
				JobServiceActivityTypeResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobServiceActivityTypeResponse();
					responseModel.setLookupId((BigInteger) row.get("lookup_id"));
					responseModel.setLookupTypeCode((String) row.get("LookupTypeCode"));
					responseModel.setLookupVal((String) row.get("LookupVal"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;

	}

	@Override
	public List<jobBillableTypeResponse> fetchBillableTypeList(String authorizationHeader, String userCode,
			int categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchBillableTypeList invoked.." + userCode + "categoryId:-" + categoryId);
		}
		Query query = null;
		Session session = null;
		jobBillableTypeResponse responseListModel = null;
		List<jobBillableTypeResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select ID,BillableTypeCode,BillableTypeDesc from SV_BILLABLE_TYPE_MST where category_id = "
				+ categoryId + ";";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new jobBillableTypeResponse();
				responseModelList = new ArrayList<jobBillableTypeResponse>();
				jobBillableTypeResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new jobBillableTypeResponse();
					responseModel.setId((Integer) row.get("ID"));
					responseModel.setBillableTypeCode((String) row.get("BillableTypeCode"));
					responseModel.setBillableTypeDesc((String) row.get("BillableTypeDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;

	}

	@Override
	public List<jobInventryChecklistReponse> fetchinventryCheckList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchinventryCheckList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		jobInventryChecklistReponse responseListModel = null;
		List<jobInventryChecklistReponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select * from [SV_INVENTORY_CHECKLIST] where isActive='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new jobInventryChecklistReponse();
				responseModelList = new ArrayList<jobInventryChecklistReponse>();
				jobInventryChecklistReponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new jobInventryChecklistReponse();
					responseModel.setId((Integer) row.get("ID"));
					responseModel.setInventoryDesc((String) row.get("INVENTORYCODE"));
					responseModel.setInventoryCode((String) row.get("INVENTORYDESC"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<jobpainscratchedResponse> fetchPaintScracthedList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPaintScracthedList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		jobpainscratchedResponse responseListModel = null;
		List<jobpainscratchedResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "  select * from [SV_PAINT_SCRATCHED] where ISACTIVE='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new jobpainscratchedResponse();
				responseModelList = new ArrayList<jobpainscratchedResponse>();
				jobpainscratchedResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new jobpainscratchedResponse();
					responseModel.setId((Integer) row.get("ID"));
					responseModel.setPaintScratchedCode((String) row.get("PAINTSCRATCHEDCODE"));
					responseModel.setPaintScratchedDesc((String) row.get("PAINTSCRATCHEDDESC"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobLabourMasterResponse> fetchLobourDetailsList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchLobourDetailsList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobLabourMasterResponse responseListModel = null;
		List<JobLabourMasterResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select  Labour_Id , LabourCode, CONCAT(LabourCode,'-',LabourDesc) as LabourDesc , LabourCharge  from [SV_LABOUR_MST] where IsActive='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobLabourMasterResponse();
				responseModelList = new ArrayList<JobLabourMasterResponse>();
				JobLabourMasterResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobLabourMasterResponse();
					responseModel.setLabourId((BigInteger) row.get("Labour_Id"));
					responseModel.setLabourCode((String) row.get("LabourCode"));
					responseModel.setLabourDesc((String) row.get("LabourDesc"));
					responseModel.setLabourCharge((BigDecimal) row.get("LabourCharge"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<JobLabourMasterResponse> fetchoutSiderLobourDetailsList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchoutSiderLobourDetailsList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobLabourMasterResponse responseListModel = null;
		List<JobLabourMasterResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select Outside_Lbr_Id, LabourCode, CONCAT(LabourCode,'-',LabourDesc) as LabourDesc from [SV_Outside_LBR_MST] where IsActive='Y';";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobLabourMasterResponse();
				responseModelList = new ArrayList<JobLabourMasterResponse>();
				JobLabourMasterResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobLabourMasterResponse();
					responseModel.setLabourId(BigInteger.valueOf((Integer) row.get("Outside_Lbr_Id")));
					responseModel.setLabourCode((String) row.get("LabourCode"));
					responseModel.setLabourDesc((String) row.get("LabourDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public JobCardCreateResponse saveJobCardData(String authorizationHeader, String userCode,
			JobCardCreateRequest jobCardData) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveJobCardData invoked.." + userCode);
			logger.debug(jobCardData.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		JobCardDataEntity jobHdrEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		String jobcardNumber=null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobHdrEntity = mapper.map(jobCardData, JobCardDataEntity.class, "jobCardSave");
//			System.out.println("jobCardData:::::::::::::::::::::" + jobCardData);
//			System.out.println("jobHdrEntity:::::::::::::::::::::" + jobHdrEntity);
//			System.out.println("isSuccess::::::::::::" + isSuccess);
			logger.debug(jobHdrEntity.toString());
			if (jobHdrEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				BigInteger branchCode = null;
				BigInteger branchId = BigInteger.valueOf(jobHdrEntity.getBranchId());
				BranchDTLResponseModel branchDtl = commonDao.fetchBranchDtlByBranchId(authorizationHeader, branchId);
				branchId = branchDtl.getBranchId();
			    jobcardNumber = commonDao.getDocumentNumberById("JC", branchId, session);
				commonDao.updateDocumentNumber("JOB CARD OPEN", branchId, jobcardNumber, session);
				if(jobcardNumber!=null) {
				jobHdrEntity.setRoNumber(jobcardNumber);
				session.save(jobHdrEntity);
				}else {
				 responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
		         responseModel.setStatusCode(500);
		         return responseModel;
				}
				
				responseModel.setRoId(jobHdrEntity.getRoId());
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess && jobcardNumber!=null) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess && jobcardNumber!=null) {
				responseModel.setVinId(jobHdrEntity.getVinId());
				responseModel.setJobCardNumber(jobHdrEntity.getRoNumber());
				responseModel.setMsg("Job Card Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else if (jobcardNumber != null) {
                responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
                responseModel.setStatusCode(500);
            } else {
                responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
            }

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveCustVoiceData(String authorizationHeader, String userCode,
			CustomberVoiceRequest customberVoiceRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveJobCardData invoked.." + userCode);
			logger.debug(customberVoiceRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		CustomberVoiceEntity custVoiceEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			custVoiceEntity = mapper.map(customberVoiceRequest, CustomberVoiceEntity.class, "customerVoice");
//			System.out.println("jobCardData:::::::::::::::::::::" + customberVoiceRequest);
//			System.out.println("jobHdrEntity:::::::::::::::::::::" + custVoiceEntity);
//			System.out.println("isSuccess::::::::::::" + isSuccess);
			logger.debug(custVoiceEntity.toString());
			if (custVoiceEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				custVoiceEntity.setRoId(roId.intValue());
				custVoiceEntity.setCreatedDate(new Date());
				custVoiceEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(custVoiceEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("customber Voice Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveTyreDetails(String authorizationHeader, String userCode,
			TyreDetailsRequest tyreDetailsRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveTyreDetails invoked.." + userCode);
			logger.debug(tyreDetailsRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		jobCardDetailsEntity jobCardDetailsEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobCardDetailsEntity = mapper.map(tyreDetailsRequest, jobCardDetailsEntity.class, "jobCardDetails");
//			System.out.println("tyreDetailsRequest:::::::::::::::::::::" + tyreDetailsRequest);
//			System.out.println("jobCardDetailsEntity:::::::::::::::::::::" + jobCardDetailsEntity);
//			System.out.println("isSuccess::::::::::::" + isSuccess);
			logger.debug(jobCardDetailsEntity.toString());
			if (jobCardDetailsEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				jobCardDetailsEntity.setRoId(roId);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(jobCardDetailsEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("job card Details Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveLabourChargeData(String authorizationHeader, String userCode,
			LabourChargeRequest labourChargeRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveLabourChargeData invoked.." + userCode);
			logger.debug(labourChargeRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		LabourChargeEntity labourChargeEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			labourChargeEntity = mapper.map(labourChargeRequest, LabourChargeEntity.class, "labourCharge");
			labourChargeEntity.setAmount(labourChargeRequest.getAmount());
			labourChargeEntity.setGstAmt(labourChargeRequest.getTotalGst());	
			logger.debug(labourChargeEntity.toString());
			if (labourChargeEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				labourChargeEntity.setRoId(roId);
				labourChargeEntity.setCreatedDate(new Date());
				labourChargeEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(labourChargeEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("labour charge Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveOuterLabourChargeData(String authorizationHeader, String userCode,
			OutSiderLabourChargeRequest outerLabourChargeRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveOuterLabourChargeData invoked.." + userCode);
			logger.debug(outerLabourChargeRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		OutSiderLabourChargeEntity outerLabourChargeEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			outerLabourChargeEntity = mapper.map(outerLabourChargeRequest, OutSiderLabourChargeEntity.class,
					"outerLabourCharge");
			logger.debug(outerLabourChargeEntity.toString());
			if (outerLabourChargeEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				outerLabourChargeEntity.setRoId(roId);
				outerLabourChargeEntity.setCreatedDate(new Date());
				outerLabourChargeEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(outerLabourChargeEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg(" Outer labour charge Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse savePaintScrachedData(String authorizationHeader, String userCode,
			PaintRequest paintRequest, Integer paintCheckBoxId, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("savePaintScrachedData invoked.." + userCode);
			logger.debug(paintRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		PaintEntity paintEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			paintEntity = mapper.map(paintRequest, PaintEntity.class, "paintScratch");
			System.out.println("paintRequest:::::::::::::::::::::" + paintRequest);
			System.out.println("paintEntity:::::::::::::::::::::" + paintEntity);
			System.out.println("isSuccess::::::::::::" + isSuccess);
			logger.debug(paintEntity.toString());
			if (paintEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				paintEntity.setRoId(roId);
				paintEntity.setPaintId(BigInteger.valueOf(paintCheckBoxId));
				paintEntity.setCreatedDate(new Date());
				paintEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(paintEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("paint Scratch Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveInventoryCodes(String authorizationHeader, String userCode,
			Integer inventoryRequestId, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveInventoryCodes invoked.." + userCode);
			logger.debug(inventoryRequestId.toString() + "ROID" + roId);
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			logger.debug(inventoryRequestId.toString());
			if (roId != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				jobInventoryCheckListEntity inventoryEntity = new jobInventoryCheckListEntity();
				// System.out.println("ROOOO"+roId.intValue());
				inventoryEntity.setRoId(roId.intValue());
				inventoryEntity.setInventoryId(inventoryRequestId);
				inventoryEntity.setCreatedDate(currDate);
				inventoryEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(inventoryEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Inventory Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

//	@Override
//	public MessageCodeResponse saveDocuments(String userCode, Integer roId, MultipartFile chassisNoPic,
//			MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("saveDocuments invoked.." + userCode);
//			logger.debug(roId.toString());
//		}
//		Session session = null;
//		Transaction transaction = null;
//		Query query = null;
//		MessageCodeResponse responseModel = new MessageCodeResponse();
//		boolean isSuccess = false;
//		try {
//			session = sessionFactory.openSession();
//			transaction = session.beginTransaction();
//			jobCardDetailsEntity jobCardDetailsEntity = null;
//			if (roId != null) {
//				isSuccess = true;
//			}
//			Date currDate = new Date();
//			if (isSuccess) {
//				String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
//				query = session.createNativeQuery(sqlQuery).addEntity(jobCardDetailsEntity.class);
//				query.setParameter("roId", roId);
//				jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();
//				String pic = null;
//				String picOther = null;
//				if (jobCardDetailsEntity != null) {
//					// Create the upload directory if it doesn't exist
//					if (chassisNoPic == null || chassisNoPic.isEmpty() && hourMeterPic == null
//							|| hourMeterPic.isEmpty()) {
//						responseModel.setMessage("File cannot be null or empty.");
//						responseModel.setCode("500");
//						return responseModel;
//					}
//					String chassis = utils.cleanFilePath(chassisNoPic.getOriginalFilename());
//					String hourMeter = utils.cleanFilePath(hourMeterPic.getOriginalFilename());
//					if (pic1 != null) {
//						pic = utils.cleanFilePath(pic1.getOriginalFilename());
//					}
//					if (pic2 != null) {
//						picOther = utils.cleanFilePath(pic2.getOriginalFilename());
//					}
//					Path uploadPath = Paths.get(uploadDir);
//					// Create the upload directory if it doesn't exist
//					if (!Files.exists(uploadPath)) {
//						Files.createDirectories(uploadPath);
//					}
//					// Save the file to the server
//					Path chassisfilePath = uploadPath.resolve(chassis);
//					Files.copy(chassisNoPic.getInputStream(), chassisfilePath);
//					Path hourMeterFilePath = uploadPath.resolve(hourMeter);
//					Files.copy(hourMeterPic.getInputStream(), hourMeterFilePath);
//					if (pic1 != null) {
//						Path picFilePath = uploadPath.resolve(pic);
//						Files.copy(pic1.getInputStream(), picFilePath);
//					}
//					if (pic2 != null) {
//						Path picOtherFilePath = uploadPath.resolve(picOther);
//						Files.copy(pic2.getInputStream(), picOtherFilePath);
//					}
//
//					jobCardDetailsEntity.setChassisNoPhotoGraph(chassisNoPic.getOriginalFilename());
//					jobCardDetailsEntity.setHourMeterPhotoGraph(hourMeterPic.getOriginalFilename());
//					if (pic1 != null) {
//						jobCardDetailsEntity.setUploadPhotoGraph(pic1.getOriginalFilename());
//					} else {
//						jobCardDetailsEntity.setUploadPhotoGraph(null);
//					}
//					if (pic2 != null) {
//						jobCardDetailsEntity.setUploadPhotoGraph1(pic2.getOriginalFilename());
//					} else {
//						jobCardDetailsEntity.setUploadPhotoGraph1(null);
//					}
//					responseModel.setCode("200");
//					responseModel.setMessage("Sucess");
//					session.merge(jobCardDetailsEntity);
//				}
//			} else {
//				responseModel.setMessage("Something went wrong !!!");
//			}
//
//			if (isSuccess) {
//
//				transaction.commit();
//			}
//
//		} catch (SQLGrammarException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			isSuccess = false;
//			logger.error(this.getClass().getName(), ex);
//		} catch (HibernateException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			isSuccess = false;
//			logger.error(this.getClass().getName(), ex);
//		} catch (Exception ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			isSuccess = false;
//			responseModel.setMessage(ex.toString());
//			logger.error(this.getClass().getName(), ex);
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//			if (isSuccess) {
//				responseModel.setMessage("Document upload Successfully.");
//				responseModel.setCode("201");
//			} else {
//				responseModel.setCode("500");
//			}
//
//		}
//		return responseModel;
//	}

//	public MessageCodeResponse saveDocuments(String userCode, Integer roId, MultipartFile chassisNoPic,
//	        MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2) {
//	    if (logger.isDebugEnabled()) {
//	        logger.debug("saveDocuments invoked.." + userCode);
//	        logger.debug(roId.toString());
//	    }
//
//	    Session session = null;
//	    Transaction transaction = null;
//	    Query query = null;
//	    MessageCodeResponse responseModel = new MessageCodeResponse();
//	    boolean isSuccess = false;
//
//	    try {
//	        session = sessionFactory.openSession();
//	        transaction = session.beginTransaction();
//	        jobCardDetailsEntity jobCardDetailsEntity = null;
//
//	        if (roId != null) {
//	            isSuccess = true;
//	        }
//
//	        Date currDate = new Date();
//
//	        if (isSuccess) {
//	            String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
//	            query = session.createNativeQuery(sqlQuery).addEntity(jobCardDetailsEntity.class);
//	            query.setParameter("roId", roId);
//	            jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();
//	            String pic = null;
//	            String picOther = null;
//
//	            if (jobCardDetailsEntity != null) {
//	                // Create the upload directory if it doesn't exist
//	                if (isNullOrEmpty(chassisNoPic.getOriginalFilename())
//	                        || isNullOrEmpty(hourMeterPic.getOriginalFilename())) {
//	                    responseModel.setMessage("File cannot be null or empty.");
//	                    responseModel.setCode("500");
//	                    return responseModel;
//	                }
//// save file 
//	                saveFile(chassisNoPic.getInputStream(), utils.cleanFilePath(chassisNoPic.getOriginalFilename()));
//	                saveFile(hourMeterPic.getInputStream(), utils.cleanFilePath(hourMeterPic.getOriginalFilename()));
//
//	                if (pic1 != null) {
//	                    saveFile(pic1.getInputStream(), utils.cleanFilePath(pic1.getOriginalFilename()));
//	                }
//	                if (pic2 != null) {
//	                    saveFile(pic2.getInputStream(), utils.cleanFilePath(pic2.getOriginalFilename()));
//	                }
//
//	                jobCardDetailsEntity.setChassisNoPhotoGraph(chassisNoPic.getOriginalFilename());
//	                jobCardDetailsEntity.setHourMeterPhotoGraph(hourMeterPic.getOriginalFilename());
//
//	                if (pic1 != null) {
//	                    jobCardDetailsEntity.setUploadPhotoGraph(pic1.getOriginalFilename());
//	                } else {
//	                    jobCardDetailsEntity.setUploadPhotoGraph(null);
//	                }
//
//	                if (pic2 != null) {
//	                    jobCardDetailsEntity.setUploadPhotoGraph1(pic2.getOriginalFilename());
//	                } else {
//	                    jobCardDetailsEntity.setUploadPhotoGraph1(null);
//	                }
//
//	                responseModel.setCode("200");
//	                responseModel.setMessage("Success");
//	                session.merge(jobCardDetailsEntity);
//	            }
//	        } else {
//	            responseModel.setMessage("Something went wrong !!!");
//	        }
//
//	        if (isSuccess) {
//	            transaction.commit();
//	        }
//
//	    } catch (SQLGrammarException ex) {
//	        if (transaction != null) {
//	            transaction.rollback();
//	        }
//	        isSuccess = false;
//	        logger.error(this.getClass().getName(), ex);
//	    } catch (HibernateException ex) {
//	        if (transaction != null) {
//	            transaction.rollback();
//	        }
//	        isSuccess = false;
//	        logger.error(this.getClass().getName(), ex);
//	    } catch (Exception ex) {
//	        if (transaction != null) {
//	            transaction.rollback();
//	        }
//	        isSuccess = false;
//	        responseModel.setMessage(ex.toString());
//	        logger.error(this.getClass().getName(), ex);
//	    } finally {
//	        if (session != null) {
//	            session.close();
//	        }
//
//	        if (isSuccess) {
//	            responseModel.setMessage("Document upload Successfully.");
//	            responseModel.setCode("201");
//	        } else {
//	            responseModel.setCode("500");
//	        }
//	    }
//
//	    return responseModel;
//	}
//
//	private void saveFile(InputStream inputStream, String fileName) throws IOException {
//	    if (StringUtils.isEmpty(fileName)) {
//	        throw new IOException("File name cannot be null or empty.");
//	    }
//	    Path uploadPath = Paths.get(uploadDir);
//	    Path filePath = uploadPath.resolve(fileName);
//	    Files.copy(inputStream, filePath);
//	}
//
//	private boolean isNullOrEmpty(String str) {
//	    return str == null || str.isEmpty();
//	}

	public MessageCodeResponse saveDocuments(String userCode, Integer roId, MultipartFile chassisNoPic,
			MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2) {
		Session session = null;
		Transaction transaction = null;
		MessageCodeResponse responseModel = new MessageCodeResponse();
		boolean isSuccess = false;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobCardDetailsEntity jobCardDetailsEntity = null;

			if (roId != null) {
				isSuccess = true;
			}

			Date currDate = new Date();
			System.out.println("bhutest 1 "+uploadDirForProd);
			if (isSuccess) {
				String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
				Query query = session.createNativeQuery(sqlQuery).addEntity(jobCardDetailsEntity.class);
				query.setParameter("roId", roId);
				jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();
				String pic = null;
				String picOther = null;

				if (jobCardDetailsEntity != null) {
					
					String roIdFolder = "";
				//	String property = System.getProperty("os.name");
					if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
						roIdFolder = uploadDir + roId + File.separator;
					} else {
						roIdFolder = uploadDirForProd + roId + File.separator;
					}
					System.out.println("bhutest 2 "+roIdFolder);
					
					// Create the roId folder if it doesn't exist
					File roIdDir = new File(roIdFolder);
					if (!roIdDir.exists()) {
						roIdDir.mkdirs();
					}

					// Save files to roId folder
					if (chassisNoPic != null) {
						saveFile(chassisNoPic.getInputStream(), roIdFolder + chassisNoPic.getOriginalFilename());
					}
					if (hourMeterPic != null) {
						saveFile(hourMeterPic.getInputStream(), roIdFolder + hourMeterPic.getOriginalFilename());
					}
					if (pic1 != null) {
						saveFile(pic1.getInputStream(), roIdFolder + pic1.getOriginalFilename());
					}

					if (pic2 != null) {
						saveFile(pic2.getInputStream(), roIdFolder + pic2.getOriginalFilename());
					}
					if (chassisNoPic != null) {
						jobCardDetailsEntity.setChassisNoPhotoGraph(chassisNoPic.getOriginalFilename());
					} else {
						jobCardDetailsEntity.setChassisNoPhotoGraph(null);
					}
					if (hourMeterPic != null) {
						jobCardDetailsEntity.setHourMeterPhotoGraph(hourMeterPic.getOriginalFilename());
					} else {
						jobCardDetailsEntity.setHourMeterPhotoGraph(null);
					}
					if (pic1 != null) {
						jobCardDetailsEntity.setUploadPhotoGraph(pic1.getOriginalFilename());
					} else {
						jobCardDetailsEntity.setUploadPhotoGraph(null);
					}

					if (pic2 != null) {
						jobCardDetailsEntity.setUploadPhotoGraph1(pic2.getOriginalFilename());
					} else {
						jobCardDetailsEntity.setUploadPhotoGraph1(null);
					}

					responseModel.setCode("200");
					responseModel.setMessage("Success");
					session.merge(jobCardDetailsEntity);
				}
			} else {
				responseModel.setMessage("Something went wrong !!!");
			}

			if (isSuccess) {
				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMessage(ex.toString());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}

			if (isSuccess) {
				responseModel.setMessage("Document upload Successfully.");
				responseModel.setCode("201");
			} else {
				responseModel.setCode("500");
			}
		}

		return responseModel;
	}

	private void saveFile(InputStream inputStream, String filePath) throws IOException {
		if (StringUtils.isEmpty(filePath)) {
			throw new IOException("File path cannot be null or empty.");
		}

		Path filePathObj = Paths.get(filePath);
		Files.copy(inputStream, filePathObj);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	@Override
	public JobCardCreateResponse saveActivityDetails(String authorizationHeader, String userCode,
			ServiceActivityRequest serviceActivityRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveActivityDetails invoked.." + userCode + serviceActivityRequest);
			logger.debug(roId.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobCardDetailsEntity jobCardDetailsEntity = null;
			if (roId != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
				query = session.createNativeQuery(sqlQuery).addEntity(jobCardDetailsEntity.class);
				query.setParameter("roId", roId);
				jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();
				String pic = null;
				String picOther = null;
				if (jobCardDetailsEntity != null) {
					jobCardDetailsEntity.setServiceActivity(serviceActivityRequest.getActivityPlanId());
					jobCardDetailsEntity.setServiceActivityId(serviceActivityRequest.getServiceTypelookupId());
					responseModel.setStatusCode(200);
					responseModel.setMsg("sucess");
					session.merge(jobCardDetailsEntity);
				}
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.toString());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Service activity save Successfully.");
				responseModel.setStatusCode(200);
			} else {
				responseModel.setStatusCode(500);
			}

		}
		return responseModel;
	}

	@Override
	public List<JobCardSearchResponse> getsearchJobCardDetails(String authorizationHeader, String userCode,
			JobCardSearchRequest jobCardSearchRequest) {

		if (logger.isDebugEnabled()) {
			logger.debug("getsearchJobCardDetails invoked.." + userCode);
			logger.debug("jobCardSearchRequest.." + jobCardSearchRequest);
		}
		Query query = null;
		Session session = null;
		JobCardSearchResponse responseListModel = null;
		List<JobCardSearchResponse> responseModelList = null;
		Integer recordCount = 0;
		// SV_RO_SEARCH_JOB_CARD
		String sqlQuery = "exec [SV_RO_SEARCH_JOB_CARD]:BranchId,:UserCode,:RONumber,:ChasisNumber,:EngineNumber,:VinNo,:CustomerName,:MobileNo,:ServiceTypeId,:RepairTypeId,:JobCardCategoryId,:InvoiceNo,:ServcieBookingNumber,:QuotationNumber,:jobCardStatus,:Fromdate,:Todate,:page,:size,:pcId,:hoId,:zoneId,:stateId,:territoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("BranchId", jobCardSearchRequest.getBranchId());
			query.setParameter("UserCode", userCode);
			query.setParameter("RONumber", jobCardSearchRequest.getJobCardNumber());
			query.setParameter("ChasisNumber", jobCardSearchRequest.getChassisNumber());
			query.setParameter("EngineNumber", jobCardSearchRequest.getEngineNumber());
			query.setParameter("VinNo", jobCardSearchRequest.getVinNumber());
			query.setParameter("CustomerName", jobCardSearchRequest.getCustomerName());
			query.setParameter("MobileNo", jobCardSearchRequest.getMobileNo());
			query.setParameter("ServiceTypeId", jobCardSearchRequest.getServiceTypeId());
			query.setParameter("RepairTypeId", jobCardSearchRequest.getRepairTypeId());
			query.setParameter("JobCardCategoryId", jobCardSearchRequest.getJobCardCategoryId());
			query.setParameter("InvoiceNo", jobCardSearchRequest.getInvoiceNumber());
			query.setParameter("ServcieBookingNumber", jobCardSearchRequest.getServiceBookingNumber());
			query.setParameter("QuotationNumber", jobCardSearchRequest.getQuotationNumber());
			query.setParameter("jobCardStatus", jobCardSearchRequest.getJobCardStatus());
			query.setParameter("Fromdate", jobCardSearchRequest.getFromDate());
			query.setParameter("Todate", jobCardSearchRequest.getToDate());
			query.setParameter("page", jobCardSearchRequest.getPage());
			query.setParameter("size", jobCardSearchRequest.getSize());
			query.setParameter("pcId", jobCardSearchRequest.getPcId());
			query.setParameter("hoId", jobCardSearchRequest.getHoId());
			query.setParameter("zoneId", jobCardSearchRequest.getZoneId());
			query.setParameter("stateId", jobCardSearchRequest.getStateId());
			query.setParameter("territoryId", jobCardSearchRequest.getTerritoryId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardSearchResponse();
				responseModelList = new ArrayList<JobCardSearchResponse>();
				JobCardSearchResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardSearchResponse();
					// Set the properties from the row data obtained from the procedure result
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setJobCardCategory((String) row.get("CategoryDesc"));
					responseModel.setChassisNo((String) row.get("ChassisNo"));
					responseModel.setVinNumber((String) row.get("VinNo"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setServiceBooking((String) row.get("bookingno"));
					responseModel.setSource((String) row.get("Source"));
					responseModel.setEngineNo((String) row.get("EngineNo"));
					responseModel.setMobileNo((String) row.get("MobileNumber"));
					// responseModel.setPlaceOfService((String) row.get("PlaceOfServcie"));
					// responseModel.setManualJobCard((String) row.get("ManualJobCard"));
					// responseModel.setManualJobCardDate((Date) row.get("ManualJobcardate"));
					responseModel.setJobCardId((BigInteger) row.get("ro_id"));
					responseModel.setJobcardNo((String) row.get("JobCardNo"));
					responseModel.setJobCreationDate((Date) row.get("JobCardDate"));
					// responseModel.setJobCardTime((String) row.get("JobCard_time"));
					responseModel.setJobCardStatus((String) row.get("Status"));
					responseModel.setCurrentHours((BigInteger) row.get("CurrentHour"));
					responseModel.setPreviousHours((BigInteger) row.get("PreviousHour"));
					// responseModel.setMeterHourChange((BigInteger) row.get("MeterChageHour"));
					// responseModel.setTotalHours((BigInteger) row.get("TotalHour"));
					responseModel.setServiceType((String) row.get("Servcietype"));
					responseModel.setRepairOrderType((String) row.get("RepairOrderType"));
					responseModel.setServiceQuotation((String) row.get("Quationno"));
//					responseModel.setQuotationDate((Date) row.get("QuationDate"));
					responseModel.setInvoice((String) row.get("InvoiceNumber"));
//					responseModel.setInvoiceDate((Date) row.get("Invoicedate"));
					// responseModel.setModelDesc((String) row.get("ModelDesc"));

//					responseModel.setInwardId((Integer) row.get("inwardId"));
//					responseModel.setProfitCenter((String) row.get("profitcenter"));
					responseModel.setCutsomerName((String) row.get("cutsomerName"));
//					responseModel.setBatteryVoltage((Integer) row.get("Batteryboltage"));
//
//					responseModel.setLrDate((Date) row.get("LRDate"));
//					responseModel.setTransportName((String) row.get("TransporterName"));
//					responseModel.setLrNumber((String) row.get("LRNumber"));
//					responseModel.setPdiDoneBy((String) row.get("PdiDoneBy"));
//					responseModel.setPendingInwardPdi((String) row.get("pendingInwardPdi"));
//					responseModel.setCompleteInwardPdi((String) row.get("completeInwardpdi"));
//
//					responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));

//					responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
//					responseModel.setFIPMakeNumber((String) row.get("FIPMakeNumber"));
//					responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));
//					responseModel.setOperatorMobileNumber((String) row.get("OperatorMobileNumber"));
//
//					responseModel.setInstallationDoneBy((String) row.get("installationdoneby"));
//					responseModel.setRepresentativeType((String) row.get("represetatiotype"));
//					responseModel.setRepresentativeName((String) row.get("representativeName"));
//					responseModel.setEstimateDate((Date) row.get("estimateDate"));
//					responseModel.setCompletionTime((String) row.get("completationTime"));
//					responseModel.setEstimateAmount((BigDecimal) row.get("estimateAmount"));
//					responseModel.setOperatorName((String) row.get("operatorName"));
//					responseModel.setApplicationUsedBy((String) row.get("applicationUsedById"));
//					responseModel.setApplicationUsedFor((String) row.get("applicationUsedFor"));
//					responseModel.setImplementType((String) row.get("implementtyep"));
//					responseModel.setImplementTypeOthers((String) row.get("implementTypeOthers"));
//					responseModel.setServicetechnician((String) row.get("servicetechnicianId"));
//					responseModel.setMechanic_one((String) row.get("mechanic_one_id"));
//					responseModel.setMechanic_two((String) row.get("mechanic_two_id"));
//					responseModel.setMechanic_three((String) row.get("mechanic_three_id"));
					responseModel.setTotalRecords((Integer) row.get("totalRecords"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;

	}

	@Override
	public JobCardCreateResponse savePdiCheckList(String authorizationHeader, String userCode,
			PdiChecklistRequest pdiChecklistRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("savePdiCheckList invoked.." + userCode);
			logger.debug(pdiChecklistRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		PdiCheckListEntity pdiCheckListEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			pdiCheckListEntity = mapper.map(pdiChecklistRequest, PdiCheckListEntity.class, "PDIcheckpoint");
			// System.out.println("pdiChecklistRequest:::::::::::::::::::::" +
			// pdiChecklistRequest);
			// System.out.println("PdiCheckListEntity:::::::::::::::::::::" +
			// pdiCheckListEntity);
			if (pdiCheckListEntity != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				pdiCheckListEntity.setRoId(roId);
				pdiCheckListEntity.setCreatedDate(new Date());
				pdiCheckListEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(pdiCheckListEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("PDI cehckPoint Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public HashMap<String, Object> fetchJobCardaTypeWise(String authorizationHeader, String userCode, String type,
			String searchText) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardaTypeWise invoked.." + searchText + "Type:::" + type);
		}
		Session session = null;
		String grnNumber = null;
		HashMap<String, Object> searchList = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = null;
		if (type.equalsIgnoreCase("chassisNo")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_CHASSIS_NO]:userCode,:searchText";
		} else if (type.equalsIgnoreCase("engineNumber")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_ENGINE]:userCode,:searchText";
		} else if (type.equalsIgnoreCase("vinNumber")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_VINNO]:userCode,:searchText";
		} else if (type.equalsIgnoreCase("mobileNumber")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_MOBILENO]:userCode,:searchText";
		} else if (type.equalsIgnoreCase("customerName")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_CUSTOMERNAME]:userCode,:searchText";
		} else if (type.equalsIgnoreCase("bookingNumber")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_BOOKINGNO]:userCode,:searchText";
		} else if (type.equalsIgnoreCase("quotatioNumber")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_QUOTATIONNO]:userCode,:searchText,null";
		} else if (type.equalsIgnoreCase("invoiceNumber")) {
			sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_INVOICENO]:userCode,:searchText";
		}

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", searchText);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList.put(type, data);

			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

	@Override
	public JobCardCreateResponse saveInspectionCheckList(String authorizationHeader, String userCode,
			InspectionCheckPointRequest inspectionChecklistRequest, BigInteger roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveInspectionCheckList invoked.." + userCode);
			logger.debug(inspectionChecklistRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		InspectionCheckListEntity inspectionCheckListEntity = new InspectionCheckListEntity();
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (inspectionChecklistRequest != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				inspectionCheckListEntity.setInstallDtlId(inspectionChecklistRequest.getInstallDtlId().intValue());
				inspectionCheckListEntity.setRoId(roId.intValue());
				inspectionCheckListEntity.setInstallationParameterId(inspectionChecklistRequest.getCheckPoint());
				inspectionCheckListEntity.setFlag(inspectionChecklistRequest.getOkFlag());
				inspectionCheckListEntity.setCreatedDate(new Date());
				inspectionCheckListEntity.setCreatedBy(userCode);
				responseModel.setStatusCode(201);
				responseModel.setMsg("sucess");
				session.save(inspectionCheckListEntity);
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Inspection cehckPoint Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardDataResponse fetchJobCardDetails(String authorizationHeader, String userCode, Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardDetails invoked.." + userCode + "roId" + roId);
		}
		Query query = null;
		Session session = null;
		JobCardDataResponse responseModel = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModel = new JobCardDataResponse();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardDataResponse();
					
					responseModel.setClosingRemark((String) row.get("closing_Resion"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setCategoryId((Integer) row.get("CategoryId"));
					responseModel.setJobCardCategory((String) row.get("CategoryDesc"));
					responseModel.setChassisNo((String) row.get("ChassisNo"));
					responseModel.setVinNumber((String) row.get("VinNo"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setServiceBooking((String) row.get("bookingno"));
					responseModel.setServiceBookingDate((Date) row.get("bookingDate"));
					responseModel.setSource((String) row.get("Source"));
					responseModel.setEngineNo((String) row.get("EngineNo"));
					responseModel.setPlaceOfService((String) row.get("PlaceOfServcie"));
					responseModel.setManualJobCard((String) row.get("ManualJobCard"));
					responseModel.setManualJobCardDate((Date) row.get("ManualJobcardate"));
					responseModel.setJobcardNo((String) row.get("JobCardNo"));
					responseModel.setJobCreationDate((Date) row.get("JobCardDate"));
					responseModel.setJobCardTime((String) row.get("JobCard_time"));
					responseModel.setJobCardStatus((String) row.get("Status"));
					responseModel.setCurrentHours((BigInteger) row.get("CurrentHour"));
					responseModel.setPreviousHours((BigInteger) row.get("PreviousHour"));
					responseModel.setMeterNoChage((BigInteger) row.get("MeterChageHour"));
					responseModel.setTotalHours((BigInteger) row.get("TotalHour"));
					responseModel.setServiceType((String) row.get("Servcietype"));
					responseModel.setRepairOrderType((String) row.get("RepairOrderType"));
					responseModel.setServiceQuatation((String) row.get("Quationno"));
					responseModel.setQuationDate((Date) row.get("QuationDate"));
					responseModel.setInvoice((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((Date) row.get("Invoicedate"));
					responseModel.setModelDesc((String) row.get("ModelDesc"));
					responseModel.setInwardId((Integer) row.get("inwardId"));
					responseModel.setProfitCenter((String) row.get("profitcenter"));
					responseModel.setCoustember((String) row.get("cutsomerName"));
					responseModel.setBatteryVoltage((BigDecimal) row.get("BatteryVoltage"));
					responseModel.setLrDate((Date) row.get("LRDate"));
					responseModel.setTransportName((String) row.get("TransporterName"));
					responseModel.setLrNumber((String) row.get("LRNumber"));
					responseModel.setPdiDoneBy((String) row.get("PdiDoneBy"));
					responseModel.setPendingInwardPdi((String) row.get("pendingInwardPdi"));
					responseModel.setCompleteInwordPdi((String) row.get("completeInwardpdi"));
					responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));
					responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
					responseModel.setFIPMakeNumber((String) row.get("FIPMakeNumber"));
					responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));

					responseModel.setOperatorManualNo((String) row.get("operatorManualNo"));

					responseModel.setOperatorMobileNumber((String) row.get("OperatorMobileNumber"));
					responseModel.setInstallationDoneBy((String) row.get("installationdoneby"));
					responseModel.setRepresentativeType((String) row.get("represetatiotype"));
					responseModel.setRepresentativeName((String) row.get("representativeName"));
					responseModel.setEstimateDate((Date) row.get("estimateDate"));
					responseModel.setCompletationTime((String) row.get("completationTime"));
					responseModel.setEstimateAmount((BigDecimal) row.get("Estimated_amount"));
					responseModel.setOperatorName((String) row.get("OperatorName"));
					responseModel.setApplicationUsedBy((String) row.get("mobileapplicationuserfor"));
					responseModel.setApplicationUsedFor((String) row.get("usedother"));
					responseModel.setImplementType((String) row.get("implementtyep"));
					responseModel.setImplementTypeOthers((String) row.get("other"));
					responseModel.setServicetechnician((String) row.get("servcietech"));
					responseModel.setMechanic_one((String) row.get("Mech1"));
					responseModel.setMechanic_two((String) row.get("Mech2"));
					responseModel.setMechanic_three((String) row.get("Mech3"));
					responseModel.setROCancelReason((String) row.get("ROCancelReason"));
					responseModel.setBranchId((Integer) row.get("branch_id"));
					responseModel.setCustomerId((BigInteger)row.get("original_customer_master_id"));
					responseModel.setWcrStatus((String)row.get("wcr_status"));
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModel;
	}

	@Override
	public TyreDetailsResponse fetchTyreDetails(String authorizationHeader, String userCode, Integer roId, int i) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchTyreDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		TyreDetailsResponse responseListModel = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new TyreDetailsResponse();
				for (Object object : data) {
					Map row = (Map) object;
					responseListModel = new TyreDetailsResponse();
					responseListModel.setFrontLHPSI((String) row.get("FRONT_LHPSI"));
					responseListModel.setFrontRHPSI((String) row.get("FRONT_RHPSI"));
					responseListModel.setRearLHPSI((String) row.get("REAR_LHPSI"));
					responseListModel.setRearRHPSI((String) row.get("REAR_RHPSI"));
					responseListModel.setFrontTireMakeLHNumber((String) row.get("FrontTyerMakeLHNumber"));
					responseListModel.setFrontTireMakeRHNumber((String) row.get("FrontTyerMakeRHNumber"));
					responseListModel.setRearTireMakeRHNumber((String) row.get("RearTyerMakeRHNumber"));
					responseListModel.setRearTireMakeLHNumber((String) row.get("RearTyerMakeLHNumber"));
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseListModel;
	}

	@Override
	public List<CustomerServiceResponse> fetchCustDetails(String authorizationHeader, String userCode, Integer roId,
			int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		CustomerServiceResponse responseListModel = null;
		List<CustomerServiceResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new CustomerServiceResponse();
				responseModelList = new ArrayList<CustomerServiceResponse>();
				CustomerServiceResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerServiceResponse();
					responseModel.setId((Integer) row.get("ID"));
					responseModel.setCustomerConcern((String) row.get("CUSTOMER_CONCERN"));
					responseModel.setActivityToBeDone((String) row.get("Activity_To_Done"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<LabourChargeResposne> fetchLabourChargeDetails(String authorizationHeader, String userCode,
			Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchLabourChargeDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		LabourChargeResposne responseListModel = null;
		List<LabourChargeResposne> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new LabourChargeResposne();
				responseModelList = new ArrayList<LabourChargeResposne>();
				LabourChargeResposne responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new LabourChargeResposne();
					responseModel.setLabourId((Integer) row.get("Labour_Id"));
					responseModel.setLabourCode((String) row.get("LabourCode"));
					responseModel.setLabourDesc((String) row.get("LabourDesc"));
					responseModel.setBillableTypeCode((String) row.get("BillableTypeDesc"));
					responseModel.setLabourCharge((BigDecimal) row.get("LabourCharge"));
					responseModel.setStandardHrs((BigDecimal) row.get("StandardHrs"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setTotalAmt((BigDecimal) row.get("TotalAmt"));
					responseModel.setInsurancePartyName((String) row.get("PartyName"));
					responseModel.setBayType((String)row.get("bayType"));
					responseModel.setMechanicName((String)row.get("mechanicName"));
					responseModel.setStartDate((Date)row.get("startDate"));
					responseModel.setEndDate((Date)row.get("endDate"));
					responseModel.setStartTime((String)row.get("startTime"));
					responseModel.setEndTime((String)row.get("endTime"));
					responseModel.setOem((Integer) row.get("OEM"));
					responseModel.setCustomer((Integer) row.get("customer"));
					responseModel.setDealer((Integer) row.get("dealer"));
					responseModel.setInsurance((Integer) row.get("insurance"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<OutSideLabourResponse> fetchOutSideLabourChargeDetails(String authorizationHeader, String userCode,
			Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOutSideLabourChargeDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		OutSideLabourResponse responseListModel = null;
		List<OutSideLabourResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new OutSideLabourResponse();
				responseModelList = new ArrayList<OutSideLabourResponse>();
				OutSideLabourResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OutSideLabourResponse();
					responseModel.setLabourCode((String) row.get("LabourCode"));
					responseModel.setLabourId((Integer) row.get("Labour_Id"));
					responseModel.setLabourDesc((String) row.get("LabourDesc"));
					responseModel.setBillableTypeCode((String) row.get("BillableTypeDesc"));
					responseModel.setBillTypeId((Integer) row.get("Bill_Type_ID"));
					responseModel.setStandardHrs((BigDecimal) row.get("StandardHrs"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setTotalAmount((BigDecimal) row.get("TotalAmt"));
					responseModel.setInsurancePartyName((String) row.get("PartyName"));
					responseModel.setBayType((String)row.get("bayType"));
					responseModel.setMechanicName((String)row.get("mechanicName"));
					responseModel.setStartDate((Date)row.get("startDate"));
					responseModel.setEndDate((Date)row.get("endDate"));
					responseModel.setStartTime((String)row.get("startTime"));
					responseModel.setEndTime((String)row.get("endTime"));
					responseModel.setOem((Integer) row.get("OEM"));
					responseModel.setCustomer((Integer) row.get("customer"));
					responseModel.setDealer((Integer) row.get("dealer"));
					responseModel.setInsurance((Integer) row.get("insurance"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<InventoryResponse> fetchInventoryDetails(String authorizationHeader, String userCode, Integer roId,
			int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInventoryDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		InventoryResponse responseListModel = null;
		List<InventoryResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new InventoryResponse();
				responseModelList = new ArrayList<InventoryResponse>();
				InventoryResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InventoryResponse();
					responseModel.setId((Integer) row.get("Inventory_id"));
					responseModel.setInventoryCode((String) row.get("INVENTORYCODE"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<PaintScratchedResponse> fetchPaintDetails(String authorizationHeader, String userCode, Integer roId,
			int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPaintDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		PaintScratchedResponse responseListModel = null;
		List<PaintScratchedResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PaintScratchedResponse();
				responseModelList = new ArrayList<PaintScratchedResponse>();
				PaintScratchedResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PaintScratchedResponse();

					responseModel.setPaintScratchedId((Integer) row.get("PAINT_SCRATCHED_ID"));
					responseModel.setPaintScratchedDesc((String) row.get("PAINTSCRATCHEDDESC"));
					responseModel.setPaintScratchedCode((String) row.get("PAINTSCRATCHEDCODE"));
					responseModel.setDiesel((String) row.get("DIESEL"));
					responseModel.setFinalActionTaken((String) row.get("FINAL_ACTION_TAKEN"));
					responseModel.setAdviceToCustomer((String) row.get("ADVICE_TO_CUST"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<PartsResponse> fetchPartDetails(String authorizationHeader, String userCode, Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDetails invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		PartsResponse responseListModel = null;
		List<PartsResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PartsResponse();
				responseModelList = new ArrayList<PartsResponse>();
				PartsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartsResponse();
					responseModel.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					responseModel.setBiliableTypeDesc((String) row.get("BillableTypeDesc"));
					responseModel.setPartNumber((String) row.get("PartNumber"));
					responseModel.setPartDesc((String) row.get("PartDesc"));
					responseModel.setPartCategory((String) row.get("Product_category"));
					responseModel.setRequestedQty((BigDecimal) row.get("RequestedQty"));
					responseModel.setIssuedQty((BigDecimal) row.get("IssuedQty"));
					responseModel.setMrp((BigDecimal) row.get("MRP"));
					responseModel.setOem((Integer) row.get("OEM"));
					responseModel.setCustomer((Integer) row.get("Customer"));
					responseModel.setDealer((Integer) row.get("Dealer"));
					responseModel.setInsurance((Integer) row.get("Insurance"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public JobCardCreateResponse closeJobCard(String authorizationHeader, String userCode,
			JobCardCloseRequest requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("cancelSparePO invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		BigInteger userId = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			jobCardEntity hdrDBEntity = null;
			if (requestModel.getRoId() != null) {
				String sqlQuery = "select * from SV_RO_HDR(NOLOCK) where ro_id =:roId";
				query = session.createNativeQuery(sqlQuery).addEntity(jobCardEntity.class);
				query.setParameter("roId", requestModel.getRoId());
				hdrDBEntity = (jobCardEntity) query.uniqueResult();
				if (hdrDBEntity != null) {
					if (!hdrDBEntity.getJobCardStatus().equals("Open")) {
						// dealer can not edit the Machine Po
						isSuccess = false;
						responseModel.setMsg("Job Card already." + hdrDBEntity.getJobCardStatus());
					}
					hdrDBEntity.setClosingResion(requestModel.getCloseRemarks());
					hdrDBEntity.setCloseJobCardDate(new Date());
					hdrDBEntity.setJobCardStatus("Closed");
					hdrDBEntity.setClosedBy(userCode);
					session.merge(hdrDBEntity);
					
					paintScratched(session,userCode,requestModel);
						
				
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Job Card  Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("RoId Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setMsg("Job Card Closed Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Closed Job Card.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}
		}
		return responseModel;
	}

	private void paintScratched(Session session, String userCode, JobCardCloseRequest requestModel) {
		
		Transaction transaction = null;
		Query query = null;
		int updatedRowCount=0;
		PaintEntity paintEntity = null;
		if (requestModel.getRoId() != null) {
			String sqlQuery = "update SV_PAINT_SCRATCHED_RO_Mapping set FINAL_ACTION_TAKEN=:finalAction, ADVICE_TO_CUST=:suggestion where ro_id =:roId";
			session = sessionFactory.openSession();
	        transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("finalAction",requestModel.getFinalAction());
			query.setParameter("suggestion", requestModel.getSuggestion());
			query.setParameter("roId", requestModel.getRoId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        updatedRowCount = query.executeUpdate();
	        transaction.commit();
		}
	}

	@Override
	public JobCardCreateResponse upkoadPdiFiles(String userCode, PdiDataUpdateRequest requestModel,
			MultipartFile files) {

		if (logger.isDebugEnabled()) {
			logger.debug("upkoadPdiFiles invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobCardDetailsEntity jobCardDetailsEntity = null;
			if (requestModel.getRoId() != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			String roId = requestModel.getRoId();
			System.out.println("roId" + roId);
			if (isSuccess) {
				String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
				query = session.createNativeQuery(sqlQuery, jobCardDetailsEntity.class);
				query.setParameter("roId", roId);

				jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();

				if (jobCardDetailsEntity != null) {
					// Create the upload directory if it doesn't exist
					if (files == null || files.isEmpty()) {
						responseModel.setMsg("File cannot be null or empty.");
						responseModel.setStatusCode(500);
						return responseModel;
					}
					String chassis = utils.cleanFilePath(files.getOriginalFilename());
					Path uploadPath = Paths.get(uploadDir);
					// Create the upload directory if it doesn't exist
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}
					// Save the file to the server
					Path filePath = uploadPath.resolve(chassis);
					Files.copy(files.getInputStream(), filePath);

					System.out.println("jobCardDetailsEntity" + jobCardDetailsEntity);
					jobCardDetailsEntity.setPdiFile(filePath.toString());
					jobCardDetailsEntity.setPdiComments(requestModel.getPdiComments());
					responseModel.setMsg("Sucess");
					responseModel.setStatusCode(201);
					session.merge(jobCardDetailsEntity);
				}
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.toString());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Document upload Successfully.");
				responseModel.setStatusCode(201);
			} else {
				responseModel.setStatusCode(500);
			}

		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse uploadInstallationFiles(String userCode, InstallationUpdateRequest requestModel,
			MultipartFile files) {
		if (logger.isDebugEnabled()) {
			logger.debug("uploadInstallationFiles invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobCardDetailsEntity jobCardDetailsEntity = null;
			if (requestModel.getRoId() != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			String roId = requestModel.getRoId();
			System.out.println("roId" + roId);
			if (isSuccess) {
				String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
				query = session.createNativeQuery(sqlQuery, jobCardDetailsEntity.class);
				query.setParameter("roId", roId);

				jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();

				if (jobCardDetailsEntity != null) {
					// Create the upload directory if it doesn't exist
					if (files == null || files.isEmpty()) {
						responseModel.setMsg("File cannot be null or empty.");
						responseModel.setStatusCode(500);
						return responseModel;
					}
					String chassis = utils.cleanFilePath(files.getOriginalFilename());
					Path uploadPath = Paths.get(uploadDir);
					// Create the upload directory if it doesn't exist
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}
					// Save the file to the server
					Path filePath = uploadPath.resolve(chassis);
					Files.copy(files.getInputStream(), filePath);

					System.out.println("jobCardDetailsEntity" + jobCardDetailsEntity);
					jobCardDetailsEntity.setInstallationFile(filePath.toString());
					jobCardDetailsEntity.setInstallationComment(requestModel.getInstallationComments());
					responseModel.setMsg("Sucess");
					responseModel.setStatusCode(201);
					session.merge(jobCardDetailsEntity);
				}
			} else {
				responseModel.setMsg("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.toString());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Document upload Successfully.");
				responseModel.setStatusCode(201);
			} else {
				responseModel.setStatusCode(500);
			}

		}
		return responseModel;
	}

	@Override
	public List<DocumentFilesResponse> fetchDocsFiles(String authorizationHeader, String userCode, Integer roId,
			int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDocsFiles invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		DocumentFilesResponse responseListModel = null;
		List<DocumentFilesResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new DocumentFilesResponse();
				responseModelList = new ArrayList<DocumentFilesResponse>();
				DocumentFilesResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DocumentFilesResponse();

					responseModel.setChassisNoPhotoGraph((String) row.get("HourMeterPhotoGraph"));
					responseModel.setHourMeterPhotoGraph((String) row.get("ChassisNoPhotoGraph"));
					responseModel.setUploadPhotoGraph((String) row.get("UploadPhotoGraph"));
					responseModel.setUploadPhotoGraph1((String) row.get("UploadPhotoGraph1"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}

	@Override
	public JobCardCreateResponse cancelJobCard(String authorizationHeader, String userCode,
			JobCardCancelRequest requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("cancelSparePO invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		BigInteger userId = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			jobCardEntity hdrDBEntity = null;
			if (requestModel.getRoId() != null) {
				String sqlQuery = "select * from SV_RO_HDR(NOLOCK) where ro_id =:roId";
				query = session.createNativeQuery(sqlQuery).addEntity(jobCardEntity.class);
				query.setParameter("roId", requestModel.getRoId());
				hdrDBEntity = (jobCardEntity) query.uniqueResult();
				if (hdrDBEntity != null) {
					if (!hdrDBEntity.getJobCardStatus().equals("Open")) {
						// dealer can not edit the Machine Po
						isSuccess = false;
						responseModel.setMsg("Job Card already." + hdrDBEntity.getJobCardStatus());
					}
					hdrDBEntity.setCancelReson(requestModel.getCancelRemarks());
					hdrDBEntity.setCancelDate(new Date());
					hdrDBEntity.setJobCardStatus("Cancelled");
					hdrDBEntity.setClosedBy(userCode);

					session.merge(hdrDBEntity);
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Job Card  Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("RoId Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setMsg("Job Card Canceled Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Canceled Job Card.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}
		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveEditPartDetails(String authorizationHeader, String userCode,
			EditPartsRequest partRequest, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveEditPartDetails invoked.." + userCode);
			logger.debug(partRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		BigInteger userId = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			PartDetailsEntity hdrDBEntity = null;
			if (partRequest != null) {
				String sqlQuery = "select * from [PA_WK_REQ_DTL] (NOLOCK) where Requisition_Dtl_Id =:id";
				query = session.createNativeQuery(sqlQuery).addEntity(PartDetailsEntity.class);
				query.setParameter("id", partRequest.getRequisitionId());
				hdrDBEntity = (PartDetailsEntity) query.uniqueResult();
				if (hdrDBEntity != null) {
					hdrDBEntity.setBillableTypeId(partRequest.getBileableId());
					hdrDBEntity.setOem(partRequest.getOem());
					hdrDBEntity.setCustomer(partRequest.getCustomer());
					hdrDBEntity.setDealer(partRequest.getDealer());
					hdrDBEntity.setInsurance(partRequest.getInsurance());					
					session.update(hdrDBEntity);
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Requisition DTL Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("partRequest is Empty.");
			}
			if (isSuccess) {
				transaction.commit();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setMsg("Job Card Edit Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Edit Job Card.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveEditCustomerDetails(String authorizationHeader, String userCode,
			EditCustomberVoiceRequest custRequest, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveEditCustomerDetails invoked.." + userCode);
			logger.debug(custRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		BigInteger userId = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			CustomberVoiceEntity hdrDBEntity = null;
			if (custRequest != null) {
				String sqlQuery = "select * from SV_CUSTOMER_VOICE (NOLOCK) where ID =:id";
				query = session.createNativeQuery(sqlQuery).addEntity(CustomberVoiceEntity.class);
				query.setParameter("id", custRequest.getCustomerId());
				hdrDBEntity = (CustomberVoiceEntity) query.uniqueResult();
				if (hdrDBEntity != null) {
					hdrDBEntity.setActivityToBeDone(custRequest.getActivityToBeDone());
					session.merge(hdrDBEntity);
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Customer DTL Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("customerRequest is Empty.");
			}
			if (isSuccess) {
				transaction.commit();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setMsg("Job Card Customer Edit Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Customer Edit Job Card.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveEditOutSideLabourDetails(String authorizationHeader, String userCode, Integer branchId, BigInteger customerId,
			List<OutSiderLabourChargeRequest> outSideLabourRequest, Integer roId, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveEditOutSideLabourDetails invoked.." + userCode);
			logger.debug(outSideLabourRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		OutSiderLabourChargeEntity outerLabourChargeEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlDeleteQuery = "Delete from SV_RO_OUTSIDE_LBR_DTL where RO_Id =:roId";
			query = session.createNativeQuery(sqlDeleteQuery);
			query.setParameter("roId", roId);
			query.executeUpdate();
			for (OutSiderLabourChargeRequest request : outSideLabourRequest) {
				
				if(request.getRate().compareTo(BigDecimal.ZERO) > 0) {
					
					LabourGstCalcDtl bean = getOuterLabourGstDetail(branchId ,customerId.intValue(),request.getLabourCodeId() ,request.getRate(), request.getHour());
					
					if(bean!=null) {
						request.setCgst(bean.getCgst());
						request.setIgst(bean.getIgst());
						request.setSgst(bean.getSgst());
						request.setCgstAmt(bean.getCgstAmt());
						request.setIgstAmt(bean.getIgstAmt());
						request.setSgstAmt(bean.getSgstAmt());
						request.setTotalGst(bean.getTotalGst());
						request.setTotalAmt(bean.getTotalAmt());
					}	
				}

				outerLabourChargeEntity = mapper.map(request, OutSiderLabourChargeEntity.class, "outerLabourCharge");
				logger.debug(outerLabourChargeEntity.toString());
				if (outerLabourChargeEntity != null) {
					isSuccess = true;

				}
				Date currDate = new Date();
				if (isSuccess) {
					BigInteger bi = BigInteger.valueOf(roId.intValue());
					outerLabourChargeEntity.setRoId(bi);
					outerLabourChargeEntity.setCreatedDate(new Date());
					outerLabourChargeEntity.setCreatedBy(userCode);
					session.save(outerLabourChargeEntity);
				} else {
					responseModel.setMsg("Something went wrong !!!");
				}

				if (isSuccess) {

					transaction.commit();
					session.getTransaction().begin();
				}

			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setMsg("Job Card outSideLabour Edit Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While outSideLabour Edit Job Card.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public JobCardCreateResponse saveEditLabourDetails(String authorizationHeader, String userCode,Integer branchId, BigInteger customerId,
			List<LabourChargeRequest> editLabourChargeRequest, Integer jobCardId, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveEditLabourDetails invoked.." + userCode);
			logger.debug(editLabourChargeRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		LabourChargeEntity labourChargeEntity = null;
		JobCardCreateResponse responseModel = new JobCardCreateResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlDeleteQuery = "Delete from SV_RO_LBR_DTL where RO_Id =:roId";
			query = session.createNativeQuery(sqlDeleteQuery);
			query.setParameter("roId", jobCardId);
			query.executeUpdate();
			for (LabourChargeRequest request : editLabourChargeRequest) {
				
				
				if(request.getRate().compareTo(BigDecimal.ZERO) > 0) {
					LabourGstCalcDtl bean = getLabourGstDetail(branchId ,customerId.intValue() ,request.getLabourCodeId(),request.getRate(), request.getHour());
					if(bean!=null) {
						request.setCgst(bean.getCgst());
						request.setIgst(bean.getIgst());
						request.setSgst(bean.getSgst());
						request.setCgstAmt(bean.getCgstAmt());
						request.setIgstAmt(bean.getIgstAmt());
						request.setSgstAmt(bean.getSgstAmt());
						request.setTotalGst(bean.getTotalGst());
						request.setTotalAmt(bean.getTotalAmt());
					}
				}	

				labourChargeEntity = mapper.map(request, LabourChargeEntity.class, "labourCharge");
				logger.debug(labourChargeEntity.toString());
				if (labourChargeEntity != null) {
					isSuccess = true;

				}
				Date currDate = new Date();
				if (isSuccess) {
					BigInteger bi = BigInteger.valueOf(jobCardId.intValue());
					labourChargeEntity.setRoId(bi);
					labourChargeEntity.setCreatedDate(new Date());
					labourChargeEntity.setCreatedBy(userCode);
					session.save(labourChargeEntity);
				} else {
					responseModel.setMsg("Something went wrong !!!");
				}

				if (isSuccess) {

					transaction.commit();
					session.getTransaction().begin();
				}

			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setMsg("Job Card outSideLabour Edit Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While outSideLabour Edit Job Card.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public MessageCodeResponse updateDocuments(String userCode, Integer roId, MultipartFile chassisNoPic,
			MultipartFile hourMeterPic, MultipartFile pic1, MultipartFile pic2) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateDocuments invoked.." + userCode);
			logger.debug(roId.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		MessageCodeResponse responseModel = new MessageCodeResponse();
		boolean isSuccess = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			jobCardDetailsEntity jobCardDetailsEntity = null;
			if (roId != null) {
				isSuccess = true;
			}
			Date currDate = new Date();
			if (isSuccess) {
				String sqlQuery = "SELECT * FROM SV_RO_DTL(NOLOCK) WHERE RO_ID = :roId";
				query = session.createNativeQuery(sqlQuery).addEntity(jobCardDetailsEntity.class);
				query.setParameter("roId", roId);
				jobCardDetailsEntity = (jobCardDetailsEntity) query.uniqueResult();
				String pic = null;
				String picOther = null;
				if (jobCardDetailsEntity != null) {
					// Create the upload directory if it doesn't exist
					if (chassisNoPic == null || chassisNoPic.isEmpty() && hourMeterPic == null
							|| hourMeterPic.isEmpty()) {
						responseModel.setMessage("File cannot be null or empty.");
						responseModel.setCode("500");
						return responseModel;
					}
					String chassis = utils.cleanFilePath(chassisNoPic.getOriginalFilename());
					String hourMeter = utils.cleanFilePath(hourMeterPic.getOriginalFilename());
					if (pic1 != null) {
						pic = utils.cleanFilePath(pic1.getOriginalFilename());
					}
					if (pic2 != null) {
						picOther = utils.cleanFilePath(pic2.getOriginalFilename());
					}
					Path uploadPath = Paths.get(uploadDir);
					// Create the upload directory if it doesn't exist
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}
					// Save the file to the server
					Path chassisfilePath = uploadPath.resolve(chassis);
					Files.copy(chassisNoPic.getInputStream(), chassisfilePath);
					Path hourMeterFilePath = uploadPath.resolve(hourMeter);
					Files.copy(hourMeterPic.getInputStream(), hourMeterFilePath);
					if (pic1 != null) {
						Path picFilePath = uploadPath.resolve(pic);
						Files.copy(pic1.getInputStream(), picFilePath);
					}
					if (pic2 != null) {
						Path picOtherFilePath = uploadPath.resolve(picOther);
						Files.copy(pic2.getInputStream(), picOtherFilePath);
					}

					jobCardDetailsEntity.setChassisNoPhotoGraph(chassisNoPic.getOriginalFilename());
					jobCardDetailsEntity.setHourMeterPhotoGraph(hourMeterPic.getOriginalFilename());
					if (pic1 != null) {
						jobCardDetailsEntity.setUploadPhotoGraph(pic1.getOriginalFilename());
					} else {
						jobCardDetailsEntity.setUploadPhotoGraph(jobCardDetailsEntity.getUploadPhotoGraph());
					}
					if (pic2 != null) {
						jobCardDetailsEntity.setUploadPhotoGraph1(pic2.getOriginalFilename());
					} else {
						jobCardDetailsEntity.setUploadPhotoGraph1(jobCardDetailsEntity.getUploadPhotoGraph1());
					}
					responseModel.setCode("200");
					responseModel.setMessage("Sucess");
					session.merge(jobCardDetailsEntity);
				}
			} else {
				responseModel.setMessage("Something went wrong !!!");
			}

			if (isSuccess) {

				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMessage(ex.toString());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMessage("Document upload Successfully.");
				responseModel.setCode("201");
			} else {
				responseModel.setCode("500");
			}

		}
		return responseModel;
	}

	@Override
	public List<JobCardAndStausResponse> getJobCardAndStausList(String userCode, BigInteger vinId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getJobCardAndStausList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobCardAndStausResponse responseListModel = null;
		List<JobCardAndStausResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select RONumber,Status from SV_RO_HDR(NOLOCK) where Status ='OPEN' and Vin_Id = :vinId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			query.setParameter("vinId", vinId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardAndStausResponse();
				responseModelList = new ArrayList<JobCardAndStausResponse>();
				JobCardAndStausResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardAndStausResponse();
					responseModel.setRoNumber((String) row.get("RONumber"));
					responseModel.setStatus((String) row.get("Status"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public MessageCodeResponse updateBookingAndQuotationStatus(String authorizationHeader, String userCode,
			ServiceBookingAndQuoationStatusRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDocsFiles invoked.." + userCode);
		}
		MessageCodeResponse response = new MessageCodeResponse();
		Query query = null;
		Session session = null;
		Integer recordCount = 0;
		String sqlQuery = "EXEC [SV_RO_BOOKING_QUOTATION_UPDATE_STATUS] :vinId,:FLAG ";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("vinId", requestModel.getVinId());
			query.setParameter("BookingFlag", requestModel.getServiceBookingFlag());
			query.setParameter("QuotationFlag", requestModel.getQuotationFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			response.setCode("201");
			response.setMessage("Updated successfully !!!");
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		}
		return response;
	}

	@Override
	public List<JobCardAndStausResponse> getJobCardAndStausListByChassis(String userCode, String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("getJobCardAndStausListByChassis invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		JobCardAndStausResponse responseListModel = null;
		List<JobCardAndStausResponse> responseModelList = null;

		String sqlQuery = "select RONumber,Status from SV_RO_HDR(NOLOCK) where ChassisNumber = :ChassisNumber order by RONumber desc";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("usercode", null);
			query.setParameter("ChassisNumber", chassis);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardAndStausResponse();
				responseModelList = new ArrayList<>();
				JobCardAndStausResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardAndStausResponse();
					responseModel.setRoNumber((String) row.get("RONumber"));
					responseModel.setStatus((String) row.get("Status"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<JobChassisDetailsResponse> fetchChassisDetailsByChassis(String authorizationHeader, String userCode,
			String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchChassisDetailsByVinId invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		JobChassisDetailsResponse responseListModel = null;
		List<JobChassisDetailsResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_PDI] :Chassis_number, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Chassis_number", chassis);
			query.setParameter("flag", "HEADER");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobChassisDetailsResponse();
				responseModelList = new ArrayList<JobChassisDetailsResponse>();
				JobChassisDetailsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobChassisDetailsResponse();
					responseModel.setVinId((BigInteger) row.get("vinId"));
					responseModel.setProfitcenter((String) row.get("PC_desc"));
					responseModel.setChassisNo((String) row.get("chassisNumber"));
					responseModel.setEngineNo((String) row.get("EngineNumber"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setItemDescription((String) row.get("MODEL_NAME"));
					responseModel.setInvoiceId((BigInteger) row.get("invoiceId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((Date) row.get("InvoiceDate"));
					responseModel.setBookingId((BigInteger) row.get("bookingId"));
					responseModel.setServiceTypeId((BigInteger) row.get("service_type_id"));
					responseModel.setServiceRepairTypeId((BigInteger) row.get("service_repair_type_id"));
					responseModel.setCoustomerId((BigInteger) row.get("customerId"));

					responseModel.setCompletePdi((Integer) row.get("completedPdi"));
					responseModel.setPendingPdi((Integer) row.get("pendingPdi"));

					responseModel.setBookingNo((String) row.get("bookingno"));
					responseModel.setBookingDate((Date) row.get("booking_date"));
					responseModel.setPreviousHour((BigInteger) row.get("previous_hour"));
					responseModel.setLrDate((Date) row.get("LRDATE"));
					responseModel.setLrNumber((String) row.get("LRNumber"));
					responseModel.setPdiDoneBy((String) row.get("PDIDONEBY"));
					responseModel.setQuotationId((Integer) row.get("Quotation_Id"));
					responseModel.setQuotationDate((Date) row.get("Quotation_Date"));
					responseModel.setQuotationNumber((String) row.get("QuotationNumber"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setMobileNo((String) row.get("customermobile"));
					responseModel.setRepairOrderType((String) row.get("repairOrderType"));
					responseModel.setServiceType((String) row.get("serviceType"));
					responseModel.setInwardId((Integer) row.get("inwardId"));
					responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));
					responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
					responseModel.setFlipMakeNumber((String) row.get("FIPMakeNumber"));
					responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));
					responseModel.setFrontTyerMakeRHNumber((String) row.get("FrontTyerMakeRHNumber"));
					responseModel.setFrontTyerMakeLHNumber((String) row.get("FrontTyerMakeLHNumber"));
					responseModel.setRearTyerMakeRHNumber((String) row.get("RearTyerMakeRHNumber"));
					responseModel.setRearTypeMakeLHNumber((String) row.get("RearTyerMakeLHNumber"));
					responseModel.setRepresentativeType((String) row.get("RepresentativeType"));
					responseModel.setRepresentativeName((String) row.get("RepresentativeName"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<PdiCheckListResponse> getAllCheckpointOfPDI(String userCode, String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAllCheckpointOfPDI invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		PdiCheckListResponse responseListModel = null;
		List<PdiCheckListResponse> responseModelList = null;

		String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_PDI] :Chassis_number, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Chassis_number", chassis);
			query.setParameter("flag", "DETAIL");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PdiCheckListResponse();
				responseModelList = new ArrayList<PdiCheckListResponse>();
				PdiCheckListResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PdiCheckListResponse();
					responseModel.setPdiDtlId((BigInteger) row.get("pdiDtlId"));
					responseModel.setCheckpointDesc((String) row.get("checkpointDesc"));
					responseModel.setDefaultTick((String) row.get("defaultTick"));
					responseModel.setAggregateId((BigInteger) row.get("aggregateId"));
					responseModel.setAggregate((String) row.get("aggregate"));
					responseModel.setAggregateSequenceNo((Integer) row.get("aggregateSequenceNo"));
					responseModel.setCheckpointSequenceNo((Integer) row.get("checkpointSequenceNo"));
					responseModel.setRemark((String) row.get("remark"));
					responseModel.setCheckpointId((BigInteger) row.get("checkpointId"));
					responseModel.setObservedSpecification((String) row.get("observedSpecification"));
					responseModel.setSpecification((String) row.get("specification"));
					responseModelList.add(responseModel);

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<JobChassisDetailsResponse> fetchOutWordChassisDetailsByChassis(String authorizationHeader,
			String userCode, String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchChassisDetailsByVinId invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		JobChassisDetailsResponse responseListModel = null;
		List<JobChassisDetailsResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_PDI]  :Chassis_number, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Chassis_number", chassis);
			query.setParameter("flag", "HEADER");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobChassisDetailsResponse();
				responseModelList = new ArrayList<JobChassisDetailsResponse>();
				JobChassisDetailsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobChassisDetailsResponse();
					responseModel.setVinId((BigInteger) row.get("vinId"));
					responseModel.setProfitcenter((String) row.get("PC_desc"));
					responseModel.setChassisNo((String) row.get("chassisNumber"));
					responseModel.setEngineNo((String) row.get("EngineNumber"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setItemDescription((String) row.get("MODEL_NAME"));
					responseModel.setInvoiceId((BigInteger) row.get("invoiceId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((Date) row.get("InvoiceDate"));
					responseModel.setBookingId((BigInteger) row.get("bookingId"));
					responseModel.setServiceTypeId((BigInteger) row.get("service_type_id"));
					responseModel.setServiceRepairTypeId((BigInteger) row.get("service_repair_type_id"));
					responseModel.setCoustomerId((BigInteger) row.get("customerId"));

					responseModel.setCompletePdi((Integer) row.get("completedPdi"));
					responseModel.setPendingPdi((Integer) row.get("pendingPdi"));

					responseModel.setBookingNo((String) row.get("bookingno"));
					responseModel.setBookingDate((Date) row.get("booking_date"));
					responseModel.setPreviousHour((BigInteger) row.get("previous_hour"));
					responseModel.setLrDate((Date) row.get("LRDATE"));
					responseModel.setLrNumber((String) row.get("LRNumber"));
					responseModel.setPdiDoneBy((String) row.get("PDIDONEBY"));
					responseModel.setQuotationId((Integer) row.get("Quotation_Id"));
					responseModel.setQuotationDate((Date) row.get("Quotation_Date"));
					responseModel.setQuotationNumber((String) row.get("QuotationNumber"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setMobileNo((String) row.get("customermobile"));
					responseModel.setRepairOrderType((String) row.get("repairOrderType"));
					responseModel.setServiceType((String) row.get("serviceType"));
					responseModel.setInwardId((Integer) row.get("inwardId"));
					responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));
					responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
					responseModel.setFlipMakeNumber((String) row.get("FIPMakeNumber"));
					responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));
					responseModel.setFrontTyerMakeRHNumber((String) row.get("FrontTyerMakeRHNumber"));
					responseModel.setFrontTyerMakeLHNumber((String) row.get("FrontTyerMakeLHNumber"));
					responseModel.setRearTyerMakeRHNumber((String) row.get("RearTyerMakeRHNumber"));
					responseModel.setRearTypeMakeLHNumber((String) row.get("RearTyerMakeLHNumber"));
					responseModel.setRepresentativeType((String) row.get("RepresentativeType"));
					responseModel.setRepresentativeName((String) row.get("RepresentativeName"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<PdiCheckListResponse> getAllCheckpointOfOutWord(String userCode, String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAllCheckpointOfPDI invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		PdiCheckListResponse responseListModel = null;
		List<PdiCheckListResponse> responseModelList = null;

		String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_OUTWARDPDI] :Chassis_number, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Chassis_number", chassis);
			query.setParameter("flag", "DETAIL");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PdiCheckListResponse();
				responseModelList = new ArrayList<PdiCheckListResponse>();
				PdiCheckListResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PdiCheckListResponse();
					responseModel.setPdiDtlId((BigInteger) row.get("pdiDtlId"));
					responseModel.setCheckpointDesc((String) row.get("checkpointDesc"));
					responseModel.setDefaultTick((String) row.get("defaultTick"));
					responseModel.setAggregateId((BigInteger) row.get("aggregateId"));
					responseModel.setAggregate((String) row.get("aggregate"));
					responseModel.setAggregateSequenceNo((Integer) row.get("aggregateSequenceNo"));
					responseModel.setCheckpointSequenceNo((Integer) row.get("checkpointSequenceNo"));
					responseModel.setRemark((String) row.get("remark"));
					responseModel.setCheckpointId((BigInteger) row.get("checkpointId"));
					responseModel.setObservedSpecification((String) row.get("observedSpecification"));
					responseModel.setSpecification((String) row.get("specification"));
					responseModelList.add(responseModel);

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}

		}
		return responseModelList;
	}

	@Override
	public List<JobChassisDetailsResponse> fetchInstallationChassisDetailsByChassis(String authorizationHeader,
			String userCode, String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInstallationChassisDetailsByChassis invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		JobChassisDetailsResponse responseListModel = null;
		List<JobChassisDetailsResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_INSTALLATION]  :Chassis_number, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Chassis_number", chassis);
			query.setParameter("flag", "HEADER");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobChassisDetailsResponse();
				responseModelList = new ArrayList<JobChassisDetailsResponse>();
				JobChassisDetailsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobChassisDetailsResponse();
					responseModel.setVinId((BigInteger) row.get("vinId"));
					responseModel.setProfitcenter((String) row.get("PC_desc"));
					responseModel.setChassisNo((String) row.get("chassisNumber"));
					responseModel.setEngineNo((String) row.get("EngineNumber"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setItemDescription((String) row.get("MODEL_NAME"));
					responseModel.setInvoiceId((BigInteger) row.get("invoiceId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((Date) row.get("InvoiceDate"));
					responseModel.setBookingId((BigInteger) row.get("bookingId"));
					responseModel.setServiceTypeId((BigInteger) row.get("service_type_id"));
					responseModel.setServiceRepairTypeId((BigInteger) row.get("service_repair_type_id"));
					responseModel.setCoustomerId((BigInteger) row.get("customerId"));

					responseModel.setCompletePdi((Integer) row.get("completedPdi"));
					responseModel.setPendingPdi((Integer) row.get("pendingPdi"));

					responseModel.setBookingNo((String) row.get("bookingno"));
					responseModel.setBookingDate((Date) row.get("booking_date"));
					responseModel.setPreviousHour((BigInteger) row.get("previous_hour"));
					responseModel.setLrDate((Date) row.get("LRDATE"));
					responseModel.setLrNumber((String) row.get("LRNumber"));
					responseModel.setPdiDoneBy((String) row.get("PDIDONEBY"));
					responseModel.setQuotationId((Integer) row.get("Quotation_Id"));
					responseModel.setQuotationDate((Date) row.get("Quotation_Date"));
					responseModel.setQuotationNumber((String) row.get("QuotationNumber"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setMobileNo((String) row.get("customermobile"));
					responseModel.setRepairOrderType((String) row.get("repairOrderType"));
					responseModel.setServiceType((String) row.get("serviceType"));
					responseModel.setInwardId((Integer) row.get("inwardId"));
					responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));
					responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
					responseModel.setFlipMakeNumber((String) row.get("FIPMakeNumber"));
					responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));
					responseModel.setFrontTyerMakeRHNumber((String) row.get("FrontTyerMakeRHNumber"));
					responseModel.setFrontTyerMakeLHNumber((String) row.get("FrontTyerMakeLHNumber"));
					responseModel.setRearTyerMakeRHNumber((String) row.get("RearTyerMakeRHNumber"));
					responseModel.setRearTypeMakeLHNumber((String) row.get("RearTyerMakeLHNumber"));
					responseModel.setRepresentativeType((String) row.get("RepresentativeType"));
					responseModel.setRepresentativeName((String) row.get("RepresentativeName"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<InstallationChecklistResponse> getAllCheckpointOfInstallation(String userCode, String chassis) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAllCheckpointOfInstallation invoked.." + chassis);
		}
		Query query = null;
		Session session = null;
		jobInventryChecklistReponse responseListModel = null;
		List<InstallationChecklistResponse> responseModelList = null;

		String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_INSTALLATION] :Chassis_number, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Chassis_number", chassis);
			query.setParameter("flag", "DETAIL");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new jobInventryChecklistReponse();
				responseModelList = new ArrayList<InstallationChecklistResponse>();
				InstallationChecklistResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InstallationChecklistResponse();
					responseModel.setInstallDtlId((Integer) row.get("installDtlId"));
					responseModel.setId((BigInteger) row.get("ID"));
					responseModel.setCheckpointDesc((String) row.get("INS_CHECK_POINT"));
					responseModel.setStatus((String) row.get("ISACTIVE"));
					responseModelList.add(responseModel);

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<AutoSelectSrvCategoryAndTypeResponse> getAutoSelectSrvCategoryAndTypeResponse(String userCode,
			Integer vin_Id, Integer kMReading) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAutoSelectSrvCategoryAndTypeResponse invoked.." + vin_Id + "kMReading" + kMReading);
		}
		Query query = null;
		Session session = null;
		AutoSelectSrvCategoryAndTypeResponse responseListModel = null;
		List<AutoSelectSrvCategoryAndTypeResponse> responseModelList = new ArrayList<AutoSelectSrvCategoryAndTypeResponse>();;

		String sqlQuery = "EXEC [AutoSelectSrvCategoryAndType] :Vin_Id, :KMReading";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Vin_Id", vin_Id);
			query.setParameter("KMReading", kMReading);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new AutoSelectSrvCategoryAndTypeResponse();
				
				AutoSelectSrvCategoryAndTypeResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new AutoSelectSrvCategoryAndTypeResponse();
					responseModel.setServiceTypeId((BigInteger) row.get("Service_Type_ID"));
					responseModel.setServiceTypeOemId((BigInteger) row.get("Service_Type_oem_Id"));
					responseModel.setSrvTypeCode((String) row.get("SrvTypeCode"));
					responseModel.setSrvTypeDesc((String) row.get("SrvTypeDesc"));
					responseModel.setServiceCategoryId((Integer) row.get("Service_Category_ID"));
					responseModel.setCategoryCode((String) row.get("CategoryCode"));
					responseModel.setCategoryDesc((String) row.get("CategoryDesc"));
					responseModelList.add(responseModel);

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public void updateSrvBookingAndQuatationStatus(Integer roId) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateSrvBookingAndQuatationStatus invoked.." + roId);
		}
		Query query = null;
		Session session = null;

		String sqlQuery = "EXEC [SV_RO_BOOKING_QUOTATION_UPDATE_STATUS]:roId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("roId", roId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public HashMap<String, Object> fetchActivityPlanDetails(String userCode,String profitcenter) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityPlanDetails invoked..");
		}
		Query query = null;
		Session session = null;
		HashMap<String, Object> response = new HashMap<>();

		String sqlQuery = "EXEC SV_ACTIVITY_FOR_JOBCARD :userCode,:profitcenter ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("profitcenter", profitcenter);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				response.put("ActivityDetails", data);
//				for (Object object : data) {
//					
//
//					
//				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return response;
	}

	@Override
	public MasterDataModelResponse getChassisDetailsForVehicle(String userCode, String chassis_no, String Isfor) {
		if (logger.isDebugEnabled()) {
			logger.debug("getChassisDetailsForVehicle invoked.." + chassis_no + "Flag" + Isfor);
		}

		Query query = null;
		Session session = null;
		MasterDataModelResponse obj = null;
		String sqlQuery = "EXEC [GetVehicleDetailsForRO] :chassis_no, :Isfor ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("chassis_no", chassis_no);
			query.setParameter("Isfor", Isfor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					obj = new MasterDataModelResponse();
					obj.setChassisNo(chassis_no);
//					obj.setVinId((Integer) row.get("VinNumber") != null ? (Integer) row.get("VinNumber") : null);
//					obj.setOmNumber((String) row.get("omNo") != null ? (String) row.get("omNo") : null);
//					obj.setRegistrationNo((String) row.get("RegistrationNo") != null ? (String) row.get("RegistrationNo") : null);
//					obj.setDistributionChannel((String) row.get("channel") != null ? (String) row.get("channel") : null);
//					obj.setEngineNo((String) row.get("EngineNo") != null ? (String) row.get("EngineNo") : null);
//					obj.setModelGroupCode((String) row.get("ModelGroupCode") != null ? (String) row.get("ModelGroupCode") : null);
//					obj.setModelGroupDesc((String) row.get("ModelGroupDesc") != null ? (String) row.get("ModelGroupDesc") : null);
//					obj.setModelVariantDesc((String) row.get("ModelVariantDesc") != null ? (String) row.get("ModelVariantDesc") : null);
//					obj.setModelFamilyDesc((String) row.get("ModelFamilyDesc") != null ? (String) row.get("ModelFamilyDesc") : null);
//					obj.setFscBooklet((String) row.get("FSC_Booklet") != null ? (String) row.get("FSC_Booklet") : null);
//					obj.setSeatCapacity((Integer) row.get("SeatCapacity") != null ? (Integer) row.get("SeatCapacity") : null);
//					obj.setInstallationDate((Date) row.get("installation_date"));
//					obj.setProfitcenter((String) row.get("ProfitCenter") != null ? (String) row.get("ProfitCenter") : null);
//					obj.setPDIDoneStatus((String)row.get("ISPDIDONE"));
//					obj.setOdometerReading((String) row.get("VALTN_ODMETR_REDNG") != null ? (String) row.get("VALTN_ODMETR_REDNG") : null);
//					obj.setMfgInvoiceDate((Date) row.get("mfgInvoiceDate"));
//					obj.setOemPriviledgeCust((String) row.get("OEm_PRIVILEGE_CUSt") != null ? (String) row.get("OEm_PRIVILEGE_CUSt") : null);
//					obj.setSsiCategory((String) row.get("SSICategory") != null ? (String) row.get("SSICategory") : null);
//					obj.setQsiCategory((String) row.get("IQSCategory") != null ? (String) row.get("IQSCategory") : null);
//					obj.setCsiCategory((String) row.get("CSICategory") != null ? (String) row.get("CSICategory") : null);
//					obj.setRelationMgr((String) row.get("relationship_mgr") != null ? (String) row.get("relationship_mgr") : null);
//					obj.setAvgKMPerDay((String) row.get("AvgKMPerDay") != null ? (String) row.get("AvgKMPerDay") : null);
//					obj.setDeliveryNoteDate((Date) row.get("DeliveryNoteDate"));
//					obj.setVehicleId((String) row.get("vehicle_id") != null ? (String) row.get("vehicle_id") : null);
//					obj.setSoldBy((String) row.get("SoldBy") != null ? (String) row.get("SoldBy") : null);
//					obj.setSaleDate((Date) row.get("SaleDate"));
//					obj.setColorCode((String) row.get("ColorCode") != null ? (String) row.get("ColorCode") : null);
//					obj.setModelDesc((String) row.get("ModelDesc") != null ? (String) row.get("ModelDesc") : null);
//					obj.setRetailDate((Date) row.get("RetailDate"));
//					obj.setModelFamilyId((Integer) row.get("Model_Family_Id") != null ? (Integer) row.get("Model_Family_Id") : null);
//					obj.setLabourDiscountPercentage((BigDecimal) row.get("LabourDiscountPercentage") != null ? (BigDecimal) row.get("LabourDiscountPercentage") : null);
//					obj.setPartDiscountPercentage((BigDecimal) row.get("PastDiscountPercentage") != null ? (BigDecimal) row.get("PastDiscountPercentage") : null);
//					obj.setIsPDIdone((Boolean) row.get("IsPDIDone") != null ? (Boolean) row.get("IsPDIDone") : null);
//					obj.setRefurbishedStatus((Boolean) row.get("IsRefurbished") != null ? (Boolean) row.get("IsRefurbished") : null);
//					obj.setRefurbishedDate((Date) row.get("RefurbishedDate"));
//					obj.setGovtVehicleStatus((Boolean) row.get("IsGovtVehicle") != null ? (Boolean) row.get("IsGovtVehicle") : null);
//					obj.setTheftVehicleStatus((Boolean) row.get("IsTheftVehicle") != null ? (Boolean) row.get("IsTheftVehicle") : null);
//					obj.setTotallyDamagedStatus((Boolean) row.get("IsTotallyDamaged") != null ? (Boolean) row.get("IsTotallyDamaged") : null);
//					obj.setPolicyExpiryDate((Date) row.get("PolicyExpiryDate"));
//					obj.setCustomerId((Integer) row.get("Customer_Id") != null ? (Integer) row.get("Customer_Id") : null);
//					obj.setCustomerName((String) row.get("customername") != null ? (String) row.get("customername") : null);
//					obj.setCustAddLine1((String) row.get("CustAddLine1") != null ? (String) row.get("CustAddLine1") : null);
//					obj.setCustAddLine2((String) row.get("CustAddLine2") != null ? (String) row.get("CustAddLine2") : null);
//					obj.setCustAddLine3((String) row.get("CustAddLine3") != null ? (String) row.get("CustAddLine3") : null);
//					obj.setPinCode((String) row.get("PinCode") != null ? (String) row.get("PinCode") : null);
//					obj.setLocalityName((String) row.get("LocalityName") != null ? (String) row.get("LocalityName") : null);
//					obj.setTehsilDesc((String) row.get("TehsilDesc") != null ? (String) row.get("TehsilDesc") : null);
//					obj.setCityDesc((String) row.get("CityDesc") != null ? (String) row.get("CityDesc") : null);
//					obj.setStateDesc((String) row.get("StateDesc") != null ? (String) row.get("StateDesc") : null);
//					obj.setMobileNumber((String) row.get("MobileNumber") != null ? (String) row.get("MobileNumber") : null);
//					obj.setAlternateMobNo((String) row.get("alternateMobNo") != null ? (String) row.get("alternateMobNo") : null);
//					obj.setFax((String) row.get("Fax") != null ? (String) row.get("Fax") : null);
//					obj.setEmail((String) row.get("Email_id") != null ? (String) row.get("Email_id") : null);
//					obj.setNextServiceDueDate((String) row.get("nextserviceduedate") != null ? (String) row.get("nextserviceduedate") : null);
//					obj.setNextDueService((String) row.get("nextservicedue") != null ? (String) row.get("nextservicedue") : null);
//					obj.setModelCode((String) row.get("ModelCode") != null ? (String) row.get("ModelCode") : null);
//					obj.setDivisionDesc((String) row.get("prod_divDesc") != null ? (String) row.get("prod_divDesc") : null);
//					obj.setSWStartDate((Date) row.get("SWStartDate"));
//					obj.setSWExpiryDate((Date) row.get("SWExpiryDate"));
//					obj.setSWExpiryKm((Integer) row.get("SWExpiryHRs") != null ? (Integer) row.get("SWExpiryHRs") : null);
//					obj.setEwStartDate((Date) row.get("EWStartDate"));
//					obj.setEWExpiryDate((Date) row.get("EWExpiryDate"));
//					obj.setEWExpiryKm((Integer) row.get("EWExpiryHRs") != null ? (Integer) row.get("EWExpiryHRs") : null);
//					obj.setAmcStartDate((Date) row.get("AMCStartDate"));
//					obj.setAmcExpiryDate((Date) row.get("AMCExpiryDate"));
//					obj.setAmcExpiryKm((Integer) row.get("AMCExpiryHRs") != null ? (Integer) row.get("AMCExpiryHRs") : null);
//					obj.setAmcPlan((String) row.get("AMCDescription") != null ? (String) row.get("AMCDescription") : null);
//					obj.setAmcPolicyNo((String) row.get("S_AMCPolicyNo") != null ? (String) row.get("S_AMCPolicyNo") : null);
//					obj.setAmcRegNumber((String) row.get("AMCRegistrationNo") != null ? (String) row.get("AMCRegistrationNo") : null);
//					obj.setRegistrationstatus((String) row.get("RegistrationStatus") != null ? (String) row.get("RegistrationStatus") : null);
//					obj.setCustomerCode((String) row.get("Customer_Code") != null ? (String) row.get("Customer_Code") : null);
//					obj.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber") != null ? (String) row.get("MfgInvoiceNumber") : null);
//					obj.setFcExpiryDate((Date) row.get("FCExpiryDate"));
//					obj.setFcIssueDate((Date) row.get("FCIssueDate"));
//					obj.setFcNumber((String) row.get("FCNumber") != null ? (String) row.get("FCNumber") : null);
//					obj.setApplicationType((String) row.get("ApplicationType") != null ? (String) row.get("ApplicationType") : null);
//					obj.setBodyType((String) row.get("BodyType") != null ? (String) row.get("BodyType") : null);
//					String totalHoursVal = (String) row.get("totalHours");
//					obj.setTotalHours(totalHoursVal != null ? totalHoursVal : "0");
//					obj.setCountry((String) row.get("country") != null ? (String) row.get("country") : null);
//					obj.setAlternateMobNo((String) row.get("alternateMobNo") != null ? (String) row.get("alternateMobNo") : null);
//					obj.setWhatsappmobileno((String) row.get("whatsappmobileno") != null ? (String) row.get("whatsappmobileno") : null);
//					obj.setCustomerType((String) row.get("customerType") != null ? (String) row.get("customerType") : null);
//					obj.setInstallationDate((Date) row.get("installationDate"));
//					obj.setProfitcenter((String) row.get("profitcenter") != null ? (String) row.get("profitcenter") : null);
//					obj.setSeries((String) row.get("series") != null ? (String) row.get("series") : null);
//					obj.setSegment((String) row.get("segment") != null ? (String) row.get("segment") : null);
//					obj.setVariant((String) row.get("variant") != null ? (String) row.get("variant") : null);
//					obj.setItemNo((String) row.get("itemNo") != null ? (String) row.get("itemNo") : null);
//					obj.setItemDesc((String) row.get("itemDesc") != null ? (String) row.get("itemDesc") : null);
//					obj.setStatus((String) row.get("status") != null ? (String) row.get("status") : null);

					if ("PDI".equals(Isfor)) {

						obj.setChassisNo(chassis_no);
						obj.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber"));
						obj.setMfgInvoiceDate((Date) row.get("mfgInvoiceDate"));
						obj.setModelDesc((String) row.get("modelDesc"));
						obj.setEngineNo((String) row.get("EngineNo"));
						obj.setProfitcenter((String) row.get("ProfitCenter"));
						obj.setItemDesc((String) row.get("ItemDescription"));
						obj.setIsPDIdone(true);
						obj.setSeries((String) row.get("series"));
						obj.setSegment((String) row.get("segment"));
						obj.setVariant((String) row.get("variant"));
						obj.setCustomerName((String) row.get("ParentDealerName"));
						obj.setCustAddLine1((String) row.get("dealeraddress1"));
						obj.setCustAddLine2((String) row.get("dealeraddress2"));
						obj.setCustAddLine3((String) row.get("DealerAddress3"));
						obj.setMobileNumber((String) row.get("MobileNumber"));
						obj.setAlternateMobNo((String) row.get("PhoneNumber"));
						obj.setEmail((String) row.get("Dealer_EMail"));
						obj.setCityDesc((String) row.get("dealer_city"));
						obj.setCountry((String) row.get("Dealer_country"));
						obj.setTehsilDesc((String) row.get("Dealer_tehsil"));
						obj.setStateDesc((String) row.get("statedesc"));
					} else {
						obj.setChassisNo(chassis_no);
						obj.setEngineNo((String) row.get("EngineNo") != null ? (String) row.get("EngineNo") : null);
						obj.setVinNumber((String) row.get("VinNumber") != null ? (String) row.get("VinNumber") : null);
						obj.setRegistrationNo(
								(String) row.get("RegistrationNo") != null ? (String) row.get("RegistrationNo") : null);
						obj.setSaleDate((Date) row.get("SaleDate") != null ? (Date) row.get("SaleDate") : null);
						obj.setSoldBy((String) row.get("SoldBy") != null ? (String) row.get("SoldBy") : null);
						obj.setMfgInvoiceNumber(
								(String) row.get("MfgInvoiceNumber") != null ? (String) row.get("MfgInvoiceNumber")
										: null);
						obj.setMfgInvoiceDate(
								(Date) row.get("mfgInvoiceDate") != null ? (Date) row.get("mfgInvoiceDate") : null);
						obj.setInstallationDate(
								(Date) row.get("installation_date") != null ? (Date) row.get("installation_date")
										: null);
						obj.setModelDesc((String) row.get("ModelDesc") != null ? (String) row.get("ModelDesc") : null);
						obj.setModelCode((String) row.get("ModelCode") != null ? (String) row.get("ModelCode") : null);
						obj.setProfitcenter(
								(String) row.get("ProfitCenter") != null ? (String) row.get("ProfitCenter") : null);
						obj.setItemDesc(
								(String) row.get("ItemDescription") != null ? (String) row.get("ItemDescription")
										: null);
						obj.setSeries((String) row.get("series") != null ? (String) row.get("series") : null);
						obj.setSegment((String) row.get("segment") != null ? (String) row.get("segment") : null);
						obj.setVariant((String) row.get("variant") != null ? (String) row.get("variant") : null);
						obj.setItemNo((String) row.get("itemNo") != null ? (String) row.get("itemNo") : null);
						obj.setItemDesc((String) row.get("itemDesc") != null ? (String) row.get("itemDesc") : null);
						obj.setSWStartDate(
								(Date) row.get("SWStartDate") != null ? (Date) row.get("SWStartDate") : null);
						obj.setSWExpiryDate(
								(Date) row.get("SWExpiryDate") != null ? (Date) row.get("SWExpiryDate") : null);
						obj.setSWExpiryHrs(
								(Integer) row.get("SWExpiryHRs") != null ? (Integer) row.get("SWExpiryHRs") : null);
						obj.setEwStartDate(
								(Date) row.get("EWStartDate") != null ? (Date) row.get("EWStartDate") : null);
						obj.setEWExpiryDate(
								(Date) row.get("EWExpiryDate") != null ? (Date) row.get("EWExpiryDate") : null);
						obj.setEWExpiryHrs(
								(Integer) row.get("EWExpiryHRs") != null ? (Integer) row.get("EWExpiryHRs") : null);
						obj.setAmcExpiryDate(
								(Date) row.get("AMCStartDate") != null ? (Date) row.get("AMCStartDate") : null);
						obj.setAmcStartDate(
								(Date) row.get("AMCStartDate") != null ? (Date) row.get("AMCStartDate") : null);
						obj.setAmcExpiryKm(
								(Integer) row.get("AMCExpiryHRs") != null ? (Integer) row.get("AMCExpiryHRs") : null);
						obj.setCustomerName(
								(String) row.get("ParentDealerName") != null ? (String) row.get("ParentDealerName")
										: null);
						obj.setCustAddLine1(
								(String) row.get("CustAddLine1") != null ? (String) row.get("CustAddLine1") : null);
						obj.setCustAddLine2(
								(String) row.get("CustAddLine2") != null ? (String) row.get("CustAddLine2") : null);
						obj.setCustAddLine3(
								(String) row.get("CustAddLine3") != null ? (String) row.get("CustAddLine3") : null);
						obj.setWhatsappmobileno(
								(String) row.get("whatsappmobileno") != null ? (String) row.get("whatsappmobileno")
										: null);
						obj.setMobileNumber(
								(String) row.get("MobileNumber") != null ? (String) row.get("MobileNumber") : null);
						obj.setAlternateMobNo(
								(String) row.get("alternateMobNo") != null ? (String) row.get("alternateMobNo") : null);
						obj.setEmail((String) row.get("Email_id") != null ? (String) row.get("Email_id") : null);
						obj.setCityDesc((String) row.get("CityDesc") != null ? (String) row.get("CityDesc") : null);
						obj.setCountry((String) row.get("country") != null ? (String) row.get("country") : null);
						obj.setTehsilDesc(
								(String) row.get("TehsilDesc") != null ? (String) row.get("TehsilDesc") : null);
						obj.setStateDesc((String) row.get("StateDesc") != null ? (String) row.get("StateDesc") : null);
						obj.setPinCode((String) row.get("PinCode") != null ? (String) row.get("PinCode") : null);
						obj.setCustomerType(
								(String) row.get("customerType") != null ? (String) row.get("customerType") : null);
						obj.setCustomerName(
								(String) row.get("customername") != null ? (String) row.get("customername") : null);
						obj.setDistrict((String) row.get("District") != null ? (String) row.get("District") : null);

					}

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);

		} finally {
			session.close();
		}
		return obj;
	}

	@Override
	public List<ActivityPlanModelResponse> fetchActivityList(String authorizationHeader, String userCode, Integer roId,
			int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		ActivityPlanModelResponse responseListModel = null;
		List<ActivityPlanModelResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new ActivityPlanModelResponse();
				responseModelList = new ArrayList<ActivityPlanModelResponse>();
				ActivityPlanModelResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityPlanModelResponse();
					responseModel.setActivityType((String) row.get("activityType"));
					responseModel.setActivityPlan((String) row.get("ActivityPlan"));
					responseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}
		return responseModelList;
	}

	@Override
	public List<JobCardServiceHistoryModelResponse> fetchServiceHistoryList(String authorizationHeader, String userCode,
			Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchServiceHistoryList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		JobCardServiceHistoryModelResponse responseListModel = null;
		List<JobCardServiceHistoryModelResponse> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = " EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID,:FLAG ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new JobCardServiceHistoryModelResponse();
				responseModelList = new ArrayList<JobCardServiceHistoryModelResponse>();
				JobCardServiceHistoryModelResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardServiceHistoryModelResponse();
					responseModel.setRoId((BigInteger) row.get("ro_id"));
					responseModel.setRoNumber((String) row.get("RONumber"));
					responseModel.setOpeningDate((Date) row.get("OpeningDate"));
					responseModel.setSrvTypeDesc((String) row.get("SrvTypeDesc"));
					responseModel.setTotalHour((BigInteger) row.get("Total_Hour"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}
		return responseModelList;
	}

	@Override
	public Map<String, Object> fetchServiceActivityByChassis(String userCode, String chassisNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchServiceActivityByChassis invoked.." + userCode + "::::::::::chassisNumber::::::::::"
					+ chassisNumber);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_GET_JOBCARD_SERVICE_HISTORY_BY_CHASSIS] :userCode,:chassisNo ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("chassisNo", chassisNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("history", data);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;
	}

	@Override
	public List<PdiCheckListResponse> getAllCheckpointOfPDIByRoId(String authorizationHeader, String userCode,
			Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAllCheckpointOfPDIByRoId invoked.." + roId);
		}
		Query query = null;
		Session session = null;
		PdiCheckListResponse responseListModel = null;
		List<PdiCheckListResponse> responseModelList = null;

		String sqlQuery = "EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID, :FLAG";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PdiCheckListResponse();
				responseModelList = new ArrayList<PdiCheckListResponse>();
				PdiCheckListResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PdiCheckListResponse();
					responseModel.setPdiDtlId((BigInteger) row.get("pdiDtlId"));
					responseModel.setCheckpointDesc((String) row.get("checkpointDesc"));
					responseModel.setDefaultTick((String) row.get("defaultTick"));
					responseModel.setAggregateId((BigInteger) row.get("aggregateId"));
					responseModel.setAggregate((String) row.get("aggregate"));
					responseModel.setAggregateSequenceNo((Integer) row.get("aggregateSequenceNo"));
					responseModel.setCheckpointSequenceNo((Integer) row.get("checkpointSequenceNo"));
					responseModel.setRemark((String) row.get("remark"));
					responseModel.setCheckpointId((BigInteger) row.get("checkpointId"));
					responseModel.setObservedSpecification((String) row.get("observedSpecification"));
					responseModel.setSpecification((String) row.get("specification"));

					responseModelList.add(responseModel);

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<InstallationChecklistResponse> getAllCheckpointOfInstallationByRoId(String authorizationHeader,
			String userCode, Integer roId, int i) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAllCheckpointOfInstallationByRoId invoked.." + roId);
		}
		Query query = null;
		Session session = null;
		jobInventryChecklistReponse responseListModel = null;
		List<InstallationChecklistResponse> responseModelList = null;
		String sqlQuery = "EXEC [SP_GET_SV_JOB_CARD_BY_RO_ID] :ROID, :FLAG";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new jobInventryChecklistReponse();
				responseModelList = new ArrayList<InstallationChecklistResponse>();
				InstallationChecklistResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InstallationChecklistResponse();
					responseModel.setInstallDtlId((Integer) row.get("installDtlId"));
					responseModel.setId(BigInteger.valueOf((Integer) row.get("ID")));
					responseModel.setCheckpointDesc((String) row.get("INS_CHECK_POINT"));
					responseModel.setStatus((String) row.get("ISACTIVE"));
					responseModelList.add(responseModel);

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public Map<String, Object> fetchQuotationSearchList(String userCode, String quotationText, int categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchQuotationSearchList invoked.." + userCode + "::::::::::quotationText::::::::::"
					+ quotationText);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = " EXEC [SV_RO_JOBCARD_SEARCH_QUOTATIONNO] :UserCode,:searchText,:categoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("searchText", quotationText);
			query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("quotationList", data);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;
	}

	@Override
	public void updateFlagInPdiTableByDtlId(int pdiDtlId, int i, int categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateFlagInPdiTableByDtlId invoked.." + pdiDtlId);
		}
		Query query = null;
		Session session = null;
		String sqlQuery = " EXEC [SV_UPDATE_INWORDPDI_BY_ID]:PdiDtlId ,:flag,:categoryId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("PdiDtlId", pdiDtlId);
			query.setParameter("flag", i);
			query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}
	
	@Override
	public void updateFlagInInstallationDtlById(int installDtlId, int isActive, Integer categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateFlagInInstallationDtlById invoked.." + installDtlId);
		}
		Query query = null;
		Session session = null;
		String sqlQuery = " EXEC [SV_UPDATE_INSTALLATIONCHECKPOINTS_BY_ID]:installDtlId ,:status,:categoryId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("installDtlId", installDtlId);
			query.setParameter("status", isActive);
			query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	@Override
	public Map<String, Object> getValidatePcrButton(String userCode, String jobCardId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getValidatePcrButton invoked.." + userCode + "::::::::::jobCardId::::::::::" + jobCardId);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = " EXEC [SV_VALIDATE_PCR_BUTTON]:jobCardId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("jobCardId", jobCardId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("PCR_List", data);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;
	}

	@Override
	public Map<String, Object> fetchJobCardStatusList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardStatusList invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "select status from [SV_RO_HDR] group by status ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("JobCardStatus", data);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;
	}

	@Override
	public List<JobChassisDetailsResponse> fetchOutWordChassisDetailsByChassisSTK(String authorizationHeader,String userCode, String chassis) {
		
				if (logger.isDebugEnabled()) {
					logger.debug("fetchChassisDetailsByVinId invoked.." + chassis);
				}
				Query query = null;
				Session session = null;
				JobChassisDetailsResponse responseListModel = null;
				List<JobChassisDetailsResponse> responseModelList = null;
				Integer recordCount = 0;
				String sqlQuery = "EXEC [SP_SV_SERVICE_GET_CHASSIS_BY_STOCKREPAIR]  :Chassis_number, :flag";
				try {
					session = sessionFactory.openSession();
					query = session.createSQLQuery(sqlQuery);
					query.setParameter("Chassis_number", chassis);
					query.setParameter("flag", "HEADER");
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List data = query.list();
					if (data != null && !data.isEmpty()) {
						responseListModel = new JobChassisDetailsResponse();
						responseModelList = new ArrayList<JobChassisDetailsResponse>();
						JobChassisDetailsResponse responseModel = null;
						for (Object object : data) {
							Map row = (Map) object;
							responseModel = new JobChassisDetailsResponse();
							responseModel.setVinId((BigInteger) row.get("vinId"));
							responseModel.setProfitcenter((String) row.get("PC_desc"));
							responseModel.setChassisNo((String) row.get("chassisNumber"));
							responseModel.setEngineNo((String) row.get("EngineNumber"));
							responseModel.setVinNo((String) row.get("vin_no"));
							responseModel.setRegistrationNo((String) row.get("registration_number"));
							responseModel.setItemDescription((String) row.get("MODEL_NAME"));
							responseModel.setInvoiceId((BigInteger) row.get("invoiceId"));
							responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
							responseModel.setInvoiceDate((Date) row.get("InvoiceDate"));
							responseModel.setBookingId((BigInteger) row.get("bookingId"));
							responseModel.setServiceTypeId((BigInteger) row.get("service_type_id"));
							responseModel.setServiceRepairTypeId((BigInteger) row.get("service_repair_type_id"));
							responseModel.setCoustomerId((BigInteger) row.get("customerId"));

							responseModel.setCompletePdi((Integer) row.get("completedPdi"));
							responseModel.setPendingPdi((Integer) row.get("pendingPdi"));

							responseModel.setBookingNo((String) row.get("bookingno"));
							responseModel.setBookingDate((Date) row.get("booking_date"));
							responseModel.setPreviousHour((BigInteger) row.get("previous_hour"));
							responseModel.setLrDate((Date) row.get("LRDATE"));
							responseModel.setLrNumber((String) row.get("LRNumber"));
							responseModel.setPdiDoneBy((String) row.get("PDIDONEBY"));
							responseModel.setQuotationId((Integer) row.get("Quotation_Id"));
							responseModel.setQuotationDate((Date) row.get("Quotation_Date"));
							responseModel.setQuotationNumber((String) row.get("QuotationNumber"));
							responseModel.setCustomerName((String) row.get("CustomerName"));
							responseModel.setMobileNo((String) row.get("customermobile"));
							responseModel.setRepairOrderType((String) row.get("repairOrderType"));
							responseModel.setServiceType((String) row.get("serviceType"));
							responseModel.setInwardId((Integer) row.get("inwardId"));
							responseModel.setStarterMotorMakeNumber((String) row.get("StarterMotorMakeNumber"));
							responseModel.setAlternatorMakeNumber((String) row.get("AlternatorMakeNumber"));
							responseModel.setFlipMakeNumber((String) row.get("FIPMakeNumber"));
							responseModel.setBatteryMakeNumber((String) row.get("BatteryMakeNumber"));
							responseModel.setFrontTyerMakeRHNumber((String) row.get("FrontTyerMakeRHNumber"));
							responseModel.setFrontTyerMakeLHNumber((String) row.get("FrontTyerMakeLHNumber"));
							responseModel.setRearTyerMakeRHNumber((String) row.get("RearTyerMakeRHNumber"));
							responseModel.setRearTypeMakeLHNumber((String) row.get("RearTyerMakeLHNumber"));
							responseModel.setRepresentativeType((String) row.get("RepresentativeType"));
							responseModel.setRepresentativeName((String) row.get("RepresentativeName"));
							responseModelList.add(responseModel);
						}
					}
				} catch (SQLGrammarException exp) {
					logger.error(this.getClass().getName(), exp);
				} catch (HibernateException exp) {
					logger.error(this.getClass().getName(), exp);
				} catch (Exception exp) {
					logger.error(this.getClass().getName(), exp);
				} finally {
					if (session != null) {
						session.close();
					}
				}
				return responseModelList;
				
	}

	@Override
	public LabourGstCalcDtl getLabourGstDetail(int branchId,int coustemberId, int labourCodeId, BigDecimal rate, BigDecimal hour) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAllCheckpointOfPDIByRoId invoked.." + branchId);
		}
		Query query = null;
		Session session = null;
	
		LabourGstCalcDtl responseModel = null;

		String sqlQuery = "EXEC [sv_get_labour_dtl] :customerid, :branchid, :unitprice, :StandardHours, :labourid";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerid", coustemberId);
			query.setParameter("branchid", branchId);
			query.setParameter("unitprice", rate);
			query.setParameter("StandardHours", hour);
			query.setParameter("labourid", labourCodeId);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new LabourGstCalcDtl();
					
					responseModel.setLabourId((BigInteger) row.get("Labour_Id"));
					responseModel.setStandardHour((Integer)row.get("STANDARDHOUR"));
					responseModel.setUnitPrice((BigDecimal) row.get("UNITPRICE"));
					responseModel.setBasePrice((BigDecimal) row.get("baseprice"));
					responseModel.setCgst((BigDecimal) row.get("CGST"));
					responseModel.setIgst((BigDecimal) row.get("IGST"));
					responseModel.setSgst((BigDecimal) row.get("SGST"));
					responseModel.setCgstAmt((BigDecimal) row.get("CGSTAMT"));
					responseModel.setIgstAmt((BigDecimal) row.get("IGSTAMT"));
					responseModel.setSgstAmt((BigDecimal) row.get("SGSTAMT"));
					responseModel.setTotalGst((BigDecimal) row.get("TOTALGST"));
					responseModel.setTotalAmt((BigDecimal) row.get("TOTALAMT"));

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}


		@Override
		public LabourGstCalcDtl getOuterLabourGstDetail(int branchId,int coustemberId, int labourCodeId, BigDecimal rate, BigDecimal hour) {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllCheckpointOfPDIByRoId invoked.." + branchId);
			}
			Query query = null;
			Session session = null;
		
			LabourGstCalcDtl responseModel = null;

			String sqlQuery = "EXEC [SV_Get_Outside_Labour_Dtl] :customerid, :branchid, :unitprice, :StandardHours, :labourid";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("customerid", coustemberId);
				query.setParameter("branchid", branchId);
				query.setParameter("unitprice", rate);
				query.setParameter("StandardHours", hour);
				query.setParameter("labourid", labourCodeId);
				
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						responseModel = new LabourGstCalcDtl();
						
						responseModel.setLabourId((BigInteger) row.get("Labour_Id"));
						responseModel.setStandardHour((Integer)row.get("STANDARDHOUR"));
						responseModel.setUnitPrice((BigDecimal) row.get("UNITPRICE"));
						responseModel.setBasePrice((BigDecimal) row.get("baseprice"));
						responseModel.setCgst((BigDecimal) row.get("CGST"));
						responseModel.setIgst((BigDecimal) row.get("IGST"));
						responseModel.setSgst((BigDecimal) row.get("SGST"));
						responseModel.setCgstAmt((BigDecimal) row.get("CGSTAMT"));
						responseModel.setIgstAmt((BigDecimal) row.get("IGSTAMT"));
						responseModel.setSgstAmt((BigDecimal) row.get("SGSTAMT"));
						responseModel.setTotalGst((BigDecimal) row.get("TOTALGST"));
						responseModel.setTotalAmt((BigDecimal) row.get("TOTALAMT"));

					}
				}
			} catch (SQLGrammarException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return responseModel;
		}

		@Override
		public Integer checkLubricant(BigInteger roId) {
		
			Query query = null;
			Session session = null;
			Integer rowCount=null;
			LabourGstCalcDtl responseModel = null;

			String sqlQuery = "EXEC [sp_check_lubricant] :roId";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("roId", roId);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						rowCount = (Integer) row.get("ROW");
					}
				}
			
		    }catch(Exception e) {
		    	logger.error(this.getClass().getName(), e);
		    }
			return rowCount;
	}			
}				

