package com.hitech.dms.web.service.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.invoice.WarrantyClaimInvoiceDao;
import com.hitech.dms.web.dao.pcr.pcrSearchDaoImpl;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwill;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwillApproval;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwillPhoto;
import com.hitech.dms.web.entity.invoice.WarrantyClaimInvoiceHdr;
import com.hitech.dms.web.entity.wcr.WarrantyWcrEntity;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchResponseDto;
import com.hitech.dms.web.model.invoice.CreditNoteSearchRequestDto;
import com.hitech.dms.web.model.invoice.CreditNoteSearchResponseDto;
import com.hitech.dms.web.model.invoice.CustomerDetailsDto;
import com.hitech.dms.web.model.invoice.InvoiceSearchRequestDto;
import com.hitech.dms.web.model.invoice.InvoiceSearchResponseDto;
import com.hitech.dms.web.model.invoice.InvoiceViewDto;
import com.hitech.dms.web.model.invoice.LabourDetailsDto;
import com.hitech.dms.web.model.invoice.PartsDetailsDto;
import com.hitech.dms.web.model.invoice.VechileDetailsDto;

/**
 * @author mahesh.kumar
 */
@Service
public class WarrantyClaimInvoiceServiceImp implements WarrantyClaimInvoiceService{
	
private static final Logger logger = LoggerFactory.getLogger(pcrSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private WarrantyClaimInvoiceDao warrantyClaimInvoiceDao;
	
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public ApiResponse<?> fetchInvoiceDetails(BigInteger wcrId, String userCode) 
	{		
		ApiResponse<InvoiceViewDto> apiResponse = new ApiResponse<>();
		
		Session session = null;
		InvoiceViewDto invoiceViewDto = new InvoiceViewDto();
		VechileDetailsDto vechileDetailsDto;
		CustomerDetailsDto customerDetailsDto;
	    List<PartsDetailsDto> partsDetailsDtoList;
	    List<LabourDetailsDto> labourDetailsList;	
	    List<LabourDetailsDto> outSideLabourDetailsList;
		
		List<?> data = null;
		
		try {
			session = sessionFactory.openSession();
			
			//For flag 1
			data = warrantyClaimInvoiceDao.fetchInvoiceDetails(session, wcrId, userCode, 1);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					invoiceViewDto.setId((BigInteger)row.get("id"));
					invoiceViewDto.setWcrNo((String)row.get("wcrNo"));
					invoiceViewDto.setWctDate((Date)row.get("wcrDate"));
					
				}
			}
			
			//For flag 2
			data = null;
			data = warrantyClaimInvoiceDao.fetchInvoiceDetails(session, wcrId, userCode, 2);
			
			if (data != null && !data.isEmpty()) {
				vechileDetailsDto = new VechileDetailsDto();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					vechileDetailsDto.setChassisNo((String)row.get("chassis_no"));
					vechileDetailsDto.setEngineNo((String)row.get("engine_no"));;
					vechileDetailsDto.setRegistrationNo((String)row.get("registration_number"));
					vechileDetailsDto.setModelVariant((String)row.get("modelVariant"));
					vechileDetailsDto.setVinNo((String)row.get("vin_no"));
//					vechileDetailsDto.setSaleDate((Date)row.get("saleDate"));
					
				}
				invoiceViewDto.setVechileDetailsDto(vechileDetailsDto);
			}
			
			//For flag 3
			data = null;
			data = warrantyClaimInvoiceDao.fetchInvoiceDetails(session, wcrId, userCode, 3);
			
