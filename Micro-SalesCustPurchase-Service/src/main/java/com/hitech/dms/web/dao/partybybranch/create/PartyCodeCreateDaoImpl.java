package com.hitech.dms.web.dao.partybybranch.create;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.allotment.create.MachineAllotCreateDaoImpl;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.invoice.ServicePartyApprovalEntity;
import com.hitech.dms.web.entity.partycode.PartyCodeEntity;
import com.hitech.dms.web.model.partybybranch.create.request.PartyApprovalEntity;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeRequestModel;
import com.hitech.dms.web.model.partybybranch.create.response.PartyCodeCreateResponseModel;

@Repository
public class PartyCodeCreateDaoImpl implements PartyCodeCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(MachineAllotCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	
	@Autowired
	private CommonDao commonDao;

	@Value("${file.upload-dir.PARTYMASTER}")
	private String templateDownloadPath;
	
	private String uploadDir="C:\\Users\\rambabu.kumar\\Documents\\thrsl\\frontend\\projects\\dms-administration\\src\\assets\\img";

	String documentName = null;
	String partyCode = null;


	@Override
	public PartyCodeCreateResponseModel create(String authorizationHeader, String userCode,
			PartyCodeCreateRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("createStore invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		LocalDate currentDate = LocalDate.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMM");

		String formattedDate = currentDate.format(formatter);

		   
		   String filePath="";
			String property = System.getProperty("os.name");
			if(property.contains("Windows")) {
				filePath=uploadDir;
			}else {
				filePath="/var/VST-DMS-APPS/FILES/Template/Stock Template/";
			}
				
		PartyCodeRequestModel beanModel = new PartyCodeRequestModel();
		List<ServicePartyApprovalEntity> partyApprovalEnity= new ArrayList<>();

		String document1Name = null;
		String document2Name = null;
		String document3Name = null;

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		String status = null;
		PartyCodeCreateResponseModel responseModel = new PartyCodeCreateResponseModel();
		MultipartFile document1=requestModel.getDocument1();
		MultipartFile document2=requestModel.getDocument2();
		MultipartFile document3=requestModel.getDocument3();
		
		System.out.println();

		
		beanModel.setBranchId(requestModel.getBranchId());
		beanModel.setPartyCategoryId(requestModel.getPartyCategoryId());
		beanModel.setPartyCode(requestModel.getPartyCode());
		beanModel.setTobranchId(requestModel.getTobranchId());
		beanModel.setFinancierID(requestModel.getFinancierID());
		beanModel.setPartyTitle(requestModel.getPartyTitle());
		beanModel.setPartyName(requestModel.getPartyName());
		beanModel.setPartyLocation(requestModel.getPartyLocation());
		beanModel.setContactTitle(requestModel.getContactTitle());
		beanModel.setPartyStatus(requestModel.getPartyStatus());
		beanModel.setDsr(requestModel.getDsr());
		System.out.println("dealerCode "+requestModel.getDealerCode());
		beanModel.setDealerCode(requestModel.getDealerCode());
		//beanModel.setOutletCategoryId(requestModel.getOutletCategory());
		beanModel.setFirstName(requestModel.getFirstName());
		beanModel.setMiddleName(requestModel.getMiddleName());
		beanModel.setLastName(requestModel.getLastName());
		beanModel.setEmail1(requestModel.getEmail1());
		beanModel.setPartyName(requestModel.getPartyName());
		beanModel.setMobileNumber(requestModel.getMobileNumber());
		beanModel.setDesignation(requestModel.getDesignation());
		beanModel.setPartyAddLine1(requestModel.getPartyAddLine1());
		beanModel.setPartyAddLine2(requestModel.getPartyAddLine2());
		beanModel.setPartyAddLine3(requestModel.getPartyAddLine3());
		beanModel.setPinId(requestModel.getPinId());
		beanModel.setTelephone(requestModel.getTelephone());
		beanModel.setFax(requestModel.getFax());
		beanModel.setEmail2(requestModel.getEmail2());
		beanModel.setCst(requestModel.getCst());
		beanModel.setLst(requestModel.getLst());
		beanModel.setPanOrTan(requestModel.getPanOrTan());
		beanModel.setGstNumber(requestModel.getGstNumber());
		beanModel.setAadharNumber(requestModel.getAadharNumber());
		beanModel.setPanNo(requestModel.getPanNo());
		beanModel.setPartsDiscountPercentage(requestModel.getPartsDiscountPercentage());
		beanModel.setLabourDiscountPercentage(requestModel.getLabourDiscountPercentage());
		beanModel.setCreditAmt(requestModel.getCreditAmt());
		beanModel.setRemarks(requestModel.getRemarks());
		beanModel.setIsOEMParty(requestModel.getIsOEMParty());
		beanModel.setIsActive(true);
		beanModel.setCreatedBy(requestModel.getCreatedBy());
		beanModel.setCreatedDate(requestModel.getCreatedDate());
		beanModel.setModifiedBy(requestModel.getModifiedBy());
		beanModel.setModifiedDate(requestModel.getModifiedDate());
		beanModel.setDocument1(document1Name);
		beanModel.setDocument2(document2Name);
		beanModel.setDocument3(document3Name);

		beanModel.setPage(requestModel.getPage());

		beanModel.setSize(requestModel.getSize());
		System.out.println("dealerCode "+beanModel.getDealerCode());

		PartyCodeEntity entity = null;
		boolean isSuccess = true;
		String msgval = "";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Date todayDate = new Date();
			String userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId = Id.toString();
			}
			entity = mapper.map(beanModel, PartyCodeEntity.class);
			
			BigInteger id = BigInteger.ZERO;
			String newlyGeneratedCode=null;


			
				
				String partyCodeGenerateQuery = "select PartyCode from Generate_PartyCodeNo( :catCode, :branchId)";
				Query query1 = session.createSQLQuery(partyCodeGenerateQuery);
				query1.setParameter("catCode", entity.getPartyCode());
				query1.setParameter("branchId", entity.getBranchId());
				query1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List generatePartyCode = query1.list();
				if (!generatePartyCode.isEmpty()) {
					for (Object object : generatePartyCode) {
						Map row = (Map) object;
						partyCode = (String) row.get("PartyCode");
						System.out.println("Original PartyCode we get as "+partyCode);
						if(entity.getPartyCategoryId().intValue()==4||entity.getPartyCategoryId().intValue()==5|| entity.getPartyCategoryId().intValue()==6)
						{
							String newPartyCode=null;
							
							if(partyCode.startsWith("MECH"))
							{
								newPartyCode =generatePartyCodeAuto("VSTM","Party Detail VST mechanic", entity.getBranchId());
								System.out.println("newPartyCode for VSTM "+newPartyCode);

							}
							else if(partyCode.startsWith("PLUM"))
							{
								newPartyCode =generatePartyCodeAuto("VSTP","Party Detail VST Plumber",entity.getBranchId());
								System.out.println("newPartyCode for VSTP "+newPartyCode);
							}
							else if(partyCode.startsWith("RET"))
							{
								newPartyCode =generatePartyCodeAuto("AR","Party Detail Authorized",entity.getBranchId());
								System.out.println("newPartyCode for AR "+newPartyCode);
								
								
							}

						     partyCode=newPartyCode;
						}					
						}
				}
			
			
			
			System.out.println("generate partyCode after else  "+partyCode);

			int  insertStatus=saveOutletCategoryDetail(requestModel.getOutletCategory(),requestModel.getPartyBranchId());
			System.out.println("after insert status "+insertStatus);
			if(insertStatus==1)
			{
				msgval="Outlet CategoryDetail Saved";
			}
			entity.setPartyCode(partyCode);
			entity.setDealerCode(requestModel.getDealerCode());

			if (entity.getPartyBranchId() != null) {
				entity.setModifiedBy(userCode);
				entity.setModifiedDate(todayDate);
				session.update(entity);
				msgval = "Updated Successfully.";
				
			} else {
				entity.setCreatedBy(userCode);
				entity.setCreatedDate(todayDate);
				id = (BigInteger) session.save(entity);
				 // save images 
				if (document1!=null) {
					status = saveDocumentAtLocation(requestModel.getDocument1(),id);
					document1Name = documentName;

				}
				if (document2!=null) {
					status = saveDocumentAtLocation(requestModel.getDocument2(),id);
					document2Name = documentName;


				}
				if (document3!=null) {
					status = saveDocumentAtLocation(requestModel.getDocument3(),id);
					document3Name = documentName;


				}
				
				// 
		
				
				
				List<ServicePartyApprovalEntity> partyApprovalEnity1 = fetchApprovalEntities(session,requestModel);
				if(!partyApprovalEnity1.isEmpty())
				{
					for(ServicePartyApprovalEntity approvalEntity:partyApprovalEnity1)
					{
						
						approvalEntity.setParty_id(id);
						session.save(approvalEntity);
					}
				}
				
				
				if (!BigInteger.ZERO.equals(id)) {
					isSuccess = true;
					msgval = "Party Created Successfully. " + partyCode;
				} else {
					isSuccess = false;
					msgval = "Some Issue";
				}
			}

			if (isSuccess) {
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
		} finally {
			if (isSuccess) {
				responseModel.setMsg(msgval);
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setMsg("Some Issue While Creating Party.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		

		return responseModel;
	}
	
	
	// new method
	
	private List<ServicePartyApprovalEntity> fetchApprovalEntities(Session session, PartyCodeCreateRequestModel requestModel) {
		ServicePartyApprovalEntity hierarchyData = null;
	    Query<?> query = null;
	    List<ServicePartyApprovalEntity> approvarList= new ArrayList<>();
	    String sqlQuery = "exec [sp_party_master_approval_hierarchy]";
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                hierarchyData = new ServicePartyApprovalEntity();
	                hierarchyData.setApproverLevelSeq((Integer)row.get("approver_level_seq"));
	                hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
	                hierarchyData.setGrpSeqNo((Integer)row.get("grp_seq_no"));
	                hierarchyData.setApprovalStatus((String)row.get("approvalStatus"));
	                hierarchyData.setIsfinalapprovalstatus((Character)row.get("isFinalApprovalStatus"));
	                hierarchyData.setRejectedFlag('N');
	                approvarList.add(hierarchyData);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    }
	    logger.info("beofore return detail "+hierarchyData);
	    return approvarList;
	}
	// new method end 

	 private String generatePartyCodeAuto(String categoryCode, String desc,BigInteger branchId)
	 {
		 Session session = null;
			String docNumber=null;
			
			
			try
			{
				
				System.out.println("before generate "+branchId +" ");
				 session = sessionFactory.openSession();
				 docNumber =commonDao.getDocumentNumber(categoryCode,branchId.intValue() ,session);
				 System.out.println("docNumber we get is "+docNumber);

				 if(docNumber!=null)
				 {
					 //commonDao.updateDocumentNumber(docNumber,"SPARE SALE INVOICE RETURN",branchId,session);
					 commonDao.updateDocumentNumber(desc,branchId,docNumber,session);
					 System.out.println("after update docNumber "+docNumber);
				 }
							
			} catch (SQLGrammarException ex) {
				
				logger.error(this.getClass().getName(), ex);
			} catch (HibernateException ex) {
				
				logger.error(this.getClass().getName(), ex);
			} catch (Exception ex) {
					logger.error(this.getClass().getName(), ex);
			} finally {
				
					
				}
				if (session != null) {
					session.close();
				}
			
		
			return docNumber; 
	 }
	
	private int saveOutletCategoryDetail(Integer[] outletCategory, BigInteger partBranchId) {
			System.out.println("OutletCategoryFetched invoked ");
		
		int status=-1;
		if(outletCategory!=null)
		{
			
			for(int i:outletCategory)
			{
				
				System.out.println("i in insert "+i);
				Session session=null;
				String sqlQuery = null;
				Query query =null;
				System.out.println("SQL query for PART BRANCH detail" + sqlQuery);
				try {
					sqlQuery = "INSERT INTO party_master_outletCategoryDetail(po_category_id, party_branchId,partyCode)"
							+ "VALUES(:po_category_id,:party_branchId,:partyCode)";
					session = sessionFactory.openSession();
					session .beginTransaction();

									query = session.createSQLQuery(sqlQuery);
									query.setParameter("po_category_id", i);
									query.setParameter("party_branchId",partBranchId);
									query.setParameter("partyCode",partyCode);
							
							System.out.println("insert Query");
							
					        

					 status = query.executeUpdate(); // Execute the insert query
					session.getTransaction().commit();
					System.out.println("rowCount after commit "+status);

				} catch (SQLGrammarException exp) {
					logger.error(this.getClass().getName(), exp);
				} catch (HibernateException exp) {
					logger.error(this.getClass().getName(), exp);
				} catch (Exception exp) {
					logger.error(this.getClass().getName(), exp);
				} finally {
					if (session  != null) {
						session.close();
					}
				}
			}
				
		}
			


		return status;
	}

	private String generatePartyCode(BigInteger partyCategoryId, String dealerCode) {
		
		logger.info("generate partyCode invoked .... "+partyCategoryId +" "+dealerCode);
		String newPartyCode=null;
		String partyCategory=null;
		try
		{
			
			LocalDate currentDate = LocalDate.now();

	        // Define the desired date format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");

	        // Format the current date using the specified format
	        String formattedDate = currentDate.format(formatter);
	        String partyCatId=partyCategoryId.toString();
	        if(partyCatId.equals("4"))
	        {
	        	partyCategory="VSTM";
	        	
	        }
	        else if(partyCatId.equals("5"))
	        {
	        	partyCategory="ER";
	        }
	        else if(partyCatId.equals("6"))
	        {
	        	partyCategory="VSTP";
	        }
	        
	        newPartyCode=partyCategory+formattedDate+"E"+dealerCode;
	        System.out.println("newPartyCode "+newPartyCode);

		}catch(Exception e)
		{
			System.out.println(" "+e.getMessage());
			
		}
		return newPartyCode;
	}

	private String saveDocumentAtLocation(MultipartFile document1, BigInteger id) {
	    String fileSaveStatus = null;

	    logger.info("file save invoked ......");
	    try {
	        if (!document1.isEmpty()) {
	            File uploadDirectory = new File(uploadDir);

	            if (!uploadDirectory.exists()) {
	                uploadDirectory.mkdirs();
	            }

	            // Create folder with ID if it doesn't exist
	            File idDirectory = new File(uploadDirectory, id.toString());
	            if (!idDirectory.exists()) {
	                idDirectory.mkdirs();
	            }

	            String filePath = idDirectory.getAbsolutePath() + File.separator + document1.getOriginalFilename();
	            documentName = document1.getOriginalFilename();
	            System.out.println("document Name in filesave: " + document1.getOriginalFilename());
	            File dest = new File(filePath);
	            document1.transferTo(dest);
	            System.out.println("after save success");
	            fileSaveStatus = "success";
	        }
	    } catch (Exception e) {
	        fileSaveStatus = "fail";
	    }
	    return fileSaveStatus;
	}

}
