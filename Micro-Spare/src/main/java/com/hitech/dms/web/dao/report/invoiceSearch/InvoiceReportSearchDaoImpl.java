package com.hitech.dms.web.dao.report.invoiceSearch;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.daoImpl.spare.deliveryChallan.DeliveryChallanDaoImpl;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.report.model.InvoicePartCustResponse;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchList;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRep;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.ZoneResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcSearchBean;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcSearchListResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

@Repository
@Transactional
public class InvoiceReportSearchDaoImpl implements InvoiceReportSearchDao {
	
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;
	

	@Override
	public InvoiceReportSearchList search(String userCode, InvoiceReportSearchRequest resquest, Device device) {
		
	
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Integer totalRow = 0;
		boolean isSuccess = true;
		List<InvoiceReportSearchRep> responseList = null;
		InvoiceReportSearchRep responseModel = null;
		InvoiceReportSearchList bean = new InvoiceReportSearchList();
		Integer rowCount = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String[] cu =null;
			if(resquest.getCustomerName()!=null) {
			 cu = resquest.getCustomerName().split("\\|");
			}
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [PA_Get_Invoice_part_report]  :FromDate, :ToDate, :UserCode, :page, :size,"
					+ " :zone, :stateId, :PartNo, :partCatId, :customerName, :kpdId, :custFlag, :branchId";
			query = session.createSQLQuery(sqlQuery);
			
			
			query.setParameter("FromDate", formatter.format(resquest.getFromDate()));
			query.setParameter("ToDate", formatter.format(DateToStringParserUtils.addDayByOne(resquest.getToDate())));
			query.setParameter("UserCode", userCode);
			query.setParameter("page", resquest.getPage());
			query.setParameter("size", resquest.getSize());
			query.setParameter("zone", resquest.getZone());
			query.setParameter("stateId", resquest.getStateId());
			query.setParameter("PartNo", resquest.getPartNumber());
			query.setParameter("partCatId", resquest.getPartCategoryId());
			query.setParameter("customerName", cu!=null?cu[1].trim():null);
			query.setParameter("kpdId", resquest.getKpdId());
			query.setParameter("custFlag", resquest.getCustFlag());
			query.setParameter("branchId", resquest.getBranchId());
			
			
		
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new LinkedList<InvoiceReportSearchRep>();
				for (Object object : data) {
					Map row = (Map) object;
								
					
					responseModel = new InvoiceReportSearchRep();
					
//					responseModel.setInvoiceSaleId((BigInteger) row.get("invoice_sale_id"));
//					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					
					
					
					responseModel.setKpdName((String) row.get("KPDNAME"));
					
					responseModel.setKpdCode((String) row.get("KPD"));
					
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					
					responseModel.setInvNumber((String) row.get("InvNumber"));
					
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setZone((String) row.get("Zone"));
					
					responseModel.setStateDesc((String) row.get("StateDesc"));
					responseModel.setDistrictDesc((String) row.get("DistrictDesc"));
					responseModel.setProductCategory((String) row.get("ProductCategory"));
					responseModel.setProductSubCategory((String) row.get("ItemCategory"));
					responseModel.setPartNumber((String) row.get("partNumber"));
					responseModel.setPartDescription((String) row.get("PARTDESC"));
					responseModel.setQty((BigDecimal) row.get("QTY"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setValue((BigDecimal) row.get("Value"));
					
					responseModel.setHsnCode((String) row.get("hsnCode"));
					responseModel.setGstPercentage((BigDecimal) row.get("gstPercentage"));
					responseModel.setGstValue((BigDecimal) row.get("gstValue"));
					responseModel.setTotalValue((BigDecimal) row.get("totalValue"));
					responseModel.setCustGstNo((String) row.get("custGSTNo"));
					responseModel.setStatus((String) row.get("status"));
					
//					responseModel.setDocType((String) row.get("DocType"));
					
					
					
					
					
					
					
					
					
					

					totalRow = (Integer) row.get("totalCount");

					responseList.add(responseModel);
					rowCount++;
				}
				bean.setTotalRowCount(totalRow);
				bean.setSearchList(responseList);
				bean.setRowCount(rowCount);
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {
				bean.setStatusCode(WebConstants.STATUS_OK_200);
				bean.setMsg("Delivery Challan Fetches Successfully...");

			} else {
				bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return bean;
	}


	@Override
	public List<partSearchResponseModel> fetchPartNumber(String userCode, InvoicePartNoRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + requestModel);
		}
		Session session = null;
		Query query = null;
		List<partSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PART_SEARCH_FOR_INVOICE_REPORT] :SearchText, :branch_id";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchText", requestModel.getSearchText());
			query.setParameter("branch_id", requestModel.getBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<partSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					partSearchResponseModel responseModel = new partSearchResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
					responseModel.setDesc((String) row.get("PartDesc"));
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
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}


	@Override
	public List<KPDResponse> getKPDList(String userCode,InvoicePartNoRequest requestModel) {

		Session session = null;
		Query query = null;
		KPDResponse responseModel=null;
		List<KPDResponse>  responseListModel= null;
		String sqlQuery = "exec [pa_Invoice_report_KPD_search] :userCode, :searchText";
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
					
					responseModel = new KPDResponse();
					
					responseModel.setId((BigInteger) row.get("Id"));
					responseModel.setCustomerName((String)row.get("CustomerCode")+" | "+(String) row.get("CustomerName"));
					responseModel.setCustomerCode((String)row.get("CustomerCode"));
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
	public List<InvoicePartCustResponse> fetchCustomerName(String userCode, InvoicePartNoRequest requestModel) {
		
		Session session = null;
		Query query = null;
		InvoicePartCustResponse responseModel=null;
		List<InvoicePartCustResponse>  responseListModel= null;
		List<partSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [pa_Invoice_report_customer_search] :userCode, :searchText ";
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
					
					responseModel = new InvoicePartCustResponse();
					
					responseModel.setId((BigInteger) row.get("Id"));
					responseModel.setFlag((String) row.get("Flag"));
					responseModel.setCustomerName((String)row.get("CustomerCode")+" | "+(String) row.get("CustomerName"));
					responseModel.setCustomerCode((String)row.get("CustomerCode"));
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

	
	

}