			if (data != null && !data.isEmpty()) {
				customerDetailsDto = new CustomerDetailsDto();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					customerDetailsDto.setCustomerId((BigInteger)row.get("customerId"));
					customerDetailsDto.setCustomerType((String)row.get("CustomerType"));
					customerDetailsDto.setCustomerCode((String)row.get("customer_code"));;
					customerDetailsDto.setCustomerName((String)row.get("customer_name"));
					customerDetailsDto.setCustomerAddress((String)row.get("country"));
					customerDetailsDto.setState((String)row.get("state"));
					customerDetailsDto.setDistrict((String)row.get("district"));
					customerDetailsDto.setTehsil((String)row.get("tehsil"));
					customerDetailsDto.setCity((String)row.get("citydesc"));
					customerDetailsDto.setPincode((String)row.get("pincode"));
					
				}
				invoiceViewDto.setCustomerDetailsDto(customerDetailsDto);
			}
			
			//For flag 4
			data = null;
			data = warrantyClaimInvoiceDao.fetchInvoiceDetails(session, wcrId, userCode, 4);
			
			if (data != null && !data.isEmpty()) {
				partsDetailsDtoList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					PartsDetailsDto partsDetailsDto = new PartsDetailsDto();
					partsDetailsDto.setPartId((Integer)row.get("part_id"));
					partsDetailsDto.setPartNumber((String)row.get("PartNumber"));
					partsDetailsDto.setPartDesc((String)row.get("PartDesc"));
					partsDetailsDto.setUom((String)row.get("uom"));
					partsDetailsDto.setHsnCode((String)row.get("HSN_CODE"));
					partsDetailsDto.setUnitPrice((BigDecimal)row.get("unitPrice"));
					partsDetailsDto.setQty((BigDecimal)row.get("qty"));
					partsDetailsDto.setNetAmt((BigDecimal)row.get("Netamount"));
					partsDetailsDto.setCgstPer((BigDecimal)row.get("PartGSTPER"));
					partsDetailsDto.setCgstAmt((BigDecimal)row.get("PartGSTPERAMT"));
					partsDetailsDto.setSgstPer((BigDecimal)row.get("PartSGST"));
					partsDetailsDto.setSgstAmt((BigDecimal)row.get("PartSGSTAMT"));
					partsDetailsDto.setIgstPer((BigDecimal)row.get("PartIGSTPER"));
					partsDetailsDto.setIgstAmt((BigDecimal)row.get("PartIGSTPERAMT"));
					partsDetailsDto.setBillableTypeDesc((String)row.get("BillableTypeDesc"));
					
					partsDetailsDtoList.add(partsDetailsDto);
				}
				invoiceViewDto.setPartsDetailsDto(partsDetailsDtoList);
			}
			
			//For flag 5
			data = null;
			data = warrantyClaimInvoiceDao.fetchInvoiceDetails(session, wcrId, userCode, 5);
			
			if (data != null && !data.isEmpty()) {
				labourDetailsList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					LabourDetailsDto labourDetails = new LabourDetailsDto();
					labourDetails.setLabourId((Integer)row.get("labour_id"));
					labourDetails.setLabourCode((String)row.get("LabourCode"));
					labourDetails.setLabourDescription((String)row.get("LabourDesc"));
					labourDetails.setHsnCode((String)row.get("HSN_CODE"));
					labourDetails.setRate((BigDecimal)row.get("Rate"));
					labourDetails.setNetAmt((BigDecimal)row.get("netAmt"));
					labourDetails.setCgstPer((BigDecimal)row.get("LBRCGSTPER"));
					labourDetails.setCgstAmt((BigDecimal)row.get("LBRCGSTPERAMT"));
					labourDetails.setSgstPer((BigDecimal)row.get("LBRSGST"));
					labourDetails.setSgstAmt((BigDecimal)row.get("LBRSGSTAMT"));
					labourDetails.setIgstPer((BigDecimal)row.get("LBRIGSTPER"));
					labourDetails.setIgstAmt((BigDecimal)row.get("LBRIGSTPERAMT"));
					
					labourDetailsList.add(labourDetails);
				}
				invoiceViewDto.setLabourDetailsDto(labourDetailsList);
			}
			
			//For flag 6
			data = null;			
			data = warrantyClaimInvoiceDao.fetchInvoiceDetails(session, wcrId, userCode, 6);
			
			if (data != null && !data.isEmpty()) {
				outSideLabourDetailsList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					LabourDetailsDto outsideLabourDetails = new LabourDetailsDto();
					outsideLabourDetails.setLabourId((Integer)row.get("labour_id"));
					outsideLabourDetails.setLabourCode((String)row.get("LabourCode"));
					outsideLabourDetails.setLabourDescription((String)row.get("LabourDesc"));
					outsideLabourDetails.setHsnCode((String)row.get("HSN_CODE"));
					outsideLabourDetails.setRate((BigDecimal)row.get("Rate"));
					outsideLabourDetails.setHour((BigDecimal)row.get("hour"));
					outsideLabourDetails.setNetAmt((BigDecimal)row.get("netAmt"));
					outsideLabourDetails.setCgstPer((BigDecimal)row.get("LBRCGSTPER"));
					outsideLabourDetails.setCgstAmt((BigDecimal)row.get("LBRCGSTPERAMT"));
					outsideLabourDetails.setSgstPer((BigDecimal)row.get("LBRSGST"));
					outsideLabourDetails.setSgstAmt((BigDecimal)row.get("LBRSGSTAMT"));
					outsideLabourDetails.setIgstPer((BigDecimal)row.get("LBRIGSTPER"));
					outsideLabourDetails.setIgstAmt((BigDecimal)row.get("LBRIGSTPERAMT"));
					
					outSideLabourDetailsList.add(outsideLabourDetails);
				}
				invoiceViewDto.setOutsideLabourDetailsDto(outSideLabourDetailsList);
			}
			
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {

			if (invoiceViewDto != null) {
				apiResponse.setResult(invoiceViewDto);
				apiResponse.setMessage("Invoce Details Fetched Successfully.");
				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}

			if (session != null) {
				session.close();
			}
		}
		
		return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> createWCRInvoice(String authorizationHeader, String userCode, WarrantyClaimInvoiceHdr requestModel) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("createWCRInvoice invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<WarrantyClaimInvoiceHdr> response = new ApiResponse<>();
		String invoiceNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				invoiceNo = commonDao.getDocumentNumberById("WCI", branchId, session);
				commonDao.updateDocumentNumber("Warranty Claim Invoice",branchId, invoiceNo, session);
			
				
				if(requestModel.getFinalSubmitFlag() !=null && requestModel.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
					String sqlQuery = "update WA_CLAIM_INVOICE_HDR set customer_invoice_no=:custInvNo,customer_invoice_date=:custInvDate,final_submit=:finalSub where id =:idInv";
		            try {
		            	Query query = null;
		            	 query = session.createNativeQuery(sqlQuery);
		            	query.setParameter("custInvNo", requestModel.getCustomerInvoiceNo());
		    			query.setParameter("custInvDate", requestModel.getCustomerInvoiceDate());
		    			query.setParameter("finalSub", requestModel.getFinalSubmitFlag());
		    			query.setParameter("idInv", requestModel.getId());
		    			
		    			int k = query.executeUpdate();
		            }
		            catch (SQLGrammarException ex) {
		    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
		    			logger.error(this.getClass().getName(), ex);
		    		} catch (Exception ex) {
		    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
		    			logger.error(this.getClass().getName(), ex);
		    		}
				}else {
					requestModel.setInvoiceNo(invoiceNo);
					if(requestModel.getId() == null) {
						requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
						requestModel.setCreatedOn(new Date());
						requestModel.setInvoiceDate(new Date());
			        }
					else {
						requestModel.setLastModifiedBy((BigInteger)mapData.get("userId"));
						requestModel.setLastModifiedDate(new Date());
			        }
			        session.save(requestModel);
			        
			        //added by Mahesh.Kumar on 30-01-2024
			        WarrantyWcrEntity wcrEntity = session.get(WarrantyWcrEntity.class, requestModel.getWcrId().longValue());
			        
			        if(wcrEntity != null) {
			        	wcrEntity.setFinalStatus("Invoice Created");
			        	session.saveOrUpdate(wcrEntity);
			        }
				}
				
				
				
			} 
			
			//Committing the transaction if everything went well.
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
				if (requestModel != null) {
					response.setResult(requestModel);
					response.setMessage("Invoice Number Created Successfully.");
					response.setStatus(WebConstants.STATUS_CREATED_201);
				}
			} else {
				response.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return response;
	}
	
	
	@Override
	public ApiResponse<List<Map<String, Object>>> autoSearchInvoiceNo(String invoiceNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = warrantyClaimInvoiceDao.autoSearchInvoiceNo(session, invoiceNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("invoiceNo", row.get("invoiceNo"));
	                responseModelList.add(responseModel);
	            }

	            apiResponse.setResult(responseModelList); // Set the result in the ApiResponse
	        } else {
	            apiResponse.setMessage("No data found."); // Handle the case when no data is returned
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<Map<String, Object>>> autoSearchWcrNo(String wcrNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = warrantyClaimInvoiceDao.autoSearchWcrNo(session, wcrNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("wcrId"));
	                responseModel.put("wcrNo", row.get("wcrNo"));
	                responseModelList.add(responseModel);
	            }

	            apiResponse.setResult(responseModelList); // Set the result in the ApiResponse
	        } else {
	            apiResponse.setMessage("No data found."); // Handle the case when no data is returned
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	
	
	
	@Override
	public ApiResponse<List<InvoiceSearchResponseDto>> invoiceSearchList(String userCode, InvoiceSearchRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<InvoiceSearchResponseDto>> apiResponse = new ApiResponse<>();
	    
	    @SuppressWarnings("unused")
		Integer dataCount = 0;

	    try {
	        session = sessionFactory.openSession();

	        List<?> data = warrantyClaimInvoiceDao.invoiceSearchList(session, userCode, requestModel);

	        List<InvoiceSearchResponseDto> responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                InvoiceSearchResponseDto response = new InvoiceSearchResponseDto();
	                response.setAction("Edit");
	                response.setId((BigInteger) row.get("invoiceId"));
	                response.setInvoiceNo((String) row.get("invoiceNo"));
	                response.setInvoiceDate((Date) row.get("invoiceDate"));
	                response.setWcrNo((String)row.get("wcrNo"));
	                response.setWcrDate((Date)row.get("wcrDate"));
	                response.setDealerCode((String) row.get("DealerCode"));
	                response.setDealerName((String) row.get("DealerName"));
	                response.setBranch((String) row.get("BranchName"));
					response.setPcrNo((String) row.get("PCRNO"));
					response.setPcrDate((Date) row.get("PCRDATE"));
					response.setStatus((String) row.get("status"));
					response.setJobCardNo((String) row.get("JobCard_No"));
					response.setJobCardDate((Date) row.get("JobCard_Date"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setVinNo((String)row.get("vin_no"));
					response.setEngineNo((String)row.get("EngineNo"));
					response.setFinalSubmit("Y".equals((String)row.get("final_submit")) ? "Submitted" : "Pending");
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

	        apiResponse.setCount(dataCount);
	        apiResponse.setResult(responseList);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	         apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	
	@Override
	public ApiResponse<?> viewInvoice(BigInteger invoiceId, String userCode) 
	{		
		ApiResponse<InvoiceViewDto> apiResponse = new ApiResponse<>();
		
		Session session = null;
		InvoiceViewDto invoiceViewDto = new InvoiceViewDto();
		VechileDetailsDto vechileDetailsDto;
		CustomerDetailsDto customerDetailsDto;
	    List<PartsDetailsDto> partsDetailsDtoList;
	    List<LabourDetailsDto> labourDetailsList;	
	    List<LabourDetailsDto> outSideLabourDetailsList;
		
		List<?> data = null;
		
		try {
			session = sessionFactory.openSession();
			
			//For flag 1
			data = warrantyClaimInvoiceDao.viewInvoice(session, invoiceId, userCode, 1);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					invoiceViewDto.setId((BigInteger)row.get("id"));
					invoiceViewDto.setInvoiceNo((String)row.get("invoiceNo"));
					invoiceViewDto.setInvoiceDate((Date)row.get("invoiceDate"));
					invoiceViewDto.setWcrNo((String)row.get("wcrNo"));
					invoiceViewDto.setWctDate((Date)row.get("wcrDate"));
					
					invoiceViewDto.setMaterial((String)row.get("material"));
					invoiceViewDto.setUnitPrice((BigDecimal)row.get("unitPrice"));
					invoiceViewDto.setQuantity((BigDecimal)row.get("quantity"));
					invoiceViewDto.setAmount((BigDecimal)row.get("amount"));
					invoiceViewDto.setSac((String)row.get("sac"));
					invoiceViewDto.setTaxPercent((BigDecimal)row.get("tax"));
					invoiceViewDto.setTaxValue((BigDecimal)row.get("taxValue"));
					invoiceViewDto.setTotalValue((BigDecimal)row.get("totalValue"));
					
					invoiceViewDto.setBasePrice((BigDecimal)row.get("basePrice"));
					invoiceViewDto.setGstAmount((BigDecimal)row.get("gstAmount"));
					invoiceViewDto.setInvoiceAmount((BigDecimal)row.get("invoiceAmount"));
					invoiceViewDto.setCustomerInvoiceNo((String)row.get("customer_invoice_no"));
					invoiceViewDto.setCustomerInvoiceDate((String)row.get("customer_invoice_date"));					
					invoiceViewDto.setFinalSubmit((String)row.get("final_submit"));				
				}
			}
			
			//For flag 2
			data = null;
			data = warrantyClaimInvoiceDao.viewInvoice(session, invoiceId, userCode, 2);
			
			if (data != null && !data.isEmpty()) {
				vechileDetailsDto = new VechileDetailsDto();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					vechileDetailsDto.setChassisNo((String)row.get("chassis_no"));
					vechileDetailsDto.setEngineNo((String)row.get("engine_no"));;
					vechileDetailsDto.setRegistrationNo((String)row.get("registration_number"));
					vechileDetailsDto.setModelVariant((String)row.get("modelVariant"));
					vechileDetailsDto.setVinNo((String)row.get("vin_no"));
//					vechileDetailsDto.setSaleDate((Date)row.get("saleDate"));
					
				}
				invoiceViewDto.setVechileDetailsDto(vechileDetailsDto);
			}
			
			//For flag 3
			data = null;
			data = warrantyClaimInvoiceDao.viewInvoice(session, invoiceId, userCode, 3);
			
			if (data != null && !data.isEmpty()) {
				customerDetailsDto = new CustomerDetailsDto();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					customerDetailsDto.setCustomerId((BigInteger)row.get("customerId"));
					customerDetailsDto.setCustomerType((String)row.get("CustomerType"));
					customerDetailsDto.setCustomerCode((String)row.get("customer_code"));;
					customerDetailsDto.setCustomerName((String)row.get("customer_name"));
					customerDetailsDto.setCustomerAddress((String)row.get("country"));
					customerDetailsDto.setState((String)row.get("state"));
					customerDetailsDto.setDistrict((String)row.get("district"));
					customerDetailsDto.setTehsil((String)row.get("tehsil"));
					customerDetailsDto.setCity((String)row.get("citydesc"));
					customerDetailsDto.setPincode((String)row.get("pincode"));
					
				}
				invoiceViewDto.setCustomerDetailsDto(customerDetailsDto);
			}
			
			//For flag 4
			data = null;
			data = warrantyClaimInvoiceDao.viewInvoice(session, invoiceId, userCode, 4);
			
			if (data != null && !data.isEmpty()) {
				partsDetailsDtoList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					PartsDetailsDto partsDetailsDto = new PartsDetailsDto();
					partsDetailsDto.setPartId((Integer)row.get("part_id"));
					partsDetailsDto.setPartNumber((String)row.get("PartNumber"));
					partsDetailsDto.setPartDesc((String)row.get("PartDesc"));
					partsDetailsDto.setUom((String)row.get("uom"));
					partsDetailsDto.setHsnCode((String)row.get("HSN_CODE"));
					partsDetailsDto.setUnitPrice((BigDecimal)row.get("unitPrice"));
					partsDetailsDto.setQty((BigDecimal)row.get("qty"));
					partsDetailsDto.setNetAmt((BigDecimal)row.get("netAmt"));
					partsDetailsDto.setCgstPer((BigDecimal)row.get("PartGSTPER"));
					partsDetailsDto.setCgstAmt((BigDecimal)row.get("PartGSTPERAMT"));
					partsDetailsDto.setSgstPer((BigDecimal)row.get("PartSGST"));
					partsDetailsDto.setSgstAmt((BigDecimal)row.get("PartSGSTAMT"));
					partsDetailsDto.setIgstPer((BigDecimal)row.get("PartIGSTPER"));
					partsDetailsDto.setIgstAmt((BigDecimal)row.get("PartIGSTPERAMT"));
					partsDetailsDto.setTotalAmt((BigDecimal)row.get("totalAmt"));//added on 25-10-24
					partsDetailsDto.setBillableTypeDesc((String)row.get("billableType"));
					
					
					partsDetailsDtoList.add(partsDetailsDto);
				}
				invoiceViewDto.setPartsDetailsDto(partsDetailsDtoList);
			}
			
			//For flag 5
			data = null;
			data = warrantyClaimInvoiceDao.viewInvoice(session, invoiceId, userCode, 5);
			
			if (data != null && !data.isEmpty()) {
				labourDetailsList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					LabourDetailsDto labourDetails = new LabourDetailsDto();
					labourDetails.setLabourCode((String)row.get("LabourCode"));
					labourDetails.setLabourDescription((String)row.get("LabourDesc"));
					labourDetails.setHsnCode((String)row.get("HSN_CODE"));
					labourDetails.setRate((BigDecimal)row.get("Rate"));
					labourDetails.setNetAmt((BigDecimal)row.get("netAmt"));
					labourDetails.setCgstPer((BigDecimal)row.get("LBRCGSTPER"));
					labourDetails.setCgstAmt((BigDecimal)row.get("LBRCGSTPERAMT"));
					labourDetails.setSgstPer((BigDecimal)row.get("LBRSGST"));
					labourDetails.setSgstAmt((BigDecimal)row.get("LBRSGSTAMT"));
					labourDetails.setIgstPer((BigDecimal)row.get("LBRIGSTPER"));
					labourDetails.setIgstAmt((BigDecimal)row.get("LBRIGSTPERAMT"));
					labourDetails.setTotalAmt((BigDecimal)row.get("totalValue"));//added on 25-10-24
					
					labourDetailsList.add(labourDetails);
				}
				invoiceViewDto.setLabourDetailsDto(labourDetailsList);
			}
			
			//For flag 6
			data = null;			
			data = warrantyClaimInvoiceDao.viewInvoice(session, invoiceId, userCode, 6);
			
			if (data != null && !data.isEmpty()) {
				outSideLabourDetailsList = new ArrayList<>();
				
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					LabourDetailsDto outsideLabourDetails = new LabourDetailsDto();
					outsideLabourDetails.setLabourCode((String)row.get("LabourCode"));
					outsideLabourDetails.setLabourDescription((String)row.get("LabourDesc"));
					outsideLabourDetails.setHsnCode((String)row.get("HSN_CODE"));
					outsideLabourDetails.setRate((BigDecimal)row.get("Rate"));
					outsideLabourDetails.setNetAmt((BigDecimal)row.get("netAmt"));
					outsideLabourDetails.setCgstPer((BigDecimal)row.get("LBRCGSTPER"));
					outsideLabourDetails.setCgstAmt((BigDecimal)row.get("LBRCGSTPERAMT"));
					outsideLabourDetails.setSgstPer((BigDecimal)row.get("LBRSGST"));
					outsideLabourDetails.setSgstAmt((BigDecimal)row.get("LBRSGSTAMT"));
					outsideLabourDetails.setIgstPer((BigDecimal)row.get("LBRIGSTPER"));
					outsideLabourDetails.setIgstAmt((BigDecimal)row.get("LBRIGSTPERAMT"));
					outsideLabourDetails.setTotalAmt((BigDecimal)row.get("totalValue"));//added on 25-10-24
					
					outSideLabourDetailsList.add(outsideLabourDetails);
				}
				invoiceViewDto.setOutsideLabourDetailsDto(outSideLabourDetailsList);
			}
			
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {

			if (invoiceViewDto != null) {
				apiResponse.setResult(invoiceViewDto);
				apiResponse.setMessage("View Invoice Details Successfully.");
				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}

			if (session != null) {
				session.close();
			}
		}
		
		return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<CreditNoteSearchResponseDto>> creditNoteSearchList(String userCode, CreditNoteSearchRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<CreditNoteSearchResponseDto>> apiResponse = new ApiResponse<>();
	    
	    @SuppressWarnings("unused")
		Integer dataCount = 0;

	    try {
	        session = sessionFactory.openSession();

	        List<?> data = warrantyClaimInvoiceDao.creditNoteSearchList(session, userCode, requestModel);

	        List<CreditNoteSearchResponseDto> responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                CreditNoteSearchResponseDto response = new CreditNoteSearchResponseDto();
	                response.setAction((String)row.get("action"));
	                response.setId((BigInteger) row.get("invoiceId"));
	                response.setInvoiceNo((String) row.get("invoiceNo"));
	                response.setInvoiceDate((Date) row.get("invoiceDate"));
	                response.setWarrantyType((String)row.get("warrantyType"));
	                response.setWcrNo((String)row.get("wcrNo"));
	                response.setWcrDate((Date)row.get("wcrDate"));
					response.setPcrNo((String) row.get("PCRNO"));
					response.setPcrDate((Date) row.get("PCRDATE"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setVinNo((String)row.get("vin_no"));
					response.setEngineNo((String)row.get("EngineNo"));
					response.setRegistrationNo((String)row.get("registrationNo"));
					response.setInvoiceAmount((BigDecimal)row.get("invoiceAmount"));
					response.setCreditNoteNo((String)row.get("creditNoteNo"));
					response.setCreditNoteDate((Date)row.get("creditNoteDate"));
					response.setCreditNoteAmount((BigDecimal)row.get("creditNoteAmount"));;
	                response.setDealerCode((String) row.get("DealerCode"));
	                response.setDealerName((String) row.get("DealerName"));
	                response.setBranch((String) row.get("BranchName"));
	                dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

	        apiResponse.setCount(dataCount);
	        apiResponse.setResult(responseList);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	         apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

}
