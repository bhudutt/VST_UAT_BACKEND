package com.hitech.dms.web.serviceImpl.sparePo;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.create.spare.createSpareDao;
import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.SparePIdAndPnoModel;
import com.hitech.dms.web.model.SpareModel.SparePoPartDltForExcel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.SpareModel.partSerachDetailsByPohdrIdResponseModel;
import com.hitech.dms.web.model.SpareModel.SparePoHDRAndPartDetailsModel;
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
import com.hitech.dms.web.service.sparePo.createSparePoService;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
public class createSparePoServiceImpl implements createSparePoService {

	@Autowired
	public createSpareDao spareDao;
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	

	@Override
	public List<partSearchResponseModel> fetchPartNumberByCategory(String userCode,
			SearchPartsByCategoryRequest searchRequest) {
		List<partSearchResponseModel> partList = spareDao.fetchPartList(userCode, searchRequest);
		return partList;
	}

	@Override
	public List<SparePoTypesResponse> getAllTypes() {
		List<SparePoTypesResponse> responeModel = spareDao.getTypes();
		return responeModel;
	}

	@Override
	public List<SparePoStatusResponse> getAllSparePoStatus() {
		List<SparePoStatusResponse> responseModel = spareDao.fetchSparePoStatus();
		return responseModel;
	}

	@Override
	public List<SparePoCategoryResponse> getAllSparePoCategory() {
		List<SparePoCategoryResponse> responseModel = spareDao.fetchSparePoAllCategory();
		return responseModel;
	}

	@Override
	public SparePoCreateResponseModel createSparePODetails(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device) {
		SparePoCreateResponseModel createResponeModel = spareDao.createSparePo(authorizationHeader, userCode,
				requestModel, device);
		return createResponeModel;
	}

	@Override
	public List<SpareJobCardResponse> getAllJobCardByBranchId(int branchId) {
		List<SpareJobCardResponse> jobCardResponse = spareDao.fetchAllJobCardByBranchId(branchId);
		return jobCardResponse;
	}

	@Override
	public List<partSearchDetailsResponse> fetchPartDetails(String userCode, int partId, BigInteger branchId,
			int poCategoryId) {
		List<partSearchDetailsResponse> parldltResponse = spareDao.fetchPartDetailsByPartId(userCode, partId, branchId,
				poCategoryId);
		return parldltResponse;
	}

