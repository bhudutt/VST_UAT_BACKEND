/**
 * 
 */
package com.hitech.dms.web.dao.dc.search;

import java.math.BigInteger;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.dc.search.request.DcSearchRequestModel;
import com.hitech.dms.web.model.dc.search.response.DcSearchMainResponseModel;
import com.hitech.dms.web.model.dc.search.response.DcSearchResponseModel;
import com.hitech.dms.web.model.dc.search.response.StockDetailsMainResponseModel;
import com.hitech.dms.web.model.dc.search.response.StockDetailsResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcSearchDaoImpl implements DcSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(DcSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public DcSearchMainResponseModel searchDcList(Session session, String userCode, DcSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchDcList invoked.." + requestModel.toString());
		}
		Query query = null;
		DcSearchMainResponseModel responseListModel = null;
		List<DcSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_DC_SEARCH] :UserCode, :PcID, :Stateid,:orgHierID, :dealerId, :BranchId, :dcNumber, :AllotNo, :EnquiryNo, :Series, :Segment, :Model, :variant, :DCStatus, :FromDate, :ToDate,"
				+ " :includeInactive, :page, :size";
		try {
			
			//System.out.println("updated Query is "+sqlQuery);
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("PcID", requestModel.getPcId());
			query.setParameter("Stateid", requestModel.getOrgHierID());
			query.setParameter("orgHierID", requestModel.getDealerId());
			query.setParameter("dealerId",requestModel.getStateId());
			query.setParameter("BranchId", requestModel.getBranchId());
			query.setParameter("dcNumber", requestModel.getDcNumber());
			query.setParameter("AllotNo", requestModel.getAllotNumber());
			query.setParameter("EnquiryNo", requestModel.getEnquiryNo());
			query.setParameter("Series", requestModel.getSeries());
			query.setParameter("Segment", requestModel.getSegment());
			query.setParameter("Model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("DCStatus", requestModel.getDcStatus());
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("ToDate", requestModel.getToDate());
			
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new DcSearchMainResponseModel();
				responseModelList = new ArrayList<DcSearchResponseModel>();
				DcSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DcSearchResponseModel();
					responseModel.setId((BigInteger) row.get("DcId"));
					responseModel.setId1((BigInteger) row.get("dealer_id"));
					responseModel.setId2((Integer) row.get("pcId"));
//					responseModel.setZone((String) row.get("ZONE"));
//					responseModel.setArea((String) row.get("AREA"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setDcNumber((String) row.get("DcNumber"));
					responseModel.setDcStatus((String) row.get("DcStatus"));
					responseModel.setDcDate((String) row.get("DcDate"));
					responseModel.setAllotNumber((String) row.get("AllotmentNumber"));
					responseModel.setAllotDate((String) row.get("AllotmentDate"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcDesc((String) row.get("PROFIT_CENTER"));
					responseModel.setAllotStatus((String) row.get("AllotStatus"));
					responseModel.setEnquiryNo((String) row.get("EnquiryNo"));
					responseModel.setEnquiryDate((String) row.get("EnquiryDate"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}

					responseModelList.add(responseModel);
				}
				logger.debug("recordCount invoked.." + recordCount);
				logger.debug("searchDcList invoked.." + requestModel.toString());
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
	public DcSearchMainResponseModel searchDcList(String userCode, DcSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchDcList invoked.." + requestModel.toString());
		}
		Session session = null;
		DcSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchDcList(session, userCode, requestModel);
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
	public StockDetailsMainResponseModel searchStock(String userCode, DcSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchDcList invoked.." + requestModel.toString());
		}
		Session session = null;
		StockDetailsMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchStock(session, userCode, requestModel);
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
	public StockDetailsMainResponseModel searchStock(Session session, String userCode, DcSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("stockList invoked.." + requestModel.toString());
		}
		Query query = null;
		StockDetailsMainResponseModel responseListModel = null;
		List<StockDetailsResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec SP_GET_ALL_STOCK_FOR_DELETION :userCode, :dealerId,:chassisNo, "
				+ " :includeInactive, :page, :size";
		try {
			String chassisNo;
			if(requestModel.getChassisNo()!=null && !requestModel.getChassisNo().equalsIgnoreCase("")) {
				chassisNo=requestModel.getChassisNo();
			}else {
				chassisNo=null;
			}
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("chassisNo", chassisNo);
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new StockDetailsMainResponseModel();
				responseModelList = new ArrayList<StockDetailsResponseModel>();
				StockDetailsResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new StockDetailsResponseModel();
					responseModel.setVinId((BigInteger) row.get("vin_id"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDesc((String) row.get("item_description"));
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setMfgDate((String) row.get("MfgDate"));
					responseModel.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber"));
					responseModel.setMfgInvoiceDate((String) row.get("MfgInvoiceDate"));;
					responseModel.setSellingDealerCode((String) row.get("MfgInvoiceDate"));
					responseModel.setCsbNumber((String) row.get("csb_number"));
					responseModel.setUnitPrice((Double) row.get("unit_price"));
					responseModel.setSaleDate((String) row.get("Sale_Date"));					
					responseModel.setAction((String) row.get("action"));
					

					
					/*
					 * if (recordCount.compareTo(0) == 0) { recordCount = (Integer)
					 * row.get("totalRecords"); }
					 */

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
	public StockDetailsMainResponseModel updateStock(String userCode, Integer vinId) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchDcList invoked.." + vinId.toString());
		}
		Session session = null;
		Query query = null;
		StockDetailsMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [SP_STOCK_DELETE] :vinId";
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("vinId", vinId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new StockDetailsMainResponseModel();
					String msg=(String) row.get("Message");
					responseModel.setMsg(msg);
				}
			}else {
				responseModel.setMsg("Error in SP");
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
		return responseModel;
	}
	
	public void deleteStockErrorHistory() {

		if (logger.isDebugEnabled()) {
			logger.debug("deleteStockErrorHistory invoked.." );
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "delete from STG_STOCK_ADJUSTMENT_UPLOAD_HISTORY";
			query = session.createNativeQuery(sqlQuery);
			query.executeUpdate();
			transaction.commit();
			
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
		
	
	}
}
