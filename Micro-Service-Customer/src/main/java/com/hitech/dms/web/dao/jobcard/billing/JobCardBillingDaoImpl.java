package com.hitech.dms.web.dao.jobcard.billing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.jobcard.billing.JobCardBillingHDREntity;
import com.hitech.dms.web.entity.jobcard.billing.JobCardRoBillLabourDTLEntity;
import com.hitech.dms.web.entity.jobcard.billing.JobCardRoBillOutSideLabourDTLEntity;
import com.hitech.dms.web.entity.jobcard.billing.JobCardRoBillPartDetailEntity;
import com.hitech.dms.web.model.jobcard.billing.request.JobBillingPLORequestModel;
import com.hitech.dms.web.model.jobcard.billing.request.JobBillingSearchRequestModel;
import com.hitech.dms.web.model.jobcard.billing.request.JobCardBillingRequestModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCommonViewResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCreateResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingCustomerTypeListResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingNumberSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingPLOResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingSearchResultResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobBillingViewResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingDetailsCommonResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingSaleTypeResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardBillingSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCardNumberSearchResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobCustomerDetailsResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobLabourDetailsResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobOutSideLabourDetailsResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobPartDetailsResponseModel;
import com.hitech.dms.web.model.jobcard.billing.response.JobVechicleDetailsResponseModel;

@Repository
public class JobCardBillingDaoImpl implements JobCardBillingDao {
	
