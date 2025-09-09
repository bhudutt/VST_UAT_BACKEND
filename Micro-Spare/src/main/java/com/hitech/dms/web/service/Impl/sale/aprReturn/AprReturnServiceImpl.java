package com.hitech.dms.web.service.Impl.sale.aprReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.HibernateException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.customer.order.SpareCustomerOrderDao;
import com.hitech.dms.web.dao.spare.sale.aprReturn.AprReturnDao;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
import com.hitech.dms.web.model.spara.delivery.challan.response.SpareDcCreateResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnCancelRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnSearchRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.CreateAprReturnRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprAppointmentSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnNumber;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.InvoicePartDetailResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.SpareAprRetunCreateResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnResponse;
import com.hitech.dms.web.service.sale.aprReturn.AprReturnService;

@Service
public class AprReturnServiceImpl implements AprReturnService{
	
	private static final Logger logger = LoggerFactory.getLogger(AprReturnServiceImpl.class);
	
	
	@Autowired
	private AprReturnDao aprReturnDao;
	
	
	@Autowired 
	private SpareCustomerOrderDao spareCustomerOrderDao;

	@Override
	public List<PartyInvoiceList> fetchPartyInvoiceList(Integer partyBranchId, String userCode) {
		
		return aprReturnDao.fetchPartyInvoiceList(partyBranchId, userCode);
	}

	@Override
	public List<InvoicePartDetailResponse> getInvoicePartDetail(Integer saleInvoiceId, String userCode) {
		
		List<InvoicePartDetailResponse> invoiceDtlList=null;
		InvoicePartDetailResponse bean =null;
		List<?> list = aprReturnDao.getInvoicePartDetail(saleInvoiceId, userCode);{
			try {
			if (list != null && !list.isEmpty()) {
				invoiceDtlList= new ArrayList<InvoicePartDetailResponse>();
	            for (Object object : list) {
	              
	                Map row = (Map) object;
	                                
	                bean = new InvoicePartDetailResponse();
	                bean.setId((BigInteger) row.get("invoice_sale_dtl_id"));
	                bean.setPartId((BigInteger) row.get("part_id"));
	                bean.setPartNo((String) row.get("PartNumber"));
	                bean.setPartDescription((String) row.get("PartDesc"));
	                bean.setProductSubCat((String) row.get("PartSubCategory"));
	                bean.setBin((String) row.get("StoreBinLocation"));
	                bean.setBinStoreId((BigInteger) row.get("stock_bin_id"));
	                bean.setBranchId((Integer) row.get("branch_id"));
	                bean.setStockStoreId((Integer) row.get("stock_store_id"));
	                bean.setPartBranchId((BigInteger) row.get("partBranch_id"));
	                bean.setStore((String)row.get("FromStore"));
	                bean.setTotalStock((Integer) row.get("totalStock"));
	                bean.setMrp((BigDecimal) row.get("MRP"));
	                bean.setBasicUnitPrice((BigDecimal) row.get("BasicValue"));
	                bean.setOrderQty((BigDecimal) row.get("Qty"));
	                bean.setDiscount((BigDecimal) row.get("DiscountRate"));
	                bean.setInvoicedQty((BigDecimal)row.get("invoiceQty"));                
	                bean.setDiscountAmount((BigDecimal) row.get("DiscountValue"));
	                bean.setAdditionalDiscountPer((BigDecimal) row.get("Add_Discount_Amount"));
	                bean.setAdditionalDiscountAmount((BigDecimal) row.get("Add_Discount_Rate"));
	                bean.setAdditionalDiscountType((String) row.get("Add_Discount_Type"));
	                bean.setReturnedQty((Integer) row.get("ReturnedQty"));	             
	            //  bean.setTaxableAmount((BigDecimal) row.get("taxableValue"));
	                bean.setCgstAmount((BigDecimal)row.get("cgst"));
	                bean.setSgstAmount((BigDecimal)row.get("sgst"));
	                bean.setIgstAmount((BigDecimal)row.get("igst"));
	                bean.setCgst((Integer)row.get("cgstPercent"));
	                bean.setSgst((Integer)row.get("sgstPercent"));
	                bean.setIgst((Integer)row.get("igstPercent"));
	                invoiceDtlList.add(bean);
	            }
			}
			}catch (SQLGrammarException sqlge) {
				sqlge.printStackTrace();
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			} 
		}
		return invoiceDtlList;
	}

