package com.hitech.dms.web.dao.pdi.create;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.FileUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.grn.create.SalesGrnCreateDao;
import com.hitech.dms.web.dao.grn.create.SalesGrnCreateDaoImpl;
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.pdi.OutwardPdiDetailsEntity;
import com.hitech.dms.web.entity.pdi.OutwardPdiEntity;
import com.hitech.dms.web.entity.pdi.PdiDetailsEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineGrnDtlEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineGrnHDREntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineGrnImplDtlEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvLedgerPEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvPEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachineVinMstEntity;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.grn.create.request.SalesGrnCreateRequestModel;
import com.hitech.dms.web.model.grn.create.response.SalesGrnCreateResponseModel;
import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;
import com.hitech.dms.web.model.pdi.create.request.PdiCreateRequestModel;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;

@Repository
public class PdiCreateDaoImpl implements PdiCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(PdiCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private MachinePOCommonDao machinePOCommonDao;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	
	
	@SuppressWarnings("deprecation")
	public PdiCreateResponseModel createPdi( String userCode,
			PdiEntity requestModel,List<MultipartFile> files) {
		if (logger.isDebugEnabled()) {
			logger.debug("createPDI invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PdiCreateResponseModel responseModel = new PdiCreateResponseModel();
		//PdiEntity pdiHDREntity = null;
		GrnTypeResponseModel grnTypeModel = null;
		boolean isSuccess = true;
		BigInteger saveId = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//pdiHDREntity = mapper.map(requestModel, PdiEntity.class, "PdiMapId");

			logger.debug(requestModel.toString());

			List<PdiDetailsEntity> pdiDtlList = requestModel.getPdiDeliveryDetailList();

			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
							
							// insert into PDI Table
							if (requestModel.getPdiDate() == null) {
								requestModel.setPdiDate(currDate);
							}
							requestModel.setCreatedBy(userId);
							requestModel.setCreatedDate(currDate);
							
							String pdiNo= "PDI/" + ThreadLocalRandom.current().nextInt(1000) + "/" + System.currentTimeMillis();
							requestModel.setPdiNo(pdiNo);
							
							Map<String, Object> dealerid=fetchDealerIdBranchId(session, requestModel.getInvoiceNumber(),requestModel.getChassisNumber(),"inward");
							String branchId=(String) dealerid.get("branch_id");
							String dealer_Id=(String) dealerid.get("dealer_Id");
							
							if(files != null && !files.isEmpty()) {
								System.out.println(" file name ");
								files.forEach(m->{
									String fileNameVal=m.getOriginalFilename();
									System.out.println(" file name "+fileNameVal);
									requestModel.setFileupload(fileNameVal);
								}
								);
								
								
							}
							
							Integer pId = (Integer) session.save(requestModel);
							//insert into PDI Details
							for (PdiDetailsEntity pdiDetailsEntity2 : pdiDtlList) {
								pdiDetailsEntity2.setPdiEntity(pId);
								if(branchId !=null) {
									pdiDetailsEntity2.setBranchId(Integer.parseInt(branchId));
								}
								if(dealer_Id !=null) {
									pdiDetailsEntity2.setDealerId(Integer.parseInt(dealer_Id));
								}
								
								
								session.save(pdiDetailsEntity2);
							}
							
							if(pId !=null && pId>0) {
								updatePdiDone(session, requestModel.getInvoiceNumber(),requestModel.getChassisNumber());
							}
							

							if (files != null && !files.isEmpty()) {
								
									
								
								try {
									files.forEach(m -> {
										String filePath = fileStorageProperties.getUploadDirMain()
												+ messageSource.getMessage("pdi.file.upload.dir",
														new Object[] { String.format("%d",pId) }, LocaleContextHolder.getLocale());
//										Map<String, String> fileMapData  = FileUtils.uploadDOCDTLDOCS(filePath, m); String.format("%04d", i + 1)
										logger.info("filePath "+filePath);
										
										FileUtils.uploadDOCDTLDOCS(filePath, m);
									});
								} catch (Exception e) {
									logger.error(this.getClass().getName(), e);
									//fileFlag = true;
								}

							}
						
					} 
				
			 else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();;
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
		} finally {
			if (isSuccess) {
				// insert / update in detail tables based on grn Type
				mapData = fetchPdiNoByPdiId(session, requestModel.getId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPdiId(requestModel.getId());
					responseModel.setPdiNumber((String) mapData.get("pdiNumber"));
					responseModel.setMsg("PDI Number Created Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
	
	@SuppressWarnings("deprecation")
	public PdiCreateResponseModel createOutwardPdi( String userCode,
			OutwardPdiEntity requestModel,List<MultipartFile> files) {
		if (logger.isDebugEnabled()) {
			logger.debug("createPDI invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PdiCreateResponseModel responseModel = new PdiCreateResponseModel();
		//PdiEntity pdiHDREntity = null;
		GrnTypeResponseModel grnTypeModel = null;
		boolean isSuccess = true;
		BigInteger saveId = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//pdiHDREntity = mapper.map(requestModel, PdiEntity.class, "PdiMapId");

			logger.debug(requestModel.toString());

			List<OutwardPdiDetailsEntity> pdiDtlList = requestModel.getPdiDeliveryDetailList();

			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
							
							// insert into PDI Table
							if (requestModel.getPdiDate() == null) {
								requestModel.setPdiDate(currDate);
							}
							requestModel.setCreatedBy(userId);
							requestModel.setCreatedDate(currDate);
							
							String pdiNo= "OPDI/" + ThreadLocalRandom.current().nextInt(1000) + "/" + System.currentTimeMillis();
							requestModel.setOutwardPdiNo(pdiNo);
							
							Map<String, Object> dealerid=fetchDealerIdBranchId(session, requestModel.getInvoiceNumber(),requestModel.getChassisNumber(),"outward");
							String branchId=(String) dealerid.get("branch_id");
							String dealer_Id=(String) dealerid.get("dealer_Id");
							
							
							
							if(files != null && !files.isEmpty()) {
								System.out.println(" file name ");
								files.forEach(m->{
									String fileNameVal=m.getOriginalFilename();
									System.out.println(" file name "+fileNameVal);
									requestModel.setFileupload(fileNameVal);
								}
								);
								
								
							}
							
							Integer pId = (Integer) session.save(requestModel);
							//insert into PDI Details
							for (OutwardPdiDetailsEntity pdiDetailsEntity2 : pdiDtlList) {
								pdiDetailsEntity2.setPdiEntity(pId);
								if(branchId !=null) {
									pdiDetailsEntity2.setBranchId(Integer.parseInt(branchId));
								}
								if(dealer_Id !=null) {
									pdiDetailsEntity2.setDealerId(Integer.parseInt(dealer_Id));
								}
								
								
								session.save(pdiDetailsEntity2);
								updateBatteryNumber(session,requestModel.getBatteryMakeNumber(),requestModel.getChassisNumber());
							}
							
							if(pId !=null && pId>0) {
								//updatePdiDone(session, requestModel.getInvoiceNumber());
							}
							

							if (files != null && !files.isEmpty()) {
								try {
									files.forEach(m -> {
										String filePath = fileStorageProperties.getUploadDirMain()
												+ messageSource.getMessage("outwardpdi.file.upload.dir",
														new Object[] { String.format("%d",pId) }, LocaleContextHolder.getLocale());
//										Map<String, String> fileMapData  = FileUtils.uploadDOCDTLDOCS(filePath, m);
										FileUtils.uploadDOCDTLDOCS(filePath, m);
									});
								} catch (Exception e) {
									logger.error(this.getClass().getName(), e);
									//fileFlag = true;
								}

							}
						
					} 
				
			 else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();;
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
		} finally {
			if (isSuccess) {
				// insert / update in detail tables based on grn Type
				mapData = fetchOutwardPdiNoByPdiId(session, requestModel.getId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPdiId(requestModel.getId());
					responseModel.setPdiNumber((String) mapData.get("outwardpdiNumber"));
					responseModel.setMsg("Outward PDI Number Created Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	public void updatePdiDone(Session session, String invoiceNo,String chassisNo) {
		String sqlQuery = "update SA_MACHINE_ERP_INVOICE_HDR set pdi_done_flag=1 where invoice_number =:invoiceNo";
		
		String sqlQuery1 = "update Sa_machine_erp_invoice_dtl set pdi_done_flag=1 where chassis_no =:chassisNo";
		Query query = null;
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("invoiceNo", invoiceNo);
			int id=query.executeUpdate();
			
			query = session.createNativeQuery(sqlQuery1);
			query.setParameter("chassisNo", chassisNo);
			int id1=query.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void updateBatteryNumber(Session session, String batteryNumber,String chassisNo) {
		String sqlQuery = " update SV_INWARD_PDI_HDR set BatteryMakeNumber=:batteryNumber where ChassisNumber=:chassisNumber";
		
		Query query = null;
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("batteryNumber", batteryNumber);
			query.setParameter("chassisNumber", chassisNo);
			int id=query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchOutwardPdiNoByPdiId(Session session, Integer pdiHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select OutwardNumber from SV_OUTWARD_PDI_HDR (nolock) pdi where pdi.id =:pdiHdrId";
		mapData.put("ERROR", "PDI Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("pdiHdrId", pdiHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String pdi_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					pdi_number = (String) row.get("OutwardNumber");
				}
				mapData.put("outwardpdiNumber", pdi_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING PDI DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING PDI DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchPdiNoByPdiId(Session session, Integer pdiHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select InwardNumber from SV_INWARD_PDI_HDR (nolock) pdi where pdi.id =:pdiHdrId";
		mapData.put("ERROR", "PDI Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("pdiHdrId", pdiHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String pdi_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					pdi_number = (String) row.get("InwardNumber");
				}
				mapData.put("pdiNumber", pdi_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING PDI DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING PDI DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchDealerIdBranchId(Session session, String invoiceNo,String chassisNo,String type) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		
		
		
		
		/*
		 * String sqlQuery =
		 * "select Inv.erp_dealer_id DealerId, DBR.branch_id BranchId " +
		 * " from SA_MACHINE_ERP_INVOICE_HDR (nolock) inv " +
		 * " inner join ADM_BP_DEALER_BRANCH(nolock) DBR on INV.erp_dealer_id=DBR.parent_dealer_id"
		 * + " where inv.invoice_number =:invoiceNo";
		 */
		//change as required by vikash for jobcard
		
		
		mapData.put("ERROR", "PDI Details Not Found");
		try {
			
			if(type.equalsIgnoreCase("outward")) {
				System.out.println("invoiceNo  "+invoiceNo);
				String sqlQuery = "select Inv.dealer_id DealerId, DBR.branch_id BranchId   "
						+ " from sa_machine_grn (nolock) inv  "
						+ " inner join ADM_BP_DEALER_BRANCH(nolock) DBR on INV.dealer_id=DBR.parent_dealer_id  "
						+ " where inv.InvoiceNo =:invoiceNo ";
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("invoiceNo", invoiceNo);
				
			}else {
				
				  String sqlQuery = "select Inv.erp_dealer_id DealerId, DBR.branch_id BranchId " +
				  " from SA_MACHINE_ERP_INVOICE_HDR (nolock) inv " +
				  " inner join ADM_BP_DEALER_BRANCH(nolock) DBR on INV.erp_dealer_id=DBR.parent_dealer_id"+
				   " where inv.invoice_number =:invoiceNo";
				  query = session.createNativeQuery(sqlQuery);
					query.setParameter("invoiceNo", invoiceNo);
			}
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String dealer_Id = null;
				String branch_id = null;
				for (Object object : data) {
					Map row = (Map) object;
					dealer_Id = (String) row.get("DealerId").toString();
					branch_id = (String) row.get("BranchId").toString();
				}
				mapData.put("dealer_Id", dealer_Id);
				mapData.put("branch_id", branch_id);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING PDI DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING PDI DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
