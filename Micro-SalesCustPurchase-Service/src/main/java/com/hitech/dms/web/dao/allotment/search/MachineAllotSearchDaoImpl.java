/**
 * 
 */
package com.hitech.dms.web.dao.allotment.search;

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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.allot.search.request.MachineAllotSearchRequestModel;
import com.hitech.dms.web.model.allot.search.response.MachineAllotSearchMainResponseModel;
import com.hitech.dms.web.model.allot.search.response.MachineAllotSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineAllotSearchDaoImpl implements MachineAllotSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineAllotSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public MachineAllotSearchMainResponseModel searchAllotList(Session session, String userCode,
			MachineAllotSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAllotList invoked.." + requestModel.toString());
		}
		Query query = null;
		MachineAllotSearchMainResponseModel responseListModel = null;
		List<MachineAllotSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_ALLOT_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :allotNumber, :enqNumber, :series, :segment, :model, :variant, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("allotNumber", requestModel.getAllotNumber());
			query.setParameter("enqNumber", requestModel.getEnquiryNo());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
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
				responseListModel = new MachineAllotSearchMainResponseModel();
				responseModelList = new ArrayList<MachineAllotSearchResponseModel>();
				MachineAllotSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachineAllotSearchResponseModel();
					responseModel.setId((BigInteger) row.get("MachineAllotmentId"));
					responseModel.setId1((BigInteger) row.get("dealer_id"));
					responseModel.setId2((Integer) row.get("pcId"));
//					responseModel.setZone((String) row.get("ZONE"));
//					responseModel.setArea((String) row.get("AREA"));
					responseModel.setAllotNumber((String) row.get("AllotmentNumber"));
					responseModel.setAllotDate((String) row.get("AllotmentDate"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcDesc((String) row.get("PROFIT_CENTER"));
					responseModel.setAllotStatus((String) row.get("AllotStatus"));
					responseModel.setEnquiryNo((String) row.get("EnquiryNo"));
					responseModel.setEnquiryDate((String) row.get("EnquiryDate"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setOnlyImplementFlag((String) row.get("OnlyImplementFlag"));
					responseModel.setDeAllotFlag((String) row.get("DeAllotFlag"));
					responseModel.setDeAllotDate((String) row.get("DeAllotDate"));
					responseModel.setDeAllotReason((String) row.get("DeAllotReason"));
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
	public MachineAllotSearchMainResponseModel searchAllotList(String userCode,
			MachineAllotSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAllotList invoked.." + requestModel.toString());
		}
		Session session = null;
		MachineAllotSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchAllotList(session, userCode, requestModel);
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
