package com.hitech.dms.web.dao.create.spare;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.SpareModel.partSerachDetailsByPohdrIdResponseModel;
import com.hitech.dms.web.model.spare.create.response.SearchPartsByCategoryRequest;
import com.hitech.dms.web.model.spare.create.response.SpareJobCardResponse;
import com.hitech.dms.web.model.spare.create.response.SparePOTcsTotalAmntResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCalculationResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoStatusResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoTypesResponse;
import com.hitech.dms.web.model.spare.create.response.SubProductCategoryResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoCalculationRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoHeaderRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoRequestModel;
import com.hitech.dms.web.model.spare.create.resquest.SparePoTcsCalculationRequest;
import com.hitech.dms.web.model.spare.search.response.SparePoHdrDetailsResponse;
import com.hitech.dms.web.model.spare.search.response.partSearchDetailsResponse;
import com.hitech.dms.web.model.spare.search.response.sparePoSearchListResponse;
import com.hitech.dms.web.model.spare.search.resquest.SparePoCancelRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.SparePoUpdateRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.partSerachRequest;

public interface createSpareDao {

	List<partSearchResponseModel> fetchPartList(String userCode,SearchPartsByCategoryRequest searchRequest);

	List<SparePoTypesResponse> getTypes();

	List<SparePoStatusResponse> fetchSparePoStatus();

	List<SparePoCategoryResponse> fetchSparePoAllCategory();

	SparePoCreateResponseModel createSparePo(String authorizationHeader, String userCode, SparePoHeaderRequest requestModel,
			Device device);

	/**
	 * @param branchId 
	 * @return
	 */
	List<SpareJobCardResponse> fetchAllJobCardByBranchId(int branchId);

	/**
	 * @param userCode 
	 * @param partId
	 * @param poCategoryId 
	 * @return
	 */
	List<partSearchDetailsResponse> fetchPartDetailsByPartId(String userCode, int partId,BigInteger branchId, int poCategoryId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param branch
	 * @param file
	 * @return
	 */
	SparePoPartUploadResponse uploadSparePoParts(String authorizationHeader, String userCode, BigInteger branch,
			Integer productCategoryId,MultipartFile file);

	/**
	 * @param userCode
	 * @param branch 
	 * @param partNumber
	 * @param productCategoryId 
	 * @return
	 */
	List<partSearchDetailsResponse> fetchPartDetailsByPartNumber(String userCode, BigInteger branch, String partNumber, Integer productCategoryId);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<SparePoCalculationResponse> fetchSparePoItemAmountCal(String userCode, SparePoCalculationRequest requestModel);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<SparePOTcsTotalAmntResponse> fetchSparePoTcsAmntCal(String userCode,
			SparePoTcsCalculationRequest requestModel);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	SparePoCreateResponseModel saveSparePoform(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<sparePoSearchListResponse> fetchSparePoDataForSerach(String userCode, partSerachRequest requestModel);

	/**
	 * @param userCode
	 * @param poHdrId
	 * @return
	 */
	List<SparePoHdrDetailsResponse> fetchPoHdrDetailsById(String userCode, Integer poHdrId);

	/**
	 * @param userCode
	 * @param poHdrId
	 * @return
	 */
	List<partSerachDetailsByPohdrIdResponseModel> fetchPoPartDetailsByPohdrId(String userCode, Integer poHdrId);

	/**
	 * @param userCode
	 * @param sparePoDealerAndDistributorRequest
	 * @return
	 */
	List<SparePoDealerAndDistributerSearchResponse> fetchDealerAndDistributorList(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	SparePoCreateResponseModel updateSparePO(String authorizationHeader, String userCode,
			SparePoUpdateRequestModel requestModel, Device device);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	SparePoCreateResponseModel cancelSparePO(String authorizationHeader, String userCode,
			SparePoCancelRequestModel requestModel, Device device);

	/**
	 * @param category_id 
	 * @return
	 */
	List<SubProductCategoryResponse> fetchSubCategoryList(Integer category_id);

	/**
	 * @param userCode
	 * @param searchText
	 * @return
	 */
	Map<String, Object> getPartyCodeList(String userCode, String searchText);

	/**
	 * @param userCode
	 * @param searchText
	 * @return
	 */
	Map<String, Object> getPartyNameList(String userCode, String searchText);

	

}
