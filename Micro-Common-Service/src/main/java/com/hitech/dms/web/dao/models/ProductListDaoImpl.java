/**
 * 
 */
package com.hitech.dms.web.dao.models;

import java.math.BigDecimal;
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
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.models.request.MachineItemDTLRequestModel;
import com.hitech.dms.web.model.models.request.MachineItemRequestModel;
import com.hitech.dms.web.model.models.request.ModelItemListRequestModel;
import com.hitech.dms.web.model.models.request.ModelsForSeriesSegmentRequestModel;
import com.hitech.dms.web.model.models.request.ProductListRequestModel;
import com.hitech.dms.web.model.models.request.SeriesSegmentRequestModel;
import com.hitech.dms.web.model.models.response.MachineItemResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.models.response.ModelItemListResponseModel;
import com.hitech.dms.web.model.models.response.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.model.models.response.ProductListResponseModel;
import com.hitech.dms.web.model.models.response.SeriesSegmentResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ProductListDaoImpl implements ProductListDao {
	private static final Logger logger = LoggerFactory.getLogger(ProductListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<ProductListResponseModel> fetchProductList(String userCode, String productLevelName,
			String searchParentText, Long pcID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProductList invoked.." + userCode);
		}
		Session session = null;
		List<ProductListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchProductList(session, userCode, productLevelName, searchParentText, pcID);
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
	public List<ProductListResponseModel> fetchProductList(Session session, String userCode, String productLevelName,
			String searchParentText, Long pcID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProductList invoked.." + productLevelName + " " + searchParentText + " " + pcID);
		}
		Query query = null;
		List<ProductListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getProductList] :productLevelName, :searchParentText, :pcID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("productLevelName", productLevelName);
			query.setParameter("searchParentText", searchParentText);
			query.setParameter("pcID", pcID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ProductListResponseModel>();
				ProductListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ProductListResponseModel();
					responseModel.setValueID((Integer) row.get("valueID"));
					responseModel.setValueName((String) row.get("valueName"));
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

	public List<ProductListResponseModel> fetchProductList(String userCode,
			ProductListRequestModel productListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchProductList invoked.." + productListRequestModel.toString());
		}
		Session session = null;
		List<ProductListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchProductList(session, userCode, productListRequestModel.getProductLevelName(),
					productListRequestModel.getSearchParentText(), productListRequestModel.getPcId());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<SeriesSegmentResponseModel> fetchSeriesSegmentList(String userCode, SeriesSegmentRequestModel ssModel) {
		Session session = null;
		List<SeriesSegmentResponseModel> seriesSegmentList = null;
		SeriesSegmentResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_CM_getSeriesSegmentList] :userCode,:pcId,:isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", ssModel.getPcId());
			query.setParameter("isFor", ssModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				seriesSegmentList = new ArrayList<SeriesSegmentResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					response = new SeriesSegmentResponseModel();
					response.setValueID((BigInteger) row.get("valueId"));
					response.setValueName((String) row.get("valueName"));
					seriesSegmentList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
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

		return seriesSegmentList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ModelsForSeriesSegmentResponseModel> fetchModelsForSeriesSegment(String userCode,
			ModelsForSeriesSegmentRequestModel ssModel) {
		Session session = null;
		List<ModelsForSeriesSegmentResponseModel> responseList = null;
		ModelsForSeriesSegmentResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SACT_getModelsForSeriesSegment] :userCode,:pcId,:seriesName,:segment";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", ssModel.getPcId());
			query.setParameter("seriesName", ssModel.getSeriesName());
			query.setParameter("segment", ssModel.getSegment());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ModelsForSeriesSegmentResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					response = new ModelsForSeriesSegmentResponseModel();
					response.setModelId((BigInteger) row.get("model_id"));
					response.setModelName((String) row.get("model_name"));
					response.setSeries((String) row.get("series_name"));
					response.setSegment((String) row.get("segment_name"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public List<MachineItemResponseModel> fetchItemDTLList(String userCode, MachineItemRequestModel ssModel) {
		Session session = null;
		List<MachineItemResponseModel> responseList = null;
		MachineItemResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_CM_GETMACHINEITEMNO_LIST] :userCode,:pcId,:dealerId,:branchId,"
				+ ":searchValue, :prodDivisionId, :productGrp, :isFor, :poOn, :codealerId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", ssModel.getPcId());
			query.setParameter("dealerId", ssModel.getDealerId());
			query.setParameter("branchId", ssModel.getBranchId());
			query.setParameter("prodDivisionId", ssModel.getProductDivisionId());
			query.setParameter("searchValue", ssModel.getSearchValue());
			query.setParameter("productGrp", ssModel.getProductGrp());
			query.setParameter("isFor", ssModel.getIsFor());
			query.setParameter("poOn", ssModel.getPoOn());
			query.setParameter("codealerId", ssModel.getCodealerId());
			

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<MachineItemResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new MachineItemResponseModel();
					response.setItemId((BigInteger) row.get("itemId"));
					response.setItemNumber((String) row.get("ItemNumber"));
					response.setItemDescription((String) row.get("ItemDescription"));
					response.setDisplayValue((String) row.get("displayValue"));
					response.setMrp((BigDecimal) row.get("MRP"));
					response.setNdp((BigDecimal) row.get("NDP"));
					response.setGstPer((BigDecimal) row.get("GstPer"));
					response.setGstAmnt((BigDecimal) row.get("GstAmnt"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public MachineItemResponseModel fetchItemDTL(String userCode, MachineItemDTLRequestModel ssModel) {
		Session session = null;
		MachineItemResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_CM_GETMACHINEITEMDTL] :userCode,:pcId,:dealerId,:branchId,:itemNumber, :productDivisionId, :placedToDealerId, :plantRsoId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", ssModel.getPcId());
			query.setParameter("dealerId", ssModel.getDealerId());
			query.setParameter("branchId", ssModel.getBranchId());
			query.setParameter("itemNumber", ssModel.getItemNumber());
			query.setParameter("productDivisionId", ssModel.getProductDivisionId());
			query.setParameter("placedToDealerId", ssModel.getPlacedToDealerId());
			query.setParameter("plantRsoId", ssModel.getPlantRsoId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					response = new MachineItemResponseModel();
					response.setItemId((BigInteger) row.get("itemId"));
					response.setItemNumber((String) row.get("ItemNumber"));
					response.setItemDescription((String) row.get("ItemDescription"));
					response.setDisplayValue((String) row.get("displayValue"));
					response.setVariant((String) row.get("Variant"));
					response.setMrp((BigDecimal) row.get("MRP"));
					response.setNdp((BigDecimal) row.get("NDP"));
					response.setGstPer((BigDecimal) row.get("GstPer"));
					response.setGstAmnt((BigDecimal) row.get("GstAmnt"));
				}
			}
		} catch (SQLGrammarException sqlge) {
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

		return response;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ModelItemListResponseModel> fetchModelItemList(String userCode,
			ModelItemListRequestModel modelItemListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchModelItemList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<ModelItemListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getModelItemList] :userCode, :pcID, :dealerId, :branchId, :modelID, :searchValue, :productGrp, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", modelItemListRequestModel.getPcID());
			query.setParameter("dealerId", modelItemListRequestModel.getDealerId());
			query.setParameter("branchId", modelItemListRequestModel.getBranchId());
			query.setParameter("modelID", modelItemListRequestModel.getModelID());
			query.setParameter("searchValue", modelItemListRequestModel.getSearchValue());
			query.setParameter("productGrp", modelItemListRequestModel.getProductGroup());
			query.setParameter("isFor", modelItemListRequestModel.getIsFor());
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
					responseModel.setModel((String) row.get("Model"));
					responseModel.setVariant((String) row.get("Variant"));
					responseModel.setChassisRequired((Character) row.get("chassis_no_req"));;
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
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<ModelByPcIdResponseModel> fetchModelListByPcId(String userCode,
			Integer pcId, String isFor) {
		Session session = null;
		List<ModelByPcIdResponseModel> responseList = null;
		ModelByPcIdResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_CM_GET_MODEL_FOR_PC] :userCode,:pcId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", pcId);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ModelByPcIdResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					response = new ModelByPcIdResponseModel();
					response.setMachineItemId((BigInteger) row.get("machine_item_id"));
					response.setSeries((String) row.get("series_name"));
					response.setSegment((String) row.get("segment_name"));
					response.setModelId((BigInteger) row.get("model_id"));
					response.setModel((String) row.get("model_name"));
					response.setVariant((String) row.get("Variant"));
					response.setItem((String) row.get("Item"));
					response.setItemDesc((String) row.get("ItemDesc"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
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
