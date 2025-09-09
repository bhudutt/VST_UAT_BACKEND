package com.hitech.dms.web.dao.activity.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.DmsCollectionUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.activity.create.request.ActivityRequestDtlEntity;
import com.hitech.dms.web.entity.activity.create.request.ActivityRequestEntity;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestDtlModel;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActivityResponseModel;
import com.hitech.dms.web.model.activity.create.response.ActivityViewAfterSubmitResponseModel;
import com.hitech.dms.web.model.activity.create.response.ActivityViewResponseModel;
import com.hitech.dms.web.model.activity.create.response.ServiceActivitySearchListResultResponse;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchListResultResponse;

@Repository
public class ActivityCreateDaoImpl implements ActivityCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(ActivityCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private DocumentNumberGenerator docNumber;

	@Override
	public List<ActivityResponseModel> activityPlanAuto(String userCode,
			ActivityRequestModel activityPlanNoAutoRequestModel) {
		Session session = null;
		ActivityResponseModel activityResponseModel = null;
		List<ActivityResponseModel> activityPlanNoList = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SV_GET_ACTIVITY_PLANNUMBER] :userCode";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			activityPlanNoList = new ArrayList<ActivityResponseModel>();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					activityResponseModel = new ActivityResponseModel();
					activityResponseModel.setActivityPlanId((Integer) row.get("ActivityPlanId"));
					activityResponseModel.setActivityPlanNumber((String) row.get("ActivityPlan"));
					activityResponseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));
					activityPlanNoList.add(activityResponseModel);
				}
			}

		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return activityPlanNoList;
	}
	@Override
	public ActivityResponseModel fetchActivityPlanDetailsById(String userCode, Integer activityPlanHdrId) {
		// TODO Auto-generated method stub
		Session session = null;
		ActivityResponseModel activityResponseModel = null;
		List<ActivityResponseModel> activityNameList = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SV_GET_ACTIVITY_PLAN_Details] :userCode,:activityPlanHdrId,:flag";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("activityPlanHdrId", activityPlanHdrId);
			query.setParameter("flag", 1);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			activityNameList = new ArrayList<ActivityResponseModel>();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					activityResponseModel = new ActivityResponseModel();
					activityResponseModel.setActivityPlanId((Integer) row.get("ActivityPlanId"));
					activityResponseModel.setActivityPlanNumber((String) row.get("ActivityPlan"));
					activityResponseModel.setActivityFromDate((Date) row.get("FromDate"));
					activityResponseModel.setActivityToDate((Date) row.get("ToDate"));
					activityResponseModel.setPcId((Integer) row.get("ProfitCenter"));
					activityResponseModel.setPcName((String) row.get("pc_desc"));
					activityResponseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));

				}
				sqlQuery = "exec [SV_GET_ACTIVITY_PLAN_Details] :userCode,:activityPlanHdrId,:flag";
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("activityPlanHdrId", activityPlanHdrId);
				query.setParameter("flag", 2);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map<?, ?> row = (Map<?, ?>) object;
						ActivityResponseModel activityResponseModelTwo = new ActivityResponseModel();
						activityResponseModelTwo.setActivityTypeId((Integer) row.get("ServiceActivityType"));
						activityResponseModelTwo.setActivityTypeName((String) row.get("ActivitySourceName"));
						activityNameList.add(activityResponseModelTwo);

					}

					activityResponseModel.setActivityNameList(activityNameList);
				}

			}

		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return activityResponseModel;
	}

	@Override
	public List<ActivityRequestDtlModel> fetchJobcardDetailsByActivityId(String userCode,
			BigInteger activityPlanHdrId) {
		// TODO Auto-generated method stub
		Session session = null;
		ActivityRequestDtlModel activityResponseModel = null;
		List<ActivityRequestDtlModel> activityNameList = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SV_JobCard_Details] :userCode,:activityNameId";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("activityNameId", activityPlanHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			activityNameList = new ArrayList<ActivityRequestDtlModel>();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					activityResponseModel = new ActivityRequestDtlModel();
					activityResponseModel.setJobCardId((BigInteger) row.get("jobcardId"));
					activityResponseModel.setJobCardNo((String) row.get("jobcardNo"));
					activityResponseModel.setJobCardDate((String) row.get("jobCardDate"));
					activityResponseModel.setModel((String) row.get("modelName"));
					activityResponseModel.setChassisNo((String) row.get("chassisNo"));
					activityResponseModel.setEngineNO((String) row.get("engineNo"));
					activityResponseModel.setMechanic((String) row.get("machanicName"));
					activityResponseModel.setJobCardCategory((String) row.get("jobCardCategory"));
					activityResponseModel.setServiceType((String) row.get("serviceType"));
					activityResponseModel.setHoursMeterReding((BigInteger) row.get("hoursData"));
					activityResponseModel.setCustomerName((String) row.get("customerName"));
					activityResponseModel.setMobileNo((String) row.get("mobileNo"));
					activityResponseModel.setTehsil((String) row.get("tehsilName"));
					activityResponseModel.setJobCardBillAmount((BigDecimal) row.get("jobCardBillAmt"));
					activityNameList.add(activityResponseModel);
				}
			}

		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return activityNameList;

	}

	public ActivityResponseModel createServiceActivity(String authorizationHeader, String userCode,
			ActivityRequestEntity requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createServiceActivity invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		boolean isSuccess = true;
		ActivityResponseModel responseModel = new ActivityResponseModel();
		int save = 0;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Date todayDate = new Date();
			String userId = null;

			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId = Id.toString();
			}
			
			String checkForDuplicate=checkForDuplicate(requestModel,session);
			if(checkForDuplicate !=null && checkForDuplicate.length()>0) {
				responseModel.setMsg("Duplicate Data "+checkForDuplicate);
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				return responseModel;
			}
			if (DmsCollectionUtils.isNotEmpty(requestModel.getDetailRequest())) {
				requestModel.getDetailRequest()
				
				.removeIf(a ->a.getCheck() ==null || a.getCheck() == false || a.getJobCardId()==null);
			}

			List<ActivityRequestDtlEntity> list = requestModel.getDetailRequest();
			if (list != null && list.size() > 0) {
				Iterator<ActivityRequestDtlEntity> iterator = list.iterator();
				while (iterator.hasNext()) {
					ActivityRequestDtlEntity obj = iterator.next();
					obj.setActivityEntity(requestModel);
					obj.setCreatedBy(userCode);
					obj.setCreatedDate(new Date());
				}
			}
			String docnum = null;
			if (requestModel.getActivityId() == null) {
				docnum = docNumber.getDocumentNumber("SVACT", requestModel.getBranchId(), session);
				requestModel.setActivityNumber(docnum);
				if (docnum == null || docnum.equals("")) {
					responseModel.setMsg("Document No. not generatng");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					return responseModel;
				}
				requestModel.setCreatedBy(userCode);
				requestModel.setCreatedDate(new Date());
				save = (Integer) session.save(requestModel);

				if (save > 0) {
					docNumber.updateDocumentNumber(docnum.substring(docnum.length() - 7), "SVACT",
							requestModel.getBranchId() + "", session, "Service Activity");
					transaction.commit();
					isSuccess = true;
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
		}
		if (isSuccess) {
			mapData = fetchActivityById(session, save);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				responseModel.setActivityPlanId(save);
				responseModel.setActivityPlanNumber((String) mapData.get("ActivityNumber"));
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setMsg((String) mapData.get("ActivityNumber") + " Activity Created Successfully ");
			} else {
				responseModel.setMsg("Some Issue While Creating Activity.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}

		return responseModel;
	}
	
	public String checkForDuplicate(ActivityRequestEntity request,Session session) {
		Boolean duplicate=false;
		Query query=null;
		StringBuilder sb=new StringBuilder();
		String returnString=null;
		List<ActivityRequestDtlEntity> detailRequest = request.getDetailRequest();	
		String sqlQuery="select distinct c.RONumber from SV_Activity_HDR a"
				+ " inner join SV_Activity_DTL as b on a.ActivityId=b.activityId"
				+ " inner join SV_RO_HDR as c on c.ro_id=b.JobCardId"
				+ " inner join SV_RO_DTL as d on d.ro_id=c.ro_id "
				+ "where a.ActivityPlanId=:activityPlanId and b.jobCardId=:jobCardId";
		try {
			query=session.createNativeQuery(sqlQuery);
			
			for(ActivityRequestDtlEntity jobCardObj:detailRequest) {
				query.setParameter("activityPlanId", request.getActivityPlanId());
				query.setParameter("jobCardId", jobCardObj.getJobCardId());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						String roNo = (String) row.get("RONumber");
						sb.append(roNo+",");
					}
					
				}
			}
			if(sb !=null && sb.length()>0) {
				returnString=sb.substring(0, sb.length()-1);
			}
			
		} catch (SQLGrammarException ex) {
			// TODO: handle exception
			duplicate=true;
			logger.error(this.getClass().getName(), ex);
		}catch (Exception ex) {
			duplicate=true;
			logger.error(this.getClass().getName(), ex);
		}
		return returnString;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchActivityById(Session session, Integer id) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select ActivityId,ActivityNumber from SV_Activity_HDR  where ActivityId=:id";
		mapData.put("ERROR", "Activity Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("id", id);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String activityNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					activityNumber = (String) row.get("ActivityNumber");
				}
				mapData.put("ActivityNumber", activityNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Activity DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Activity DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	public ServiceActivitySearchListResultResponse viewSubmitActivity(String userCode,
			ActivityRequestModel activityPlanNoAutoRequestModel) {

		// TODO Auto-generated method stub
		Session session = null;
		ServiceActivitySearchListResultResponse responseListModel = null;
		ActivityViewResponseModel activityResponseModel = null;
		List<ActivityViewResponseModel> activityPlanNoList = null;
		NativeQuery<?> query = null;
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
		Integer recordCount = 0;
		String sqlQuery = "exec [SV_View_Submit_Acitivit] :userCode,:pcId,:dealerId,:branchId,:fromDate,:toDate,:page,:size";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", activityPlanNoAutoRequestModel.getPcId());
			query.setParameter("dealerId", activityPlanNoAutoRequestModel.getDealerId());
			query.setParameter("branchId", activityPlanNoAutoRequestModel.getBranchId());
			query.setParameter("fromDate", activityPlanNoAutoRequestModel.getActivityFromDate());
			query.setParameter("toDate", activityPlanNoAutoRequestModel.getActivityToDate());
			query.setParameter("page", activityPlanNoAutoRequestModel.getPage());
			query.setParameter("size", activityPlanNoAutoRequestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			Date date;
			activityPlanNoList = new ArrayList<ActivityViewResponseModel>();
			if (data != null && !data.isEmpty()) {
				responseListModel=new ServiceActivitySearchListResultResponse();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					activityResponseModel = new ActivityViewResponseModel();
					activityResponseModel.setActivityHdrId((Integer) row.get("ActivityId"));
					activityResponseModel.setActivityNo((String) row.get("ActivityNumber"));
					//(Date) row.get("ActivityDate")
					activityResponseModel.setActivityCreationDate(formatDate((Date)row.get("ActivityDate")));
					activityResponseModel.setActivityLocation((String) row.get("activityLocation"));
					activityResponseModel.setActivityFromDate(formatDate((Date)row.get("activityFromDate")));
					activityResponseModel.setActivityToDate(formatDate((Date)row.get("activityToDate")));
					activityResponseModel.setActualFromDate(formatDate((Date)row.get("actualActivityFromDate")));
					activityResponseModel.setActualToDate(formatDate((Date)row.get("actualActivityToDate")));
					activityResponseModel.setTotalAmount((BigDecimal) row.get("totalAmount"));
					activityResponseModel.setPcName((String) row.get("pc_desc"));
					activityResponseModel.setActivityPlanNumber((String) row.get("ActivityPlan"));
					activityResponseModel.setActivityPlanDate(formatDate((Date)row.get("ActivityPlanDate")));
					activityResponseModel.setActivityName((String) row.get("ActivitySourceName"));
					activityResponseModel.setBranchName((String) row.get("BranchName"));
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					
					activityPlanNoList.add(activityResponseModel);
					
				}
				
				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchResult(activityPlanNoList);
			}

		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseListModel;

	}
	
	public String formatDate(Date inDate) {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
		String format="";
		try {
			format = sourceFormat.format(inDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format;
	}

	@Override
	public Map<String, Object> getJobCardNumberList(String userCode, String searchText,String planNoId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getJobCardNumberList invoked.." + userCode + "::::::::::searchText::::::::::" + searchText);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SV_SEARCH_JOBCARD_FOR_ACTIVITY] :userCode,:searchText,:planNoId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", searchText);
			query.setParameter("planNoId", planNoId);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("JobCardDetails", data);

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
	public List<ActivityViewAfterSubmitResponseModel> fetchActivityServiceViewList(String userCode, Device device, Integer activityHdrId){
		Session session = null;
		List<ActivityViewAfterSubmitResponseModel> responseModelList = null;
		ActivityViewAfterSubmitResponseModel responseModel = null;
		Query<ActivityViewAfterSubmitResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_SEARCH_ADDSERVICE_VIEW_LIST] :activityHdrId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("activityHdrId", activityHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityViewAfterSubmitResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityViewAfterSubmitResponseModel();
					responseModel.setActivityHdrId((Integer) row.get("ActivityId"));
					responseModel.setActivityNo((String) row.get("ActivityNumber"));
					responseModel.setActivityCreationDate((Date) row.get("ActivityDate"));
					responseModel.setActivityLocation((String) row.get("activityLocation"));
					responseModel.setActivityFromDate((Date) row.get("activityFromDate"));
					responseModel.setActivityToDate((Date) row.get("activityToDate"));
					responseModel.setActualFromDate((Date) row.get("actualActivityFromDate"));
					responseModel.setActualFromDate((Date) row.get("actualActivityFromDate"));
					responseModel.setActualToDate((Date) row.get("actualActivityToDate"));
					//responseModel.setTotalAmount((BigDecimal) row.get("totalAmount"));
					responseModel.setPcName((String) row.get("pc_desc"));
					responseModel.setActivityPlanNumber((String) row.get("ActivityPlan"));
					responseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));
					responseModel.setActivityName((String) row.get("ActivitySourceName"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setJobCardNo((String) row.get("jobcardno"));
					responseModel.setJobCardDate((String) row.get("jobcarddate"));
					responseModel.setModel((String) row.get("model"));
					responseModel.setChassisNo((String) row.get("chassisNo"));
					responseModel.setEngine((String) row.get("engineNo"));
					responseModel.setMechanic((String) row.get("mechanic"));
					responseModel.setJobCardCategory((String) row.get("jobcardcategory"));
					responseModel.setServiceType((String) row.get("servicetype"));
					responseModel.setHours((BigInteger) row.get("hours"));
					responseModel.setCustName((String) row.get("custName"));
					responseModel.setMobileNo((String) row.get("mobileNo"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setJobCardBillAmount((BigDecimal) row.get("jobcardbillamount"));
					responseModel.setTotalAmount((BigDecimal) row.get("totalamount"));
					
					
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
