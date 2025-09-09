package com.hitech.dms.web.controller.outletCategory.search.daoImpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.controller.outletCategory.search.dao.PartyDetailFecthDao;
import com.hitech.dms.web.entity.partycode.PartyCodeEditResponse;
import com.hitech.dms.web.model.outletCategory.search.response.OutletCategoryPartsModel;
import com.hitech.dms.web.model.partybybranch.create.request.PanGstSearchRequest;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeUpdateRequest;
import com.hitech.dms.web.model.partycode.search.response.PartyDetailFetchResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


@Repository
public class PartyDetailFecthDaoImpl  implements PartyDetailFecthDao{

	private static final Logger logger = LoggerFactory.getLogger(PartyDetailFecthDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	
	@Autowired
	ConnectionConfiguration dataSource;
	
	@Override
	public PartyDetailFetchResponse fetchPartyDetalByPartyBranchId(Integer partyBranchId,Integer branchId,String dealerCode) {
		String Id = partyBranchId.toString();
		String branchId1=branchId.toString();
		logger.info("OutletCategorySearchDaoImpl  is invoked ...... " + partyBranchId, " branchId"+branchId1+" dealerCode"+dealerCode);
		PartyDetailFetchResponse response=new  PartyDetailFetchResponse();
		Session session = null;
		BigInteger partyBranchId1=null;
		Query query = null;		
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [party_detail]  :partyBranchId,:branch_id,:dealerCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partyBranchId", Id);
			query.setParameter("branch_id", branchId1);
			query.setParameter("dealerCode", dealerCode);

			System.out.println("after form Sql is "+sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					response = new PartyDetailFetchResponse();

					 partyBranchId1 = (BigInteger) row.get("party_branch_id");
					response.setPartyBranchId(partyBranchId1!=null?partyBranchId1:null);
					
					response.setBranchId((BigInteger) row.get("Branch_ID"));
					BigInteger partyCategoryId=(BigInteger)row.get("party_category_id");
					response.setPartyCategoryId(partyCategoryId!=null?partyCategoryId:null);
					String partyTitle=(String) row.get("PartyTitle");
					response.setPartyTitle(partyTitle!=null?partyTitle:null);
					String PartyCode = (String) row.get("PartyCode");
					response.setPartyCode(PartyCode!=null?PartyCode:null);
					String partyName = (String) row.get("PartyName");
					response.setPartyName(partyName!=null?partyName:"");
					String partyLocation = (String) row.get("Party_Location");
					response.setPartyLocation(partyLocation!=null?partyLocation:null);
					String GSTNo=(String) row.get("GST_NUMBER");
					response.setGstNumber(GSTNo!=null?GSTNo:"");
					String PartyStatus=(String) row.get("partyStatus");
					response.setPartyStatus(PartyStatus!=null?PartyStatus:null);
					String email=(String) row.get("Email1");
					response.setEmail1(email!=null?email:"");
					String mobileNo=(String) row.get("MobileNumber");
					response.setMobileNumber(mobileNo!=null?mobileNo:null);
					char isActive=(char) row.get("IsActive");
					
					response.setIsActive(isActive!= '\0'?isActive:null);
					
					String Dsr=(String) row.get("dsr");
					response.setDsr(Dsr!=null?Dsr:"");
					String FirstName=(String) row.get("FirstName");
					response.setFirstName(FirstName!=null?FirstName:null);
					String lastName=(String) row.get("LastName");
					response.setMiddleName(lastName!=null?lastName:null);
					String MiddleName=(String) row.get("MiddleName");
					response.setMiddleName(MiddleName!=null?MiddleName:null);
					String Designation=(String) row.get("Designation");
					response.setDesignation(Designation!=null?Designation:null);

					String PanNo=(String) row.get("PAN_OR_TAN");
					response.setPanOrTan(PanNo!=null?PanNo:"");

					String aadharNo=(String) row.get("AADHAR_NUMBER");
					response.setAadharNumber(aadharNo!=null?aadharNo:null);

					String address1=(String) row.get("PartyAddLine1");
					response.setPartyAddLine1(address1!=null?address1:null);

					String address2=(String) row.get("PartyAddLine2");
					response.setPartyAddLine2(address2!=null?address2:null);


					String address3=(String) row.get("PartyAddLine3");
					response.setPartyAddLine3(address3!=null?address3:null);

//					String district=(String) row.get("dsr");
//					String tahsil=(String) row.get("dsr");
//					String village=(String) row.get("dsr");
					BigInteger pincode=(BigInteger) row.get("pin_id");
					response.setPinId(pincode!=null?pincode:null);
//					String state=(String) row.get("dsr");
//					String country=(String) row.get("dsr");
					String email2=(String) row.get("Email2");
					response.setEmail2(email2!=null?email2:"");
					String document1=(String) row.get("document1");
					response.setDocument1(document1!=null?document1:null);

					String document2=(String) row.get("document2");
					response.setDocument2(document2!=null?document2:null);

					String document3=(String) row.get("document3");
					response.setDocument3(document3!=null?document3:null);


					String createdBy=(String) row.get("CreatedBy");
					response.setCreatedBy(createdBy!=null?createdBy:null);
					Date createdDate=(Date)row.get("CreatedDate");
					response.setCreatedDate(createdDate!=null?createdDate:null);

					String modifiedBy=(String) row.get("ModifiedBy");
					response.setModifiedBy(modifiedBy!=null?modifiedBy:null);

					Date modifiedDate=(Date) row.get("ModifiedDate");
					response.setModifiedDate(modifiedDate!=null?modifiedDate:null);
					
					
					
					String parentDealerName=(String)row.get("ParentDealerName");
					String BranchName=(String) row.get("BranchName");
					String dealerCode1=(String) row.get("dealerCode");
					
					response.setParentDealerName(parentDealerName!=null?parentDealerName:null);
					response.setBranchName(BranchName!=null ?BranchName:null);
					response.setDealerCode(dealerCode!=null?dealerCode1:null);

					
				}}

					if(partyBranchId1==null)
					{
						return null;
					}
			
			
			System.out.println("before send response " + response);

			

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
		return response;
		
	}

