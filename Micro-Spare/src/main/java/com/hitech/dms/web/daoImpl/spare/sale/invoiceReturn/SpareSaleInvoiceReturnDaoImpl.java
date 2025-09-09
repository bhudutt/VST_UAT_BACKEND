package com.hitech.dms.web.daoImpl.spare.sale.invoiceReturn;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.spare.sale.invoiceReturn.SpareSaleInvoiceReturnDao;
import com.hitech.dms.web.entity.user.InvoiceReturnApprovalEntity;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.BinDetailRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanPartDetailRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.ClaimDetailModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.GrnDetailModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.GrnHeaderResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnApproveStatus;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnDetail;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnDetailSaveResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnParts;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnRequestModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.InvoiceReturnSearchResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.PartDetailsModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.PartyDetailModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SearchInvoiceReturnRequest;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SpareInvoiceRetunSearchModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SpareInvoiceReturnHdrModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.SpareSaleInvoiceReturnPartDtlModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.UpdatePartDetailResponse;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.UploadDisagreeDocumentModel;
import com.hitech.dms.web.model.spare.sale.invoiceReturn.VstWithoutRefPartDtl;
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
@Transactional
public class SpareSaleInvoiceReturnDaoImpl implements SpareSaleInvoiceReturnDao {

	
	@Autowired
	CommonDao commonDao;
	
	
	@Autowired
	ConnectionConfiguration dataSource;
	
	@Value("${file.upload-dir.InvoiceReturnReport}")
	private String InvoiceReturnReport;
	
	

