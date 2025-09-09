/**
 * 
 */
package com.hitech.dms.web.dao.allotment.autolist;

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

import com.hitech.dms.web.model.allot.autolist.request.MachineEnqAllotAutoListRequestModel;
import com.hitech.dms.web.model.allot.autolist.response.MachineEnqAllotAutoListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineAllotAutoListDaoImpl implements MachineAllotAutoListDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineAllotAutoListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<MachineEnqAllotAutoListResponseModel> fetchEnqListForAllot(String userCode,
			MachineEnqAllotAutoListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqListForAllot invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<MachineEnqAllotAutoListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_ALLOT_ENQ_AUTO_LIST] :userCode, :pcId, :dealerId, :branchId, :includeInactive, :searchText, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("searchText", requestModel.getSearchText());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<MachineEnqAllotAutoListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					MachineEnqAllotAutoListResponseModel responseModel = new MachineEnqAllotAutoListResponseModel();
					responseModel.setEnquiryHdrId((BigInteger) row.get("EnquiryId"));
					responseModel.setEnquiryNo((String) row.get("EnquiryNumber"));
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
