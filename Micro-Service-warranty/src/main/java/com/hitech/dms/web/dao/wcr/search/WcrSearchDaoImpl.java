package com.hitech.dms.web.dao.wcr.search;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.pcr.pcrSearchDaoImpl;
import com.hitech.dms.web.model.wcr.search.WCRApprovalRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrApprovalResponseDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchResponseDto;

@SuppressWarnings("deprecation")
@Repository
public class WcrSearchDaoImpl implements WcrSearchDao{
	
private static final Logger logger = LoggerFactory.getLogger(pcrSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	
	@Override
	public List<Map<String, Object>> autoSearchWcrNo(String wcrNo) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sv_wa_wcr_autosearch_wcr_No] :wcrNo";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("wcrNo", wcrNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("wcrNo", row.get("wcrNo"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	
	@Override
	public List<Map<String, Object>> autoSearchPcrNo(String pcrNo) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [autosearch_pcr_No_for_wcr] :pcrNo";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("pcrNo", pcrNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("pcrNo", row.get("pcr_no"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	
	
	@SuppressWarnings({"rawtypes", "unused"})
	@Override
	public ApiResponse<List<WcrSearchResponseDto>> wcrSearchList(String userCode, WcrSearchRequestDto requestModel) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("wcrSearchList invoked.. " + userCode + " " + requestModel.toString());
	    }
	    
	    
	    Session session = null;
	    Map<String, Object> mapData = null;
	    boolean isSuccess = true;
	    
	    
//	    private Session session;
	    ApiResponse<List<WcrSearchResponseDto>> apiResponse = new ApiResponse<>();
	    
	    List<WcrSearchResponseDto> wcrSearchList = new ArrayList<>();
	    @SuppressWarnings("unused")
	    Integer dataCount = 0;
	    
	    String sqlQuery = "exec [SV_WA_WCR_Search_Details] :WCRNO, :wcrType, :wcrStatus, :userCode, :hoUserId, :PCRNO, :RONumber, :ChassisNo, :fromDate, :toDate, :page, :size";
	    
	    try{
	    	session = sessionFactory.openSession();
	    	mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
	    	BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
	        Query query = session.createSQLQuery(sqlQuery);
	        query.setParameter("WCRNO", requestModel.getWcrNo());
	        query.setParameter("wcrType", requestModel.getWcrType());
	        query.setParameter("wcrStatus", requestModel.getWcrStatus());
	        query.setParameter("userCode", userCode);
	        query.setParameter("hoUserId", hoUserId);
	        query.setParameter("PCRNO", requestModel.getPcrNo());
	        query.setParameter("ChassisNo", requestModel.getChassisNo());
	        query.setParameter("RONumber", requestModel.getJobCardNo());
	        query.setParameter("fromDate", requestModel.getFromDate());
	        query.setParameter("toDate", requestModel.getToDate());
	        query.setParameter("page", requestModel.getPage());
	        query.setParameter("size", requestModel.getSize());
	        
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List data = query.list();
	        
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map row = (Map) object;
	                WcrSearchResponseDto response = new WcrSearchResponseDto();
	                response.setId((BigInteger)row.get("wcrId"));
	                response.setAction((String)row.get("Action"));
	                response.setWcrNo((String)row.get("wcrNo"));
	                response.setWcrDate((Date)row.get("wcrDate"));
	                response.setStatus((String)row.get("wcrStatus"));
	                response.setJobCardNo((String) row.get("JobCard_No"));
	                response.setJobCardDate((Date) row.get("JobCard_Date"));
	                response.setPcrId((BigInteger)row.get("pcrId"));
	                response.setPcrNo((String) row.get("PCRNO"));
	                response.setPcrCreatedDate((Date) row.get("PCRDATE"));
	                response.setGoodwillId((BigInteger)row.get("goodwillId"));
	                response.setChassisNo((String) row.get("chassis_no"));
	                response.setVinNo((String)row.get("vin_no"));
	                response.setEngineNo((String)row.get("EngineNo"));
	                response.setHour((BigInteger)row.get("hour"));
	                response.setTypeOfClaim((String)row.get("typeOfClaim"));
	                dataCount = (Integer) row.get("recordCount");
	                wcrSearchList.add(response);
	            }

	            apiResponse.setCount(dataCount);
		        apiResponse.setResult(wcrSearchList);
	        }else {
				apiResponse.setCount(dataCount);
		        apiResponse.setResult(wcrSearchList);
//				isSuccess = false;
				apiResponse.setMessage("Data Not Found.");
			}
	    } catch (SQLGrammarException exp) {
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	public WcrApprovalResponseDto approveRejectWCR(String userCode,
			WCRApprovalRequestDto requestModel) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		WcrApprovalResponseDto responseModel = new WcrApprovalResponseDto();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SV_WA_WCR_APPROVAL] :hoUserId, :wcrId, :approvalStatus, :remark, :rejectReason";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
//					query.setParameter("userCode", userCode);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("wcrId", requestModel.getWcrId());
					query.setParameter("approvalStatus", requestModel.getApprovalStatus());
					query.setParameter("remark", requestModel.getRemarks());
					query.setParameter("rejectReason", requestModel.getRejectReason());
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
						responseModel.setMsg("Error While Validating WCR Approval.");
					}
				} else {
					isSuccess = false;
					responseModel.setMsg("HO User Not Found.");
				}
			} else {
				isSuccess = false;
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
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

}
