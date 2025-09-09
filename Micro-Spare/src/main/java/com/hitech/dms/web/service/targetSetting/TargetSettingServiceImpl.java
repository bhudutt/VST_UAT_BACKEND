package com.hitech.dms.web.service.targetSetting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.spare.grn.mapping.SpareGRNDao;
import com.hitech.dms.web.dao.spare.party.mapping.DealerDistributorMappingDao;
import com.hitech.dms.web.dao.spare.targetSetting.TargetSettingDao;
import com.hitech.dms.web.entity.targetSetting.TargetSettingDtlEntity;
import com.hitech.dms.web.entity.targetSetting.TargetSettingHdrEntity;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.model.targetSetting.request.TargetSettingRequestModel;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingDtlResponse;
import com.hitech.dms.web.model.targetSetting.response.TargetSettingResponseModel;

@Repository
public class TargetSettingServiceImpl implements TargetSettingService {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private TargetSettingDao targetSettingDao;

	@Autowired
	private SpareGRNDao spareGRNDao;

	@Autowired
	private DealerDistributorMappingDao dealerDistributorMappingDao;

	private static final Logger logger = LoggerFactory.getLogger(TargetSettingServiceImpl.class);

	@Override
	public HashMap<BigInteger, String> fetchTargetFor(String userType) {
		return targetSettingDao.fetchTargetFor(userType);
	}

	@Override
	public List<SparePoCategoryResponse> getProductCategory(BigInteger partyCategoryId) {
		return targetSettingDao.getProductCategory(partyCategoryId);
	}

	@Override
	public TargetSettingResponseModel uploadExcel(String authorizationHeader, String userCode,
			TargetSettingRequestModel targetSettingRequestModel, MultipartFile file) {
		TargetSettingResponseModel targetSettingResponseModel = new TargetSettingResponseModel();

		targetSettingResponseModel = validateUploadedFile(authorizationHeader, userCode, targetSettingRequestModel,
				file);
		if (targetSettingResponseModel != null) {
//			targetSettingResponseModel.setTargetNumber(null);
//			targetSettingResponseModel
//					.setTargetSettingDtlResponse(targetSettingResponseModel.getTargetSettingDtlResponse());
//			targetSettingResponseModel.setTotalTarget(targetSettingResponseModel.getTotalTarget());
			targetSettingResponseModel.setMsg("Target Setting Uploaded Successfully.");
			targetSettingResponseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
		}
		return targetSettingResponseModel;
	}

	@Override
	public TargetSettingResponseModel submitTargetData(String authorizationHeader, String userCode,
			TargetSettingRequestModel targetSettingRequestModel, MultipartFile file) {
		TargetSettingResponseModel targetSettingResponseModel = new TargetSettingResponseModel();

		targetSettingResponseModel = validateUploadedFile(authorizationHeader, userCode, targetSettingRequestModel,
				file);
		if (targetSettingResponseModel != null) {
			// insert records into table

			targetSettingResponseModel = saveTargetData(userCode, targetSettingRequestModel,
					targetSettingResponseModel.getTargetSettingDtlResponse(), new Date(),
					targetSettingResponseModel.getTotalTarget());

			targetSettingResponseModel.setMsg("Target Setting Uploaded Successfully.");
			targetSettingResponseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
		}
		return targetSettingResponseModel;
	}

