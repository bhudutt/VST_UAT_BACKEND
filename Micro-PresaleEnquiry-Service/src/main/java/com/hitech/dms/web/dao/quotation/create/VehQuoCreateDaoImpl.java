/**
 * 
 */
package com.hitech.dms.web.dao.quotation.create;

import java.math.BigInteger;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.quotation.VehQuoChargesEntity;
import com.hitech.dms.web.entity.quotation.VehQuoDTLEntity;
import com.hitech.dms.web.entity.quotation.VehQuoHDREntity;
import com.hitech.dms.web.entity.quotation.VehQuoImplementEntity;
import com.hitech.dms.web.model.quotation.create.request.VehQuoHDRRequestModel;
import com.hitech.dms.web.model.quotation.create.response.VehQuoResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class VehQuoCreateDaoImpl implements VehQuoCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(VehQuoCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public VehQuoResponseModel saveVehicleQuotation(String userCode, VehQuoHDRRequestModel vehQuoHDRRequestModel,
			Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveVehicleQuotation invoked.." + vehQuoHDRRequestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		VehQuoResponseModel responseModel = new VehQuoResponseModel();
		VehQuoHDREntity vehQuoHDREntity = null;
		boolean isSuccess = true;
		String sqlQuery = "Select BranchCode from ADM_BP_DEALER_BRANCH where branch_id =:branchId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			vehQuoHDREntity = mapper.map(vehQuoHDRRequestModel, VehQuoHDREntity.class, "QuoMapId");

			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("branchId", vehQuoHDRRequestModel.getBranchId());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					String branchCode = null;
					for (Object object : data) {
						Map row = (Map) object;
						branchCode = (String) row.get("BranchCode");
					}
					// current date
					Date currDate = new Date();

					// set Temp Quotation Code
					vehQuoHDREntity.setQuotationNumber("TQN" + branchCode + currDate.getTime());

					if (isSuccess) {
						List<VehQuoChargesEntity> vehQuoChrgList = vehQuoHDREntity.getVehQuoChrgList();
						List<VehQuoDTLEntity> vehQuoDTLList = vehQuoHDREntity.getVehQuoDTLList();
						List<VehQuoImplementEntity> vehQuoImplementList = vehQuoHDREntity.getVehQuoImplementList();
						if (vehQuoChrgList != null && !vehQuoChrgList.isEmpty()) {
							for (VehQuoChargesEntity chargesEntity : vehQuoChrgList) {
								chargesEntity.setVehQuoHDR(vehQuoHDREntity);
							}
						}
						if (vehQuoDTLList != null && !vehQuoDTLList.isEmpty()) {
							for (VehQuoDTLEntity vehQuoDTLEntity : vehQuoDTLList) {
								vehQuoDTLEntity.setVehQuoHDR(vehQuoHDREntity);
							}
						}
						if (vehQuoImplementList != null && !vehQuoImplementList.isEmpty()) {
							for (VehQuoImplementEntity vehQuoImplementEntity : vehQuoImplementList) {
								vehQuoImplementEntity.setVehQuoHDR(vehQuoHDREntity);
							}
						}

						vehQuoHDREntity.setCreatedBy(userId);
						vehQuoHDREntity.setCreatedDate(currDate);
						session.save(vehQuoHDREntity);
					}
				} else {
					// branch id not found
					responseModel.setMsg("Branch Not Found.");
					isSuccess = false;
				}
			} else {
				// user not found
				responseModel.setMsg("User Not Found.");
				isSuccess = false;
			}
			if (isSuccess) {
				transaction.commit();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess && vehQuoHDREntity.getQuotationHDRId() != null) {
				mapData = fetchQuoNoByQuoDRId(vehQuoHDREntity.getQuotationHDRId());
				responseModel.setQuotationNumber(vehQuoHDREntity.getQuotationNumber());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setQuotationNumber((String) mapData.get("quotationNumber"));
				}
				responseModel.setQuotationHDRId(vehQuoHDREntity.getQuotationHDRId());
				responseModel.setMsg("Vehicle Quotation Created Successful.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Vehicle Quotation .");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchQuoNoByQuoDRId(BigInteger quoHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select quotation_number from SA_QUOTATION (nolock) q where q.Quotation_id =:quoHDRId";
		mapData.put("ERROR", "Quotation Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("quoHDRId", quoHDRId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String quotationNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					quotationNumber = (String) row.get("quotation_number");
				}
				mapData.put("quotationNumber", quotationNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING QUOTATION DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING QUOTATION DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
}
