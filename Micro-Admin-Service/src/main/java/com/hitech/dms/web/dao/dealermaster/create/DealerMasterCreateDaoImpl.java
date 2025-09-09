package com.hitech.dms.web.dao.dealermaster.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.dealermaster.DealerMasterEntity;
import com.hitech.dms.web.model.dealermaster.create.request.DealerCreateRequestModel;
import com.hitech.dms.web.model.dealermaster.create.request.DealerListRequestModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerDivisionCodeListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerDivisionNameListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerGroupListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerListResponseModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerMasterCreateResponseModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerMasterSearchListResultResponse;
import com.hitech.dms.web.model.dealermaster.create.response.DealerNameListModel;
import com.hitech.dms.web.model.dealermaster.create.response.ProfitCenterUnderUserResponseModel;

@Repository
public class DealerMasterCreateDaoImpl implements DealerMasterCreateDao{

	private static final Logger logger = LoggerFactory.getLogger(DealerMasterCreateDaoImpl.class);
	
	@Qualifier("sessionFactoryForReports1")
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private SessionFactory parimarySessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public DealerMasterCreateResponseModel create(String userCode, DealerCreateRequestModel requestedData,
			Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("create invoked.." + userCode);
			logger.debug(requestedData.toString());
		}
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		DealerMasterCreateResponseModel responseModel = new DealerMasterCreateResponseModel();
		Query query = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			 List<DealerMasterEntity> list=requestedData.getDealerMasterEntity();
			   for(DealerMasterEntity obj:list) {
				   obj.setBatchId(BigInteger.valueOf((new Date().getTime()/1000)));
				 BigInteger id = (BigInteger) session.save(obj);
			   }
   
