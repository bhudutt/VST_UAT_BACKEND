/**
 * 
 */
package com.hitech.dms.web.dao.pr.search;

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
import com.hitech.dms.web.model.grn.search.request.GrnSearchRequestModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseMainModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseModel;
import com.hitech.dms.web.model.pr.search.request.PurchaseReturnSearchRequestModel;
import com.hitech.dms.web.model.pr.search.response.PurchaseReturnSearchMainResponseModel;
import com.hitech.dms.web.model.pr.search.response.PurchaseReturnSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PurchaseReturnSearchDaoImpl implements PurchaseReturnSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseReturnSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PurchaseReturnSearchMainResponseModel searchSalesPurchaseReturnList(Session session, String userCode,
			PurchaseReturnSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchSalesPurchaseReturnList invoked.." + requestModel.toString());
		}
		Query query = null;
		PurchaseReturnSearchMainResponseModel responseListModel = null;
		List<PurchaseReturnSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_GRN_PUR_RET_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :grnNumber, "
				+ " :prNumber, :series, :segment, :model, :variant, :grnTypeId, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("grnNumber", requestModel.getGrnNo());
			query.setParameter("prNumber", requestModel.getPurchaseReturnNo());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("grnTypeId", requestModel.getGrnTypeId());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("todate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new PurchaseReturnSearchMainResponseModel();
				responseModelList = new ArrayList<PurchaseReturnSearchResponseModel>();
				PurchaseReturnSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PurchaseReturnSearchResponseModel();
					responseModel.setId((BigInteger) row.get("purchase_return_id"));
					responseModel.setId1((BigInteger) row.get("dealer_id"));
					responseModel.setId2((Integer) row.get("pc_id"));
//					responseModel.setZone((String) row.get("ZONE"));
//					responseModel.setArea((String) row.get("AREA"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setPurchaseReturnNumber((String) row.get("PurchaseReturnNo"));
					responseModel.setPurchaseReturnDate((String) row.get("PurchaseReturnDate"));
					responseModel.setGrnNumber((String) row.get("GrnNumber"));
					responseModel.setGrnDate((String) row.get("GrnDate"));
					responseModel.setGrnType((String) row.get("GrnType"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcDesc((String) row.get("PROFIT_CENTER"));
//					responseModel.setGrnStatus((String) row.get("GrnStatus"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNo"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setPartyName((String) row.get("Party_Name"));
					responseModel.setPartyCode((String) row.get("Party_Code"));
//					responseModel.setTransporterName((String) row.get("transporter_Name"));
					responseModel.setGrossTotalValue((BigDecimal) row.get("GrossTotalValue"));
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
	public PurchaseReturnSearchMainResponseModel searchSalesPurchaseReturnList(String userCode, PurchaseReturnSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchSalesPurchaseReturnList invoked.." + requestModel.toString());
		}
		Session session = null;
		PurchaseReturnSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchSalesPurchaseReturnList(session, userCode, requestModel);
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
