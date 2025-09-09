package com.hitech.dms.web.dao.activityClaim.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.activityClaim.search.request.ActivityClaimSearchRequestModel;
import com.hitech.dms.web.model.activityClaim.search.response.ActivityClaimSearchListResponseModel;
import com.hitech.dms.web.model.activityClaim.search.response.ActivityClaimSearchMainResponseModel;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class ActivityClaimSearchDaoImpl implements  ActivityClaimSearchDao{

	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	@Override
	public ActivityClaimSearchMainResponseModel fetchActivityClaimSearchList(String userCode,
			ActivityClaimSearchRequestModel acRequestModel) {
		NativeQuery<?> query = null;
		Session session = null;
		ActivityClaimSearchListResponseModel responseModel = null;
		List<ActivityClaimSearchListResponseModel> responseModelList = null;
		ActivityClaimSearchMainResponseModel acMainSearch = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SACT_SEARCH_ACTIVITYCLAIMS] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :activityClaimNumber, :fromDate, :toDate , :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", acRequestModel.getPcId());
			query.setParameter("orgHierId", acRequestModel.getOrgHierId());
			query.setParameter("dealerId", acRequestModel.getDealerId());
			query.setParameter("branchId", acRequestModel.getBranchId());
			query.setParameter("activityClaimNumber", acRequestModel.getActivityClaimNumber());
			query.setParameter("fromDate", acRequestModel.getFromDate());
			query.setParameter("toDate", acRequestModel.getToDate());
			query.setParameter("includeInactive", 'N');
			query.setParameter("page", acRequestModel.getPage());
			query.setParameter("size", acRequestModel.getSize());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ActivityClaimSearchListResponseModel>();
				acMainSearch = new ActivityClaimSearchMainResponseModel();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					responseModel = new ActivityClaimSearchListResponseModel();
					responseModel.setDealerId((BigInteger) row.get("dealer_id"));
					
					responseModel.setActivityPlanNo((String) row.get("activity_plan_no"));
					
					responseModel.setActivityClaimNo((String) row.get("activity_claim_no"));
					responseModel.setClaimApprovedAmount((BigDecimal) row.get("claim_approved_amount"));
//					responseModel.setActivityFromDate((String) row.get("activity_from_date"));
//					responseModel.setActivityToDate((String) row.get("activity_to_date"));
					responseModel.setId((BigInteger) row.get("activity_plan_hdr_id"));
					responseModel.setActivityPlanDate((String) row.get("activity_plan_date"));
					responseModel.setDealerCode((String) row.get("dealer_code"));
					responseModel.setDealerName((String) row.get("dealer_name"));
					responseModel.setProfitCenter((String) row.get("profit_center"));
					responseModel.setFinancialYear((Integer) row.get("financial_year"));
					responseModel.setFinancialMonth((String) row.get("financial_month"));;
					responseModel.setTotalBudget((BigDecimal) row.get("total_budget"));
					responseModel.setTotalApprovedBudget((BigDecimal) row.get("total_approved_budget"));
					responseModel.setTotalReimbursement((BigDecimal) row.get("total_reimbursement"));
					responseModel.setId((BigInteger) row.get("activity_claim_hdr_id"));
					responseModel.setRemarks((String) row.get("remarks"));
					
					responseModel.setLevelOneApprovedDate((String) row.get("Level1_Approved_Date"));
					responseModel.setLevelTwoApprovedDate((String) row.get("Level2_Approved_Date"));
					responseModel.setLevelThreeApprovedDate((String) row.get("Level3_Approved_Date"));
					
					StringBuilder fetchApproval = fetchApproval((BigInteger) row.get("activity_claim_hdr_id"),session,(BigInteger) row.get("levelId"));
					String[] split = fetchApproval.toString().split("#");
					responseModel.setAction( split[0].toString().equalsIgnoreCase("test")?"":split[0].toString()   );
					responseModel.setActivityClaimStatus(split[1].toString());
					responseModel.setRemarks(split[2].toString().equalsIgnoreCase("test")?"":split[2].toString() );
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(responseModel);
					}
				acMainSearch.setAcSearch(responseModelList);
				acMainSearch.setRecordCount(recordCount);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return acMainSearch;
	}
	
	public StringBuilder fetchApproval(BigInteger claimId,Session session,BigInteger levelId) {
		String val="";
		String statusval="";
		String remarksVal="";
		String str1="",str2="",str3="";
		String status1="",status2="",status3="";
		String remarks1="",remarks2="",remarks3="";
		StringBuilder sb=new StringBuilder();
		NativeQuery<?> query = null;
		try {
			String sqlQuery = "exec [GET_RET_ACT_APPROVAL] :claimHdrId";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("claimHdrId", claimId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					String apprvalStatus=(String) row.get("approval_status");
					String approvedDate=(String) row.get("approved_date");
					Integer desigLevel=(Integer) row.get("designation_level_id");
					String remarks=(String) row.get("remarks");
					//	((BigInteger) row.get("approver_level_seq"));
					
					if(desigLevel==32) {
						str1=approvedDate;
						status1=apprvalStatus;
						remarks1=remarks;
					}else if(desigLevel==7) {
						str2=approvedDate;
						status2=apprvalStatus;
						remarks2=remarks;
					}else if(desigLevel==1) {
						str3=approvedDate;
						status3=apprvalStatus;
						remarks3=remarks;
					}
					
				}
			}
			
			
			
			
			if(str1==null && str2==null && str3==null) {
				statusval=status1;
				remarksVal=remarks1;
				if(levelId.equals(BigInteger.valueOf(32))) {
					val="Approve";
				}else {
					val="";
				}
			}else if(str1!=null && str2==null && str3==null) {
				statusval=status2;
				remarksVal=remarks1;
				if(levelId.equals(BigInteger.valueOf(7))) {
					val="Approve";
				}else {
					val="";
				}
			}else if(str1!=null && str2!=null && str3==null) {
				statusval=status3;
				remarksVal=remarks2;
				
				if(levelId.equals(BigInteger.valueOf(1))) {
					val="Approve";
				}else {
					val="";
				}
			}else if(str1!=null && str2!=null && str3!=null) {
				
				if(remarks3.contains("Rejected")) {
					remarksVal=remarks2;
					statusval=status2;
					if(remarks2.contains("Rejected")) {
						remarksVal=remarks1;
						statusval=status1;
					}
				}else {
					statusval=status3;
					remarksVal=remarks3;
				}
				
				
				val="";
			}
			
			
			
			if(remarksVal !=null && !remarksVal.equalsIgnoreCase("")) {
				
			}else {
				remarksVal="test";
			}
			
			if(val !=null && !val.equalsIgnoreCase("")) {
				
			}else {
				val="test";
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName(), e);
		}
		return sb.append(val).append("#").append(statusval).append("#").append(remarksVal);
	}

}
