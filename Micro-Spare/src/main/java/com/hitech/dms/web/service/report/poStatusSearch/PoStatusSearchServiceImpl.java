package com.hitech.dms.web.service.report.poStatusSearch;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.report.kpdSearch.KpdOrderStatusSearchDaoImpl;
import com.hitech.dms.web.dao.report.poStatusSearch.PoStatusSearchDao;
import com.hitech.dms.web.model.report.model.PoStatusSearchList;
import com.hitech.dms.web.model.report.model.PoStatusSearchRequest;
import com.hitech.dms.web.model.report.model.PoStatusSearchResponse;

@Service
public class PoStatusSearchServiceImpl implements PoStatusSearchService{
	
	private static final Logger logger = LoggerFactory.getLogger(KpdOrderStatusSearchDaoImpl.class);
	
	@Autowired
	private PoStatusSearchDao poStatusSearchDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public ApiResponse<List<Map<String, Object>>> autoCompletePoNo(String poNo, BigInteger branchId) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = poStatusSearchDao.autoCompletePoNo(session, poNo, branchId);
	        
	        Map row1 = (Map) data.get(0);
            System.out.println((String)row1.get("Message"));

	        if ((String)row1.get("Message") == null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("poId", row.get("poId"));
	                responseModel.put("poNo", row.get("poNo"));
	                
	                responseModelList.add(responseModel);
	            }
	            apiResponse.setResult(responseModelList); // Set the result in the ApiResponse
	        } else {
	            apiResponse.setMessage((String)row1.get("Message")); // Handle the case when no data is returned
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	
	@Override
	public PoStatusSearchList poStatusReportSearch(String userCode, PoStatusSearchRequest resquest, Device device) {
		


		Session session = null;
		Transaction transaction = null;
		List<PoStatusSearchResponse> responseList = null;
		PoStatusSearchResponse responseModel = null;
		PoStatusSearchList bean = new PoStatusSearchList();
		Integer totalRow = 0;
		Integer rowCount = 0;
		boolean isSuccess = true;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
//        session = sessionFactory.openSession();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

	        List<?> data = poStatusSearchDao.poStatusReportSearch(session, userCode, resquest, device);
	        
	        if (data != null && !data.isEmpty()) {
				responseList = new LinkedList<PoStatusSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
								
					responseModel = new PoStatusSearchResponse();

					responseModel.setPoDate((Date) row.get("poDate"));
					responseModel.setPoNumber((String) row.get("poNumber"));			
					responseModel.setSupplier((String) row.get("supplier"));
					responseModel.setProfitCentre((String) row.get("profitCentre"));
					responseModel.setCategoryName((String) row.get("categoryName"));
					responseModel.setLocation((String) row.get("location"));
					responseModel.setPartNo((String) row.get("partNo"));
					responseModel.setPartDesc((String) row.get("partDesc"));
					responseModel.setOrderQty((BigDecimal) row.get("orderQty"));
					responseModel.setRate((BigDecimal) row.get("rate"));
					responseModel.setValue((BigDecimal) row.get("value"));
					responseModel.setSupplyQty((BigDecimal) row.get("supplyQty"));
					responseModel.setPendingQty((BigDecimal) row.get("pendingQty"));
					responseModel.setSupplyValue((BigDecimal) row.get("supplyValue"));
					responseModel.setStatus((String) row.get("status"));
					totalRow = (Integer) row.get("totalCount");
	
					responseList.add(responseModel);
					rowCount++;
				}
				bean.setTotalRowCount(totalRow);
				bean.setSearchList(responseList);
				bean.setRowCount(rowCount);
			}
		}catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {
				bean.setStatusCode(WebConstants.STATUS_OK_200);
				bean.setMsg("PO Status Fetches Successfully...");

			} else {
				bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
        return bean;
	}

}
