/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.exchange.update;

import java.math.BigInteger;
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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.customer.CustomerHDREntity;
import com.hitech.dms.web.entity.oldvehicle.OldVehicleInvEntity;
import com.hitech.dms.web.model.enquiry.exchange.request.ExchangeVehicleUpdateRequestModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleUpdateResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ExchangeVehicleUpdateDaoImpl implements ExchangeVehicleUpdateDao {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeVehicleUpdateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ExchangeVehicleUpdateResponseModel updateExchangeVeh(String userCode,
			ExchangeVehicleUpdateRequestModel exchangeVehicleUpdateRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateExchangeVeh invoked.." + userCode);
			logger.debug(exchangeVehicleUpdateRequestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		ExchangeVehicleUpdateResponseModel exchangeVehicleUpdateResponseModel = new ExchangeVehicleUpdateResponseModel();
		String sqlQuery = "Select BranchCode from ADM_BP_DEALER_BRANCH where branch_id =:branchId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				CustomerHDREntity customerHDREntity = null;
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("branchId", exchangeVehicleUpdateRequestModel.getBranchId());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {

					sqlQuery = "Select * from SA_OLD_VEHICLE_INV (nolock) s where s.Branch_Id =:branchId and enquiry_id =:enquiryId and old_veh_id =:oldVehId";
					query = session.createNativeQuery(sqlQuery).addEntity(OldVehicleInvEntity.class);
					query.setParameter("branchId", exchangeVehicleUpdateRequestModel.getBranchId());
					query.setParameter("enquiryId", exchangeVehicleUpdateRequestModel.getEnquiryId());
					query.setParameter("oldVehId", exchangeVehicleUpdateRequestModel.getOldVehId());
					OldVehicleInvEntity oldVehicleInvDBEntity = (OldVehicleInvEntity) query.uniqueResult();
					if (oldVehicleInvDBEntity != null) {
						// update table & merge it

						// validate mobile is exist in customer table or not
						// fetch Customer Details using mobile number
						mapData = fetchCustomerDTLBtMobileNo(session,
								exchangeVehicleUpdateRequestModel.getBuyerContactNumber());
						if (mapData != null && mapData.get("SUCCESS") != null) {
							customerHDREntity = (CustomerHDREntity) mapData.get("customerHDREntity");
							if (customerHDREntity != null) {
								// update customer id in old veh table
								oldVehicleInvDBEntity.setBuyerId(customerHDREntity.getCustomerId());

							} else {
								// only update number , name in old veh table
								oldVehicleInvDBEntity.setBuyerName(exchangeVehicleUpdateRequestModel.getBuyerName());
							}
							oldVehicleInvDBEntity.setBrandName("");
							oldVehicleInvDBEntity.setBuyerName(exchangeVehicleUpdateRequestModel.getBuyerName());
							oldVehicleInvDBEntity
									.setBuyerContactNo(exchangeVehicleUpdateRequestModel.getBuyerContactNumber());
							oldVehicleInvDBEntity.setSellingPrice(exchangeVehicleUpdateRequestModel.getSellingPrice());
							oldVehicleInvDBEntity.setStatus(WebConstants.SOLD);
							oldVehicleInvDBEntity.setSaleDate(exchangeVehicleUpdateRequestModel.getSaleDate());
							oldVehicleInvDBEntity.setSaleRemarks(exchangeVehicleUpdateRequestModel.getSaleRemarks());
							oldVehicleInvDBEntity.setModifiedBy(userId);
							oldVehicleInvDBEntity.setModifiedDate(new Date());

							session.merge(oldVehicleInvDBEntity);
							transaction.commit();

							exchangeVehicleUpdateResponseModel.setMsg("Old Vehicle Inventoy Susscessfully Updated.");
							exchangeVehicleUpdateResponseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
						} else {
							// error while validating buyer contact number
							exchangeVehicleUpdateResponseModel.setMsg("Error While Validating Buyer Contact Number");
						}
					} else {
						// Data not found in Old VehTable
						exchangeVehicleUpdateResponseModel.setMsg("Data not found in Old Vehicle Inventory.");
					}
				} else {
					// branch not found.
					exchangeVehicleUpdateResponseModel.setMsg("Branch Not Found.");
				}
			} else {
				// user not found
				exchangeVehicleUpdateResponseModel.setMsg("User Not Found");
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
			exchangeVehicleUpdateResponseModel.setMsg("Server Side Error, Contact Your System Administrator.");
			exchangeVehicleUpdateResponseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
			exchangeVehicleUpdateResponseModel.setMsg("Server Side Error, Contact Your System Administrator.");
			exchangeVehicleUpdateResponseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
			exchangeVehicleUpdateResponseModel.setMsg("Server Side Error, Contact Your System Administrator.");
			exchangeVehicleUpdateResponseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return exchangeVehicleUpdateResponseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchCustomerDTLBtMobileNo(Session session, String mobileNo) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		CustomerHDREntity customerHDREntity = null;
		Query query = null;
		String sqlQuery = "Select TOP 1 * from CM_CUST_HDR (nolock) ch where ch.mobile_no =:mobileNo order by Customer_Id desc";
		try {
			query = session.createNativeQuery(sqlQuery).addEntity(CustomerHDREntity.class);
			query.setParameter("mobileNo", mobileNo);
			customerHDREntity = (CustomerHDREntity) query.uniqueResult();
			mapData.put("customerHDREntity", customerHDREntity);
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING CUSTOMER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING CUSTOMER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
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
}
