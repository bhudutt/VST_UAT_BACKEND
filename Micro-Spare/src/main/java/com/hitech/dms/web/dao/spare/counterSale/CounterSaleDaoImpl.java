package com.hitech.dms.web.dao.spare.counterSale;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.counterSale.CounterSaleResponse;
import com.hitech.dms.web.model.spare.picklist.PickListResponse;

@Repository
public class CounterSaleDaoImpl implements CounterSaleDao {
	private static final Logger logger = LoggerFactory.getLogger(CounterSaleDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CommonDao commonDao;

	@Override
	public ApiResponse<HashMap<BigInteger, String>> searchCustomerDetails(String searchText, String userCode) {
		Session session = null;
		ApiResponse<HashMap<BigInteger, String>> apiResponse = null; 
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_Search_Counter_Sale :searchText";
		String msg = null;

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
//			query.setParameter("userCode", userCode);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
	    		apiResponse = new ApiResponse<>();

				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("Message");

					if (msg != null && msg.equalsIgnoreCase("Customer detail not matched , Kindly enter further details.")) {
						apiResponse.setMessage(msg);
						apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					} else {
						searchList.put((BigInteger) row.get("Counter_Sale_Id"), (String) row.get("customer"));
						apiResponse.setStatus(WebConstants.STATUS_OK_200);
					}
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (searchList != null) {
				apiResponse.setResult(searchList);
			}
			if (session.isOpen())
				session.close();
		}

		return apiResponse;
	}

	@Override
	public CounterSaleResponse searchCustomerDetails(int counterSaleId) {
		Session session = null;
		CounterSaleResponse counterSaleResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Counter_Sale] :counterSaleId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("counterSaleId", counterSaleId);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					counterSaleResponse = new CounterSaleResponse();
					counterSaleResponse.setId((BigInteger) row.get("Counter_Sale_Id"));
					counterSaleResponse.setCustomerName((String) row.get("Customer_Name"));
					counterSaleResponse.setMobileNo((String) row.get("Mobile_No"));
					counterSaleResponse.setPinId((BigInteger) row.get("pin_id"));
					counterSaleResponse.setPinCode((String) row.get("PinCode"));
					counterSaleResponse.setPostOffice((String) row.get("PostOffice"));
					counterSaleResponse.setCity((String) row.get("CityDesc"));
					counterSaleResponse.setTehsil((String) row.get("TehsilDesc"));
					counterSaleResponse.setDistrict((String) row.get("DistrictDesc"));
					counterSaleResponse.setState((String) row.get("StateDesc"));
				
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return counterSaleResponse;
	}

}
