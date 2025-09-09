package com.hitech.dms.web.dao.activityplan.search;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.activityplan.search.request.ActivityPlanSearchRequestModel;
import com.hitech.dms.web.model.activityplan.search.response.ActivityPlanSearchResponseModel;
import com.hitech.dms.web.model.activityplan.search.response.ActivityPlanSearchResultResponseModel;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityPlanSearchDaoImpl implements ActivityPlanSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ActivityPlanSearchResultResponseModel fetchActivityPlanList(String userCode,
			ActivityPlanSearchRequestModel activityPlanSearchRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"fetchActivityPlanList invoked.." + userCode + " " + activityPlanSearchRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		ActivityPlanSearchResultResponseModel responseModel = null;
		List<ActivityPlanSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SACT_SEARCH_ACTIVITYPLAN] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :activityNumber, :fromDate, :toDate, :isIncludeInActive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", activityPlanSearchRequestModel.getPcId());
			query.setParameter("orgHierId", activityPlanSearchRequestModel.getOrgHierID());
			query.setParameter("dealerId", activityPlanSearchRequestModel.getDealerId());
			query.setParameter("branchId", activityPlanSearchRequestModel.getBranchId());
			query.setParameter("activityNumber", activityPlanSearchRequestModel.getActivityNumber());
			query.setParameter("fromDate", activityPlanSearchRequestModel.getFromDate1());
			query.setParameter("toDate", activityPlanSearchRequestModel.getToDate1());
			query.setParameter("isIncludeInActive", activityPlanSearchRequestModel.getIncludeInActive());
			query.setParameter("page", activityPlanSearchRequestModel.getPage());
			query.setParameter("size", activityPlanSearchRequestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityPlanSearchResponseModel>();
				responseModel = new ActivityPlanSearchResultResponseModel();
				ActivityPlanSearchResponseModel searchModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					searchModel = new ActivityPlanSearchResponseModel();
					searchModel.setSrlNo((BigInteger) row.get("Sr_No"));
					searchModel.setActivityPlanHdrId((BigInteger) row.get("activity_plan_hdr_id"));
					//searchModel.setAction((String) row.get("Action"));
					searchModel.setActivityNumber((String) row.get("activity_number"));
					searchModel.setActivityPlanStatus((String) row.get("ActivityPlanStatus"));
					searchModel.setActivityDate((String) row.get("Activity_Date"));
					searchModel.setProfitCenter((String) row.get("Profit_Center"));
					searchModel.setSeries((String) row.get("Series"));
					searchModel.setSegment((String) row.get("Segment"));
					searchModel.setFinancialYear((String) row.get("FincialYear"));
					searchModel.setActivityMonth((String) row.get("Month"));
					searchModel.setState((String) row.get("Dealer_State"));
					searchModel.setDealerName((String) row.get("Dealer_Name"));
					searchModel.setDealerCode((String) row.get("Dealer_Code"));
					//searchModel.setBillingPlan((String) row.get("Billing_plan"));
					searchModel.setTotalBudget((String) row.get("Total_Budget"));
					searchModel.setTotalDays((String) row.get("Total_Days"));
					searchModel.setPlanUploaderName((String) row.get("uploaderName"));
					
					String fetchApproval = fetchApproval((BigInteger) row.get("activity_plan_hdr_id"),session,(BigInteger) row.get("levelId"));
					String[] arr=fetchApproval.split("#");
					searchModel.setAction(arr[0].equalsIgnoreCase("NA")?"":arr[0]);
					searchModel.setStateHeadRemark(arr[1].equalsIgnoreCase("NA")?"":arr[1]);
					searchModel.setMktDivHeadRemark(arr[2].equalsIgnoreCase("NA")?"":arr[2]);
					searchModel.setLevelOneApprovedDate((String) row.get("Level1_Approved_Date"));
					searchModel.setLevelTwoApprovedDate((String) row.get("Level2_Approved_Date"));
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(searchModel);
				}

				responseModel.setRecordCount(recordCount);
				
				responseModelList.sort((o1, o2) -> {
				    String action1 = o1.getAction();
				    String action2 = o2.getAction();

				    // Case 1: Both actions are null or empty (they are considered equal)
				    if ((action1 == null || action1.isBlank()) && (action2 == null || action2.isBlank())) {
				        return 0;
				    }

				    // Case 2: "Approve" should come first
				    if ("Approve".equals(action1) && (action2 == null || action2.isBlank())) {
				        return -1; // "Approve" comes first
				    }
				    if ("Approve".equals(action2) && (action1 == null || action1.isBlank())) {
				        return 1; // "Approve" comes first
				    }

				    // Case 3: Both are valid non-empty strings, sort alphabetically
				    return action1.compareTo(action2);
				});
				
				responseModel.setSearchResult(responseModelList);
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
	
	public String fetchApproval(BigInteger planHdrId,Session session,BigInteger levelId) {
		String val="";
		String str1="",str2="";
		String remark1="";
		
		
		NativeQuery<?> query = null;
		try {
			String sqlQuery = "exec [GET_plan_ACT_APPROVAL] :planHdrId";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("planHdrId", planHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					String apprvalStatus=(String) row.get("approval_status");
					String approvedDate=(String) row.get("approved_date");
					Integer desigLevel=(Integer) row.get("designation_level_id");
					//	((BigInteger) row.get("approver_level_seq"));
					String remark=(String) row.get("remark");
					if(remark !=null && !remark.trim().equalsIgnoreCase("")) {
						remark1=remark1.concat("#"+remark);
					}else {
						remark1=remark1.concat("#NA");
					}
					if(desigLevel==32) {
						str1=approvedDate;
						
					}else if(desigLevel==14) {
						str2=approvedDate;
						
					}
					
				}
			}
			System.out.println("str1 "+str1+" str2 "+str2+" levelId "+levelId);
			if(levelId.equals(BigInteger.valueOf(32)) && str1==null && str2==null) {
				val="Approve";
			}else if(levelId.equals(BigInteger.valueOf(14)) && str1!=null && str2==null) {
				val="Approve";
			}else {
				val="";
			}
			val=val.trim().equalsIgnoreCase("")?"NA":val;
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		}
		return val.concat(remark1);
	}
}
