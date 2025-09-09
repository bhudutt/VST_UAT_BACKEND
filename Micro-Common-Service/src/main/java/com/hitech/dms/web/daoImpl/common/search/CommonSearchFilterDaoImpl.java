package com.hitech.dms.web.daoImpl.common.search;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.common.search.CommonFilterPcDao;
import com.hitech.dms.web.model.common.search.CommonFilterDealerModel;
import com.hitech.dms.web.model.common.search.CommonFilterDealerResponse;
import com.hitech.dms.web.model.common.search.CommonFilterFinalResponse;
import com.hitech.dms.web.model.common.search.CommonFilterHoModel;
import com.hitech.dms.web.model.common.search.CommonFilterModel;
import com.hitech.dms.web.model.common.search.CommonFilterPcListRes;
import com.hitech.dms.web.model.common.search.CommonFilterStateModel;
import com.hitech.dms.web.model.common.search.CommonFilterZoneModel;
import com.hitech.dms.web.model.common.search.CommonTerritoryModel;
import com.hitech.dms.web.model.common.search.DistrictResponse;
import com.hitech.dms.web.model.common.search.PcListCommon;
import com.hitech.dms.web.model.org.response.OrgLevelDealerBranchResponseModel;

@Repository
public class CommonSearchFilterDaoImpl implements CommonFilterPcDao {

	@Autowired
	private SessionFactory sessionFactory;
	private static final Logger logger = LoggerFactory.getLogger(CommonSearchFilterDaoImpl.class);

