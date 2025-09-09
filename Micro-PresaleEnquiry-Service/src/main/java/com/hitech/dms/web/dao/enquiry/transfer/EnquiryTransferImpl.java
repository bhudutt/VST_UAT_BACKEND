/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.transfer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.enquiry.transfer.EnquiryTransferEntity;
import com.hitech.dms.web.model.enquiry.salesman.request.SalesmanListFormModel;
import com.hitech.dms.web.model.enquiry.salesman.response.SalesmanListModel;
import com.hitech.dms.web.model.enquiry.transfer.request.EnquiryTransferRequestModel;
import com.hitech.dms.web.model.enquiry.transfer.response.EnquiryTransferResponseModel;
import com.hitech.dms.web.service.salesman.request.SalesmanServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquiryTransferImpl implements EnquiryTransferDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryTransferImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private SalesmanServiceClient salesmanServiceClient;

	@SuppressWarnings("unchecked")
	private Map<String, Object> validateSalesman(String authorizationHeader, BigInteger salesmanTransferId,
			BigInteger branchId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		SalesmanListFormModel formModel = new SalesmanListFormModel();
		formModel.setDealerOrBranch("BRANCH");
		formModel.setDealerOrBranchID(branchId.longValue());
		HeaderResponse userAuthResponse = salesmanServiceClient.getSalesmanList(authorizationHeader,
				"BRANCH", branchId.longValue());
		Object object = userAuthResponse.getResponseData();
		List<SalesmanListModel> salesmanList = null;
		if(object != null) {
			try {
				String jsonString = new com.google.gson.Gson().toJson(object);
				salesmanList = jsonArrayToObjectList(jsonString, SalesmanListModel.class);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(this.getClass().getName(), e);
			}
		}
		if (salesmanList != null && !salesmanList.isEmpty()) {
			boolean isExist = salesmanList.stream()
					.anyMatch(salesmanModel -> salesmanTransferId.compareTo(salesmanModel.getSalesmanID()) == 0);
			if(isExist) {
				mapData.put("SUCCESS", true);
			}else {
				mapData.put("SUCCESS", "Salesman Not Found.");
			}
		} else {
			mapData.put("ERROR", "Salesman Not Found.");
		}

		return mapData;
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public EnquiryTransferResponseModel transferEnqSalesman(String authorizationHeader, String userCode,
			EnquiryTransferRequestModel enquiryTransferRequestModel, Device device) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		EnquiryTransferEntity enquiryTransferEntity = null;
		EnquiryTransferResponseModel responseModel = new EnquiryTransferResponseModel();
		boolean isSuccess = false;
		String sqlQuery = "update SA_ENQ_HDR set salesman_id =:salesmanId, ModifiedBy =:modifiedBy, ModifiedDate = GetDate() where enquiry_id in (:enquiryIdList) and branch_id =:branchId and salesman_id =:salesmanFromId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				// validate TransferToId
				mapData = validateSalesman(authorizationHeader, enquiryTransferRequestModel.getTransferToId(),
						enquiryTransferRequestModel.getBranchId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					query = session.createNativeQuery(sqlQuery);
					query.setParameterList("enquiryIdList", enquiryTransferRequestModel.getEnquiryIdList());
					query.setParameter("branchId", enquiryTransferRequestModel.getBranchId());
					query.setParameter("salesmanId", enquiryTransferRequestModel.getTransferToId());
					query.setParameter("salesmanFromId", enquiryTransferRequestModel.getTransferFromId());
					query.setParameter("modifiedBy", userId);
					int result = query.executeUpdate();
					if (result > 0) {
						if (enquiryTransferRequestModel.getEnquiryIdList() != null
								&& !enquiryTransferRequestModel.getEnquiryIdList().isEmpty()) {
							for (BigInteger enquiryId : enquiryTransferRequestModel.getEnquiryIdList()) {
								enquiryTransferEntity = new EnquiryTransferEntity();
								enquiryTransferEntity.setEnquiryHdrId(enquiryId);
								enquiryTransferEntity
										.setTransferFromId(enquiryTransferRequestModel.getTransferFromId());
								enquiryTransferEntity.setTransferToId(enquiryTransferRequestModel.getTransferToId());
								enquiryTransferEntity.setTransferDate(new Date());
								enquiryTransferEntity.setCreatedBy(userId);
								enquiryTransferEntity.setCreatedDate(new Date());
								session.save(enquiryTransferEntity);
							}
						}
						responseModel.setMsg("Salesman Transfered Successfully.");
						isSuccess = true;
						transaction.commit();
					} else {
						// enquiry ids not found
						responseModel.setMsg("Selected Enquiries not found.");
					}
				} else {
					// Transfer Salesman not found
					responseModel.setMsg("Selected Transfer Salesman not found.");
				}
			} else {
				// user not found
				responseModel.setMsg("User not found");
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg("Software Exception, please inform.");
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg("Software Exception, please inform.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg("Software Exception, please inform.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if(isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Object> T cast(Object obj) {
	    return (T) obj;
	}
	
	public static List<?> convertObjectToList(Object obj) {
	    List<?> list = new ArrayList<>();
	    if (obj.getClass().isArray()) {
	        list = Arrays.asList((Object[])obj);
	    } else if (obj instanceof Collection) {
	        list = new ArrayList<>((Collection<?>)obj);
	    }
	    return list;
	}
	
	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
	    ObjectMapper mapper = new ObjectMapper();
	    CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
	    List<T> ts = mapper.readValue(json, listType);
	    logger.debug("class name: {}", ts.get(0).getClass().getName());
	    return ts;
	}
}
