package com.hitech.dms.web.dao.partmaster.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.hitech.dms.app.utils.DmsCollectionUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.storemaster.create.StoreMasterCreateDaoImpl;
import com.hitech.dms.web.entity.master.storemaster.request.StoreMasterEntity;
import com.hitech.dms.web.entity.partmaster.create.request.AdminPartMasterEntity;
import com.hitech.dms.web.entity.partmaster.create.request.BranchPartMasterEntity;
import com.hitech.dms.web.entity.partmaster.create.request.PartStockBinEntity;
import com.hitech.dms.web.entity.partmaster.create.request.PartStockStoreEntity;
import com.hitech.dms.web.model.partmaster.create.request.PartControlModel;
import com.hitech.dms.web.model.partmaster.create.request.PartMasterFormRequestModel;
import com.hitech.dms.web.model.partmaster.create.request.PartSearchRequestModel;
import com.hitech.dms.web.model.partmaster.create.request.PartStockBinModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterAutoListResponseModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterCreateResponseModel;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterFormRequestModel;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterModel;
import com.hitech.dms.web.model.storemaster.create.response.StoreMasterCreateResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;
import com.hitech.dms.web.spare.party.model.mapping.request.PartBranchRequestModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartBranchStoreResponseModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartStockBinRequestModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartTableRequestModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingResponseModel;

@Repository
public class PartMasterCreateDaoImpl implements PartMasterCreateDao {


	
	private static final Logger logger = LoggerFactory.getLogger(PartMasterCreateDaoImpl.class);
	
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
	private static String BIN_TYPE = "NORMAL";

	@Override
	public PartMasterCreateResponseModel createPartMaster(String authorizationHeader, String userCode,
			PartMasterFormRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createStore invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PartMasterCreateResponseModel responseModel = new PartMasterCreateResponseModel();
		BranchPartMasterEntity partMasterEntity=null;
		boolean isSuccess = true;
		
		List<PartStockBinEntity> partStockBinEntityList = null;
		BranchPartMasterEntity dataBaseBranchPartEntity = null;
		AdminPartMasterEntity dbAdminPartMasterEntity = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId=Id.toString();
			}
			Boolean updateFlag = false;
			 partMasterEntity = mapper.map(requestModel, BranchPartMasterEntity.class,
					"branchPartMasterDetailsMapId");
			 
			 if (null != partMasterEntity && partMasterEntity.getPartBranchId() != null) {
					partStockBinEntityList = partMasterEntity.getPartStockBinEntityList();
					session.evict(partMasterEntity);
					dataBaseBranchPartEntity = (BranchPartMasterEntity) session.get(BranchPartMasterEntity.class,
							partMasterEntity.getPartBranchId());
					System.out.println(dataBaseBranchPartEntity.getPartBranchId());
					dbAdminPartMasterEntity = (AdminPartMasterEntity) session.get(AdminPartMasterEntity.class,
							dataBaseBranchPartEntity.getPartId());
					System.out.println(dbAdminPartMasterEntity.getOemPartIndicator());
					dataBaseBranchPartEntity.setAdminPartMasterEntity(dbAdminPartMasterEntity);

					setDataToUpdatedBranchPartMasterEntity(partMasterEntity, dataBaseBranchPartEntity);
					setBranchPartMasterCreatedModifiedDetails(dataBaseBranchPartEntity, userCode);
					session.update(dataBaseBranchPartEntity);
					managePartBranchBin(partStockBinEntityList, userCode, partMasterEntity.getBranchId(), partMasterEntity, session);
					updateFlag = true;
				}
			 transaction.commit();
			 
			
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
				responseModel.setMsg("Part Master Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				responseModel.setMsg("Some Issue While Creating Store.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		

		return responseModel;
	}
	
