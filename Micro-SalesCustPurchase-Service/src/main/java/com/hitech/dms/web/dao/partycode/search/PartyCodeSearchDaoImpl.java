package com.hitech.dms.web.dao.partycode.search;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.integration.core.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.outletCategory.search.response.PartyMasterApproveEntity;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partycode.search.response.DealerDetailResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCategoryResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCodeSearchMainResponseModel;
import com.hitech.dms.web.model.partycode.search.response.PartyCodeSearchResponseModel;
import com.hitech.dms.web.model.partycode.search.response.PartyDetailAddressResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyMasterChangeStatusResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyNameResponseModel;

@Repository
public class PartyCodeSearchDaoImpl implements PartyCodeSearchDao {

	private static final Logger logger = LoggerFactory.getLogger(PartyCodeSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PartyCodeSearchMainResponseModel searchParyCodeViewList(Session session, String userCode,
			PartyCodeCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAllotList invoked.." + requestModel.toString()+"partyCategory1id "+requestModel.getPartyCategoryId());
		}
		BigInteger partyCategoryId=requestModel.getPartyCategoryId();
		Query query = null;
		PartyCodeSearchMainResponseModel responseListModel = null;
		List<PartyCodeSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_PARTY_CODE_SEARCH]  :categoryCode,:partyCode,:partyName ,:usercode,:status,:includeInactive,:page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("categoryCode", requestModel.getPartyCategoryId());
			query.setParameter("partyCode", requestModel.getPartyCode());
			query.setParameter("partyName", requestModel.getPartyName());
			query.setParameter("usercode", userCode);
			query.setParameter("status",requestModel.getStatus());
			query.setParameter("includeInactive","N");
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PartyCodeSearchMainResponseModel();
				responseModelList = new ArrayList<PartyCodeSearchResponseModel>();
				PartyCodeSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyCodeSearchResponseModel();
					responseModel.setPartyBranchId((BigInteger) row.get("party_branch_id"));
					BigInteger branchid=(BigInteger) row.get("Branch_ID");
					System.out.println("branchId after set get is "+branchid);
					responseModel.setBranchId(branchid);
					partyCategoryId=(BigInteger) row.get("party_category_id");
					responseModel.setPartyCategoryId(partyCategoryId);
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("Party_Location"));;
					responseModel.setContactTitle((String) row.get("ContactTitle"));
					responseModel.setFirstName((String) row.get("FirstName"));
					responseModel.setMiddleName((String) row.get("MiddleName"));
					responseModel.setLastName((String) row.get("LastName"));
					responseModel.setEmail1((String) row.get("Email1"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setDesignation((String) row.get("Designation"));
					responseModel.setPartyAddLine1((String) row.get("PartyAddLine1"));
					responseModel.setPartyAddLine2((String) row.get("PartyAddLine2"));			
					responseModel.setPartyAddLine3((String) row.get("PartyAddLine3"));	
				//	responseModel.setBranchId((BigInteger) row.get("Branch_Id"));
					responseModel.setDealerCode((String) row.get("dealerCode"));
					Character isactive=(Character) row.get("IsActive");
					recordCount=(Integer) row.get("totalRecords");
					if(isactive.equals('Y')) {
						responseModel.setStatus("Y");
					}else {
						responseModel.setStatus("N");
					}
					String partyStatus=(String) row.get("PartyStatus");
					if(partyStatus!=null)
					{
						responseModel.setPartyStatus((String) row.get("PartyStatus"));
					}
					
					if(partyCategoryId!=null && (partyCategoryId.intValue()==4||partyCategoryId.intValue()==5||partyCategoryId.intValue()==6 ||partyCategoryId.intValue()==14))
					{
					responseModel.setAction((String)row.get("Action"));
					}
					
					
					responseModelList.add(responseModel);
				}

				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchList(responseModelList);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseListModel;
	}

	@Override
	public PartyCodeSearchMainResponseModel searchPartyCodeList(String userCode,
			PartyCodeCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyCodeList invoked.." + requestModel.toString());
		}
		Session session = null;
		PartyCodeSearchMainResponseModel responseModel = null;
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
	
	
	@Override
	public List<PartyNameResponseModel> searchPartyNameList(String userCode,
			Integer categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Session session = null;
		List<PartyNameResponseModel> responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchPartyNameList(session, userCode, categoryId);
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
			String sqlQuery = "select * from ADM_BP_MST_PARTY_CTGRY where IsActive=:active and party_category_id in(1,2,3,4,5,6,14)";
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
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PartyNameResponseModel> searchPartyNameList(Session session, String userCode,
			Integer categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Query query = null;
		PartyNameResponseModel responseListModel = null;
		List<PartyNameResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [PARTY_NAME_LIST_BY_CATEGORY_ID] :usercode, :categoryId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", null);
			query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PartyNameResponseModel();
				responseModelList = new ArrayList<PartyNameResponseModel>();
				PartyNameResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyNameResponseModel();
					responseModel.setPartyNameId((Integer) row.get("Party_Master_Id"));
					responseModel.setPartyName((String) row.get("PartyName"));
					
					/*
					 * if (recordCount.compareTo(0) == 0) { recordCount = (Integer)
					 * row.get("totalRecords"); }
					 */

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
		}
		return responseModelList;
	}

	@Override
	public PartyMasterChangeStatusResponse updatePartyMasterActiveStatus(Integer id, String isActive) {
		
	    logger.info("updatePartyMasterActiveStatus is invoked .... "+ id +" "+isActive);
	    PartyMasterChangeStatusResponse status= new PartyMasterChangeStatusResponse();
	    try
	    {
	    	Session session= sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        String hql = "update ADM_BP_PARTY_BRANCH SET IsActive = :isActive WHERE party_branch_id = :id";
	        
	        Query query = session.createSQLQuery(hql);
	        query.setParameter("isActive", isActive);
	        query.setParameter("id", id);
			//query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

	        
	        int updatedRowCount = query.executeUpdate();
	        
	        transaction.commit();
	        
	        status.setStatusCode(200);
	        status.setStatusMassage("Successfully updated");
	        
	        System.out.println("Updated " + updatedRowCount + " rows.");

	    	
	    }catch(Exception e)
	    {
	    	status.setStatusCode(501);
	    	status.setStatusMassage("Status not updated Successfully internal server error");
	    }
		
		return status;
	}

	
	@Override
	public PartyMasterChangeStatusResponse updatePartyApproveStatus(PartyMasterApproveEntity request,String userCode) {

		Integer partyApprovalStatus=0;
		  logger.info("updatePartyMasterActiveStatus is invoked .... "+request);
		    PartyMasterChangeStatusResponse status= new PartyMasterChangeStatusResponse();
		    try
		    {
		    	Session session= sessionFactory.openSession();
		        Transaction transaction = session.beginTransaction();
		        String hql = "update ADM_BP_PARTY_BRANCH SET PartyStatus = :approveStatus WHERE party_branch_id = :id";
		        
		        Query query = session.createSQLQuery(hql);
		        query.setParameter("approveStatus", request.getStatus());
		        query.setParameter("id", request.getPartyBranchId());
				//query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		        int updatedRowCount = query.executeUpdate();
		        transaction.commit();
		        
		        if(updatedRowCount>=1)
		        {
		        	partyApprovalStatus=updatePartyApproval(request,userCode);
		        }
		        if(partyApprovalStatus>=1)
		        {
		        	
		        	    status.setStatusCode(200);
				        status.setStatusMassage("Successfully updated");
				        
		        }
		     System.out.println("Updated " + partyApprovalStatus + " rows.");

		    	
		    }catch(Exception e)
		    {
		    	status.setStatusCode(501);
		    	status.setStatusMassage("Status not updated Successfully internal server error");
		    }
			
			return status;

	}

	private Integer updatePartyApproval(PartyMasterApproveEntity request, String userCode) {
		
	
		Integer partyApprovalStatus=0;
		  logger.info("updatePartyApproval is invoked .... "+request);
		    PartyMasterChangeStatusResponse status= new PartyMasterChangeStatusResponse();
		    try
		    {
		    	
		    	Session session= sessionFactory.openSession();
		        Transaction transaction = session.beginTransaction();
		        String hql = "exec UPDATE_PARTY_APPROVAL :partyBranchId,:Status,:userCode,:rejectedFlag,:hoUserId,:remarks";
		        Query query = session.createSQLQuery(hql);
		        query.setParameter("partyBranchId", request.getPartyBranchId());
		        query.setParameter("Status", request.getStatus());
		        query.setParameter("userCode",userCode);
		        query.setParameter("Status", request.getStatus());
		        query.setParameter("rejectedFlag",request.getStatus().equalsIgnoreCase("approved")?'N':'Y');
		        query.setParameter("hoUserId", request.getHoUserId());
		        query.setParameter("remarks", request.getStatus());
		        query.executeUpdate();
				transaction.commit();
				partyApprovalStatus=1;
				status.setStatusCode(200);
		        status.setStatusMassage("Successfully updated");
		        System.out.println("Updated " + partyApprovalStatus + " rows.");

		    	
		    }catch(Exception e)
		    {
		    	e.printStackTrace();
		    	status.setStatusCode(501);
		    	status.setStatusMassage("Status not updated Successfully internal server error");
		    }
		    
			return partyApprovalStatus;

	}

	@Override
	public List<PartyDetailAddressResponse> getDealerAdressDetail(String pinId, String dealerCode, String branchId) {
		
		Session session = null;
		Query query = null;
		PartyDetailAddressResponse responseModel= new PartyDetailAddressResponse();
		List<PartyDetailAddressResponse> responseModelList = null;

		DealerDetailResponse responseModel1 = new DealerDetailResponse();
		List<PartyCategoryResponse>  responseListModel= null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [SP_CM_GEO_Details] :pinID";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pinID", pinId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseModelList = new ArrayList<PartyDetailAddressResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyDetailAddressResponse();
					
					responseModel.setPinID((BigInteger) row.get("pin_id"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setLocalityCode((String) row.get("localityCode"));
					responseModel.setLocalityName((String) row.get("localityName"));
					responseModel.setDisplayName((String) row.get("displayname"));
					responseModel.setCityID((BigInteger) row.get("city_id"));
					responseModel.setCityDesc((String) row.get("cityDesc"));
					responseModel.setTehsilID((BigInteger) row.get("tehsil_id"));
					responseModel.setTehsilDesc((String) row.get("tehsilDesc"));
					responseModel.setDistrictID((BigInteger) row.get("district_id"));
					responseModel.setDistricDesc((String) row.get("districtDesc"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setCountryID((BigInteger) row.get("country_id"));
					responseModel.setCountryDesc((String) row.get("countryDesc"));
					responseModelList.add(responseModel);					
				}

			}
			
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			responseModel.setStatusCode(302);
			responseModel.setStatusMessage("Not able to fetch Detail server error");
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			responseModel.setStatusCode(302);
			responseModel.setStatusMessage("Not able to fetch Detail server error");
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
				responseModel.setStatusCode(200);
				responseModel.setStatusMessage("Successfully Fecth Detail");
				
			}
		}
		return responseModelList;

		
	}
}
