package com.hitech.dms.web.serviceImpl.spare.creditDebit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.creditDebit.CreditDebitNoteDao;
import com.hitech.dms.web.dao.spare.customer.order.SpareCustomerOrderDao;
import com.hitech.dms.web.entity.spare.creditDebit.note.CreditDebitNoteEntity;
import com.hitech.dms.web.model.spara.creditDebit.note.request.CreateCrDrNoteRequest;
import com.hitech.dms.web.model.spara.creditDebit.note.request.FilterCreditDebitNoteReq;
import com.hitech.dms.web.model.spara.creditDebit.note.response.CreditDebitNoteReponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.FilterCreditDebitNoteRep;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.ViewResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.service.spare.creditDebit.CreditDebitNoteService;
import com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer.BinToBinTransferServiceImpl;

@Service
public class CreditDebitNoteServiceImpl implements CreditDebitNoteService{
	
	private static final Logger logger = LoggerFactory.getLogger(CreditDebitNoteServiceImpl.class);

	@Autowired
	private CreditDebitNoteDao creditDebitNoteDao;
	
	@Autowired
	private SpareCustomerOrderDao spareCustomerOrderDao;
	
	@Override
	public List<PaymentVoucherList> getCreditAndDebitType(String lookupTypeCode) {
		return creditDebitNoteDao.getCreditAndDebitType(lookupTypeCode);
	}

	@Override
	public SaveResponse save(String authorizationHeader, String userCode,
			@Valid CreateCrDrNoteRequest requestModel, Device device) throws ParseException {
		SaveResponse response=null;
		CustOrderProductCtgResponseModel res=new CustOrderProductCtgResponseModel();
		CreditDebitNoteEntity entity = new CreditDebitNoteEntity();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		if(requestModel.getVoucherType().equals("CREDIT")) {
			 res = spareCustomerOrderDao.getDocumentNumber("CRN", requestModel.getBranchId());
		}
		if(requestModel.getVoucherType().equals("DEBIT")) {
			 res = spareCustomerOrderDao.getDocumentNumber("DRN", requestModel.getBranchId());
		}
		if(res==null && res.getProductCtgNumber()==null) {
			SaveResponse responseModel = new SaveResponse();
			responseModel.setMsg("Unable to generate a DOC number Kindly inform admin.");
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			responseModel.setNumber(res.getProductCtgNumber());
			return responseModel;
		}
			BeanUtils.copyProperties(requestModel, entity);
			entity.setCreditDebitNo(res.getProductCtgNumber());
			entity.setCreditDebitDate(formatter.parse(requestModel.getCreditDebitDate()));
		    BigInteger id = this.creditDebitNoteDao.save(entity,userCode);
				if(id!=null) {
					response = new SaveResponse();
					response.setId(id);
					response.setNumber(res.getProductCtgNumber());
				}
			
		return response;
	}

	@Override
	public List<PartyCategoryResponse> partyTypeList(Integer dealerTypeId) {
		 	
		List<PartyCategoryResponse> finalList = new ArrayList<>();
		List<String> dealer = Arrays.asList("CO-DEALER", "DISTRIBUTOR/STOCKIES/OEM", "SUPPLIER / VENDOR");
//		List<String> receiptDealer = Arrays.asList("CO-DEALER", "COUNTER SALE CUSTOMER");
//		List<String> paymentDistributor = Arrays.asList("CO-DISTRIBUTOR/STOCKIES/OEM", "SUPLLIER/VENDOR",  "COUNTER SALE CUSTOMER");
		List<String> distributor = Arrays.asList("DEALER", "CO-DISTRIBUTOR/STOCKIES/OEM", "AUTHORISED RETAILER", "VST MECHANIC", "VST PLUMBER", "SUPPLIER / VENDOR");
		List<PartyCategoryResponse> list = creditDebitNoteDao.partyTypeList();;
		
		
		
		
		if(dealerTypeId==9 || dealerTypeId==13){
			
			for(PartyCategoryResponse bean: list) {
				if(distributor.contains(bean.getPartyCategoryName())) {
					finalList.add(bean);
				}
			}
		}else {
			
				
				for(PartyCategoryResponse bean: list) {
					if(dealer.contains(bean.getPartyCategoryName())) {
						finalList.add(bean);
					}
				}
			
		}
		
		return finalList;
	}

	@Override
	public List<SaveResponse> searchCreditDebitNumber(String searchText, String userCode) {
		
		return creditDebitNoteDao.searchCreditDebitNumber(searchText,userCode);
	}

