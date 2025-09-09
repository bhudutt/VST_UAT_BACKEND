package com.hitech.dms.web.dao.installation.create;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.dao.pdi.create.PdiCreateDaoImpl;
import com.hitech.dms.web.entity.installation.InstallationDetailsEntity;
import com.hitech.dms.web.entity.installation.InstallationEntity;
import com.hitech.dms.web.entity.pdi.PdiDetailsEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.model.Installation.create.response.InstallationCreateResponseModel;
import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;

@Repository
public class InstallationCreateDaoImpl implements InstallationCreateDao {

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
	public InstallationCreateResponseModel createInstallation( String userCode,
			InstallationEntity requestModel,List<MultipartFile> files) {
		if (logger.isDebugEnabled()) {
			logger.debug("createInstallation invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		InstallationCreateResponseModel responseModel = new InstallationCreateResponseModel();
		//PdiEntity pdiHDREntity = null;
		Integer installationId=0;
		boolean isSuccess = true;
		BigInteger saveId = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//pdiHDREntity = mapper.map(requestModel, PdiEntity.class, "PdiMapId");

			logger.debug(requestModel.toString());

			List<InstallationDetailsEntity> installationDtlList = requestModel.getInstallationDetailList();

			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
							
							// insert into PDI Table
							if (requestModel.getInstallationDate() == null) {
								requestModel.setInstallationDate(currDate);
							}
							requestModel.setCreatedBy(userId);
							requestModel.setCreatedDate(currDate);
							
							String installationNo= "INSTN/" + ThreadLocalRandom.current().nextInt(1000) + "/" + System.currentTimeMillis();
							requestModel.setInstallationNo(installationNo);
							
							if(files != null && !files.isEmpty()) {
								System.out.println(" file name ");
								files.forEach(m->{
									String fileNameVal=m.getOriginalFilename();
									System.out.println(" file name "+fileNameVal);
									requestModel.setFileupload(fileNameVal);
								}
								);
								
								
							}
							
							 installationId = (Integer) session.save(requestModel);
							 Integer mainId=installationId;
							//insert into PDI Details
							for (InstallationDetailsEntity installationDetailsEntity2 : installationDtlList) {
								installationDetailsEntity2.setInstallationId(installationId);
								
								if (installationDetailsEntity2.getCreatedDate() == null) {
									installationDetailsEntity2.setCreatedDate(currDate);
								}
								installationDetailsEntity2.setCreatedBy(userId);
								installationDetailsEntity2.setCreatedDate(currDate);
								session.save(installationDetailsEntity2);
							}
							
							if(installationId !=null && installationId>0) {
								updateInstallationDate(session, requestModel.getChassisNo());
							}

							if (files != null && !files.isEmpty()) {
								try {
									files.forEach(m -> {
										String filePath = fileStorageProperties.getUploadDirMain()
												+ messageSource.getMessage("installation.file.upload.dir",
														new Object[] { String.format("%d",mainId) }, LocaleContextHolder.getLocale());
									//	Map<String, String> fileMapData  = FileUtils.uploadDOCDTLDOCS(filePath, m); String.format("%04d", i + 1)
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
				mapData = fetchInstallationNoById(session, installationId);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					
					responseModel.setInstallationId(installationId);
					responseModel.setInstallationNumber((String) mapData.get("installationNumber"));
					responseModel.setMsg("Installation Number Created Successfully.");
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
	
	public void updateInstallationDate(Session session, String chassisNo) {
		String sqlQuery = "update SA_MACHINE_VIN_MASTER set installation_date=:date where chassis_no =:chassisNo";
		Date currDate = new Date();
		Query query = null;
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("date", currDate);
			query.setParameter("chassisNo", chassisNo);
			int id=query.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchInstallationNoById(Session session, Integer installationHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select InstallationNumber from SV_INSTALLATION_HDR (nolock) ins where ins.id =:insHdrId";
		mapData.put("ERROR", "Installation Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("insHdrId", installationHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String pdi_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					pdi_number = (String) row.get("InstallationNumber");
				}
				mapData.put("InstallationNumber", pdi_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Installation DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Installation DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
