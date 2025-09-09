package com.hitech.dms.web.service.sparePo;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.SparePoHDRAndPartDetailsModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.spare.create.response.SearchPartsByCategoryRequest;
import com.hitech.dms.web.model.spare.create.response.SpareJobCardResponse;
import com.hitech.dms.web.model.spare.create.response.SparePOTcsTotalAmntResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCalculationResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoStatusResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoTypesResponse;
import com.hitech.dms.web.model.spare.create.response.SubProductCategoryResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoCalculationRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoHeaderRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoTcsCalculationRequest;
import com.hitech.dms.web.model.spare.search.response.partSearchDetailsResponse;
import com.hitech.dms.web.model.spare.search.response.sparePoSearchListResponse;
import com.hitech.dms.web.model.spare.search.resquest.SparePoCancelRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.SparePoUpdateRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.partSerachRequest;

import net.sf.jasperreports.engine.JasperPrint;

public interface createSparePoService {

	/**
	 * 
	 * @param userCode
	 * @param branch 
	 * @param partNumber
	 * @param categoryId 
	 * @return
	 */
	List<partSearchResponseModel> fetchPartNumberByCategory(String userCode,SearchPartsByCategoryRequest searchRequest);

	List<SparePoTypesResponse> getAllTypes();

	List<SparePoStatusResponse> getAllSparePoStatus();

	List<SparePoCategoryResponse> getAllSparePoCategory();

	/**
	 * 
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	SparePoCreateResponseModel createSparePODetails(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device);

	/**
	 * @param branchId
	 * @return
	 */
	List<SpareJobCardResponse> getAllJobCardByBranchId(int branchId);

	/**
	 * @param userCode
	 * @param partId
	 * @param branchId 
	 * @param poCategoryId 
	 * @return
	 */
	List<partSearchDetailsResponse> fetchPartDetails(String userCode, int partId, BigInteger branchId, int poCategoryId);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param branch
	 * @param dealer 
	 * @param productCategoryId 
	 * @param file
	 * @return
	 */
	SparePoPartUploadResponse validateUploadedFile(String authorizationHeader, String userCode, BigInteger branch,
			Integer dealer, Integer productCategoryId, MultipartFile file);

	/**
	 * @param userCode
	 * @param partNumber
	 * @return
	 */
	List<partSearchDetailsResponse> fetchPartDetailsByPartNumber(String userCode, String partNumber);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<SparePoCalculationResponse> calculateSparePOItemAmount(String userCode,
			SparePoCalculationRequest requestModel);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<SparePOTcsTotalAmntResponse> calculateSparePoTcsTotalAmount(String userCode,
			SparePoTcsCalculationRequest requestModel);

	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	SparePoCreateResponseModel saveSparePODetails(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	List<sparePoSearchListResponse> sparePoDataList(String userCode, partSerachRequest requestModel);

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	JasperPrint ExcelGeneratorReport(HttpServletRequest request, String string, HashMap<String, Object> jasperParameter,
			String filePath);

	/**
	 * @param jasperPrint
	 * @param format
	 * @param printStatus
	 * @param outputStream
	 * @param reportName
	 * @throws Exception 
	 */
	void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception;


	/**
	 * @param userCode
	 * @param poHdrId
	 * @return
	 */
	SparePoHDRAndPartDetailsModel sparePoHDRAndPartDetails(String userCode, Integer poHdrId);

	/**
	 * @param userCode
	 * @param sparePoDealerAndDistributorRequest
	 * @return
	 */
	List<SparePoDealerAndDistributerSearchResponse> getDealerAndDistributor(String userCode, SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest);

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
	List<SubProductCategoryResponse> getSubProductCategoryList(Integer category_id);

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	JasperPrint PdfGeneratorReportForSparePo(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath);

	/**
	 * @param userCode
	 * @param device
	 * @param searchText
	 * @return
	 */
	Map<String, Object> getPartyCodeList(String userCode, Device device, String searchText);

	/**
	 * @param userCode
	 * @param device
	 * @param searchText
	 * @return
	 */
	Map<String, Object> getPartyNameList(String userCode, Device device, String searchText);

	

	
}
