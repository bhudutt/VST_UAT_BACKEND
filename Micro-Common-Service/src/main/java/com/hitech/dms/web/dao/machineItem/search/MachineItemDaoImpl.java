/**
 * 
 */
package com.hitech.dms.web.dao.machineItem.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.machineItem.request.ItemDTLRequestModel;
import com.hitech.dms.web.model.machineItem.request.ItemListRequestModel;
import com.hitech.dms.web.model.machineItem.response.ItemDTLResponseModel;
import com.hitech.dms.web.model.machineItem.response.ItemListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineItemDaoImpl implements MachineItemDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineItemDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ItemListResponseModel> fetchMachineItemList(Session session, String userCode, Integer pcId,
			BigInteger modelId, String productGroup, String searchValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineItemList invoked.." + productGroup);
		}
		Query query = null;
		List<ItemListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getItemList] :userCode, :pcId, :modelId, :productGroup, :searchValue";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", pcId);
			query.setParameter("modelId", modelId);
			query.setParameter("productGroup", productGroup);
			query.setParameter("searchValue", searchValue);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ItemListResponseModel>();
				ItemListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ItemListResponseModel();
					responseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setDisplayValue((String) row.get("displayValue"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<ItemListResponseModel> fetchMachineItemList(String userCode, ItemListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineItemList invoked.." + requestModel.toString());
		}
		Session session = null;
		List<ItemListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchMachineItemList(session, userCode, requestModel.getPcId(),
					requestModel.getModelId(), requestModel.getProductGroup(), requestModel.getSearchValue());
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ItemDTLResponseModel fetchMachineItemDTL(Session session, String userCode, BigInteger itemId,
			BigInteger branchId, BigInteger customerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineItemDTL invoked.." + itemId);
		}
		Query query = null;
		ItemDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_CM_getItemDetails] :userCode, :itemId, :branchId, :customerId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("itemId", itemId);
			query.setParameter("branchId", branchId);
			query.setParameter("customerId", customerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ItemDTLResponseModel();
					responseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setSegmentName((String) row.get("segment_name"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setModelId((BigInteger) row.get("model_id"));
					responseModel.setModelName((String) row.get("model_name"));

					responseModel.setBasicPrice((BigDecimal) row.get("basic_price"));
					responseModel.setCgstAmount((BigDecimal) row.get("cgst_amount"));
					responseModel.setCgstPercent((Double) row.get("cgst_percent"));
					responseModel.setDiscount((BigDecimal) row.get("discount"));
					responseModel.setGstAmount((BigDecimal) row.get("gst_amount"));
					responseModel.setIgstAmount((BigDecimal) row.get("igst_amount"));
					responseModel.setIgstPercent((Double) row.get("igst_percent"));
					responseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
					responseModel.setQty((Integer) row.get("qty"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setSgstAmount((BigDecimal) row.get("sgst_amount"));
					responseModel.setSgstPercent((Double) row.get("sgst_percent"));
					responseModel.setTaxableValue((BigDecimal) row.get("taxable_value"));
					responseModel.setTotalAmount((BigDecimal) row.get("total_amount"));

				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModel;
	}

	@Override
	public ItemDTLResponseModel fetchMachineItemDTL(String userCode, ItemDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineItemDTL invoked.." + requestModel.toString());
		}
		Session session = null;
		ItemDTLResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchMachineItemDTL(session, userCode, requestModel.getItemId(), requestModel.getBranchId(),
					requestModel.getCustomerId());
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
