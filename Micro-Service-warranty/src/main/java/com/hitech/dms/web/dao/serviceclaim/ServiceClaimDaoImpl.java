package com.hitech.dms.web.dao.serviceclaim;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.serviceclaim.InstaillationStatusBean;
import com.hitech.dms.web.model.serviceclaim.JobCardStatusBean;
import com.hitech.dms.web.model.serviceclaim.PDIStatusBean;
import com.hitech.dms.web.model.serviceclaim.ServiceCLaimApprovalRequestDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimJCListRequest;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimSearchRequestDto;

/**
 * @author suraj.gaur
 */
@Repository
@SuppressWarnings("deprecation")
public class ServiceClaimDaoImpl implements ServiceClaimDao {
	
	@Override
	public List<?> getJobcardClaimList(Session session, String userCode, ServiceClaimJCListRequest requestModel) {
		String sqlQuery = "exec [SV_JOBCARD_CLAIM_LIST] :userCode, :claimType, :fromDate, :toDate";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("userCode", userCode);
	        query.setParameter("claimType", requestModel.getClaimType());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> viewServiceClaim(Session session, BigInteger claimId, Integer flag) {
		String sqlQuery = "exec [SV_SERVICE_CLAIM_VIEW] :claimId, :flag";
	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("claimId", claimId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> getClaimTypes(Session session) {
		String sqlQuery = "EXEC SV_SERVICE_CLIAM_TYPES";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> autoSearchClaimNo(Session session, String claimNo) {
	    String sqlQuery = "exec [SV_SERVICE_CLAIM_AUTO_SEARCH_CLAIM_NO] :claimNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("claimNo", claimNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}	
	
	@Override
	public List<?> getClaimStatus(Session session) {
		String sqlQuery = "select distinct status from SV_SERVICE_CLAIM_HDR";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> serviceClaimSearch(Session session, String userCode, ServiceClaimSearchRequestDto requestModel) {
	    String sqlQuery = "exec [SV_SERVICE_CLAIM_SEARCH] :claimNo, :userCode, :claimTypeId, :status, :fromDate, :toDate, :page, :size";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("claimNo", requestModel.getClaimNo());
	        query.setParameter("userCode", userCode);
	        query.setParameter("claimTypeId", requestModel.getClaimTypeId());
	        query.setParameter("status", requestModel.getStatus());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());

	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<?> getApprovalHierarchy(Session session) {
		String sqlQuery = "exec [SV_SERVICE_CLAIM_APPROVAL_HIERARCHY_LEVEL]";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	@Override
	public List<?> approveRejectSVClaim(Session session, String userCode, BigInteger hoUserId, ServiceCLaimApprovalRequestDto requestModel) {
	    String sqlQuery = "exec SP_SV_SERVICE_CLAIM_APPROVAL :hoUserId, :serviceClaimId, :approvalStatus, :remark, :rejectReason";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hoUserId", hoUserId);
			query.setParameter("serviceClaimId", requestModel.getServiceClaimId());
			query.setParameter("approvalStatus", requestModel.getApprovalStatus());
			query.setParameter("remark", requestModel.getRemarks());
			query.setParameter("rejectReason", requestModel.getRejectReason());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	

	@Override
	public Integer installationAppAndRej(Session session, String userCode, BigInteger hoUserId, Integer installationId,
			String statusApproveReject) {
		
		    String sqlQuery = "update SV_SERVICE_CLAIM_DTL set Status=:statusApproveReject where INSTALLATION_ID=:installationId";
		    Query<?> query = null;
		    Integer data = null;

		    try {
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("installationId", installationId);
				query.setParameter("statusApproveReject", statusApproveReject);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				data = query.executeUpdate();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return data;
		}

	@Override
	public void approveRejectStatusClaim(Session session, String userCode, BigInteger hoUserId,
			List<InstaillationStatusBean> instaillationStatus) {
		
		  String sqlQuery = "update SV_SERVICE_CLAIM_DTL set Status=:statusApproveReject where INSTALLATION_ID=:installationId";
		  Query<?> query = null;
		    
		  for(InstaillationStatusBean bean : instaillationStatus) {
			  try {
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("installationId", bean.getInstallationId());
					query.setParameter("statusApproveReject", bean.getStatusApproveReject());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					query.executeUpdate();
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
		  }    
	}
	
	@Override
	public void approveRejectPDIStatusClaim(Session session, String userCode, BigInteger hoUserId,
			List<PDIStatusBean> pdiStatus) {
		
		  String sqlQuery = "update SV_SERVICE_CLAIM_DTL set Status=:statusApproveReject where PDI_ID=:PDI_ID";
		  Query<?> query = null;
		    
		  for(PDIStatusBean bean : pdiStatus) {
			  try {
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("PDI_ID", bean.getPdiId());
					query.setParameter("statusApproveReject", bean.getStatusApproveReject());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					query.executeUpdate();
			    } catch (Exception e) {
			        e.printStackTrace();
			    }

		  }
		    
	}
	
	@Override
	public void approveRejectJCStatusClaim(Session session, String userCode, BigInteger hoUserId,
			List<JobCardStatusBean> jobCardDtl) {
		
		  String sqlQuery = "update SV_SERVICE_CLAIM_DTL set Status=:statusApproveReject where JOBCARD_CAT_ID=:JOBCARD_CAT_ID";
		  Query<?> query = null;
		    
		  for(JobCardStatusBean bean : jobCardDtl) {
			  try {
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("JOBCARD_CAT_ID", bean.getJobCardId());
					query.setParameter("statusApproveReject", bean.getStatusApproveReject());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					query.executeUpdate();
			    } catch (Exception e) {
			        e.printStackTrace();
			    }

		  }
		    
	}
}
