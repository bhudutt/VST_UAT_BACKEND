package com.hitech.dms.web.serviceImpl.spare.paymentVoucher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.customer.order.SpareCustomerOrderDao;
import com.hitech.dms.web.dao.spare.paymentVoucher.PaymentVoucherDao;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderCreateResponseModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.CreatePayVoucherRequest;
import com.hitech.dms.web.model.spara.payment.voucher.request.FilterPaymentVoucherReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.PVpartyCodeRequestModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.PartyTypeReqest;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocForGrnInvReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocListRequest;
import com.hitech.dms.web.model.spara.payment.voucher.response.CardResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.ChequeDdResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.EWalletResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.GrnInvReferenceDocList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PVGrnAndInvoiceResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentBankList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherReponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.RtgsNeftImpsResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.SearchPaymentVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.viewPayVoucherResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.service.spare.paymentVoucher.PaymentVoucherService;

@Service
public class PaymentVoucherServiceImpl implements PaymentVoucherService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentVoucherServiceImpl.class);
	@Autowired
	private PaymentVoucherDao paymentVoucherDao;
	
	@Autowired
	private SpareCustomerOrderDao spareCustomerOrderDao;

	@Override
	public PayVoucherResponse savePayment(String authorizationHeader, String userCode,
			@Valid CreatePayVoucherRequest requestModel, Device device) {
		CustOrderProductCtgResponseModel response=null;
		if(requestModel.getPvTypeValueCode().equals("PAYMENT")) {
			response = spareCustomerOrderDao.getDocumentNumber("PAYV", requestModel.getBranchId());
		}
		if(requestModel.getPvTypeValueCode().equals("RECEIPT")) {
			 response = spareCustomerOrderDao.getDocumentNumber("RECV", requestModel.getBranchId());
		}
		
		if(response==null && response.getProductCtgNumber()==null) {
			PayVoucherResponse responseModel = new PayVoucherResponse();
			responseModel.setMsg("Unable to generate a DOC number Kindly inform admin.");
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			responseModel.setPaymentVoucherNumber(response.getProductCtgNumber());
			return responseModel;
		}
		requestModel.setDocNo(response.getProductCtgNumber());
		return paymentVoucherDao.savePayment(authorizationHeader, userCode, requestModel,device);
	}

	@Override
	public List<PaymentVoucherList> fetchPaymentReceiptList(String lookupTypeCode) {
		return paymentVoucherDao.fetchPaymentReceiptList(lookupTypeCode);
	}

	@Override
	public List<PaymentBankList> getBankCategory(String bankCode, String userCode) {
		return paymentVoucherDao.getBankCategory(bankCode,userCode);
	}

	@Override
	public List<PartyCategoryResponse> searchPartyTypeCategory(PartyTypeReqest requestModel, String userCode) {

	
		List<PartyCategoryResponse> finalList = new ArrayList<>(); 
		List<String> paymentDealer = Arrays.asList("CO-DEALER", "DISTRIBUTOR/STOCKIES/OEM", "SUPPLIER/VENDOR", "SUPLLIER / VENDOR", "COUNTER SALE CUSTOMER");
		List<String> receiptDealer = Arrays.asList("CO-DEALER", "COUNTER SALE CUSTOMER");
		List<String> paymentDistributor = Arrays.asList("CO-DISTRIBUTOR/STOCKIES/OEM", "SUPPLIER/VENDOR", "SUPLLIER / VENDOR",  "COUNTER SALE CUSTOMER","AUTHORISED RETAILER", "VST MECHANIC", "VST PLUMBER");
		List<String> receiptDistributor = Arrays.asList("DEALER", "CO-DISTRIBUTOR/STOCKIES/OEM", "AUTHORISED RETAILER", "VST MECHANIC", "VST PLUMBER", "SUPPLIER/VENDOR", "SUPLLIER / VENDOR", "COUNTER SALE CUSTOMER");
		List<PartyCategoryResponse> list = paymentVoucherDao.searchPartyTypeCategory();
		
		
		if(requestModel.getPaymentVoucherType().trim().equals("PAYMENT") && requestModel.getDealerTypeId()==10){
			
			for(PartyCategoryResponse bean: list) {
				if(paymentDealer.contains(bean.getPartyCategoryName().trim())) {
					finalList.add(bean);
				}
			}
		}


		if(requestModel.getPaymentVoucherType().trim().equals("RECEIPT") && requestModel.getDealerTypeId()==10){
					
					for(PartyCategoryResponse bean: list) {
						if(receiptDealer.contains(bean.getPartyCategoryName().trim())) {
							finalList.add(bean);
						}
					}
				}
		
		if(requestModel.getPaymentVoucherType().trim().equals("PAYMENT") && 	
		(requestModel.getDealerTypeId()==13 || requestModel.getDealerTypeId()==9)){
			
			for(PartyCategoryResponse bean: list) {
				if(paymentDistributor.contains(bean.getPartyCategoryName().trim())) {
					finalList.add(bean);
				}
			}
		}
		if(requestModel.getPaymentVoucherType().trim().equals("RECEIPT") && 	
				(requestModel.getDealerTypeId()==13 || requestModel.getDealerTypeId()==9)) {
					
					for(PartyCategoryResponse bean: list) {
						if(receiptDistributor.contains(bean.getPartyCategoryName().trim())) {
							finalList.add(bean);
						}
					}
				}
		return finalList;
	}

	@Override
	public PartyCodeListResponseModel searchPartyCodeList(String userCode, PVpartyCodeRequestModel requestModel) {
		return paymentVoucherDao.searchPVpartyCodeList(userCode,requestModel);
	}

	@Override
	public GrnInvReferenceDocList getRefDocDetail(String userCode, RefDocForGrnInvReq requestModel) {
	
		BigDecimal totalPendingAmt = new BigDecimal(0.00);
		BigDecimal totalSettleAmt = new BigDecimal(0.00);
		GrnInvReferenceDocList finalList =null;
		List<PVGrnAndInvoiceResponse> res = paymentVoucherDao.getRefDocDetail( userCode,  requestModel);
		if(!res.isEmpty()) {
			finalList = new GrnInvReferenceDocList();
			for(PVGrnAndInvoiceResponse bean:res) {
				totalPendingAmt = totalPendingAmt.add(bean.getPendingAmt()!=null?bean.getPendingAmt():totalPendingAmt);
				totalSettleAmt = totalSettleAmt.add(bean.getSettleAmt()!=null?bean.getSettleAmt():totalSettleAmt);
			}
			finalList.setDataList(res);
			finalList.setTotalPendingAmt(totalPendingAmt);
			finalList.setTotalSettleAmt(totalSettleAmt);
		}
		return finalList;
	}

	@Override
	public BigDecimal getPartyWiseAmt(String partyCode, String pvTypeCode) {
		
		return paymentVoucherDao.getPartyWiseAmt(partyCode, pvTypeCode);
	}

	@Override
	public SearchPaymentVoucherResponse filter(String authorizationHeader, String userCode,
			FilterPaymentVoucherReq requestModel, Device device) {
		
		SearchPaymentVoucherResponse finalList = new SearchPaymentVoucherResponse();
		List<PaymentVoucherReponse> pvList=null;
		PaymentVoucherReponse bean = null;
		Integer totalCount=0;
		Integer rowCount=0;
		try {
		List<?> list =  paymentVoucherDao.filter(requestModel,userCode);	
			if (list != null && !list.isEmpty()) {
				pvList= new ArrayList<PaymentVoucherReponse>();
	            for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                bean = new PaymentVoucherReponse();
	                bean.setId((BigInteger) row.get("ID"));  
//	                Integer type =(Integer)row.get("VOUCHER_TYPE_ID");
	                bean.setVoucherType((String)row.get("voucherTypeName"));
	                bean.setPaymentVoucherDate((Date) row.get("DOC_DATE"));
	                bean.setPaymentVoucherNo((String)row.get("DOC_NO"));
	                if((String)row.get("COUNTER_SALE_PARTY_NAME")!=null) {
	  	                bean.setPartyName((String)row.get("COUNTER_SALE_PARTY_NAME"));
	                }else {
	                	bean.setPartyCode((String)row.get("PARTY_CODE"));
	  	                bean.setPartyName((String)row.get("PARTYNAME"));
	                }
	              
	                bean.setAmount((BigDecimal)row.get("AMOUNT"));
	    			totalCount =   10; //(Integer)row.get("totalCount");
	    			pvList.add(bean);
	    			rowCount++;			
	            }
	            finalList.setSearchList(pvList); 
	            finalList.setTotalCount(totalCount);
	            finalList.setRowCount(rowCount);
	        } else {
	        	finalList.setMsg("No data found."); 
	        	
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        finalList.setMsg(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        finalList.setMsg(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return finalList;
	}

	@Override
	public List<SaveResponse> searchPaymentVoucherNumber(String searchText, String userCode) {

		return paymentVoucherDao.searchPaymentVoucherNumber(searchText,userCode);
	}

	@Override
	public viewPayVoucherResponse viewPaymentVoucherDetail(BigInteger paymentVoucherId, String userCode) {
		viewPayVoucherResponse bean = null;
		try {
		List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,1);	
		if (list != null && !list.isEmpty()) {

			for (Object object : list) {
                @SuppressWarnings("rawtypes")
                Map row = (Map) object;
                bean = new viewPayVoucherResponse();    
             // bean.setId((Integer) row.get("id"));  
                bean.setBranchName((String)row.get("BranchName"));
                bean.setVoucherType((String)row.get("Voucher_Type"));
                bean.setDocNo((String)row.get("DOC_NO"));
                bean.setPartyCode((String)row.get("PartyCode"));
                bean.setDocDate((Date)row.get("DOC_DATE"));
                bean.setPartyType((String)row.get("partyType"));
                bean.setPartyName((String)row.get("PartyName"));
                bean.setPartyAddLine1((String)row.get("PartyAddLine1"));
                bean.setPartyAddLine2((String)row.get("PartyAddLine2"));
                bean.setPartyAddLine3((String)row.get("PartyAddLine3"));
                
                bean.setMobileNumber((String)row.get("MobileNumber"));
                bean.setDistrictDesc((String)row.get("DistrictDesc"));
                bean.setRecepitMode((String)row.get("Recepit_Mode"));
                bean.setTehsilDesc((String)row.get("TehsilDesc"));
                bean.setCityDesc((String)row.get("CityDesc"));
                bean.setStateDesc((String)row.get("StateDesc"));
                bean.setCountryDesc((String)row.get("CountryDesc"));
                bean.setPinCode((String)row.get("PinCode"));
                bean.setGstNumber((String)row.get("GST_NUMBER"));
                bean.setPanOrTan((String)row.get("PAN_OR_TAN"));
                bean.setSaleInvoiceId((Integer)row.get("SALE_INVOICE_ID"));
                bean.setCounterSalePartyName((String)row.get("COUNTER_SALE_PARTY_NAME"));
                
                bean.setReferenceDocument((String)row.get("Reference_Document"));
                bean.setAmount((BigDecimal)row.get("AMOUNT"));
                bean.setAdvAmt((BigDecimal)row.get("ADV_AMT"));
                bean.setCreatedDate((Date)row.get("CreatedDate"));
                bean = getReceiptModeDetails(bean, paymentVoucherId,bean.getRecepitMode(),userCode);
                
             }
			}
		  } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(this.getClass().getName(), exp);
	    }
	
	    return bean;
	}
	

	private viewPayVoucherResponse getReceiptModeDetails(viewPayVoucherResponse beanResponse, BigInteger paymentVoucherId, String recepitMode, String userCode) {
		
		if(recepitMode.equalsIgnoreCase("CASH")) {
			RefDocListRequest bean = null;
			List<RefDocListRequest> listRefDoc = new ArrayList<>();
			try {
				List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,6);	
			 if (list != null && !list.isEmpty()) {
				 
				for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                bean = new RefDocListRequest();    
	                bean.setGrnInvId((String) row.get("GRN_INVOICE_NUMBER"));
	                bean.setGrnInvDate((Date)row.get("GRN_INV_DATE"));
	                bean.setGrnInvAmt((BigDecimal) row.get("GRN_INV_AMT"));
	                bean.setPendingAmt((BigDecimal) row.get("PENDING_AMT"));
	                bean.setSettleAmt((BigDecimal) row.get("SETTLE_AMT"));
	                bean.setSettleDate((Date) row.get("SETTLE_DATE"));
	                bean.setTotalPendingAmt((BigDecimal) row.get("TOTAL_PENDING_AMT"));
	                bean.setTotalSettlementAmt((BigDecimal) row.get("TOTAL_SETTLEMENT_AMT"));
	                listRefDoc.add(bean);
	            	}
				 beanResponse.setRefDocList(listRefDoc);
				}
			  } catch (HibernateException exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      } catch (Exception exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      }
		
		    return beanResponse;
		 } 
		if(recepitMode!=("CASH")) {
			RefDocListRequest bean = null;
			List<RefDocListRequest> listRefDoc = new ArrayList<>();
			try {
				List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,6);	
			 if (list != null && !list.isEmpty()) {
				 
				for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                bean = new RefDocListRequest();    
	                bean.setGrnInvId((String) row.get("GRN_INVOICE_NUMBER"));
	                bean.setGrnInvDate((Date)row.get("GRN_INV_DATE"));
	                bean.setGrnInvAmt((BigDecimal) row.get("GRN_INV_AMT"));
	                bean.setPendingAmt((BigDecimal) row.get("PENDING_AMT"));
	                bean.setSettleAmt((BigDecimal) row.get("SETTLE_AMT"));
	                bean.setSettleDate((Date) row.get("SETTLE_DATE"));
	                bean.setTotalPendingAmt((BigDecimal) row.get("TOTAL_PENDING_AMT"));
	                bean.setTotalSettlementAmt((BigDecimal) row.get("TOTAL_SETTLEMENT_AMT"));
	                listRefDoc.add(bean);
	            	}
				 beanResponse.setRefDocList(listRefDoc);
				}
			  } catch (HibernateException exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      } catch (Exception exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      }
		
		//    return beanResponse;
		 } 
		 if(recepitMode.equalsIgnoreCase("CHEQUE/DD")) {
			try {
				ChequeDdResponse response=null;
				List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,2);	
			 if (list != null && !list.isEmpty()) {
				 
				for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                response = new ChequeDdResponse();
	                response.setChequeDdNo((String) row.get("CHEQUE_DD_NO"));
	                response.setChequeDdDate((Date)row.get("CHEQUE_DD_DATE"));
	                response.setChequeBankName((String) row.get("CHEQUE_BANK_NAME"));             
	                beanResponse.setChequeDdResponse(response);
					}
				}
			  } catch (HibernateException exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      } catch (Exception exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      }
		
		    return beanResponse;
			
		}else if(recepitMode.equalsIgnoreCase("CREDIT/DEBIT CARD")) {
			try {
				CardResponse response=null;
			List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,3);
			 if (list != null && !list.isEmpty()) {
				 
					for (Object object : list) {
		                @SuppressWarnings("rawtypes")
		                Map row = (Map) object;
		                
		                response = new CardResponse();
		                
		                response.setCardNo((String) row.get("CARD_NO"));
		                response.setCardDate((Date)row.get("CARD_DATE"));
		                response.setCardTypeName((String)row.get("cardTypeName"));
		                response.setCardName((String)row.get("CARD_NAME"));
		                response.setTranNo((String)row.get("TRAN_NO"));
		                response.setTranDate((Date)row.get("TRAN_DATE"));
		                beanResponse.setCardResponse(response);
		            	}
					}
				  } catch (HibernateException exp) {
			        String errorMessage = "An error occurred: " + exp.getMessage();
			        logger.error(this.getClass().getName(), exp);
			      } catch (Exception exp) {
			        String errorMessage = "An error occurred: " + exp.getMessage();
			        logger.error(this.getClass().getName(), exp);
			      }
			
			    return beanResponse;
			
		}else if(recepitMode.equalsIgnoreCase("E-WALLET")) {
			try {
				EWalletResponse response=null;
			List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,4);
			if (list != null && !list.isEmpty()) {
				 
				for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                response = new EWalletResponse();
	                response.setETranNo((String)row.get("E_TRAN_NO"));
	                response.setETranDate((Date)row.get("E_TRAN_DATE"));
	                response.setEServProvider((String)row.get("LookupText"));
	                beanResponse.setEwalletResponse(response);
	            	}
				}
			  } catch (HibernateException exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      } catch (Exception exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      }
		
		    return beanResponse;
		
			
		}else if(recepitMode.equalsIgnoreCase("RTGS/NEFT/IMPS")) {
			try {
				RtgsNeftImpsResponse response=null;
			List<?> list =  paymentVoucherDao.viewPaymentVoucherDetail(paymentVoucherId,userCode,5);
			if (list != null && !list.isEmpty()) {
				 
				for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                response = new RtgsNeftImpsResponse();
	             //   response.setRtgsTranBankId((Integer)row.get("LookupText"));
	                response.setRtgsTranBankName((String)row.get("LookupText"));
	                response.setRtgsTranNo((String)row.get("RTGS_TRAN_NO"));
	                response.setRtgsTranDate((Date)row.get("RTGS_TRAN_DATE"));
	                
	                beanResponse.setRtgsNeftImpsResponse(response);
	            	}
				}
			  } catch (HibernateException exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      } catch (Exception exp) {
		        String errorMessage = "An error occurred: " + exp.getMessage();
		        logger.error(this.getClass().getName(), exp);
		      }
		
		    return beanResponse;
		
			
		}
		
		return null;
		
		
	}

	@Override
	public List<PartyCategoryResponse> partyCodeList(String searchText, String userCode) {
		
		return paymentVoucherDao.partyCodeList(searchText,userCode);
	}

	@Override
	public List<SparePoDealerAndDistributerSearchResponse> getPartyNameList(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest) {
	
		return paymentVoucherDao.getPartyNameList(userCode, sparePoDealerAndDistributorRequest);
	}

	@Override
	public DistributorDetailResponse getPartyNameDetails(Integer distributorId) {
		return paymentVoucherDao.getPartyNameDetails(distributorId);
	}

	@Override
	public DistributorDetailResponse getPartyWisePVDetails(Integer distributorId, String flag) {
		 return paymentVoucherDao.getPartyWisePVDetails(distributorId,flag);
	}

	@Override
	public List<PartyCategoryResponse> getPvCategory(String userCode) {
		return paymentVoucherDao.getPvCategory(userCode);
	}
	

}
