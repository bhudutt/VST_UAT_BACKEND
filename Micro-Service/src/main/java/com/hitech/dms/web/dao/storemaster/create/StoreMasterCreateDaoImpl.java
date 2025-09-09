package com.hitech.dms.web.dao.storemaster.create;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.master.storemaster.request.StoreMasterEntity;
import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterFormRequestModel;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterModel;
import com.hitech.dms.web.model.storemaster.create.response.StoreMasterCreateResponseModel;
import com.hitech.dms.web.model.storemaster.create.response.StroeSearchResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

@Repository
public class StoreMasterCreateDaoImpl implements StoreMasterCreateDao {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreMasterCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	
	@Autowired
	private CommonDao commonDao;
	
	
	@Autowired
	private CommonServiceClient commonServiceClient;

	@Override
	public StoreMasterCreateResponseModel createStoreMaster(String authorizationHeader, String userCode,
			StoreMasterFormRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createStore invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		StoreMasterCreateResponseModel responseModel = new StoreMasterCreateResponseModel();
		StoreMasterEntity entity=null;
		List<String> duplicate=new ArrayList<String>();
		List<String> added=new ArrayList<String>();
		boolean isSuccess = true;
		String msg="";
		Boolean mainBranchCheck=false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			List<StoreMasterEntity> storeMasterEntityList = null;
			List<StoreMasterModel> storeMasterModelList = requestModel.getStoreMasterList();
			Date todayDate = new Date();
			String userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId=Id.toString();
			}
			if (null != storeMasterModelList && !storeMasterModelList.isEmpty()) {
				storeMasterEntityList = new ArrayList<StoreMasterEntity>();
				for (StoreMasterModel storeMasterModel : storeMasterModelList) {
					entity = mapper.map(storeMasterModel, StoreMasterEntity.class, "storeMasterMapId");
					entity.setBranchId(entity.getBranchId());
					if (entity.getBranchStoreId() != null) {
						entity.setModifiedBy(userCode);
						entity.setModifiedDate(todayDate);
					} else {
						entity.setCreatedBy(userCode);
						entity.setCreatedDate(todayDate);
					}
					storeMasterEntityList.add(entity);
				}
			}
			List<Integer> ids=new ArrayList<Integer>();
			if (null != storeMasterEntityList && !storeMasterEntityList.isEmpty()) {
				for (StoreMasterEntity storeMasterEntity : storeMasterEntityList) {
					if (storeMasterEntity.getBranchStoreId() == null) 
					{
						 mainBranchCheck=checkMainBranch(session,storeMasterEntity);
						if(mainBranchCheck) {
							msg="Main store is already exist for this Branch";
							//responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
							isSuccess = false;
						}else {
							Boolean check=checkDuplicate(session,storeMasterEntity);
							if(check) {
								duplicate.add(storeMasterEntity.getStoreCode());
							}else {
								Integer id = (Integer) session.save(storeMasterEntity);
								if(id>0) {
									added.add(storeMasterEntity.getStoreCode());
								}
							}
						}
					} else {
						ids.add(storeMasterEntity.getBranchStoreId());
						session.update(storeMasterEntity);
					}
				}
				transaction.commit();
				
			}
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		}finally {
			if (isSuccess) {
				responseModel.setMsg(""+"Successfully Created store="+added+" Duplicate store="+duplicate);
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				if(mainBranchCheck) {
					responseModel.setMsg(msg);
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}else {
					responseModel.setMsg("Some Issue While Creating Store.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
				
			}
			if (session != null) {
				session.close();
			}
		}
		

		return responseModel;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public StoreMasterCreateResponseModel updateStoreMaster(String userCode, Integer storeId, String isActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateStoreMaster invoked.." + userCode);
			logger.debug(storeId.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		Map<String, Object> mapData = null;
		StoreMasterCreateResponseModel storeMasterResponse = new StoreMasterCreateResponseModel();
		Date todayDate = new Date();
		Query query = null;
		Query query1 = null;
		boolean isSuccess = true;
		BigInteger userId = null;
		try {
			String sqlQuery = "update PA_BRANCH_STORE set IsActive =:IsActive,  ModifiedDate =:ModifiedDate ,"
					+ " ModifiedBy =:ModifiedBy where branch_store_id = :storeMasterId";
			String sqlQuery1 = "select  * from PA_STOCK_STORE where branch_store_id=:storeMasterId";
					
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				
				query1 = session.createSQLQuery(sqlQuery1);
				query1.setParameter("storeMasterId", storeId);
				query1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List list = query1.list();
				if (list != null && list.size() > 0) {
					storeMasterResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
					msg = "You can not De-Activate because this store contain some parts";
					storeMasterResponse.setMsg(msg);
					isSuccess = false;
				}else {
					query = session.createSQLQuery(sqlQuery);
					query.setParameter("storeMasterId", storeId);
					query.setParameter("IsActive", isActive);
					query.setParameter("ModifiedDate", todayDate);
					query.setParameter("ModifiedBy", userId);
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					int queryUpdate = query.executeUpdate();
					if(queryUpdate == 0) {
						isSuccess = false;
					}
				}
				
				

			}
			
		if (isSuccess) {
			transaction.commit();
			session.close();
//			bayMasterResponse.setBayCode(.getBayCode());
			storeMasterResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Store Master Updated Successfully";
			storeMasterResponse.setMsg(msg);
		} else {
			transaction.commit();
			session.close();
			storeMasterResponse.setStatusCode(statusCode);
			storeMasterResponse.setMsg(msg);
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			storeMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			storeMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			storeMasterResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return storeMasterResponse;
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
	public Boolean checkDuplicate(Session session,StoreMasterEntity storeMasterEntity) {
		Boolean check=false;
		Query query = null;
		try {
			String sqlQuery = "select * from PA_BRANCH_STORE where branch_id=:branch_id and StoreCode=:StoreCode ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branch_id", storeMasterEntity.getBranchId());
			query.setParameter("StoreCode", storeMasterEntity.getStoreCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if(data !=null && data.size()>0) {
				check=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return check;
	}
	
	public Boolean checkMainBranch(Session session,StoreMasterEntity storeMasterEntity) {
		Boolean check=false;
		Query query = null;
		try {
			
			Boolean mainStoreStatus = storeMasterEntity.getMainStoreStatus();
			if(mainStoreStatus) {
				String sqlQuery = "select * from PA_BRANCH_STORE where branch_id=:branch_id and isMainStore=:isMainStore ";
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("branch_id", storeMasterEntity.getBranchId());
				query.setParameter("isMainStore", 'Y');
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if(data !=null && data.size()>0) {
					check=true;
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return check;
	}
	
	
	public List<StroeSearchResponseModel> searchStoreMaster(String authorizationHeader, String userCode,Integer page, Integer size,Integer dealerId) {
		if (logger.isDebugEnabled()) {
			//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Query query = null;
		Session session = null;
		StroeSearchResponseModel responseListModel = null;
		List<StroeSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec SP_GET_BRANCH_UnderUser :usercode, :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", userCode);
			query.setParameter("includeInactive", 'N');
			query.setParameter("page", page);
			query.setParameter("size", size);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new StroeSearchResponseModel();
				responseModelList = new ArrayList<StroeSearchResponseModel>();
				StroeSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new StroeSearchResponseModel();
					responseModel.setStoreId((Integer) row.get("branch_store_id"));
					responseModel.setStoreCode((String) row.get("StoreCode"));
					responseModel.setStoreDesc((String) row.get("StoreDesc"));
					Character isActive=(Character) row.get("IsActive");
							if(isActive.equals('Y')) {
								responseModel.setActive("Y");
							}else {
								responseModel.setActive("N");
							}
					
					Character isMainStore=(Character) row.get("isMainStore");
							if(isMainStore.equals('Y')) {
								responseModel.setDefauleStore("Y");
							}else {
								responseModel.setDefauleStore("N");
							}
					

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
		}
		return responseModelList;
	}




	
}