	String documentName = null;
	
	
	private static final Logger logger = LoggerFactory.getLogger(SpareSaleInvoiceReturnDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	//BigInteger branchId = BigInteger.ZERO;
	//String uploadDir="C:\\Users\\rambabu.kumar\\Documents\\thrsl\\frontend\\projects\\dms-service-requisition\\src\\assets\\img\\";
	private final String uploadDir = "C:\\VST-DMS-APPS\\Micro-Frontend_Apps\\dms-service-requisition\\assets\\img\\";
	
	
	

	@Override
	public InvoiceReturnDetail getInvoiceDetailByDOcNo(String mrnNo, String userCode) {

		// Integer category = Integer.parseInt("mrn "+ mrnNo+" "+userCode);
		logger.info("getInvoiceDetailByDocNo  is invoked ...... " + userCode, " " + mrnNo);
		InvoiceReturnDetail responseList = new InvoiceReturnDetail();
		InvoiceReturnDetail returnDetail = null;
		List<GrnHeaderResponse> grnHeaderList = null;
		GrnHeaderResponse model = null;
		List<PartDetailsModel> partDetailList = null;
		GrnDetailModel grnModel = null;
		ClaimDetailModel claimModel = null;
		Session session = null;
		boolean isSuccess = false;
		Query query = null;
		Date currentDate = new Date();
		String spareReturnInvoiceNo = "";
		try {
			session = session = sessionFactory.openSession();
			String sqlQuery = "select sp.*,st.Grn_type from PA_GRN_HDR sp inner join  SA_PO_MST_GRN_TYPE st on st.id=sp.mrnType_id where MRNNumber=:mrnNumber";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("mrnNumber", mrnNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				grnHeaderList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model = new GrnHeaderResponse();
					BigInteger mrnhdrId = (BigInteger) row.get("mrn_hdr_id");
					model.setMrnHdrId(mrnhdrId != null ? mrnhdrId : null);
					BigInteger branchId = (BigInteger) row.get("branch_id");
					model.setBranchId(branchId != null ? branchId : null);
					String mrnNumber = (String) row.get("MRNNumber");
					model.setMrnNumber(mrnNumber != null ? mrnNumber : "");
					Date mrnDate = (Date) row.get("MRNDate");
					String formattedDate = dateFormat.format(mrnDate);
					//System.out.println("aaaa" + formattedDate);
					//System.out.println("formattedDate " + dateFormat.parse(formattedDate));

					model.setMrnDate(formattedDate != null ? formattedDate : null);
					BigInteger partyId = (BigInteger) row.get("party_id");
					model.setPartyId(partyId != null ? partyId : null);
					String Status = (String) row.get("status");
					model.setStatus(Status != null ? Status : "");
					BigInteger branchStoreId =  (BigInteger) row.get("branch_store_id");
					model.setBranchStoreId(branchStoreId != null ? branchStoreId : null);
					String invoiceNumber = (String) row.get("InvoiceNo");
					model.setInvoiceNumber(invoiceNumber != null ? invoiceNumber : "");
					Date invoiceDate = (Date) row.get("InvoiceDate");
					String invoiceDate1 = dateFormat.format(invoiceDate);
					//System.out.println("bbbb " + invoiceDate1);
					//System.out.println("invoiceDate " + dateFormat.parse(invoiceDate1));
					model.setInvoiceDate(invoiceDate1 != null ? invoiceDate1 : null);
					String remarks = (String) row.get("Remarks");
					model.setRemarks(remarks != null ? remarks : "");
					String ClaimStatus = (String) row.get("ClaimStatus");
					model.setClaimStatus(ClaimStatus != null ? ClaimStatus : "");
					Date claimDate = (Date) row.get("ClaimDate");
					String claimDate1 = dateFormat.format(invoiceDate);
					model.setClaimDate(claimDate1 != null ? claimDate1 : null);
					// String returnInvoiceDate=
					String returnInvoiceDate = dateFormat.format(currentDate);
					//System.out.println("InvoiceReturnDate " + returnInvoiceDate);
					model.setSpareSaleInvoiceDate(returnInvoiceDate);
					//spareReturnInvoiceNo = generateReturnInvoiceNo(branchId, userCode, invoiceNumber);
				//	System.out.println("spareReturnInvoiceNo " + spareReturnInvoiceNo);
					String grnFrom=(String) row.get("Grn_type");
					model.setGrnFrom(grnFrom);
					
					model.setSpareSaleReturnInvoiceNo(spareReturnInvoiceNo);
					grnHeaderList.add(model);
					isSuccess = true;

				}

			}
			if (isSuccess) {
				System.out.println("isSuccess" + isSuccess);
				for (GrnHeaderResponse res : grnHeaderList) {
					System.out.println("return InvoiceDate" + res.getSpareSaleInvoiceDate());
					System.out.println("return InvoiceNo " + res.getSpareSaleReturnInvoiceNo());
					grnModel = new GrnDetailModel();
					grnModel.setGrnNumber(res.getMrnNumber());
					grnModel.setGrnDate(res.getMrnDate());
					grnModel.setGrnFrom(res.getGrnFrom());
					grnModel.setInvoiceNo(res.getInvoiceNumber());
					grnModel.setInvoiceDate(res.getInvoiceDate());
					grnModel.setSpareSaleReturnInvoiceNo(res.getSpareSaleReturnInvoiceNo());
					grnModel.setSpareSaleReturnInvoiceDate(res.getSpareSaleInvoiceDate());
					InvoiceReturnDetail partResponse= new InvoiceReturnDetail();
					partResponse = getPartDeatilList(mrnNo, userCode,"GRN",null);
					responseList.setPartDetailList(partResponse.getPartDetailList());;
					responseList.setBasicAmountTotal(partResponse.getBasicAmountTotal());
					responseList.setGstAmountTotal(partResponse.getGstAmountTotal());
					responseList.setTotalAmountTotal(partResponse.getTotalAmountTotal());
					responseList.setTotalVorCharges(partResponse.getTotalVorCharges());

				}
				if (grnModel != null) {
					responseList.setGrnDetail(grnModel);
					isSuccess = true;

				}
				
			}

			if (isSuccess) {
				responseList.setStatusCode(200);
				responseList.setStatusMessage("SuccessFully Fecth Detail");
			}

		} catch (SQLGrammarException exp) {
			responseList.setStatusCode(302);
			responseList.setStatusMessage("Error while  fetching detail");
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			responseList.setStatusCode(302);
			responseList.setStatusMessage("Error while  fetching detail");
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
			responseList.setStatusCode(302);
			responseList.setStatusMessage("Error while  fetching detail");
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}

	private String generateReturnInvoiceNo(BigInteger branchId, String userCode, String invoiceNumber,Session session) {
		
		
		String docNumber=null;
		
		
		try
		{
			
			//System.out.println("before generate "+branchId +" ");
			 //session = sessionFactory.openSession();
			 docNumber =getDocumentNumber("INVR",branchId.intValue() ,session);
			 System.out.println("docNumber we get is "+docNumber);

			 if(docNumber!=null)
			 {
				 
				 updateDocumentNo(docNumber,"INVR",branchId,session,"SPARE SALE INVOICE RETURN");

				 
			 }
			 //docNumber =getDocumentNumber("INVR",branchId.intValue() ,session);
			 System.out.println("docNumber we get after "+docNumber);
						
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
	
	
	private String getDocumentNumber(String documentPrefix, Integer branchId, Session session1) {
		Query query = null;
		String documentNumber = null;
		 Session session= null;
		 session=sessionFactory.openSession();
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);

		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		session.close();
		return documentNumber;
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
			//System.out.println("before branchId "+branchId);
			
		

		  
	  }

	@Override
	public InvoiceReturnDetail getInvoiceDetailByClaimGenerationNo(String claimGenerationNo, String userCode) {
		// TODO Auto-generated method stub
		InvoiceReturnDetail responseList = new InvoiceReturnDetail();
		InvoiceReturnDetail returnDetail = null;
		List<GrnHeaderResponse> grnHeaderList = null;
		GrnHeaderResponse model = null;
		List<PartDetailsModel> partDetailList = null;
		GrnDetailModel grnModel = null;
		ClaimDetailModel claimModel = null;
		Session session = null;
		String spareReturnInvoiceNo = null;
		Date currentDate = new Date();
		boolean isSuccess = false;
		Query query = null;
		try {
			session =  sessionFactory.openSession();
			String sqlQuery="exec INVOICE_RETURN_CLAIM_DETAIL :ClaimGenerationNo,:UserCode";
			//String sqlQuery = "select * from PA_GRN_HDR where ClaimGenerationNumber=:claimGenerationNo";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ClaimGenerationNo", claimGenerationNo);
			query.setParameter("UserCode",userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				grnHeaderList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model = new GrnHeaderResponse();
					BigInteger mrnhdrId = (BigInteger) row.get("mrn_hdr_id");
					model.setMrnHdrId(mrnhdrId != null ? mrnhdrId : null);
					BigInteger branchId = (BigInteger) row.get("branch_id");
					model.setBranchId(branchId != null ? branchId : null);
					String mrnNumber = (String) row.get("MRNNumber");
					model.setMrnNumber(mrnNumber != null ? mrnNumber : "");
					String claimType=(String) row.get("ClaimType");
					model.setClaimType(claimType);
					//Date mrnDate = (Date) row.get("MRNDate");
					//String formattedDate = dateFormat.format(mrnDate);
					//System.out.println("aaaa" + formattedDate);
					//System.out.println("formattedDate " + dateFormat.parse(formattedDate));

					//model.setMrnDate(formattedDate != null ? formattedDate : null);
				//	BigInteger partyId = (BigInteger) row.get("party_id");
				//	model.setPartyId(partyId != null ? partyId : null);
					//String Status = (String) row.get("status");
					//model.setStatus(Status != null ? Status : "");
					//BigInteger branchStoreId = (BigInteger) row.get("branch_store_id");
					//model.setBranchStoreId(branchStoreId != null ? branchStoreId : null);
					String invoiceNumber = (String) row.get("InvoiceNo");
					model.setInvoiceNumber(invoiceNumber != null ? invoiceNumber : "");
					Date invoiceDate = (Date) row.get("InvoiceDate");
					String invoiceDate1 = dateFormat.format(invoiceDate);
					System.out.println("invoiceDate " + dateFormat.parse(invoiceDate1));
					model.setInvoiceDate(invoiceDate1 != null ? invoiceDate1 : null);
					//String remarks = (String) row.get("Remarks");
					//model.setRemarks(remarks != null ? remarks : "");
					//String ClaimStatus = (String) row.get("ClaimStatus");
					//model.setClaimStatus(ClaimStatus != null ? ClaimStatus : "");
					Date claimDate = (Date) row.get("claim_date");
					String claimDate1 = dateFormat.format(claimDate);
					System.out.println("claimDate1  " + dateFormat.parse(claimDate1));
					model.setClaimDate(claimDate1 != null ? claimDate1 : null);
					String returnInvoiceDate = dateFormat.format(currentDate);
					System.out.println("InvoiceReturnDate " + returnInvoiceDate);
					model.setSpareSaleInvoiceDate(returnInvoiceDate);
					spareReturnInvoiceNo = generateReturnInvoiceNo(branchId, userCode, invoiceNumber,session);
					System.out.println("spareReturnInvoiceNo " + spareReturnInvoiceNo);
					model.setSpareSaleReturnInvoiceNo(spareReturnInvoiceNo);
					System.out.println("model "+model.toString());
					grnHeaderList.add(model);
					isSuccess = true;

				}

			}
			if (isSuccess) {
				responseList.setHeaderResponse(grnHeaderList);
				System.out.println("isSuccess" + isSuccess);
				for (GrnHeaderResponse res : grnHeaderList) {
					grnModel = new GrnDetailModel();
					grnModel.setGrnNumber(res.getMrnNumber());
					grnModel.setGrnDate(res.getMrnDate());
					grnModel.setGrnFrom("VST");
					grnModel.setInvoiceNo(res.getInvoiceNumber());
					grnModel.setInvoiceDate(res.getInvoiceDate());
					grnModel.setSpareSaleReturnInvoiceNo(res.getSpareSaleReturnInvoiceNo());
					grnModel.setSpareSaleReturnInvoiceDate(res.getSpareSaleInvoiceDate());
					InvoiceReturnDetail partResponse= new InvoiceReturnDetail();
					partResponse = getPartDeatilList(null,userCode,"CLAIM",claimGenerationNo);
					responseList.setPartDetailList(partResponse.getPartDetailList());;
					responseList.setBasicAmountTotal(partResponse.getBasicAmountTotal());
					responseList.setGstAmountTotal(partResponse.getGstAmountTotal());
					responseList.setTotalAmountTotal(partResponse.getTotalAmountTotal());

				}
				if (grnModel != null) {
					responseList.setGrnDetail(grnModel);
					isSuccess = true;

				}
			
			}

			if (isSuccess) {
				responseList.setStatusCode(200);
				responseList.setStatusMessage("SuccessFully Fecth Detail");
			}

		} catch (SQLGrammarException exp) {
			responseList.setStatusCode(302);
			responseList.setStatusMessage("Error while  fetching detail");
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			responseList.setStatusCode(302);
			responseList.setStatusMessage("Error while  fetching detail");
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
			responseList.setStatusCode(302);
			responseList.setStatusMessage("Error while  fetching detail");
		} finally {
			if (session != null) {
				session.close();
			}
		}

	   //System.out.println("before send final response "+responseList);
		return responseList;
	}

	private InvoiceReturnDetail getPartDeatilList(String mrnNo, String userCode, String returnType,String claimGenerationNumber) {

		logger.info("get PartDetail entity   is invoked ...... " + mrnNo, " " + userCode+ " "+returnType);

		InvoiceReturnDetail response= new InvoiceReturnDetail();
		List<PartDetailsModel> partDetailList = null;
		PartDetailsModel model = new PartDetailsModel();
		BigDecimal totalValue = BigDecimal.ZERO;
		BigDecimal totalValuePrecise = BigDecimal.ZERO;

		BigDecimal gstTotalTotalSum = BigDecimal.ZERO;
		BigDecimal TotalVorChargesSum = BigDecimal.ZERO;
		BigDecimal totalValueTotal = BigDecimal.ZERO;
		BigDecimal basicTotalSum = BigDecimal.ZERO;
		
		Session session = null;
		boolean isSuccess = false;
		Query query = null;
		try {
			session = session = sessionFactory.openSession();
			String sqlQuery = "exec [spare_sales_invoice_return_details] :MRNNumber,:UserCode,:ClaimGenerationNumber,:returnType";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("MRNNumber", mrnNo);
			query.setParameter("UserCode", userCode);
			query.setParameter("ClaimGenerationNumber",claimGenerationNumber);
			query.setParameter("returnType", returnType);
			

			System.out.println("SqlQuery we get " + sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				partDetailList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model = new PartDetailsModel();
					BigInteger mrnHderId=(BigInteger) row.get("mrn_hdr_id");
					model.setMrnHdrId(mrnNo!=null?mrnHderId:mrnHderId.ZERO);
					String partNo = (String) row.get("PARTNumber");
					model.setPartNo(partNo != null ? partNo : null);
					Integer partId = (Integer) row.get("part_id");
					BigInteger claimtypeId=(BigInteger) row.get("Claim_Type_Id");
					model.setClaimtypeId(claimtypeId!=null?claimtypeId:claimtypeId.ZERO);
					String partDescription = (String) row.get("PartDesc");
					Integer branchId = (Integer) row.get("branch_id");
					model.setPartId(partId);
					model.setBranchId(branchId != null ? branchId : 0);
					model.setPartDescription(partDescription != null ? partDescription : null);
					BigDecimal invoiceQty = (BigDecimal) row.get("I_InvoiceQty");
					model.setInvoiceQty(invoiceQty != null ? invoiceQty : BigDecimal.ZERO);
					
					BigDecimal claimQty = (BigDecimal) row.get("Qty");
					Integer currentStock=(Integer) row.get("currentStock");
					model.setCurrentStock(currentStock!=null?currentStock:0);
					model.setClaimQty(claimQty != null ? claimQty : BigDecimal.ZERO);
					BigDecimal uniTprice = (BigDecimal) row.get("I_NDP");
					model.setUnitPrice(uniTprice != null ? uniTprice : BigDecimal.ZERO);
					BigDecimal discount = (BigDecimal) row.get("I_DiscountValue");
					String claimType = (String) row.get("ClaimType");
					Date claimDate = (Date) row.get("claim_date");
					String claimDate1 =null;
					if(claimDate!=null)
					{
						claimDate1= dateFormat.format(claimDate);
					}
					
					model.setClaimDate(claimDate != null ? claimDate1 : null);
					model.setClaimType(claimType != null ? claimType : null);

					model.setDiscount(discount != null ? discount : BigDecimal.ZERO);

					BigDecimal gstValue = (BigDecimal) row.get("I_TaxValue");
					BigDecimal gstPrecise=BigDecimal.ZERO;
					if(gstValue!=null)
					{
						 gstPrecise=gstValue.setScale(2,RoundingMode.DOWN);

						
					}
					if (gstValue != null) {
						gstTotalTotalSum = gstTotalTotalSum.add(gstPrecise);
						System.out.println("gstTotal  " + gstTotalTotalSum);

					}
					BigDecimal vorCharge = (BigDecimal)row.get("vorCharge");
					if(vorCharge!=null) {
						TotalVorChargesSum = TotalVorChargesSum.add(vorCharge);
						model.setVorCharge(vorCharge);
					}
					
					model.setGstValue(gstPrecise != null ? gstPrecise : BigDecimal.ZERO);
					//BigDecimal accessableAmount = (BigDecimal) row.get("BillValue");
					BigDecimal accessableAmount=BigDecimal.ZERO;
					BigDecimal accessableAmountPrecise=BigDecimal.ZERO;
					if(returnType=="CLAIM")
					{
						if (claimQty != null && uniTprice !=null) {
						    accessableAmount = claimQty.multiply(uniTprice);
						    if(accessableAmount!=null)
						    {
							    accessableAmountPrecise=accessableAmount.setScale(2,RoundingMode.DOWN);

						    }
						}	
					}
					if(returnType=="GRN")
					{
						
					  
						if (invoiceQty != null && uniTprice !=null) {
							if(claimQty!=null)
							{
								  BigDecimal finalGrnQty=invoiceQty.subtract(claimQty);
									accessableAmount = finalGrnQty.multiply(uniTprice);
									if(accessableAmount!=null)
									{
									    accessableAmountPrecise=accessableAmount.setScale(2,RoundingMode.DOWN);

									}


							}
							else
							{ BigDecimal finalGrnQty=invoiceQty.subtract(claimQty.ZERO);
							 accessableAmount = finalGrnQty.multiply(uniTprice);
							 if(accessableAmount!=null)
							 {
								    accessableAmountPrecise=accessableAmount.setScale(2,RoundingMode.DOWN);
 
							 }

								
							}
							
						}	
					}
					
//					
					model.setAccessableAmount(accessableAmountPrecise != null ? accessableAmountPrecise : BigDecimal.ZERO);
					if (accessableAmount != null) {
						basicTotalSum = basicTotalSum.add(accessableAmountPrecise);
						
					}
					if (uniTprice != null) {
						if(gstValue!=null)
						{
							totalValue = accessableAmount.add(gstValue);
							totalValue=		totalValue.add(vorCharge);
							if(totalValue!=null)
							{
								totalValuePrecise=totalValue.setScale(2,RoundingMode.DOWN);

							}

							

						}
						else
						{
							totalValue = accessableAmount.add(gstValue.ZERO);
							if(totalValue!=null)
							{
								totalValuePrecise=totalValue.setScale(2,RoundingMode.DOWN);

							}


						}

					}
					
				
					model.setTotalValue(totalValuePrecise != null ? totalValuePrecise : BigDecimal.ZERO);
					if (totalValue != null) {
						totalValueTotal = totalValueTotal.add(totalValuePrecise);
						System.out.println("totalValue " + totalValueTotal);

					}
					BigDecimal recieptQty = (BigDecimal) row.get("I_InvoiceQty");

					model.setRecieptQty(recieptQty != null ? recieptQty : BigDecimal.ZERO);
				

					String remarks = (String) row.get("Remarks");
					model.setRemarks(remarks != null ? remarks : null);

					partDetailList.add(model);

				}
				
				response.setPartDetailList(partDetailList);
				BigDecimal totalValueTotal1=BigDecimal.ZERO;
				BigDecimal basicTotalSum1=BigDecimal.ZERO;
				BigDecimal gstTotalTotalSum1=BigDecimal.ZERO;
				
				if(basicTotalSum!=null)
				{
					 basicTotalSum1=basicTotalSum.setScale(2,RoundingMode.DOWN);

				}
				if(gstTotalTotalSum!=null)
				{
					 gstTotalTotalSum1=gstTotalTotalSum.setScale(2,RoundingMode.DOWN);

				}
				if(totalValueTotal!=null)
				{
					 totalValueTotal1=totalValueTotal.setScale(2,RoundingMode.DOWN);

				}

				response.setBasicAmountTotal(basicTotalSum1);
				response.setGstAmountTotal(gstTotalTotalSum1);
				response.setTotalAmountTotal(totalValueTotal1);
				response.setTotalVorCharges(TotalVorChargesSum);
				
				 System.out.println("gstSum"+gstTotalTotalSum);
				 System.out.println("basicSum "+basicTotalSum);
				 System.out.println("toatlSum "+totalValueTotal);
				 //System.out.println("before send response"+partDetailList);

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

		//System.out.println("Part Detail List before send "+response);
		return response;
	}

	@Override
	public InvoiceReturnDetailSaveResponse saveInvoiceReturnDetail(InvoiceReturnRequestModel requestData,
			String userCode) {
		
		InvoiceReturnApprovalEntity ent= new InvoiceReturnApprovalEntity();

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked..");
		}
		InvoiceReturnDetailSaveResponse response= new InvoiceReturnDetailSaveResponse();
		Session Insertsession = null;
		Session selectsession = null;
		Session session=null;
		session=sessionFactory.openSession();
	
		String returnType=null;
		String grnStatus=null;
		SpareInvoiceReturnHdrModel requestModel=requestData.getHeader();
		SpareInvoiceReturnHdrModel checkExistData=new SpareInvoiceReturnHdrModel();
		int invoiceReturnCount=0;
		if(requestModel!=null)
		{
			
			checkExistData=fetchExistInvReturn(requestModel,userCode);
			if(checkExistData!=null)
			{
				response.setStatusCode(302);
				response.setStatusMessage("InvoiceReturn Already Exist for  "+requestModel.getGrnId());
				return response;
				
			}
		}
		
		
		String spareReturnInvoiceNo = generateReturnInvoiceNo(requestModel.getBranchId(), userCode,null,session);
		System.out.println("spareInvoiceReturnNo "+spareReturnInvoiceNo);
		requestModel.setSpareInvoiceReturnNo(spareReturnInvoiceNo);
		System.out.println("claimId  "+requestModel.getClaimId());
		System.out.println("GRNNo  "+requestModel.getGrnNumber());

		System.out.println("dealerId "+requestModel.getDealerId());
		Query query = null;
		String message = null;
		boolean isSuccess = false;
		String sqlQuery = null;
		int returnTypeId=0;
		if(requestModel.getReturnTypeId().equals("VST - WITHOUT REFRENCE")) {
			returnType="VST - WITHOUT REFRENCE"; 
		}
		else if(requestModel.getClaimId()==null|| requestModel.getClaimId().equals("0")|| requestModel.getClaimId().equals(""))
		{
			grnStatus="Waiting for approval";
			returnType="GRN";
			
		}
		else
		{
			returnType="CLAIM";
		}
		//System.out.println("grnStatus before "+grnStatus);
			try {
			sqlQuery = "INSERT INTO PA_INV_RETURN_HDR(INV_RET_NO,BRANCH_ID,DEALERID,INV_RET_DATE,RETURN_TYPE_ID,CLAIM_ID,GRN_ID,BASIC_AMOUNT,GST_AMOUNT,TOTAL_AMOUNT,CREATEDBY,CREATEDDATE,Status,VST_TYPE,TOTAL_VOR_CHARGE)"
					+ "VALUES(:INV_RET_NO,:BRANCH_ID,:DEALERID,:INV_RTE_DATE,:RETURN_TYPE_ID,:CLAIM_ID,:GRN_ID,:BASIC_AMOUNT,:GST_AMOUNT,:TOTAL_AMOUNT,:CREATEDBY,:CREATEDDATE,:Status, :VSTTYPE, :TOTAL_VOR_CHARGE)";
			Insertsession = sessionFactory.openSession();
			Insertsession.beginTransaction();
			
			query = Insertsession.createSQLQuery(sqlQuery)
					.setParameter("INV_RET_NO",requestModel.getSpareInvoiceReturnNo())
					.setParameter("BRANCH_ID",requestModel.getBranchId())
					.setParameter("DEALERID", requestModel.getDealerId())
					.setParameter("INV_RTE_DATE", new Date())
					.setParameter("RETURN_TYPE_ID",returnType)
					.setParameter("CLAIM_ID", requestModel.getReturnTypeId().equalsIgnoreCase("CLAIMNO")?requestModel.getClaimId():null)
					.setParameter("GRN_ID",requestModel.getReturnTypeId().equalsIgnoreCase("GRNNO")?requestModel.getGrnId():null)
					.setParameter("BASIC_AMOUNT",requestData.getTotalBasicValue())
					.setParameter("GST_AMOUNT",requestData.getTotalTaxValue())
					.setParameter("TOTAL_AMOUNT",requestData.getTotalAllValue())
					.setParameter("CREATEDBY",userCode)
					.setParameter("CREATEDDATE",new Date())
				    .setParameter("Status",grnStatus)
					.setParameter("VSTTYPE",requestModel.getVstType())
			        .setParameter("TOTAL_VOR_CHARGE",requestData.getTotalVocChargesValue());
			int rowCount = query.executeUpdate(); // Execute the insert query
			Insertsession.getTransaction().commit();
			if(rowCount==1)
			{
				 int saveRetInvPartDetails=0;
				 System.out.println("spare Invoice before send "+requestModel.getSpareInvoiceReturnNo());
				 int invoiceReturnHdrId=fetchInvoiceReturnHdrId(userCode,requestModel.getSpareInvoiceReturnNo());
				 System.out.println("spare Invoice Return no after  "+invoiceReturnHdrId);
				 if(invoiceReturnHdrId!=0)
				 {
					 saveRetInvPartDetails=saveReturnPartInvoicePartDetails(requestData.getPartDetails(),userCode,invoiceReturnHdrId,requestModel.getBranchId());
					 
					 if(saveRetInvPartDetails >=1)
						{
						 
						 List<InvoiceReturnApprovalEntity> approvalList= fetchApprovalEntity(requestData,userCode);
						 
						 if(!approvalList.isEmpty())
						 {
							 
							 int saveApproveEntity=saveApproverEntity(approvalList,invoiceReturnHdrId);
							 if(saveApproveEntity>=1)
							 {
								         System.out.println("after save approval Entity"+saveApproveEntity);
								 		response.setStatusCode(200);
									response.setStatusMessage("Successfully Saved with approval User");
							 }
							
						 }
						 	
							
						}
						else
						{
							response.setStatusCode(503);
							response.setStatusMessage("Unable to save Part Details");
						}
				 }
				
				
			}
			else
			{
				response.setStatusCode(304);
				response.setStatusMessage("Part Details Not saved ");
			}

		} catch (SQLGrammarException exp) {
			response.setStatusCode(320);
			response.setStatusMessage("Somerthing Went wrong while Saved");
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			response.setStatusCode(302);
			response.setStatusMessage("Not saved "+exp);
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			response.setStatusCode(320);
			response.setStatusMessage("Somerthing Went wrong while Saved");
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (selectsession != null) {
				selectsession.close();
			}
		}

		return response;
	}

	private void setParameter(String string, String vstType) {
		// TODO Auto-generated method stub
		
	}

	private int saveApproverEntity(List<InvoiceReturnApprovalEntity> approvalList, int invoiceReturnHdrId) {
		int saveStatus=0;
		
		Session session=null;
		Transaction tx=null;
		
		try
		{
			
			session = sessionFactory.openSession();
			tx=session.beginTransaction();
			for(InvoiceReturnApprovalEntity entity:approvalList)
			{
				entity.setInvRetNo(invoiceReturnHdrId);
				session.save(entity);
			}
			
			tx.commit();
			saveStatus=1;
			
		}catch(Exception e )
		{
			e.printStackTrace();
		}
		logger.info("after commit transaction "+saveStatus);
		
		return saveStatus;
	}

	private List<InvoiceReturnApprovalEntity> fetchApprovalEntity(InvoiceReturnRequestModel requestData,
			String userCode) {
		
		InvoiceReturnApprovalEntity hierarchyData = null;
	    Query<?> query = null;
	    Session session=null;
	    List<InvoiceReturnApprovalEntity> approvarList= new ArrayList<>();
	    String sqlQuery = "exec [sp_invoice_return_approval_hierarchy]";
	    try {
	    	session=sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                hierarchyData = new InvoiceReturnApprovalEntity();
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
	    logger.info("beofore return detail "+approvarList);
	    return approvarList;
		
		
		
	}

	private SpareInvoiceReturnHdrModel fetchExistInvReturn(SpareInvoiceReturnHdrModel requestModel, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceReturnHdrId invoked.." +requestModel.getGrnId());
		}
		SpareInvoiceReturnHdrModel response=null;
		Session session = null;
		Transaction transaction = null;
		boolean isSuccess=false;
		Query query = null;
		
		
		try
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String getInvoiceHdrId = "select * from PA_INV_RETURN_HDR where GRN_ID=:grnId";
			Query query1 = session.createSQLQuery(getInvoiceHdrId);
			query1.setParameter("grnId",requestModel.getGrnId());
			query1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List getInvoiceHdrIdList= query1.list();
			if (!getInvoiceHdrIdList.isEmpty()) {
				for (Object object : getInvoiceHdrIdList) {
					Map row = (Map) object;
						response= new SpareInvoiceReturnHdrModel();
						
						String invoiceReturnNo=(String) row.get("INV_RET_NO");
						System.out.println("InvoiceReturnFound at select "+invoiceReturnNo);
						response.setInvoiceNo(invoiceReturnNo!=null?invoiceReturnNo:null);
				}
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
				
			} else {
				
			}
			if (session != null) {
				session.close();
			}
		}
		

		return response;

	}