		if (isSuccess) {
			transaction.commit();
			session.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Dealer Master Created Successfully Check in 5 Minutes";
			responseModel.setMsg(msg);
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
	
      }
      return responseModel;
	}

	@Override
	public List<DealerListResponseModel> fetchDealerList(String userCode, Device device) {
		
		Session session = null;
		List<DealerListResponseModel> responseModelList = null;
		DealerListResponseModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "SELECT parent_dealer_id,ParentDealerCode,ParentDealerName FROM ADM_BP_DEALER";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DealerListResponseModel();
					responseModel.setDealerId((BigInteger) row.get("parent_dealer_id"));
					responseModel.setDealerCode((String) row.get("ParentDealerCode"));
					responseModel.setDealerName((String) row.get("ParentDealerName"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;

	}

	@Override
	public List<DealerGroupListModel> fetchDealerGroupList(String userCode, Device device) {
		
		Session session = null;
		List<DealerGroupListModel> responseModelList = null;
		DealerGroupListModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "select dealer_type_code,Dealer_type_desc from ADM_BP_MST_DEALER_TYPE";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerGroupListModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DealerGroupListModel();
					responseModel.setGroupId((String) row.get("dealer_type_code"));
					responseModel.setDealerGroupName((String) row.get("Dealer_type_desc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;

	}

	@Override
	public List<DealerNameListModel> fetchDealerNameList(String userCode, Device device,String dealerCode) {
		Session session = null;
		List<DealerNameListModel> responseModelList = null;
		DealerNameListModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "exec [SP_GET_DEALERNAME_BY_DEALERCODE] :DealerCode";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DealerCode", dealerCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerNameListModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DealerNameListModel();
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<DealerDivisionCodeListModel> fetchDivisionCodeList(String userCode, Device device) {
		
		Session session = null;
		List<DealerDivisionCodeListModel> responseModelList = null;
		DealerDivisionCodeListModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "select prod_divCd from ADM_BP_MST_PROD_DIVISION";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerDivisionCodeListModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DealerDivisionCodeListModel();
					responseModel.setDivisionCode((String) row.get("prod_divCd"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<DealerDivisionNameListModel> fetchDivisionNameList(String userCode, Device device) {
		
		Session session = null;
		List<DealerDivisionNameListModel> responseModelList = null;
		DealerDivisionNameListModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "exec [SP_GET_DIVISIONLIST_BY_DIVISIONCODE] ";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerDivisionNameListModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DealerDivisionNameListModel();
					responseModel.setDivisionCode((String) row.get("prod_divCd"));
					responseModel.setDivisionName((String) row.get("prod_divDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public DealerMasterSearchListResultResponse getDealerList(String userCode, Device device, DealerListRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(
					"getDealerList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		DealerMasterSearchListResultResponse responseListModel = null;
		List<DealerListModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_DEALER_LIST] :UserCode, :DealerCode, :page, :size";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("DealerCode", requestModel.getDealerCode());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
			responseListModel= new DealerMasterSearchListResultResponse();
			responseModelList=  new ArrayList<DealerListModel>();
			DealerListModel responseModel=null;
			for (Object object : data) {
				Map row = (Map) object;
				
					
					responseModel = new DealerListModel();
					responseModel.setId((BigInteger) row.get("erp_dealer_id"));
					responseModel.setAction("Edit");
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
			//		responseModel.setContactPerson((String) row.get("Dealer_Contact_Person"));
					responseModel.setAddress1((String) row.get("DealerAddress1"));
					responseModel.setAddress2((String) row.get("DealerAddress2"));
					responseModel.setDistrict((String) row.get("Dealer_district"));
					responseModel.setTehsil((String) row.get("Dealer_tehsil")); 
					responseModel.setStateCode((String) row.get("Dealer_State"));
					responseModel.setCity((String) row.get("City"));
					responseModel.setPinCode((String) row.get("Dealer_Pincode"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
			//		responseModel.setTelephoneNumber((String) row.get("PhoneNumber"));
					responseModel.setGstNumber((String) row.get("gst_no"));
					responseModel.setPanNumber((String) row.get("pan_no"));
					responseModel.setCompanyCode((String) row.get("Company_Code"));		
					responseModel.setCountry((String) row.get("Dealer_country"));
					responseModel.setMailId((String) row.get("Dealer_EMail"));
					responseModel.setBankName((String) row.get("bank_name"));
					responseModel.setBankBranchName((String) row.get("bank_branch_name"));
					responseModel.setIfscCode((String) row.get("ifsc_code"));
					responseModel.setMicrCode((String) row.get("micrCode"));
					responseModel.setTINNo((String) row.get("TANNo"));
					responseModel.setCINNo((String) row.get("CINNo"));
		//			responseModel.setERPVehPriceListCode((String) row.get("ERPVehPriceListCode"));
		//			responseModel.setERPPartsPriceListCode((String) row.get("ERPPartsPriceListCode"));
					responseModel.setSalesOrg((String) row.get("SalesOrg"));
		//			responseModel.setActivationDate((String) row.get("ActivationDate"));
					responseModel.setCreatedDate((String) row.get("CreatedDate"));
					responseModel.setDealerStatus((String) row.get("dealerStatus"));

					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(responseModel);
				}
				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchResult(responseModelList);
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
	public List<DealerListModel> fetchDealerViewList(String userCode, Device device, Integer batchId) {
		Session session = null;
		List<DealerListModel> responseModelList = null;
		DealerListModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "exec [SP_DEALER_VIEW_LIST] :BatchId";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("BatchId", batchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerListModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DealerListModel();
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
		//			responseModel.setContactPerson((String) row.get("Dealer_Contact_Person"));
					responseModel.setAddress1((String) row.get("DealerAddress1"));
					responseModel.setAddress2((String) row.get("DealerAddress2"));
					responseModel.setDistrict((String) row.get("Dealer_district"));
					responseModel.setTehsil((String) row.get("Dealer_tehsil")); 
					responseModel.setStateCode((String) row.get("State_Code"));
					responseModel.setCity((String) row.get("Dealer_city"));
					responseModel.setPinCode((String) row.get("Dealer_Pincode"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
		//			responseModel.setTelephoneNumber((String) row.get("PhoneNumber"));
					responseModel.setGstNumber((String) row.get("gst_no"));
					responseModel.setPanNumber((String) row.get("pan_no"));
		//			responseModel.setCompanyCode((String) row.get("Company_Code"));		
					responseModel.setCountry((String) row.get("Dealer_country"));
					responseModel.setMailId((String) row.get("Dealer_EMail"));
					responseModel.setBankName((String) row.get("bank_name"));
					responseModel.setBankBranchName((String) row.get("bank_branch_name"));
					responseModel.setIfscCode((String) row.get("ifsc_code"));
					responseModel.setMicrCode((String) row.get("micrCode"));
					responseModel.setTINNo((String) row.get("TANNo"));
					responseModel.setCINNo((String) row.get("CINNo"));
		//			responseModel.setERPVehPriceListCode((String) row.get("ERPVehPriceListCode"));
		//			responseModel.setERPPartsPriceListCode((String) row.get("ERPPartsPriceListCode"));
					responseModel.setSalesOrg((String) row.get("SalesOrg"));
		//			responseModel.setActivationDate((String) row.get("ActivationDate"));
					responseModel.setCreatedDate((String) row.get("CreatedDate"));
					responseModel.setDealerStatus((String) row.get("dealerStatus"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ProfitCenterUnderUserResponseModel> fetchProfitCenterList(String userCode, Device device) {
		Session session = null;
		List<ProfitCenterUnderUserResponseModel> responseModelList = null;
		ProfitCenterUnderUserResponseModel responseModel = null;
		Query<?> query = null;
		String sqlQuery = "select pc_desc from  ADM_BP_MST_PROFIT_CENTER";
		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ProfitCenterUnderUserResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ProfitCenterUnderUserResponseModel();
					responseModel.setProfitCenter((String) row.get("pc_desc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}


	

	@Override
	public DealerMasterCreateResponseModel editDealerMaster(String status, String dealerId, String userCode) {
		Session session = null;
		DealerMasterCreateResponseModel response = new DealerMasterCreateResponseModel();
		Query<?> query = null;
		
		String sqlQuery = "exec UPDATE_ERP_DEALER_STATUS :dealerStatus, :dealerId";

		try {
			session = parimarySessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			 query.setParameter("dealerStatus", status);
		     query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			 List data = query.list();
			 if (data != null && !data.isEmpty()) {
				
					for (Object object : data) {
						
						Map row = (Map) object;
						if (Integer.valueOf((String)row.get("status"))==1) {
				            response.setMsg("Dealer status updated successfully...");
				            response.setStatusCode(200);
				        } else {
				            response.setMsg("No dealer updated. Check dealer ID.");
				            response.setStatusCode(404);
				        }
					}
			  }	
		    } catch (Exception e) {
		        logger.error(this.getClass().getName(), e);
		        response.setMsg("Failed to update dealer status due to server error.");
		        response.setStatusCode(500);
		    }

		    return response;
	}
	

	
}
