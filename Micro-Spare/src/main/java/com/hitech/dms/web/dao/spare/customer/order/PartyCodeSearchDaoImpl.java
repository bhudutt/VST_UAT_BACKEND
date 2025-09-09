package com.hitech.dms.web.dao.spare.customer.order;

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
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.entity.spare.delivery.challan.DeliveryChallanHdrEntity;
import com.hitech.dms.web.model.spara.customer.order.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderPartyCodeSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeModel;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

/*
*
*@Author Vivek Gupta
*/

@Repository
public class PartyCodeSearchDaoImpl implements PartyCodeSearchDao{
	
	
	private static final Logger logger = LoggerFactory.getLogger(PartyCodeSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public PartyCodeListResponseModel searchPartyCodeList(String userCode,
			PartyCodeCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyCodeList invoked.." + requestModel.toString());
		}
		Session session = null;
		PartyCodeListResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchParyCodeViewList(session, userCode, requestModel);
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
	public PartyCodeListResponseModel searchParyCodeViewList(Session session, String userCode,
			PartyCodeCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAllotList invoked.." + requestModel.toString());
		}
		Query query = null;
		//List<PartyCodeListResponseModel> resp = new ArrayList<PartyCodeListResponseModel>();
		PartyCodeListResponseModel partyCodeResp = new PartyCodeListResponseModel();
		PartyCodeModel partyCodeModel = null;
		List<PartyCodeModel> partyCodeStr =new ArrayList<PartyCodeModel>();
		Integer recordCount = 0;
		String sqlQuery = "exec [Sp_get_Customer_party_code] :barnchid,:PartyCode, :PartyTypeId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("barnchid", requestModel.getBranchId());
			query.setParameter("PartyCode", requestModel.getSearchText());
			query.setParameter("PartyTypeId", requestModel.getPartyType());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					partyCodeModel = new PartyCodeModel();
					partyCodeModel.setPartyCode((String)row.get("PartyCode"));
					partyCodeModel.setBranchPartyCodeId((BigInteger)row.get("party_branch_id"));
					partyCodeStr.add(partyCodeModel);
					recordCount++;
				}
				
				partyCodeResp.setPartyCode(partyCodeStr);
				partyCodeResp.setRecordCount(recordCount);
				
				//resp.add(partyCodeResp);
					
					
					
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
		return partyCodeResp;
	}
	
	@Override
	public List<PartyCategoryResponse> searchPartyCategoryMaster(String userCode) {
		if (logger.isDebugEnabled()) {
			//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Session session = null;
		Query query = null;
		PartyCategoryResponse responseModel=null;
		List<PartyCategoryResponse>  responseListModel= null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select * from ADM_BP_MST_PARTY_CTGRY where IsActive=:active ";
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
	public CustomerOrderPartyCodeSearchResponseModel partyDetailsById(String userCode, Integer partyBranchId) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyCodeList invoked.." + partyBranchId.toString());
		}
		Session session = null;
		CustomerOrderPartyCodeSearchResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = partyDetailById(session, userCode, partyBranchId);
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
	public CustomerOrderPartyCodeSearchResponseModel partyDetailById(Session session, String userCode, Integer partyBranchId) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAllotList invoked.." + partyBranchId.toString());
		}
		Query query = null;
		CustomerOrderPartyCodeSearchResponseModel responseModel = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_PARTY_CODE_SEARCH_CUSTOMERORDER] :PartybranchId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("PartybranchId", partyBranchId);
		
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerOrderPartyCodeSearchResponseModel();
					
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setAddress((String) row.get("Address"));
					responseModel.setPincode((String) row.get("pincode"));
					responseModel.setPinId((BigInteger) row.get("pin_id"));
					responseModel.setPartyCategoryId((BigInteger) row.get("party_category_id"));
					responseModel.setPartyCode((String) row.get("partycode"));
					responseModel.setState((String) row.get("STATE"));
					responseModel.setDistrict((String) row.get("DISTRICT"));
					responseModel.setTehsilTalukaMandal((String) row.get("TEHSIL"));
					responseModel.setCityVillage((String) row.get("CITY"));
					responseModel.setPostOffice((String) row.get("postOffice"));
					responseModel.setAddress1((String)row.get("PartyAddline1"));
					responseModel.setAddress2((String)row.get("PartyAddline2"));
					responseModel.setAddress3((String)row.get("PartyAddline3"));
					responseModel.setCountry((String)row.get("CountryDesc"));
					responseModel.setGstNo((String)row.get("GST_NUMBER"));
					responseModel.setPanNo((String)row.get("PAN_OR_TAN"));
					responseModel.setMobileNo((String) row.get("MobileNumber"));
			
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
	
	
	@Override
	public List<CustomerOrderNumberResponse> searchCustomerOrderNumber(String searchStr, String userCode) {
		if (logger.isDebugEnabled()) {
			//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Session session = null;
		Query query = null;
		CustomerOrderNumberResponse responseModel=null;
		List<CustomerOrderNumberResponse>  responseListModel= null;
		Map<String, Object> mapData = null;
		BigInteger userId = null;
		try {
			session = sessionFactory.openSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = (BigInteger) mapData.get("userId");
			}
			String sqlQuery = "select Customer_Id, Customer_Order_Number from PA_Customer_Order_HDR where Customer_Order_Number LIKE " +"'%" + searchStr + "%' and CreatedBy="+userId+" order by Customer_Order_Number desc";
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<CustomerOrderNumberResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerOrderNumberResponse();
					responseModel.setId((Integer) row.get("Customer_Id"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
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

	@SuppressWarnings("deprecation")
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
	public List<PartyCategoryResponse> partyCodeList(String searchText,String userCode) {
		
		Session session = null;
		Query query = null;
		PartyCategoryResponse responseModel=null;
		List<PartyCategoryResponse>  responseListModel= null;  
		
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [CO_Search_Party_Code] :searchText,:userCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<PartyCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyCategoryResponse();
					responseModel.setPartyCategoryId((BigInteger) row.get("party_branch_id"));
					responseModel.setPartyCategoryName((String) row.get("PartyName"));
					responseModel.setPartyCategoryCode((String) row.get("PartyCode"));
					
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

	

}
