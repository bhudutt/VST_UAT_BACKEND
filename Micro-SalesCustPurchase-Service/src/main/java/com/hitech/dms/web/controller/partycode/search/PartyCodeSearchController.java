package com.hitech.dms.web.controller.partycode.search;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.controller.outletCategory.search.dao.OutCategorySearchDao;
import com.hitech.dms.web.controller.outletCategory.search.dao.PartyDetailFecthDao;
import com.hitech.dms.web.dao.partycode.search.PartyCodeSearchDao;
import com.hitech.dms.web.entity.partycode.PartyCodeEditResponse;
import com.hitech.dms.web.model.outletCategory.search.response.OutletCategoryPartsModel;
import com.hitech.dms.web.model.outletCategory.search.response.OutletCategorySearchResponse;
import com.hitech.dms.web.model.outletCategory.search.response.PartyMasterApproveEntity;
import com.hitech.dms.web.model.partybybranch.create.request.PanGstSearchRequest;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeUpdateRequest;
import com.hitech.dms.web.model.partybybranch.create.request.PartySearchExcelRequst;
import com.hitech.dms.web.model.partycode.search.response.DealerDetailResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCategoryResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCodeSearchMainResponseModel;
import com.hitech.dms.web.model.partycode.search.response.PartyDetailAddressResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyDetailFetchResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyMasterChangeStatusResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyNameResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/partycode")
@SecurityRequirement(name = "hitechApis")
public class PartyCodeSearchController {