	private void setDataToUpdatedBranchPartMasterEntity(BranchPartMasterEntity branchPartMasterEntity,
			BranchPartMasterEntity dataBaseBranchPartEntity) {
		dataBaseBranchPartEntity.setAccountCategoryId(branchPartMasterEntity.getAccountCategoryId());
		if (dataBaseBranchPartEntity.getAdminPartMasterEntity().getOemPartIndicator() != null
				&& dataBaseBranchPartEntity.getAdminPartMasterEntity().getOemPartIndicator().equalsIgnoreCase("l")) {
			dataBaseBranchPartEntity.setPurchasePrice(branchPartMasterEntity.getNdp());
			dataBaseBranchPartEntity.setMRP(branchPartMasterEntity.getMRP());
			dataBaseBranchPartEntity.setNdp(branchPartMasterEntity.getNdp());
		}
		dataBaseBranchPartEntity.setAuctionable(branchPartMasterEntity.getAuctionable());
		if (branchPartMasterEntity.getAuctionable() != null && branchPartMasterEntity.getAuctionable()) {
			dataBaseBranchPartEntity.setAuctionableQty(branchPartMasterEntity.getAuctionableQty());
			dataBaseBranchPartEntity.setAuctionablePart(branchPartMasterEntity.getAuctionablePart());
			dataBaseBranchPartEntity.setAuctionablePartRate(branchPartMasterEntity.getAuctionablePartRate());
			if (branchPartMasterEntity.getDocName() != null) {
				dataBaseBranchPartEntity.setDocName(branchPartMasterEntity.getDocName());
				dataBaseBranchPartEntity.setDocPath(branchPartMasterEntity.getDocPath());
				dataBaseBranchPartEntity.setDocType(branchPartMasterEntity.getDocType());
			}
		}

	}
	private void setBranchPartMasterCreatedModifiedDetails(BranchPartMasterEntity branchPartMasterEntity,
			String userCode) {
		if (null != branchPartMasterEntity) {
			if (null == branchPartMasterEntity.getCreatedBy() && null == branchPartMasterEntity.getCreatedDate()) {
				branchPartMasterEntity.setCreatedBy(userCode);
				branchPartMasterEntity.setCreatedDate(new Date());
			} else {
				branchPartMasterEntity.setModifiedBy(userCode);
				branchPartMasterEntity.setModfiedDate(new Date());

			}
		}
	}

	private void managePartBranchBin(List<PartStockBinEntity> partStockBinEntityList, String userCode, Integer branchId,
			BranchPartMasterEntity branchPartMasterEntity, Session session) {
		Query query = null;
		Integer stockStoreId = null;
		PartStockStoreEntity partStockStoreEntity = null;
		if (DmsCollectionUtils.isNotEmpty(partStockBinEntityList)) {
			for (PartStockBinEntity partStockBinEntity : partStockBinEntityList) {
				if (partStockBinEntity.getStockBinId() == 0 && null != partStockBinEntity.getBinName()
						&& !partStockBinEntity.getBinName().isEmpty()) {
					query = session.createQuery(
							"SELECT stockStoreId FROM PartStockStoreEntity WHERE partBranchId=:partBranchId and branchStoreId=:branchStoreId ");
					query.setParameter("branchStoreId", partStockBinEntity.getBranchStoreId());
					query.setParameter("partBranchId", branchPartMasterEntity.getPartBranchId());
					List list = query.list();
					if (DmsCollectionUtils.isNotEmpty(list)) {
						stockStoreId = (Integer) list.get(0);
					} else {
						partStockStoreEntity = new PartStockStoreEntity();
						partStockStoreEntity.setBranchId(branchId);
						partStockStoreEntity.setPartBranchId(branchPartMasterEntity.getPartBranchId());
						partStockStoreEntity.setBranchStoreId(partStockBinEntity.getBranchStoreId());
						partStockStoreEntity.setCurrentStock(new BigDecimal(0.0));
						partStockStoreEntity.setCurrentStockAmount(new BigDecimal(0.0));
						partStockStoreEntity.setCreatedBy(userCode);
						partStockStoreEntity.setCreatedDate(new Date());
						session.save(partStockStoreEntity);
						stockStoreId = partStockStoreEntity.getStockStoreId();
					}
					partStockBinEntity.setBranchId(branchId);
					partStockBinEntity.setPartBranchId(branchPartMasterEntity.getPartBranchId());
					partStockBinEntity.setStockStoreId(stockStoreId);
					partStockBinEntity.setBinType(BIN_TYPE);
					partStockBinEntity.setBinBlocked(Boolean.FALSE);
					if (partStockBinEntity.getActiveStatus() == null) {
						partStockBinEntity.setActiveStatus(true);
					}
					if (partStockBinEntity.getDeafultStatus() == null) {
						partStockBinEntity.setDeafultStatus(false);
					}
					partStockBinEntity.setCurrentStockAmount(BigDecimal.ZERO);
					partStockBinEntity.setCreatedBy(userCode);
					partStockBinEntity.setCreatedDate(new Date());
					session.save(partStockBinEntity);
				} else {
					partStockBinEntity.setModifiedBy(userCode);
					partStockBinEntity.setModifiedDate(new Date());
					updateStockBinEntity(session, partStockBinEntity, branchPartMasterEntity.getPartBranchId(),
							branchId, userCode);
				}
			}
		}
	}
	
