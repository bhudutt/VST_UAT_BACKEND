package com.hitech.dms.web.service.spare.inventorymanagement.deadstockupload;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.deadstockupload.DeadStockUploadEntity;
import com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload.DeadStockSearchRequest;

@Transactional
public interface DeadStockUploadService {
	ApiResponse<?> uploadDeadStock(BigInteger branchId, MultipartFile deadStockFile);
	
	ApiResponse<?> saveDeadStockUpload(String userCode, List<DeadStockUploadEntity> requestModels, List<MultipartFile> files);
	
	ApiResponse<?>  autoCompletePartNo(String partNo, BigInteger branchId);
	
	ApiResponse<?> searchDeadStockUpload(String userCode, DeadStockSearchRequest requestModel);

}
