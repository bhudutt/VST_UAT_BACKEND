/**
 * 
 */
package com.hitech.dms.web.dao.dc.autolist;

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

import com.hitech.dms.web.model.dc.autolist.request.AllotForDCAutoListRequestModel;
import com.hitech.dms.web.model.dc.autolist.response.AllotForDCAutoListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AllotForDCAutoListDaoImpl implements AllotForDCAutoListDao {
	private static final Logger logger = LoggerFactory.getLogger(AllotForDCAutoListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<AllotForDCAutoListResponseModel> fetchAllotListForDc(String userCode,
			AllotForDCAutoListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAllotListForDc invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<AllotForDCAutoListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_DC_ALLOT_ENQ_AUTO_LIST] :userCode, :pcId, :dealerId, :branchId, :searchText, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("searchText", requestModel.getSearchText());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<AllotForDCAutoListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					AllotForDCAutoListResponseModel responseModel = new AllotForDCAutoListResponseModel();
					responseModel.setMachineAllotmentId((BigInteger) row.get("MachineAllotmentId"));
					responseModel.setAllotNumber((String) row.get("AllotNumber"));
					responseModel.setAllotDate((String) row.get("AllotDate"));
					responseModel.setEnquiryHdrId((BigInteger) row.get("EnquiryId"));
					responseModel.setEnquiryNo((String) row.get("EnquiryNumber"));
					responseModel.setEnquiryType((String) row.get("EnquiryType"));
					responseModel.setEnquiryDate((String) row.get("EnquiryDate"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));

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
}
