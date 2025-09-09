/**
 * 
 */
package com.hitech.dms.web.dao.inv.po.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.inv.search.request.InvoiceSearchRequestModel;
import com.hitech.dms.web.model.inv.search.response.InvoiceSearchMainResponseModel;
import com.hitech.dms.web.model.inv.search.response.InvoiceSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvoiceSearchDaoImpl implements InvoiceSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public InvoiceSearchMainResponseModel searchInvoiceList(Session session, String userCode,
			InvoiceSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchInvoiceList invoked.." + requestModel.toString());
		}
		Query query = null;
		InvoiceSearchMainResponseModel responseListModel = null;
		List<InvoiceSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_INV_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :dcNumber, :invoiceNumber, :poNumber, :series, :segment, :model, :variant, :invoiceStatus, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("dcNumber", requestModel.getDcNumber());
			query.setParameter("invoiceNumber", requestModel.getInvoiceNumber());
			query.setParameter("poNumber", requestModel.getPoNumber());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("invoiceStatus", requestModel.getInvoiceStatus());
			query.setParameter("fromDate", (requestModel.getFromDate1() == null ? null
					: requestModel.getFromDate1()));
			query.setParameter("todate", (requestModel.getToDate1() == null ? null
					: requestModel.getToDate1()));
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new InvoiceSearchMainResponseModel();
				responseModelList = new ArrayList<InvoiceSearchResponseModel>();
				InvoiceSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InvoiceSearchResponseModel();
					responseModel.setId((BigInteger) row.get("SalesInvoiceHdrId"));
					responseModel.setId1((BigInteger) row.get("dealer_id"));
					responseModel.setId2((Integer) row.get("pcId"));
//					responseModel.setZone((String) row.get("ZONE"));
//					responseModel.setArea((String) row.get("AREA"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setInvoiceStatus((String) row.get("InvoiceStatus"));
					responseModel.setInvoiceType((String) row.get("InvoiceType"));
					responseModel.setDcNumber((String) row.get("DcNumber"));
					responseModel.setPoNumber((String) row.get("PoNumber"));
					responseModel.setInvoiceCancelDate((String) row.get("InvoiceCancelDate"));
					responseModel.setInvoiceCancelRemark((String) row.get("InvoiceCancelRemark"));
					responseModel.setDealerShip((String) row.get("DealerShip"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setPcDesc((String) row.get("PROFIT_CENTER"));
					responseModel.setTotalBasicAmnt((BigDecimal) row.get("TotalBasicAmnt"));
					responseModel.setTotalGstAmnt((BigDecimal) row.get("TotalBasicAmnt"));
					responseModel.setTotalInvoiceAmnt((BigDecimal) row.get("TotalBasicAmnt"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
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
	public InvoiceSearchMainResponseModel searchInvoiceList(String userCode, InvoiceSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchInvoiceList invoked.." + requestModel.toString());
		}
		Session session = null;
		InvoiceSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchInvoiceList(session, userCode, requestModel);
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
}