	private static final Logger logger = LoggerFactory.getLogger(JobCardBillingDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
//	@Autowired
//	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public JobBillingCreateResponseModel create(String authorizationHeader, String userCode,
			JobCardBillingRequestModel requestModel, Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("createJobCardBilling invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		Map<String, Object> mapData = null;
		JobCardBillingHDREntity entity=null;
		JobBillingCreateResponseModel responseModel = new JobBillingCreateResponseModel();
		String billingNo = null;
		boolean isSuccess = true;
		try {
			BigInteger billingId=null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//requestModel = mapper.map(requestModel, JobCardBillingRequestModel.class, "JobCardBillingHDREntityMapId");
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger userId = null;
				userId =  (BigInteger) mapData.get("userId");
				//System.out.println(userId+"Hell0");
				String userCodename=  userId.toString();
				
				billingNo = commonDao.getDocumentNumberById("JCB", requestModel.getJobCardBillingHDREntity().getBranchId(), session);
				if(billingNo==null||billingNo.isEmpty()) {
					isSuccess = false;
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("Document No not generating......");
					return responseModel;
					
				}
				requestModel.getJobCardBillingHDREntity().setDocNumber(billingNo);
				requestModel.getJobCardBillingHDREntity().setCreatedBy(userCodename);
				commonDao.updateDocumentNumber("JOB CRAD BILLING",requestModel.getJobCardBillingHDREntity().getBranchId(), billingNo, session);
				BigInteger save =(BigInteger) session.save(requestModel.getJobCardBillingHDREntity());
				mapData = fetchJobBillingforByBillingId(session, requestModel.getJobCardBillingHDREntity().getDocNumber());
				   billingId = (BigInteger) mapData.get("billingId");
				   System.out.println(billingId);
				  
				   List<JobCardRoBillPartDetailEntity> list=requestModel.getJobCardRoBillPartDetailEntity();
				   if (list != null) {
				   for(JobCardRoBillPartDetailEntity obj:list) {
					   obj.setRoBillPartDetailId(save);
					   obj.setRoBillId(billingId);
					   session.save(obj);
				    }
				   }	   
				   List<JobCardRoBillLabourDTLEntity> list1=requestModel.getJobCardRoBillLabourDTLEntity();
				   if (list1 != null) {
				   for(JobCardRoBillLabourDTLEntity obj:list1) {
					   obj.setRoBillLbrDtlId(save);
					   obj.setRoBillId(billingId);
					   session.save(obj);
				    }
				   }
				   List<JobCardRoBillOutSideLabourDTLEntity> list2=requestModel.getJobCardRoBillOutSideLabourDTLEntity();
				   if (list2 != null) {
				   for(JobCardRoBillOutSideLabourDTLEntity obj:list2) {
					   obj.setRoBillId(billingId);
					   session.save(obj);
				    }
				   }
				   UpdateStatus(requestModel.getJobCardBillingHDREntity().getRoId(),session);
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("Resource Not Found");
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
			if (isSuccess) {
				// insert
				mapData = fetchBillingNoBybillingId(session, requestModel.getJobCardBillingHDREntity().getRoBillingId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setBillingId(requestModel.getJobCardBillingHDREntity().getRoBillingId());
					responseModel.setBillingNo(msg);
					responseModel
							.setMsg((String) mapData.get("billingNo") + " " + "Billing Number Created Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
		
	}
	
	private void UpdateStatus(BigInteger roId,
			Session session) {
		Query query = null;
		String sqlQuery = "exec [SV_GET_BILL_STATUS_UPDATE] :RoId";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("RoId", roId);
		int k = query.executeUpdate();
		
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchJobBillingforByBillingId(Session session, String docNumber) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select ro_bill_id from SV_RO_BILL_HDR  where DocNumber =:docNumber";
		mapData.put("ERROR", "Billing Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("docNumber", docNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger billingId = null;
				for (Object object : data) {
					Map row = (Map) object;
					billingId = (BigInteger) row.get("ro_bill_id");
				}
				mapData.put("billingId", billingId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING BILLING DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING BILLING DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchBillingNoBybillingId(Session session, BigInteger billingId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select RO.ro_bill_id ro_bill_id, RO.DocNumber DocNumber from SV_RO_BILL_HDR RO where RO.ro_bill_id =:billingId";
		mapData.put("ERROR", "Billing Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("billingId", billingId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String billingNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					billingNo = (String) row.get("DocNumber");
				}
				mapData.put("billingNo", billingNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING BILLING DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING BILLING DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	

	
	@Override
	public List<JobCardBillingSaleTypeResponseModel> fetchSaleTypeList(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSaleTypeList invoked.." + userCode);
		} 
		
		Session session = null;
		JobCardBillingSaleTypeResponseModel responseListModel=null;
		List<JobCardBillingSaleTypeResponseModel> responseModelList=null;
		String cashType="CASH_TYPE";
		Query<JobCardBillingSaleTypeResponseModel> query = null;
		String sqlQuery = "select lookup_id, LookupTypeCode, LookupVal, DisplayOrder  from SYS_LOOKUP where LookupTypeCode= '"+cashType+"'";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel= new JobCardBillingSaleTypeResponseModel();
				responseModelList= new ArrayList<JobCardBillingSaleTypeResponseModel>();
				JobCardBillingSaleTypeResponseModel responseModel=null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardBillingSaleTypeResponseModel();
					responseModel.setId((BigInteger) row.get("lookup_id"));
					responseModel.setTypeCode((String) row.get("LookupTypeCode"));
					responseModel.setTypeName((String) row.get("LookupVal"));
					responseModel.setDisplayOrder((Integer) row.get("DisplayOrder"));
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
	public List<JobCardBillingSearchResponseModel> fetchJobCardBillingList(String authorizationHeader,
			String userCode, String jobCardNo) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardBillingList invoked.." + userCode);
		} 
		
		Query query = null;
		Session session = null;
		JobCardBillingSearchResponseModel responseListModel=null;
		List<JobCardBillingSearchResponseModel> responseModelList=null;
		String sqlQuery = "exec [SP_GET_JOBCARD_BILLING_SEARCH] :usercode, :jobcardNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", userCode);
			query.setParameter("jobcardNo", jobCardNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel= new JobCardBillingSearchResponseModel();
				responseModelList= new ArrayList<JobCardBillingSearchResponseModel>();
				JobCardBillingSearchResponseModel responseModel=null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCardBillingSearchResponseModel();
					responseModel.setJobCardNo((String) row.get("RONumber"));
					responseModel.setOpenDate((String) row.get("JobCardDate"));
					responseModel.setJobCardStatus((String) row.get("Status"));
					responseModel.setVehSrNo((String) row.get("VehSrNo"));
					responseModel.setRoId((BigInteger) row.get("ro_id"));
					responseModel.setCloseDate((String) row.get("CloseDate"));	
					responseModel.setSaleDate((String) row.get("SaleDate"));
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
	public JobCardBillingDetailsCommonResponseModel fetchJobCardBillingDetailsList(String authorizationHeader,
			String userCode, Integer roId, Integer flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobCardBillingDetailsList invoked.." + userCode);
		} 
		
		Session session = null;
		JobCardBillingDetailsCommonResponseModel responseModel = new JobCardBillingDetailsCommonResponseModel();
		try {
			session = sessionFactory.openSession();
			
			if(flag==1) {
				JobVechicleDetailsResponseModel jobVechicleDetailsResponseModel = fetchVechicleDTLList(session, userCode, roId, 1);
				responseModel.setJobVechicleDetailsResponseModel(jobVechicleDetailsResponseModel);
			}else if(flag==2) {
				JobCustomerDetailsResponseModel jobCustomerDetailsResponseModel = fetchCustomerDTLList(session, userCode,
						roId, 2);
				responseModel.setJobCustomerDetailsResponseModel(jobCustomerDetailsResponseModel);
			}else if(flag==3) {
				List<JobPartDetailsResponseModel> jobPartDetailsResponseModel = fetchPartDTLList(session, userCode, roId,
						3);
				responseModel.setJobPartDetailsResponseModel(jobPartDetailsResponseModel);
			}else if(flag==4) {
				List<JobLabourDetailsResponseModel> jobLabourDetailsResponseModel = fetchLabourDTLList(session, userCode,
						roId, 4);
				responseModel.setJobLabourDetailsResponseModel(jobLabourDetailsResponseModel);
			}else if(flag==5) {
				List<JobOutSideLabourDetailsResponseModel> jobOutSideLabourDetailsResponseModel = fetchOutSideLabourDTLList(session, userCode,
						roId, 5);
				responseModel.setJobOutSideLabourDetailsResponseModel(jobOutSideLabourDetailsResponseModel);
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

	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public JobVechicleDetailsResponseModel fetchVechicleDTLList(Session session, String userCode, Integer roId, Integer flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryDTL invoked.." + userCode);
		}
		Query query = null;
		JobVechicleDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARDBILL_DETAIL] :ROID, :FLAG";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", flag);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobVechicleDetailsResponseModel();
					responseModel.setJobCardNumber((String) row.get("RONumber"));
					responseModel.setJobCardStatus((String) row.get("Status"));
					responseModel.setJobCardCloseDate((String) row.get("ClosedJobCard_date"));
					responseModel.setSaleDate((String) row.get("Sale_Date"));;
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setModelVariant((String) row.get("ModelVariant"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setVinNo((String) row.get("vin_no"));	
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public JobCustomerDetailsResponseModel fetchCustomerDTLList(Session session, String userCode, Integer roId, Integer flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLList invoked.." + userCode);
		}
		Query query = null;
		JobCustomerDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARDBILL_DETAIL] :ROID, :FLAG";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", flag);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobCustomerDetailsResponseModel();
					responseModel.setCustomerType((String) row.get("CustomerType"));
					responseModel.setCustomerCode((String) row.get("customer_code"));	
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setMobile((String) row.get("mobile_no"));
					responseModel.setState((String) row.get("state"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setCity((String) row.get("citydesc"));;
					responseModel.setCityId((BigInteger) row.get("cityid"));
					responseModel.setPinCode((String) row.get("pincode"));
					responseModel.setBranchId((Integer) row.get("branch_id"));
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<JobPartDetailsResponseModel> fetchPartDTLList(Session session, String userCode,
		Integer roId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDTLList invoked.." + userCode);
		}
		Query query = null;
		List<JobPartDetailsResponseModel> responseModelList = null;
		JobPartDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARDBILL_DETAIL] :ROID, :FLAG";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobPartDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobPartDetailsResponseModel();
					responseModel.setBillableType((String) row.get("PartBillableTypeDesc"));
					responseModel.setPartNo((String) row.get("PartNumber"));
					responseModel.setPartDescription((String) row.get("PartDesc"));
					responseModel.setUom((String) row.get("UomDesc"));
					responseModel.setHsnCode((String) row.get("HSN_CODE"));
					responseModel.setUnitPrice((BigDecimal) row.get("Rate"));
					responseModel.setQty((BigDecimal) row.get("qty"));
					responseModel.setAmount((BigDecimal) row.get("amount"));
					responseModel.setDisc((BigDecimal) row.get("DiscountRate"));
					responseModel.setDiscAmt((BigDecimal) row.get("DiscountValue"));
					responseModel.setNetAmt((BigDecimal) row.get("BillValue"));
					responseModel.setCgst((BigDecimal) row.get("PartGSTPER"));
					responseModel.setCgstAmt((BigDecimal) row.get("PartGSTPERAMT"));
					responseModel.setSgct((BigDecimal) row.get("PartSGST"));
					responseModel.setSgstAmt((BigDecimal) row.get("PartSGSTAMT"));
					responseModel.setIgst((BigDecimal) row.get("PartIGSTPER"));
					responseModel.setIgstAmt((BigDecimal) row.get("PartIGSTPERAMT"));
					responseModel.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					responseModel.setRoId((BigInteger) row.get("RO_ID"));
					responseModel.setPartBranchId((Integer) row.get("partBranch_id"));
					responseModel.setBilableTypeId((Integer) row.get("ID"));;
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<JobLabourDetailsResponseModel> fetchLabourDTLList(Session session, String userCode,
			Integer roId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchLabourDTLList invoked.." + userCode);
		}
		Query query = null;
		List<JobLabourDetailsResponseModel> responseModelList = null;
		JobLabourDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARDBILL_DETAIL] :ROID, :FLAG";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobLabourDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobLabourDetailsResponseModel();
					responseModel.setBillableType((String) row.get("LabourBillableTypeDesc"));
					responseModel.setLabourCode((String) row.get("LabourCode"));
					responseModel.setLabourDescription((String) row.get("LabourDesc"));
					responseModel.setHsnCode((String) row.get("HSN_CODE"));
					responseModel.setLbrHours((BigDecimal) row.get("lbr_StandardHrs"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setLbrAmount((BigDecimal) row.get("lbr_amount"));
					responseModel.setDisc((BigDecimal) row.get("DiscountRate"));
					responseModel.setDiscAmt((BigDecimal) row.get("DiscountValue"));
					responseModel.setNetAmt((BigDecimal) row.get("BillValue"));
					responseModel.setCgst((BigDecimal) row.get("LBRCGSTPER"));
					responseModel.setCgstAmt((BigDecimal) row.get("LBRCGSTPERAMT"));
					responseModel.setSgct((BigDecimal) row.get("LBRSGST"));
					responseModel.setSgstAmt((BigDecimal) row.get("LBRSGSTAMT"));
					responseModel.setIgst((BigDecimal) row.get("LBRIGSTPER"));
					responseModel.setIgstAmt((BigDecimal) row.get("LBRIGSTPERAMT"));
					responseModel.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					responseModel.setRoId((BigInteger) row.get("ro_id"));
					responseModel.setLabourId((Integer) row.get("labour_id"));
					responseModel.setBilableTypeId((Integer) row.get("ID"));
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<JobOutSideLabourDetailsResponseModel> fetchOutSideLabourDTLList(Session session, String userCode,
			Integer roId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOutSideLabourDTLList invoked.." + userCode);
		}
		Query query = null;
		List<JobOutSideLabourDetailsResponseModel> responseModelList = null;
		JobOutSideLabourDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARDBILL_DETAIL] :ROID, :FLAG";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ROID", roId);
			query.setParameter("FLAG", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobOutSideLabourDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobOutSideLabourDetailsResponseModel();
					responseModel.setBillableType((String) row.get("outsideLbrBillableDesc"));
					responseModel.setLabourCode((String) row.get("LabourCode"));
					responseModel.setLabourDescription((String) row.get("LabourDesc"));
					responseModel.setHsnCode((String) row.get("HSN_CODE"));
					responseModel.setHour((BigDecimal) row.get("StandardHrs"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setAmount((BigDecimal) row.get("amounts"));
					responseModel.setDisc((BigDecimal) row.get("DiscountRate"));
					responseModel.setDiscAmt((BigDecimal) row.get("DiscountValue"));
					responseModel.setNetAmt((BigDecimal) row.get("BillValue"));
					responseModel.setCgst((BigDecimal) row.get("LBROCGSTPER"));
					responseModel.setCgstAmt((BigDecimal) row.get("LBROCGSTPERAMT"));
					responseModel.setSgct((BigDecimal) row.get("LBROSGST"));
					responseModel.setSgstAmt((BigDecimal) row.get("LBROSGSTAMT"));
					responseModel.setIgst((BigDecimal) row.get("LBROIGSTPER"));
					responseModel.setIgstAmt((BigDecimal) row.get("LBROIGSTPERAMT"));
					responseModel.setLabourId((Integer) row.get("labour_id"));	
					responseModel.setBillableTypeId((Integer) row.get("ID"));
					responseModel.setRoId((Integer) row.get("ro_id"));;
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public JobBillingSearchResultResponseModel fetchJobBillingSearchList(String authorizationHeader, String userCode,
			JobBillingSearchRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobBillingSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		JobBillingSearchResultResponseModel responseListModel = null;
	    List<JobBillingSearchResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		
		String sqlQuery = "exec [SP_GET_JOBCARD_BILLING_LIST_SEARCH] :Usercode, :JobcardNo, :JobCardFormDate, :JobCardToDate, :JobCardBillNo, :JobCardBillFormDate, :JobCardBillToDate, :Billstatus, :SaleType, :PaymentMode, :Engine, :Chassis, :CustomerType, :CustomerName, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Usercode", userCode);
			query.setParameter("JobcardNo", requestModel.getJobCardNo());
			query.setParameter("JobCardFormDate",requestModel.getJobCardFormDate());
			query.setParameter("JobCardToDate", requestModel.getJobCardToDate());
			query.setParameter("JobCardBillNo", requestModel.getBillingNumber());
			query.setParameter("JobCardBillFormDate", requestModel.getBillingFormDate());
			query.setParameter("JobCardBillToDate", requestModel.getBillingToDate());
			query.setParameter("Billstatus", requestModel.getBillingStatus());
			query.setParameter("SaleType", requestModel.getSaleType());
			query.setParameter("PaymentMode", requestModel.getPaymentMode());
			query.setParameter("Engine", requestModel.getEngineNo());
			query.setParameter("Chassis", requestModel.getChassisNo());
			query.setParameter("CustomerType", requestModel.getCustomerType());
			query.setParameter("CustomerName", requestModel.getCustomerName());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new JobBillingSearchResultResponseModel();
				responseModelList = new ArrayList<JobBillingSearchResponseModel>();
				JobBillingSearchResponseModel responseModel=null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobBillingSearchResponseModel();
					
					responseModel.setId((BigInteger) row.get("ro_bill_id"));
					responseModel.setJobCardNo((String) row.get("JobCardNo"));
					responseModel.setJobCardDate((Date) row.get("JobCardDate"));  
					responseModel.setBillingNumber((String) row.get("BillingNumber"));
					responseModel.setBillingDate((Date) row.get("BillingDate"));
					responseModel.setBillingStatus((String) row.get("BillingStatus"));
					responseModel.setSaleType((String) row.get("saleType"));
					responseModel.setSaleDate((Date) row.get("SaleDate"));  
					responseModel.setPaymentMode((String) row.get("paymentMode"));
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setCustomerType((String) row.get("CustomerType"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setCustomerCode((String) row.get("CustomerCode"));
					responseModel.setBasePrice((BigDecimal) row.get("BasePrice"));
					responseModel.setDiscount((BigDecimal) row.get("Discount"));
					responseModel.setTaxableAmount((BigDecimal) row.get("TaxableAmount"));
					responseModel.setInvoiceAmount((BigDecimal) row.get("InvoiceAmount"));


					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(responseModel);
				}

				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchResult(responseModelList);

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
	public JobBillingCommonViewResponseModel fetchJobBillingViewList(String authorizationHeader, String userCode,
			Integer roBillingId, Integer flag) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobBillingViewList invoked.." + userCode);
		} 
		
		Session session = null;
		JobBillingCommonViewResponseModel responseModel = new JobBillingCommonViewResponseModel();
		try {
			session = sessionFactory.openSession();
			if(flag==1) {
			    JobBillingViewResponseModel jobBillingViewResponseModel = fetchViewDTLList(session, userCode, roBillingId, 1);
				responseModel.setJobBillingViewResponseModel(jobBillingViewResponseModel);
			
			}else if(flag==2) {
				List<JobPartDetailsResponseModel> jobPartDetailsResponseModel = fetchPartViewDTLList(session, userCode, roBillingId, 2
						);
				responseModel.setJobPartDetailsResponseModel(jobPartDetailsResponseModel);
			
			} else if(flag==3) {
				List<JobLabourDetailsResponseModel> jobLabourDetailsResponseModel = fetchLabourViewDTLList(session, userCode,
						roBillingId, 3);
				responseModel.setJobLabourDetailsResponseModel(jobLabourDetailsResponseModel);
			
			}else if(flag==4) {
				List<JobOutSideLabourDetailsResponseModel> jobOutSideLabourDetailsResponseModel = fetchOutSideLabourViewDTLList(session, userCode,
						roBillingId, 4);
				responseModel.setJobOutSideLabourDetailsResponseModel(jobOutSideLabourDetailsResponseModel);
		 
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

	private JobBillingViewResponseModel fetchViewDTLList(Session session, String userCode, Integer roBillingId, Integer flag) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLList invoked.." + userCode);
		}
		Query query = null;
		JobBillingViewResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_BILLING_LIST] :RollBillId, :Flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RollBillId", roBillingId);
			query.setParameter("Flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobBillingViewResponseModel();
					responseModel.setId((BigInteger) row.get("ro_bill_id"));
					responseModel.setJobCardBillNo((String) row.get("JobCardBillNo"));
					responseModel.setJobCardBillDate((String) row.get("JobCardBillDate"));
					responseModel.setBillStatus((String) row.get("BillStatus"));
					responseModel.setJobCardNumber((String) row.get("RoNumber"));
					
					responseModel.setCloseDate((String) row.get("CloseDate")!=null?(String)row.get("CloseDate"):null);
					responseModel.setSaleDate((String) row.get("SaleDate")!=null?(String) row.get("SaleDate"):null);
					responseModel.setSaleType((String) row.get("SaleType"));
					responseModel.setPaymentMode((String) row.get("paymentMode"));
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setRegistrationNumber((String) row.get("Registration_Number"));
					responseModel.setModelVariant((String) row.get("ModelVariant"));
					responseModel.setVinNo((String) row.get("VIN_NO"));
					responseModel.setCustomerCode((String) row.get("customer_code"));
					responseModel.setCustomerType((String) row.get("CustomerType"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setMobileNo((String) row.get("mobile_no"));
					responseModel.setCompanyName((String) row.get("company_name"));
					responseModel.setPinCode((String) row.get("pincode"));
					responseModel.setVillage((String) row.get("village"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setState((String) row.get("state"));
					responseModel.setBasePrice((BigDecimal) row.get("T_BasicValue"));
					responseModel.setDiscount((BigDecimal) row.get("T_DiscountValue"));
					responseModel.setTaxableAmount((BigDecimal) row.get("T_TaxValue"));
					responseModel.setGstAmount((BigInteger) row.get("Total_GST"));
					responseModel.setInvoiceAmount((BigDecimal) row.get("T_BillValue"));
					
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

	private List<JobPartDetailsResponseModel> fetchPartViewDTLList(Session session, String userCode, Integer roBillingId, Integer flag) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartViewDTLList invoked.." + userCode);
		} 
		
		Query query = null;
		List<JobPartDetailsResponseModel> responseModelList = null;
		JobPartDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_BILLING_LIST] :RollBillId, :Flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RollBillId", roBillingId);
			query.setParameter("Flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobPartDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobPartDetailsResponseModel();
					responseModel.setPartDetailsId((BigInteger) row.get("ro_bill_prt_dtl_id"));
					responseModel.setRoBillId((BigInteger) row.get("partroBillId"));
					responseModel.setRoId((BigInteger) row.get("partRoId"));
					responseModel.setRequisitionId((BigInteger) row.get("partrequisitionid"));
//					responseModel.setPartBranchId((Integer) row.get("partBranch_id"));
					responseModel.setPartBranchIds((BigInteger) row.get("partBranch_id"));
					responseModel.setBillableType((String) row.get("PartBillableTypeDesc"));
					responseModel.setPartNo((String) row.get("PartNumber"));
					responseModel.setPartDescription((String) row.get("PartDesc"));
					responseModel.setUom((String) row.get("UomDesc"));
					responseModel.setHsnCode((String) row.get("partHSN_CODE"));
					responseModel.setUnitPrice((BigDecimal) row.get("partRate"));
					responseModel.setAmount((BigDecimal) row.get("amount"));
					responseModel.setQty((BigDecimal) row.get("partqty"));
					responseModel.setDisc((BigDecimal) row.get("partDiscountRate"));
					responseModel.setDiscAmt((BigDecimal) row.get("partDiscountValue"));
					responseModel.setNetAmt((BigDecimal) row.get("partBillValue"));
					responseModel.setCgst((BigDecimal) row.get("PartCGSTPER"));
					responseModel.setCgstAmt((BigDecimal) row.get("PartCGSTPERAMT"));
					responseModel.setSgct((BigDecimal) row.get("PartSGST"));
					responseModel.setSgstAmt((BigDecimal) row.get("PartSGSTAMT"));
					responseModel.setIgst((BigDecimal) row.get("PartIGSTPER"));
					responseModel.setIgstAmt((BigDecimal) row.get("PartIGSTPERAMT"));
					responseModel.setTotalAmt((BigDecimal) row.get("PatTOTALAMT"));					
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
	
	
	private List<JobLabourDetailsResponseModel> fetchLabourViewDTLList(Session session, String userCode,
			Integer roBillingId, Integer flag) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchLabourViewDTLList invoked.." + userCode);
		} 
		Query query = null;
		List<JobLabourDetailsResponseModel> responseModelList = null;
		JobLabourDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_BILLING_LIST] :RollBillId, :Flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RollBillId", roBillingId);
			query.setParameter("Flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobLabourDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobLabourDetailsResponseModel();
					responseModel.setRoBillId((BigInteger) row.get("ro_bill_lbr_dtl_id"));
					responseModel.setRoLBRDetailsId((BigInteger) row.get("ro_bill_id"));
					//responseModel.setLabourId((Integer) row.get("labour_id"));
					responseModel.setLabourIds((BigInteger) row.get("labour_id"));
					responseModel.setRequisitionId((BigInteger) row.get("requisition_id"));;
					responseModel.setBillableType((String) row.get("LabourBillableTypeDesc"));
					responseModel.setLabourCode((String) row.get("LBRLABOURCODE"));
					responseModel.setLabourDescription((String) row.get("LBRLABOURDESC"));
					responseModel.setHsnCode((String) row.get("LRBHSNCODE"));
					responseModel.setLbrHours((BigDecimal) row.get("lbr_StandardHrs"));
					responseModel.setRate((BigDecimal) row.get("LBRRate"));
					responseModel.setLbrAmount((BigDecimal) row.get("lbr_amount"));
					responseModel.setDisc((BigDecimal) row.get("LBRDiscountRate"));
					responseModel.setDiscAmt((BigDecimal) row.get("LBRDiscountValue"));
					responseModel.setNetAmt((BigDecimal) row.get("LBRBillValue"));
					responseModel.setCgst((BigDecimal) row.get("LBRCGSTPER"));
					responseModel.setCgstAmt((BigDecimal) row.get("LBRCGSTPERAMT"));
					responseModel.setSgct((BigDecimal) row.get("LBRSGST"));
					responseModel.setSgstAmt((BigDecimal) row.get("LBRSGSTAMT"));
					responseModel.setIgst((BigDecimal) row.get("LBRIGSTPER"));
					responseModel.setIgstAmt((BigDecimal) row.get("LBRIGSTPERAMT"));
					responseModel.setTotalAmt((BigDecimal) row.get("LBRTotalGST"));
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

	private List<JobOutSideLabourDetailsResponseModel> fetchOutSideLabourViewDTLList(Session session, String userCode,
			Integer roBillingId, Integer flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOutSideLabourViewDTLList invoked.." + userCode);
		} 
		Query query = null;
		List<JobOutSideLabourDetailsResponseModel> responseModelList = null;
		JobOutSideLabourDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_BILLING_LIST] :RollBillId, :Flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RollBillId", roBillingId);
			query.setParameter("Flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobOutSideLabourDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobOutSideLabourDetailsResponseModel();
					responseModel.setRoBillId((BigInteger) row.get("ro_bill_id"));
					responseModel.setOutSideLBRDetailsId((Integer) row.get("ro_bill_outside_lbr_dtl_id"));
					//responseModel.setLabourId((Integer) row.get("labour_id"));
					responseModel.setLabourIds((BigInteger) row.get("labour_id"));
					responseModel.setBillableType((String) row.get("OutsideLbrBillableTypeDesc"));
					responseModel.setLabourCode((String) row.get("outSideLabourCode"));
					responseModel.setLabourDescription((String) row.get("outSideLabourDesc"));
					responseModel.setHsnCode((String) row.get("OLBRHSNCODE"));
					responseModel.setHour((BigDecimal) row.get("StandardHrs"));
					responseModel.setRate((BigDecimal) row.get("outSideRate"));
					responseModel.setAmount((BigDecimal) row.get("amounts"));
					responseModel.setDisc((BigDecimal) row.get("outSideDiscountRate"));
					responseModel.setDiscAmt((BigDecimal) row.get("outSideDiscountValue"));
					responseModel.setNetAmt((BigDecimal) row.get("outSideBillValue"));
					responseModel.setCgst((BigDecimal) row.get("LBROCGSTPER"));
					responseModel.setCgstAmt((BigDecimal) row.get("LBROCGSTPERAMT"));
					responseModel.setSgct((BigDecimal) row.get("LBROSGST"));
					responseModel.setSgstAmt((BigDecimal) row.get("LBROSGSTAMT"));
					responseModel.setIgst((BigDecimal) row.get("LBROIGSTPER"));
					responseModel.setIgstAmt((BigDecimal) row.get("LBROIGSTPERAMT"));
					responseModel.setTotalAmt((BigDecimal) row.get("LBROTOTALAMT"));
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
	public JobBillingCreateResponseModel jobCardBillingUpdate(String authorizationHeader, String userCode,
			JobCardBillingRequestModel requestModel, Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("jobCardBillingUpdate invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		Map<String, Object> mapData = null;
		JobBillingCreateResponseModel responseModel = new JobBillingCreateResponseModel();
		String billingNo = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger userId = null;
				userId =  (BigInteger) mapData.get("userId");
				System.out.println();
				String userCodename=  userId.toString();
				requestModel.getJobCardBillingHDREntity().setCreatedBy(userCodename);
				session.merge(requestModel.getJobCardBillingHDREntity());
				 List<JobCardRoBillPartDetailEntity> list=requestModel.getJobCardRoBillPartDetailEntity();
				   for(JobCardRoBillPartDetailEntity obj:list) {
					   
					   session.merge(obj);
					   
				   }
				   List<JobCardRoBillLabourDTLEntity> list1=requestModel.getJobCardRoBillLabourDTLEntity();
				   for(JobCardRoBillLabourDTLEntity obj:list1) {
					   
					   session.merge(obj);
					  
				   }
				   
				   List<JobCardRoBillOutSideLabourDTLEntity> list2=requestModel.getJobCardRoBillOutSideLabourDTLEntity();
				   for(JobCardRoBillOutSideLabourDTLEntity obj:list2) {
					   
					   session.merge(obj);
					  
				   }

			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("Resource Not Found");
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
			if (isSuccess) {
				// insert
				mapData = fetchBillingNoBybillingId(session, requestModel.getJobCardBillingHDREntity().getRoBillingId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setBillingId(requestModel.getJobCardBillingHDREntity().getRoBillingId());
					responseModel.setBillingNo(msg);
					responseModel
							.setMsg((String) mapData.get("billingNo") + " " + "Billing Number Update Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;

	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public JobBillingPLOResponseModel fetchJobBillingPartLabourOutsideLBRList(String authorizationHeader,
			String userCode, JobBillingPLORequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchJobBillingPartLabourOutsideLBRList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		JobBillingPLOResponseModel responseModel = null;
		String sqlQuery = "exec [GetTaxeBreakForParts] :BranchID, :CalculationFor, :partORLbr_ID, :Party_branch_id, :customer_id, :state_id, :SaleAmount, :Discount";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("BranchID", requestModel.getBranchId());
			query.setParameter("CalculationFor",requestModel.getDiscountFor());
			query.setParameter("partORLbr_ID",requestModel.getPartORLbrID());
			query.setParameter("Party_branch_id",requestModel.getPartybranchId());
			query.setParameter("customer_id",requestModel.getCustomerId());
			query.setParameter("state_id",requestModel.getStateId());
			query.setParameter("SaleAmount",requestModel.getSaleamount());
			query.setParameter("Discount",requestModel.getDiscount());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new JobBillingPLOResponseModel();
					responseModel.setCgst((BigDecimal) row.get("cgst"));
					responseModel.setSgst((BigDecimal) row.get("sgst"));
					responseModel.setIgst((BigDecimal) row.get("igst"));
					responseModel.setCgstPercentage((BigDecimal) row.get("cgstPercentage"));
					responseModel.setSgstPercentage((BigDecimal) row.get("sgstPercentage"));
					responseModel.setIgstPercentage((BigDecimal) row.get("igstPercentage"));
					responseModel.setTotalChargeAmount((BigDecimal) row.get("totalChargeAmount"));
					responseModel.setTotalAmount((BigDecimal) row.get("totalAmount"));
					
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
	public List<JobBillingNumberSearchResponseModel> fetchJobBillingNumberBySearchList(String authorizationHeader,
			String userCode, String billingNumber) {
		Session session = null;
		List<JobBillingNumberSearchResponseModel> responseModelList = null;
		JobBillingNumberSearchResponseModel responseModel = null;
		Query<JobBillingNumberSearchResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_BILLINGNUMBER_SEARCH] :BillingNumber";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("BillingNumber", billingNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobBillingNumberSearchResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new JobBillingNumberSearchResponseModel();
					responseModel.setBillingNumber((String) row.get("DocNumber"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<JobCardNumberSearchResponseModel> fetchJobCardNumberBySearchList(String authorizationHeader,
			String userCode, String jobCardNumber) {
		     
		      
		Session session = null;
		List<JobCardNumberSearchResponseModel> responseModelList = null;
		JobCardNumberSearchResponseModel responseModel = null;
		Query<JobCardNumberSearchResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_JobcardNOBYBILL_SEARCH] :JobCardNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("JobCardNo", jobCardNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobCardNumberSearchResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new JobCardNumberSearchResponseModel();
					responseModel.setJobcardNumber((String) row.get("RONumber"));				
					responseModel.setStatus((String) row.get("status"));		
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<JobBillingCustomerTypeListResponseModel> fetchCustomerTypeList(String authorizationHeader,
			String userCode) {
		
		Session session = null;
		List<JobBillingCustomerTypeListResponseModel> responseModelList = null;
		JobBillingCustomerTypeListResponseModel responseModel = null;
		Query<JobBillingCustomerTypeListResponseModel> query = null;
		String sqlQuery = "exec [SP_GET_CUSTOMER_TYPE_LIST]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<JobBillingCustomerTypeListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new JobBillingCustomerTypeListResponseModel();
					responseModel.setCustomerType((String) row.get("LookupVal"));
					responseModel.setCustomerTypeCode((String) row.get("LookupTypeCode"));;
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}
	
	
}