	@Override
	public SpareAprRetunCreateResponse createAprReturn(String authorizationHeader, String userCode,
			@Valid CreateAprReturnRequest requestModel, Device device) {
		CustOrderProductCtgResponseModel response = spareCustomerOrderDao.getDocumentNumber(requestModel.getDocumentType(), BigInteger.valueOf(requestModel.getBranchId()));
		if(response.getProductCtgNumber()==null) {
			SpareAprRetunCreateResponse responseModel = new SpareAprRetunCreateResponse();
			responseModel.setMsg("Unable to generate a DOC number Kindly inform admin.");
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			responseModel.setAprReturnNumber(response.getProductCtgNumber());
			return responseModel;
		}
		Integer returnedQty = aprReturnDao.getQtyAndReturnQtyForApr(requestModel.getSparePartDetails(),userCode);
		requestModel.setAprReturedDocNo(response.getProductCtgNumber());
		return aprReturnDao.createAprReturn(userCode,requestModel);
				
	}

	@Override
	public List<AprReturnNumber> fetchAprReturnNoList(String userCode, InvoicePartNoRequest requestModel) {
		
		return aprReturnDao.fetchAprReturnNoList(userCode, requestModel);
	}

	@Override
	public List<PartyInvoiceList> fetchInvoiceNumList(String userCode, InvoicePartNoRequest requestModel) {
		
		return aprReturnDao.fetchInvoiceNumList(userCode, requestModel);
	}

	@Override
	public AprReturnSearchList search(String userCode, AprReturnSearchRequest resquest, Device device) {
		return aprReturnDao.search(userCode, resquest, device);
	}

	@Override
	public ViewAprReturnResponse viewAprReturnDetail(BigInteger aprReturnId, String userCode) {
		ViewAprReturnResponse headerResponse = aprReturnDao.viewAprReturnHeader(aprReturnId, userCode);
		headerResponse.setDetailList(aprReturnDao.viewAprReturnDetail(aprReturnId, userCode));
		
		return headerResponse;
	}

	@Override
	public List<PartyCategoryResponse> aprPartyCodeList(String searchText, String userCode) {
		return aprReturnDao.aprPartyCodeList(searchText, userCode);
	}

	@Override
	public AprAppointmentSearchList aprAppointmentSearch(String userCode, AprReturnSearchRequest resquest,
			Device device) {
		return aprReturnDao.aprAppointmentSearch(userCode, resquest, device);
	}
	
	@Override
	public AprAppointmentSearchList aprMappingReportSearch(String userCode, AprReturnSearchRequest resquest,
			Device device) {
		return aprReturnDao.aprMappingReportSearch(userCode, resquest, device);
	}
	

	@Override
	public SpareAprRetunCreateResponse cancelAprReturn(String userCode, AprReturnCancelRequest request,Device device) {
		
		SpareAprRetunCreateResponse response = null;
		
		 Integer id =   aprReturnDao.cancelAprReturnStatus(userCode,request,device);
		   if(id!=null) {
			  Integer aprReturnId = aprReturnDao.cancelAprReturn(userCode,request,device);
			  if(aprReturnId!=null) {
				  response = new SpareAprRetunCreateResponse();
				  response.setMsg("Cancel the apr return successfully...");
				  response.setStatusCode(200);
				  response.setAprReturnHdrId(aprReturnId);
				  return response;
			  }
		   }
		   return response;
	}

}
