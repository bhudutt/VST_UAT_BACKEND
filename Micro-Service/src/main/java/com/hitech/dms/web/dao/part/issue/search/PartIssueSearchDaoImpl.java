package com.hitech.dms.web.dao.part.issue.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

import com.hitech.dms.web.model.partissue.search.request.PartIssueSearchRequestModel;
import com.hitech.dms.web.model.partissue.search.response.PartIssueSearchListResultResponse;
import com.hitech.dms.web.model.partissue.search.response.PartIssueSearchResponseModel;
import com.hitech.dms.web.model.partissue.search.response.SparePartIssueCommonResponseModel;
import com.hitech.dms.web.model.partissue.search.response.SparePartIssuePartDetailResponseModel;
import com.hitech.dms.web.model.partissue.search.response.SparePartIssueRequisitionDetailResponse;
import com.hitech.dms.web.model.partissue.search.response.SparePartIssueViewResponseModel;

@Repository
public class PartIssueSearchDaoImpl implements PartIssueSearchDao{
	private static final Logger logger = LoggerFactory.getLogger(PartIssueSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public PartIssueSearchListResultResponse PartIssueSearchList(String userCode,
			PartIssueSearchRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(
					"PartIssueSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		PartIssueSearchListResultResponse responseListModel = null;
	    List<PartIssueSearchResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [SP_GETPARTISSUE_SEARCH] :UserCode, :FromDate, :ToDate, :PartIssueNumber, :PartIssueType, :JobCardNo, :RequisitionNo, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("ToDate", requestModel.getToDate());
			query.setParameter("PartIssueNumber", requestModel.getPartIssueNo());
			query.setParameter("PartIssueType", requestModel.getIssueType());
			query.setParameter("JobCardNo", requestModel.getJobCardNo());
			query.setParameter("RequisitionNo", requestModel.getRequisitionNo());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new PartIssueSearchListResultResponse();
				responseModelList = new ArrayList<PartIssueSearchResponseModel>();
				PartIssueSearchResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartIssueSearchResponseModel();
					response.setId((BigInteger) row.get("Issue_id"));
					response.setIssueNo((String) row.get("IssueNumber"));
					response.setIssueDate((String) row.get("Issue_Date"));
					response.setIssueType((String) row.get("Req_Type"));
					response.setIssueBY((String) row.get("issueBy"));
					//response.setRequestedBy((String) row.get(""));
					response.setRequisitionNo((String) row.get("requisitionnumber"));
					response.setRequisitionDate((String) row.get("requisition_date"));
					response.setJobCardNo((String) row.get("ronumber"));
					response.setJobCardDate((String) row.get("openingDate"));
					//response.setRequestedQTY((BigDecimal) row.get("OriginalRequestQty"));
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(response);
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
	public SparePartIssueCommonResponseModel PartIssueViewList(String userCode, Integer issueId) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("PartIssueViewList invoked.." + userCode);
		}
			Session session = null;
			SparePartIssueCommonResponseModel responseModel=new SparePartIssueCommonResponseModel();
		 
		try {
			
			session = sessionFactory.openSession();
			SparePartIssueViewResponseModel sparePartIssueViewResponseModel= fetchSpareViewdetails(session, userCode, issueId);
			responseModel.setSparePartIssueViewResponseModel(sparePartIssueViewResponseModel);
			List<SparePartIssueRequisitionDetailResponse>sparePartIssueRequisitionDetailResponse=fetchRequisitionDetails(session, userCode, issueId);
			responseModel.setSparePartIssueRequisitionDetailResponse(sparePartIssueRequisitionDetailResponse);
			List<SparePartIssuePartDetailResponseModel>sparePartIssuePartDetailResponseModel=fetchSparePartDetails(session, userCode, issueId);
			responseModel.setSparePartIssuePartDetailResponseModel(sparePartIssuePartDetailResponseModel);
		
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


	public SparePartIssueViewResponseModel fetchSpareViewdetails(Session session, String userCode, Integer issueId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSpareViewdetails invoked.." + userCode);
		}
		Query query = null;
		SparePartIssueViewResponseModel responseModel=null;
		String sqlQuery = "exec [SP_GET_PART_ISSUE_VIEW_LIST] :IssueId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("IssueId", issueId);
			
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePartIssueViewResponseModel();
					responseModel.setIssueType((String) row.get("Req_Type"));
					responseModel.setIssueNumber((String) row.get("IssueNumber"));
					responseModel.setIssueDate((String) row.get("IssueDate"));
					responseModel.setRoNumber((String) row.get("RONUMBER"));
					responseModel.setOpeningDate((String) row.get("JobcardDate"));
					responseModel.setIssueBy((String) row.get("issueBy"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setChassisNumber((String) row.get("chassis_no"));
					responseModel.setRegistrationNumber((String) row.get("Registration_Number"));
					responseModel.setModelVariant((String) row.get("ModelVariant"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setRequisitionNo((String) row.get("requisitionnumber"));
					responseModel.setRequisitionDates((String) row.get("Requisitiondate"));
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
	
	public List<SparePartIssueRequisitionDetailResponse> fetchRequisitionDetails(Session session, String userCode,
			Integer issueId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchRequisitionDetails invoked.." + userCode);
		}
		Query query = null;
		List<SparePartIssueRequisitionDetailResponse> responseModelList = null;
		SparePartIssueRequisitionDetailResponse responseModel = null;
		String sqlQuery = "exec [SP_GET_PART_ISSUE_VIEW_LIST] :IssueId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("IssueId", issueId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePartIssueRequisitionDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePartIssueRequisitionDetailResponse();
					responseModel.setRequisitionNumber((String) row.get("requisitionnumber"));
					responseModel.setRequisitionDate((String) row.get("Requisitiondate"));
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
	public List<SparePartIssuePartDetailResponseModel> fetchSparePartDetails(Session session, String userCode,
			Integer issueId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchRequisitionDetails invoked.." + userCode);
		}
		Query query = null;
		List<SparePartIssuePartDetailResponseModel> responseModelList = null;
		SparePartIssuePartDetailResponseModel responseModel = null;
		String sqlQuery = "exec [SP_GET_PART_ISSUE_VIEW_LIST] :IssueId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("IssueId", issueId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePartIssuePartDetailResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePartIssuePartDetailResponseModel();
					responseModel.setRequisitionNumber((String) row.get("requisitionnumber"));
					responseModel.setPartNumber((String) row.get("partNumber"));
					responseModel.setPartDescription((String) row.get("PartDescription"));
					responseModel.setPendingQty((BigDecimal) row.get("PendingQty"));
					responseModel.setRequestQty((BigDecimal) row.get("RequestQty"));
					responseModel.setIssuedQty((BigDecimal) row.get("IssuedQty"));
					responseModel.setTotalBranchStock((Integer) row.get("TotalBranchStock"));
					responseModel.setCurrentStock((BigDecimal) row.get("currentStock"));
					responseModel.setMrp((BigDecimal) row.get("mrp"));
					responseModel.setStoreDesc((String) row.get("StoreDesc"));
					responseModel.setBinLocation((String) row.get("BinLocation"));
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
}
