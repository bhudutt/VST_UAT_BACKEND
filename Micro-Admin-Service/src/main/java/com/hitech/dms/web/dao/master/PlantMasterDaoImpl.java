package com.hitech.dms.web.dao.master;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.plantMaster.PoPlantMaster;
import com.hitech.dms.web.model.master.request.PoPlantMasterRequest;
import com.hitech.dms.web.model.master.response.PlantMasterModel;

@Repository
public class PlantMasterDaoImpl implements PlantMasterDao {

	private static final Logger logger = LoggerFactory.getLogger(PlantMasterDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PlantMasterModel createPlantCode(PoPlantMasterRequest poPlantMasterRequest, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(poPlantMasterRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		PlantMasterModel plantMasterModel = new PlantMasterModel();

		Map<String, Object> mapData = null;

		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String active = "N";
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				PoPlantMaster poPlantMaster = new PoPlantMaster();

				Integer poPlantId = checkIfPlantDetailsAlreadyExist(poPlantMasterRequest);

				if (poPlantId == null) {

					String sqlQuery = "Exec Create_Plant_Master :PlantCode, :PlantName, :PinCode, "
							+ ":Location, :userCode, :createdDate";

					session = sessionFactory.openSession();
					query = session.createSQLQuery(sqlQuery);
					query.setParameter("PlantName", poPlantMasterRequest.getPlantName());
					query.setParameter("PlantCode", poPlantMasterRequest.getPlantCode());
					query.setParameter("PinCode", poPlantMasterRequest.getPinCode());
					query.setParameter("Location", poPlantMasterRequest.getLocation());
					query.setParameter("userCode", userCode);
					query.setParameter("createdDate", todayDate);
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

//					poPlantMaster.setPlantName(poPlantMasterRequest.getPlantName());
//					poPlantMaster.setPlantCode(poPlantMasterRequest.getPlantCode());
//					poPlantMaster.setLocation(poPlantMasterRequest.getLocation());
//					poPlantMaster.setIsActive('Y');
//					poPlantMaster.setCreatedBy(userCode);
//					poPlantMaster.setCreatedDate(todayDate);
//					id = (BigInteger) session.save(poPlantMaster);

					List data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map row = (Map) object;
							msg = (String) row.get("Message");
							if (msg != null && msg.equalsIgnoreCase("Incorrect Pin")) {
								plantMasterModel.setMsg(msg);
								plantMasterModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
								isSuccess = false;
							}
						}
					}
				} else {
					isSuccess = false;
					plantMasterModel.setMsg("Plant Code or Plant Name already exist");
					plantMasterModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				plantMasterModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Plant Created Successfully";
				plantMasterModel.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				plantMasterModel.setMsg(msg);
				plantMasterModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			plantMasterModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			plantMasterModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			plantMasterModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return plantMasterModel;

	}

	private Integer checkIfPlantDetailsAlreadyExist(PoPlantMasterRequest poPlantMasterRequest) {
		Session session = sessionFactory.openSession();
		Query query = null;
		Integer poPlantId = null;

		PoPlantMaster poPlantMaster = new PoPlantMaster();
		String sqlQuery = "SELECT distinct po_plant_id " + "	from SA_MST_PO_PLANT(nolock)" + "	where Plant_code = "
				+ poPlantMasterRequest.getPlantCode() + " or Plant_name = '" + poPlantMasterRequest.getPlantName()
				+ "'";

		query = session.createNativeQuery(sqlQuery);

		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				poPlantId = (Integer) row.get("po_plant_id");
			}
		}

		return poPlantId;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@Override
	public List<PoPlantMasterRequest> fetchPlantMasterList() {
		Session session = null;
		List<PoPlantMasterRequest> plantMasterList = null;
		PoPlantMasterRequest poPlantMaster = null;
		Query query = null;
		String sqlQuery = "Exec Get_Plant_Master";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				plantMasterList = new ArrayList<PoPlantMasterRequest>();
				for (Object object : data) {
					Map row = (Map) object;
					poPlantMaster = new PoPlantMasterRequest();
					poPlantMaster.setPlantName((String) row.get("Plant_name"));
					poPlantMaster.setPlantCode((String) row.get("Plant_code"));
					poPlantMaster.setLocation((String) row.get("Location"));
					poPlantMaster.setPinCode((String) row.get("PinCode"));
					plantMasterList.add(poPlantMaster);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return plantMasterList;
	}
}
