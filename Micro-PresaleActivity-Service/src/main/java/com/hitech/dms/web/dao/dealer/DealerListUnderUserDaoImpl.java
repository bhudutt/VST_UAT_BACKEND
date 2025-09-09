/**
 * 
 */
package com.hitech.dms.web.dao.dealer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.dealer.UserDealerResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;


@Repository
public class DealerListUnderUserDaoImpl implements DealerListUnderUserDao {
	private static final Logger logger = LoggerFactory.getLogger(DealerListUnderUserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonServiceClient commonServiceClient;

	public List<UserDealerResponseModel> fetchUserDealerList(String authorizationHeader, String isInactiveInclude) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserDealerList invoked..");
		}
		List<UserDealerResponseModel> dlrList = null;
		try {
			HeaderResponse headerResponse = commonServiceClient.dealersUnderUserList(authorizationHeader,
					isInactiveInclude, "ACTIVITYPLAN");
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					dlrList = jsonArrayToObjectList(jsonString, UserDealerResponseModel.class);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return dlrList;
	}
	
	
	public List<UserDealerResponseModel> fetchUserDealerListNew(String authorizationHeader, String isInactiveInclude,Integer pcId,String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserDealerList invoked..");
		}
		List<UserDealerResponseModel> dlrList = null;
		
		Session session = null;
		Query query = null;
		String sqlQuery = "exec [SP_CM_getDealersUnderUser_byProfit_center] :userCode, :isInclude, :isFor, :pcId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isInclude", isInactiveInclude);
			query.setParameter("isFor", "ACTIVITYPLAN");
			query.setParameter("pcId", pcId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				dlrList = new ArrayList<UserDealerResponseModel>();
				UserDealerResponseModel obj=null;
				for (Object object : data) {
					Map row = (Map) object;
					obj=new UserDealerResponseModel();
					obj.setDealerCode((String) row.get("DEALER_CODE"));
					obj.setDealerId((BigInteger) row.get("DEALER_ID"));
					obj.setDealerLocation((String) row.get("DEALER_LOCATION"));
					obj.setDealerName((String) row.get("DEALER_NAME"));
					dlrList.add(obj);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return dlrList;
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}
}
