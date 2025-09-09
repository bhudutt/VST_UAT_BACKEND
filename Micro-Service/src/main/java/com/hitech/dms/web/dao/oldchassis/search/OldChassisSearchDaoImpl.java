package com.hitech.dms.web.dao.oldchassis.search;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.oldchassis.search.request.OldChassisSearchListRequestModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisSearchListResponseModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisSearchListResultResponseModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisValidateEVRListResponseModel;


@Repository
public class OldChassisSearchDaoImpl implements OldChassisSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(OldChassisSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public OldChassisSearchListResultResponseModel OldChassisSearchList(String userCode,
			OldChassisSearchListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"OldChassisSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		OldChassisSearchListResultResponseModel responseListModel = null;
	    List<OldChassisSearchListResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [SP_GETOLDCHASSIS_SEARCHBY_LIST] :pcId, :orgHierID, :dealerId, :branchId, :model, :fromDate, :toDate, :chassisNo, :registrationNo, :custMobileNo, :UserCode, :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("fromDate", requestModel.getFromDate());
			query.setParameter("toDate", requestModel.getToDate());
			query.setParameter("chassisNo", requestModel.getChassisNo());
			query.setParameter("registrationNo", requestModel.getRegistrationNo());
			query.setParameter("custMobileNo", requestModel.getCustMobileNo());	
			query.setParameter("UserCode", userCode);		
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("page", requestModel.getPage());	
			query.setParameter("size", requestModel.getSize());	
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new OldChassisSearchListResultResponseModel();
				responseModelList = new ArrayList<OldChassisSearchListResponseModel>();
				OldChassisSearchListResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new OldChassisSearchListResponseModel();
					response.setId((BigInteger) row.get("vin_id"));
					response.setProfitCenter((String) row.get("profitcenter"));
					response.setZone((String) row.get("zone"));
					response.setState((String) row.get("state"));
					response.setTerritory((String) row.get("terrority"));
					response.setAction((String) row.get("validate"));
					response.setDealership((String) row.get("dealership"));
					response.setBranch((String) row.get("branch"));
					response.setModel((String) row.get("model"));
					response.setVariant((String) row.get("varriant"));
					response.setStatus((String) row.get("status"));
					response.setItemNo((String) row.get("item_no"));
					response.setItemDescription((String) row.get("item_description"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setEngineNo((String) row.get("engine_no"));
					response.setVinNo((String) row.get("vin_no"));
					response.setRegistrationNo((String) row.get("registration_number"));
					response.setCustomerName((String) row.get("customername"));
					response.setCustomerMobile((String) row.get("customermobile"));
					
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(response);
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
	public List<OldChassisValidateEVRListResponseModel> fetchValidateEVRList(String userCode, Device device) {

		Session session = null;
		List<OldChassisValidateEVRListResponseModel> responseModelList = null;
		OldChassisValidateEVRListResponseModel responseModel = null;
		Query<OldChassisValidateEVRListResponseModel> query = null;
		String sqlQuery = "Select vin_id, engine_no,vin_no,registration_number from SA_MACHINE_VIN_MASTER";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisValidateEVRListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new OldChassisValidateEVRListResponseModel();
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
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
	

	
}
