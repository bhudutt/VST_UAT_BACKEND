package com.hitech.dms.web.dao.dealer.employee.create;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.dealer.employee.create.DealerEmployeeAddressEntity;
import com.hitech.dms.web.entity.dealer.employee.create.DealerEmployeeEntity;
import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeHdrRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeReportingRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeCreateResponseModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeReportingResponseModel;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class DealerEmployeeDaoImpl implements DealerEmployeeDao {

	private static final Logger logger = LoggerFactory.getLogger(DealerEmployeeDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private CommonDao commonDao;

	@Override
	public DealerEmployeeCreateResponseModel createDealerEmployee(String userCode,
			DealerEmployeeHdrRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createDealerEmployee invoked..");
		}

		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		DealerEmployeeCreateResponseModel responseModel = new DealerEmployeeCreateResponseModel();
		DealerEmployeeEntity dealerEmployeeEntity = null;
		boolean isSuccess = true;
		String msg = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dealerEmployeeEntity = mapper.map(requestModel, DealerEmployeeEntity.class,
						"dealerEmployeeEntityMapId");
				List<DealerEmployeeAddressEntity> dealerAddressList = dealerEmployeeEntity.getDealerAddress();
				mapData = fetchDealerCode(session, dealerEmployeeEntity.getDealerId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					String dealerCode = (String) mapData.get("dealerCode");
					String lastEmpCode = null;
					mapData = fetchLastDlrEmpNo(session, dealerEmployeeEntity.getDealerId());
					if (mapData != null && mapData.get("SUCCESS") != null) {
						if(mapData.get("EmpCode") != null) {
							lastEmpCode = (String) mapData.get("EmpCode");
						}
					}
					if (dealerAddressList != null && !dealerAddressList.isEmpty()) {
						Date currDate = new Date();
						String empCode = "E" + dealerCode;
						if(lastEmpCode != null && !lastEmpCode.equals("")) {
							String lastSuffix = null;
							try {
								lastSuffix = lastEmpCode.substring(dealerCode.length() + 1, lastEmpCode.length());
								Integer i = Integer.valueOf(lastSuffix);
								if(i == 0) {
									empCode = empCode + "0001";
								}else {
									empCode = empCode+String.format("%04d", i + 1);
								}
							} catch(Exception ex) {
								empCode = empCode + "0001";
							}
						}else {
							empCode = empCode + "0001";
						}						
						dealerEmployeeEntity.setEmployeeCode(empCode);
						dealerEmployeeEntity.setCreatedBy(userCode);
						dealerEmployeeEntity.setCreatedDate(currDate);
						for (DealerEmployeeAddressEntity add : dealerAddressList) {
							add.setDealerEmpHdr(dealerEmployeeEntity);
							add.setCreatedBy(userCode);
							add.setCreatedDate(currDate);
						}
						dealerEmployeeEntity.setDealerAddress(dealerAddressList);
						session.save(dealerEmployeeEntity);
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("Dealer Employee Detail Not Found.");
					}
				} else {
					isSuccess = false;
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("Dealer Detail Not Found.");
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
			if (isSuccess) {
				mapData = fetchDealerEmployeeCode(session, dealerEmployeeEntity.getEmployeeId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setEmpCode((String) mapData.get("employeeCode"));
					responseModel.setMsg("Dealer Employee Created Successful.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					responseModel.setEmployeeId(dealerEmployeeEntity.getEmployeeId());
				}
			} else {
				logger.debug(requestModel != null ? requestModel.toString() : null);
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("Error While Creating Dealer Employee.");
			}
			if (session != null) {
				session.close();
			}
		}

		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchDealerEmployeeCode(Session session, BigInteger dealerEmployeeId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select EmpCode from ADM_BP_DEALER_EMP (nolock) sacih where sacih.emp_Id =:dealerEmployeeId";
		mapData.put("ERROR", "Employee Details Not Found");
		try {
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
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastDlrEmpNo(Session session, BigInteger dealerId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Top 1 sacih.EmpCode from ADM_BP_DEALER_EMP (nolock) sacih where sacih.dealer_Id =:dealerId order by sacih.emp_Id desc ";
		mapData.put("ERROR", "Last Employee Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String empCode = null;
				for (Object object : data) {
					Map row = (Map) object;
					empCode = (String) row.get("EmpCode");
				}
				mapData.put("EmpCode", empCode);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING LAST EMPLOYEE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING LAST EMPLOYEE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchDealerCode(Session session, BigInteger dealerId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select ParentDealerCode from ADM_BP_DEALER (nolock) sacih where sacih.parent_dealer_id =:dealerId";
		mapData.put("ERROR", "Dealer Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String dealerCode = null;
				for (Object object : data) {
					Map row = (Map) object;
					dealerCode = (String) row.get("ParentDealerCode");
				}
				mapData.put("dealerCode", dealerCode);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING DEALER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING DEALER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@Override
	public List<DealerEmployeeReportingResponseModel> dealerReportingEmployee(String userCode,
			DealerEmployeeReportingRequestModel requestModel, Device device) {
		Session session = null;
		DealerEmployeeReportingResponseModel dealerEmployeeReporting = null;
		List<DealerEmployeeReportingResponseModel> dealerEmployeeReportingList = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_DLR_REPORTING_EMP] :dealerId, :searchText, :empCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("searchText", requestModel.getSearchText());
			query.setParameter("empCode", requestModel.getEmpCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				dealerEmployeeReportingList = new ArrayList<DealerEmployeeReportingResponseModel>();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					dealerEmployeeReporting = new DealerEmployeeReportingResponseModel();
					dealerEmployeeReporting.setDisplayValue((String) row.get("displayValue"));
					dealerEmployeeReporting.setReportingEmpCode((String) row.get("reportingEmpCode"));
					dealerEmployeeReporting.setReportingEmpId((BigInteger) row.get("reportingEmpID"));
					dealerEmployeeReporting.setReportingEmpName((String) row.get("reportingEmpName"));
					dealerEmployeeReportingList.add(dealerEmployeeReporting);
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
		return dealerEmployeeReportingList;
	}

}