	private static final Logger logger = LoggerFactory.getLogger(PartyCodeSearchController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	
	@Value("${file.upload-dir.PartySearchReport}")
	private String partySearchReport;
	
	@Autowired
	private PartyCodeSearchDao searchDao;
	
	
	@Autowired
	OutCategorySearchDao outletCategoryDao;
	
	@Autowired
	PartyDetailFecthDao partyDetailDao;
	
	@PostMapping("/search")
	public ResponseEntity<?> searchAllotList(@RequestBody PartyCodeCreateRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		System.out.println("the request is search  "+requestModel);
		PartyCodeSearchMainResponseModel responseModel = searchDao.searchPartyCodeList(userCode,requestModel);
		//System.out.println("at controller we get list is "+responseModel);
		if (responseModel != null && responseModel.getSearchList() != null
				&& !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party code Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/displayCategoryCode")
	public ResponseEntity<?> getCategoryCode(
			@RequestParam(value = "categoryCode", required = true) Integer categoryId,OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyNameResponseModel> responseModel = searchDao.searchPartyNameList(userCode, categoryId);
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/changeStatus")
	public ResponseEntity<?> changePartyMasterStatus(
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "isActive", required = true) String isActive,
			OAuth2Authentication authentication) {
		String userCode = null;
		PartyMasterChangeStatusResponse responseModel=null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		System.out.println("at controller request "+id +" "+isActive);
		 responseModel = searchDao.updatePartyMasterActiveStatus(id, isActive);
		System.out.println("after response "+responseModel);
		if (responseModel != null 
				&& responseModel.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party master Status is Updated Successfully");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party master Status is  not Updated  or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/approveStatus")
	public ResponseEntity<?> changePartyDetailApproveStatus(
			@RequestBody PartyMasterApproveEntity  request,
			OAuth2Authentication authentication) {
		String userCode = null;
		PartyMasterChangeStatusResponse responseModel=null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		System.out.println("at controller requestsss "+request);
		 responseModel = searchDao.updatePartyApproveStatus(request,userCode);
		System.out.println("after response "+responseModel);
		if (responseModel != null 
				&& responseModel.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party master approve  Status is Updated Successfully");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party master approve Status is  not Updated  or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/dealerAddressDetail")
	public ResponseEntity<?> getDealerAddressDetail(
			@RequestParam(value = "pinId", required = true) String pinId,
			@RequestParam(value = "dealerCode") String dealerCode,
			@RequestParam(value = "branchId") String branchId,

			OAuth2Authentication authentication) {
		String userCode = null;
		List<PartyDetailAddressResponse>  responseModel=null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		System.out.println("at controller request "+pinId +" "+dealerCode+" "+branchId);
		 responseModel = searchDao.getDealerAdressDetail(pinId, dealerCode,branchId);
		System.out.println("after response "+responseModel);
		if (responseModel != null 
				&& responseModel.get(0).getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Adress Detail feteched");
		} else {
			codeResponse.setCode(Integer.toString(501));
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Unable to fetch dealer Address");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	//approveStatus
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/displayCategoryVal")
	public ResponseEntity<?> displayCategoryVal(
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyCategoryResponse> responseModel = searchDao.searchPartyCategoryMaster(userCode);
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/displayOutletCategory")
	public ResponseEntity<?> displayOutletCategoryVal(
			@RequestParam(value = "categoryId", required = true) Integer categoryId,OAuth2Authentication authentication) {
			String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		OutletCategorySearchResponse responseModel=outletCategoryDao.getOutLetCategorySearch(userCode, categoryId.toString());
		System.out.println("response at controller "+responseModel);
		// List<PartyCategoryResponse> responseModel = searchDao.searchPartyCategoryMaster(userCode);
		if (responseModel != null 
				&& responseModel.getStatusCode()==200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OutletCategory   list not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/displayOutletCategoryList")
	public ResponseEntity<?> displayOutletCategoryValList(
			@RequestParam(value = "categoryId", required = true)
			Integer categoryId,
			@RequestParam(value = "partyCode",required = true)

			String partyCode,
			OAuth2Authentication authentication) {
			String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<OutletCategoryPartsModel> responseModel=outletCategoryDao.getOutLetCategoryList(userCode, categoryId.toString(),partyCode);
		System.out.println("response at controller "+responseModel);
		
		// List<PartyCategoryResponse> responseModel = searchDao.searchPartyCategoryMaster(userCode);
		if (responseModel != null 
				) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OutletCategory   list not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchPartyDetails")
	public ResponseEntity<?> fetchPartyDetailsByPartyBranchId(
			@RequestParam(value = "partyBranchId", required = true) Integer partyBranchId,
			@RequestParam(value = "branchId", required = true) Integer branchId,
			@RequestParam(value = "dealerCode", required = true) String dealerCode,
			OAuth2Authentication authentication) {
			String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		System.out.println("params "+branchId +" dealerCode "+dealerCode);
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyDetailFetchResponse responseModel=partyDetailDao.fetchPartyDetalByPartyBranchId(partyBranchId,branchId,dealerCode);
		
		System.out.println("response at controller "+responseModel);
		// List<PartyCategoryResponse> responseModel = searchDao.searchPartyCategoryMaster(userCode);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OutletCategory   list not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/updatePartyDetails")
	public ResponseEntity<?> fetchPartyDetailsByPartyBranchId(
			@RequestBody PartyCodeUpdateRequest requestModel,
			OAuth2Authentication authentication) {
			String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();	
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		System.out.println("PartyCodeCreateRequestModel "+requestModel);
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyCodeEditResponse responseModel=partyDetailDao.updatePartyCode(requestModel, userCode);
		
		//System.out.println("response at controller "+responseModel);
		// List<PartyCategoryResponse> responseModel = searchDao.searchPartyCategoryMaster(userCode);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("OutletCategory   list not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

	
	@PostMapping("/checkExistingPanAndGST")
	public ResponseEntity<?> checkExistingPanOrGST(
			@RequestBody PanGstSearchRequest requestModel,
			OAuth2Authentication authentication) {
			String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();	
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		System.out.println("checkGSTPANEXIST  "+requestModel);
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PanGstSearchRequest> responseModel=partyDetailDao.checkExistPanGst(requestModel, userCode);
		
		if (responseModel != null && responseModel.size()!=0 ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch successfully GST pan list " + formatter.format(new Date()));
			codeResponse.setMessage("GST pan  List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("GST pan  List list not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/exportPartySearchExcel")
	public void exportPartySearchExcelReport(@RequestBody PartySearchExcelRequst requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		printStatus="true";
	   System.out.println("request at controller "+requestModel +"printStatus "+printStatus);
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		String reportName = "PartySearch ";
		
		SimpleDateFormat smdf = getSimpleDateFormat();
		//String fromDate = smdf.format(requestModel.getFromDate());
		//String toDate = smdf.format(requestModel.getToDate());
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("CategoryCode",requestModel.getCategoryCode()!=null?requestModel.getCategoryCode():null);
		jasperParameter.put("PartyCode", requestModel.getPartyCode()!=null?requestModel.getPartyCode():null);
		jasperParameter.put("PartyName", requestModel.getPartyName()!=null?requestModel.getPartyName():null);
		jasperParameter.put("UserCode", userCode!=null?userCode:null);
		jasperParameter.put("status", requestModel.getStatus()!=null?requestModel.getStatus():null);
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = partySearchReport;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/CouponManagement/";
		}
		System.out.println("at excel generate ");
		JasperPrint jasperPrint = partyDetailDao.ExcelGeneratorReportdao(request, "SEARCHPARTYMASTER.jasper", jasperParameter,
				filePath);
		System.out.println("after excel generate");
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				

				response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".xlsx");
				response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			}

			outputStream = response.getOutputStream();

			partyDetailDao.printReport1(jasperPrint, format, printStatus,outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}

		
		
		
		
		
	}
	
	
}
