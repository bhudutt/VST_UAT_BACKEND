package com.hitech.dms.web.dao.master;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.master.response.ItemMasterModel;

@Repository
public class ItemMasterDaoImpl implements ItemMasterDao {

	private static final Logger logger = LoggerFactory.getLogger(ItemMasterDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<ItemMasterModel> fetchItemMasterDetails(String itemNo) {
		Session session = null;
		List<ItemMasterModel> itemMasterModelList = null;
		ItemMasterModel itemMasterModelResponse = null;

		Integer dataCount = 0;

		Query query = null;
		String sqlQuery = "exec [Get_Item_Master] :itemNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("itemNo", itemNo);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				itemMasterModelList = new ArrayList<ItemMasterModel>();
				for (Object object : data) {
					Map row = (Map) object;
					itemMasterModelResponse = new ItemMasterModel();

					itemMasterModelResponse.setItemNo(itemNo);
					itemMasterModelResponse.setItemDescription((String) row.get("item_description"));
					itemMasterModelResponse.setProductGroup((String) row.get("product_group"));
					itemMasterModelResponse.setProductDivId((BigInteger) row.get("prod_div_id"));
					itemMasterModelResponse.setProductDivision((String) row.get("productDivision"));
					itemMasterModelResponse.setPcId((BigInteger) row.get("pc_id"));
					itemMasterModelResponse.setProfitCente((String) row.get("profitCenter"));
					itemMasterModelResponse.setModelId((BigInteger) row.get("model_id"));
					itemMasterModelResponse.setModelName((String) row.get("model_name"));
					itemMasterModelResponse.setSeriesName((String) row.get("series_name"));
					itemMasterModelResponse.setSegmentName((String) row.get("segment_name"));
					itemMasterModelResponse.setVariant((String) row.get("variant"));
					itemMasterModelResponse.setIsChasisNoReq((Character) row.get("isChasisNoReq"));
					itemMasterModelResponse.setIsEngineNoReq((Character) row.get("isEngineNoReq"));
					itemMasterModelResponse.setIsActive((Character) row.get("isActive"));
					itemMasterModelResponse.setHsnCode((String) row.get("hsn_code"));
					itemMasterModelResponse.setGst((Double) row.get("gst"));

					itemMasterModelList.add(itemMasterModelResponse);
				}
			}
		} catch (SQLGrammarException exp) {
			String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (Exception exp) {
			String errorMessage = "An error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} finally {
//				if (spareGrnDetailsResponseList != null) {
//					apiResponse = new ApiResponse<>();
//					apiResponse.setCount(dataCount);
//					apiResponse.setResult(spareGrnDetailsResponseList);
//					apiResponse.setMessage("GRN Details Search Successfully.");
//					apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
//				}
			if (session != null) {
				session.close();
			}
		}

		return itemMasterModelList;
	}

	@Override
	public HashMap<BigInteger, String> fetchItemMasterList(String userCode, Integer dealerId, Integer branchId, String searchText) {
		Session session = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec SP_CM_GETMACHINEITEMNO_MASTER_LIST :userCode, :dealerId, :branchId, :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", dealerId);
			query.setParameter("branchId", branchId);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
//					Integer customerId = (BigInteger) row.get("itemId");
					searchList.put((BigInteger) row.get("itemId"), (String) row.get("ItemNumber"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

}
