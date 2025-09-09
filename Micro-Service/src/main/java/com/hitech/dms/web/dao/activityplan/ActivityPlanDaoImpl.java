package com.hitech.dms.web.dao.activityplan;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.activityplan.ActivityPlanApprovalEntity;
import com.hitech.dms.web.entity.activityplan.ActivityPlanDTLEntity;
import com.hitech.dms.web.model.activity.create.request.ActivityPlanApprovalRequestModel;
import com.hitech.dms.web.model.activity.create.request.ActivityPlanApprovalUpdateModel;
import com.hitech.dms.web.model.activitymaster.response.ActivityNumberSearchResponseModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanEditModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanEditRequestModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanRequestModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanSearchRequestModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanApprovalResponse;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanSearchResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanSearchResultResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanServiceTypeListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStateDealerWiseListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStateListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStatusResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanViewResponseModel;



@Repository
public class ActivityPlanDaoImpl implements ActivityPlanDao{
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private DocumentNumberGenerator docNumber;
	@Override
	public ActivityPlanResponseModel createActivityPlan(String userCode, Device device,
			ActivityPlanRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createActivityPlan invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		Map<String, Object> mapData = null;
		ActivityPlanResponseModel responseModel = new ActivityPlanResponseModel();
		String activityPlanNo = null;
		Query query = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			Integer activityPlanId=null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				requestModel.getActivityPlanHDREntity().setCreatedBy(userId);
				String finalValue=null;
				Map<String, String> data= fetchPlanNoIncrement(session);
				for(String value : data.values()) {
					finalValue=value;
					System.out.println(value);
				}
			    requestModel.getActivityPlanHDREntity().setActivityPlanNo(finalValue);
				Integer save =(Integer) session.save(requestModel.getActivityPlanHDREntity());
				mapData=fetchActivityPlanforByActivityId(session, requestModel.getActivityPlanHDREntity().getActivityPlanNo());
				activityPlanId=(Integer) mapData.get("activityPlanId");
				System.out.println(activityPlanId);
				
				List<ActivityPlanDTLEntity> list=requestModel.getActivityPlanDTLEntity();
				for(ActivityPlanDTLEntity obj:list)
				{
					obj.setId(save);
					obj.setActivityPlanDtlId(activityPlanId);
					obj.setCreatedBy(userId);
					session.save(obj);
				}
				
               List<ActivityPlanApprovalEntity> ActivityPlanApproval = fetchApprovalEntities(session, requestModel);
		        
		        if (!ActivityPlanApproval.isEmpty()) {
		            for (ActivityPlanApprovalEntity approvalEntity : ActivityPlanApproval) {
		            	approvalEntity.setActivityId(activityPlanId);
		                session.save(approvalEntity);
		            }
		        }
				
		
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
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
				mapData = fetchActivityPlanByActivityPlanId(session, requestModel.getActivityPlanHDREntity().getActivityPlanId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setActivityPlanId(requestModel.getActivityPlanHDREntity().getActivityPlanId());
					responseModel.setActivityPlanNo(msg);
					 responseModel.setActivityPlanNo((String)mapData.get("activityPlanNo"));
					responseModel
							.setMsg((String) mapData.get("activityPlanNo") + " " + "Activity Plan Created Successfully.");
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
	
	private List<ActivityPlanApprovalEntity> fetchApprovalEntities(Session session, ActivityPlanRequestModel requestModel) {
	    List<ActivityPlanApprovalEntity> ActivityPlanApproval = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_Activity_get_approval_hierarchy_level]";
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                ActivityPlanApprovalEntity hierarchyData = new ActivityPlanApprovalEntity();
	                hierarchyData.setActivityId((Integer)row.get("Activity_id"));
	                hierarchyData.setApproverLevelSeq((Integer)row.get("approver_level_seq"));
	                hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
	                hierarchyData.setGrpSeqNo((Integer)row.get("grp_seq_no"));
	                hierarchyData.setApprovalStatus((String)row.get("approvalStatus"));
	                hierarchyData.setIsfinalapprovalstatus((Character)row.get("isFinalApprovalStatus"));
	                hierarchyData.setRejectedFlag('N');
	                ActivityPlanApproval.add(hierarchyData);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    }
	    return ActivityPlanApproval;
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
	

	public Map<String, String> fetchPlanNoIncrement(Session session) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPlanNoIncrement invoked.." +  "::::::::::IncrementOrder::::::::::"
					);
		}
		Query query = null;
		Map<String, String> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [GenerateAndInsertPlanNumber] ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				Integer activityPlanId = null;
				for (Object object : data) {
					Map row = (Map) object;
					String activityPlanNo = (String) row.get("activityPlanNo");
					responseListModel.put("ActivityPlanNo", activityPlanNo);
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
		return responseListModel;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchActivityPlanforByActivityId(Session session, String activityPlanNo) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select ActivityPlanId from SV_Activity_Plan  where ActivityPlan =:activityPlanNo";
		mapData.put("ERROR", "Activity Plan Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("activityPlanNo", activityPlanNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				Integer activityPlanId = null;
				for (Object object : data) {
					Map row = (Map) object;
					activityPlanId = (Integer) row.get("ActivityPlanId");
				}
				mapData.put("activityPlanId", activityPlanId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY PLAN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY PLAN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchActivityPlanByActivityPlanId(Session session, Integer activityPlanId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select ActivityPlanId, ActivityPlan ActivityPlan from SV_Activity_Plan where ActivityPlanId =:activityPlanId";
		mapData.put("ERROR", "Activity Plan Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("activityPlanId", activityPlanId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String activityPlanNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					activityPlanNo = (String) row.get("ActivityPlan");
				}
				mapData.put("activityPlanNo", activityPlanNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY PLAN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ACTIVITY PLAN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@Override
	public List<ActivityPlanStateListResponseModel> fetchStateList(String userCode, Device device) {
		
		
		Session session = null;
		List<ActivityPlanStateListResponseModel> responseModelList = null;
		ActivityPlanStateListResponseModel responseModel = null;
		Query<ActivityPlanStateListResponseModel> query = null;
		String sqlQuery = "exec [sv_get_State]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanStateListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityPlanStateListResponseModel();
					responseModel.setStateId((BigInteger) row.get("state_ID"));
					responseModel.setState((String) row.get("statedesc"));
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
	public List<ActivityPlanStateDealerWiseListResponseModel> fetchStateDealerWiseList(String userCode, Device device,
			Integer stateId,  String dealerCode) {
		Session session = null;
		List<ActivityPlanStateDealerWiseListResponseModel> responseModelList = null;
		ActivityPlanStateDealerWiseListResponseModel responseModel = null;
		Query<ActivityPlanStateDealerWiseListResponseModel> query = null;
		String sqlQuery = "exec [SV_Get_Dealer_StateWise] :StateId, :ParentDealerCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("StateId", stateId);
			query.setParameter("ParentDealerCode", dealerCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanStateDealerWiseListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityPlanStateDealerWiseListResponseModel();
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setDealerId((BigInteger) row.get("parent_dealer_id"));
					responseModel.setDealerCode((String) row.get("ParentDealerCode"));
					responseModel.setDealerLocation((String) row.get("ParentDealerLocation"));
					responseModel.setDealerName((String) row.get("ParentDealerName"));
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
	public List<ActivityPlanServiceTypeListResponseModel> fetchServiceActivityTypeList(String userCode, Device device) {
		
		Session session = null;
		List<ActivityPlanServiceTypeListResponseModel> responseModelList = null;
		ActivityPlanServiceTypeListResponseModel responseModel = null;
		Query<ActivityPlanServiceTypeListResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_Activity_LIST]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanServiceTypeListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityPlanServiceTypeListResponseModel();
					responseModel.setServiceId((Integer) row.get("Activity_ID"));
					responseModel.setServiceActivityType((String) row.get("ActivitySourceName"));
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
	public ActivityPlanSearchResultResponseModel fetchActivityPlanSearchList(String userCode, Device device,
			ActivityPlanSearchRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivityPlanSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Map<String, Object> mapData = null;
		Session session = null;
		ActivityPlanSearchResultResponseModel responseListModel = null;
	    List<ActivityPlanSearchResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		
		String sqlQuery = "exec [SV_GET_SEARCH_ADDPLAN_LIST] :PlanNumber, :UserCode, :hoUserId, :FromDate, :ToDate, :Status, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			mapData = fetchHOUserDTLByUserCode(session, userCode);
			BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
			query.setParameter("PlanNumber", requestModel.getActivityPlanNo());
			query.setParameter("UserCode", userCode);
			query.setParameter("hoUserId", hoUserId);
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("ToDate", requestModel.getToDate());
			query.setParameter("Status", requestModel.getActivityPlanStatus());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new ActivityPlanSearchResultResponseModel();
				responseModelList = new ArrayList<ActivityPlanSearchResponseModel>();
				ActivityPlanSearchResponseModel responseModel=null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ActivityPlanSearchResponseModel();
					responseModel.setId((Integer) row.get("ActivityPlanId"));
					responseModel.setAction((String) row.get("Action"));
					responseModel.setActivityPlanNo((String) row.get("ActivityPlan"));
					responseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));
					responseModel.setActivityPlanStatus((String) row.get("LookupVal"));
					responseModel.setDealerCode((String) row.get("ParentDealerCode"));
					responseModel.setActivityType((String) row.get("Activitytype"));
					responseModel.setDealerName((String) row.get("ParentDealerName"));
					responseModel.setDealerLocation((String) row.get("ParentDealerLocation"));
					responseModel.setState((String) row.get("statedesc"));
					responseModel.setFromDate((Date) row.get("fromdate"));
					responseModel.setToDate((Date) row.get("todate"));
					responseModel.setTotalNumberOfDays((Integer) row.get("TotalNo_OfDays"));
					responseModel.setTotalActivityCost((BigDecimal) row.get("TotalActivityCost"));
					responseModel.setVstShare((BigInteger) row.get("VSTShare"));
					responseModel.setNumberOfJobCardTarget((BigInteger) row.get("NO_OfDay_JobCardTarget"));
					responseModel.setServiceRevenueTarget((BigInteger) row.get("Service_Revenue_Target"));
					responseModel.setNumberOfEnquiry((BigInteger) row.get("No_Of_Eqquiry"));
					responseModel.setNumberOfDelivery((BigInteger) row.get("No_Of_Delivery"));
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
	public List<ActivityPlanViewResponseModel> fetchActivityPlanViewList(String userCode, Device device,
			Integer activityId) {
		Session session = null;
		List<ActivityPlanViewResponseModel> responseModelList = null;
		ActivityPlanViewResponseModel responseModel = null;
		Query<ActivityPlanViewResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_SEARCH_ADDPLAN_VIEW_LIST] :activityPlanId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("activityPlanId", activityId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanViewResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityPlanViewResponseModel();
					responseModel.setActivityPlanNo((String) row.get("ActivityPlan"));
					responseModel.setActivityPlanStatus((String) row.get("ActivityPlanStatus"));
					responseModel.setActivityPlanDate((Date) row.get("ActivityPlanDate"));
					responseModel.setState((String) row.get("statedesc"));
					responseModel.setDealerCode((String) row.get("ParentDealerCode"));
					responseModel.setDealerName((String) row.get("ParentDealerName"));
					responseModel.setDealerLocation((String) row.get("ParentDealerLocation"));
					responseModel.setActivityType((String) row.get("Activitytype"));
					responseModel.setFromDate((Date) row.get("fromdate"));
					responseModel.setToDate((Date) row.get("todate"));
					responseModel.setTotalnumberofDays((Integer) row.get("TotalNo_OfDays"));
					responseModel.setTotalActivityCost((BigDecimal) row.get("TotalActivityCost"));
					responseModel.setVstShare((BigInteger) row.get("VSTShare"));
					responseModel.setNumberOfJobCardTarget((BigInteger) row.get("NO_OfDay_JobCardTarget"));
					responseModel.setServiceRevenueTarget((BigInteger) row.get("Service_Revenue_Target"));
					responseModel.setNumberOfEnquiry((BigInteger) row.get("No_Of_Eqquiry"));
					responseModel.setNumberofDelivery((BigInteger) row.get("No_Of_Delivery"));
					responseModel.setActivityDtlId((Integer) row.get("Id"));
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
	public List<ActivityPlanStatusResponseModel> fetchActivityPlanStatusList(String userCode, Device device) {
		
		Session session = null;
		List<ActivityPlanStatusResponseModel> responseModelList = null;
		ActivityPlanStatusResponseModel responseModel = null;
		String ActivityPlanType="ACTIVITY_PLAN_TYPE";
		Query<ActivityPlanStatusResponseModel> query = null;
		String sqlQuery = "select lookup_id,LookupTypeCode,LookupVal, DisplayOrder from SYS_LOOKUP where LookupTypeCode='"+ActivityPlanType+"' and Displayorder in(2,3)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanStatusResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityPlanStatusResponseModel();
//					responseModel.setStatusId((BigInteger) row.get("lookup_id"));
					responseModel.setStatus((String) row.get("LookupVal"));
//					responseModel.setPlanOrder((Integer) row.get("DisplayOrder"));
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
	public ActivityPlanResponseModel ActivityPlanStatusUpdate(String userCode, Device device, ActivityPlanApprovalRequestModel requestModel) {
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		ActivityPlanResponseModel responseModel=new ActivityPlanResponseModel();
		
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			List<ActivityPlanApprovalUpdateModel> list=requestModel.getActivityPlanApprovalUpdateModel();
			
			for(ActivityPlanApprovalUpdateModel obj:list) {
				 String sqlQuery = "exec [SV_GET_PLANSTATUS_UPDATE] :ActivityPlanStatus, :ActivityPlanId, :TotalActivityCost";
					query = session.createSQLQuery(sqlQuery);
	       			query.setParameter("ActivityPlanStatus", obj.getActivityPlanStatus());
	       			query.setParameter("ActivityPlanId", obj.getActivityPlanId());
	       			query.setParameter("TotalActivityCost", obj.getTotalActivityCost());
					int k = query.executeUpdate();
			}
			
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_OK_200);
			
			msg = "Activity Plan Status Update Successfully";
			responseModel.setMsg(msg);
		}else {
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			msg = "Bad Request";
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
	
      }
     return responseModel;

	}

	@Override
	public List<ActivityNumberSearchResponseModel> fetchActivityPlanNumberBySearchList(String userCode, Device device,
			String activityPlanNo) {
		Session session = null;
		List<ActivityNumberSearchResponseModel> responseModelList = null;
		ActivityNumberSearchResponseModel responseModel = null;
		Query<ActivityNumberSearchResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_PLANNUMBER_SEARCH] :PlanNumber";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("PlanNumber", activityPlanNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityNumberSearchResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityNumberSearchResponseModel();
					responseModel.setPlanNumber((String) row.get("ActivityPlan"));
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
	public List<ActivityPlanStatusResponseModel> fetchActivityPlanStatusListId(String userCode, Device device) {
		Session session = null;
		List<ActivityPlanStatusResponseModel> responseModelList = null;
		ActivityPlanStatusResponseModel responseModel = null;
		//String ActivityPlanType="ACTIVITY_PLAN_TYPE";
		Query<ActivityPlanStatusResponseModel> query = null;
		String sqlQuery = "select ActivityPlanId, ActivityPlanStatus from SV_Activity_Plan";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanStatusResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ActivityPlanStatusResponseModel();
					responseModel.setActivityId((Integer) row.get("ActivityPlanId"));
					responseModel.setStatus((String) row.get("ActivityPlanStatus"));
					//responseModel.setPlanOrder((Integer) row.get("DisplayOrder"));
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
	public ActivityPlanApprovalResponse approveRejectActivityPlan(String userCode,
			com.hitech.dms.web.model.activityplan.request.ActivityPlanApprovalRequestModel requestModel) {
			Session session = null;
			Transaction transaction = null;
			Query query = null;
			Map<String, Object> mapData = null;
			ActivityPlanApprovalResponse responseModel = new ActivityPlanApprovalResponse();
		
			boolean isSuccess = true;
			String sqlQuery = "exec [SV_ACTIVITY_PLAN_APPROVAL] :userCode, :hoUserId, :ActivityId, :approvalStatus, :remark";
			try {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				BigInteger userId = null;
				String msg = null;
				String approvalStatus = null;
				mapData = fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					userId = (BigInteger) mapData.get("userId");
					mapData = fetchHOUserDTLByUserCode(session, userCode);
					if (mapData != null && mapData.get("SUCCESS") != null) {
						BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
						query = session.createNativeQuery(sqlQuery);
						query.setParameter("userCode", userCode);
						query.setParameter("hoUserId", hoUserId);
						query.setParameter("ActivityId", requestModel.getActivityId());
						query.setParameter("approvalStatus", requestModel.getApprovalStatus());
						query.setParameter("remark", requestModel.getRemarks());
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
						List data = query.list();
						if (data != null && !data.isEmpty()) {
							for (Object object : data) {
								Map row = (Map) object;
								msg = (String) row.get("msg");
								responseModel.setMsg(msg);
								approvalStatus = (String) row.get("approvalStatus");
								responseModel.setApprovalStatus(approvalStatus);
							}
						} else {
							isSuccess = false;
							responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
							responseModel.setMsg("Error While Validating Approval.");
						}
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("HO User Not Found.");
					}
				} else {
					isSuccess = false;
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("User Not Found.");
				}
				if (isSuccess) {
					transaction.commit();
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					responseModel.setApprovalStatus(approvalStatus);
					responseModel.setMsg(msg);
				}
			} catch (SQLGrammarException ex) {
				if (transaction != null) {
					transaction.rollback();
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg(ex.getMessage());
				logger.error(this.getClass().getName(), ex);
			} catch (HibernateException ex) {
				if (transaction != null) {
					transaction.rollback();
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg(ex.getMessage());
				logger.error(this.getClass().getName(), ex);
			} catch (Exception ex) {
				if (transaction != null) {
					transaction.rollback();
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg(ex.getMessage());
				logger.error(this.getClass().getName(), ex);
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return responseModel;
		

	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select ho.ho_usr_id from ADM_HO_USER (nolock) HO INNER JOIN ADM_USER (nolock) u ON u.ho_usr_id = HO.ho_usr_id where u.UserCode =:userCode";
		mapData.put("ERROR", "HO USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("ho_usr_id");
				}
				mapData.put("hoUserId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@Override
	public ActivityPlanResponseModel ActivityPlanEdit(String authorizationHeader, String userCode,
			ActivityPlanEditRequestModel requestModel, Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityPlanEdit invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		
		ActivityPlanResponseModel responseModel = new ActivityPlanResponseModel();
		boolean isSuccess = true;
		Query query = null;
		try {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		List<ActivityPlanEditModel> list=requestModel.getActivityPlanEditModel();
		
		for(ActivityPlanEditModel obj:list) {
			 String sqlQuery = "exec [SP_GET_PLAN_DTL_UPDATE] :ActivityDtlId, :FromDate, :toDate, :totalNoOfDays, :totalActivityCost, :vstShare, :noofDayJobCardTarget, :serviceRevenueTarget, :noofEqquiry, :noofDelivery, :branchId";
				query = session.createSQLQuery(sqlQuery);
       			query.setParameter("ActivityDtlId", obj.getActivityDtlId());
       			query.setParameter("FromDate", obj.getFromDate());
       			query.setParameter("toDate", obj.getToDate());
       			query.setParameter("totalNoOfDays", obj.getTotalNoOfDays());
       			query.setParameter("totalActivityCost", obj.getTotalActivityCost());
       			query.setParameter("vstShare", obj.getVstShare());
       			query.setParameter("noofDayJobCardTarget", obj.getNoofDayJobCardTarget());
       			query.setParameter("serviceRevenueTarget", obj.getServiceRevenueTarget());
       			query.setParameter("noofEqquiry", obj.getNoofEqquiry());
       			query.setParameter("noofDelivery", obj.getNoofDelivery());
       			query.setParameter("branchId", obj.getBranchId());
       			
				int k = query.executeUpdate();
		}
		
	if (isSuccess) {
		transaction.commit();
		session.close();
		//sessionFactory.close();
		responseModel.setStatusCode(WebConstants.STATUS_OK_200);
		
		msg = "Activity Plan Detail Update Successfully";
		responseModel.setMsg(msg);
	}else {
		responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		msg = "Bad Request";
	}
	} catch (SQLGrammarException ex) {
		if (transaction != null) {
			transaction.rollback();
		}
		isSuccess = false;
		responseModel.setMsg(ex.getMessage());
		logger.error(this.getClass().getName(), ex);
	} catch (HibernateException ex) {
		if (transaction != null) {
			transaction.rollback();
		}
		isSuccess = false;
		responseModel.setMsg(ex.getMessage());
		logger.error(this.getClass().getName(), ex);
	} catch (Exception ex) {
		if (transaction != null) {
			transaction.rollback();
		}
		isSuccess = false;
		responseModel.setMsg(ex.getMessage());
		logger.error(this.getClass().getName(), ex);
	} finally {
		if (session != null) {
			session.close();
		}

  }
 return responseModel;
 

}
}
