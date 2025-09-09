

package com.hitech.dms.web.daoImpl.coupon.management;

import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.config.persistance.ConfigConnection;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.coupon.management.CouponManagementDao;
import com.hitech.dms.web.model.coupon.management.ApprovalDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSaveResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponDetailsEntity;
import com.hitech.dms.web.model.coupon.management.CouponHeaderEntity;
import com.hitech.dms.web.model.coupon.management.CouponSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponSearchResponse;
import com.hitech.dms.web.model.coupon.management.DocumentNoModel;
import com.hitech.dms.web.model.coupon.management.SearchCouponDetailResponse;
import com.hitech.dms.web.model.coupon.management.UpdateCoupanRequest;
import com.hitech.dms.web.model.coupon.management.UpdateCouponResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.UpdatePartDetailResponse;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Repository
public class CouponManagementDaoImpl  implements  CouponManagementDao{

	private static final Logger logger=LoggerFactory.getLogger(CouponManagementDaoImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	ConnectionConfiguration dataSource;
	
//	@Autowired
//	ConfigConnection dataSource;
//	
	
	@Value("${file.upload-dir.CouponManagementReport}")
	private String CouponManagementReport;
	
	
	@Override
	public CouponDetailSaveResponse saveCouponDetails(CouponDetailSaveResponse couponDetail, String userCode) {
		
		Map<Integer,String> saveStatus=new HashMap<>();
		Map<Integer,String> saveCoupanDetailStatus=new HashMap<>();

		logger.debug("saveCouponDetails" +userCode);
		CouponDetailSaveResponse response =  new CouponDetailSaveResponse();
		BigInteger branchId=getBranchIdByuserCode(userCode);
		String docNumber=null;
		Session session=null;
		Session updateSession1=null;
		try
		{
			
			if(couponDetail.getCouponHeader()!=null)
			{
				
				 session = sessionFactory.openSession();
				 updateSession1=sessionFactory.openSession();
				 docNumber =commonDao.getDocumentNumber("CM",branchId.intValue() ,session);
				 System.out.println("docNumber we get is "+docNumber);

				 if(docNumber!=null)
				 {
					
					 updateDocumentNo(docNumber,"CM",branchId,updateSession1,"Coupon Management");
				 }
							
				 System.out.println("after update docNumber"+docNumber);
	
				 couponDetail.getCouponHeader().setDocumentNo(docNumber);
				saveStatus=saveCouponHeader(couponDetail,userCode);
				if(saveStatus.containsKey(200))
				{
					saveCoupanDetailStatus=saveCouponDetails11(couponDetail,userCode);


				}
				else
				{

					saveCoupanDetailStatus.put(302, "Header Detail Not Saved");
				}
			}
			
			
			
			
			if(saveCoupanDetailStatus.containsKey(200))
			{
				response.setStatusCode(200);
				response.setStatusMessage("saved Successfully docNumber : "+docNumber);
			}
			else
			{

				response.setStatusCode(301);
				response.setStatusMessage("Detail Not saved Successfully");
			}
			
			
			
		
			
		}catch (SQLGrammarException exp) {
			response.setStatusCode(301);
			response.setStatusMessage(exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(301);
			response.setStatusMessage(exp.getMessage());
		} catch (Exception exp) {
			response.setStatusCode(301);
			response.setStatusMessage(exp.getMessage());
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		System.out.println("before send response "+response);
		
		return response;
	}
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	
	  private void updateDocumentNo(String lastDocumentNumber, String documentPrefix, BigInteger branchId,
				Session session,String docDesc)
	  {
		  logger.info("updateDocumentNo "+lastDocumentNumber +"branch "+branchId+ " doc "+documentPrefix);
			try
			{
		        Transaction transaction = session.beginTransaction();

				if (null != lastDocumentNumber) {
					SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
					String currentDate = dateFormat1.format(new java.util.Date());
					String updateQuery = "EXEC [Update_INV_Doc_No] :LastDocumentNumber,:DocumentTypeDesc,:CurrentDate,:BranchID,:DocumentType";
					Query query = session.createSQLQuery(updateQuery);
					query.setParameter("LastDocumentNumber", lastDocumentNumber);
					query.setParameter("DocumentTypeDesc", docDesc);
					query.setParameter("CurrentDate", currentDate);
					query.setParameter("BranchID",branchId);
					query.setParameter("DocumentType", documentPrefix);
					query.executeUpdate();
					transaction.commit();
					System.out.println("after execute ");
				}
				
								
			}catch (SQLGrammarException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);

			} catch (Exception e) {
				
				e.printStackTrace();
				System.out.println("main exception message "+e.getMessage());

			} finally {
				if (session != null) {
					session.close();
				}
			}
			System.out.println("before branchId "+branchId);
			
		

		  
	  }

	private BigInteger getBranchIdByuserCode(String userCode) {
		
		BigInteger branchId=null;
		Session session = null;
		Query query = null;
		String sqlQuery=null;
		try
		{
			session = session = sessionFactory.openSession();
			 sqlQuery = "select * from FN_CM_GETBRANCH_BYUSERCODE(:userCode,:includeInactive)";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("includeInactive", "N");

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					branchId=(BigInteger) row.get("BRANCH_ID");
				    System.out.println("branchId "+branchId);
			
				}
			}
		}catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);

		} catch (Exception exp) {

		} finally {
			if (session != null) {
				session.close();
			}
		}
		System.out.println("before branchId "+branchId);
		return branchId;
	}
	
	
