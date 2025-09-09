package com.hitech.dms.web.dao.spare.party.mapping;

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
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingDtlEntity;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingEntity;
import com.hitech.dms.web.entity.spare.partymaster.mapping.PartyDistributorRetailerEntity;
import com.hitech.dms.web.model.spare.party.mapping.request.DealerDistributorMappingRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerDistributorModel;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerMappingHeaderResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorModel;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyMappingResponse;

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
					dealerCodeSearchResponse.setParentDealerLocation((String) row.get("ParentDealerLocation"));
					dealerCodeSearchResponse.setParentDealerName((String) row.get("ParentDealerName"));
					dealerCodeSearchResponse.setDealerPincode((String) row.get("Dealer_Pincode"));
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
					distributorDetailResponse.setDistributorCode((String) row.get("ParentDealerCode"));
					distributorDetailResponse.setDistrict((String) row.get("Dealer_district"));
					distributorDetailResponse.setTehsil((String) row.get("Dealer_tehsil"));
					distributorDetailResponse.setPinCode((String) row.get("dealer_pincode"));
					distributorDetailResponse.setPinId((BigInteger) row.get("pin_id"));
					distributorDetailResponse.setState((String) row.get("Dealer_state"));
					distributorDetailResponse.setBranchId((String) row.get("branch_id"));
					//added by vivek
					distributorDetailResponse.setDealerAddress1((String) row.get("DealerAddress1"));
					distributorDetailResponse.setDealerAddress2((String) row.get("DealerAddress2"));
					distributorDetailResponse.setDealerAddress3((String) row.get("DealerAddress3"));
					distributorDetailResponse.setDealerCity((String) row.get("Dealer_city"));
					distributorDetailResponse.setDealerCountry((String) row.get("Dealer_country"));
					distributorDetailResponse.setDealerPincode((String) row.get("Dealer_Pincode"));
					distributorDetailResponse.setMobileNumber((String) row.get("MobileNumber"));
					distributorDetailResponse.setGstNo((String) row.get("gst_no"));
					distributorDetailResponse.setPanNo((String) row.get("pan_no"));
					distributorDetailResponse.setTanNo((String) row.get("TANNo"));
					distributorDetailResponse.setCinNo((String) row.get("CINNo"));		
					
					
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

				DealerDistributorMappingEntity dealerDistributorMappingEntity = new DealerDistributorMappingEntity();

				id = checkIfDealerDistributorMappingAlreadyExist(dealerDistributorMappingRequest.getHeaderId(), 
						dealerDistributorMappingRequest.getDealerId());

				if (id == null) {
					dealerDistributorMappingEntity.setMappingToDealer(dealerDistributorMappingRequest.getHeaderId());
					dealerDistributorMappingEntity.setDealerId(dealerDistributorMappingRequest.getDealerId());
					
					DistributorDetailResponse distributorDetailResponse = 
							fetchDistributorDetails(dealerDistributorMappingRequest.getDealerId());
					
					dealerDistributorMappingEntity.setDealerName(distributorDetailResponse.getDistributorName());
					dealerDistributorMappingEntity.setDealerDistrict(distributorDetailResponse.getDistrict());
					dealerDistributorMappingEntity.setDealerState(distributorDetailResponse.getState());
					dealerDistributorMappingEntity.setCreatedDate(todayDate);
					dealerDistributorMappingEntity.setCreatedBy(userId);
					id = (BigInteger) session.save(dealerDistributorMappingEntity);
				} 
				
				
				for (DealerDistributorModel dealerDistributorModel : dealerDistributorMappingRequest
						.getDealerDistributorModelList()) {
					DealerDistributorMappingDtlEntity dealerDistributorMappingDtlEntity = new DealerDistributorMappingDtlEntity();

					isExist = checkIfDealerDistributorDtlExist(id,
							dealerDistributorModel.getDealerId());
					
					if(!isExist) {
						dealerDistributorMappingDtlEntity.setDealerId(dealerDistributorModel.getDealerId());
						dealerDistributorMappingDtlEntity.setHdrId(id);
						
						DistributorDetailResponse distributorDetailResponse = 
								fetchDistributorDetails(dealerDistributorModel.getDealerId());

						dealerDistributorMappingDtlEntity.setDealerName(distributorDetailResponse.getDistributorName());
						dealerDistributorMappingDtlEntity.setDealerCode(distributorDetailResponse.getDistributorCode());
						dealerDistributorMappingDtlEntity.setDealerPinCode(distributorDetailResponse.getPinCode());
						dealerDistributorMappingDtlEntity.setDealerDistrict(distributorDetailResponse.getDistrict());
						dealerDistributorMappingDtlEntity.setDealerState(distributorDetailResponse.getState());
						
						dealerDistributorMappingDtlEntity.setCreatedBy(userId);
						dealerDistributorMappingDtlEntity.setCreatedDate(todayDate);
						id = (BigInteger) session.save(dealerDistributorMappingDtlEntity);
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
	public boolean checkIfDealerDistributorDtlExist(BigInteger id, Integer dealerId) {
		Session session = sessionFactory.openSession();
		Query query = null;

		String sqlQuery = "select * from PA_Dealer_Distributor_Mapping_Dtl(nolock) where hdr_id =:hdrId "
				+ "and Dealer_id =:dealerId";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("hdrId", id);
		query.setParameter("dealerId", dealerId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
//				return BigInteger.valueOf((Integer) row.get("Id"));
				return true;
			}
		}

		return false;
	}

	@Override
	public BigInteger checkIfDealerDistributorMappingAlreadyExist(BigInteger mappingToId, Integer dealerId) {
		Session session = sessionFactory.openSession();
		Query query = null;

		String sqlQuery = "select * from [PA_Dealer_Distributor_Mapping](nolock) where Mapping_To_Dealer =:mappingTo and Dealer_id =:dealerId";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("mappingTo", mappingToId);
		query.setParameter("dealerId", dealerId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				BigInteger id = BigInteger.valueOf((Integer) row.get("Id"));
				return id;
			}
		}
		return null;
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
					String sqlQuery = "delete from PA_Dealer_Distributor_Mapping_Dtl where id IN(:id)";
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
	public List<DistributorMappingResponse> fetchDealerDistributorMappingList(Integer parentDealerId,
			Integer mappingToId) {
		Session session = null;
		DistributorMappingResponse distributorMappingResponse = null;
		List<DistributorMappingResponse> distributorMappingResponseList = null;
		Query query = null;
		String sqlQuery = "exec [SP_Get_Dealer_Distributor_Mapping] :mappingToId, :ParentDealerId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ParentDealerId", parentDealerId);
			query.setParameter("mappingToId", mappingToId);

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
