package com.hitech.dms.web.dao.registrationcertificate.create;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
import com.hitech.dms.web.entity.pdi.PdiDetailsEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachineVinMstEntity;
import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;
import com.hitech.dms.web.model.registrationcertificate.create.response.RegistrationCreateResponseModel;

@Repository
public class RegistrationCertificateDaoImpl implements RegistrationCertificateDao {

private static final Logger logger = LoggerFactory.getLogger(RegistrationCertificateDaoImpl.class);
	
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
	private final String uploadDir = "C:\\VST-DMS-APPS\\FILES\\RCUPDATE\\";
	private final String uploadDirForProd = "/var/VST-DMS-APPS/FILES/RCUPDATE/";
	
	
	@SuppressWarnings("deprecation")
	public RegistrationCreateResponseModel updateRegistration( String userCode,
			SalesMachineVinMstEntity requestModel,List<MultipartFile> files) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateRegistration invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		RegistrationCreateResponseModel responseModel = new RegistrationCreateResponseModel();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			logger.debug(requestModel.toString());
			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
							requestModel.setCreatedBy(userId);
							requestModel.setCreatedDate(currDate);
						
							if(requestModel.getVinId() !=null) {
								
								String procedureQuery = "select * from SA_MACHINE_VIN_MASTER where registration_number =:registrationNo and vin_id !=:vinId ";
								query = session.createSQLQuery(procedureQuery);
								query.setParameter("registrationNo",requestModel.getRegistrationNumber());
								query.setParameter("vinId", requestModel.getVinId());
								query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
								List data = query.list();
								int updateRegistrationCertificate=0;
								if(data.size()>0) {
									responseModel.setMsg("This Registration No. is already Available");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									updateRegistrationCertificate = updateRegistrationCertificate(session, requestModel,files);
								}
								
								if(updateRegistrationCertificate>0) {
									transaction.commit();;
									responseModel.setVinId(requestModel.getVinId());
									responseModel.setChassisNumber(requestModel.getChassisNo());
									responseModel.setMsg("Registration No. updated Successfully.");
									responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
								}else {
									responseModel.setMsg("some issue while updating.");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
								}
								
							}else {
								responseModel.setMsg("VinId not found.");
								responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
							}
						} 
			 else {
				// User not found
				responseModel.setMsg("User Not Found.");
			}
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} finally {
				
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
	
	public int updateRegistrationCertificate(Session session, SalesMachineVinMstEntity requestModel,List<MultipartFile> files) {
		String sqlQuery = "update SA_MACHINE_VIN_MASTER set registration_number=:registrationNo,last_modified_date=:last_modified_date,last_modified_by=:last_modified_by,fileName=:filename where vin_id =:vinId";
		int id = 0;
		Query query = null;
		String fileName="";
		for (MultipartFile file : files) {
		    // Process each file
		     fileName = file.getOriginalFilename();
		    // Additional processing logic
		}
		
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("registrationNo", requestModel.getRegistrationNumber());
			query.setParameter("vinId", requestModel.getVinId());
			query.setParameter("last_modified_date", requestModel.getCreatedDate());
			query.setParameter("last_modified_by", requestModel.getCreatedBy());
			query.setParameter("filename", fileName);
			
			 id=query.executeUpdate();
			 if(id>0) {
				 uploadFile(requestModel.getVinId(),files);
			 }
			 
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return id;
	}
	
	public void uploadFile(BigInteger vinId,List<MultipartFile> files) {
		
		 
			 if (files != null && !files.isEmpty()) {
					try {
						files.forEach(m -> {
//							String filePath = fileStorageProperties.getUploadDirMain()
//									+ messageSource.getMessage("vin.file.upload.dir",
//											new Object[] { String.format("%d",id) }, LocaleContextHolder.getLocale());
////							Map<String, String> fileMapData  = FileUtils.uploadDOCDTLDOCS(filePath, m); String.format("%04d", i + 1)
//							logger.info("filePath "+filePath);
//							
//							FileUtils.uploadDOCDTLDOCS(filePath, m);
							
							
							try {
								String rcIdFolder = "";
								if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
									rcIdFolder = uploadDir + vinId + File.separator;
								} else {
									rcIdFolder = uploadDirForProd + vinId + File.separator;
								}
								// Create the roId folder if it doesn't exist
								File rcIdDir = new File(rcIdFolder);
								if (!rcIdDir.exists()) {
									rcIdDir.mkdirs();
								}
								
								String strPath=rcIdFolder + m.getOriginalFilename();
								System.out.println("strPath "+strPath);
								Path filePathObj = Paths.get(strPath);
								Files.copy(m.getInputStream(), filePathObj);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						});
					} catch (Exception e) {
						logger.error(this.getClass().getName(), e);
						//fileFlag = true;
					}

				}
			 
		
	}
}