private BigInteger getBranchIdByAdminCode(String userCode) {
		
		BigInteger branchId=null;
		Session session = null;
		Query query = null;
		String sqlQuery=null;
		try
		{
			session = session = sessionFactory.openSession();
			 sqlQuery = "select * from FN_CM_GetOrgHierForBranchesUnderUser(:userCode,:includeInactive)";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("includeInactive", "N");

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					branchId=(BigInteger) row.get("branch_id");
				    System.out.println("branchId "+branchId);
			
				}
			}
		}catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);

		} catch (Exception exp) {

		} finally {
			if (session != null) {
				session.close();
			}
		}
		System.out.println("before branchId for admin  "+branchId);
		return branchId;
	}


	
	

	private Map<Integer, String> saveCouponDetails11(CouponDetailSaveResponse couponDetail, String userCode) {
	
		
		logger.debug("saveCouponDetails11  {} => ");
		
		
		Map<Integer,String> responseList= new HashMap<Integer,String>();
		List<CouponDetailsEntity> couponList=couponDetail.getCouponDetails();
		
		Session session = null;
		boolean isSuccess = false;
		Query query = null;
		String sqlQuery=null;
		
		try
		{
			
			for(CouponDetailsEntity entity:couponList)
			{
				session = session = sessionFactory.openSession();
				sqlQuery = "INSERT INTO SM_COUPON_MGMT_DTL(Document_No,Punching_Date,Coupon_No,Coupon_value,Approved_Status,Remarks,Created_By,Created_Date)"
						+ "VALUES(:Document_No,:Punching_Date,:Coupon_No,:Coupon_value,:Approved_Status,:Remarks,:Created_By,:Created_Date)";
				session = sessionFactory.openSession();
				session.beginTransaction();
				 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		            Date date = dateFormat.parse(entity.getPunchingDate());
		            System.out.println("Parsed Date: " + date);
				
				query = session.createSQLQuery(sqlQuery)
						.setParameter("Document_No",couponDetail.getCouponHeader().getDocumentNo())
						.setParameter("Punching_Date",new Date())
						.setParameter("Coupon_No", entity.getCouponNumber())
						.setParameter("Coupon_value",entity.getCouponValue())
						.setParameter("Approved_Status","Waiting for Approval")
						.setParameter("Remarks",entity.getRemarks())
						.setParameter("Created_By",userCode)
						.setParameter("Created_Date",new Date());
					
						int rowCount = query.executeUpdate(); // Execute the insert query
						session.getTransaction().commit();
						System.out.println("cpDetail after commit "+rowCount);
						if(rowCount==1)
						{

							responseList.put(200,"Data Inserted Successfullly");
						}
						else
						{

							responseList.put(302,"Data saved unSuccessfull");

						}

			}
			
			
		}catch (SQLGrammarException exp) {
			responseList.put(302,exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			responseList.put(302,exp.getMessage());

		} catch (Exception exp) {
			responseList.put(302,exp.getMessage());

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}
		
	private Map<Integer, String> saveCouponHeader(CouponDetailSaveResponse couponDetail, String userCode) {
		
		logger.debug("saveCouponHeader ==>{} ");
		Map<Integer, String> responseList= new HashMap<>();
		CouponHeaderEntity header= couponDetail.getCouponHeader();
		if(header==null)
		{
		
			responseList.put(302,"coupon Header Detail is Empty");
			return responseList;
		}
		
		Session session = null;
		boolean isSuccess = false;
		Query query = null;
		String sqlQuery=null;
		
		try
		{
			
			
			session = session = sessionFactory.openSession();
			sqlQuery = "INSERT INTO SM_COUPON_MGMT_HDR(Document_No,Document_Date,Document_Status,Approved_Total_Amount,Dealer_code,Created_By,Created_Date)"
					+ "VALUES(:Document_No,:Document_Date,:Document_Status,:Approved_Total_Amount,:Dealer_code,:Created_By,:Created_Date)";
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			query = session.createSQLQuery(sqlQuery)
					.setParameter("Document_No",header.getDocumentNo())
					.setParameter("Document_Date",new Date())
					.setParameter("Document_Status", header.getDocumentStatus())
					.setParameter("Approved_Total_Amount",header.getApprovedTotalAmount())
					.setParameter("Dealer_code",couponDetail.getDealerCode())
					.setParameter("Created_By",userCode)
					.setParameter("Created_Date",new Date());
				
					int rowCount = query.executeUpdate(); // Execute the insert query
					session.getTransaction().commit();
					if(rowCount==1)
					{
						responseList.put(200,"Data Inserted Successfullly");
					}
					else
					{
						responseList.put(302,"Data saved unSuccessfull");

					}
			
		   
			
		}catch (SQLGrammarException exp) {
			responseList.put(302,exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			responseList.put(302,exp.getMessage());

		} catch (Exception exp) {
			responseList.put(302,exp.getMessage());

		} finally {
			if (session != null) {
				session.close();
			}
		}

		
		
		
		return responseList;
	}

	@Override
	public CouponSearchResponse ftechCouponDetils(CouponDetailSearchRequest request, String userCode) {
		
		
		logger.debug("ftechCouponDetils => "+request.getDealerCode() +" "+request.getDocumentNo() +" "+request.getSearchType());
		
		List<SearchCouponDetailResponse> searchList =null;
		
		SearchCouponDetailResponse response= null;
		CouponHeaderEntity headerDetails=null;
		
		CouponSearchResponse searchResponse = new CouponSearchResponse();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
     
		
		Session session=null;
		Query query =null;
		String sqlQuery = null;
		
		try
		{
			
			session = session = sessionFactory.openSession();
			sqlQuery = "exec [COUPON_MANAGMENT_SEARCH] :DocumentNo,:DealerCode,:SearchType,:UserCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DocumentNo",request.getDocumentNo());
			query.setParameter("DealerCode", request.getDealerCode());
			query.setParameter("SearchType", request.getSearchType());
			query.setParameter("UserCode",userCode);

			

			System.out.println("SqlQuery we get " + sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SearchCouponDetailResponse();
					 String dealerCode=(String) row.get("Dealer_code");
					 response.setDealerCode(dealerCode!=null?dealerCode:null);
					 BigInteger couponAmount=(BigInteger) row.get("approvedAmount");
					 System.out.println("the approved Amount "+couponAmount);
					 String Status= (String) row.get("Document_Status");
					 System.out.println("status we get is "+Status);
					 response.setStatus(Status!=null?Status:null);
					  response.setCouponAmount(couponAmount!=null?couponAmount:null);
					  String documentNo=(String) row.get("Document_No");
					  response.setDocumentNo(documentNo!=null?documentNo:null);
					  Date documentDate= (Date) row.get("Document_Date");
					  System.out.println("documentDate "+documentDate);
					  String date = null;
					  if(documentDate!=null)
					  {
						  date = dateFormat.format(documentDate);
						  
					  }
					  response.setDocumentDate(date!=null?date:null);
					  System.out.println("Parsed Date: " + date);
					  BigInteger parentDealerId =(BigInteger)row.get("parent_dealer_id");
					 // response.setParentDealerId(parentDealerId!=null?parentDealerId:null);
					  String branchName=(String) row.get("BranchName");
					  response.setBranchName(branchName!=null?branchName:null);
					  String branchLocation=(String) row.get("BranchLocation");
					  response.setBranchLocation(branchLocation!=null?branchLocation:null);
					  String dealerName=(String) row.get("ParentDealerName");
					  response.setDealerName(dealerName!=null?dealerName:null);
					  BigInteger totalCouponValue=(BigInteger) row.get("TotalCouponValue");
					  response.setTotalCouponAmount(totalCouponValue!=null?totalCouponValue:null);
					  String action =Status;
					  response.setAction(action);
					  response.setCreditNoteNo(null);
					  response.setCreditNoteDate(null);
					  response.setCreditNotAmount(null);
					  searchList.add(response);
					  searchResponse.setSearchList(searchList);
					  searchResponse.setStatusCode(200);
					  searchResponse.setStatusMessage("Data get Successfully");
				}
				
			
			}
			
		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			searchResponse.setStatusCode(302);
			  searchResponse.setStatusMessage(exp.getMessage());
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			searchResponse.setStatusCode(302);
			  searchResponse.setStatusMessage(exp.getMessage());

		} catch (Exception exp) {
			searchResponse.setStatusCode(302);
			  searchResponse.setStatusMessage(exp.getMessage());

		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		
		
		return searchResponse;
	}

	@Override
	public List<String> searchDocumentNo(String searchType, String searchText, String userCode) {

		System.out.println("searchType at daoImpl "+searchType +" "+searchText+" "+userCode);
		List<String> searchList= null;
		DocumentNoModel model= null;
		Session session=null;
		String SqlQuery=null;
		Query query = null;
		String sqlQuery = "exec [COUPON_MANAGEMENT_SEARCH_DOCUMENT] :SearchText, :SearchType,:UserCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchType", searchText);
			query.setParameter("SearchText", searchType);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);	
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model = new DocumentNoModel();
					String documentNo=(String) row.get("Document_No");
					System.out.println("documentNo we get "+documentNo);
//					model.setDocumentNo(documentNo!=null?documentNo:"");
					searchList.add(documentNo);
					System.out.println(searchList);
					
					
				}
				}

			
		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			

		} catch (Exception exp) {
			

		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return searchList;
	}

	@Override
	public CouponDetailResponse getCouponDetail(String dealerCode, String documentNo, String userCode) {
		
		CouponDetailResponse response = new CouponDetailResponse();
		
		List<CouponDetailsEntity> detailList= null;
		CouponDetailsEntity entity=null;
	
		
		Session session=null;
		Query query =null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		boolean isSuccess=false;
		String sqlQuery="exec [COUPON_MANAGEMENT_DETAIL] :DocuemntNo";
		System.out.println("Documentttttttt "+documentNo);
	//	String sqlQuery="select * from SM_COUPON_MGMT_DTL where Document_No=:documentNo";
		
		try
		{
			
			session=sessionFactory.openSession();
			query=session.createSQLQuery(sqlQuery);
			query.setParameter("DocuemntNo", documentNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);	
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				detailList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					 entity= new CouponDetailsEntity();
					 BigInteger hdrDtlId=(BigInteger)row.get("Coupon_Hdr_dtl_id");
					 entity.setCouponHdrDtlId(hdrDtlId!=null?hdrDtlId:null);
					 String documentNo1=(String)row.get("Document_No");
					 entity.setDocumentNo(documentNo1!=null?documentNo1:null);
					 Date punchingDate=(Date)row.get("Punching_Date");
					 String punchDate=null;
					 if(punchingDate!=null)
					 {
						 punchDate=dateFormat.format(punchingDate);
					 }
					 entity.setPunchingDate(sqlQuery!=null?punchDate:null);
					 Integer couponNumber=(Integer) row.get("Coupon_No");
					 entity.setCouponNumber(couponNumber!=null?couponNumber:null);
					 BigInteger couponValue =(BigInteger) row.get("Coupon_value");
					 entity.setCouponValue(couponValue!=null?couponValue:null);
					 String approvedStatus =(String) row.get("Approved_Status");
					 entity.setApprovedStatus(approvedStatus!=null?approvedStatus:null);
					 BigInteger approvedAmount=(BigInteger) row.get("Approved_Total_Amount");
					 entity.setApprovedTotalAmount(approvedAmount!=null?approvedAmount:null);
					 String remarks=(String) row.get("Remarks");
					 entity.setRemarks(remarks!=null?remarks:null);
					 detailList.add(entity);
					 response.setDetailList(detailList);
				
					 
					 isSuccess=true;
					   
				}
				
			}
			
			
			if(isSuccess)
			{
				response.setStatusCode(200);
				response.setStatusMessage("Data Successfully fetched");
			}
			else
			{
				
				response.setStatusCode(302);
				response.setStatusMessage("Data not successfully found");
			}
			
			
			
			
		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			

		} catch (Exception exp) {
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			

		} finally {
			if (session != null) {
				session.close();
			}
		}
		return response;
	}

	@Override
	public UpdateCouponResponse updateApprovalRequest(List<UpdateCoupanRequest> updateList,String documentNo,BigInteger approvedAmount, String userCode) {
	
		logger.info("updateApprovalRequest {} "+userCode);
		UpdateCouponResponse response = new UpdateCouponResponse();
		String documentNo1=documentNo;
		List<UpdateCoupanRequest> couponList=updateList;
		String statusRejected=null;
		String statusApproved=null;
		String statusEmpty=null;
		if(updateList == null)
		{
			response.setStatusCode(302);
			response.setStatusMessage("Update list is Empty");
			return response;
		}
		System.out.println("before stream list "+updateList);
		boolean status = updateList.stream()
                .allMatch(obj -> obj.getApprovalStatus() != null);

        if(status ==false)
        {
        
        	statusEmpty="Partially approved";
        }
        System.out.println("after check status "+statusEmpty);
		Session session=null;
		Query query =null;
		int updatedRowCount=0;
		boolean isSuccess=false;
		try
		{
			
			for(UpdateCoupanRequest detail: updateList)
			{
			
				
			String sqlQuery = "update SM_COUPON_MGMT_DTL set Approved_Status=:Approved_Status where Coupon_Hdr_dtl_id=:detailId";
			session = sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("Approved_Status",detail.getApprovalStatus());
			query.setParameter("detailId", detail.getDetailId());
			if(detail!=null)
			{
				
				if(detail.getApprovalStatus()!=null)
					{
				if(detail.getApprovalStatus().equalsIgnoreCase("Approved"))
				{
					statusApproved=detail.getApprovalStatus();
					
				}
				else
				{
					statusRejected=detail.getApprovalStatus();
					
				}
				}
			}
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
	         updatedRowCount = query.executeUpdate();
	        
	        transaction.commit();
	        
	        isSuccess=true;
	        System.out.println("Coupon Detail Updated Successfully"+updatedRowCount);
	        
	        
		}
			
			if(isSuccess && updatedRowCount==1)
			{
				String statusToSend=null;
				if(statusRejected!=null)
				{
					statusToSend=statusRejected;
					
				}
				else
				{
					statusToSend=statusApproved;	
				}
				
				System.out.print("before send method "+statusToSend );
		        System.out.println("before send status "+statusEmpty);
		        int updateStatus=0;
                if(statusEmpty!=null)
                {
    		         updateStatus=updateHeaderStatus(documentNo1,statusEmpty,userCode,approvedAmount);

                }
                else
                {
    		         updateStatus=updateHeaderStatus(documentNo1,statusToSend,userCode,approvedAmount);

                }
				if(updateStatus==1)
				{
					response.setStatusCode(200);
					response.setStatusMessage("All row Updated Successfully");
				}
				else
				{
					response.setStatusCode(302);
					response.setStatusMessage("Header Status no updated ");	
				}
				
			}
			else
			{
				response.setStatusCode(302);
				response.setStatusMessage("All row  not Updated Successfully");
			}
		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			

		} catch (Exception exp) {
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			

		} finally {
			if (session != null) {
				session.close();
			}
		}

		
		
		
		
		
		
		return response;
	}

	private int updateHeaderStatus(String documentNo1,String statusSend, String userCode,BigInteger approvedAmount) {
		
		logger.info("update headerStatus "+documentNo1 +"Status "+statusSend + " approvedAmt" +approvedAmount);
		
		Session session=null;
		Query query =null;
		int statusValue=0;
		int updatedRowCount=0;
		boolean isSuccess=false;
		try
		{
			
			String sqlQuery = "update SM_COUPON_MGMT_HDR set Document_Status=:Approved_Status,Approved_Total_Amount=:approvedAmount where Document_No=:documentNo";
			session = sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("Approved_Status",statusSend);
			query.setParameter("approvedAmount", approvedAmount);
			query.setParameter("documentNo",documentNo1);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
	         updatedRowCount = query.executeUpdate();
	        
	        transaction.commit();
	        
	        isSuccess=true;
	        System.out.println("Coupon status Successfully"+updatedRowCount);
	        
	        
		
			
			if(isSuccess && updatedRowCount==1)
			{
				statusValue=1;
				
			}
			else
			{
				
				statusValue=0;
			}
		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			
			
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			
			

		} catch (Exception exp) {
			exp.printStackTrace();
			
			

		} finally {
			if (session != null) {
				session.close();
			}
		}

		
		
		
		return statusValue;
		
		
		

	}

	@Override
	public CouponSearchResponse getCouponDetailByDate(CouponSearchRequest request,String userCode) {
		
		if(request.getDealerCode()==null)
		{
			System.out.println("In null dealerCode");
		}
		//System.out.println("searchType at getCouponDetailByDate  "+request.getFromDate() +" "+request.getToDate()+" "+userCode+"docNo"+request.getDocNo() +" page "+request.getPage() +"size "+request.getSize());
		List<SearchCouponDetailResponse> searchList =null;
		SearchCouponDetailResponse response= null;
		CouponSearchResponse searchResponse = new CouponSearchResponse();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		Integer recordCount=0;
     
		Session session=null;
		String SqlQuery=null;
		Query query =null;
		String sqlQuery = null;
		//if(docNo)
		String newDoc=request.getDocNo();
		if(newDoc==null) {
			newDoc=null;
		}
		
		
		try
		{
			
			
            System.out.println("dealerCode "+request.getDealerCode());
			session = session = sessionFactory.openSession();
			String dealerCodecheck=request.getDealerCode();
			if (dealerCodecheck==null)
			{
				System.out.println("In if ");
				sqlQuery = "exec [COUPON_MANAGEMENT_SEARCH_DATE_FILTER] :pcId,:orgHierID,:dealerId,:branchId,:Stateid,:zoneId,:territoryId,:FromDate,:ToDate,:UserCode,null,:DocNo,:page,:size";
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("pcId",request.getPcId());
				query.setParameter("orgHierID",request.getOrgHierId());
				query.setParameter("dealerId",request.getDealerId());
				query.setParameter("branchId",request.getBranchId());
				query.setParameter("Stateid",request.getStateId());
				query.setParameter("zoneId",request.getZoneId());
				query.setParameter("territoryId",request.getTerritoryId());
				query.setParameter("FromDate",request.getFromDate());
				query.setParameter("ToDate", request.getToDate());
				query.setParameter("UserCode",userCode);
				query.setParameter("DocNo",newDoc);
				query.setParameter("page",request.getPage());
				query.setParameter("size",request.getSize());

			
			}
			else
			{
				System.out.println("In else ");
				sqlQuery = "exec [COUPON_MANAGEMENT_SEARCH_DATE_FILTER] :pcId,:orgHierID,:dealerId,:branchId,:Stateid,:zoneId,:territoryId,:FromDate,:ToDate,:UserCode,:DealerCode,:DocNo,:page,:size";
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("pcId",request.getPcId());
				query.setParameter("orgHierID",request.getOrgHierId());
				query.setParameter("dealerId",request.getDealerId());
				query.setParameter("branchId",request.getBranchId());
				query.setParameter("Stateid",request.getStateId());
				query.setParameter("zoneId",request.getZoneId());
				query.setParameter("territoryId",request.getTerritoryId());
				query.setParameter("FromDate",request.getFromDate());
				query.setParameter("ToDate", request.getToDate());
				query.setParameter("UserCode",userCode);
				query.setParameter("DealerCode",request.getDealerCode()!= null ? request.getDealerCode(): "null");
				query.setParameter("DocNo",newDoc);
				query.setParameter("page",request.getPage());
				query.setParameter("size",request.getSize());
				//query.setParameter("DealerCode",dealerCode1!= null ? dealerCode1: "null");
			}
			
			System.out.println("SqlQuery we get fromDate and to Date  " + sqlQuery+ " "+request.getFromDate()+" "+request.getToDate() +" "+request.getDocNo());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SearchCouponDetailResponse();
					 String dealerCode=(String) row.get("Dealer_code");
					 response.setDealerCode(dealerCode!=null?dealerCode:null);
					  BigInteger couponAmount=(BigInteger) row.get("couponAmount");
					  response.setCouponAmount(couponAmount!=null?couponAmount:null);
					  String documentNo=(String) row.get("Document_No");
					  String status=(String) row.get("Document_Status");
					  response.setStatus(status!=null?status:null);
					  response.setDocumentNo(documentNo!=null?documentNo:null);
					  Date documentDate= (Date) row.get("Document_Date");
					  System.out.println("documentDate "+documentDate);
					  String date = null;
					  if(documentDate!=null)
					  {
						  date = dateFormat.format(documentDate);
						  
					  }
					  response.setDocumentDate(date!=null?date:null);
					  System.out.println("Parsed Date: " + date);
					  BigInteger parentDealerId =(BigInteger)row.get("parent_dealer_id");
					  //response.setParentDealerId(parentDealerId!=null?parentDealerId:null);
					  String branchName=(String) row.get("BranchName");
					  response.setBranchName(branchName!=null?branchName:null);
					  String branchLocation=(String) row.get("BranchLocation");
					  response.setBranchLocation(branchLocation!=null?branchLocation:null);
					  BigInteger totalCouponAmount=(BigInteger) row.get("TotalCouponValue");
					  response.setTotalCouponAmount(totalCouponAmount!=null?totalCouponAmount:null);
					  String dealerName=(String) row.get("ParentDealerName");
					  response.setDealerName(dealerName!=null?dealerName:null);
					  if (recordCount.compareTo(0) == 0) {
							recordCount = (Integer) row.get("totalRecords");
						}
					  
					  String action ="Go for approval";
					  response.setAction(action);
					  response.setCreditNoteNo(null);
					  response.setCreditNoteDate(null);
					  response.setCreditNotAmount(null);
					  searchList.add(response);
					  searchResponse.setSearchList(searchList);
					  searchResponse.setStatusCode(200);
					  searchResponse.setTotalRecordCount(recordCount);
					  searchResponse.setStatusMessage("Data get Successfully");
				}
				
			
			}
			
		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			searchResponse.setStatusCode(302);
			  searchResponse.setStatusMessage(exp.getMessage());
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			searchResponse.setStatusCode(302);
			  searchResponse.setStatusMessage(exp.getMessage());

		} catch (Exception exp) {
			searchResponse.setStatusCode(302);
			  searchResponse.setStatusMessage(exp.getMessage());

		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		
		System.out.println("before sending response "+searchResponse);
		return searchResponse;

	}
	@Override
	public ApprovalDetailResponse getApprovalDetail(String userCode) {
		
		ApprovalDetailResponse response = new ApprovalDetailResponse();;
		Session session=null;
		String SqlQuery=null;
		Query query =null;
		String sqlQuery = null;
		
		
		try
		{
			
			session = session = sessionFactory.openSession();
			sqlQuery = "exec [COUPON_MANAGEMENT_APPROVAl] :userCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode",userCode);
			System.out.println("SqlQuery we get " + sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					
					
					 BigInteger userId =(BigInteger) row.get("user_id");
					 response.setUserId(userId!=null?userId:null);
					 Integer usertypeId = (Integer) row.get("USER_TYPE_ID");
					 response.setUsertypeId(usertypeId);

					 BigInteger hoUserId = (BigInteger) row.get("ho_user_id");
					 response.setHoUserId(hoUserId);

					 String employeeName = (String) row.get("employee_name");
					 response.setEmployeeName(employeeName);

					 String employeeCode = (String) row.get("employee_code");
					 response.setEmployeeCode(employeeCode);

					 Integer departmentId = (Integer) row.get("department_id");
					 response.setDepartmentId(departmentId);

					 Integer hoDesignationId = (Integer) row.get("ho_designation_id");
					 response.setHoDesignationId(hoDesignationId);

					 Integer hoDesignationLevelId = (Integer) row.get("ho_designation_level_id");
					 response.setHoDesignationLevelId(hoDesignationLevelId);
					 
					 
					 response.setStatusCode(200);
					 response.setStatusMessage("Data fetched Successfully");
					
				}
				}

		}
		catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());
			  
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());

		} catch (Exception exp) {
			response.setStatusCode(302);
			response.setStatusMessage(exp.getMessage());

		} finally {
			if (session != null) {
				session.close();
			}
		}

		
		
		return response;
	}
	@Override
	public JasperPrint PdfGeneratorReportForJobCardOpenDao(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			// String filePath =
			// ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();
			String filePathsVariable = filePath + jasperName;
			System.out.println("filePath  " + filePathsVariable);
			connection = dataSource.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePathsVariable, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
		
		
	}
	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception {
		System.out.println("printReport entry "+printStatus);
		if (format != null && format.equalsIgnoreCase("pdf")) {
			JRPdfExporter exporter = new JRPdfExporter();
			System.out.println("printReport pdf entry ");

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			if (printStatus != null && printStatus.equals("true")) {
				System.out.println("printReport pdf entry after if ");
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
				configuration.setPdfJavaScript("this.print();");
				System.out.println("printReport  this print ");

			}
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			System.out.println("after final exportReport");


		} else if (format != null && format.equalsIgnoreCase("xls")) {
			System.out.println("printReport pdf entry else if ");
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

			exporter.exportReport();
		}

	}
	@Override
	public void printReport1(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) {
	
		if (format != null && format.equalsIgnoreCase("xls")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}

		
	}
	@Override
	public JasperPrint ExcelGeneratorReportdao(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jaspername;
			System.out.println("filePaths  " + filePaths);
			connection = dataSource.getConnection();
			System.out.println("connection " + connection);
			if (connection != null) {
				System.out.println("jasperParameter" + jasperParameter);
				System.out.println("filePaths" + filePaths);
				jasperPrint = JasperFillManager.fillReport(filePaths, jasperParameter, connection);

				System.out.println(jasperPrint.getName());
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;

	}
	
	

}
