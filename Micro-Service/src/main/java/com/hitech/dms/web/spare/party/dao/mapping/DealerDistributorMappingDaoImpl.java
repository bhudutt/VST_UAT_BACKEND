package com.hitech.dms.web.spare.party.dao.mapping;

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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.baymaster.create.BayMasterDaoImpl;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingDtlEntity;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingEntity;
import com.hitech.dms.web.entity.spare.partymaster.mapping.PartyDistributorRetailerEntity;
import com.hitech.dms.web.model.baymaster.responselist.BayMasterResponseModel;
import com.hitech.dms.web.model.baymaster.responselist.BayTypeModel;
import com.hitech.dms.web.spare.party.model.mapping.request.DealerDistributorMappingRequest;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorModel;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerMappingHeaderResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorModel;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyMappingResponse;

@Repository
public class DealerDistributorMappingDaoImpl implements DealerDistributorMappingDao {

	private static final Logger logger = LoggerFactory.getLogger(DealerDistributorMappingDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Override
	public List<DealerMappingHeaderResponse> fetchHeaderList(String lookupTypeCode) {

		Session session = null;
		List<DealerMappingHeaderResponse> dealerMappingHeaderResponseList = null;
		DealerMappingHeaderResponse dealerMappingHeaderResponse = null;
		Query<DealerMappingHeaderResponse> query = null;
		String sqlQuery = "select lookup_id, LookupVal, DisplayOrder from SYS_LOOKUP where LookupTypeCode = 'DI-DS-MAP'";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				dealerMappingHeaderResponseList = new ArrayList<DealerMappingHeaderResponse>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					dealerMappingHeaderResponse = new DealerMappingHeaderResponse();
					dealerMappingHeaderResponse.setValueId((BigInteger) row.get("lookup_id"));
					dealerMappingHeaderResponse.setValueCode((String) row.get("LookupVal"));
					dealerMappingHeaderResponse.setDisplayValue((Integer) row.get("DisplayOrder"));

					dealerMappingHeaderResponseList.add(dealerMappingHeaderResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return dealerMappingHeaderResponseList;
	}

	@Override
	public List<DealerCodeSearchResponse> searchDealerDistributor(String isFor, String searchText, String userCode) {
		Session session = null;
		DealerCodeSearchResponse dealerCodeSearchResponse = null;
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = null;
		Query query = null;
		String sqlQuery = "exec [SP_Get_Dealer_List] :Isfor, :searchText";

		try {
			session = sessionFactory.openSession();
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Isfor", isFor);
			query.setParameter("searchText", searchText);
//			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				dealerCodeSearchResponseList = new ArrayList<DealerCodeSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					dealerCodeSearchResponse = new DealerCodeSearchResponse();
					dealerCodeSearchResponse.setParent_dealer_id((BigInteger) row.get("parent_dealer_id"));
					dealerCodeSearchResponse.setDistributorCode((String) row.get("parentdealercode"));
					dealerCodeSearchResponse.setDealer((String) row.get("value"));
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
	public DistributorDetailResponse fetchDistributorDetails(Integer distributorId) {
		Session session = null;
		DistributorDetailResponse distributorDetailResponse = null;
		Query query = null;
		
		String sqlQuery = "exec [SP_Get_DealerDetails] :ParentDealerId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ParentDealerId", distributorId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					distributorDetailResponse = new DistributorDetailResponse();
					distributorDetailResponse.setDistributorName((String) row.get("ParentDealerName"));
					distributorDetailResponse.setDistrict((String) row.get("Dealer_district"));
					distributorDetailResponse.setTehsil((String) row.get("Dealer_tehsil"));
					distributorDetailResponse.setPinCode((String) row.get("dealer_pincode"));
					distributorDetailResponse.setState((String) row.get("Dealer_state"));
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
	public List<DealerCodeSearchResponse> searchDistributorList(Integer parentDealerId, String searchText, String isFor, String userCode) {
		Session session = null;
		DealerCodeSearchResponse dealerCodeSearchResponse = null;
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = null;
		Query query = null;
		String sqlQuery = "exec [SP_Get_Dealer_Distributor_Search] :ParentDealerId, :Isfor, :SearchText";

		try {
			session = sessionFactory.openSession();
//			String isFor = "Dealer";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ParentDealerId", parentDealerId);
			query.setParameter("Isfor", isFor);
			query.setParameter("SearchText", searchText);
//			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				dealerCodeSearchResponseList = new ArrayList<DealerCodeSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					dealerCodeSearchResponse = new DealerCodeSearchResponse();
					dealerCodeSearchResponse.setParent_dealer_id((BigInteger) row.get("parent_dealer_id"));
					dealerCodeSearchResponse.setDistributorCode((String) row.get("ParentDealerCode"));
					dealerCodeSearchResponse.setDealer((String) row.get("value"));
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
	public DealerDistributorMappingResponse createDealerDistributorMapping(String userCode,
			DealerDistributorMappingRequest dealerDistributorMappingRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("dealer distributor mapping invoked.." + userCode);
			logger.debug(dealerDistributorMappingRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		DealerDistributorMappingResponse dealerDistributorMappingResponse = new DealerDistributorMappingResponse();
		List<DealerDistributorMappingResponse> dealerDistributorMappingResponseList = null;
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

				for (DealerDistributorModel dealerDistributorModel : dealerDistributorMappingRequest
						.getDealerDistributorModelList()) {
					DealerDistributorMappingEntity dealerDistributorMappingEntity = new DealerDistributorMappingEntity();
					DealerDistributorMappingDtlEntity dealerDistributorMappingDtlEntity = new DealerDistributorMappingDtlEntity();

					id = checkIfDealerDistributorDtlExist(dealerDistributorMappingRequest.getHeaderId(),
							dealerDistributorMappingRequest.getDealerId());
					
					if(id == null) {
						dealerDistributorMappingDtlEntity.setDealerId(dealerDistributorMappingRequest.getDealerId());
						dealerDistributorMappingDtlEntity.setHdrId(dealerDistributorMappingRequest.getHeaderId());
						
						DistributorDetailResponse distributorDetailResponse = 
								fetchDistributorDetails(dealerDistributorMappingRequest.getDealerId());

						dealerDistributorMappingDtlEntity.setDealerName(distributorDetailResponse.getDistributorName());
						dealerDistributorMappingDtlEntity.setDealerCode(distributorDetailResponse.getDistributorCode());
						dealerDistributorMappingDtlEntity.setDealerPinCode(distributorDetailResponse.getPinCode());
						dealerDistributorMappingDtlEntity.setDealerDistrict(distributorDetailResponse.getDistrict());
						dealerDistributorMappingDtlEntity.setDealerState(distributorDetailResponse.getState());
						
						dealerDistributorMappingDtlEntity.setCreatedBy(userId);
						dealerDistributorMappingDtlEntity.setCreatedDate(todayDate);
						id = (BigInteger) session.save(dealerDistributorMappingDtlEntity);
					}
					isExist = checkIfDealerDistributorMappingAlreadyExist(id, 
							dealerDistributorModel.getDealerId());

					if (!isExist) {
						dealerDistributorMappingEntity.setMappingToDealer(id);
						dealerDistributorMappingEntity.setDealerId(dealerDistributorModel.getDealerId());
						
						DistributorDetailResponse distributorDetailResponse = 
								fetchDistributorDetails(dealerDistributorModel.getDealerId());
						
						dealerDistributorMappingEntity.setDealerName(distributorDetailResponse.getDistributorName());
						dealerDistributorMappingEntity.setDealerDistrict(distributorDetailResponse.getDistrict());
						dealerDistributorMappingEntity.setDealerState(distributorDetailResponse.getState());
						dealerDistributorMappingEntity.setCreatedDate(todayDate);
						dealerDistributorMappingEntity.setCreatedBy(userId);
						session.save(dealerDistributorMappingEntity);
					} else {
						isSuccess = false;
						msg = "Dealer Distributor mapping already exist";
						dealerDistributorMappingResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						dealerDistributorMappingResponse.setMsg(msg);
					}

				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
//					partyMappingResponse.setBayCode(.getBayCode());
				dealerDistributorMappingResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Dealer Distributor Mapping Created Successfully";
				dealerDistributorMappingResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				dealerDistributorMappingResponse.setStatusCode(statusCode);
				dealerDistributorMappingResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			dealerDistributorMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			dealerDistributorMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			dealerDistributorMappingResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return dealerDistributorMappingResponse;

	}

	@Override
	public BigInteger checkIfDealerDistributorDtlExist(Integer headerId, Integer dealerId) {
		Session session = sessionFactory.openSession();
		Query query = null;

		String sqlQuery = "select * from PA_Dealer_Distributor_Mapping_Dtl where hdr_id =:hdrId "
				+ "and Dealer_id =:dealerId";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("hdrId", headerId);
		query.setParameter("dealerId", dealerId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				return BigInteger.valueOf((Integer) row.get("Id"));
			}
		}

		return null;
	}

	@Override
	public boolean checkIfDealerDistributorMappingAlreadyExist(BigInteger id, Integer dealerId) {
		Session session = sessionFactory.openSession();
		Query query = null;

		String sqlQuery = "select * from [PA_Dealer_Distributor_Mapping] where Mapping_To_Dealer =:mappingTo and Dealer_id =:dealerId";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("mappingTo", id);
		query.setParameter("dealerId", dealerId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				id = BigInteger.valueOf((Integer) row.get("Id"));
				return true;
			}
		}
		return false;
	}

	@Override
	public DealerDistributorMappingResponse deleteDealerDistributorMapping(List<Integer> id) {
			if (logger.isDebugEnabled()) {
					logger.debug("delete party mapping invoked..");
					logger.debug(id.toString());
				}
				Session session = null;
				Transaction transaction = null;
				String msg = null;
				int statusCode = 0;
				Map<String, Object> mapData = null;
				DealerDistributorMappingResponse dealerDistributorMappingResponse = new DealerDistributorMappingResponse();
				Date todayDate = new Date();
				Query query = null;
				boolean isSuccess = true;
				BigInteger userId = null;
				try {
					String sqlQuery = "delete from PA_Dealer_Distributor_Mapping where id IN(:id)";
					session = sessionFactory.openSession();
					query = session.createSQLQuery(sqlQuery);
					transaction = session.beginTransaction();
//					mapData = fetchUserDTLByUserCode(session, userCode);
//					if (mapData != null && mapData.get("SUCCESS") != null) {
//						userId = (BigInteger) mapData.get("userId");
						query = session.createSQLQuery(sqlQuery);
						query.setParameter("id", id);
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

						int queryUpdate = query.executeUpdate();
						if(queryUpdate == 0) {
							isSuccess = false;
						}

//					}
					
				if (isSuccess) {
					transaction.commit();
					session.close();
					dealerDistributorMappingResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
					msg = "Dealer Distributor Mapping Deleted Successfully";
					dealerDistributorMappingResponse.setMsg(msg);
				} else {
					transaction.commit();
					session.close();
					dealerDistributorMappingResponse.setStatusCode(statusCode);
					dealerDistributorMappingResponse.setMsg(msg);
				}
				} catch (SQLGrammarException ex) {
					if (transaction != null) {
						transaction.rollback();
					}
					isSuccess = false;
					dealerDistributorMappingResponse.setMsg(ex.getMessage());
					logger.error(this.getClass().getName(), ex);
				} catch (HibernateException ex) {
					if (transaction != null) {
						transaction.rollback();
					}
					isSuccess = false;
					dealerDistributorMappingResponse.setMsg(ex.getMessage());
					logger.error(this.getClass().getName(), ex);
				} catch (Exception ex) {
					if (transaction != null) {
						transaction.rollback();
					}
					isSuccess = false;
					dealerDistributorMappingResponse.setMsg(ex.getMessage());
					logger.error(this.getClass().getName(), ex);
				} finally {
					if (session != null) {
						session.close();
					}
				}
				return dealerDistributorMappingResponse;
		
		}

	@Override
	public List<DistributorMappingResponse> fetchDealerDistributorMappingList(Integer parentDealerId, Integer hdrId) {
		Session session = null;
		DistributorMappingResponse distributorMappingResponse = null;
		List<DistributorMappingResponse> distributorMappingResponseList = null;
		Query query = null;
		String sqlQuery = "exec [SP_Get_Dealer_Distributor_Mapping] :hdrId, :ParentDealerId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ParentDealerId", parentDealerId);
			query.setParameter("hdrId", hdrId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				distributorMappingResponseList = new ArrayList<DistributorMappingResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					distributorMappingResponse = new DistributorMappingResponse();
					distributorMappingResponse.setId((Integer) row.get("Id"));
					distributorMappingResponse.setDistributorName((String) row.get("Dealer_Name"));
					distributorMappingResponse.setDistributorCode((String) row.get("Dealer_Code"));
					distributorMappingResponse.setDistrict((String) row.get("Dealer_district"));
					distributorMappingResponse.setTehsil((String) row.get("Dealer_tehsil"));
					distributorMappingResponse.setPinCode((String) row.get("Dealer_Pincode"));
					distributorMappingResponse.setState((String) row.get("Dealer_state"));
					distributorMappingResponseList.add(distributorMappingResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return distributorMappingResponseList;
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
