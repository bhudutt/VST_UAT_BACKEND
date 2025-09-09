/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.model;

import java.math.BigInteger;
import java.util.ArrayList;
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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.enquiry.model.request.BrandListRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.EnqModelRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelItemDTLRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelItemListRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelVariantListRequestModel;
import com.hitech.dms.web.model.enquiry.model.response.BrandListResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.EnqModelResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelItemDTLResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelItemListResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelVariantListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnqModelDaoImpl implements EnqModelDao {
	private static final Logger logger = LoggerFactory.getLogger(EnqModelDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<EnqModelResponseModel> fetchEnqModelList(String userCode, Integer pcId, Long activityPlanID,
			String searchText, Long activityId, Long dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelList invoked.." + userCode);
		}
		Session session = null;
		List<EnqModelResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqModelList(session, userCode, pcId, activityPlanID, searchText, activityId,
					dealerId);
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
	public List<EnqModelResponseModel> fetchEnqModelList(Session session, String userCode, Integer pcId,
			Long activityPlanID, String searchText, Long activityId, Long dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelList invoked.." + userCode);
		}
		Query query = null;
		List<EnqModelResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getModelList] :userCode, :pcId, :activityPlanID, :searchText, :activityId, :dealerId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", pcId);
			query.setParameter("activityPlanID", activityPlanID);
			query.setParameter("searchText", searchText);
			query.setParameter("activityId", activityId);
			query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnqModelResponseModel>();
				EnqModelResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnqModelResponseModel();
					responseModel.setModelID((BigInteger) row.get("model_id"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setSegmentName((String) row.get("segment_name"));
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

	public List<EnqModelResponseModel> fetchEnqModelList(String userCode, EnqModelRequestModel enqModelRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelList invoked.." + userCode);
		}
		Session session = null;
		List<EnqModelResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqModelList(session, userCode, enqModelRequestModel.getPcId(),
					enqModelRequestModel.getActivityPlanID(), enqModelRequestModel.getSearchText(),
					enqModelRequestModel.getActivityId(), enqModelRequestModel.getDealerId());
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

	public List<ModelVariantListResponseModel> fetchEnqVariantModelList(String userCode, Long modelID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqVariantModelList invoked.." + modelID);
		}
		Session session = null;
		List<ModelVariantListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqVariantModelList(session, userCode, modelID);
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
	public List<ModelVariantListResponseModel> fetchEnqVariantModelList(Session session, String userCode,
			Long modelID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqVariantModelList invoked.." + userCode);
		}
		Query query = null;
		List<ModelVariantListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getModelVariantList] :userCode, :modelID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("modelID", modelID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ModelVariantListResponseModel>();
				ModelVariantListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ModelVariantListResponseModel();
					responseModel.setVariantID((BigInteger) row.get("variant_id"));
					responseModel.setVariantName((String) row.get("variant_name"));
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

	public List<ModelVariantListResponseModel> fetchEnqVariantModelList(String userCode,
			ModelVariantListRequestModel modelVariantListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelList invoked.." + userCode);
		}
		Session session = null;
		List<ModelVariantListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqVariantModelList(session, userCode, modelVariantListRequestModel.getModelID());
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

	public List<ModelItemListResponseModel> fetchEnqModelItemList(String userCode, Integer pcID, Long activityPlanID,
			Long modelID, String searchValue, Long activityId, Long dealerId, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelItemList invoked.." + userCode);
		}
		Session session = null;
		List<ModelItemListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqModelItemList(session, userCode, pcID, activityPlanID, modelID, searchValue,
					activityId, dealerId, isFor);
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
	public List<ModelItemListResponseModel> fetchEnqModelItemList(Session session, String userCode, Integer pcID,
			Long activityPlanID, Long modelID, String searchValue, Long activityId, Long dealerId, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelItemList invoked.." + userCode);
		}
		Query query = null;
		List<ModelItemListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getModelItemList] :userCode, :pcID, :activityPlanID, :modelID, :searchValue, :activityId, :dealerId, :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", pcID);
			query.setParameter("activityPlanID", activityPlanID);
			query.setParameter("modelID", modelID);
			query.setParameter("searchValue", searchValue);
			query.setParameter("activityId", activityId);
			query.setParameter("dealerId", dealerId);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ModelItemListResponseModel>();
				ModelItemListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ModelItemListResponseModel();
					responseModel.setMachineItemID((BigInteger) row.get("machine_item_id"));
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

	public List<ModelItemListResponseModel> fetchEnqModelItemList(String userCode,
			ModelItemListRequestModel modelItemListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelItemList invoked.." + userCode);
		}
		Session session = null;
		List<ModelItemListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqModelItemList(session, userCode, modelItemListRequestModel.getPcID(),
					modelItemListRequestModel.getActivityPlanID(), modelItemListRequestModel.getModelID(),
					modelItemListRequestModel.getSearchValue(), modelItemListRequestModel.getActivityId(),
					modelItemListRequestModel.getDealerId(), modelItemListRequestModel.getIsFor());
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

	public ModelItemDTLResponseModel fetchEnqModelItemDTL(String userCode, Long itemID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelItemDTL invoked.." + itemID);
		}
		Session session = null;
		ModelItemDTLResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchEnqModelItemDTL(userCode, itemID);
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ModelItemDTLResponseModel fetchEnqModelItemDTL(Session session, String userCode, Long itemID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelItemDTL invoked.." + itemID);
		}
		Query query = null;
		ModelItemDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_ENQ_getModelItemDetails] :userCode, :itemID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("itemID", itemID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ModelItemDTLResponseModel();
					responseModel.setMachineItemID((BigInteger) row.get("machine_item_id"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setSegmentName((String) row.get("segment_name"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setModelId((BigInteger) row.get("model_id"));
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

	public ModelItemDTLResponseModel fetchEnqModelItemDTL(String userCode,
			ModelItemDTLRequestModel modelItemDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqModelItemDTL invoked.." + modelItemDTLRequestModel.toString());
		}
		Session session = null;
		ModelItemDTLResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchEnqModelItemDTL(session, userCode, modelItemDTLRequestModel.getItemID());
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

	public List<BrandListResponseModel> fetchEnqBrandList(String userCode, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqBrandList invoked.." + isFor);
		}
		Session session = null;
		List<BrandListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqBrandList(session, userCode, isFor);
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
	public List<BrandListResponseModel> fetchEnqBrandList(Session session, String userCode, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqBrandList invoked.." + isFor);
		}
		Query query = null;
		List<BrandListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getBrandList] :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<BrandListResponseModel>();
				BrandListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new BrandListResponseModel();
					responseModel.setBrandID((BigInteger) row.get("brand_id"));
					responseModel.setBrandName((String) row.get("brand_name"));
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

	public List<BrandListResponseModel> fetchEnqBrandList(String userCode,
			BrandListRequestModel modelItemListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqBrandList invoked.." + modelItemListRequestModel.toString());
		}
		Session session = null;
		List<BrandListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnqBrandList(session, userCode, modelItemListRequestModel.getIsFor());
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
}
