/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.search;

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
import com.hitech.dms.web.model.machinepo.search.request.MachinePOSearchRequestModel;
import com.hitech.dms.web.model.machinepo.search.response.MachinePOSearchListResponseModel;
import com.hitech.dms.web.model.machinepo.search.response.MachinePOSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePOSearchDaoImpl implements MachinePOSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public MachinePOSearchListResponseModel fetchMachinePOSearchList(Session session, String userCode,
			MachinePOSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePOSearchList invoked.." + requestModel.toString());
		}
		Query query = null;
		MachinePOSearchListResponseModel responseListModel = null;
		List<MachinePOSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SAORD_PO_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :fromDate, :todate, :series, :segment, :model, :variant, :poStatus, "
				+ " :poOn, :partyId, :plantId, :poNumber, :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: requestModel.getFromDate()));
			query.setParameter("todate", (requestModel.getToDate() == null ? null
					: requestModel.getToDate()));
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("poStatus", requestModel.getPoStatus());
			query.setParameter("poOn", requestModel.getPoTypeId());
			query.setParameter("partyId", requestModel.getPoToDealerId());
			query.setParameter("plantId", requestModel.getPoPlantId());
			query.setParameter("poNumber", requestModel.getPoNumber());
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new MachinePOSearchListResponseModel();
				responseModelList = new ArrayList<MachinePOSearchResponseModel>();
				MachinePOSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachinePOSearchResponseModel();
					responseModel.setPoHdrId((BigInteger) row.get("po_hdr_id"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setZone((String) row.get("ZONE"));
					responseModel.setArea((String) row.get("AREA"));
					responseModel.setDealerShip((String) row.get("DEALERSHIP"));
					responseModel.setProfitCenter((String) row.get("PROFIT_CENTER"));
					responseModel.setPoNumber((String) row.get("PO_NO"));
					responseModel.setPoDate((String) row.get("PO_DATE"));
					responseModel.setPoStatus((String) row.get("PO_STATUS"));
					responseModel.setPoReleasedDate((String) row.get("PO_RELEASE_DATE"));
					responseModel.setPoOn((String) row.get("PO_ON"));
					responseModel.setPartyCode((String) row.get("PARTY_CODE"));
					responseModel.setPartyName((String) row.get("PARTY_NAME"));
					responseModel.setRso((String) row.get("RSO"));
					responseModel.setRemarks((String) row.get("REMARKS"));
					responseModel.setSoNumber((String) row.get("SO_NUMBER"));
					responseModel.setSoDate((String) row.get("SO_DATE"));
					responseModel.setBaseAmount((BigDecimal) row.get("BASE_AMOUNT"));
					responseModel.setTotalGstAmount((BigDecimal) row.get("TOTAL_GST_AMOUNT"));
					responseModel.setTcsPer((BigDecimal) row.get("TCS_Perc"));
					responseModel.setTcsAmount((BigDecimal) row.get("TCS_AMOUNT"));
					responseModel.setTotalPOAmount((BigDecimal) row.get("TOTAL_PO_AMOUNT"));
					responseModel.setInvoiceNumber((String) row.get("INVOICE_NO"));
					responseModel.setInvoiceDate((String) row.get("INVOICE_DATE"));
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
	public MachinePOSearchListResponseModel fetchMachinePOSearchList(String userCode,
			MachinePOSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePOSearchList invoked.." + requestModel.toString());
		}
		Session session = null;
		MachinePOSearchListResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchMachinePOSearchList(session, userCode, requestModel);
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
