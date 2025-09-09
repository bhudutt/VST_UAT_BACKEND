package com.hitech.dms.web.dao.partreturn.search;

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

import com.hitech.dms.web.model.partreturn.request.PartReturnSearchRequestModel;
import com.hitech.dms.web.model.partreturn.response.PartReturnSearchListResultResponse;
import com.hitech.dms.web.model.partreturn.response.PartReturnSearchResponseModel;
import com.hitech.dms.web.model.partreturn.response.PartReturnViewResponseModel;

@Repository
public class PartReturnSearchDaoImpl implements PartReturnSearchDao{

	private static final Logger logger = LoggerFactory.getLogger(PartReturnSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public PartReturnSearchListResultResponse PartReturnSearchList(String userCode,
			PartReturnSearchRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(
					"PartReturnSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		PartReturnSearchListResultResponse responseListModel = null;
	    List<PartReturnSearchResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [SP_GETPARTRETURN_SEARCH] :UserCode, :FromDate, :ToDate, :PartReturnNumber, :PartReturnType, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("ToDate", requestModel.getTodate());
			query.setParameter("PartReturnNumber", requestModel.getPartReturnNo());
			query.setParameter("PartReturnType", requestModel.getReturnType());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new PartReturnSearchListResultResponse();
				responseModelList = new ArrayList<PartReturnSearchResponseModel>();
				PartReturnSearchResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartReturnSearchResponseModel();
					response.setId((BigInteger) row.get("Wk_Return_id"));
					response.setPartReturnNo((String) row.get("ReturnNumber"));
					response.setPartReturnDate((Date) row.get("Return_Date"));
					response.setPartReturnType((String) row.get("Req_Type"));
					response.setReturnBy((String) row.get("returnBy"));
					response.setIssueNumber((String) row.get("IssueNumber"));
					response.setIssueDate((Date) row.get("Issue_date"));
					response.setJobCardNo((String) row.get("ronumber"));
					response.setJobCardDate((Date) row.get("openingDate"));
					response.setReasonForReturn((String) row.get("delay_reason_desc"));
					//response.setReturnQTY((BigDecimal) row.get("ReturnedQty"));
					
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
	public List<PartReturnViewResponseModel> PartReturnViewList(String userCode, Integer returnId) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"PartReturnViewList invoked.." + userCode + " " );
		}
		
		Session session = null;
		List<PartReturnViewResponseModel>  responseList = null;
	    PartReturnViewResponseModel response = null;
	    NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETPARTRETURN_VIEW_LIST] :ReturnId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ReturnId", returnId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<PartReturnViewResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartReturnViewResponseModel();
					response.setReturnType((String) row.get("Req_Type"));
					response.setReturnNumber((String) row.get("ReturnNumber"));
					response.setReturnDate((String) row.get("Return_Date"));
					response.setRoNumber((String) row.get("ronumber"));
					response.setIssueNumber((String) row.get("IssueNumber"));
					response.setIssueDate((String) row.get("Issue_date"));
					response.setOpeningDate((String) row.get("openingDate"));
					response.setReturnBy((String) row.get("returnBy"));
					response.setDelayReasonDesc((String) row.get("delay_reason_desc"));
					response.setChassisNumber((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("Registration_Number"));
					response.setModelVariant((String) row.get("ModelVariant"));
					response.setCustomerName((String) row.get("CustomerName"));
					response.setRequisitionNumber((String) row.get("requisitionnumber"));
					response.setPartNumber((String) row.get("partNumber"));
					response.setPartDesc((String) row.get("partDesc"));
					response.setPendingRequestedQty((BigDecimal) row.get("pendingRequestedQty"));
					response.setRequestedQty((BigDecimal) row.get("requestedQty"));
					response.setIssuedQty((BigDecimal) row.get("IssuedQty"));
					response.setReturnedQty((BigDecimal) row.get("ReturnedQty"));
					response.setStoreDesc((String) row.get("StoreDesc"));
					response.setBinLocation((String) row.get("BinLocation"));
					response.setRemarks((String) row.get("Remarks"));
					responseList.add(response);
				}
			}
		 
		}catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

}