	public TargetSettingResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			TargetSettingRequestModel targetSettingRequestModel, MultipartFile file) {

//		logger.debug("validateUploadedFile : " + spareUploadRequestModel.toString());
		logger.debug("userCode validateUploadedFile : " + userCode);
		TargetSettingResponseModel responseModel = new TargetSettingResponseModel();
		boolean isSuccess = true;
		String msg = null;
		try {
			ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("PARTY/DSR CODE", "APR", "MAY", "JUN",
					"JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "JAN", "FEB", "MAR"));

			Map<Integer, String> map = new HashMap<Integer, String>();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(file.getInputStream());
			logger.debug("workbook - ", workbook);

			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			logger.debug("totalRows - ", totalRows);

			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				Cell cell = row.getCell(colIx);
				logger.info("cell - ", cell.getStringCellValue());
				if (staticHeaderList.contains(cell.getStringCellValue().trim())) {
					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
				} else {
					logger.error("INVALID EXCEL FORMAT.");
					responseModel.setMsg("INVALID EXCEL FORMAT.");
					responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
					isSuccess = false;
					break;
				}
			}

			TargetSettingDtlResponse targetSettingDtlResponse = null;
			List<TargetSettingDtlResponse> targetSettingDtlResponseList = null;

			TargetSettingHdrEntity targetSettingHdrEntity = null;
//
//			TargetSettingDtlEntity targetSettingDtlEntity = null;
//			List<TargetSettingDtlEntity> targetSettingDtlEntityList = null;
			Date currDate = new Date();
			Double totalTarget = (double) 0;

			Double aprTotalTarget = (double) 0;
			Double mayTotalTarget = (double) 0;
			Double junTotalTarget = (double) 0;
			Double julTotalTarget = (double) 0;
			Double augTotalTarget = (double) 0;
			Double sepTotalTarget = (double) 0;
			Double octTotalTarget = (double) 0;
			Double novTotalTarget = (double) 0;
			Double decTotalTarget = (double) 0;
			Double janTotalTarget = (double) 0;
			Double febTotalTarget = (double) 0;
			Double marTotalTarget = (double) 0;

			if (isSuccess) {
//				targetSettingDtlEntityList = new ArrayList<TargetSettingDtlEntity>();
				String record = null;
//				targetSettingDtlEntity = new TargetSettingDtlEntity();
				targetSettingDtlResponseList = new ArrayList<TargetSettingDtlResponse>();

				for (int x = 1; x < totalRows; x++) {
					targetSettingDtlResponse = new TargetSettingDtlResponse();

					Row dataRow = sheet.getRow(x);
					int k = 1;
					if (!isSuccess) {
						break;
					}
					try {
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {

							Cell cell = dataRow.getCell(colIx);
							String cellName = cell.getSheet().getRow(0).getCell(colIx).getRichStringCellValue()
									.toString();

							if (cellName.equalsIgnoreCase("PARTY/DSR CODE")) {
								record = checkandreturnCellVal(cell);

								String partyCode = null;
								String partyName = null;
								String partyLocation = null;
								String pinCode = null;

								if (targetSettingRequestModel.getTargetFor().equalsIgnoreCase("AUTHORISED RETAILER")) {
									PartyCodeSearchResponse partyCodeSearchResponse = getPartyId(record,
											targetSettingRequestModel.getTargetFor(), userCode);
									partyCode = partyCodeSearchResponse.getPartyCode();
									partyName = partyCodeSearchResponse.getPartyName();
									partyLocation = partyCodeSearchResponse.getPartyLocation();
									pinCode = partyCodeSearchResponse.getPinCode();

									targetSettingDtlResponse.setPartyId(partyCodeSearchResponse.getPartyBranchId());
									targetSettingDtlResponse
											.setParentDealerLocation(partyCodeSearchResponse.getPartyLocation());
									targetSettingDtlResponse.setDealerPincode(partyCodeSearchResponse.getPinCode());
								} else {
									DealerCodeSearchResponse dealerCodeSearchResponse = getDelerId(record,
											targetSettingRequestModel.getTargetFor(), userCode);

									partyCode = dealerCodeSearchResponse.getDistributorCode();
									partyName = dealerCodeSearchResponse.getParentDealerName();
									partyLocation = dealerCodeSearchResponse.getParentDealerLocation();
									pinCode = dealerCodeSearchResponse.getDealerPincode();
									targetSettingDtlResponse.setPartyId(dealerCodeSearchResponse.getParent_dealer_id());
								}
								if(targetSettingRequestModel.getTargetHdrId() == null) {
									targetSettingDtlResponse = targetSettingDao.checkIfPartyAlreadyExist(
											targetSettingRequestModel.getTargetFor(),
											targetSettingRequestModel.getProductCategory(),
											targetSettingDtlResponse.getPartyId());									
								}
								targetSettingDtlResponse.setPartyCode(partyCode);
								targetSettingDtlResponse.setParentDealerName(partyName);
								targetSettingDtlResponse.setParentDealerLocation(partyLocation);
								targetSettingDtlResponse.setDealerPincode(pinCode);

							} else if (cellName.equalsIgnoreCase("APR")) {
								if (targetSettingDtlResponse.getApr() != null && targetSettingDtlResponse.getApr() > 0) {
									targetSettingDtlResponse.setApr((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setApr(numericVal);
									aprTotalTarget = Double.sum(aprTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("MAY")) {
								if (targetSettingDtlResponse.getMay() != null && targetSettingDtlResponse.getMay() > 0) {
									targetSettingDtlResponse.setMay((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setMay(numericVal);
									mayTotalTarget = Double.sum(mayTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("JUN")) {
								if (targetSettingDtlResponse.getJun() != null && targetSettingDtlResponse.getJun() > 0) {
									targetSettingDtlResponse.setJun((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setJun(numericVal);
									junTotalTarget = Double.sum(junTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("JUL")) {
								if (targetSettingDtlResponse.getJul() != null && targetSettingDtlResponse.getJul() > 0) {
									targetSettingDtlResponse.setJul((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setJul(numericVal);
									julTotalTarget = Double.sum(julTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("AUG")) {
								if (targetSettingDtlResponse.getAug() != null && targetSettingDtlResponse.getAug() > 0) {
									targetSettingDtlResponse.setAug((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setAug(numericVal);
									augTotalTarget = Double.sum(augTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("SEP")) {
								if (targetSettingDtlResponse.getSep() != null && targetSettingDtlResponse.getSep() > 0) {
									targetSettingDtlResponse.setSep((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setSep(numericVal);
									sepTotalTarget = Double.sum(sepTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("OCT")) {
								if (targetSettingDtlResponse.getOct() != null && targetSettingDtlResponse.getOct() > 0) {
									targetSettingDtlResponse.setOct((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setOct(numericVal);
									octTotalTarget = Double.sum(octTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("NOV")) {
								if (targetSettingDtlResponse.getNov() != null && targetSettingDtlResponse.getNov() > 0) {
									targetSettingDtlResponse.setNov((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setNov(numericVal);
									novTotalTarget = Double.sum(novTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("DEC")) {
								if (targetSettingDtlResponse.getDec() != null && targetSettingDtlResponse.getDec() > 0) {
									targetSettingDtlResponse.setDec((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setDec(numericVal);
									decTotalTarget = Double.sum(decTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("JAN")) {
								if (targetSettingDtlResponse.getJan() != null && targetSettingDtlResponse.getJan() > 0) {
									targetSettingDtlResponse.setJan((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setJan(numericVal);
									janTotalTarget = Double.sum(janTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("FEB")) {
								if (targetSettingDtlResponse.getFeb() != null && targetSettingDtlResponse.getFeb() > 0) {
									targetSettingDtlResponse.setFeb((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setFeb(numericVal);
									febTotalTarget = Double.sum(febTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							} else if (cellName.equalsIgnoreCase("MAR")) {
								if (targetSettingDtlResponse.getMar() != null && targetSettingDtlResponse.getMar() > 0) {
									targetSettingDtlResponse.setMar((double) 0);
								} else {
									record = checkandreturnCellVal(cell);
									Double numericVal = cell.getNumericCellValue();
									targetSettingDtlResponse.setMar(numericVal);
									marTotalTarget = Double.sum(marTotalTarget, numericVal);
									totalTarget = Double.sum(totalTarget, numericVal);
								}
							}

//							if (colIx == 0) {
//								record = checkandreturnCellVal(cell);
//							
//								if(targetSettingRequestModel.getTargetFor().equalsIgnoreCase("AUTHORISED RETAILER")) {
//									PartyCodeSearchResponse partyCodeSearchResponse = getPartyId(record,
//											targetSettingRequestModel.getTargetFor(), userCode);
//
//									targetSettingDtlResponse.setPartyCode(partyCodeSearchResponse.getPartyCode());
//									targetSettingDtlResponse.setPartyId(partyCodeSearchResponse.getPartyBranchId());
//									targetSettingDtlResponse.setParentDealerName(partyCodeSearchResponse.getPartyName());
//									targetSettingDtlResponse.setParentDealerLocation(partyCodeSearchResponse.getPartyLocation());;
//									targetSettingDtlResponse.setDealerPincode(partyCodeSearchResponse.getPinCode());									
//								} else {
//									DealerCodeSearchResponse dealerCodeSearchResponse = getDelerId(record,
//											targetSettingRequestModel.getTargetFor(), userCode);
//
//									targetSettingDtlResponse.setPartyCode(dealerCodeSearchResponse.getDistributorCode());
//									targetSettingDtlResponse.setPartyId(dealerCodeSearchResponse.getParent_dealer_id());
//									targetSettingDtlResponse.setParentDealerName(dealerCodeSearchResponse.getParentDealerName());
//									targetSettingDtlResponse.setParentDealerLocation(dealerCodeSearchResponse.getParentDealerLocation());;
//									targetSettingDtlResponse.setDealerPincode(dealerCodeSearchResponse.getDealerPincode());									
//								}
//							} else if (colIx == 1) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setApr(numericVal);
//								aprTotalTarget = Double.sum(aprTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 2) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setMay(numericVal);
//								mayTotalTarget = Double.sum(mayTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 3) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setJun(numericVal);
//								junTotalTarget = Double.sum(junTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 4) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setJul(numericVal);
//								julTotalTarget = Double.sum(julTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 5) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setAug(numericVal);
//								augTotalTarget = Double.sum(augTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 6) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setSep(numericVal);
//								sepTotalTarget = Double.sum(sepTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 7) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setOct(numericVal);
//								octTotalTarget = Double.sum(octTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 8) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setNov(numericVal);
//								novTotalTarget = Double.sum(novTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 9) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setDec(numericVal);
//								decTotalTarget = Double.sum(decTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 10) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setJan(numericVal);
//								janTotalTarget = Double.sum(janTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 11) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setFeb(numericVal);
//								febTotalTarget = Double.sum(febTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							} else if (colIx == 12) {
//								record = checkandreturnCellVal(cell);
//								Double numericVal = cell.getNumericCellValue();
//								targetSettingDtlResponse.setMar(numericVal);
//								marTotalTarget = Double.sum(marTotalTarget, numericVal);
//								totalTarget = Double.sum(totalTarget, numericVal);
//							}
						}

						targetSettingDtlResponseList.add(targetSettingDtlResponse);
					} catch (Exception ex) {
						isSuccess = false;
						if (msg == null) {
							msg = ex.getMessage();
						}
						logger.error(this.getClass().getName(), ex);
						break;
					}
				}
//				set total target month
//				targetSettingDtlResponse.setApr(aprTotalTarget);
//				targetSettingDtlResponse.setMay(mayTotalTarget);
//				targetSettingDtlResponse.setJun(junTotalTarget);
//				targetSettingDtlResponse.setJul(julTotalTarget);
//				targetSettingDtlResponse.setAug(augTotalTarget);
//				targetSettingDtlResponse.setSep(sepTotalTarget);
//				targetSettingDtlResponse.setOct(octTotalTarget);
//				targetSettingDtlResponse.setNov(novTotalTarget);
//				targetSettingDtlResponse.setDec(decTotalTarget);
//				targetSettingDtlResponse.setJan(janTotalTarget);
//				targetSettingDtlResponse.setFeb(febTotalTarget);
//				targetSettingDtlResponse.setMar(marTotalTarget);
//				targetSettingDtlResponseList.add(targetSettingDtlResponse);
				responseModel.setTargetSettingDtlResponse(targetSettingDtlResponseList);
			} else {
				// error
				if (msg == null) {
					msg = "Error While Uploading Spare Target.";
				}
				isSuccess = false;
				logger.error("error");
				logger.error(msg);
				responseModel.setMsg(msg);
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		} catch (Exception ex) {
			isSuccess = false;
			if (msg == null) {
				msg = ex.getMessage();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		}
		if (!isSuccess) {
			// error
			if (msg == null) {
				msg = "Error While Uploading Target.";
			}
			responseModel.setMsg(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;

	}

	private TargetSettingResponseModel saveTargetData(String userCode,
			TargetSettingRequestModel targetSettingRequestModel,
			List<TargetSettingDtlResponse> targetSettingDtlResponseList, Date currDate, Double totalTarget) {

		TargetSettingResponseModel targetSettingResponseModel = new TargetSettingResponseModel();
		TargetSettingHdrEntity targetSettingHdrEntity = null;
		TargetSettingDtlEntity targetSettingDtlEntity = null;
		List<TargetSettingDtlEntity> targetSettingDtlEntityList = null;

		Session session = null;
		session = sessionFactory.openSession();

		try {
			targetSettingHdrEntity = new TargetSettingHdrEntity();

			if (targetSettingRequestModel.getTargetHdrId() != null) {
				targetSettingHdrEntity.setTargetNumber(targetSettingRequestModel.getTargetNumber());
			
			} else {
				targetSettingHdrEntity.setTargetNumber(
						commonDao.getDocumentNumber("TRS", targetSettingRequestModel.getBranchId(), session));
			}
			targetSettingHdrEntity.setTargetFor(targetSettingRequestModel.getTargetFor());
			targetSettingHdrEntity.setTargetDate(targetSettingRequestModel.getTargetDate());
			targetSettingHdrEntity.setProductCategory(targetSettingRequestModel.getProductCategory());
			targetSettingHdrEntity.setOutletCategory(targetSettingRequestModel.getOutletCategory());
			targetSettingHdrEntity.setCreatedBy(userCode);
			targetSettingHdrEntity.setCreatedDate(currDate);

			targetSettingHdrEntity.setTotalTarget(totalTarget);

			targetSettingDtlEntityList = new ArrayList<TargetSettingDtlEntity>();
			for (TargetSettingDtlResponse targetSettingDtlResponse : targetSettingDtlResponseList) {
				targetSettingDtlEntity = new TargetSettingDtlEntity();
				targetSettingDtlEntity.setPartyId(targetSettingDtlResponse.getPartyId());
				targetSettingDtlEntity.setApr(targetSettingDtlResponse.getApr());
				targetSettingDtlEntity.setMay(targetSettingDtlResponse.getMay());
				targetSettingDtlEntity.setJun(targetSettingDtlResponse.getJun());
				targetSettingDtlEntity.setJul(targetSettingDtlResponse.getJul());
				targetSettingDtlEntity.setAug(targetSettingDtlResponse.getAug());
				targetSettingDtlEntity.setSep(targetSettingDtlResponse.getSep());
				targetSettingDtlEntity.setOct(targetSettingDtlResponse.getOct());
				targetSettingDtlEntity.setNov(targetSettingDtlResponse.getNov());
				targetSettingDtlEntity.setDec(targetSettingDtlResponse.getDec());
				targetSettingDtlEntity.setJan(targetSettingDtlResponse.getJan());
				targetSettingDtlEntity.setFeb(targetSettingDtlResponse.getFeb());
				targetSettingDtlEntity.setMar(targetSettingDtlResponse.getMar());
				targetSettingDtlEntityList.add(targetSettingDtlEntity);
			}

			targetSettingResponseModel = targetSettingDao.saveTargetSettingData(targetSettingHdrEntity,
					targetSettingDtlEntityList, targetSettingRequestModel.getTargetHdrId(), userCode);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return targetSettingResponseModel;
	}

	private PartyCodeSearchResponse getPartyId(String partyCode, String isFor, String userCode) {
		PartyCodeSearchResponse partyCodeSearchResponse = null;
		List<PartyCodeSearchResponse> partyCodeSearchResponseList = spareGRNDao.searchPartyCode(partyCode, userCode);
		if (partyCodeSearchResponseList != null) {
			partyCodeSearchResponse = partyCodeSearchResponseList.get(0);
		}
		return partyCodeSearchResponse;
	}

	private DealerCodeSearchResponse getDelerId(String dealerCode, String isFor, String userCode) {
		DealerCodeSearchResponse dealerCodeSearchResponse = null;
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = dealerDistributorMappingDao
				.searchDealerDistributor(isFor, dealerCode, userCode);
		if (dealerCodeSearchResponseList != null) {
			dealerCodeSearchResponse = dealerCodeSearchResponseList.get(0);
		}
		return dealerCodeSearchResponse;
	}

	private String checkandreturnCellVal(Cell cell) {
		String cellval = null;
		if (cell != null) {
			CellType type = cell.getCellType();
			if (type == CellType.STRING) {
				cellval = cell.getStringCellValue().replace(System.getProperty("line.separator"), "")
						.replaceAll("\\r\\n|\\r|\\n", " ");
			} else if (type == CellType.NUMERIC) {
				Double numericVal = cell.getNumericCellValue();
				cellval = numericVal.toString();
				cellval = cellval.substring(0, cellval.length() - 2);
				cellval = cellval.replaceAll("\\.", "");
			} else if (type == CellType.BLANK) {
				cellval = "";
			}
		}
		return cellval;
	}

	@Override
	public List<TargetSettingResponseModel> fetchTargetSettingData(String userCode) {
		return targetSettingDao.fetchTargetSettingData(userCode);
	}

	@Override
	public TargetSettingResponseModel fetchTargetSettingHdrAndDtl(BigInteger targetHdrId, String userCode) {
		TargetSettingResponseModel targetSettingResponseModel = targetSettingDao.fetchTargetData(targetHdrId, userCode,
				null, null);
		targetSettingResponseModel
				.setTargetSettingDtlResponse(targetSettingDao.fetchTargetDtlData(targetHdrId, userCode, null, null));
		return targetSettingResponseModel;
	}

}
