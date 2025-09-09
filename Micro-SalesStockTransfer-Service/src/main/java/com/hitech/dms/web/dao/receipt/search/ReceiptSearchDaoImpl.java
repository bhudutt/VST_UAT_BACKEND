/**
 * 
 */
package com.hitech.dms.web.dao.receipt.search;

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
import com.hitech.dms.web.model.stock.receipt.search.request.ReceiptSearchRequestModel;
import com.hitech.dms.web.model.stock.receipt.search.response.ReceiptSearchMainResponseModel;
import com.hitech.dms.web.model.stock.receipt.search.response.ReceiptSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ReceiptSearchDaoImpl implements ReceiptSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(ReceiptSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ReceiptSearchMainResponseModel searchReceiptList(Session session, String userCode,
			ReceiptSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchReceiptList invoked.." + requestModel.toString());
		}
		Query query = null;
		ReceiptSearchMainResponseModel responseListModel = null;
		List<ReceiptSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_TR_RECEIPT_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :receiptNumber, :receiptBy, :receiptToBranchId, :series, :segment, :model, :variant, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("receiptNumber", requestModel.getReceiptNumber());
			query.setParameter("receiptBy", requestModel.getReceiptBy());
			query.setParameter("receiptToBranchId", requestModel.getReceiptToBranchId());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
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
				responseListModel = new ReceiptSearchMainResponseModel();
				responseModelList = new ArrayList<ReceiptSearchResponseModel>();
				ReceiptSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ReceiptSearchResponseModel();
					responseModel.setId((BigInteger) row.get("ReceiptId"));
					responseModel.setId1((BigInteger) row.get("dealerId"));
					responseModel.setId2((BigInteger) row.get("branchId"));
					responseModel.setDealerShip((String) row.get("DealerShip"));
					responseModel.setProfitCenter((String) row.get("ProfitCenter"));
					responseModel.setReceiptNumber((String) row.get("ReceiptNumber"));
					responseModel.setReceiptDate((String) row.get("ReceiptDate"));
					responseModel.setReceiptBy((String) row.get("ReceiptBy"));
					responseModel.setIssueNumber((String) row.get("IssueNumber"));
					responseModel.setIssueDate((String) row.get("IssueDate"));
					responseModel.setReceiptFrom((String) row.get("ReceiptFrom"));
					responseModel.setReceiptToBranch((String) row.get("ReceiptToBranch"));
					responseModel.setId3((BigInteger) row.get("IssueId"));
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
	public ReceiptSearchMainResponseModel searchReceiptList(String userCode, ReceiptSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchReceiptList invoked.." + requestModel.toString());
		}
		Session session = null;
		ReceiptSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchReceiptList(session, userCode, requestModel);
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
