package com.hitech.dms.web.dao.partrequisition.search;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionTypeResponseModel;
import com.hitech.dms.web.model.partrequisition.search.request.PartRequisitionSearchRequestModel;
import com.hitech.dms.web.model.partrequisition.search.response.PartRequisitionSearchListResultResponse;
import com.hitech.dms.web.model.partrequisition.search.response.PartRequisitionSearchResponseModel;
import com.hitech.dms.web.model.partrequisition.search.response.SparePartRequisitionViewResponseModel;


@Repository
public class PartRequisitionSearchDaoImpl implements PartRequisitionSearchDao{

	private static final Logger logger = LoggerFactory.getLogger(PartRequisitionSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public PartRequisitionSearchListResultResponse PartRequitionSearchList(String userCode,
			PartRequisitionSearchRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(
					"PartRequitionSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		PartRequisitionSearchListResultResponse responseListModel = null;
	    List<PartRequisitionSearchResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [SP_GETPARTREQUITION_SEARCH] :UserCode, :FromDate, :ToDate, :JobCardNo, :RequisitionNumber, :RequisitionType, :page, :size, :pcId, :orgHierID, :dealerId, :branchId, :Stateid, :ZoneId, :TerritoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("ToDate", requestModel.getToDate());
			query.setParameter("JobCardNo", requestModel.getJobCardNo());
			query.setParameter("RequisitionNumber", requestModel.getRequisitionNo());
			query.setParameter("RequisitionType", requestModel.getRequisitionType());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setParameter("pcId", requestModel.getProfitCenterId());
			query.setParameter("orgHierID",requestModel.getOrgHierarchyId() );
			query.setParameter("dealerId",requestModel.getDealerId() );
			query.setParameter("branchId",requestModel.getBranchId() );
			query.setParameter("Stateid", requestModel.getStateId());
			query.setParameter("ZoneId", requestModel.getZone());
			query.setParameter("TerritoryId", requestModel.getTerritory());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new PartRequisitionSearchListResultResponse();
				responseModelList = new ArrayList<PartRequisitionSearchResponseModel>();
				PartRequisitionSearchResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartRequisitionSearchResponseModel();
					response.setId((BigInteger) row.get("Requisition_Id"));
					response.setAction((String) row.get("Action"));
					response.setRequisitionNo((String) row.get("RequisitionNumber"));
					response.setRequisitionType((String) row.get("Req_Type"));
					response.setRequisitionDate((String) row.get("Requisition_Date"));
					response.setJobCardNo((String) row.get("RONumber"));
					response.setJobCarddate((String) row.get("OpeningDate"));
					response.setRequisitionStatus((String) row.get("RequisitionStatus"));
					response.setTotalRequestedQty((BigDecimal) row.get("RequestedQty"));
					
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
	public List<SparePartRequisitionViewResponseModel> PartRequisitionViewList(String userCode, Integer requisitionId) {
		Session session = null;
		List<SparePartRequisitionViewResponseModel> responseModelList = null;
		SparePartRequisitionViewResponseModel responseModel = null;
		Query<SparePartRequisitionTypeResponseModel> query = null;
		
		String sqlQuery = "exec [SP_GETPARTREQUITION_VIEWLIST] :RequisitionId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RequisitionId", requisitionId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePartRequisitionViewResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new SparePartRequisitionViewResponseModel();
					responseModel.setRequisitionNumber((String) row.get("RequisitionNumber"));
					responseModel.setRequisitionDate((String) row.get("RequisitonDate"));
					responseModel.setRequisitionType((String) row.get("RequisitionTYpe"));
					responseModel.setJobCardNumber((String) row.get("JobCardNumber"));
					responseModel.setJobCardDate((String) row.get("JobCardDate"));
					responseModel.setRequestedBy((String) row.get("RequestedBy"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setChassisNumber((String) row.get("ChassisNumber"));
					responseModel.setRegistrationNumber((String) row.get("Registration_number"));
					responseModel.setModelVariant((String) row.get("ModelVariant"));
					responseModel.setCustomerName((String) row.get("customerName"));
					responseModel.setPartNumber((String) row.get("partNumber"));
					responseModel.setPartDescription((String) row.get("PartDescription"));
					responseModel.setUomDescription((String) row.get("UomDescription"));
					responseModel.setCurrentStock((Integer) row.get("CurrentStock"));
					responseModel.setMrp((BigDecimal) row.get("MRP"));
					responseModel.setRequisitionQty((BigDecimal) row.get("RequisitionQty"));
					responseModel.setIssuedQty((BigDecimal) row.get("issuedQty"));
					responseModel.setPendingQty((BigDecimal) row.get("pendingQty"));
					responseModel.setPartBranchId((Integer) row.get("partBranch_id"));
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
