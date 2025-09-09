package com.hitech.dms.web.dao.enquiry.followup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.enquiry.followup.request.FollowupListRequestModel;
import com.hitech.dms.web.model.enquiry.followup.request.FollowupListResponseModel;
@Repository
public class EnquiryFollowupListDaoImpl implements EnquiryFollowupListDao{
	private static final Logger logger = LoggerFactory.getLogger(EnquiryFollowupListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Override
	public List<FollowupListResponseModel> FollowupList(String userCode,
			FollowupListRequestModel followupListRequestModel) {
		
		Session session = null;
		List<FollowupListResponseModel> responseModelList = null;
		FollowupListResponseModel responseModel = null;
		Query<FollowupListResponseModel> query = null;
		String sqlQuery = "Select Followup_Id, Followup_ActionName from ENQ_FollowUP_List";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<FollowupListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new FollowupListResponseModel();
					responseModel.setFollowupId((Integer) row.get("Followup_Id"));
					responseModel.setFollowupActionName((String) row.get("Followup_ActionName"));
					
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	

}