	@Override
	public CommonFilterPcListRes getPcListCommon(CommonFilterModel request, String userCode) {

		CommonFilterPcListRes response = new CommonFilterPcListRes();
		List<PcListCommon> pcList = null;
		Session session = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;

		sqlQuery = "select * from ADM_BP_MST_PROFIT_CENTER";
		try {
			session = sessionFactory.openSession();
			sqlQuery = "select * from ADM_BP_MST_PROFIT_CENTER";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			pcList = new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					PcListCommon model = new PcListCommon();
					model.setPcId((Integer) row.get("pc_id"));
					model.setPcCode((String) row.get("pc_code"));
					model.setPcDesc((String) row.get("pc_desc"));
					pcList.add(model);
					isSuccess = true;
					// response.setPcList(pcList);

				}

			}
		} catch (SQLGrammarException exp) {
			response.setStatusCode(301);
			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(301);
			isSuccess = false;
			response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			response.setStatusCode(301);
			response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				response.setStatusCode(200);
				response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {
				session.close();
			}
		}

		response.setPcList(pcList);

		// System.out.println("before send response PC "+response);

		return response;

	}

	@Override
	public List<CommonFilterHoModel> getCommonHoList(CommonFilterModel request, String userCode) {

		List<CommonFilterHoModel> response = null;

		Session session = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;

		// sqlQuery="select * from ADM_BP_MST_PROFIT_CENTER";
		try {
			session = sessionFactory.openSession();
			sqlQuery = "exec [GET_CM_COMMON_USER_LIST] :UserCode,:Flag,:PcId,:LevelId,:ZoneId,:StateId,:DealerId";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setParameter("UserCode", userCode);
			query.setParameter("Flag", request.getFlag());
			query.setParameter("PcId", request.getPcId());
			query.setParameter("LevelId", request.getLevelId());
			query.setParameter("ZoneId", request.getZoneId());
			query.setParameter("StateId", request.getStateId());
			query.setParameter("DealerId", 0);

			response = new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					CommonFilterHoModel model = new CommonFilterHoModel();
					// model.setHierId((java.math.BigInteger) row.get("org_hierarchy_id"));
					// model.setHierarchyCode((String)row.get("hierarchy_code"));
					model.setHierarchyDesc((String) row.get("hierarchy_desc"));
					model.setHierarchyCode((String) row.get("hierarchy_code"));
					model.setLevelId((Integer) row.get("level_id"));
					response.add(model);
					isSuccess = true;

				}

			}
		} catch (SQLGrammarException exp) {
//			response.setStatusCode(301);
//			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			// response.setStatusCode(301);
			isSuccess = false;
			// response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			// response.setStatusCode(301);
			// response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				// response.setStatusCode(200);
				// response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {
				session.close();
			}
		}

		// response.setPcList(pcList);

		// System.out.println("before send response Ho"+response);

		return response;

	}

	@Override
	public List<CommonFilterZoneModel> getCommonZoneList(CommonFilterModel request, String userCode) {

		List<CommonFilterZoneModel> zoneModelList = null;
		Session session = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;

		// sqlQuery="select * from ADM_BP_MST_PROFIT_CENTER";
		try {
			session = sessionFactory.openSession();
			sqlQuery = "exec [GET_CM_COMMON_USER_LIST] :UserCode,:Flag,:PcId,:LevelId,:ZoneId,:StateId,:DealerId";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setParameter("UserCode", userCode);
			query.setParameter("Flag", request.getFlag());
			query.setParameter("PcId", request.getPcId());
			query.setParameter("LevelId", request.getLevelId());
			query.setParameter("ZoneId", request.getZoneId());
			query.setParameter("StateId", request.getStateId());
			query.setParameter("DealerId", 0);

			zoneModelList = new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					CommonFilterZoneModel model = new CommonFilterZoneModel();
					model.setOrgZoneHierarchyId((BigInteger) row.get("org_Zone_hierarchy_id"));
					model.setLevelId((Integer) row.get("level_id"));
					model.setOrgHierarchyId((BigInteger) row.get("org_hierarchy_id"));
					model.setZoneCode((String) row.get("Zone_CODE"));
					model.setZoneDesc((String) row.get("Zone_DESC"));
					zoneModelList.add(model);
					isSuccess = true;

				}

			}
		} catch (SQLGrammarException exp) {
//			response.setStatusCode(301);
//			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			// response.setStatusCode(301);
			isSuccess = false;
			// response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			// response.setStatusCode(301);
			// response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				// response.setStatusCode(200);
				// response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {
				session.close();
			}
		}
		List<CommonFilterZoneModel> uniqueList = zoneModelList.stream().collect(Collectors
				.toMap(CommonFilterZoneModel::getZoneDesc, model -> model, (existing, replacement) -> existing))
				.values().stream().collect(Collectors.toList());

		System.out.println("before send response zone unique " + uniqueList);

		return uniqueList;

	}

	@Override
	public List<CommonFilterStateModel> getCommonStateList(CommonFilterModel request, String userCode) {

		List<CommonFilterStateModel> stateList = null;
		Session session = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;

		// sqlQuery="select * from ADM_BP_MST_PROFIT_CENTER";
		try {
			session = sessionFactory.openSession();
			sqlQuery = "exec [GET_CM_COMMON_USER_LIST] :UserCode,:Flag,:PcId,:LevelId,:ZoneId,:StateId,:DealerId";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setParameter("UserCode", userCode);
			query.setParameter("Flag", request.getFlag());
			query.setParameter("PcId", request.getPcId());
			query.setParameter("LevelId", request.getLevelId());
			query.setParameter("ZoneId", request.getZoneId());
			query.setParameter("StateId", request.getStateId());
			query.setParameter("DealerId", 0);

			stateList = new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					CommonFilterStateModel model = new CommonFilterStateModel();

					model.setStateId((java.math.BigInteger) row.get("state_id"));
					model.setOrgStateId((Integer) row.get("Org_State_Id"));
					model.setStateCode((String) row.get("State_CODE"));
					model.setStateDesc((String) row.get("State_desc"));
					stateList.add(model);
					isSuccess = true;

				}

			}
		} catch (SQLGrammarException exp) {
//			response.setStatusCode(301);
//			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			// response.setStatusCode(301);
			isSuccess = false;
			// response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			// response.setStatusCode(301);
			// response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				// response.setStatusCode(200);
				// response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {
				session.close();
			}
		}

		// System.out.println("before send response state "+stateList);

		return stateList;
	}

	@Override
	public List<CommonTerritoryModel> getCommonTerritoryList(CommonFilterModel request, String userCode) {

		List<CommonTerritoryModel> territoryList = null;
		Session session = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;

		// sqlQuery="select * from ADM_BP_MST_PROFIT_CENTER";
		try {
			session = sessionFactory.openSession();
			sqlQuery = "exec [GET_CM_COMMON_USER_LIST] :UserCode,:Flag,:PcId,:LevelId,:ZoneId,:StateId,:DealerId";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setParameter("UserCode", userCode);
			query.setParameter("Flag", request.getFlag());
			query.setParameter("PcId", request.getPcId());
			query.setParameter("LevelId", request.getLevelId());
			query.setParameter("ZoneId", request.getZoneId());
			query.setParameter("StateId", request.getStateId());
			query.setParameter("DealerId", 0);

			territoryList = new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					CommonTerritoryModel model = new CommonTerritoryModel();
					model.setTerritoryId((BigInteger) row.get("Territory_id"));
					model.setStateId((Integer) row.get("state_id"));
					model.setTerritoryCode((String) row.get("Territory_CODE"));
					model.setTerritoryDesc((String) row.get("Territory_desc"));
					territoryList.add(model);
					isSuccess = true;

				}

			}
		} catch (SQLGrammarException exp) {
//			response.setStatusCode(301);
//			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			// response.setStatusCode(301);
			isSuccess = false;
			// response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			// response.setStatusCode(301);
			// response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				// response.setStatusCode(200);
				// response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {
				session.close();
			}
		}

		List<CommonTerritoryModel> uniqueList = territoryList.stream().collect(Collectors
				.toMap(CommonTerritoryModel::getTerritoryDesc, model -> model, (existing, replacement) -> existing))
				.values().stream().collect(Collectors.toList());

		System.out.println("before send response territory unique " + uniqueList);

		return uniqueList;
	}

	@Override
	public CommonFilterDealerResponse getCommonDealerList(CommonFilterModel request, String userCode) {

		logger.info("common Dealer invoked =>{}" + request + " userCode " + userCode);
		List<CommonFilterDealerModel> dealerList = null;
		CommonFilterDealerResponse response = new CommonFilterDealerResponse();
		Session session = null;
		Query query = null;
		String sqlQuery = null;
		BigInteger orgHierId;
		boolean isSuccess = true;

		// sqlQuery="select * from ADM_BP_MST_PROFIT_CENTER";
		try {
			session = sessionFactory.openSession();
			sqlQuery = "exec [GET_CM_COMMON_USER_LIST] :UserCode,:Flag,:PcId,:LevelId,:ZoneId,:StateId,:DealerId";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setParameter("UserCode", userCode);
			query.setParameter("Flag", request.getFlag());
			query.setParameter("PcId", request.getPcId());
			query.setParameter("LevelId", request.getLevelId());
			query.setParameter("ZoneId", request.getZoneId());
			query.setParameter("StateId", request.getStateId());
			query.setParameter("DealerId", 0);

			dealerList = new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					CommonFilterDealerModel model = new CommonFilterDealerModel();
					model.setParentDealerId((BigInteger) row.get("parent_dealer_id"));
					model.setDealerStateId((BigInteger) row.get("Dealer_State_ID"));
					model.setParentDealerCode((String) row.get("ParentDealerCode"));
					model.setParentDealerName((String) row.get("ParentDealerName"));
					dealerList.add(model);
					isSuccess = true;
					if (isSuccess) {
						orgHierId = getOrgHierarchyId(request, userCode, session);
						if (orgHierId != null) {

							response.setOrgHierId(orgHierId);
						}
					}

				}
				response.setDealerList(dealerList);

			}
		} catch (SQLGrammarException exp) {
			response.setStatusCode(301);
			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(301);
			isSuccess = false;
			response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			response.setStatusCode(301);
			response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				response.setStatusCode(200);
				response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {
				session.close();
			}
		}

		return response;
	}

	private BigInteger getOrgHierarchyId(CommonFilterModel request, String userCode, Session session) {

		BigInteger orgHierId = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;

		// sqlQuery="select * from ADM_BP_MST_PROFIT_CENTER";
		try {

			sqlQuery = "exec [GET_CM_COMMON_USER_hierarchy] :UserCode,:Flag,:TerritoryId,:HierarchyCode";

			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setParameter("UserCode", userCode);
			query.setParameter("Flag", request.getFlag());
			query.setParameter("TerritoryId", request.getTerritoryId());
			query.setParameter("HierarchyCode", request.getHierarchyCode());
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					orgHierId = (BigInteger) row.get("org_hierarchy_id");
					System.out.println("orgHierId " + orgHierId);
					isSuccess = true;

				}

			}
		} catch (SQLGrammarException exp) {

			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);

			isSuccess = false;
		} catch (Exception exp) {

			isSuccess = false;
		} finally {

			if (isSuccess = true) {
				// response.setStatusCode(200);
				// response.setMessage("Pc List fetched successfully");

			}
			if (session != null) {

			}
		}

		return orgHierId;
	}

	@Override
	public CommonFilterFinalResponse getCommonFilterAllResponse(BigInteger pcId, String userCode) {
		CommonFilterFinalResponse response = new CommonFilterFinalResponse();
		Session session = null;

		Query query = null;
		String sqlQuery = null;
		boolean isSuccess = true;
		BigInteger dealerId = null;
		List<Map<String, Object>> hoObjectList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> zoneObjectList =new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> stateObjectList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> territoryObjectList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> dealeroObjectList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> branchList = new ArrayList<Map<String, Object>>();

		sqlQuery = "WITH RankedResults AS (SELECT a.*,bd.parentdealername,ROW_NUMBER() OVER (PARTITION BY a.level_id ORDER BY LEVEL_ID) AS RowRank"
				+ "    FROM FN_CM_GetOrgHierForBranchesUnderUser('" + userCode
				+ "', 'N') AS a INNER JOIN adm_bp_dealer BD ON BD.parent_dealer_id = a.dealer_id WHERE pc_id = " + pcId
				+ ")" + "	SELECT * FROM RankedResults WHERE RowRank = 1;";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			int i = 1;
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {

					Map row = (Map) object;
					if (i == 1) {
						Map<String, Object> hoObject = new HashMap<String,Object>();
						hoObject.put("dealerId", row.get("dealer_id"));
						hoObject.put("pcId", row.get("pc_id"));
						hoObject.put("hoId", row.get("org_hierarchy_id"));
						hoObject.put("levelId", row.get("level_id"));
						hoObject.put("hoCode", row.get("hierarchy_code"));
						hoObject.put("hoDesc", row.get("hierarchy_desc"));
						hoObject.put("parentdealername", row.get("parentdealername"));
						hoObjectList.add(hoObject);
						response.setHoObject(hoObjectList);

					}

					if (i == 2) {
						Map<String, Object> zoneObject = new HashMap<String,Object>();
						zoneObject.put("dealerId", row.get("dealer_id"));
						zoneObject.put("pcId", row.get("pc_id"));
						zoneObject.put("zoneId", row.get("org_hierarchy_id"));
						zoneObject.put("levelId", row.get("level_id"));
						zoneObject.put("zoenCode", row.get("hierarchy_code"));
						zoneObject.put("zoneDesc", row.get("hierarchy_desc"));
						zoneObject.put("parentdealername", row.get("parentdealername"));
						zoneObjectList.add(zoneObject);
						response.setZoneObject(zoneObjectList);

					}

					if (i == 3) {
						Map<String, Object> stateObject = new HashMap<String,Object>();
						stateObject.put("dealerId", row.get("dealer_id"));
						stateObject.put("pcId", row.get("pc_id"));
						stateObject.put("stateId", row.get("org_hierarchy_id"));
						stateObject.put("levelId", row.get("level_id"));
						stateObject.put("stateCode", row.get("hierarchy_code"));
						stateObject.put("stateDesc", row.get("hierarchy_desc"));
						stateObject.put("parentdealername", row.get("parentdealername"));
						stateObjectList.add(stateObject);
						response.setStateObject(stateObjectList);

					}
					if (i == 4) {
						Map<String, Object> territoryObject = new HashMap<String,Object>();
						dealerId = (BigInteger) row.get("dealer_id");
						territoryObject.put("dealerId", row.get("dealer_id"));
						territoryObject.put("pcId", row.get("pc_id"));
						territoryObject.put("levelId", row.get("level_id"));
						territoryObject.put("territoryId", row.get("org_hierarchy_id"));
						territoryObject.put("territoryCode", row.get("hierarchy_code"));
						territoryObject.put("territoryDesc", row.get("hierarchy_desc"));
						territoryObject.put("parentdealername", row.get("parentdealername"));
						territoryObjectList.add(territoryObject);
						response.setTerritoryObject(territoryObjectList);
						if (dealerId != null) {
							branchList = fetchOrgLevelDealerBranchList(session, userCode, dealerId, "N");
							response.setBranchList(branchList);

						}

					}

					isSuccess = true;
					i++;

				}

			}
		} catch (SQLGrammarException exp) {
			response.setStatusCode(301);
			response.setMessage(exp.getMessage());
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(301);
			isSuccess = false;
			response.setMessage(exp.getMessage());
		} catch (Exception exp) {
			response.setStatusCode(301);
			response.setMessage(exp.getMessage());
			isSuccess = false;
		} finally {

			if (isSuccess) {
				response.setStatusCode(200);
				response.setMessage("Pc List fetched successfully");
			} else {
				response.setStatusCode(404);
				response.setMessage("Pc List Not Found !!!");
			}
			if (session != null) {
				session.close();
			}
		}

		return response;

	}

	public List<Map<String, Object>> fetchOrgLevelDealerBranchList(Session session, String userCode,
			BigInteger dealerId, String includeInactive) {

		List<Map<String, Object>> branchObjectList = new ArrayList<Map<String, Object>>();
		Map<String, Object> branchObject = new HashMap<String, Object>();
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
					branchObject.put("branch_id", row.get("BRANCH_ID"));
					branchObject.put("branch_code", row.get("BRANCH_ID"));
					branchObject.put("branch_name", row.get("BranchName"));
					branchObject.put("branch_location", row.get("BranchLocation"));
					branchObject.put("display_name", row.get("DISPLAY_NAME"));

					branchObjectList.add(branchObject);

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
		return branchObjectList;
	}

	@Override
	public List<DistrictResponse> getCommonDistrictByStateIdResponse(BigInteger stateId, String userCode) {
	    List<DistrictResponse> distList = new ArrayList<>();
	    try (Session session = sessionFactory.openSession()) {
	        String sqlQuery = "SELECT district_id, DistrictCode, DistrictDesc FROM CM_GEO_DIST WHERE state_id=:stateId";
	        List<Map<String, Object>> results = session.createSQLQuery(sqlQuery)
	                .setParameter("stateId", stateId)
	                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
	                .list();

	        for (Map<String, Object> row : results) {
	        	DistrictResponse model = new DistrictResponse();
	            model.setId((BigInteger) row.get("district_id"));
	            model.setDistrictCode((String) row.get("DistrictCode"));
	            model.setDistrictDesc((String) row.get("DistrictDesc"));
	            distList.add(model);
	        }
	    } catch (HibernateException e) {
	        logger.error("Error fetching state list", e);
	    } catch (Exception e) {
	        logger.error("Unexpected error", e);
	    }
	    return distList;
	}

}