	@Override
	public SparePoPartUploadResponse validateUploadedFile(String authorizationHeader, String userCode,
			BigInteger branch, Integer dealer, Integer productCategoryId, MultipartFile file) {
		SparePoPartUploadResponse spareUploadResponse = spareDao.uploadSparePoParts(authorizationHeader, userCode,
				branch, productCategoryId, file);
		System.out.println("spareUploadResponse:::::::::" + spareUploadResponse);
		List<SparePoPartDltForExcel> partForExcelList = null;
		Map<String, String> errorPartData = spareUploadResponse.getErrorPartData();
		Map<String, String> errorData = new HashMap<>();
		try {
			if (spareUploadResponse.getPartAndQty() != null || spareUploadResponse.getStatusCode() != 201) {
				partForExcelList = new ArrayList<SparePoPartDltForExcel>();
//				System.out.println("Service.........." + spareUploadResponse.getPartAndQty());
				Map<String, Integer> data = spareUploadResponse.getPartAndQty();
				int index = 0;
				for (var entry : data.entrySet()) {
					index++;
//					System.out.println(entry.getKey() + "/" + entry.getValue());
					List<partSearchDetailsResponse> parldltResponse = spareDao.fetchPartDetailsByPartNumber(userCode,
							branch, entry.getKey(), productCategoryId);
					System.out.println("parldltResponse:::::" + parldltResponse);
					if (parldltResponse != null && !parldltResponse.isEmpty()) {
						partSearchDetailsResponse parstResponse = parldltResponse.get(0);
						SparePoCalculationRequest requestModel = new SparePoCalculationRequest();
						BigInteger big_branch = branch;
						BigInteger big_dealer = BigInteger.valueOf(dealer);
						requestModel.setBranchId(big_branch);
						requestModel.setDealerId(big_dealer);
						BigInteger big_partId = BigInteger.valueOf(parstResponse.getPartId());
						requestModel.setPartId(big_partId);
						requestModel.setQty(entry.getValue());
						List<SparePoCalculationResponse> calculationResponse = spareDao
								.fetchSparePoItemAmountCal(userCode, requestModel);
						// System.out.println("parldltResponse::::::::::::::" + parstResponse);
						// System.out.println("calculationResponse::::::::::::::" +
						// calculationResponse);
						if (calculationResponse != null) {
							SparePoPartDltForExcel partForExcel = new SparePoPartDltForExcel();
							SparePIdAndPnoModel pid = new SparePIdAndPnoModel();
							pid.setPartId(parstResponse.getPartId());
							pid.setPartNo(parstResponse.getPartNumber());
							partForExcel.setPartNo(parstResponse.getPartNumber());
							partForExcel.setPartId(parstResponse.getPartId());
							partForExcel.setPIdAndPno(pid);
							partForExcel.setPartDescription(parstResponse.getPartDesc());
							partForExcel.setProdSubCat(parstResponse.getProductSubCategory());
							partForExcel.setPackQty(parstResponse.getPackQty());
							partForExcel.setMinOrderQty(parstResponse.getMinOrderQty());
							partForExcel.setCurrentStock(parstResponse.getCurrentStock());
							partForExcel.setBackOrderQty(parstResponse.getBackOrderQty());
							partForExcel.setTransitQty(parstResponse.getTransitQty());
							partForExcel.setMrpPrice(parstResponse.getBasicUnitPrice());
							partForExcel.setQuantity(entry.getValue());
							if (entry.getValue() % parstResponse.getPackQty() == 0) {
								partForExcel.setNetAmount(calculationResponse.get(0).getNetAmount());
								partForExcel.setGstAmount(calculationResponse.get(0).getItemGstAmount());
								partForExcel.setGstPercent(calculationResponse.get(0).getItemGstPer());
								partForExcel.setTotalAmount(calculationResponse.get(0).getTotalAmount());
							} else {
								BigDecimal val = BigDecimal.valueOf(0.00);
								partForExcel.setNetAmount(val);
								partForExcel.setGstAmount(val);
								partForExcel.setGstPercent(val);
								partForExcel.setTotalAmount(val);
							}
							partForExcel.setSoNo(parstResponse.getSONO());
							partForExcel.setSoDate(parstResponse.getSODate());
							partForExcel.setSapRemarks(parstResponse.getSAPRemarks());
							partForExcel.setViewImage(parstResponse.getViewImage());
							partForExcelList.add(partForExcel);

						} else {
							errorData.put(String.valueOf(index),
									"This part does not have a price set. You need to set it. ("
											+ entry.getKey() + ").");
							spareUploadResponse.setErrorPartData(errorData);
						}

					} else if (spareUploadResponse.getPartAndQty() != null|| spareUploadResponse.getPartAndQty().isEmpty()) {
						
						System.out.println(spareUploadResponse.getErrorPartData());
						if (errorPartData != null && !errorPartData.isEmpty()) {
							errorData.put(String.valueOf(index),
									"This part No us duplicate ("
											+ entry.getKey() + ").");
							spareUploadResponse.setErrorPartData(errorPartData);
						} else {
//							spareUploadResponse
//									.setMsg("you selected partcategetory doesn't match with uploaded the parts");
//							spareUploadResponse.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
							errorData.put(String.valueOf(index),
									"The selected part category does not match with the uploaded part ("
											+ entry.getKey() + ").");
							spareUploadResponse.setErrorPartData(errorData);
						}
					}

				}
			} else {
				spareUploadResponse.setMsg("Something wents wrong !!!");
				spareUploadResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

			spareUploadResponse.setPartForExcelList(partForExcelList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return spareUploadResponse;

	}

	@Override
	public List<partSearchDetailsResponse> fetchPartDetailsByPartNumber(String userCode, String partNumber) {
		// return spareDao.fetchPartDetailsByPartNumber(userCode, partNumber);
		return null;
	}

	@Override
	public List<SparePoCalculationResponse> calculateSparePOItemAmount(String userCode,
			SparePoCalculationRequest requestModel) {
		return spareDao.fetchSparePoItemAmountCal(userCode, requestModel);
	}

	@Override
	public List<SparePOTcsTotalAmntResponse> calculateSparePoTcsTotalAmount(String userCode,
			SparePoTcsCalculationRequest requestModel) {
		return spareDao.fetchSparePoTcsAmntCal(userCode, requestModel);
	}

	@Override
	public SparePoCreateResponseModel saveSparePODetails(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device) {
		return spareDao.saveSparePoform(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public List<sparePoSearchListResponse> sparePoDataList(String userCode, partSerachRequest requestModel) {
		return spareDao.fetchSparePoDataForSerach(userCode, requestModel);
	}

	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String downloadPaths) {

		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePath = downloadPaths + jaspername;
			System.out.println("filePath  " + filePath);
			connection = dataSourceConnection.getConnection();
			System.out.println("connection " + connection);
			if (connection != null) {
				System.out.println("jasperParameter" + jasperParameter);
				System.out.println("filePath" + filePath);
				jasperPrint = JasperFillManager.fillReport(filePath, jasperParameter, connection);

				System.out.println(jasperPrint.getName());
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception {

		if (format != null && format.equalsIgnoreCase("xls")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
		} else if (format != null && format.equalsIgnoreCase("pdf")) {
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			if (printStatus != null && printStatus.equals("true")) {
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
				configuration.setPdfJavaScript("this.print();");
			}
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}
	}

	@Override
	public SparePoHDRAndPartDetailsModel sparePoHDRAndPartDetails(String userCode, Integer poHdrId) {
		SparePoHDRAndPartDetailsModel responseModel = new SparePoHDRAndPartDetailsModel();
		List<SparePoHdrDetailsResponse> hdrDetailsResponse = spareDao.fetchPoHdrDetailsById(userCode, poHdrId);
		List<partSerachDetailsByPohdrIdResponseModel> partDetailsResponse = spareDao
				.fetchPoPartDetailsByPohdrId(userCode, poHdrId);
		responseModel.setHdrDetailsResponse(hdrDetailsResponse);
		responseModel.setPartDetailsResponse(partDetailsResponse);
		return responseModel;
	}

	@Override
	public List<SparePoDealerAndDistributerSearchResponse> getDealerAndDistributor(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest) {
		return spareDao.fetchDealerAndDistributorList(userCode, sparePoDealerAndDistributorRequest);
	}

	@Override
	public SparePoCreateResponseModel updateSparePO(String authorizationHeader, String userCode,
			SparePoUpdateRequestModel requestModel, Device device) {
		return spareDao.updateSparePO(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public SparePoCreateResponseModel cancelSparePO(String authorizationHeader, String userCode,
			SparePoCancelRequestModel requestModel, Device device) {
		return spareDao.cancelSparePO(authorizationHeader, userCode, requestModel, device);
	}

	@Override
	public List<SubProductCategoryResponse> getSubProductCategoryList(Integer category_id) {

		return spareDao.fetchSubCategoryList(category_id);
	}

	@Override
	public JasperPrint PdfGeneratorReportForSparePo(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			// String filePath =
			// ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();

			String filePathsVariable = filePath + jasperName;
			System.out.println("filePath  " + filePathsVariable);
			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePathsVariable, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	@Override
	public Map<String, Object> getPartyCodeList(String userCode, Device device, String searchText) {
		
		
		return  spareDao.getPartyCodeList( userCode, searchText);
	}

	@Override
	public Map<String, Object> getPartyNameList(String userCode, Device device, String searchText) {
		
		return spareDao.getPartyNameList( userCode, searchText);
	}
}
