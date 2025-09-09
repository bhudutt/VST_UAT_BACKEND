package com.hitech.dms.web.dao.enquiry.lostDrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.enquiry.lostDrop.request.LostDropResponseModel;

@Repository
public class EnquiryLostDropDaoImpl implements EnquiryLostDropDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryLostDropDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<LostDropResponseModel> getLostDropReason(String flag) {

		Session session = null;
		List<LostDropResponseModel> result = null;
		LostDropResponseModel model = null;
		try {
			session = sessionFactory.openSession();
			NativeQuery query = session.createSQLQuery("Exec SP_ENQ_getLostDropReason :Flag");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List list = query.setParameter("Flag", flag).list();
			if (list != null && !list.isEmpty()) {
				result = new ArrayList<LostDropResponseModel>();
				for (Object obj : list) {
					Map row = (Map) obj;
					model = new LostDropResponseModel();
					model.setReasonId((Integer) row.get("enq_reason_id"));
					model.setReasonCode((String) row.get("ReasonCode"));
					model.setReasonDescription((String) row.get("ReasonDescription"));
					result.add(model);
				}
			}

		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}
}
