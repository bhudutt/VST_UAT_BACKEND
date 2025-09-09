package com.hitech.dms.web.partsStock.Dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.partsStock.Model.PartBranchDetailResponse;
import com.hitech.dms.web.partsStock.Model.PartBranchDetailStatus;
import com.hitech.dms.web.partsStock.Model.PartsStockUploadModel;
import com.hitech.dms.web.partsStock.Model.StockBinDetailResponseModel;
import com.hitech.dms.web.partsStock.Model.StockHeaderWithPartBranchIdModel;
import com.hitech.dms.web.partsStock.Model.StockSaveHeaderResponseModel;
import com.hitech.dms.web.partsStock.Model.StoreSearchResponseModel;
import com.hitech.dms.web.partsStockController.create.response.PartsUploadCreateResponse;

public interface PartsStockUploadCreateDao {
	
	
	public PartsUploadCreateResponse partsStockUploadDao(String authorizationHeader, String userCode,Integer dealer,Integer branch
			,MultipartFile file);

	public PartBranchDetailStatus checkStockBinandUploadStockDetail(List<StoreSearchResponseModel> storeListOfParts,
			Integer branch, String userCode, Integer delaer, List<PartsStockUploadModel> excelListOfParts, List<StoreSearchResponseModel> storeList);

	public StockSaveHeaderResponseModel saveStockHeaderWithPartBranchId(
			List<PartBranchDetailResponse> partBranchDetailResponse, Integer branch, String userCode, Integer delaer,
			List<PartsStockUploadModel> excelListOfParts);

	public StockBinDetailResponseModel updateStockBinDetailTableStockBinId(
			List<StockHeaderWithPartBranchIdModel> stockHeaderList, List<PartsStockUploadModel> excelListOfParts, String userCode);

	
}

