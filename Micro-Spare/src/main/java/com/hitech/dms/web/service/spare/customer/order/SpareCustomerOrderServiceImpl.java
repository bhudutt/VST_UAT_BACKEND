package com.hitech.dms.web.service.spare.customer.order;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.customer.order.SpareCustomerOrderDao;
import com.hitech.dms.web.model.SpareModel.SparePIdAndPnoModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartDetailsRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoListRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCalculationRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCancelRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderUpdateRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderCreateResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderCalculationResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;



/**
 * @author Vivek.Gupta
 *
 */

@Service
public class SpareCustomerOrderServiceImpl implements SpareCustomerOrderService {

	

	@Autowired
	private SpareCustomerOrderDao spareCustomerOrderDao;


	@Override
	public SpareCustOrderCreateResponseModel createCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderRequest requestModel, Device device) {
		
		CustOrderProductCtgResponseModel response = spareCustomerOrderDao.getDocumentNumber(requestModel.getDocumentType(), requestModel.getBranchId());
		if(response.getProductCtgNumber()==null && requestModel.getCustomerOrderNumber()==null) {
			SpareCustOrderCreateResponseModel responseModel = new SpareCustOrderCreateResponseModel();
			responseModel.setMsg("Unable to generate a DOC number Kindly inform admin.");
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			responseModel.setCustomerOrderNumber(requestModel.getCustomerOrderNumber());
			return responseModel;
		}
		requestModel.setCustomerOrderNumber(response.getProductCtgNumber());
		return spareCustomerOrderDao.createCustomerOrder(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public SpareCustomerOrderPartUploadResponse validateUploadedFile(String authorizationHeader, String userCode, BigInteger branchId,
			 Integer productCategoryId, Integer dealerId, Integer partyTypeId, Integer partyBranchId, MultipartFile file) {

		SpareCustomerOrderPartUploadResponse spareUploadResponse = spareCustomerOrderDao.uploadSpareCustomerOrderPart(authorizationHeader,
				userCode, branchId, productCategoryId, dealerId, file);
		List<SpareCustOrderPartDetailResponse> partForExcelList = null;
		Map<String, String> errorPartData = spareUploadResponse.getErrorPartData();
		Map<String, String> errorData = new HashMap<>();
		try {
		if (spareUploadResponse.getPartAndQty() != null && spareUploadResponse.getStatusCode() == 201) {
			 partForExcelList = new ArrayList<>();
			Map<String, Integer> data = spareUploadResponse.getPartAndQty();
			int index = 0;
			for (var entry : data.entrySet()) {
				index++;
				List<SpareCustOrderPartDetailResponse> parldltResponse = spareCustomerOrderDao.fetchPartDetailsByPartNumber(userCode, entry.getKey(), entry.getValue(),productCategoryId,branchId,partyTypeId);
				if (parldltResponse!=null && !parldltResponse.get(0).getProductCtgFlag() && parldltResponse.get(0).getMsg().equalsIgnoreCase("OK")) {
				SpareCustOrderPartDetailResponse parstResponse = parldltResponse.get(0);
				SpareCustomerOrderCalculationRequest requestModel = new SpareCustomerOrderCalculationRequest();
				
				requestModel.setBranchId(branchId);
				requestModel.setDealerId(dealerId);
				requestModel.setPartId(BigInteger.valueOf(parstResponse.getPartId()));
				requestModel.setQty(entry.getValue());
				requestModel.setPartBranchId(parstResponse.getPartBranchId());		
				
				SpareCustOrderPartDetailResponse partForExcel = new SpareCustOrderPartDetailResponse();
				SparePIdAndPnoModel pid = new SparePIdAndPnoModel();
				pid.setPartId(parstResponse.getPartId());
				pid.setPartNo(parstResponse.getPartNo());
				partForExcel.setPartNo(parstResponse.getPartNo());
				partForExcel.setPartId(parstResponse.getPartId());
				partForExcel.setPartDesc(parstResponse.getPartDesc());
				partForExcel.setProductSubCategory(parstResponse.getProductSubCategory());
				partForExcel.setHSNCode(parstResponse.getHSNCode());
				partForExcel.setCurrentStock(parstResponse.getCurrentStock());
				partForExcel.setOrderQty(entry.getValue());
				partForExcel.setInvoicedQty(parstResponse.getInvoicedQty());
				partForExcel.setPartBranchId(parstResponse.getPartBranchId());
				
				List<SpareCustomerOrderCalculationResponse> calResponse = spareCustomerOrderDao.fetchSpareCoItemAmountCal(userCode, requestModel,partyBranchId);
				if(calResponse!=null) {
					
				SpareCustomerOrderCalculationResponse calculationResponse = calResponse.get(0);
				partForExcel.setBasicUnitPrice(calculationResponse.getBasicUnitPrice());
				partForExcel.setTotalBasePrice(calculationResponse.getTotalBasePrice());
				partForExcel.setCgst(calculationResponse.getCgst());
				partForExcel.setCgstAmount(calculationResponse.getCgstAmount());
				partForExcel.setSgst(calculationResponse.getSgst());
				partForExcel.setSgstAmount(calculationResponse.getSgstAmount());
				partForExcel.setIgst(calculationResponse.getIgst());
				partForExcel.setIgstAmount(calculationResponse.getIgstAmount());
				}else {
					errorData.put(String.valueOf(index),
							"This part does not have a price set. You need to set it. ("
									+ entry.getKey() + ").");
					spareUploadResponse.setErrorPartData(errorData);
				}			
				partForExcelList.add(partForExcel);
			  }else if (spareUploadResponse.getPartAndQty() != null|| spareUploadResponse.getPartAndQty().isEmpty()) {
	
				
				  errorData.put(String.valueOf(index),parldltResponse.get(0).getMsg());
		        	spareUploadResponse.setErrorPartData(errorData);
					
			  }else if(parldltResponse!=null && parldltResponse.get(0).getProductCtgFlag()) {
				  spareUploadResponse.setMsg("Product category of uploaded parts not matched");
				  spareUploadResponse.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			  }
				else {
					spareUploadResponse.setMsg("Something wents wrong !!!");
					spareUploadResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);

				}
			}

		}else {
			
				
				  if (errorPartData != null && !errorPartData.isEmpty()) {
						spareUploadResponse.setErrorPartData(errorPartData);
				  }else {
					  spareUploadResponse.setMsg("Something wents wrong !!!");
					  spareUploadResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				  }
			
		    
		}
			spareUploadResponse.setPartForExcelList(partForExcelList);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return spareUploadResponse;

	}



	@Override
	public List<SparePoCategoryResponse> getAllCustOrderPoCategory() {

		return spareCustomerOrderDao.getAllCustOrderPoCategory();
	}

	@Override
	public List<partSearchResponseModel> fetchPartNumber(String userCode, CustomerOrderPartNoRequest requestModel) {

		return spareCustomerOrderDao.fetchPartNumber(userCode, requestModel);

	}
	
	@Override
	public List<SpareCustOrderPartDetailResponse> fetchCOPartDetails(String userCode, CustomerOrderPartDetailsRequest bean) {
		return spareCustomerOrderDao.fetchCOPartDetailsByPartId(userCode,  bean);
	}

	@Override
	public List<SpareCustOrderPartDetailResponse> customerOrderPartDetail(String userCode,
			CustomerOrderPartNoRequest requestModel) {
		return spareCustomerOrderDao.customerOrderPartDetail(userCode,  requestModel);
	}

	@Override
	public List<SparePoCategoryResponse> getAllSubCategory() {
		
		return spareCustomerOrderDao.getAllSubCategory();
	}

	@Override
	public SpareCustOrderCreateResponseModel updateCustomerOrder(String authorizationHeader, String userCode,
			@Valid List<SpareCustomerOrderUpdateRequest> requestModel, Device device) {
	
		return spareCustomerOrderDao.updateCustomerOrder(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public SpareCustOrderCreateResponseModel cancelCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderCancelRequest requestModel, Device device) {
		
		return spareCustomerOrderDao.cancelCustomerOrder(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public SpareCustOrderCreateResponseModel editSpareCustomerOrder(String authorizationHeader, String userCode,
			@Valid SpareCustomerOrderRequest requestModel, Device device) {
	
		return spareCustomerOrderDao.editSpareCustomerOrder(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public SpareCustOrderCreateResponseModel deletePartNofromDTL(String authorizationHeader, String userCode,
			CustomerOrderPartNoListRequest requestModel, Device device) {
		SpareCustOrderCreateResponseModel response=null;
		Integer id = spareCustomerOrderDao.deletePartNofromDTL(authorizationHeader,userCode,requestModel,device);
		if(id!=null && id>0) {
			response = new SpareCustOrderCreateResponseModel();
			response.setMsg("delete row successfully");
		}
		return response;
	}

//	@Override
//	public SpareCustomerOrderPartUploadResponse saveUploadedFile(String authorizationHeader, String userCode,
//			BigInteger branchId, Integer productCategoryId, Integer dealerId, Integer partyTypeId, MultipartFile file) throws IOException  {
//		SpareCustomerOrderPartUploadResponse response = new SpareCustomerOrderPartUploadResponse();
//		 List<CustomerOrderExcelEntity> res =null;
//		    XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//		    XSSFSheet sheet = workbook.getSheetAt(0);
//		//    List<CustomerOrderExcelEntity> coExcelData = new ArrayList<CustomerOrderExcelEntity>();
//		    DataFormatter dataFormatter = new DataFormatter();
//		    for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
//		        Row row = sheet.getRow(n);
//		        CustomerOrderExcelEntity entity = new CustomerOrderExcelEntity();
//		        int i = row.getFirstCellNum();
//		        
//		        entity.setPartNumber(String.valueOf(dataFormatter.formatCellValue(row.getCell(i))));
//		        entity.setQty(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(++i))));
//		        entity.setBranchId(branchId);
//		        entity.setProductCategoryId(productCategoryId);
//		        entity.setPartyCodeId(partyTypeId);
//		        customerOrderRepository.save(entity);
//		     }
//		  
//		    response.setMsg("Product category of uploaded parts not matched");
//		    response.setStatusCode(WebConstants.STATUS_CREATED_201);
//		      return response;
//		    
//	}
//	
	
//	@Override
//	public SpareCustomerOrderPartUploadResponse saveUploadedFile(String authorizationHeader, String userCode,
//	        BigInteger branchId, Integer productCategoryId, Integer dealerId, Integer partyTypeId, MultipartFile file) throws IOException {
//	    SpareCustomerOrderPartUploadResponse response = new SpareCustomerOrderPartUploadResponse();
//	    List<CustomerOrderExcelEntity> res = new ArrayList<>(); // Initialize list to hold uploaded data
//	    try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			DataFormatter dataFormatter = new DataFormatter();
//  
//
//			for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
//			    Row row = sheet.getRow(n);
//			    CustomerOrderExcelEntity entity = new CustomerOrderExcelEntity();
//			    int i = row.getFirstCellNum();
//
//			    entity.setPartNumber(String.valueOf(dataFormatter.formatCellValue(row.getCell(i))));
//			    entity.setQty(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(++i))));
//			    entity.setBranchId(branchId);
//			    entity.setProductCategoryId(productCategoryId);
//			    entity.setPartyCodeId(partyTypeId);
//			    res.add(entity); // Add entity to the list
//			}
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}
//	    if (!res.isEmpty()) {
//	        customerOrderRepository.saveAll(res);
//	        response.setMsg("Uploaded parts saved successfully");
//	        response.setStatusCode(WebConstants.STATUS_CREATED_201);
//	    } else {
//	        response.setMsg("uploaded parts not save");
//	        response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//	    }
//
//	    return response;
//	}
	
	
	
	
	

}
