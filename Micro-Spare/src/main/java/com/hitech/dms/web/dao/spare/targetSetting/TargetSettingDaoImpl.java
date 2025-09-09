package com.hitech.dms.web.dao.spare.targetSetting;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.targetSetting.TargetSettingDtlEntity;
import com.hitech.dms.web.entity.targetSetting.TargetSettingHdrEntity;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingDtlResponse;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingResponseModel;

@Repository
public class TargetSettingDaoImpl implements TargetSettingDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(TargetSettingDaoImpl.class);

	@Override
	public HashMap<BigInteger, String> fetchTargetFor(String userType) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Target_For] :userType";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userType", userType);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("party_category_id"), (String) row.get("PartyCategoryName"));
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

	@Override
	public List<SparePoCategoryResponse> getProductCategory(BigInteger partyCategoryId) {
		Session session = null;
		List<SparePoCategoryResponse> responseList = null;
		SparePoCategoryResponse response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [GET_PRODUCT_CATEGORY_BY_PARTYCATEGORY] :partyCategoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partyCategoryId", partyCategoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePoCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePoCategoryResponse();
					response.setPO_Category_Desc((String) row.get("PO_Category_Desc"));
					response.setPO_Category_Id((Integer) row.get("PO_Category_Id"));
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

	@Override
	public TargetSettingResponseModel saveTargetSettingData(TargetSettingHdrEntity targetSettingHdrEntity,
			List<TargetSettingDtlEntity> targetSettingDtlEntityList, BigInteger targetHdrId, String userCode) {
		boolean isSuccess = true;
		Session session = null;
		TargetSettingResponseModel responseModel = null;
		Transaction transaction = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
//		mapData.put("ERROR", "ERROR WHILE INSERTING INTO STG TABLE.");
		BigInteger id = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			responseModel = new TargetSettingResponseModel();
			if (targetSettingDtlEntityList != null) {

				TargetSettingResponseModel targetSettingResponseModel = fetchTargetData(targetHdrId, userCode,
						targetSettingHdrEntity.getTargetFor(), targetSettingHdrEntity.getProductCategory());
				if (targetSettingResponseModel.getId() != null) {
					targetSettingHdrEntity
							.setVersionNumber(targetSettingResponseModel.getVersionNumber().add(BigInteger.ONE));
					session.save(targetSettingHdrEntity);
				}
				id = (BigInteger) session.save(targetSettingHdrEntity);

				for (TargetSettingDtlEntity targetSettingDtlEntity : targetSettingDtlEntityList) {
					targetSettingDtlEntity.setTargetHdrId(id);
					session.save(targetSettingDtlEntity);

					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					responseModel.setMsg("SUCCESSFULLY INSERTED INTO Target Setting TABLE.");
				}
			}

			if (isSuccess) {
				transaction.commit();
//				responseModel = fetchTargetData(id, userCode, targetSettingHdrEntity.getTargetFor());
//				responseModel.setTargetSettingDtlResponse(
//						fetchTargetDtlData(id, userCode, targetSettingHdrEntity.getTargetFor()));
				session.clear();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(ex.getErrorCode());
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@Override
	public TargetSettingResponseModel fetchTargetData(BigInteger id, String userCode, String targetFor,
			String productCategory) {
		Session session = null;
		TargetSettingResponseModel responseModel = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Target_Setting_Details]  :id, :userCode, :targetFor, :productCategory, :flag";

		try {
			session = sessionFactory.openSession();
			String flag = "Header";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("id", id);
			query.setParameter("userCode", userCode);
			query.setParameter("targetFor", null);
			query.setParameter("productCategory", null);
//			query.setParameter("targetFor", targetFor.equalsIgnoreCase("null") ? null : targetFor);
//			query.setParameter("productCategory", productCategory.equalsIgnoreCase("null") ? null : productCategory);
			query.setParameter("flag", flag);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new TargetSettingResponseModel();
					responseModel.setId((BigInteger) row.get("target_hdr_id"));
					responseModel.setTargetNumber((String) row.get("target_number"));
					responseModel.setTargetDate((Date) row.get("target_date"));
					responseModel.setTargetFor((String) row.get("target_for"));
					responseModel.setProductCategory((String) row.get("product_category"));
					responseModel.setVersionNumber((BigInteger) row.get("version_no"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {

			if (session.isOpen())
				session.close();
		}

		return responseModel;
	}

	@Override
	public List<TargetSettingDtlResponse> fetchTargetDtlData(BigInteger id, String userCode, String targetFor,
			String productCategory) {
		Session session = null;
		TargetSettingDtlResponse responseModel = null;
		List<TargetSettingDtlResponse> responseModelList = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Target_Setting_Details] :id, :userCode, :targetFor, :productCategory, :flag";

		try {
			session = sessionFactory.openSession();
			String flag = "Detail";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("id", id);
			query.setParameter("userCode", userCode);
			query.setParameter("targetFor", null);
			query.setParameter("productCategory", null);
//			query.setParameter("targetFor", targetFor.equalsIgnoreCase("null") ? null : targetFor);
//			query.setParameter("productCategory", productCategory.equalsIgnoreCase("null") ? null : productCategory);
			query.setParameter("flag", flag);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<TargetSettingDtlResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new TargetSettingDtlResponse();
					responseModel.setPartyId((BigInteger) row.get("party_id"));
					responseModel.setParentDealerLocation((String) row.get("ParentDealerLocation"));
					responseModel.setParentDealerName((String) row.get("ParentDealerName"));
					responseModel.setDealerPincode((String) row.get("Dealer_Pincode"));
					responseModel.setPartyCode((String) row.get("ParentDealerCode"));
					responseModel.setApr((Double) row.get("apr"));
					responseModel.setMay((Double) row.get("may"));
					responseModel.setJun((Double) row.get("jun"));
					responseModel.setJul((Double) row.get("jul"));
					responseModel.setAug((Double) row.get("aug"));
					responseModel.setSep((Double) row.get("sep"));
					responseModel.setOct((Double) row.get("oct"));
					responseModel.setNov((Double) row.get("nov"));
					responseModel.setDec((Double) row.get("dec"));
					responseModel.setJan((Double) row.get("jan"));
					responseModel.setFeb((Double) row.get("feb"));
					responseModel.setMar((Double) row.get("mar"));
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
	public TargetSettingDtlResponse checkIfPartyAlreadyExist(String targetFor, String productCategory,
			BigInteger partyId) {
		Session session = null;
		TargetSettingDtlResponse responseModel = null;
		boolean isExist = false;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Target_Setting_By_Party] :targetFor, :productCategory, :partyId, :year";

		Integer year = 2023;
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("targetFor", targetFor);
			query.setParameter("productCategory", productCategory);
			query.setParameter("partyId", partyId);
			query.setParameter("year", year);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new TargetSettingDtlResponse();
					responseModel.setPartyId((BigInteger) row.get("party_id"));

					responseModel.setApr((Double) row.get("apr"));
					responseModel.setMay((Double) row.get("may"));
					responseModel.setJun((Double) row.get("jun"));
					responseModel.setJul((Double) row.get("jul"));
					responseModel.setAug((Double) row.get("aug"));
					responseModel.setSep((Double) row.get("sep"));
					responseModel.setOct((Double) row.get("oct"));
					responseModel.setNov((Double) row.get("nov"));
					responseModel.setDec((Double) row.get("dec"));
					responseModel.setJan((Double) row.get("jan"));
					responseModel.setFeb((Double) row.get("feb"));
					responseModel.setMar((Double) row.get("mar"));

					boolean isMonthDataExist = false;
					if ((Character) row.get("isMonthDataExist") == 'Y') {
						isMonthDataExist = true;
					}
					responseModel.setMonthDataExist(isMonthDataExist);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {

			if (session.isOpen())
				session.close();
		}

		return responseModel;
	}

	@Override
	public List<TargetSettingResponseModel> fetchTargetSettingData(String userCode) {
		Session session = null;
		List<TargetSettingResponseModel> responseList = null;
		TargetSettingResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [PA_Get_Target_Setting_Search] :targetFor, :useCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("targetFor", null);
			query.setParameter("useCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<TargetSettingResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new TargetSettingResponseModel();
					response.setId((BigInteger) row.get("target_hdr_id"));
					response.setTargetNumber((String) row.get("target_number"));
					response.setTargetFor((String) row.get("target_for"));
					response.setProductCategory((String) row.get("product_category"));
					response.setVersionNumber((BigInteger) row.get("version_no"));
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
