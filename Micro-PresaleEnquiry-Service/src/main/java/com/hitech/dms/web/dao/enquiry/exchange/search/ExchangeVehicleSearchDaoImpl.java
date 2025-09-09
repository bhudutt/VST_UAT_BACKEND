/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.exchange.search;

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
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.enquiry.exchange.request.ExchangeVehicleSearchRequestModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeEnquiryRes;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleSearchMainResponseModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleSearchResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptTypeResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ExchangeVehicleSearchDaoImpl implements ExchangeVehicleSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeVehicleSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ExchangeVehicleSearchMainResponseModel fetchExchangeVehicleEnquiryList(String userCode,
			ExchangeVehicleSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchExchangeVehicleEnquiryList invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		ExchangeVehicleSearchMainResponseModel exchangeVehicleSearchMainResponseModel = null;
		List<ExchangeVehicleSearchResponseModel> exchangeVehicleSearchResponseList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_ENQ_EXCHANGE_VEH_LIST] :userCode, :pcId, :orgId, :dealerId, :branchId, :enquiryNumber, :fromDate, :toDate, :vehicleStatus, :includeInActive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcID());
			query.setParameter("orgId", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerID());
			query.setParameter("branchId", requestModel.getBranchID());
			query.setParameter("enquiryNumber", requestModel.getEnquiryNumber());
			query.setParameter("fromDate", requestModel.getExchangeFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getExchangeFromDate()));
			query.setParameter("toDate", requestModel.getExchangeToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getExchangeToDate()));
			query.setParameter("vehicleStatus", requestModel.getVehicleStatus());
			query.setParameter("includeInActive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				exchangeVehicleSearchMainResponseModel = new ExchangeVehicleSearchMainResponseModel();
				exchangeVehicleSearchResponseList = new ArrayList<ExchangeVehicleSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					ExchangeVehicleSearchResponseModel exchangeVehicleSearchResponseModel = new ExchangeVehicleSearchResponseModel();
					exchangeVehicleSearchResponseModel.setSrNo((BigInteger) row.get("SrNo"));
					exchangeVehicleSearchResponseModel.setAction((String) row.get("action"));
					exchangeVehicleSearchResponseModel.setOldVehId((BigInteger) row.get("old_veh_id"));
					exchangeVehicleSearchResponseModel.setBranchId((BigInteger) row.get("Branch_Id"));
					exchangeVehicleSearchResponseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					exchangeVehicleSearchResponseModel.setEnqNumber((String) row.get("enquiryNumber"));
					exchangeVehicleSearchResponseModel.setVehStatus((String) row.get("vehicleStatus"));
					exchangeVehicleSearchResponseModel.setBrandName((String) row.get("BrandName"));
					exchangeVehicleSearchResponseModel.setModelName((String) row.get("ModelName"));
					exchangeVehicleSearchResponseModel.setModelYear((Integer) row.get("ModelYear"));
					exchangeVehicleSearchResponseModel.setInvInDate((String) row.get("InvInDate"));
					exchangeVehicleSearchResponseModel.setEstimatedExchangePrice((BigDecimal) row.get("EstimatedExchangePrice"));
					exchangeVehicleSearchResponseModel.setBuyerName((String) row.get("BuyerName"));
					exchangeVehicleSearchResponseModel.setBuyerContactNumber((String) row.get("BuyerContactNumber"));
					exchangeVehicleSearchResponseModel.setSaleDate((String) row.get("SaleDate"));
					exchangeVehicleSearchResponseModel.setSaleRemarks((String) row.get("SaleRemarks"));
					exchangeVehicleSearchResponseModel.setSellingPrice((BigDecimal) row.get("SellingPrice"));
					
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					exchangeVehicleSearchResponseList.add(exchangeVehicleSearchResponseModel);
				}

				exchangeVehicleSearchMainResponseModel.setExchangeVehicleSearchList(exchangeVehicleSearchResponseList);
				exchangeVehicleSearchMainResponseModel.setCountRecords(recordCount);
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
		return exchangeVehicleSearchMainResponseModel;
	}

	@Override
	public List<ExchangeEnquiryRes> getEnquiryNumbers(String userCode) {
		    Session session = null;
			List<ExchangeEnquiryRes> pcTypeList = null;
			ExchangeEnquiryRes responseModel = null;
			NativeQuery<?> query = null;
			String sqlQuery = "exec [SP_FM_GetENQ_NUMBER] :userCode";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<?> data = query.list();
				if (data != null && !data.isEmpty()) {
					pcTypeList = new ArrayList<ExchangeEnquiryRes>();
					for (Object object : data) {
						@SuppressWarnings("rawtypes")
						Map row = (Map) object;
					
						responseModel = new ExchangeEnquiryRes();
						responseModel.setEnquiryId((BigInteger)row.get("enquiry_id"));
						responseModel.setEnqNumber((String) row.get("enquiryNumber"));
						
						pcTypeList.add(responseModel);
					}
				}
			} catch (SQLGrammarException sqlge) {
				logger.error(this.getClass().getName(), sqlge);
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			} finally {
				if (session != null) {
					session.close();
				}
			}

			return pcTypeList;
		}
}
