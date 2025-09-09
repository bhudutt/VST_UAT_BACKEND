package com.hitech.dms.web.dao.machinestock.search;

import java.math.BigDecimal;
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
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.hitech.dms.web.model.machinestock.search.MachineStockOverAllSearchResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockOverallResultResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchRequestModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchResultResponseModel;

@Repository
public class MachineStockSearchDaoImpl implements MachineStockSearchDao {

	private static final Logger logger = LoggerFactory.getLogger(MachineStockSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public MachineStockSearchResultResponseModel machineStockSearchList(String userCode,
			MachineStockSearchRequestModel requestModel) {
		logger.debug("machineStockSearchList invoked.." + userCode + " " + requestModel.toString());
		if (logger.isDebugEnabled()) {
			logger.debug("machineStockSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		MachineStockSearchResultResponseModel responseListModel = null;
		List<MachineStockSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_Get_StockReport] :Usercode, :DealerCode, :Fromdate, :Todate, :ChassisNo, :engineno, :VinNo, :InvoiceNo, :includeInactive, :orgId, "
				+ ":pcId, :itemNo, :variant, :modelId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Usercode", userCode);
			query.setParameter("DealerCode", requestModel.getDealerCode());
			query.setParameter("Fromdate", requestModel.getFromDate());
			query.setParameter("Todate", requestModel.getToDate());
			query.setParameter("ChassisNo", requestModel.getChassisNo());
			query.setParameter("engineno", requestModel.getEngineNo());
			query.setParameter("VinNo", requestModel.getVinNo());
			query.setParameter("InvoiceNo", requestModel.getInvoiceNo());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("orgId", requestModel.getOrgId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("itemNo", requestModel.getItemNo());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("modelId", requestModel.getModelId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				responseListModel = new MachineStockSearchResultResponseModel();
				responseModelList = new ArrayList<MachineStockSearchResponseModel>();

				MachineStockSearchResponseModel searchModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					searchModel = new MachineStockSearchResponseModel();
					searchModel.setZone((String) row.get("ZONE"));
					searchModel.setState((String) row.get("state"));
					searchModel.setTerritory((String) row.get("territory"));
					searchModel.setDealership((String) row.get("dealership"));
					searchModel.setBranch((String) row.get("branch"));
					searchModel.setProductDivision((String) row.get("productdivision"));
					searchModel.setModel((String) row.get("model"));
					searchModel.setVariant((String) row.get("variant"));
					searchModel.setItemNumber((String) row.get("item_no"));
					searchModel.setItemDescription((String) row.get("item_description"));
					searchModel.setChassisNo((String) row.get("chassis_no"));
					searchModel.setVinNo((String) row.get("vin_no"));
					searchModel.setEngineNo((String) row.get("engine_no"));
					searchModel.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber"));
					searchModel.setMfgDate((Date) row.get("MfgDate"));
					searchModel.setMfgInvoiceDate((String) row.get("MfgInvoiceDate"));
					searchModel.setRegistrationNumber((String) row.get("registration_number"));
					searchModel.setInstallationDate((Date) row.get("installation_date"));
					searchModel.setPdiInwardDate((Date) row.get("pdiinwarddate"));
					searchModel.setUnitPrice((BigDecimal) row.get("unit_price"));

					searchModel.setNoOfDayStock((Integer) row.get("noofdateinstock"));
					searchModel.setPoNo((String) row.get("pono"));
					searchModel.setPoDate((Date) row.get("podate"));
					searchModel.setTransporter((String) row.get("transporter"));
					searchModel.setLrNo((String) row.get("LRNo"));
					searchModel.setLrDate((Date) row.get("LRDate"));
					searchModel.setGrnNo((String) row.get("GRNNo"));
					searchModel.setGrnDate((String) row.get("GRNDate"));
					searchModel.setProfitCenter((String) row.get("Profitcenter"));
					searchModel.setStatus((String) row.get("Status"));
					searchModel.setCustomermMobileNo((String) row.get("Customermobno"));
					searchModel.setDeliverychallanNo((String) row.get("DeliverychallanNo"));
					searchModel.setDeliverychallanDate((String) row.get("Deliverychallandate"));
					searchModel.setCustomerinvoiceNo((String) row.get("Customerinvoiceno"));
					searchModel.setCustomerinvoiceDate((String) row.get("Customerinvoicedate"));
					searchModel.setCustomerName((String) row.get("Customername"));
					searchModel.setStockQuantity((Integer) row.get("StockQuantity"));
					searchModel.setProductGroup((String) row.get("product_group"));
					searchModel.setArea((String) row.get("AREA"));
					searchModel.setStockQuantity((Integer) row.get("stockQty"));
					recordCount++;
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(searchModel);
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
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;

	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public MachineStockOverallResultResponseModel machineStockOverAllSearchList(String userCode,
			MachineStockSearchRequestModel requestModel) {
		logger.debug("machineStockOverAllSearchList invoked.." + userCode + " " + requestModel.toString());
		if (logger.isDebugEnabled()) {
			logger.debug("machineStockOverAllSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		MachineStockOverallResultResponseModel responseListModel = null;
		List<MachineStockOverAllSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_Get_AsOnDateStockReport] :Usercode, :DealerId, :branchId, :PCID, :Fromdate, :Todate, :ProductDivision, "
				+ " :MODEL, :ItemNo, :includeInactive, :orgId, :page, :size , :stateId, :chessisNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Usercode", userCode);
			query.setParameter("DealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchID());
			query.setParameter("PCID", requestModel.getPcId());
			query.setParameter("Fromdate", requestModel.getFromDate());
			query.setParameter("Todate", requestModel.getToDate());
			query.setParameter("ProductDivision", requestModel.getProductDivision());
			query.setParameter("MODEL", requestModel.getModelId());
			query.setParameter("ItemNo", requestModel.getItemNo());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("orgId", requestModel.getOrgId());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setParameter("stateId", requestModel.getStateId());
			query.setParameter("chessisNo", requestModel.getChassisNo());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				responseListModel = new MachineStockOverallResultResponseModel();
				responseModelList = new ArrayList<MachineStockOverAllSearchResponseModel>();

				MachineStockOverAllSearchResponseModel searchModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					searchModel = new MachineStockOverAllSearchResponseModel();
					// searchModel.setZone((String) row.get("ZONE"));
					searchModel.setState((String) row.get("state"));
					// searchModel.setTerritory((String) row.get("territory"));
					searchModel.setDealership((String) row.get("dealership"));
					// searchModel.setBranch((String) row.get("branch"));
					searchModel.setParentDealerCode((String) row.get("ParentDealerCode"));
					searchModel.setParentDealerLocation((String) row.get("ParentDealerLocation"));
					searchModel.setProductDivision((String) row.get("productdivision"));
					searchModel.setModel((String) row.get("model"));
					searchModel.setVariant((String) row.get("variant"));
					searchModel.setItemNumber((String) row.get("item_no"));
					searchModel.setItemDescription((String) row.get("item_description"));
					searchModel.setChassisNo((String) row.get("chassis_no"));
					searchModel.setVinNo((String) row.get("vin_no"));
					searchModel.setEngineNo((String) row.get("engine_no"));
					searchModel.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber"));
					// searchModel.setMfgDate((Date) row.get("MfgDate"));
					searchModel.setMfgInvoiceDate((String) row.get("MfgInvoiceDate"));
					// searchModel.setRegistrationNumber((String) row.get("registration_number"));
					// searchModel.setInstallationDate((Date) row.get("installation_date"));
					// searchModel.setPdiInwardDate((Date) row.get("pdiinwarddate"));
					searchModel.setUnitPrice((BigDecimal) row.get("unit_price"));
					searchModel.setUnitPriceMrp((BigDecimal) row.get("UNITPRICEMRP"));

					searchModel.setNoOfDayStock((Integer) row.get("noofdateinstock"));
					// searchModel.setPoNo((String) row.get("pono"));
					// searchModel.setPoDate((Date) row.get("podate"));
					// searchModel.setTransporter((String) row.get("transporter"));
					// searchModel.setLrNo((String) row.get("LRNo"));
					// searchModel.setLrDate((Date) row.get("LRDate"));
					searchModel.setGrnNo((String) row.get("GRNNo"));
					searchModel.setGrnDate((String) row.get("GRNDate"));
					searchModel.setProfitCenter((String) row.get("Profitcenter"));
					searchModel.setStatus((String) row.get("Status"));
					// searchModel.setCustomermMobileNo((String) row.get("Customermobno"));
					// searchModel.setDeliverychallanNo((String) row.get("DeliverychallanNo"));
					// searchModel.setDeliverychallanDate((String) row.get("Deliverychallandate"));
					// searchModel.setCustomerinvoiceNo((String) row.get("Customerinvoiceno"));
					// searchModel.setCustomerinvoiceDate((String) row.get("Customerinvoicedate"));
					// searchModel.setCustomerName((String) row.get("Customername"));
					// searchModel.setStockQuantity((Integer) row.get("StockQuantity"));
					// searchModel.setProductGroup((String) row.get("product_group"));
					// searchModel.setArea((String) row.get("AREA"));
					searchModel.setStockQuantity((Integer) row.get("StockQuantity"));
					recordCount++;
					//if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					//}
					responseModelList.add(searchModel);
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
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;

	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public MachineStockOverallResultResponseModel machineTransitStockSearchList(String userCode,
			MachineStockSearchRequestModel requestModel) {
		logger.debug("machineStockOverAllSearchList invoked.." + userCode + " " + requestModel.toString());
		if (logger.isDebugEnabled()) {
			logger.debug("machineStockOverAllSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		MachineStockOverallResultResponseModel responseListModel = null;
		List<MachineStockOverAllSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_Get_AsOnDateStockReport] :Usercode, :DealerId, :branchId, :PCID, :Fromdate, :Todate, :ProductDivision, "
				+ " :MODEL, :ItemNo, :includeInactive, :orgId, :page, :size , :stateId, :chessisNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Usercode", userCode);
			query.setParameter("DealerId", requestModel.getDealerCode());
			query.setParameter("branchId", requestModel.getBranchID());
			query.setParameter("PCID", requestModel.getPcId());
			query.setParameter("Fromdate", requestModel.getFromDate());
			query.setParameter("Todate", requestModel.getToDate());
			query.setParameter("ProductDivision", requestModel.getProductDivision());
			query.setParameter("MODEL", requestModel.getModelId());
			query.setParameter("ItemNo", requestModel.getItemNo());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("orgId", requestModel.getOrgId());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setParameter("stateId", requestModel.getStateId());
			query.setParameter("chessisNo", requestModel.getChassisNo());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				responseListModel = new MachineStockOverallResultResponseModel();
				responseModelList = new ArrayList<MachineStockOverAllSearchResponseModel>();

				MachineStockOverAllSearchResponseModel searchModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					searchModel = new MachineStockOverAllSearchResponseModel();
					// searchModel.setZone((String) row.get("ZONE"));
					searchModel.setState((String) row.get("state"));
					// searchModel.setTerritory((String) row.get("territory"));
					searchModel.setDealership((String) row.get("dealership"));
					// searchModel.setBranch((String) row.get("branch"));
					searchModel.setParentDealerCode((String) row.get("ParentDealerCode"));
					searchModel.setParentDealerLocation((String) row.get("ParentDealerLocation"));
					searchModel.setProductDivision((String) row.get("productdivision"));
					searchModel.setModel((String) row.get("model"));
					searchModel.setVariant((String) row.get("variant"));
					searchModel.setItemNumber((String) row.get("item_no"));
					searchModel.setItemDescription((String) row.get("item_description"));
					searchModel.setChassisNo((String) row.get("chassis_no"));
					searchModel.setVinNo((String) row.get("vin_no"));
					searchModel.setEngineNo((String) row.get("engine_no"));
					searchModel.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber"));
					// searchModel.setMfgDate((Date) row.get("MfgDate"));
					searchModel.setMfgInvoiceDate((String) row.get("MfgInvoiceDate"));
					// searchModel.setRegistrationNumber((String) row.get("registration_number"));
					// searchModel.setInstallationDate((Date) row.get("installation_date"));
					// searchModel.setPdiInwardDate((Date) row.get("pdiinwarddate"));
					searchModel.setUnitPrice((BigDecimal) row.get("unit_price"));
					searchModel.setUnitPriceMrp((BigDecimal) row.get("UNITPRICEMRP"));

					searchModel.setNoOfDayStock((Integer) row.get("noofdateinstock"));
					// searchModel.setPoNo((String) row.get("pono"));
					// searchModel.setPoDate((Date) row.get("podate"));
					// searchModel.setTransporter((String) row.get("transporter"));
					// searchModel.setLrNo((String) row.get("LRNo"));
					// searchModel.setLrDate((Date) row.get("LRDate"));
					searchModel.setGrnNo((String) row.get("GRNNo"));
					searchModel.setGrnDate((String) row.get("GRNDate"));
					searchModel.setProfitCenter((String) row.get("Profitcenter"));
					searchModel.setStatus((String) row.get("Status"));
					// searchModel.setCustomermMobileNo((String) row.get("Customermobno"));
					// searchModel.setDeliverychallanNo((String) row.get("DeliverychallanNo"));
					// searchModel.setDeliverychallanDate((String) row.get("Deliverychallandate"));
					// searchModel.setCustomerinvoiceNo((String) row.get("Customerinvoiceno"));
					// searchModel.setCustomerinvoiceDate((String) row.get("Customerinvoicedate"));
					// searchModel.setCustomerName((String) row.get("Customername"));
					// searchModel.setStockQuantity((Integer) row.get("StockQuantity"));
					// searchModel.setProductGroup((String) row.get("product_group"));
					// searchModel.setArea((String) row.get("AREA"));
					searchModel.setStockQuantity((Integer) row.get("StockQuantity"));
					recordCount++;
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(searchModel);
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
			if (session != null) {
				session.close();
			}
		}
		return responseListModel;

	}

}
