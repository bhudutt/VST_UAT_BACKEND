package com.hitech.dms.web.partsStockController.create.response;

import java.util.List;
import java.util.Map;

import com.hitech.dms.web.entity.user.SparePartsStockEntity;
import com.hitech.dms.web.entity.user.StockUploadStagingEntity;
import com.hitech.dms.web.partsStock.Model.PartsStockUploadModel;
import com.hitech.dms.web.partsStock.Model.StoreSearchResponseModel;

import lombok.Data;


@Data
public class PartsUploadCreateResponse {
	
	private Map<String ,String> errorPartsData;
	private Map<String, Integer> partQuantity;
	private List<PartsStockUploadModel> excelListOfParts;
	private List<SparePartsStockEntity> SparePartsList;
	private List<StockUploadStagingEntity> StagingPartsEntity;
	private List<StoreSearchResponseModel>  storeListOfParts;
	private Integer statusCode;
	private Integer totalRowCount;
	private String message;
	
	
	
	

}
