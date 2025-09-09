/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.common;

import java.math.BigInteger;
import java.util.ArrayList;
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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.entity.machinepo.MachinePOStatusEntity;
import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;
import com.hitech.dms.web.model.machinepo.orderTo.request.MachinePOOrderToRequestModel;
import com.hitech.dms.web.model.machinepo.orderTo.response.MachinePOOrderToResponseModel;
import com.hitech.dms.web.model.machinepo.plant.response.PoPlantRequstModel;
import com.hitech.dms.web.model.machinepo.plant.response.PoPlantResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePOCommonDaoImpl implements MachinePOCommonDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOCommonDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<MachinePOOrderToResponseModel> fetchPOTOTypeList(String userCode,
			MachinePOOrderToRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPOTOTypeList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<MachinePOOrderToResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SAORD_GETPOTOTYPE] :userCode, :pcId, :dealerId, :branchId, :poHdrId, :isIncludeInActive ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("poHdrId", requestModel.getPoHdrId());
			query.setParameter("isIncludeInActive", requestModel.getIncludeInActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<MachinePOOrderToResponseModel>();
				MachinePOOrderToResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachinePOOrderToResponseModel();
					responseModel.setPoOrderToId((Integer) row.get("po_to_type_id"));
					responseModel.setTypeCode((String) row.get("type_code"));
					responseModel.setTypeName((String) row.get("type_name"));
					responseModel.setApplicableToDealer((String) row.get("applicableToDealer"));
					responseModel.setApplicableToDistributor((String) row.get("applicableToDistributor"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public MachinePOOrderToResponseModel fetchPOTOTypeDTL(String userCode, Integer poOrderToId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPOTOTypeDTL invoked.." + userCode + " " + poOrderToId);
		}
		Session session = null;
		MachinePOOrderToResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchPOTOTypeDTL(session, userCode, poOrderToId);
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public MachinePOOrderToResponseModel fetchPOTOTypeDTL(Session session, String userCode, Integer poOrderToId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPOTOTypeDTL invoked.." + userCode + " " + poOrderToId);
		}
		Query query = null;
		MachinePOOrderToResponseModel responseModel = null;
		String sqlQuery = "Select * from SA_MST_PO_TO_TYPE where po_to_type_id =:poOrderToId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("poOrderToId", poOrderToId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachinePOOrderToResponseModel();
					responseModel.setPoOrderToId((Integer) row.get("po_to_type_id"));
					responseModel.setTypeCode((String) row.get("type_code"));
					responseModel.setTypeName((String) row.get("type_name"));
					responseModel.setApplicableToDealer((String) row.get("applicableToDealer"));
					responseModel.setApplicableToDistributor((String) row.get("applicableToDistributor"));
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModel;
	}

	public MachinePOStatusEntity fetchMachinePOStatusDTL(String userCode, Integer poStatusId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePOStatusDTL invoked.." + userCode + " " + poStatusId);
		}
		Session session = null;
		MachinePOStatusEntity machinePOStatusEntity = null;
		try {
			session = sessionFactory.openSession();
			machinePOStatusEntity = fetchMachinePOStatusDTL(session, userCode, poStatusId);
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return machinePOStatusEntity;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public MachinePOStatusEntity fetchMachinePOStatusDTL(Session session, String userCode, Integer poStatusId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePOStatusDTL invoked.." + userCode + " " + poStatusId);
		}
		Query query = null;
		MachinePOStatusEntity machinePOStatusEntity = null;
		String sqlQuery = "Select * from SA_MST_PO_STATUS (NOLOCK) where po_status_id =:poStatusId";
		try {
			query = session.createSQLQuery(sqlQuery).addEntity(MachinePOStatusEntity.class);
			query.setParameter("poStatusId", poStatusId);
			machinePOStatusEntity = (MachinePOStatusEntity) query.uniqueResult();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return machinePOStatusEntity;
	}

	public List<MachinePOStatusEntity> fetchMachinePOStatusList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePOStatusList invoked.." + userCode);
		}
		Session session = null;
		List<MachinePOStatusEntity> machinePOStatusList = null;
		try {
			session = sessionFactory.openSession();
			machinePOStatusList = fetchMachinePOStatusList(session, userCode);
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return machinePOStatusList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<MachinePOStatusEntity> fetchMachinePOStatusList(Session session, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePOStatusList invoked.." + userCode);
		}
		Query query = null;
		List<MachinePOStatusEntity> machinePOStatusList = null;
		String sqlQuery = "Select * from SA_MST_PO_STATUS (NOLOCK)";
		try {
			query = session.createSQLQuery(sqlQuery).addEntity(MachinePOStatusEntity.class);
			machinePOStatusList = query.list();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return machinePOStatusList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PoPlantResponseModel> fetchPOPlantList(String userCode, PoPlantRequstModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPOPlantList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<PoPlantResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SAORD_GETPLANTLIST] :userCode, :pcId, :dealerId, :branchId, :includeInactive";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PoPlantResponseModel>();
				PoPlantResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PoPlantResponseModel();
					responseModel.setPoPlantId((Integer) row.get("plantId"));
					responseModel.setPlantCode((String) row.get("plantCode"));
					responseModel.setPlantName((String) row.get("plantName"));
					responseModel.setLocation((String) row.get("location"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select HO.ho_usr_id from ADM_HO_USER (nolock) HO INNER JOIN ADM_USER (nolock) u ON u.ho_usr_id = HO.ho_usr_id where u.UserCode =:userCode";
		mapData.put("ERROR", "HO USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("ho_usr_id");
				}
				mapData.put("hoUserId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id, dlr_emp_id, ho_usr_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("userId", (BigInteger) row.get("user_id"));
					mapData.put("dlrEmpId", (BigInteger) row.get("dlr_emp_id"));
					mapData.put("hoUserId", (BigInteger) row.get("ho_usr_id"));
				}
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

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<?> fetchApprovalData(Session session, String approvalCode) {
		String sqlQuery = "select approver_level_seq, designation_level_id, grp_seq_no, 'Pending at '+dl.DesignationLevelDesc as approvalStatus,"
				+ "       isFinalApprovalStatus" + "       from SYS_APPROVAL_FLOW_MST(nolock) sf"
				+ "       inner join ADM_HO_MST_DESIG_LEVEL(nolock) dl on sf.designation_level_id=dl.ho_designation_level_id"
				+ "       where transaction_name=:approvalCode" + "       order by approver_level_seq,grp_seq_no";
		List data = null;
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("approvalCode", approvalCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return data;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public GrnTypeResponseModel fetchGrnTypeDtl(Integer grnTypeId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGrnTypeDtl invoked.." + grnTypeId);
		}
		Session session = null;
		Query query = null;
		GrnTypeResponseModel responseModel = null;
		String sqlQuery = "Select * from SA_MST_MACHINE_GRN_TYPE (nolock) where GRN_type_id =:grnTypeId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnTypeId", grnTypeId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GrnTypeResponseModel();
					responseModel.setGrnTypeId((Integer) row.get("GRN_type_id"));
					responseModel.setGrnTypeCode((String) row.get("grn_type_code"));
					responseModel.setGrnType((String) row.get("grn_type"));

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
	
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public Integer fetchB2CFlag(String invoiceNo) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchB2CFlag invoked.." + invoiceNo);
		}
		Session session = null;
		Query query = null;
		Integer flag = 0;
		String sqlQuery = "Select btocflag from SA_MACHINE_ERP_INVOICE_HDR (nolock) where invoice_number =:invoice ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("invoice", invoiceNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					if(((Integer) row.get("btocflag")) !=null) {
						flag=((Integer) row.get("btocflag"));
					}
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return flag;
	}
}