	@Override
	public PartyCodeEditResponse updatePartyCode(PartyCodeUpdateRequest request, String UserCode) {
		
		PartyCodeEditResponse response= new PartyCodeEditResponse();
		
		logger.info("updatePartyCode  is invoked ...... " + request, " userCode"+UserCode);
//		PartyDetailFetchResponse response=new  PartyDetailFetchResponse();
		Session session = null;
		BigInteger partyBranchId1=null;
		 Transaction tx = null;
		Query query = null;		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String sqlQuery = "update ADM_BP_PARTY_BRANCH set MobileNumber=:mobileNo,AADHAR_NUMBER=:aadharNo,PAN_OR_TAN=:panNo,GST_NUMBER=:gst,Email1=:email,PartyAddLine1=:address1,PartyAddLine2=:address2,PartyAddLine3=:address3,Party_Location=:partyLocation where party_branch_id=:partyBranchId";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("mobileNo", request.getMobileNumber());
			query.setParameter("aadharNo", request.getAadharNumber());
			query.setParameter("panNo", request.getPanNo());
			query.setParameter("email", request.getEmail());
			query.setParameter("address1", request.getAddress1());
			query.setParameter("address2", request.getAddress2());
			query.setParameter("address3", request.getAddress3());
			query.setParameter("partyLocation",request.getPartyLocation());

			query.setParameter("gst", request.getGst());
			query.setParameter("partyBranchId", request.getPartyBranchId());

			System.out.println("after form Sql is "+sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			 int rowsAffected = query.executeUpdate();
	            tx.commit();

	            System.out.println("Rows affected: " + rowsAffected);
	            if(rowsAffected>0)
	            {
	            	response.setStatusCode(200);
	            	response.setStatusMessage("Successfully Updated "+request.getPartyBranchId());
	            }
//			List data = query.list();
//			if (data != null && !data.isEmpty()) {
//
//				for (Object object : data) {
//					Map row = (Map) object;
//
//				}
//			}
//			
		}catch(Exception e)
		{
			
			
			e.printStackTrace();
			response.setStatusCode(302);
        	response.setStatusMessage("Not updated successfully "+e.getMessage());
			
		}
		return response;
	}

	@Override
	public List<PanGstSearchRequest> checkExistPanGst(PanGstSearchRequest request, String userCode) {
		Log.info("check pan GST invoked ");
		List<PanGstSearchRequest> responseList= new ArrayList<>();
		
		Session session = null;
		BigInteger partyBranchId1=null;
		Query query = null;		
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [GST_PAN_SEARCH]  :GST_NUMBER,:PAN_OR_TAN,:UserCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("GST_NUMBER",request.getGst());
			query.setParameter("PAN_OR_TAN", request.getPanOrTan());
			query.setParameter("UserCode", userCode);

			System.out.println("after form Sql is "+sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
		        for (Object object : data) {
		            Map row = (Map) object;
		            PanGstSearchRequest req = new PanGstSearchRequest();
		            System.out.println("In List Data");
		            String pan = (String) row.get("PAN_OR_TAN");
		            String gst = (String) row.get("GST_NUMBER");
		            System.out.println("get Data " + pan);
		            System.out.println("get gst " + gst);
		            req.setPanOrTan(pan != null ? pan : null);
		            req.setGst(gst != null ? gst : null);
		            if (req != null) {
		                responseList.add(req);
		            }
		        }
		    } else {
		        responseList = null;
		    }
		}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		System.out.println("before return "+responseList);

		return responseList;
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


	

}
