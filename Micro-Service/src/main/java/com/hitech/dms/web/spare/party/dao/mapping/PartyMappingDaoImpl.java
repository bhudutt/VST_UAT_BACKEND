package com.hitech.dms.web.spare.party.dao.mapping;

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
import com.hitech.dms.web.entity.spare.partymaster.mapping.PartyDistributorRetailerEntity;
import com.hitech.dms.web.model.baymaster.create.request.BayMasterRequest;
import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingRequest;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorModel;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyMappingResponse;

@Repository
public class PartyMappingDaoImpl implements PartyMappingDao {

	private static final Logger logger = LoggerFactory.getLogger(PartyMappingDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<PartyCategoryResponse> fetchPartyCategoryMaster(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyNameList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		PartyCategoryResponse responseModel = null;
		List<PartyCategoryResponse> responseListModel = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select * from ADM_BP_MST_PARTY_CTGRY where"
					+ " PartyCategoryCode in('MECH', 'RET', 'PLUM') and IsActive=:active ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("active", 'Y');
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				responseListModel = new ArrayList<PartyCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyCategoryResponse();
					responseModel.setPartyCategoryId((BigInteger) row.get("party_category_id"));
					responseModel.setPartyCategoryName((String) row.get("PartyCategoryName"));
					responseModel.setPartyCategoryCode((String) row.get("PartyCategoryCode"));

					responseListModel.add(responseModel);
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
		return responseListModel;
	}

	@Override
	public List<PartyCodeSearchResponse> searchPartyCodeByPartyCategory(int partyCategoryId, String searchText,
			String userCode) {
		Session session = null;
		PartyCodeSearchResponse partyCodeSearchResponse = null;
		List<PartyCodeSearchResponse> partyCodeSearchResponseList = null;
		Query query = null;
		String sqlQuery = "exec [sp_get_party_category_search] :partycategoryId, :partycategory, :UserCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partycategoryId", partyCategoryId);
			query.setParameter("partycategory", searchText);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partyCodeSearchResponseList = new ArrayList<PartyCodeSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					partyCodeSearchResponse = new PartyCodeSearchResponse();
					partyCodeSearchResponse.setPartyCode((String) row.get("PartyCode"));
					partyCodeSearchResponse.setPartyBranchId((BigInteger) row.get("party_branch_id"));
					partyCodeSearchResponseList.add(partyCodeSearchResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partyCodeSearchResponseList;
	}

	@Override
	public PartyDetailResponse fetchPartyDetailsByPartyCode(int partyBranchId) {
		Session session = null;
		PartyDetailResponse partyDetailResponse = null;
		List<PartyDetailResponse> partyDetailResponseList = null;
		Query query = null;
		String sqlQuery = "exec [sp_get_party_branch_details] :partybranchid";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partybranchid", partyBranchId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partyDetailResponseList = new ArrayList<PartyDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					partyDetailResponse = new PartyDetailResponse();
					partyDetailResponse.setPartyName((String) row.get("PartyName"));
					partyDetailResponse.setPartyStatus((Character) row.get("IsActive"));
					partyDetailResponse.setPartyLocation((String) row.get("PartyLocation"));
					partyDetailResponse.setPartyMobileNo((String) row.get("MobileNumber"));
					partyDetailResponse.setPartyEmailId((String) row.get("Email"));
					partyDetailResponse.setGstNo((String) row.get("GSTNUMBER"));
					partyDetailResponse.setDistrict((String) row.get("District"));
					partyDetailResponse.setTehsil((String) row.get("Tehsil"));
					partyDetailResponse.setVillage((String) row.get("Village"));
					partyDetailResponse.setPincode((String) row.get("PinCode"));
					partyDetailResponse.setState((String) row.get("State"));
					partyDetailResponse.setCountry((String) row.get("Country"));

//					partyDetailResponseList.add(partyDetailResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partyDetailResponse;
	}

	@Override
	public List<DealerCodeSearchResponse> searchDealers(String dealer, String userCode) {
		Session session = null;
		DealerCodeSearchResponse dealerCodeSearchResponse = null;
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = null;
		Query query = null;
		String sqlQuery = "exec [Sp_Get_Distributor_List] :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", dealer);
//			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				dealerCodeSearchResponseList = new ArrayList<DealerCodeSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					dealerCodeSearchResponse = new DealerCodeSearchResponse();
					dealerCodeSearchResponse.setDealer((String) row.get("value"));
					dealerCodeSearchResponse.setDistributorCode((String) row.get("code"));
					dealerCodeSearchResponse.setParent_dealer_id((BigInteger) row.get("parent_dealer_id"));
					dealerCodeSearchResponseList.add(dealerCodeSearchResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return dealerCodeSearchResponseList;
	}

	@Override
	public DistributorDetailResponse fetchDealerDetailsBydealer(int parentDealerId) {
		Session session = null;
		DistributorDetailResponse distributorDetailResponse = null;
		List<DistributorDetailResponse> distributorDetailResponseList = null;
		Query query = null;
		String sqlQuery = "exec [sp_get_distributor_details] :parentDealerId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("parentDealerId", parentDealerId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				distributorDetailResponseList = new ArrayList<DistributorDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					distributorDetailResponse = new DistributorDetailResponse();
					distributorDetailResponse.setDistributorName((String) row.get("ParentDealerName"));
					distributorDetailResponse.setDistributorCode((String) row.get("ParentDealerCode"));
					distributorDetailResponse.setDistrict((String) row.get("District"));
					distributorDetailResponse.setTehsil((String) row.get("Tehsil"));
					distributorDetailResponse.setPinCode((String) row.get("Pincode"));
					distributorDetailResponse.setState((String) row.get("State"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return distributorDetailResponse;
	}

	@Override
	public PartyMappingResponse createPartyMapping(String userCode, PartyMappingRequest partyMappingRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("createBayMaster invoked.." + userCode);
			logger.debug(partyMappingRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		PartyMappingResponse partyMappingResponse = new PartyMappingResponse();
		List<PartyMappingResponse> partyMappingResponseList = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String active = "N";
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				for (DistributorModel distributorModel : partyMappingRequest.getDistributorList()) {
					PartyDistributorRetailerEntity partyDistributorRetailerEntity = new PartyDistributorRetailerEntity();

					boolean isExist = checkIfPartyMappingAlreadyExist(partyMappingRequest.getPartyBranchId(),
							partyMappingRequest.getPartyCategoryId(), distributorModel.getDealerId());

					if (!isExist) {
						partyDistributorRetailerEntity.setPartyCategoryId(partyMappingRequest.getPartyCategoryId());
						partyDistributorRetailerEntity.setPartyBranchId(partyMappingRequest.getPartyBranchId());
						partyDistributorRetailerEntity.setDealerId(distributorModel.getDealerId());
						partyDistributorRetailerEntity.setIsActive(distributorModel.isActive());
						partyDistributorRetailerEntity.setCreatedBy(userId);
						partyDistributorRetailerEntity.setCreatedDate(todayDate);

						session.save(partyDistributorRetailerEntity);
					} else {
						isSuccess = false;
						msg = "Party mapping already exist";
						partyMappingResponse.setStatusCode(statusCode);
						partyMappingResponse.setMsg(msg);
					}
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
//					partyMappingResponse.setBayCode(.getBayCode());
				partyMappingResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Party Distributor Mapping Created Successfully";
				partyMappingResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				partyMappingResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				partyMappingResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			partyMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			partyMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			partyMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return partyMappingResponse;

	}

	@Override
	public boolean checkIfPartyMappingAlreadyExist(Integer partyBranchId, 
			Integer partyCategoryId, Integer dealerId) {
		Session session = sessionFactory.openSession();
		Query query = null;

		String sqlQuery = "select * from [PA_DISTRIBUTOR_RETAILER_MAPPING] where Party_category_id "
				+ "=:partyCategoryId and Party_branch_id=:partyBranchId and Dealer_id =:dealerId";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("partyCategoryId", partyCategoryId);
		query.setParameter("partyBranchId", partyBranchId);
		query.setParameter("dealerId", dealerId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				BigInteger id = BigInteger.valueOf((Integer) row.get("Id"));
				return true;
			}
		}
		return false;
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
	public PartyMappingResponse deletePartyMapping(List<Integer> id, String isActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete party mapping invoked..");
			logger.debug(id.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		Map<String, Object> mapData = null;
		PartyMappingResponse partyMappingResponse = new PartyMappingResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger userId = null;
		try {
			String sqlQuery = "delete from PA_DISTRIBUTOR_RETAILER_MAPPING where id IN(:id)";
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			transaction = session.beginTransaction();
//				mapData = fetchUserDTLByUserCode(session, userCode);
//				if (mapData != null && mapData.get("SUCCESS") != null) {
//					userId = (BigInteger) mapData.get("userId");
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("id", id);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			int queryUpdate = query.executeUpdate();
			if (queryUpdate == 0) {
				isSuccess = false;
			}

//				}

			if (isSuccess) {
				transaction.commit();
				session.close();
				partyMappingResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Party Mapping Deleted Successfully";
				partyMappingResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				partyMappingResponse.setStatusCode(statusCode);
				partyMappingResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			partyMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			partyMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			partyMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return partyMappingResponse;

	}

	@Override
	public List<PartyDistributorMappingResponse> fetchPartyMappingList(Integer partyCategoryId, Integer partyBranchId) {
		Session session = null;
		PartyDistributorMappingResponse partyDistributorMappingResponse = null;
		List<PartyDistributorMappingResponse> partyDistributorMappingResponseList = null;
		Query query = null;
		String sqlQuery = "exec [sp_get_distributor_Mapping_list] :Party_category_id, :Party_branch_id";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Party_category_id", partyCategoryId);
			query.setParameter("Party_branch_id", partyBranchId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partyDistributorMappingResponseList = new ArrayList<PartyDistributorMappingResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					partyDistributorMappingResponse = new PartyDistributorMappingResponse();
					partyDistributorMappingResponse.setId((Integer) row.get("Id"));
					partyDistributorMappingResponse.setDistributorName((String) row.get("ParentDealerName"));
					partyDistributorMappingResponse.setDistributorCode((String) row.get("ParentDealerCode"));
					partyDistributorMappingResponse.setDistrict((String) row.get("District"));
					partyDistributorMappingResponse.setTehsil((String) row.get("Tehsil"));
					partyDistributorMappingResponse.setPinCode((String) row.get("Pincode"));
					partyDistributorMappingResponse.setState((String) row.get("State"));
					partyDistributorMappingResponseList.add(partyDistributorMappingResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partyDistributorMappingResponseList;
	}
}
