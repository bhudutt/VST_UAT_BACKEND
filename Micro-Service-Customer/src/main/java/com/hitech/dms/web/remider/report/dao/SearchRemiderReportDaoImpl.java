package com.hitech.dms.web.remider.report.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.service.report.request.RemiderSearchBean;
import com.hitech.dms.web.model.service.report.request.ReminderReportReq;
import com.hitech.dms.web.model.service.report.request.ServicePlanStatusBean;
import com.hitech.dms.web.model.service.report.request.ServiceSearchReportBean;
import com.hitech.dms.web.model.service.report.response.ChessisDetails;
import com.hitech.dms.web.model.service.report.response.CustomerDtlResponse;
import com.hitech.dms.web.model.service.report.response.ModelResponse;
import com.hitech.dms.web.model.service.report.response.ServiceTypeResponse;
import com.hitech.dms.web.model.service.report.response.ZoneResponse;

@Repository
public class SearchRemiderReportDaoImpl implements SearchRemiderReportDao{
	
	
	private static final Logger logger = LoggerFactory.getLogger(SearchRemiderReportDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<ZoneResponse> getZoneList(String userCode) {
	
		Session session = null;
		Query query = null;
		ZoneResponse responseModel=null;
		List<ZoneResponse>  responseListModel= null;
		String sqlQuery = "exec [PA_Get_Zone_Inv_report] :userCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new ZoneResponse();
					
					responseModel.setZoneName((String)row.get("Zone"));
					
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
	public List<CustomerDtlResponse> fetchCustomerName(String userCode, ReminderReportReq requestModel) {
		
		Session session = null;
		Query query = null;
		CustomerDtlResponse responseModel=null;
		List<CustomerDtlResponse>  responseListModel= null;
	
		String sqlQuery = "exec [sv_service_remainder_report_customer_search] :userCode, :searchText ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", requestModel.getSearchText());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new CustomerDtlResponse();
					
					responseModel.setId((BigInteger) row.get("customerId"));
//					responseModel.setFlag((String) row.get("Flag"));
					responseModel.setCustomerName((String)row.get("customerCode")+" | "+(String) row.get("customerName"));
					responseModel.setCustomerCode((String)row.get("customerCode"));
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
	public List<CustomerDtlResponse> getDealerList(String userCode, ReminderReportReq requestModel) {
	
			Session session = null;
			Query query = null;
			CustomerDtlResponse responseModel=null;
			List<CustomerDtlResponse>  responseListModel= null;
			String sqlQuery = "exec [pa_service_remainder_report_Dealer_search] :userCode, :searchText";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("searchText", requestModel.getSearchText());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					
					responseListModel = new ArrayList<>();
					for (Object object : data) {
						Map row = (Map) object;
						
						responseModel = new CustomerDtlResponse();
						
						responseModel.setId((BigInteger) row.get("Id"));
						responseModel.setCustomerName((String)row.get("CustomerCode")+" | "+(String) row.get("CustomerName"));
						responseModel.setCustomerCode((String)row.get("CustomerCode"));
						responseModel.setCustomerCode((String)row.get("CustomerCode"));
						responseModel.setParentDealerId((BigInteger)row.get("parentDealerId"));
						
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
	public List<ChessisDetails> vinDetailsByChassisNo(String chassisNo, String userCode) {
		
		Session session = null;
		Query query = null;
		ChessisDetails responseModel=null;
		List<ChessisDetails>  responseListModel= null;
	
		String sqlQuery = "exec sp_service_remainder_search_by_chassis :userCode,:chassisNo";
		
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("chassisNo", chassisNo);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new ChessisDetails();
					
					responseModel.setCode((String) row.get("code"));
					responseModel.setValue((String) row.get("value"));
					responseModel.setChessisNumber((String)row.get("chessis_no"));
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
	public List<ServiceSearchReportBean> getsearchDetails(String userCode, RemiderSearchBean bean) {
		
		Session session = null;
		Query query = null;
		ServiceSearchReportBean responseModel=null;
		List<ServiceSearchReportBean>  responseListModel= null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String sqlQuery = "exec Service_Reminder_Report :Usercode,:dealerId, :CustomerId, :stateId, :ModelId, :ChassisNo, :FromDate, :ToDate, :ServiceName, :branchId, :page, :size ";
		
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Usercode", userCode);
			query.setParameter("dealerId", bean.getDealerId());
			query.setParameter("CustomerId", bean.getCustomerId());
			query.setParameter("stateId", bean.getStateId());
			query.setParameter("ModelId", bean.getModelId());
			query.setParameter("ChassisNo", bean.getChassisNo());
			query.setParameter("FromDate", formatter.format(bean.getFromDate()));
			query.setParameter("ToDate",  formatter.format(DateToStringParserUtils.addDayByOne(bean.getToDate())));
			query.setParameter("ServiceName", bean.getServiceName());
			query.setParameter("branchId", bean.getBranchId());
			query.setParameter("page", bean.getPage());
			query.setParameter("size", bean.getSize());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new ServiceSearchReportBean();
	
					responseModel.setEngineNo((String) row.get("EngineNo"));
					responseModel.setChassisNo((String) row.get("ChassisNo"));		
				    responseModel.setItemNo((String) row.get("ItemNumber"));
				    responseModel.setItemDescription((String) row.get("ItemDescription"));
				    responseModel.setModelName((String) row.get("Model"));
				    responseModel.setParentDealerCode((Integer) row.get("DealerCode"));
				    responseModel.setParentDealerName((String) row.get("DealerName"));
				    responseModel.setDealerState((String) row.get("DealerState"));
				    responseModel.setCustomerName((String) row.get("CustomerName"));
				    responseModel.setAddress1((String) row.get("CustomerAddress"));
				    responseModel.setMobileNo((String) row.get("MobileNo"));
				    responseModel.setPinCode((String) row.get("PinCode"));
				    responseModel.setCityDesc((String) row.get("City"));
				    responseModel.setTehsilDesc((String) row.get("Tehsil"));
				    responseModel.setDistrictDesc((String) row.get("CustomerDistrict"));
				    responseModel.setStateDesc((String) row.get("DealerState"));
				    responseModel.setDeliveryChallanDate((Date) row.get("DeliveryChallanDate"));
				    responseModel.setPreviousServiceName((String)row.get("PreviousServiceName"));
				    responseModel.setPreviousServiceDate((Date)row.get("PreviousServiceDate"));
				    responseModel.setPreviousServiceHours((Integer)row.get("PreviousServiceHours"));
				    responseModel.setNextServiceName((String)row.get("NextServiceName"));
				    responseModel.setNextServiceDate((Date)row.get("NextServiceDate"));
				    responseModel.setNoOfDaysFromDateOfDelivery((Integer)row.get("NoOfDaysFromDateOfDelivery"));
				    
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
	public List<ModelResponse> getModelList(String userCode, Device device) {

			Session session = null;
			Query query = null;
			List<ModelResponse> responseModelList = null;
			ModelResponse responseModel = null;
//			String sqlQuery = "select DISTINCT   mm.model_id, concat(mm.series_name, + ' | ' + mm.Model_name) as Model_name \r\n"
//					+ "from CM_MST_MODEL mm where mm.model_name <> '' order by Model_name asc ";
			String sqlQuery = " select DISTINCT mm.model_id, concat(mm.series_name, + ' | ' + mm.Model_name,+'|'+ mm.segment_name) as Model_name from CM_MST_MODEL mm where mm.model_name <> '' and isActive='Y' order by Model_name asc";

			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					responseModelList = new ArrayList<ModelResponse>();
					for (Object object : data) {
						@SuppressWarnings("rawtypes")
						Map row = (Map) object;
						responseModel = new ModelResponse();
						responseModel.setModelId((BigInteger) row.get("model_id"));
						responseModel.setModelName((String) row.get("Model_name"));
						responseModelList.add(responseModel);
					}
				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}

			return responseModelList;
		}

	@Override
	public List<ServicePlanStatusBean> searchPlanStatus(String userCode, RemiderSearchBean bean) {
		
		Session session = null;
		Query query = null;
		ServicePlanStatusBean responseModel=null;
		List<ServicePlanStatusBean>  responseListModel= null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String sqlQuery = "exec Service_Plan_Status_Report :Usercode,:dealerId, :CustomerId, :stateId, :ModelId, :ChassisNo, :FromDate, :ToDate, :ServiceName, :branchId, :page, :size";
		
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Usercode", userCode);
			query.setParameter("dealerId", bean.getDealerId());
			query.setParameter("CustomerId", bean.getCustomerId());
			query.setParameter("stateId", bean.getStateId());
			query.setParameter("ModelId", bean.getModelId());
			query.setParameter("ChassisNo", bean.getChassisNo());
			query.setParameter("FromDate", formatter.format(bean.getFromDate()));
			query.setParameter("ToDate",  formatter.format(DateToStringParserUtils.addDayByOne(bean.getToDate())));
			query.setParameter("ServiceName", bean.getServiceName());
			query.setParameter("branchId", bean.getBranchId());
			query.setParameter("page", bean.getPage());
			query.setParameter("size", bean.getSize());
			
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new ServicePlanStatusBean();
	
					 responseModel.setProfitCenter((String) row.get("ProfitCenter"));
					 responseModel.setDealerName((String) row.get("DealerName"));
					 responseModel.setDealerCode((Integer) row.get("DealerCode"));
					 responseModel.setDealerState((String) row.get("DealerState"));
					 responseModel.setDealerDistrict((String) row.get("DealerDistrict"));
					 responseModel.setCustomerName((String) row.get("CustomerName"));
					 responseModel.setAddress1((String) row.get("CustomerAddress"));
					 responseModel.setDistrict((String) row.get("CustomerDistrict"));
					 responseModel.setTehsil((String) row.get("Tehsil"));
					 responseModel.setCity((String) row.get("City"));
					 responseModel.setPinCode((String) row.get("PinCode"));
					 responseModel.setMobileNo((String) row.get("MobileNo"));
					 responseModel.setDeliveryChallanDate((Date) row.get("DeliveryChallanDate"));
					 responseModel.setItemNo((String) row.get("ItemNumber"));
					 responseModel.setModelName((String) row.get("Model"));
					 responseModel.setItemDescription((String) row.get("ItemDescription"));
					 responseModel.setEngineNo((String) row.get("EngineNo"));
					 responseModel.setChassisNo((String) row.get("ChassisNo"));	
					 responseModel.setPreviousServiceName((String)row.get("PreviousServiceName"));
				     responseModel.setPreviousServiceDate((Date)row.get("PreviousServiceDate"));
				     responseModel.setPreviousServiceHours((Integer)row.get("PreviousServiceHours"));
				     responseModel.setNextServiceName((String)row.get("NextServiceName"));
				     responseModel.setNextServiceDate((Date)row.get("NextServiceDate"));  
				 
				    responseModel.setNoOfDaysFromDateOfDelivery((Integer)row.get("NoOfDaysFromDateOfDelivery"));
				    responseModel.setPDI((Date)row.get("PDI"));
				    responseModel.setInstallation((Date)row.get("INSTALLATION"));
				    responseModel.setService_1((Date)row.get("Service1"));
				    responseModel.setService_2((Date)row.get("Service2"));
				    responseModel.setService_3((Date)row.get("Service3"));
				    responseModel.setService_4((Date)row.get("Service4"));
				    responseModel.setService_5((Date)row.get("Service5"));
				    responseModel.setService_6((Date)row.get("Service6"));
				    responseModel.setService_7((Date)row.get("Service7"));
				    responseModel.setService_8((Date)row.get("Service8"));
				     
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
	public List<ServiceTypeResponse> getServiceTypeList(String userCode) {
		
		Session session = null;
		Query query = null;
		List<ServiceTypeResponse> responseList = null;
		ServiceTypeResponse responseModel = null;

		String sqlQuery = " SELECT SrvTypeDesc, MIN(Service_Type_oem_ID) AS Service_Type_oem_ID, SrvTypeCode FROM sv_oem_srv_type nolock GROUP BY SrvTypeDesc, SrvTypeCode;";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceTypeResponse>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceTypeResponse();
					responseModel.setId((Integer) row.get("Service_Type_oem_ID"));
					responseModel.setName((String) row.get("SrvTypeDesc"));
					responseList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseList;
	}

}
