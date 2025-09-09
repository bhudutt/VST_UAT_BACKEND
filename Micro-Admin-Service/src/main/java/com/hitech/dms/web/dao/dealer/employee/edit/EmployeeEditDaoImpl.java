/**
 * 
 */
package com.hitech.dms.web.dao.dealer.employee.edit;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.dealer.employee.create.DealerEmployeeAddressEntity;
import com.hitech.dms.web.entity.dealer.employee.create.DealerEmployeeEntity;
import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeHdrRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeCreateResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EmployeeEditDaoImpl implements EmployeeEditDao {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeEditDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings("deprecation")
	@Override
	public DealerEmployeeCreateResponseModel updateDealerEmployee(String userCode,
			DealerEmployeeHdrRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateDealerEmployee invoked..");
		}
		logger.info(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		DealerEmployeeCreateResponseModel responseModel = new DealerEmployeeCreateResponseModel();
		DealerEmployeeEntity dealerEmployeeEntity = null;
		boolean isSuccess = true;
		String sqlQuery = "Select * from ADM_BP_DEALER_EMP (nolock) where emp_Id =:empId";

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dealerEmployeeEntity = mapper.map(requestModel, DealerEmployeeEntity.class,
						"dealerEmployeeEntityMapId");

				query = session.createNativeQuery(sqlQuery).addEntity(DealerEmployeeEntity.class);
				query.setParameter("empId", requestModel.getEmployeeId());
				DealerEmployeeEntity dealerEmployeeDBEntity = (DealerEmployeeEntity) query.uniqueResult();
				if (dealerEmployeeDBEntity != null) {
					List<DealerEmployeeAddressEntity> addressDBList = dealerEmployeeDBEntity.getDealerAddress();

					// clone db entity
					DealerEmployeeEntity cloneEmpDBEntity = (DealerEmployeeEntity) dealerEmployeeDBEntity.clone();
					List<DealerEmployeeAddressEntity> cloneAddressDBList = dealerEmployeeDBEntity.getDealerAddress();

					dealerEmployeeDBEntity = new DealerEmployeeEntity(dealerEmployeeEntity);
					dealerEmployeeDBEntity.setEmployeeId(cloneEmpDBEntity.getEmployeeId());
					dealerEmployeeDBEntity.setEmployeeCode(cloneEmpDBEntity.getEmployeeCode());
					dealerEmployeeDBEntity.setCreatedBy(cloneEmpDBEntity.getCreatedBy());
					dealerEmployeeDBEntity.setCreatedDate(cloneEmpDBEntity.getCreatedDate());

					List<DealerEmployeeAddressEntity> dealerAddressList = dealerEmployeeDBEntity.getDealerAddress();
					if (dealerAddressList != null && !dealerAddressList.isEmpty()) {
						Date currDate = new Date();
						dealerEmployeeDBEntity.setModifiedBy(userCode);
						dealerEmployeeDBEntity.setModifiedDate(currDate);
						for (int k = 0; k < dealerAddressList.size(); k++) {
							DealerEmployeeAddressEntity add = dealerAddressList.get(k);
							DealerEmployeeAddressEntity addressEntity = null;
							if (cloneAddressDBList != null && !cloneAddressDBList.isEmpty()
									&& cloneAddressDBList.size() > 0) {
								addressEntity = cloneAddressDBList.get(k);
								add.setEmpAddressId(addressEntity.getEmpAddressId());
								add.setModifiedBy(userCode);
								add.setModifiedDate(currDate);
								add.setCreatedBy(cloneEmpDBEntity.getCreatedBy());
								add.setCreatedDate(cloneEmpDBEntity.getCreatedDate());
							} else {
								add.setCreatedBy(userCode);
								add.setCreatedDate(cloneEmpDBEntity.getCreatedDate());
							}
							add.setDealerEmpHdr(dealerEmployeeDBEntity);
						}
						dealerEmployeeDBEntity.setDealerAddress(dealerAddressList);

						/*
						 * sqlQuery = "select isActive from ADM_USER where UserCode=:userCode"; query =
						 * session.createNativeQuery(sqlQuery); query.setParameter("userCode",
						 * dealerEmployeeDBEntity.getEmployeeCode()); List<?> data = (List<?>)
						 * query.list(); // System.out.println("data::::"+data); if (!data.isEmpty()) {
						 * if (data.get(0).equals('N')) { dealerEmployeeDBEntity.setIsActive('N'); }
						 * else { dealerEmployeeDBEntity.setIsActive(requestModel.getIsActive()); } }
						 */
						dealerEmployeeDBEntity.setIsActive(requestModel.getIsActive());
						session.merge(dealerEmployeeDBEntity);
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("Dealer Employee Detail Not Found.");
					}
				}
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				mapData = fetchDealerEmployeeCode(dealerEmployeeEntity.getEmployeeId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setEmpCode((String) mapData.get("employeeCode"));
					responseModel.setMsg("Dealer Employee Updated Successful.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					responseModel.setEmployeeId(dealerEmployeeEntity.getEmployeeId());
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("Error While Creating Dealer Employee.");
			}
		}

		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchDealerEmployeeCode(BigInteger dealerEmployeeId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select EmpCode from ADM_BP_DEALER_EMP (nolock) sacih where sacih.emp_Id =:dealerEmployeeId";
		mapData.put("ERROR", "Employee Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dealerEmployeeId", dealerEmployeeId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String employeeCode = null;
				for (Object object : data) {
					Map row = (Map) object;
					employeeCode = (String) row.get("EmpCode");
				}
				mapData.put("employeeCode", employeeCode);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING EMPLOYEE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING EMPLOYEE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
}