	private boolean updateStockBinEntity(Session session, PartStockBinEntity partStockBinEntity, Integer partBranchId,
			Integer logginBranchId, String userCode) {
		Query query = null;
		boolean updatableStockBinFlag = false;
		try {
			query = session.createQuery(
					"UPDATE PartStockBinEntity set modifiedBy=:modifiedBy,modifiedDate=:modifiedDate,deafultStatus=:deafultStatus,activeStatus=:activeStatus WHERE stockBinId=:stockBinId");
			query.setParameter("modifiedBy", userCode);
			query.setParameter("modifiedDate", new Date());
			query.setParameter("deafultStatus", partStockBinEntity.getDeafultStatus());
			query.setParameter("activeStatus", partStockBinEntity.getActiveStatus());
			query.setParameter("stockBinId", partStockBinEntity.getStockBinId());
			query.executeUpdate();
			updatableStockBinFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return updatableStockBinFlag;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<PartMasterAutoListResponseModel> fetchPartNumber(String userCode,
			PartSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + requestModel.toString());
		}

		Session session = null;
		Query query = null;
		
		
		List<PartMasterAutoListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SearchPartDetails] :branchId, :criteria, :searchOn, :division, :partCategoryCode";
		try {
			
			
			session = sessionFactory.openSession();
			Integer branchId=getBranchIdByUSerCode(userCode,session);

			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("criteria", requestModel.getCriteria());
			query.setParameter("searchOn", requestModel.getSearchOn());
			query.setParameter("division", requestModel.getPartDivision());
			query.setParameter("partCategoryCode", requestModel.getPartCategoryCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PartMasterAutoListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					PartMasterAutoListResponseModel responseModel = new PartMasterAutoListResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("PartNumber"));
					responseModel.setDisplayValue((String) row.get("PartDesc"));
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
	
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public PartMasterFormRequestModel fetchPartNumberDetails(String userCode,
			String partNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNodetail invoked.." + partNumber);
		}
		Session session = null;
		Query query = null;
		PartMasterFormRequestModel responseModel=null;
		Map<String, Object> mapData = null;
		BranchPartMasterEntity branchPartMasterEntity = null;
		AdminPartMasterEntity adminPartMasterEntity = null;
		List<AdminPartMasterEntity> adminPartMasterEntitylist = null;
		List<BranchPartMasterEntity> branchPartMasterEntityList = null;
		List<PartStockBinEntity> partStockBinEntityDBList = null;
		Map<String, BigDecimal> partBranchStockKeyValues = null;
		try {
			session = sessionFactory.openSession();
			String userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			System.out.println("mapDatana "+mapData);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId=Id.toString();
			}
			
			Integer branchId=getBranchIdByUSerCode(userCode,session);
			System.out.println("branchId after we get"+branchId);
			query = session.createQuery("FROM AdminPartMasterEntity where partNumber= :partNumber");
			query.setParameter("partNumber", partNumber);
			adminPartMasterEntitylist = query.list();
			if (DmsCollectionUtils.isNotEmpty(adminPartMasterEntitylist)) {
				adminPartMasterEntity = adminPartMasterEntitylist.get(0);//PA_PART_TABLE
			}
			if (null != adminPartMasterEntity && null != adminPartMasterEntity.getId()) {
				query = session
						.createQuery("FROM BranchPartMasterEntity where partId= :partId and branch_id=:branchId ");//AND branchId= :branchId(will be use later
				query.setParameter("partId", adminPartMasterEntity.getId());
				query.setParameter("branchId", branchId);
				//System.out.println("query we build "+query);
				branchPartMasterEntityList = query.list();
				
				if (DmsCollectionUtils.isNotEmpty(branchPartMasterEntityList)) {
					branchPartMasterEntity = branchPartMasterEntityList.get(0);
				}
				if (null != branchPartMasterEntity) {
					branchPartMasterEntity.setAdminPartMasterEntity(adminPartMasterEntity);
					branchPartMasterEntity.setPartStockBinEntityList(partStockBinEntityDBList);
				}
				
				partBranchStockKeyValues=getPartBranchStockKeyValues(branchId, adminPartMasterEntity.getPartNumber(), adminPartMasterEntity.getId(),
						branchPartMasterEntity.getPartBranchId(), "ALL", session);
				
				if (partBranchStockKeyValues != null && !partBranchStockKeyValues.isEmpty()) {
					branchPartMasterEntity.setWipQty(partBranchStockKeyValues.get("wipQty"));
					branchPartMasterEntity.setStockAmount(partBranchStockKeyValues.get("stockAmount"));
					branchPartMasterEntity.setBackOrderQty(partBranchStockKeyValues.get("backOrderQty"));
					branchPartMasterEntity.setOnHandQty(partBranchStockKeyValues.get("onHandQty"));
					branchPartMasterEntity.setInTransitQty(partBranchStockKeyValues.get("inTransitQty"));
				} else {
					branchPartMasterEntity.setWipQty(BigDecimal.ZERO);
					branchPartMasterEntity.setStockAmount(BigDecimal.ZERO);
					branchPartMasterEntity.setBackOrderQty(BigDecimal.ZERO);
					branchPartMasterEntity.setOnHandQty(BigDecimal.ZERO);
					branchPartMasterEntity.setInTransitQty(BigDecimal.ZERO);
				}
				
				// get branch and part wise stock and all value 
				
			}
			if(branchPartMasterEntity==null) {
				responseModel.setErrorMsg("Part No. "+partNumber+"not found");
				return responseModel;
			}
			List<PartStockBinModel> partStockBinModelList = getPartStockBinDetails(1,
					branchPartMasterEntity.getPartBranchId());
			System.out.println("stockList "+partStockBinModelList);
			
			responseModel = mapper.map(branchPartMasterEntity,
					PartMasterFormRequestModel.class, "branchPartMasterDetailsMapId");
			responseModel.setPartStockBinModelList(partStockBinModelList);
			
			if (responseModel != null
					&& !responseModel.getAdminPartMasterModel().getControlCode().isEmpty()) {
				List<PartControlModel> partControlList=null;//fetchPartControlList(session,branchPartMasterModel.getAdminPartMasterModel().getControlCode());
				responseModel.setTaxControlCode(responseModel.getAdminPartMasterModel().getControlCode());
				responseModel.setHsn(responseModel.getAdminPartMasterModel().getControlCode());
				if (DmsCollectionUtils.isNotEmpty(partControlList)) {
					String taxCategory = "";
					int counter = 0;
					for (PartControlModel controlModel : partControlList) {
						if (counter == 0)
							taxCategory = taxCategory + controlModel.getTaxTypeDesc() + "@" + controlModel.getTaxRate()
									+ "%";
						else
							taxCategory = taxCategory + ", " + controlModel.getTaxTypeDesc() + "@"
									+ controlModel.getTaxRate() + "%";
						if (controlModel.getTaxTypeDesc().equalsIgnoreCase("IGST"))
							responseModel.setIgst("" + controlModel.getTaxRate());
						else if (controlModel.getTaxTypeDesc().equalsIgnoreCase("SGST"))
							responseModel.setSgst("" + controlModel.getTaxRate());
						else if (controlModel.getTaxTypeDesc().equalsIgnoreCase("CGST"))
							responseModel.setCgst("" + controlModel.getTaxRate());
						counter++;
					}
					responseModel.setTaxCategory(taxCategory);
				} else {
					responseModel.setCgst("0.00");
					responseModel.setSgst("0.00");
					responseModel.setIgst("0.00");
				}
			} else {
				responseModel.setTaxControlCode("");
				responseModel.setCgst("0.00");
				responseModel.setSgst("0.00");
				responseModel.setIgst("0.00");
			}
			
			responseModel.setWorkShopWIPQty(branchPartMasterEntity.getWorkShopWIPQty());
			responseModel.setDeliveryChallanReserveQty(branchPartMasterEntity.getDeliveryChallanReserveQty());
			if (null != responseModel.getAdminPartMasterModel().getMrpChangedAllowedFlag()
					&& responseModel.getAdminPartMasterModel().getMrpChangedAllowedFlag()) {
				responseModel.setCssEnabledMrpChangedAllowed(true);
			} else {
				responseModel.setCssEnabledMrpChangedAllowed(false);
			}
			
			setStockBinDetailGrid(responseModel);
			if (null != responseModel.getAdminPartMasterModel().getOemPartIndicator()) {
				responseModel.getAdminPartMasterModel().setOemPartIndicator(
						responseModel.getAdminPartMasterModel().getOemPartIndicator().toUpperCase());
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
		return responseModel;
	}
	
	
	private Integer getBranchIdByUSerCode(String userCode, Session session) {
		
		Integer branchId=0;
		try
		{
			  Query query = session.createSQLQuery(
				        "select * from  FN_CM_GETBRANCH_BYUSERCODE(:userCode, 'N') as branchId"
				    ).setParameter("userCode", userCode);
				    
				    List<Object[]> data = query.list();
				    if (data != null && !data.isEmpty()) {
				        for (Object[] row : data) {
				           BigInteger branchId1 = (BigInteger) row[0];
				           branchId=branchId1.intValue();
				            System.out.println("Branch Id: " + branchId);
				        }
				    }
			
		}catch(Exception e)
		{
			
			e.printStackTrace();
		}
		
		return branchId;
	}

	public Map<String, BigDecimal> getPartBranchStockKeyValues(Integer branchId, String partNo, Integer partId,
			Integer partBranchId, String requiredValueFlag, Session session) {
		Map<String, BigDecimal> partBranchStockKeyValues = new HashMap<String, BigDecimal>();
		Boolean needToCloseSession = false;
		try {
			if (session == null) {
				session = sessionFactory.openSession();
				needToCloseSession = true;
			}
			Map<String, BigDecimal> resultMap = 
			(Map<String, BigDecimal>) session.createSQLQuery(
							"select * from FN_GetPartStockDetails(" + partBranchId + ",'" + requiredValueFlag + "') ")
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).uniqueResult();
			if (false) {//resultMap != null && !resultMap.isEmpty()
				partBranchStockKeyValues.put("wipQty", resultMap.get("wipQty"));
				partBranchStockKeyValues.put("stockAmount", resultMap.get("stockAmount"));
				partBranchStockKeyValues.put("backOrderQty", resultMap.get("backOrderQty"));
				partBranchStockKeyValues.put("onHandQty", resultMap.get("onHandQty"));
				partBranchStockKeyValues.put("inTransitQty", resultMap.get("inTransitQty"));
			} else {
				partBranchStockKeyValues.put("wipQty", BigDecimal.TEN);
				partBranchStockKeyValues.put("stockAmount", BigDecimal.TEN);
				partBranchStockKeyValues.put("backOrderQty", BigDecimal.TEN);
				partBranchStockKeyValues.put("onHandQty", BigDecimal.TEN);
				partBranchStockKeyValues.put("inTransitQty", BigDecimal.TEN);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return partBranchStockKeyValues;
	}
	
	
	public List<PartStockBinModel> getPartStockBinDetails(Integer branchId, Integer partBranchId) {
		Session session = null;
		Query query = null;
		System.out.println("stockBin List fetched ");
		List<PartStockBinModel> partStockBinModelList = new ArrayList<PartStockBinModel>();
		List data = null;
		Iterator iterator = null;
		String sqlQuery = "SELECT SB.stock_bin_id,SB.BinName,SB.IsDefault,SB.IsActive,SB.stock_store_id"
				+ ",BS.branch_store_id,BS.StoreDesc,SB.currentStock FROM  PA_STOCK_BIN SB LEFT JOIN PA_STOCK_STORE SS ON SS.stock_store_id=SB.stock_store_id AND SB.branch_id=SB.branch_id AND SB.partBranch_id=SB.partBranch_id LEFT JOIN PA_BRANCH_STORE BS ON BS.branch_store_id=SS.branch_store_id and BS.branch_id=SS.branch_id WHERE SB.partBranch_id=:partBranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partBranchId", partBranchId);
			data = query.list();
			// ram updated code
			// data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					PartStockBinModel binModel = new PartStockBinModel();
					Map row = (Map) object;
					binModel.setStockBinId((BigInteger) row.get("stock_bin_id"));
					binModel.setBinName((String) row.get("BinName"));
					char isDefault=(char)row.get("IsDefault");
					char isActive=(char)row.get("IsActive");

					if (isDefault == 'Y') {
						binModel.setDeafultStatus(true);
					} else {
						binModel.setDeafultStatus(false);
					}
					if (isActive == 'Y') {
						binModel.setActiveStatus(true);
					} else {
						binModel.setActiveStatus(false);
					}
					binModel.setBranchStoreId((Integer) row.get("branch_store_id"));
					binModel.setBranchStoreName((String) row.get("StoreDesc"));
					//binModel.setStockStoreId((BigInteger) objects[4]);
					binModel.setCurrentStock((BigDecimal) row.get("currentStock"));
					partStockBinModelList.add(binModel);
				
			}
			}
			// end of new code 
//
//			if (DmsCollectionUtils.isNotEmpty(list)) {
//				iterator = list.iterator();
//				while (iterator.hasNext()) {
//					Object[] objects = (Object[]) iterator.next();
//					PartStockBinModel binModel = new PartStockBinModel();
//					binModel.setBranchStoreId((Integer) objects[5]);
//					binModel.setBranchStoreName((String) objects[6]);
//					binModel.setStockBinId((BigInteger) objects[0]);
//					System.out.println("get stock from object "+objects[7]);
//					binModel.setBinName((String) objects[1]);
//					if ((char) objects[2] == 'Y') {
//						binModel.setDeafultStatus(true);
//					} else {
//						binModel.setDeafultStatus(false);
//					}
//					if ((char) objects[3] == 'Y') {
//						binModel.setActiveStatus(true);
//					} else {
//						binModel.setActiveStatus(false);
//					}
//					binModel.setStockStoreId((BigInteger) objects[4]);
//					binModel.setCurrentStock((BigDecimal) objects[7]);
//					partStockBinModelList.add(binModel);
//				}
//
//			}

		} catch (Exception e) {
			e.printStackTrace();;
		} 
	   System.out.println("stockBinList "+partStockBinModelList);
		return partStockBinModelList;
	}
	@SuppressWarnings("rawtypes")
	public List<PartControlModel> fetchPartControlList(Session session, String controlNo) {
		List<PartControlModel> partControlList = null;
		PartControlModel partControlModel = null;
		Query query = null;
		String sqlQuery = "EXEC SP_GETHSN_DETAILS :controlNo";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("controlNo", controlNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partControlList = new ArrayList<PartControlModel>();
				for (Object object : data) {
					partControlModel = new PartControlModel();
					Map row = (Map) object;
					partControlModel.setControlCodeId((Integer) row.get("Control_code_id"));
					partControlModel.setControlCode((String) row.get("Control_code"));
					partControlModel.setTaxTypeId((Integer) row.get("taxcharge_oem_id"));
					partControlModel.setTaxRate((BigDecimal) row.get("Tax_Rate"));
					partControlModel.setTaxTypeDesc((String) row.get("TaxChargeCode"));
					partControlList.add(partControlModel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return partControlList;
	}
	
	private void setStockBinDetailGrid(PartMasterFormRequestModel branchPartMasterModel) {
		List<PartStockBinModel> partStockBinModelList = branchPartMasterModel.getPartStockBinModelList();
		if (DmsCollectionUtils.isNotEmpty(partStockBinModelList)) {
			for (PartStockBinModel binModel : partStockBinModelList) {
				binModel.setCheckStockBinExistsInDBFlag(true);
			}
		}
	}

	@Override
	public PartyMappingResponseModel fetchPartNumberDetailsnew(String userCode, String partNumber) {
		
		System.out.println("In new Method "+userCode);
		Session session = null;
		Query query = null;
		Map<String,Object> mapData = new HashMap<>();
		PartTableRequestModel partrequestResponse;
		PartBranchRequestModel partBranchResponse;
		List<PartBranchStoreResponseModel> branchStoreList;
		List<PartStockBinRequestModel> branchStockList;
		
		try
		{
			
			session = sessionFactory.openSession();
			String userId = null;
			String sqlQuery=null;
			boolean isSuccess=false;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			System.out.println("mapDatana "+mapData);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId=Id.toString();
			}
			
			sqlQuery = "select  * from  PA_PART where PartNumber='"+partNumber+"'";
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.getResultList();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					partrequestResponse= new PartTableRequestModel();
					partrequestResponse.setPartNumber((String)row.get("PartNumber"));
					partrequestResponse.setPartDesc((String)row.get("PartDesc"));
					partrequestResponse.setUomId((Integer)row.get("uomId"));
					partrequestResponse.setAltPartNumber((String)row.get("AltPartNumber"));
					partrequestResponse.setIssueIndiacatorId((Integer)row.get("issueindicator_id"));
					partrequestResponse.setPartCategoryId((Integer)row.get("partcategory_id"));
					partrequestResponse.setAllowDecimalQty((String)row.get("AllowDecimalInQty"));
					partrequestResponse.setMinOrderQty((Integer)row.get("MinOrderQty"));
					partrequestResponse.setAggregateId((Integer)row.get("AGGREGATE_ID"));
					partrequestResponse.setModelGroupId((Integer)row.get("Model_Group_Id"));
					partrequestResponse.setModelPlatformId((Integer)row.get("MODEL_PLATFORM_ID"));
					partrequestResponse.setPerVehicleQuantity((Integer)row.get("PerVehicleQty"));
					partrequestResponse.setPackQty((Integer)row.get("PackQty"));
					partrequestResponse.setOrderToOem((String)row.get("OrderToOEM"));
					partrequestResponse.setModelVariantId((Integer)row.get("Model_Variant_Id"));
					partrequestResponse.setIsPartWarranty((String)row.get("[sPartUnderWarranty"));
					partrequestResponse.setIsPartMfgNo((String)row.get("Is_MFG_PartNo"));
					partrequestResponse.setHssnCode((String)row.get("HSN_CODE"));
					partrequestResponse.setIGst((BigInteger)row.get("[IGST"));
					
					partrequestResponse.setCGst((BigInteger)row.get("[CGST"));

					partrequestResponse.setSGst((BigInteger)row.get("[SGST"));

					

					
					//int rowCount = query.executeUpdate();
					
				
					isSuccess=true;
					System.out.println("before return stock Store Detail "+partrequestResponse);
					

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
		
		return null;
	}
}
