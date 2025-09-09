/**
 * 
 */
package com.hitech.dms.web.dao.org;

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

import com.hitech.dms.web.model.org.request.OrgLeveDealerRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelByDeptRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelDealerBranchRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelHierForParentRequestModel;
import com.hitech.dms.web.model.org.response.OrgLeveDealerResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelByDeptResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelDealerBranchResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelHierForParentResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OrgLevelDaoImpl implements OrgLevelDao {
	private static final Logger logger = LoggerFactory.getLogger(OrgLevelDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<OrgLevelByDeptResponseModel> fetchOrgLevelListByDept(String userCode, String deptCode, Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelListByDept invoked.." + userCode);
		}
		Session session = null;
		List<OrgLevelByDeptResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchOrgLevelListByDept(session, userCode, deptCode, pcId);
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
	public List<OrgLevelByDeptResponseModel> fetchOrgLevelListByDept(Session session, String userCode, String deptCode,
			Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelListByDept invoked.." + deptCode);
		}
		Query query = null;
		List<OrgLevelByDeptResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getOrgLevelByDept] :deptCode, :pcId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("deptCode", deptCode);
			query.setParameter("pcId", pcId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OrgLevelByDeptResponseModel>();
				OrgLevelByDeptResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OrgLevelByDeptResponseModel();
					responseModel.setLevelID((Integer) row.get("LEVEL_ID"));
					responseModel.setLevelCode((String) row.get("LEVEL_CODE"));
					responseModel.setLevelDesc((String) row.get("LEVEL_DESC"));
					responseModel.setSqNo((BigInteger) row.get("SEQ_NO"));
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

	public List<OrgLevelByDeptResponseModel> fetchOrgLevelListByDept(String userCode,
			OrgLevelByDeptRequestModel orgLevelByDeptRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelListByDept invoked.." + orgLevelByDeptRequestModel.toString());
		}
		Session session = null;
		List<OrgLevelByDeptResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchOrgLevelListByDept(session, userCode, orgLevelByDeptRequestModel.getDeptCode(),
					orgLevelByDeptRequestModel.getPcId());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<OrgLevelHierForParentResponseModel> fetchOrgLevelHierForParent(String userCode, Integer levelID,
			Long orgHierID, String includeInactive, String isFor, BigInteger dealerId, Integer departmentId, Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelHierForParent invoked.." + levelID + " " + orgHierID);
		}
		Session session = null;
		List<OrgLevelHierForParentResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchOrgLevelHierForParent(session, userCode, levelID, orgHierID, includeInactive, isFor,
					dealerId, departmentId, pcId);
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
	public List<OrgLevelHierForParentResponseModel> fetchOrgLevelHierForParent(Session session, String userCode,
			Integer levelID, Long orgHierID, String includeInactive, String isFor, BigInteger dealerId, Integer departmentId, Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelHierForParent invoked.." + levelID + " " + orgHierID);
		}
		Query query = null;
		List<OrgLevelHierForParentResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getOrgLevelHierForParent] :userCode, :levelID, :orgHierID, :includeInactive, :isFor, :dealerId, :departmentId, :pcId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("levelID", levelID);
			query.setParameter("orgHierID", orgHierID);
			query.setParameter("includeInactive", includeInactive);
			query.setParameter("isFor", isFor);
			query.setParameter("dealerId", dealerId);
			query.setParameter("departmentId", departmentId);
			query.setParameter("pcId", pcId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OrgLevelHierForParentResponseModel>();
				OrgLevelHierForParentResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OrgLevelHierForParentResponseModel();
					responseModel.setOrgHierarchyID((BigInteger) row.get("org_hierarchy_id"));
					responseModel.setLevelID((Integer) row.get("level_id"));
					responseModel.setHierarchyCode((String) row.get("hierarchy_code"));
					responseModel.setHierarchyDesc((String) row.get("hierarchy_desc"));
					responseModel.setParentOrgHierarchyID((BigInteger) row.get("parent_org_hierarchy_id"));
					responseModel.setChildLevel((Integer) row.get("child_level"));
					Character isActive = (Character) row.get("isActive");
					if(isActive != null && isActive.toString().equals("Y")) {
						responseModel.setIsActive(true);
					}else {
						responseModel.setIsActive(false);
					}
					
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setDepartmentId((Integer) row.get("department_id"));
					responseModel.setDepartmentDesc((String) row.get("DepartmentDesc"));
					
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

	public List<OrgLevelHierForParentResponseModel> fetchOrgLevelHierForParent(String userCode,
			OrgLevelHierForParentRequestModel orgLevelHierForParentRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelHierForParent invoked.." + orgLevelHierForParentRequestModel.toString());
		}
		Session session = null;
		List<OrgLevelHierForParentResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchOrgLevelHierForParent(session, userCode,
					orgLevelHierForParentRequestModel.getLevelID(), orgLevelHierForParentRequestModel.getOrgHierID(),
					orgLevelHierForParentRequestModel.getIncludeInactive(), orgLevelHierForParentRequestModel.getIsFor(),
					orgLevelHierForParentRequestModel.getDealerId(), orgLevelHierForParentRequestModel.getDepartmentId(),
					orgLevelHierForParentRequestModel.getPcId());
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
	public List<OrgLeveDealerResponseModel> fetchOrgLevelDealerList(Session session, String userCode,
			BigInteger orgHierID, String includeInactive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelDealerList invoked.." + orgHierID + " " + includeInactive);
		}
		Query query = null;
		List<OrgLeveDealerResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getOrgLevelDealer] :userCode, :orgHierID, :includeInactive";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("orgHierID", orgHierID);
			query.setParameter("includeInactive", includeInactive);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OrgLeveDealerResponseModel>();
				OrgLeveDealerResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OrgLeveDealerResponseModel();
					responseModel.setDealerId((BigInteger) row.get("DEALER_ID"));
					responseModel.setDealerCode((String) row.get("DEALER_CODE"));
					responseModel.setDealerName((String) row.get("DEALER_NAME"));
					responseModel.setDealerLocation((String) row.get("DEALER_LOCATION"));
					responseModel.setDisplayName((String) row.get("DISPLAY_NAME"));

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

	public List<OrgLeveDealerResponseModel> fetchOrgLevelDealerList(String userCode,
			OrgLeveDealerRequestModel orgLeveDealerRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("orgLeveDealerRequestModel invoked.." + orgLeveDealerRequestModel.toString());
		}
		Session session = null;
		List<OrgLeveDealerResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchOrgLevelDealerList(session, userCode, orgLeveDealerRequestModel.getOrgHierID(),
					orgLeveDealerRequestModel.getIncludeInactive());
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
	public List<OrgLevelDealerBranchResponseModel> fetchOrgLevelDealerBranchList(Session session, String userCode,
			BigInteger dealerId, String includeInactive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelDealerBranchList invoked.." + dealerId + " " + includeInactive);
		}
		Query query = null;
		List<OrgLevelDealerBranchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getOrgLevelDealerBranch] :userCode, :dealerId, :includeInactive";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", dealerId);
			query.setParameter("includeInactive", includeInactive);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OrgLevelDealerBranchResponseModel>();
				OrgLevelDealerBranchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OrgLevelDealerBranchResponseModel();
					responseModel.setBranchId((BigInteger) row.get("BRANCH_ID"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setBranchLocation((String) row.get("BranchLocation"));
					responseModel.setDisplayName((String) row.get("DISPLAY_NAME"));

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

	public List<OrgLevelDealerBranchResponseModel> fetchOrgLevelDealerBranchList(String userCode,
			OrgLevelDealerBranchRequestModel orgLeveDealerBranchRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("orgLeveDealerRequestModel invoked.." + orgLeveDealerBranchRequestModel.toString());
		}
		Session session = null;
		List<OrgLevelDealerBranchResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchOrgLevelDealerBranchList(session, userCode,
					orgLeveDealerBranchRequestModel.getDealerId(),
					orgLeveDealerBranchRequestModel.getIncludeInactive());
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
	public List<OrgLevelByDeptResponseModel> getPcEnquiry(String userCode) {
				if (logger.isDebugEnabled()) {
					logger.debug("fetchOrgLevelListByDept invoked..");
				}
				Session session = null;
				List<OrgLevelByDeptResponseModel> responseModelList = null;
				try {
					session = sessionFactory.openSession();
					responseModelList = getPcEnquiry(session, userCode);
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
	public List<OrgLevelByDeptResponseModel> getPcEnquiry(Session session, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOrgLevelListByDept invoked.." + userCode);
		}
		Query query = null;
		List<OrgLevelByDeptResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getOrgLevelByDept_by_user] :userCode";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("deptCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OrgLevelByDeptResponseModel>();
				OrgLevelByDeptResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OrgLevelByDeptResponseModel();
					responseModel.setLevelID((Integer) row.get("LEVEL_ID"));
					responseModel.setLevelCode((String) row.get("LEVEL_CODE"));
					responseModel.setLevelDesc((String) row.get("LEVEL_DESC"));
					responseModel.setSqNo((BigInteger) row.get("SEQ_NO"));
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
}
