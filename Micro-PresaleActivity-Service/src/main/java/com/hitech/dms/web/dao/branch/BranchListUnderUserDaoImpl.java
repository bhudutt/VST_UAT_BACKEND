/**
 * 
 */
package com.hitech.dms.web.dao.branch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.branch.UserBranchListModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class BranchListUnderUserDaoImpl implements BranchListUnderUserDao {
	private static final Logger logger = LoggerFactory.getLogger(BranchListUnderUserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonServiceClient commonServiceClient;

	public List<UserBranchListModel> fetchUserBranchList(String authorizationHeader, String isInactiveInclude) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserBranchList invoked..");
		}
		List<UserBranchListModel> branchList = null;
		try {
			HeaderResponse headerResponse = commonServiceClient.branchesUnderUserList(authorizationHeader,
					isInactiveInclude);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					branchList = jsonArrayToObjectList(jsonString, UserBranchListModel.class);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return branchList;
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}
}