	private int fetchInvoiceReturnHdrId(String userCode, String spareInvoiceReturnNo) {
		int INV_RET_HDR_ID=0;
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceReturnHdrId invoked.." +spareInvoiceReturnNo);
		}
		Session session = null;
		Transaction transaction = null;
		boolean isSuccess=false;
		Query query = null;
		
		try
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String getInvoiceHdrId = "select * from PA_INV_RETURN_HDR where INV_RET_NO=:spareInvoiceReturnNo";
			Query query1 = session.createSQLQuery(getInvoiceHdrId);
			query1.setParameter("spareInvoiceReturnNo",spareInvoiceReturnNo);
			query1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List getInvoiceHdrIdList= query1.list();
			if (!getInvoiceHdrIdList.isEmpty()) {
				for (Object object : getInvoiceHdrIdList) {
					Map row = (Map) object;
					INV_RET_HDR_ID = (Integer) row.get("ID");
					System.out.println("Invoice Return hdr id "+INV_RET_HDR_ID);

				}
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
				
			} else {
				
			}
			if (session != null) {
				session.close();
			}
		}
		

		return INV_RET_HDR_ID;
	}

	private int saveReturnPartInvoicePartDetails(List<SpareSaleInvoiceReturnPartDtlModel> partDetails,
			String userCode,Integer invReturnHdrId, BigInteger branchId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.."+invReturnHdrId +" branchId "+branchId);
		}
		InvoiceReturnDetailSaveResponse response= new InvoiceReturnDetailSaveResponse();
		SpareSaleInvoiceReturnPartDtlModel partDetailResponse=null;
		Session Insertsession = null;
		Session selectsession = null;
		Query query = null;
		String message = null;
		boolean isSuccess = false;
		String sqlQuery = null;
		int returnTypeId=0;
		int saveStatuOfPartDetails=0;
		List<String> stockBinList = new ArrayList<String>();
		try {
			
			for(SpareSaleInvoiceReturnPartDtlModel res:partDetails)
			{
			
			sqlQuery = "INSERT INTO PA_INV_RETURN_DTL(INV_RET_HDR_ID,PARTID,INVOICE_QTY,CLAIM_QTY,UNIT_PRICE,DISCOUNT,BASIC_PRICE,GST_AMOUNT,ACCESSABLE_AMOUNT,TOTAL_VALUE,RECEIPT_QTY,REMARKS,CREATEDBY,CREATEDDATE,STOCK_BIN_ID,GST_PERCENTAGE,RETURN_QTY,VOR_CHARGE)"
					+ "VALUES(:INV_RET_HDR_ID,:PARTID,:INVOICE_QTY,:CLAIM_QTY,:UNIT_PRICE, :DISCOUNT,:BASIC_PRICE,:GST_AMOUNT,:ACCESSABLE_AMOUNT,:TOTAL_VALUE,:RECEIPT_QTY,:REMARKS,:CREATEDBY,:CREATEDDATE, :STOCK_BIN_ID, :GST_PERCENTAGE, :RETURN_QTY,:VOR_CHARGE)";
			
			if(res.getStockBinListId()!=null) {
			String[] binList = res.getStockBinListId().split(",");
			stockBinList = Arrays.asList(binList);
			if(stockBinList!=null && !stockBinList.isEmpty()) {
			for(String binBean:stockBinList) {
				Integer issueQty = res.getBinDetailList().get(binBean.trim()).getIssueQty().intValue()==0?res.getReturnQty().intValue():res.getBinDetailList().get(binBean.trim()).getIssueQty().intValue();
				BigDecimal gstAmount = res.getBasicUnitPrice().multiply(BigDecimal.valueOf(issueQty)).multiply(res.getGstPercentage()).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);
				BigDecimal total = gstAmount.add(res.getBasicUnitPrice().multiply(BigDecimal.valueOf(issueQty)));
				Insertsession = sessionFactory.getCurrentSession();
				query = Insertsession.createSQLQuery(sqlQuery)
						.setParameter("INV_RET_HDR_ID",invReturnHdrId)
						.setParameter("PARTID",res.getPartId())
						.setParameter("INVOICE_QTY",res.getInvoiceQty())
						.setParameter("CLAIM_QTY", res.getClaimQty()!=null?res.getClaimQty():0)
						.setParameter("UNIT_PRICE",res.getUnitPrice())
						.setParameter("DISCOUNT",res.getDiscount())
						.setParameter("BASIC_PRICE",res.getUnitPrice()!=null?res.getUnitPrice():res.getBasicUnitPrice())
						.setParameter("GST_AMOUNT",res.getGstValue()!=null?res.getGstValue():gstAmount)
						.setParameter("ACCESSABLE_AMOUNT",res.getAccessableAmount())
						.setParameter("TOTAL_VALUE",res.getTotalValue()!=null?res.getTotalValue():total)
						.setParameter("RECEIPT_QTY",res.getRecieptQty())
						.setParameter("REMARKS",res.getRemarks())
						.setParameter("CREATEDBY",userCode)
						.setParameter("CREATEDDATE",new Date())
				        .setParameter("STOCK_BIN_ID", Integer.parseInt(binBean.trim()))
				        .setParameter("GST_PERCENTAGE",res.getGstPercentage())
				        .setParameter("RETURN_QTY",issueQty)
						.setParameter("VOR_CHARGE",res.getVorCharge());
				         saveStatuOfPartDetails += query.executeUpdate();  // Execute the insert query
				         updateInvReturnStock(res,binBean,userCode);
			   }
			 }
			}
			else {
				Insertsession = sessionFactory.getCurrentSession();
				query = Insertsession.createSQLQuery(sqlQuery)
						.setParameter("INV_RET_HDR_ID",invReturnHdrId)
						.setParameter("PARTID",res.getPartId())
						.setParameter("INVOICE_QTY",res.getInvoiceQty())
						.setParameter("CLAIM_QTY", res.getClaimQty()!=null?res.getClaimQty():0)
						.setParameter("UNIT_PRICE",res.getUnitPrice())
						.setParameter("DISCOUNT",res.getDiscount())
						.setParameter("BASIC_PRICE",res.getUnitPrice()!=null?res.getUnitPrice():res.getBasicUnitPrice())
						.setParameter("GST_AMOUNT",res.getGstValue()!=null?res.getGstValue():res.getGstAmount())
						.setParameter("ACCESSABLE_AMOUNT",res.getAccessableAmount())
						.setParameter("TOTAL_VALUE",res.getTotalValue())
						.setParameter("ACCESSABLE_AMOUNT",res.getAccessableAmount())
						.setParameter("RECEIPT_QTY",res.getRecieptQty())
						.setParameter("REMARKS",res.getRemarks())
						.setParameter("CREATEDBY",userCode)
						.setParameter("CREATEDDATE",new Date())
				        .setParameter("STOCK_BIN_ID",res.getStockBinListId())
				        .setParameter("GST_PERCENTAGE",res.getGstPercentage())
				        .setParameter("RETURN_QTY",res.getReturnQty())
				        .setParameter("VOR_CHARGE",res.getVorCharge());
				         saveStatuOfPartDetails += query.executeUpdate();  // Execute the insert query
			}
			
			}
		//	Insertsession.getTransaction().commit();

			

		} catch (SQLGrammarException exp) {
			response.setStatusCode(320);
			response.setStatusMessage("Somerthing Went wrong while update partDetails");
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			response.setStatusCode(200);
			response.setStatusMessage("Successfully Saved");
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			response.setStatusCode(320);
			response.setStatusMessage("Somerthing Went wrong while Saved");
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (selectsession != null) {
				selectsession.close();
			}
		}

		System.out.println("at return "+saveStatuOfPartDetails);
	
		return saveStatuOfPartDetails;
	}
	@Transactional
	private void updateInvReturnStock(SpareSaleInvoiceReturnPartDtlModel req,String binBean, String userCode) {
       List<String> stockBinList = new ArrayList<String>();
		

		String sqlQuery = "exec [SP_PA_INV_RETURN_UPDATE_STOCK] :branchId, :returnQty, :binId, :partBranchId, :stockStoreId, :partId,"
				+ ":branchStoreId, :basicUnitPrice, :userCode ";
		org.hibernate.query.Query<?> query = null;
		try {
			
		
				
//				String[] binList = req.getStockBinListId().split(",");
//				stockBinList = Arrays.asList(binList);
				
				Session session = sessionFactory.getCurrentSession();
				query = session.createSQLQuery(sqlQuery);
				
					query.setParameter("branchId", req.getBranchId());
				    query.setParameter("returnQty", req.getBinDetailList().get(binBean.trim()).getIssueQty().intValue()==0?req.getReturnQty().intValue():req.getBinDetailList().get(binBean.trim()).getIssueQty().intValue());
				    query.setParameter("binId",Integer.parseInt(binBean.trim()));
				    query.setParameter("partBranchId", req.getPartBranchId());
				    query.setParameter("stockStoreId", req.getBinDetailList().get(binBean.trim()).getStockStoreId());
				    query.setParameter("partId", req.getPartId());
				    query.setParameter("branchStoreId", req.getBinDetailList().get(binBean.trim()).getBranchStoreId());
				    query.setParameter("basicUnitPrice", req.getBasicUnitPrice());
				    query.setParameter("userCode", userCode);
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					query.executeUpdate();
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	private Integer getBranchIdByUSerCode(String userCode, Session session) {
		
		Integer branchId=0;
		try
		{
			//Query query=null;
			 SQLQuery query = session.createSQLQuery("SELECT * FROM FN_CM_GETBRANCH_BYUSERCODE(:userCode, :parameter)");
			    query.setParameter("userCode", userCode);
			    query.setParameter("parameter", "N");
			    List<Object[]> data = query.list();
			    if (data != null && !data.isEmpty()) {
			        for (Object[] row : data) {
			            BigInteger branchId1 = (BigInteger) row[0];
			            branchId=branchId1.intValue();
			            // Assuming branchId is the first column
			            System.out.println("Branch Id: "+branchId);
			        }
			    }
			
		}catch(Exception e)
		{
			
			e.printStackTrace();
		}
		
		return branchId;
	}
	private int updatePartDetailByPartId(InvoiceReturnRequestModel resquest, String userCode) {
		
		int updateStatus=0;
		Integer StockStoreStatus=0;
		SpareInvoiceReturnHdrModel req=resquest.getHeader();
		Session session = null;
		
		if(logger.isDebugEnabled())
		{
			logger.info("In update partDetail  userCOde "+userCode +" partId "+resquest);
			
			
		}
		try
		{
		session=sessionFactory.openSession();
		UpdatePartDetailResponse response=null;
		
		Query query = null;
	    boolean isSuccess=false;
	    Integer partUpdateStatus= updatePartBranch(resquest,userCode);
	    
	    System.out.println("after partBranchUpdate status is "+partUpdateStatus);
	    if(partUpdateStatus>=1)
	    {
	    	updateStatus=1;
	    }
	    else
	    {
	    	updateStatus=0;
	    }

	    }
		catch(Exception e )
		{
			updateStatus=0;
			e.printStackTrace();
		}
	    
	    
	    
	    
			
			return updateStatus;
	}



	private Integer saveStockBinDetail(Integer partBranchId1,Integer branchId, Integer stockStoreId, BigInteger stockBinId,
			String userCode, SpareSaleInvoiceReturnPartDtlModel partModel,String documentId,boolean isPartyId,PartyDetailModel model,String isFor) {
		Integer saveStockDetailStatus=0;
		
		logger.info("saveStockBinDetail invoked {} "+isPartyId);
		
		
		System.out.println("saveStockBinDetail invoked ");
		try
		{
		    Session Insertsession=null;
		    
		    int OnHandQty=0;
			if(isFor.equalsIgnoreCase("Addition"))
			{
				 OnHandQty=partModel.getInvoiceQty()+model.getOnHandQty();

			}
			else if(isFor.equalsIgnoreCase("Subtraction"))
			{
				 OnHandQty=partModel.getInvoiceQty()-partModel.getClaimQty();

			}
		    BigDecimal totalStockAmount = new BigDecimal(OnHandQty);
		    System.out.println("OnhandQty after condition"+totalStockAmount);
		    BigDecimal currentStockAmount = totalStockAmount.multiply(partModel.getUnitPrice());
		    System.out.println("currentStockAmount after condition"+currentStockAmount);
		   // BigInteger document = new BigInteger(documentId);
			Query query = null;
			String sqlQuery=null;
		    boolean isSuccess=false;
		    sqlQuery = "INSERT INTO PA_STOCK_BIN_DTL(branch_id,partBranch_id,stock_store_id,stock_bin_id,document_id,TABLE_NAME,TransactionDate,IssueQty,"
		    		+ "ReceiptQty,AvlbQty,CreatedDate,CreatedBy)"
					+ "VALUES(:branch_id,:partBranch_id,:stock_store_id,:stock_bin_id,:document_id,:TABLE_NAME,"
					+ ":TransactionDate,:IssueQty,:ReceiptQty,:AvlbQty,:CreatedDate,:CreatedBy)";
			Insertsession = sessionFactory.openSession();
			Insertsession.beginTransaction();
			
					query = Insertsession.createSQLQuery(sqlQuery)
					.setParameter("branch_id",branchId)
					.setParameter("partBranch_id",partBranchId1)
					.setParameter("stock_store_id",stockStoreId)
					.setParameter("stock_bin_id",stockBinId)

					.setParameter("document_id",191)
					.setParameter("TABLE_NAME","PA_INVOICE_RET_HDR")
					.setParameter("TransactionDate",new Date())
					.setParameter("IssueQty",isFor.equalsIgnoreCase("Subtraction")?partModel.getInvoiceQty():0)
					.setParameter("ReceiptQty",isFor.equalsIgnoreCase("Addition")?partModel.getInvoiceQty():0)
					.setParameter("AvlbQty",partModel.getInvoiceQty())
					.setParameter("CreatedDate",new Date())
					.setParameter("CreatedBy",userCode);
					
			int rowCount =query.executeUpdate(); 
			// Execute the insert query
			System.out.println("rowCount we get after insert value "+rowCount);
			if(rowCount>=1)
			{
				saveStockDetailStatus=1;
	
			}
			
			Insertsession.getTransaction().commit();
		}catch(Exception e)
		{
			saveStockDetailStatus=0;
			e.printStackTrace();
		}
		return saveStockDetailStatus;
	}
	
	
	// 
	
	//update pa_part_branch
	private int updatePartBranch(InvoiceReturnRequestModel resquest, String userCode)
		{
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked..");
		}
		Integer updateStockBinStatus=0;
		int updatedRowCount=0;
		int isPartUpdateDealer=0;
		Integer partBranchIdForParty=0;
		PartyDetailModel partyDetail = null;
		Session session = null;
		Session selectSession= null;
		String grnNumber = null;
		session = sessionFactory.openSession();
		Integer partBranchId=0;
	
		Integer updateStockStoreIdStatus=0;
		Integer saveBinDetailStatus=0;
		BigInteger branchId=resquest.getHeader().getBranchId();
		Integer branch_Id=branchId.intValue();
		 Integer updateInvoiceReturn=0;

		
		Query query = null;
		Query insertQuery= null;
		boolean isSuccess=false;
		boolean isPartyId=false;
		
		try
		{
			
			for(SpareSaleInvoiceReturnPartDtlModel partModel:resquest.getPartDetails())
			{
			
				if(resquest.getHeader().getPartyId()!=null)
				{
					 partyDetail= getPartBranchByPartyCode(partModel.getPartId(),resquest.getHeader().getPartyId(),userCode);

				}

			 String grnFrom=resquest.getHeader().getGrnFrom();
			 System.out.println("grn From we get is "+grnFrom);
			 String isFor="SUBTRACT";
			  updateInvoiceReturn=UpdateInvoiceReturn(resquest,partModel,userCode,isFor,partyDetail);

			 if(!grnFrom.equalsIgnoreCase("VST")) {
				 isFor="ADD";
				 //partyDetail= getPartBranchByPartyCode(partModel.getPartId(),resquest.getHeader().getPartyId(),userCode);

				  updateInvoiceReturn=UpdateInvoiceReturn(resquest,partModel,userCode,isFor,partyDetail);

				 
			 }
			
			 
			 
			
			
				        
			}
			
			
			
			
			
			
		
		}catch (SQLGrammarException exp) {
			updatedRowCount=0;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			updatedRowCount=0;
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			updatedRowCount=0;
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
		}
		}
	
		return updateInvoiceReturn;
	}
	
	
	
	
	
	
	private Integer UpdateInvoiceReturn(InvoiceReturnRequestModel req,
			SpareSaleInvoiceReturnPartDtlModel partModel, String userCode,String isFor,PartyDetailModel model) {
		
			Session session=null;
			Query query=null;
			Integer partBranchId=0;
			Integer stockBinId=0;
			Integer branchStoreId=0;
			Integer branch=0;
			Integer updateInvoiceStatus=0;
			Integer currentBinStock=0;
			Integer OnHandQty=0;
			boolean isSuccess=false;
			try
			{
				
			if(isFor=="ADD")
			{
				
				partBranchId=model.getPartBranchId();
				stockBinId=model.getStockBinId().intValue();
				branchStoreId=model.getStockStoreId();
				branch=model.getBranchId();
				  //currentBinStock=getCurrentBinStock(model.getPartBranchId(),model.getStockBinId(),userCode);
				  //System.out.println("Existing currentBinStock in subtraction "+currentBinStock);
				 OnHandQty=partModel.getInvoiceQty();
				 System.out.println("Existing onHandQty in Addition "+OnHandQty);
			}
			else
			{
				partBranchId=partModel.getPartBranchId().intValue();
				stockBinId=partModel.getStockBinId().intValue();
				branchStoreId=partModel.getBranchStoreId();
				branch=req.getHeader().getBranchId().intValue();
				OnHandQty=partModel.getInvoiceQty()-partModel.getClaimQty();
				System.out.println("Existing onHandQty in subtraction "+OnHandQty);
			}



		    BigDecimal totalStockAmount = new BigDecimal(OnHandQty);
			System.out.println("OnhandQty after condition"+totalStockAmount);
			session=sessionFactory.openSession();
			String sqlQuery = "exec SP_Update_Invoice_return :flag,:fromPartBranch,:fromStockBin,:fromBranchStore,:Qty,:fromBranch,:fbasicUnitPrice";
			Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("flag",isFor);
			query.setParameter("fromPartBranch",partBranchId);
			query.setParameter("fromStockBin",stockBinId);
			query.setParameter("fromBranchStore",branchStoreId);
			query.setParameter("Qty",OnHandQty);
			query.setParameter("fromBranch",branch);
			query.setParameter("fbasicUnitPrice",partModel.getUnitPrice());
			 query.executeUpdate();
			 transaction.commit();
			 System.out.println("Commit data successfully ");
			 updateInvoiceStatus=1;
			
			System.out.println("after update status is ");
			
		       
		        

	      
			
			
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
		
		return updateInvoiceStatus;
	}

	private Integer updatePartBranchDetail(SpareSaleInvoiceReturnPartDtlModel partModel,
			String userCode,Integer branchId,boolean isPartyId,Integer existingQty,PartyDetailModel partyDetail,String isFor)
	{
		Integer updateStatus=0;
		Integer OnHandQty=0;
		logger.info("updatePartBranchDetail invoked {} "+isPartyId);
		try
		{
			
			Query query=null;
			Session session=null;


			if(isFor.equalsIgnoreCase("Addition"))
			{
				 OnHandQty=partModel.getInvoiceQty()+partyDetail.getOnHandQty();

			}
			else if(isFor.equalsIgnoreCase("Subtraction"))
			{
				Integer existingQty1=existingQty-partModel.getInvoiceQty();
				 OnHandQty=existingQty1-partModel.getClaimQty();

			}
			boolean isSuccess=false;
			
			System.out.println("We have onHandQty is  invoiceQty "+partModel.getInvoiceQty()+" claimQty "+partModel.getClaimQty()+" onHandQty "+OnHandQty);
			String sqlQuery = "update PA_PART_BRANCH set OnHandQty=:OnHandQty,ModifiedDate=:modifiedDate,ModifiedBy=:modifiedBy where partBranch_id=:partBranch_id";
			Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("OnHandQty",OnHandQty);
			query.setParameter("modifiedDate",LocalDateTime.now());
			query.setParameter("modifiedBy", userCode);
			if(isFor.equalsIgnoreCase("Subtraction"))
			{
				query.setParameter("partBranch_id",partModel.getPartBranchId());
	
			}
			else
			{
				query.setParameter("partBranch_id",partyDetail.getPartBranchId());

			}
//			query.setParameter("branchId",isFor.equalsIgnoreCase("Addition")?partyDetail.getBranchId():branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			Integer partBranchUpdateStatus = query.executeUpdate();
	        if(partBranchUpdateStatus>=1)
	        {
	        	updateStatus=1;
	        }
	        transaction.commit();
	        isSuccess=true;
	        System.out.println("partBranch Updated Successfully");
			
		}catch(Exception e)
		{
			updateStatus=0;
			e.printStackTrace();
		}
		
		return updateStatus;
	}
	
	private PartyDetailModel getPartBranchByPartyCode(Integer partId, BigInteger partyId, String userCode) {
		
		Integer partBranchId=0;
		PartyDetailModel response= new PartyDetailModel();

		System.out.println("partyId and partId "+partId +"  "+partyId);
		//Integer partyIdLong = partyId.intValue();
		//BigInteger party_id=new BigInteger(partyId);
		
		try
		{
			
			Session session= null;
			Query insertQuery=null;
			session=sessionFactory.openSession();
        	String sqlQuery1 = "select pb.branch_id,pb.onHandQty,pb.partBranch_id,ps.stock_bin_id,PA.branch_store_id as stock_store_id from ADM_BP_DEALER_BRANCH AB "
        			+ " inner join PA_PART_BRANCH pb on pb.branch_id=Ab.branch_id inner join PA_STOCK_BIN PS on PS.partBranch_id=pb.partBranch_id "
        			+ "inner join PA_STOCK_STORE PA on PA.partBranch_id=pb.partBranch_id  where parent_dealer_id=:partyId and pb.part_id=:partId";
			insertQuery = session.createSQLQuery(sqlQuery1);
//			insertQuery.setParameter("userCode",userCode);
			insertQuery.setParameter("partId",partId);
			insertQuery.setParameter("partyId",partyId);

			insertQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println("store Procedure "+insertQuery);
			List data = insertQuery.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					partBranchId=(Integer)row.get("partBranch_id");
					response.setPartBranchId(partBranchId);
					response.setBranchId((Integer)row.get("branch_id"));
					response.setOnHandQty((Integer)row.get("onHandQty"));
					response.setStockBinId((BigInteger) row.get("stock_bin_id"));
					response.setStockStoreId((Integer) row.get("stock_store_id"));
					System.out.println("getPartBranchValue latest"+response);
				}
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	private Integer GetPartBranchId(SpareSaleInvoiceReturnPartDtlModel partModel, String userCode) {
		
		Integer branchId=0;
		try
		{
			
			Session session= null;
			Query insertQuery=null;
			session=sessionFactory.openSession();
        	String sqlQuery1 = "SELECT  partBranch_id,branch_id from PA_PART_BRANCH where part_id =:partId";
			insertQuery = session.createSQLQuery(sqlQuery1);
			insertQuery.setParameter("partId",partModel.getPartId());
			insertQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = insertQuery.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					
					branchId=(Integer)row.get("branch_id");
					System.out.println("getPartBranchValue latest"+branchId);
				}
				
			}
		}
		
		
			
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return branchId;
	}

	private Integer updateStockStatusByPartBranch(Integer partBranchId,String userCode,Integer branchId,
			SpareSaleInvoiceReturnPartDtlModel partModel,BigInteger stockBinId,Integer existingQty,
			Integer stockStoreId,boolean isPartyId,PartyDetailModel partyDetail,String isFor) {

		logger.info("updateStockStatusByPartBranch invoked {}"+isFor);
		Integer currentBinStock=0;
		
		
		
		Integer OnHandQty=0;
		Integer stockBinStatus=0;
		Integer updatedRowCount=0;
		Integer stockDetailStatus=0;
		BigDecimal currentStockAmount=BigDecimal.ZERO;
		Session session=null;
		Query query = null;
	    boolean isSuccess=false;
	    Integer partBranchId1=0;
		if(isFor.equalsIgnoreCase("Addition"))
		{
			 //currentBinStock=getCurrentBinStock(partyDetail.getPartBranchId(),partyDetail.getStockBinId(),userCode);
			// System.out.println("Existing currentBinStock in subtraction "+currentBinStock);
			 OnHandQty=partModel.getInvoiceQty();
			 System.out.println("Existing onHandQty in Addition "+OnHandQty);

		}
		else if(isFor.equalsIgnoreCase("Subtraction"))
		{
			// currentBinStock=getCurrentBinStock(partModel.getPartBranchId().intValue(),stockBinId,userCode);
			// System.out.println("Existing currentBinStock in subtraction "+currentBinStock);
			// Integer existingQty1=currentBinStock-partModel.getInvoiceQty();
			 OnHandQty=partModel.getInvoiceQty()-partModel.getClaimQty();
			 System.out.println("Existing onHandQty in subtraction "+OnHandQty);

		}
		System.out.println("OnhandQty after condition"+OnHandQty);
	    BigDecimal totalStockAmount = new BigDecimal(OnHandQty);
		System.out.println("OnhandQty after condition"+totalStockAmount);
		currentStockAmount=totalStockAmount.multiply(partModel.getUnitPrice());
		System.out.println("currentStockAmount after condition"+currentStockAmount);

		
		
		try
		{
			session= sessionFactory.openSession();
			String sqlQuery = "update PA_STOCK_BIN set "
					+ "currentStock=:currentStock,currentStockAmount=:currentStockAmount,ModifiedDate=:modifiedDate,ModifiedBy=:modifiedBy where stock_bin_id=:stockBinId and partBranch_id=:partBranch_id";
	        Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
		
			query.setParameter("currentStock",OnHandQty);
			query.setParameter("currentStockAmount",currentStockAmount);
			query.setParameter("modifiedDate",LocalDateTime.now());
			query.setParameter("modifiedBy",userCode);
			query.setParameter("stockBinId",isFor.equalsIgnoreCase("Addition")?partyDetail.getStockBinId():stockBinId);
			query.setParameter("partBranch_id",isFor.equalsIgnoreCase("Addition")?partyDetail.getPartBranchId():partModel.getPartBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			 updatedRowCount = query.executeUpdate();
	         System.out.println("isSuccess "+updatedRowCount);
	        transaction.commit();
	        if(updatedRowCount>=1)
	        {
		        stockBinStatus=1;

	        }
	        isSuccess=true;
	        System.out.println("partBranch Updated Successfully");
		}catch(Exception e)
		{
			stockBinStatus=0;
			e.printStackTrace();
		}
	
		
		return stockBinStatus;
	}	
	
private Integer getCurrentBinStock(Integer partBranchId, BigInteger stockBinId, String userCode) {
		
		Integer binStock=0;
		BigDecimal BinStock=null;
		try
		{
			
			Session session = null;
			Query query = null;
			String sqlQuery = "select * from PA_STOCK_BIN where partBranch_id =:partBranchId and stock_bin_id=:stockBinId";
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partBranchId",partBranchId);
			query.setParameter("stockBinId",stockBinId);
			 List<Object[]> data = query.list(); 
			 if (data != null && !data.isEmpty()) {
				 for (Object[] row : data) {
			            // Assuming currentStock is stored in the first column of the result set
					 BinStock = (BigDecimal) row[6];
					 binStock=BinStock.intValue();
			        }
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//System.out.println("before return bin Stock "+binStock);
		return binStock;
	}

private Integer getCurrentStoreStock(Integer partBranchId, Integer branchStoreId, String userCode) {
	
	Integer stockStore=0;
	BigDecimal stockStore1=null;
	try
	{
		
		Session session = null;
		Query query = null;
		String sqlQuery = "select * from PA_STOCK_STORE where partbranch_id=:partBranch_id and branch_store_id=:branchStoreId";
		session = sessionFactory.openSession();
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("partBranch_id",partBranchId);
		query.setParameter("branchStoreId",branchStoreId);
		List<Object[]> data = query.list(); 
		 if (data != null && !data.isEmpty()) {
			 for (Object[] row : data) {
		            // Assuming currentStock is stored in the first column of the result set
		            stockStore1 = (BigDecimal) row[4];
		            stockStore=stockStore1.intValue();
		            
		        }
		}
		
	}catch(Exception e)
	{
		e.printStackTrace();
	}
//	System.out.println("before return store Stock "+stockStore);
	return stockStore;
}

private Integer updateStockStoreByPartBranch(Integer partBranchId,String userCode,Integer branchId, SpareSaleInvoiceReturnPartDtlModel partModel
		,BigInteger stockBinId,Integer stockStoreId,Integer existingQty,boolean isPartyId,PartyDetailModel model,String isFor) {
		logger.info("updateStockStoreByPartBranch {}"+isPartyId);
		
		Integer storeStock=0;
	
		System.out.println(" isPartyId"+isPartyId);
		Integer stockBinStatus=0;
		Integer updatedRowCount=0;
		Integer stockStoreStatus=0;
		Session session=null;
		Query query = null;
		BigDecimal currentStockAmount=BigDecimal.ZERO;
	    boolean isSuccess=false;
	    Integer OnHandQty=0;
	    Integer partBranchId1=0;
	    
	    
	    if(isFor.equalsIgnoreCase("Addition"))
		{
	    	storeStock=getCurrentStoreStock(model.getPartBranchId(),model.getStockStoreId(),userCode);
			 System.out.println("Existing currentBinStock in Addition "+storeStock);
			 OnHandQty=storeStock+model.getOnHandQty();

		}
		else if(isFor.equalsIgnoreCase("Subtraction"))
		{
	    	storeStock=getCurrentStoreStock(partModel.getPartBranchId().intValue(),stockStoreId,userCode);
			 System.out.println("Existing currentBinStock in Subtraction "+storeStock);

	    	Integer existingQty1=storeStock-partModel.getInvoiceQty();

			 OnHandQty=existingQty1-partModel.getClaimQty();
			 System.out.println("Existing OnHandQty in Addition "+OnHandQty);


		}
		System.out.println("OnhandQty after condition"+OnHandQty);
	    BigDecimal totalStockAmount = new BigDecimal(OnHandQty);
	    System.out.println("OnhandQty after condition"+totalStockAmount);
	    currentStockAmount=totalStockAmount.multiply(partModel.getUnitPrice());
	    System.out.println("currentStockAmount after condition"+currentStockAmount);
	    
		try
		{
			session= sessionFactory.openSession();
			String sqlQuery = "update PA_STOCK_STORE set currentStock=:currentStock,currentStockAmount=:currentStockAmount,ModifiedDate=:modifiedDate,ModifiedBy=:modifiedBy where branch_store_id=:stockStoreId and partBranch_id=:partBranchId";
			session = sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();

			query = session.createSQLQuery(sqlQuery);
		
			query.setParameter("currentStock",OnHandQty);
			query.setParameter("currentStockAmount",currentStockAmount);
			query.setParameter("modifiedDate",LocalDateTime.now());
			query.setParameter("modifiedBy", userCode);
			query.setParameter("stockStoreId",isFor.equalsIgnoreCase("Addition")?model.getStockStoreId():stockStoreId);
			query.setParameter("partBranchId",isFor.equalsIgnoreCase("Addition")?model.getPartBranchId():partModel.getPartBranchId());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			 updatedRowCount = query.executeUpdate();
			 if(updatedRowCount>=1)
			 {
				 stockStoreStatus=1;

			 }
	         System.out.println("isSuccess "+updatedRowCount);
	        transaction.commit();
//			 stockStoreStatus=1;
	        isSuccess=true;
	        System.out.println("StockStore Updated Successfully");
		}catch(Exception e)
		{
			stockStoreStatus=0;
			e.printStackTrace();
		}
	
		
		return stockStoreStatus;
	}	
	// end 
	
	
	
	
	

	private int updateReturnPartStock(List<UpdatePartDetailResponse> listDetail, String userCode,
			BigInteger branchId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.."+userCode +" "+branchId +" ");
		}
		int updatedRowCount=0;
		Session session = null;
		String grnNumber = null;
		Query query = null;
	    boolean isSuccess=false;
		try
		{
			for(UpdatePartDetailResponse detail: listDetail)
			{
			
			String sqlQuery = "update PA_STOCK_BIN_DTL set RecieptQty=:recieptQty,AvlbQty=:avlbQty where branchId=:branchId and partBranchId:partBranchId";
			session = sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();

			query = session.createSQLQuery(sqlQuery);
			query.setParameter("recieptQty",detail.getRecieptQuantity());
			query.setParameter("avlbQty", detail.getAvlbQty());
			query.setParameter("avlbQty", detail.getAvlbQty());
			query.setParameter("branchId", detail.getAvlbQty());
			query.setParameter("partBranchId", detail.getPartBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
	         updatedRowCount = query.executeUpdate();
	        
	        transaction.commit();
	        
	        System.out.println("StockBinDtl Updated Successfully");
	        
	        

			
			

			}
			
		
		}catch (SQLGrammarException exp) {
			
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
	
		return updatedRowCount;
	}

	@Override
	public List<SpareInvoiceRetunSearchModel> searchInvoiceReturnDao(String searchType, String searchText, String userCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.."+searchType +" "+searchText +" "+userCode);
		}
		
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Session session = null;
		String grnNumber = null;
		List<String> searchList = null;
		List<SpareInvoiceRetunSearchModel> invoiceReturnList=null;
		SpareInvoiceRetunSearchModel model=null;
		System.out.println("looking out for the invoiceNoGrnList"+searchType +" searchText "+searchText+ "userCode "+userCode);
		Query query = null;
		String sqlQuery = "exec [Invoice_Return_Grn_search_List] :searchText, :searchType,:userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("searchType", searchType);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			invoiceReturnList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model= new SpareInvoiceRetunSearchModel();
					String partyCode=(String)row.get("PartyCode");
				//	model.setPartyCode(partyCode!=null?partyCode:"");
					String partyName=(String)row.get("customerName");
					model.setPartyName(partyName!=null?partyName:"");
//					BigInteger mrnHdrid= (BigInteger) row.get("mrn_hdr_id");
//					model.setMrnHd(mrnHdrid!=null?mrnHdrid:mrnHdrid.ZERO);
					String branchName=(String) row.get("branchName");
					model.setBranchName(branchName!=null?branchName:"");
					String claimgenerationNumber=(String)row.get("CLAIM_ID");
				//	model.setClaimId(claimgenerationNumber!=null?claimgenerationNumber:null);
					Date claimDate=(Date) row.get("ClaimDate");
					String formattedClaimDate=null;
					Date parsedCLaimDate=null;
					if(claimDate!=null)
					{
						 formattedClaimDate = outputDateFormat.format(claimDate);
						 System.out.println("formattedClaimDate "+formattedClaimDate);
						 parsedCLaimDate = outputDateFormat.parse(formattedClaimDate);
					}
					System.out.println("parsedClaim "+parsedCLaimDate);
				//	model.setClaimDate(formattedClaimDate!=null?formattedClaimDate:"");
					String mrnNumber=(String) row.get("grn_id");
				//	model.setGrnId(mrnNumber!=null?mrnNumber:"");
					Date mrnDate=(Date) row.get("MRNDate");
					Date parsedmrnDate=null;
					String formattedMrnDate=null;
					if(mrnDate!=null)
					{
						 formattedMrnDate = outputDateFormat.format(mrnDate);
						System.out.println("formattedMrnDate"+formattedMrnDate);
						parsedmrnDate =outputDateFormat.parse(formattedMrnDate);
					}
					System.out.println("mrnParsed "+parsedmrnDate);
					model.setMrnDate(formattedMrnDate!=null?formattedMrnDate:"");
					String claimType=(String) row.get("ClaimType");
					model.setClaimType(claimType!=null?claimType:"");
					BigDecimal claimValue=(BigDecimal) row.get("ClaimValue");
					model.setClaimValue(claimValue!=null?claimValue:null);
					String invoiceNo =(String) row.get("invoiceNo");
					model.setInvoiceNo(invoiceNo!=null?invoiceNo:"");
					Date invoiceDate=(Date) row.get("InvoiceDate");
					Date parsedInvDate=null;
					String formattedInvDate=null;
					String agreeStatus=(String)(String) row.get("agreeStatus");
					System.out.println("We got agree Status "+agreeStatus);
					
					if(invoiceDate!=null)
					{
						formattedInvDate = outputDateFormat.format(invoiceDate);

						parsedInvDate = outputDateFormat.parse(formattedInvDate);
						
					}

					model.setInvoiceDate(formattedInvDate!=null?formattedInvDate:null);

					String status=(String) row.get("Status");
					System.out.println("status we get "+status);
					model.setStatus(status);
					if(claimgenerationNumber==null|| claimgenerationNumber.equals(""))
					{
						model.setAction("Go for approval");
						model.setStatus(status!=null?status:"");
						model.setAgreeStatus(agreeStatus!=null ?agreeStatus:null);



					}
					System.out.println("after set status "+model.getStatus());
					invoiceReturnList.add(model);

		
				}
			}
			
			//System.out.println("before return "+invoiceReturnList);
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return invoiceReturnList;

	}

	@Override
	public InvoiceReturnApproveStatus updateInvoiceReturnStatus(InvoiceReturnRequestModel request, String userCode) {
		logger.info("updatePartyMasterActiveStatus is invoked .... "+ request);
		int allTableStatus=0;
		InvoiceReturnApproveStatus status= new InvoiceReturnApproveStatus();
		String grnId=request.getHeader().getGrnNumber();
		String status1=request.getHeader().getInvoiceReturnStatus();
		Integer approveDataStatus=0;
		
	    try
	    {
	    	Session session= sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        String hql=null;
	       if(status1.equalsIgnoreCase("Approved"))
  	       {
  		       hql = "update PA_INV_RETURN_HDR SET Status = :grnStatus WHERE GRN_ID = :grnId";

  	       }
  	       if(status1.equalsIgnoreCase("Rejected"))
  	       {
  		       hql = "update PA_INV_RETURN_HDR SET Status = :grnStatus WHERE GRN_ID = :grnId";

  	       }
  	       
  	       
  	       
  	       if(status1.equalsIgnoreCase("Agree"))
  	       {
  		       hql = "update PA_INV_RETURN_HDR SET agreeStatus = :grnStatus WHERE GRN_ID = :grnId";

  	    	   
  	       }

	       	if(status1.equalsIgnoreCase("Approved"))
	        {
	        	
	        	allTableStatus=updatePartDetailByPartId(request,userCode);
	        	if(allTableStatus>=1)
	        	{
	        			      	        
	      	        Query query = session.createSQLQuery(hql);
	      	        query.setParameter("grnStatus",status1);
	      	        query.setParameter("grnId",grnId);
	      	        System.out.print("at here we get Value hql "+hql);
	      	        int updatedRowCount = query.executeUpdate();
	      	        transaction.commit();
	      	        if(updatedRowCount>=1) {
	      	        	
	      	        	approveDataStatus=updatePartyApproval(request,userCode);
	      	        }
	      	        
	        		status.setStatusCode(200);
			        status.setStatusMessagel("Successfully updated detail for GrnId "+request.getHeader().getGrnNumber());
	        	}
	        	else
	        	{
	        		status.setStatusCode(302);
			        status.setStatusMessagel("Status not updated for grnId "+request.getHeader().getGrnNumber());
	        	}
	        }
	       
	        else if(status1.equalsIgnoreCase("Rejected") )
	        {
	        	 Query query = session.createSQLQuery(hql);
	      	        query.setParameter("grnStatus",status1);
	      	        query.setParameter("grnId",grnId);
	      	        System.out.print("at here we get Value hql "+hql);

	      	        
	      	        int updatedRowCount = query.executeUpdate();
	      	        
	      	        transaction.commit();
	      	        if(updatedRowCount>=1)
	      	        {
	      	        	status.setStatusCode(200);
	    		        status.setStatusMessagel("status updated  successfully "+request.getHeader().getGrnNumber());
	      	        }
	      	        else
	      	        {
	      	        	status.setStatusCode(302);
	    		        status.setStatusMessagel("status Not Updated  successfully "+request.getHeader().getGrnNumber());
	      	        }
	        	
	        }
//	        else
//	        {
//	        	status.setStatusCode(302);
//		        status.setStatusMessagel("Fail to update Status for grnId "+request.getHeader().getGrnNumber());
//	        }

	    	
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    	status.setStatusCode(302);
	        status.setStatusMessagel("Fail to update Status with grn id "+request.getHeader().getGrnNumber());
	    }
		
		return status;

	}

	
	private Integer updatePartyApproval(InvoiceReturnRequestModel request, String userCode) {
		
		
		Integer invoiceRetunStatus=0;
	  logger.info("updatePartyApproval is invoked .... "+request);
	   // PartyMasterChangeStatusResponse status= new PartyMasterChangeStatusResponse();
	    try
	    {
	    	Session session= sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        String hql = "exec UPDATE_INVOICE_RETURN_APPROVAL :inv_ret_id,:Status,:userCode,:rejectedFlag,:hoUserId,:remarks";
	        Query query = session.createSQLQuery(hql);
	        query.setParameter("inv_ret_id",request.getHeader().getId());
	        query.setParameter("Status", request.getHeader().getInvoiceReturnStatus());
	        query.setParameter("userCode",userCode);
	        query.setParameter("rejectedFlag",request.getHeader().getInvoiceReturnStatus().equalsIgnoreCase("Approved")?'N':'Y');
	        query.setParameter("hoUserId", request.getHoUserId());
	        query.setParameter("remarks", request.getHeader().getInvoiceReturnStatus());
	        query.executeUpdate();
			transaction.commit();
			invoiceRetunStatus=1;
			
	        
	        System.out.println("Updated " + invoiceRetunStatus + " rows.");

	    	
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    	
	    }
	    
		return invoiceRetunStatus;

}

	@Override
	public InvoiceReturnSearchResponse searchInvoiceReturnByDateDao(SearchInvoiceReturnRequest request,
			String userCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.."+request +"   "+userCode);
		}
		InvoiceReturnSearchResponse response = new InvoiceReturnSearchResponse();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Session session = null;
		Integer totalRecord=0;
		String grnNumber = null;
		List<String> searchList = null;
		Integer totalRecords=0;
		List<SpareInvoiceRetunSearchModel> invoiceReturnList=null;
		SpareInvoiceRetunSearchModel model=null;
		//System.out.println("looking out for the invoiceNoGrnList"+fromDate +" searchText "+toDate+ "userCode "+userCode);
		Query query = null;
		String sqlQuery = "exec [Invoice_return_date_filter] :fromDate,:toDate,:userCode,:grnNo,:claimNo,:invReturnId, :page,:size";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("fromDate", request.getFromDate());
			query.setParameter("toDate", request.getToDate());
			query.setParameter("userCode", userCode);
			query.setParameter("grnNo", request.getMrnNo());
			query.setParameter("claimNo", request.getClaimNo());
			query.setParameter("invReturnId", request.getInvoiceReturnId());
			query.setParameter("page", request.getPage());
			query.setParameter("size", request.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			invoiceReturnList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model= new SpareInvoiceRetunSearchModel();
					String partyCode=(String)row.get("partyCode");
				//	model.setPartyCode(partyCode!=null?partyCode:"");
					String partyName=(String)row.get("customerName");
					model.setPartyName(partyName!=null?partyName:"");
//					BigInteger mrnHdrid= (BigInteger) row.get("mrn_hdr_id");
//					model.setMrnHd(mrnHdrid!=null?mrnHdrid:mrnHdrid.ZERO);
					String branchName=(String) row.get("branchName");
					model.setBranchName(branchName!=null?branchName:"");
					String claimgenerationNumber=(String)row.get("CLAIM_ID");
					String invoiceReturnNo=(String) row.get("INV_RET_NO");
					String invoiceRetDate=(String)row.get("INV_RET_DATE");
					System.out.println("claimGenerationNumber "+claimgenerationNumber);
				//	model.setClaimId(claimgenerationNumber!=null?claimgenerationNumber:"");
					Date claimDate=(Date) row.get("ClaimDate");
					String formattedClaimDate=null;
					Date parsedCLaimDate=null;
					if(claimDate!=null)
					{
						 formattedClaimDate = outputDateFormat.format(claimDate);
						 System.out.println("formattedClaimDate "+formattedClaimDate);
						 parsedCLaimDate = outputDateFormat.parse(formattedClaimDate);
					}
					System.out.println("parsedClaim "+parsedCLaimDate);
				//	model.setClaimDate(formattedClaimDate!=null?formattedClaimDate:"");
					model.setInvoiceReturnNo(invoiceReturnNo);
					model.setInvoiceReturnDate(invoiceRetDate);
					String mrnNumber=(String) row.get("grn_id");
					model.setGrnId(mrnNumber!=null?mrnNumber:"");
					model.setNetAmountSum((BigDecimal)row.get("BASIC_AMOUNT"));
					model.setTotalgstSum((BigDecimal)row.get("GST_AMOUNT"));
					model.setTotalAmount((BigDecimal)row.get("TOTAL_AMOUNT"));
					model.setTotalVorCharge((BigDecimal)row.get("TOTAL_VOR_CHARGE"));
					
					model.setId((Integer)row.get("ID"));
					Date mrnDate=(Date) row.get("MRNDate");
					Date parsedmrnDate=null;
					String formattedMrnDate=null;
					if(mrnDate!=null)
					{
						 formattedMrnDate = outputDateFormat.format(mrnDate);
						System.out.println("formattedMrnDate"+formattedMrnDate);
						parsedmrnDate =outputDateFormat.parse(formattedMrnDate);
					}
					System.out.println("mrnParsed "+parsedmrnDate);
					model.setMrnDate(formattedMrnDate!=null?formattedMrnDate:"");
					String claimType=(String) row.get("ClaimType");
					model.setClaimType(claimType!=null?claimType:"");
					BigDecimal claimValue=(BigDecimal) row.get("claimvalue");
					model.setClaimValue(claimValue!=null?claimValue:claimValue.ZERO);
					String invoiceNo =(String) row.get("InvoiceNo");
					totalRecords=(int) row.get("totalRecords");
					logger.info("total Records we get "+totalRecords);
					model.setInvoiceNo(invoiceNo!=null?invoiceNo:"");
					Date invoiceDate=(Date) row.get("InvoiceDate");
					Date parsedInvDate=null;
					
					String formattedInvDate=null;
					if(invoiceDate!=null)
					{
						formattedInvDate = outputDateFormat.format(invoiceDate);

						parsedInvDate = outputDateFormat.parse(formattedInvDate);
						
					}

					model.setInvoiceDate(formattedInvDate!=null?formattedInvDate:null);
					String agreeStatus=(String)row.get("agreeStatus");
					
					String status=(String) row.get("Status");
					model.setAction((String)row.get("Action"));
					System.out.println("status we get "+status);
					model.setStatus(status);
//					if(status !=null && status.equalsIgnoreCase("Waiting for Approval"))
//					{
//						 System.out.println("In if condition "+status);
//						model.setAction("Go for approval");
//					}
					
					
//					if(claimgenerationNumber=="0" || claimgenerationNumber=="")
//					{
//						model.setAction("Go for approval");
//						model.setStatus(status!=null?status:"");
//						model.setAgreeStatus(agreeStatus!=null?agreeStatus:null);
//
//
//					}
					System.out.println("after set status "+model.getStatus());
					response.setTotalRecord(totalRecords);
					invoiceReturnList.add(model);
					response.setMessage("successfully Fetched ");
					response.setStatusCode(200);

		
				}
				
				response.setSearchList(invoiceReturnList);
				response.setTotalRecord(totalRecords);
			}
			
			//System.out.println("before return "+invoiceReturnList);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(302);
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return response;
	}

	@Override
	public UploadDisagreeDocumentModel uploadDisagreeDocument(UploadDisagreeDocumentModel request, String userCode) {
		 //	String filePath="";
		 	String document1Name = null;
		 	String fileSaveStatus="";
			String property = System.getProperty("os.name");
			String hql=null;
			UploadDisagreeDocumentModel response = new UploadDisagreeDocumentModel();
			
			logger.info("file save invoked ......"+request.getGrnId());
			try {
				Session session= sessionFactory.openSession();
		        Transaction transaction = session.beginTransaction();
		       if(request.getIsFor().equalsIgnoreCase("disagree"))
		       {
		    	   
		    	   if (!request.getDocument1().isEmpty()) {
						File uploadDirectory = new File(uploadDir);

						// Create the upload directory if it doesn't exist
						if (!uploadDirectory.exists()) {
							uploadDirectory.mkdirs();
						}
						
						File idDirectory = new File(uploadDirectory, request.getGrnId().toString());
			            if (!idDirectory.exists()) {
			                idDirectory.mkdirs();
			            }

						//grnId

						String filePath=idDirectory + File.separator + request.getDocument1().getOriginalFilename();
						documentName = request.getDocument1().getOriginalFilename();
						System.out.println("document Name in filesave"+request.getDocument1().getOriginalFilename());
						File dest = new File(filePath);
						request.getDocument1().transferTo(dest);
						System.out.println("after save success");
						hql= "update PA_INV_RETURN_HDR SET disagreeDocument = :document1,agreeStatus='disAgree' ,status='Waiting for Approval'  WHERE GRN_ID = :grnId";


		    	   	}
		       }
		       else
	    	   {
					hql= "update PA_INV_RETURN_HDR SET disagreeDocument=:document1,agreeStatus='agree'  WHERE GRN_ID = :grnId";

	    	   }
	    	   Query query = session.createSQLQuery(hql);
	    	   query.setParameter("document1",documentName);
	    	   query.setParameter("grnId",request.getGrnId() );
	    	   int updatedRowCount = query.executeUpdate();
				        
				        transaction.commit();
				        if(updatedRowCount>=1)
				        {
				        	response.setStatusCode(200);
				        	response.setStatusMessage("Successfully updated");
				        }
				        else
				        {
				        	response.setStatusCode(302);
				        	response.setStatusMessage("Fail to update Status");
				        }
				        
				        
				       // status.setStatusMassage("Successfully updated");
				        
				        System.out.println("Updated " + updatedRowCount + " rows.");

					
					

				

			} catch (Exception e) {
				response.setStatusCode(302);
	        	response.setStatusMessage("Fail to update disAgreeStatus");			}
			return response;
		
	}

	@Override
	public InvoiceReturnSearchResponse invoiceReturnViewDetail(SearchInvoiceReturnRequest request, String userCode) {
		logger.info("invoiceReturnViewDetail exec =>{}");
		InvoiceReturnSearchResponse response = new InvoiceReturnSearchResponse();
		SpareInvoiceRetunSearchModel invoiceReturnHeader;
		List<SpareInvoiceRetunSearchModel> searchList= new ArrayList<>();
		List<InvoiceReturnParts> partList;
		Integer totalRecord;
		BigDecimal totalBasicAmount=BigDecimal.ZERO;
		BigDecimal totalGstAmount=BigDecimal.ZERO;
		BigDecimal totalAmount=BigDecimal.ZERO;
		BigDecimal totalVorCharge=BigDecimal.ZERO;
		try
		{
			invoiceReturnHeader=getInvoiceReturnHeader(request,1,userCode);
			searchList.add(invoiceReturnHeader);
			if(request.getReturnType().trim().equalsIgnoreCase("Part Return to VST")) {
				partList=getInvoicePartList(request,3,userCode);
			}else {
				partList=getInvoicePartList(request,2,userCode);
			}
			
			response.setSearchList(searchList);
			for(InvoiceReturnParts part:partList)
			{
				System.out.println("parts "+part.toString());
				BigDecimal unitPrice=part.getUnitPrice();
				BigDecimal gstValue=part.getGST_AMOUNT();
				BigDecimal totalValue=part.getTotalValue();
				BigDecimal vorCharge=part.getVorCharge();
				
				System.out.println("unitPrice "+unitPrice);
				System.out.println("gstValue "+gstValue);
				System.out.println("totalValue "+totalValue);
				
				if(unitPrice!=null)
				{
					totalBasicAmount=totalBasicAmount.add(unitPrice);

				}

				System.out.println("totalBasicAmount "+totalBasicAmount);
				
				if(gstValue!=null)
				{
					totalGstAmount=totalGstAmount.add(gstValue);

				}
				System.out.println("totalGstAmount "+totalGstAmount);

				if(totalValue!=null)
				{
					totalAmount=totalAmount.add(totalValue);

				}
				if(vorCharge!=null) {
					totalVorCharge=totalVorCharge.add(vorCharge);
				}
				System.out.println("totalAmount "+totalAmount);

				
//				if(part.getUnitPrice()!=null) {
//					totalBasicAmount.add(part.getUnitPrice());
//				}
//				if(part.getGST_AMOUNT()!=null) {
//					
//					totalGstAmount.add(part.getGST_AMOUNT());
//				}
//				if(part.getTotalValue()!=null) {
//					totalAmount.add(part.getTotalValue());
//				}
			}
			response.setPartList(partList);
			response.setTotalBasicAmount(totalBasicAmount);
			response.setTotalGstAmount(totalGstAmount);
			response.setTotalAmount(totalAmount);
			response.setTotalVorCharges(totalVorCharge);
			
		}catch(Exception e)
		{
			response.setStatusCode(302);
			response.setMessage("Data Not found "+e.getMessage());
			e.printStackTrace();
		}
		response.setStatusCode(200);
		response.setMessage("Data Return successfully ");
		return response;
	}

	private List<InvoiceReturnParts> getInvoicePartList(SearchInvoiceReturnRequest request, int i, String userCode) {
		SpareInvoiceRetunSearchModel response= null;
		System.out.println("getPartDetail Invoked {}");
		List<InvoiceReturnParts> partList=null;
	//	InvoiceReturnParts res=null;
		Session session = null;
		Query query = null;
		String sqlQuery = "exec [spare_sales_invoice_return_view] :MRNNumber,:invoiceReturnNo,:UserCode,:ClaimGenerationNumber,:ReturnType,:flag";

		try
		{
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("MRNNumber",request.getMrnNo());
			query.setParameter("invoiceReturnNo", request.getInvoiceReturnNo());
			query.setParameter("UserCode",userCode);
			query.setParameter("ClaimGenerationNumber",null);
			query.setParameter("ReturnType",null);
			query.setParameter("flag",i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			partList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					InvoiceReturnParts res=new InvoiceReturnParts();
					Map row = (Map) object;
					 Integer partId=(Integer) row.get("PARTID");
					String partNumber=(String) row.get("PartNumber");
					String partDesc=(String) row.get("PartDesc");
					Integer invoiceQty=(Integer) row.get("INVOICE_QTY");
					Integer currentStock=(Integer) row.get("currentStock");
					Date claimDate = (Date) row.get("CreatedDate");
					String claimDate1=null;
					if(claimDate!=null)
					{
						 claimDate1 = dateFormat.format(claimDate);

					}
					
					String remarks =(String) row.get("ClaimRemarks");
					String claimtype=(String)row.get("ClaimType");
					System.out.println("bbbb " + claimDate);
					Integer claimQty=(Integer) row.get("CLAIM_QTY");
					BigDecimal unitPrice=(BigDecimal) row.get("UNIT_PRICE");
					BigDecimal Discount=(BigDecimal) row.get("DISCOUNT");
					BigDecimal accessableAmount=(BigDecimal) row.get("ACCESSABLE_AMOUNT");
					BigDecimal gstAmount=(BigDecimal) row.get("GST_AMOUNT");
					BigDecimal totalValue=(BigDecimal) row.get("TOTAL_VALUE");
					BigDecimal recieptQuantity=(BigDecimal) row.get("RECIEPT_QTY");
					String returnType = (String)row.get("RETURN_TYPE_ID");
					if(returnType!=null && returnType.equalsIgnoreCase("VST - WITHOUT REFRENCE")) {
						res.setPartId(partId);
						res.setPartNo(partNumber);
						res.setCurrentStock(currentStock!=null?currentStock:0);
						res.setPartDescription(partDesc);
						res.setPartCategory((String)row.get("partCategory"));
						res.setReturnQty((Integer)row.get("returnQty"));
						res.setBasicUnitPrice((BigDecimal)row.get("basicUnitPrice"));
						res.setStore((String)row.get("store"));
						res.setBinLocation((String)row.get("binLocation"));
						res.setPrice((BigDecimal)row.get("price"));
						res.setVorCharge((BigDecimal)row.get("vorCharge"));
						res.setGstPercent((BigDecimal)row.get("gstPercent"));
						res.setGstTotal((BigDecimal)row.get("GST"));
						
						res.setBasicAmount((BigDecimal)row.get("basicAmount"));
						res.setTotalGST((BigDecimal)row.get("GST"));
						res.setTotalAmount((BigDecimal)row.get("total"));
						res.setReturnType(returnType);
						
					}else {
					res.setPartId(partId);
					res.setPartNo(partNumber);
					res.setCurrentStock(currentStock!=null?currentStock:0);
					res.setPartDescription(partDesc);
					res.setInvoiceQty(invoiceQty);
					res.setClaimQty(claimQty);
					res.setUnitPrice(unitPrice);
					res.setVorCharge((BigDecimal)row.get("vorcharges"));
					res.setDiscount(Discount);
					res.setAccessableAmount(accessableAmount);
					res.setGST_AMOUNT(gstAmount);
					res.setRecieptQuantity(recieptQuantity);
					res.setTotalValue(totalValue);
					res.setClaimtype(claimtype);
					res.setClaimDate(claimDate1!=null?claimDate1:null);
					res.setPartBranchId((BigInteger)row.get("partBranchId"));
					res.setStockBinId((BigInteger) row.get("stock_bin_id"));
					res.setBranchStoreId((Integer)row.get("branch_store_id"));
					res.setRemarks(remarks);
					}
					//Vst without Reference
					
				
				
				
					
					partList.add(res);
				}
				
			}
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			if (session.isOpen())
				session.close();
		}
		
		//System.out.println("before return parts "+partList);
		return partList;
	}

	

	private SpareInvoiceRetunSearchModel getInvoiceReturnHeader(SearchInvoiceReturnRequest request, int i,
			String userCode) {
		
		System.out.println("getInvoiceReturnHeader invoked {}");
		SpareInvoiceRetunSearchModel response= null;
		Session session = null;
		String grnDate1=null;
		String invoiceDate1 = null;
		String claimDate1 = null;
		Query query = null;
		String sqlQuery = "exec spare_sales_invoice_return_view :MRNNumber,:invoiceReturnNo, :UserCode,:ClaimGenerationNumber,:ReturnType,:flag";

		try
		{
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("MRNNumber",request.getMrnNo());
			query.setParameter("invoiceReturnNo",request.getInvoiceReturnNo());
			query.setParameter("UserCode",userCode);
			query.setParameter("ClaimGenerationNumber",null);
			query.setParameter("ReturnType",null);
			query.setParameter("flag",i);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					response=new SpareInvoiceRetunSearchModel();
					Map row = (Map) object;
					String invoiceReturnNo=(String)row.get("INV_RET_NO");
					Integer Id=(Integer) row.get("ID");
					 String grnId=(String) row.get("GRN_ID");
					String invoiceReturnDate=(String) row.get("INV_RET_DATE");
					Date grnDate=(Date)row.get("MRNDate");
					if(grnDate!=null) {
						 grnDate1=dateFormat.format(grnDate);
					}
					
					//String grnDate=(String) row.get("MRNDate");
					String grnType=(String) row.get("Grn_type");
					String returnType=(String)row.get("RETURN_TYPE_ID");
					
					Date invoiceDate = (Date) row.get("InvoiceDate");
					if(invoiceDate!=null) {
						 invoiceDate1 = dateFormat.format(invoiceDate);
					}
					
					System.out.println("bbbb " + invoiceDate1);
					String invoiceNo=(String) row.get("invoiceNo");
					String claimType=(String) row.get("ClaimType");
					Date claimDate = (Date) row.get("InvoiceDate");
					String claimId=(String)row.get("CLAIM_ID");
					if(invoiceDate!=null) {
						 claimDate1 = dateFormat.format(invoiceDate);
					}
					
					String status=(String) row.get("Status");
					String agreeStatus=(String) row.get("agreeStatus");
					String disagreeImage=(String)row.get("disagreeDocument");
					System.out.println("claimDate1"+claimDate1);
					BigDecimal netAmountSum=(BigDecimal)row.get("netAmountSum");
					BigDecimal totalGstSum=(BigDecimal)row.get("totalGstSum");
					BigDecimal totalValue=(BigDecimal)row.get("totalSum");
					BigDecimal totalVorCharge =(BigDecimal) row.get("totalVorCharges");
					//String claimDate=(String) row.get("claimDate");
					
					String claimRemarks=(String) row.get("claimRemarks");
					response.setId(Id);
					response.setBranchId((Integer)row.get("BRANCH_ID"));
					response.setPartyId((BigInteger) row.get("party_id"));
					response.setInvoiceReturnNo(invoiceReturnNo);
					response.setGrnId(grnId);
					response.setInvoiceReturnDate(invoiceReturnDate);
					response.setMrnDate(grnDate1!=null?grnDate1:null);
					response.setBranchName((String) row.get("branchname"));
					response.setGrnType(grnType);
					response.setInvoiceNo(invoiceNo);
					response.setReturnType(returnType);
					response.setStatus(status);
					response.setAgreeStatus(agreeStatus);
					response.setBranchStoreId((Integer)row.get("branch_store_id"));
					response.setStockBinId((BigInteger)row.get("stock_bin_id"));
					response.setDisagreeImage(disagreeImage);
					response.setInvoiceDate(invoiceDate1 != null ? invoiceDate1 : null);

					//response.setInvoiceDate(invoiceDate);
					response.setClaimType(claimType);
					response.setClaimId(claimId);
					response.setClaimDate((String)row.get("claimDate"));
					response.setClaimRemarks(claimRemarks);
					response.setNetAmountSum(netAmountSum);
					response.setTotalgstSum(totalGstSum);
					response.setTotalAmount(totalValue);
					response.setTotalVorCharge(totalVorCharge);
				}
				
			}
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			if (session.isOpen())
				session.close();
		}
		
		//System.out.println("before return header"+response);
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
	public JasperPrint invoiceReturnExcelReport(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jasperName;
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
	public void printInvoiceExcelReport(JasperPrint jasperPrint, String format, String printStatus,
			OutputStream outputStream, String reportName) throws Exception {
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
	public List<partSearchResponseModel> fetchPartNumber(String userCode, String searchText) {
		
			if (logger.isDebugEnabled()) {
				logger.debug("fetchPartNo invoked.." + searchText);
			}
			Session session = null;
			Query query = null;
			List<partSearchResponseModel> responseModelList = null;
			String sqlQuery = "exec [SP_GET_PART_SEARCH_FOR_INV_RETURN] :SearchText, :UserCode";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("SearchText", searchText);
				query.setParameter("UserCode", userCode);
				
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					responseModelList = new ArrayList<partSearchResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						partSearchResponseModel responseModel = new partSearchResponseModel();
						responseModel.setPartId((Integer) row.get("part_id"));
						responseModel.setPartNo((String) row.get("partNumber"));
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

	@Override
	public VstWithoutRefPartDtl getInvoiceReturnPartDetails(String userCode, BigInteger partId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartId invoked.." + partId);
		}
		Session session = null;
		Query query = null;
		VstWithoutRefPartDtl responseModel = null;
		String sqlQuery = "exec [SP_GET_PART_DETAILS_FOR_INV_RETURN] :UserCode, :partId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("partId", partId);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new VstWithoutRefPartDtl();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartBranchId((Integer) row.get("partBranch_id"));
					responseModel.setBranchId((BigInteger) row.get("BRANCH_ID"));
					responseModel.setDealerId((BigInteger) row.get("dealer_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
					responseModel.setPartDescription((String) row.get("PartDesc")); 
					responseModel.setProductSubCat((String) row.get("ProductSubCategory"));
					responseModel.setCurrentStock((Integer) row.get("CurrentStock"));
					responseModel.setStore((String) row.get("StoreName"));
					responseModel.setIndividualBin((Integer) row.get("BinCount"));
					responseModel.setBinLocation((String) row.get("BinName"));
					responseModel.setBinId((String) row.get("BinId"));
					responseModel.setStockStoreId((String) row.get("stock_store_id"));
					responseModel.setBranchStoreId((String) row.get("branch_store_id"));
					responseModel.setBasicUnitPrice((BigDecimal) row.get("unitPrice"));
					responseModel.setPrice((BigDecimal) row.get("price"));
					responseModel.setGst((BigDecimal) row.get("gst"));
					responseModel.setGstPer((BigInteger) row.get("gstPercentage"));
					responseModel.setTotal((BigDecimal) row.get("total"));
					
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
		return responseModel;
	}
	
	@Override
	public List<PartyInvoiceList> fetchInvoiceReturnNumList(String userCode, InvoicePartNoRequest requestModel) {
		
		if (logger.isDebugEnabled()) {
			//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Session session = null;
		Query query = null;
		PartyInvoiceList bean=null;
		List<PartyInvoiceList> invNoList = null;
		try {
			session = sessionFactory.openSession();
			 String sqlQuery = "EXEC GET_INVOICE_RETURN_NO_LIST_BY_USER :USERCODE, :APRDOC";
			query = session.createSQLQuery(sqlQuery);
			 query.setParameter("USERCODE", userCode);
		     query.setParameter("APRDOC", requestModel.getSearchText().toUpperCase());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				invNoList = new ArrayList<PartyInvoiceList>();
				for (Object object : data) {
					  Map row = (Map) object;
		                bean = new PartyInvoiceList();
		                bean.setSaleInvoiceId((BigInteger) row.get("id"));  
		                bean.setInvoiceNumber((String) row.get("INV_RET_NO"));   
		               
		                 invNoList.add(bean);
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
		return invNoList;
	}

	
}

	
		


