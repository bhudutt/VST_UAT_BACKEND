package com.hitech.dms.web.dao.advancetrackingreport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
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
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.advancereport.AdvanceReportSearchListResultResponseModel;
import com.hitech.dms.web.model.advancereport.AdvanceTrackingReportRequestModel;
import com.hitech.dms.web.model.advancereport.AdvanceTrackingReportResponseModel;
import com.hitech.dms.web.model.advancereport.FinancierResponseListModel;

@Repository
public class AdvanceTrackingReportDaoImpl implements AdvanceTrackingReportDao{
	
	private static final Logger logger = LoggerFactory.getLogger(AdvanceTrackingReportDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public AdvanceReportSearchListResultResponseModel Search(String userCode,
			AdvanceTrackingReportRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"Advance Tracking Report Search invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		AdvanceReportSearchListResultResponseModel responseListModel = null;
	    List<AdvanceTrackingReportResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [ADVANCE_TRACTING_REPORT] :pcId, :orgHierID, :dealerId, :branchId, :Stateid, :Model, :LoanCash, :FinancialInstitute, :FromDate, :ToDate, :UserCode, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("Stateid", requestModel.getStateid());
			query.setParameter("Model", requestModel.getModel());
			query.setParameter("LoanCash", requestModel.getLoanCash());
			query.setParameter("FinancialInstitute", requestModel.getFinancialInstitute());
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("ToDate", requestModel.getToDate());
			query.setParameter("UserCode", userCode);
			query.setParameter("page", requestModel.getPage());	
			query.setParameter("size", requestModel.getSize());	
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new AdvanceReportSearchListResultResponseModel();
				responseModelList = new LinkedList<AdvanceTrackingReportResponseModel>();
				AdvanceTrackingReportResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new AdvanceTrackingReportResponseModel();
					
					response.setState((String) row.get("StateDesc"));
					response.setCluster((String)row.get("Cluster"));
					response.setTm((String) row.get("TM"));
					response.setDealershipName((String) row.get("ParentDealerName"));
					response.setLocation((String) row.get("ParentDealerLocation"));
					response.setCustomerName((String) row.get("customerName"));
					response.setVillage((String) row.get("village"));
					response.setTehsil((String) row.get("TehsilDesc"));
					response.setDistrict((String) row.get("DistrictDesc"));
					response.setContactNo((String) row.get("mobileNumber"));
					response.setModel((String) row.get("ModelName"));
					response.setDateOfDelivery((String) row.get("DATEOFDELIVERY"));
					response.setAgeing((Integer) row.get("AGEINGNooOfDays"));
					response.setLoanOrCash((String) row.get("LoanCash"));
					response.setFinancialInstitution((String) row.get("FinancialInstitution"));
					response.setDealPrice((BigDecimal) row.get("DEALPRICE"));
					response.setNdpPrice((BigDecimal) row.get("Ndp_price"));
					response.setMarginMoneyReceivedAmount((BigDecimal) row.get("MARGIN_MONEY_RECEIVED_AMNT"));
					response.setExchange((Character) row.get("EXCHANGE"));
					response.setLoanAmount((BigDecimal) row.get("LOANAMOUNT"));
				//	response.setBalanceOwed((BigDecimal) row.get("BALANCEO"));
					response.setLoanAmountDisbursed((Integer)row.get("Loan_Amount_Disbursed"));
					response.setDateOfDisbursement((String) row.get("DATE_OF_DISBURSMENT"));
					
					response.setRetailFinanceStage((String) row.get("RETAIL_FINANCE_STAGE"));
					//response.setSubStage((String) row.get("SUB_STAGE"));
					response.setRetailStatus((String) row.get("Retail_status"));
					
				
					response.setSubsidyEstimatedAmount((BigDecimal)row.get("subsidy_estimated_amount"));
					//response.setIsExchangeTractorAvailable((char) row.get("exchange_required"));
					response.setExchangePurchaseValue((String) row.get("EXCHANGE_PURCHASE_VALUE"));
				//	response.setExchangeSaleableValue((BigDecimal) row.get("Exchange_Saleable_Value"));
					response.setExchangeSoldValue((BigDecimal) row.get("Exchange_Sold_Value"));
					response.setTotalAmountRecieved((BigDecimal) row.get("total_amount_received"));
					response.setPendingRetailAmount((BigDecimal) row.get("pending_amount"));
					//response.setPreApproval((String) row.get("Pre_Approval"));
					//response.setPostApproval((String) row.get("Post_Approval"));
					response.setExpectedDateOfRetail((String) row.get("Expected_Date_of_Retail"));
					response.setRemarks1((String) row.get("Remarks1"));
					response.setRemarks2((String)row.get("Remarks2"));
					response.setRemarks3((String)row.get("Remarks3"));
					response.setEnquiryNo((String)row.get("ENQUIRY_NO"));
					response.setChassis((String)row.get("CHASSIS_NO"));
					response.setEnquiryId((BigInteger)row.get("enquiry_id"));
					
					
					
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
	public List<FinancierResponseListModel> fetchFinancierList(String userCode, Object object) {
		
		Session session = null;
		List<FinancierResponseListModel> responseModelList = null;
		FinancierResponseListModel responseModel = null;
		Query<FinancierResponseListModel> query = null;
		String sqlQuery = "EXEC [SP_GET_Financier_List]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<FinancierResponseListModel>();
				for (Object object1 : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object1;
					responseModel = new FinancierResponseListModel();
					responseModel.setFinancierId((BigInteger) row.get("financierID"));
					responseModel.setFinancierTypeId((BigInteger) row.get("financierTypeID"));
					responseModel.setFinancierName((String) row.get("FinancierName"));
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