	@Override
	public FilterCreditDebitNoteRep filter(String authorizationHeader, String userCode,
			@Valid FilterCreditDebitNoteReq requestModel, Device device) {
		FilterCreditDebitNoteRep finalList = new FilterCreditDebitNoteRep();
		List<CreditDebitNoteReponse> crDrList=null;
		CreditDebitNoteReponse bean = null;
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("MMM dd yyyy hh:mma");
	    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Integer totalCount=0;
		Integer rowCount=0;
		try {
		List<?> list =  creditDebitNoteDao.filter(requestModel,userCode);	
			if (list != null && !list.isEmpty()) {
				crDrList= new ArrayList<CreditDebitNoteReponse>();
	            for (Object object : list) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                bean = new CreditDebitNoteReponse();
	                bean.setId((BigInteger) row.get("Cr_Dr_Id"));
//	                bean.setVoucherTypeId( (BigInteger)row.get("Voucher_Type_Id"));
	                bean.setVoucherType(row.get("Cr_Dr_No").toString().substring(0, 3).equalsIgnoreCase("CRN")?"CREDIT":"DEBIT");;
	                bean.setCreditDebitNo((String) row.get("Cr_Dr_No"));
	                Date date = inputDateFormat.parse((String)row.get("Cr_Dr_Date"));
	                bean.setCreditDebitDate(outputDateFormat.format(date));
	                bean.setCreditDebitAmt((BigDecimal)row.get("Cr_Dr_Amt"));
	                bean.setPartyCategoryName((String)row.get("PartyCategoryName"));
	    			bean.setAddress((String)row.get("DealerAddress1")+","+(String)row.get("DealerAddress2")+","+(String)row.get("DealerAddress3"));
	    			bean.setTehsilTalukaMandal((String)row.get("Dealer_tehsil"));
	    			bean.setPincode((String)row.get("dealer_pincode"));
	    			bean.setPartyCode((String)row.get("ParentDealerCode"));
	    			bean.setState((String)row.get("Dealer_state"));
	    			bean.setCityVillage((String)row.get("Dealer_city"));
	    			bean.setPartyName((String)row.get("ParentDealerName"));
	    			bean.setDistrict((String)row.get("Dealer_district"));
	    	//		bean.setPostOffice( row.get("Cr_Dr_Id"));
	    			bean.setCountry((String)row.get("Dealer_country"));
	    			bean.setPanNo((String)row.get("pan_no"));
	    			bean.setGstNo((String)row.get("gst_no"));
	    			bean.setMobileNo((String)row.get("MobileNumber"));
	    			totalCount = (Integer)row.get("totalCount");
	    			crDrList.add(bean);
	    			rowCount++;
	            }
	            finalList.setSearchList(crDrList); 
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
	public ViewResponse viewCreditDebitDetail(BigInteger creditDebitNoteId, String userCode) {
	
		ViewResponse bean=null;
		List<?> list = creditDebitNoteDao.viewCreditDebitDetail(creditDebitNoteId, userCode);
	    try {
		if (list != null && !list.isEmpty()) {
            for (Object object : list) {
                @SuppressWarnings("rawtypes")
                Map row = (Map) object;
                bean = new ViewResponse();
                bean.setId((BigInteger) row.get("Cr_Dr_Id"));
//                bean.setVoucherTypeId( (BigInteger)row.get("Voucher_Type_Id"));
                bean.setVoucherType(row.get("Cr_Dr_No").toString().substring(0, 3).equalsIgnoreCase("CRN")?"CREDIT":"DEBIT");
                bean.setCreditDebitNo((String) row.get("Cr_Dr_No"));
                bean.setCreditDebitDate((String)row.get("Cr_Dr_Date"));
                bean.setCreditDebitAmt( (BigDecimal)row.get("Cr_Dr_Amt"));
                bean.setPartyCategoryName((String)row.get("PartyCategoryName"));
    			bean.setAddress1((String)row.get("DealerAddress1"));
    			bean.setAddress2((String)row.get("DealerAddress2"));
    			bean.setAddress3((String)row.get("DealerAddress3"));
    			bean.setTehsilTalukaMandal((String)row.get("Dealer_tehsil"));
    			bean.setPincode((String)row.get("dealer_pincode"));
    			bean.setPartyCode((String)row.get("ParentDealerCode"));
    			bean.setState((String)row.get("Dealer_state"));
    			bean.setCityVillage((String)row.get("Dealer_city"));
    			bean.setPartyName((String)row.get("ParentDealerName"));
    			bean.setDistrict((String)row.get("Dealer_district"));
    			bean.setPostOffice((String)row.get("PostOffice"));
    			bean.setCountry((String)row.get("Dealer_country"));
    			bean.setPanNo((String)row.get("pan_no"));
    			bean.setGstNo((String)row.get("gst_no"));
    			bean.setMobileNo((String)row.get("MobileNumber"));
    			bean.setRemark((String)row.get("Remark"));
    			bean.setBranchName((String)row.get("BranchName"));
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

}
