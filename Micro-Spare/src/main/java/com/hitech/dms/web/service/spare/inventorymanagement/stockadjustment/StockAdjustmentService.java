package com.hitech.dms.web.service.spare.inventorymanagement.stockadjustment;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment.SpareStockAdjustmentHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.SearchStockAdjustmentRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentApprovalRequestDto;

@Transactional
public interface StockAdjustmentService {
	
	ApiResponse<?> uploadStockAdjustment(BigInteger branchId, MultipartFile stockAdjfile);
	
	ApiResponse<?> saveStockAdjustment(String userCode, SpareStockAdjustmentHdr requestModel);
	
	ApiResponse<?> autoSearchAdjustmentNo(String adjustmentNo);
	
	ApiResponse<?>  searchStockAdjustment(String userCode, SearchStockAdjustmentRequestDto  requestModel);
	
	ApiResponse<?> viewStockAdjustment(BigInteger adjustmentId);
	
	ApiResponse<?> approveRejectStockAdj(String userCode, StockAdjustmentApprovalRequestDto requestModel);
	
	ApiResponse<?> getMrplist(BigInteger partId);
	
	ApiResponse<?>  autoCompletePartNo(String partNo, BigInteger branchId);
	
	ApiResponse<?> getStorelist(BigInteger partBranchId, BigInteger branchId);

}
