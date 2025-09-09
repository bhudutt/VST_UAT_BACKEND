package com.hitech.dms.web.dao.sfrmapping.dao;

import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.aprmapping.SfrMappingEntity;
import com.hitech.dms.web.model.admin.sfrmapping.SfrMappingRequestModel;
import com.hitech.dms.web.model.admin.sfrmapping.SfrMappingResponseModel;

@Repository
public class SfrMappingDaoImpl implements SfrMappingDao {

	private static final Logger logger = LoggerFactory.getLogger(SfrMappingDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public SfrMappingResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			SfrMappingRequestModel sfrMappingRequestModel, MultipartFile files) {
		logger.debug("validateUploadedFilesfr : " + sfrMappingRequestModel.toString());
		logger.debug("userCode validateUploadedFilesfr : " + userCode);
		SfrMappingResponseModel responseModel = new SfrMappingResponseModel();
		boolean isSuccess = true;
		String msg = null;
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			InputStream in = files.getInputStream();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			//int totalRows = 0;
			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			
			// Iterate through each row in the sheet
			/*
			 * for (Row row1 : sheet) { if (row1 != null && hasNonEmptyCell(row1)) {
			 * totalRows++; } }
			 */
			System.out.println("totalRows "+totalRows);
			if(maxColIx !=11) {
				responseModel.setMsg("Column No. Not Matched");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				return responseModel;
			}
			
			
			List<SfrMappingEntity> list = new ArrayList<SfrMappingEntity>();
			SfrMappingEntity stgSfrUploadEntity = null;
			Date currDate = new Date();
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			if (isSuccess) {
				String record = null;
				Map<String, Object> mapData = null;
				String userId = null;
				mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger Id = (BigInteger) mapData.get("userId");
					userId=Id.toString();
				}
				for (int x = 1; x < totalRows; x++) {
					Row dataRow = sheet.getRow(x);
					try {
						
						stgSfrUploadEntity = new SfrMappingEntity();
						stgSfrUploadEntity.setCreatedDate(currDate);
						stgSfrUploadEntity.setCreatedBy(userId);
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							Cell cell = dataRow.getCell(colIx);
							
								record = checkandreturnCellVal(cell,0);
								if (record != null && !record.equalsIgnoreCase("")) {
									if (colIx == 0) {
										record = checkandreturnCellVal(cell,0);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("KPD Code contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setKpdCode(record);
											}else {
												responseModel.setMsg("KPD Code can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									} else if (colIx == 1) {
										record = checkandreturnCellVal(cell,1);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("KPD Name contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setKpdName(record);
											}else {
												responseModel.setMsg("KPD Code can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									} else if (colIx == 2) {
										record = checkandreturnCellVal(cell,2);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("Autherized Retailer Name contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAutherizeRetailerName(record);
											}else {
												responseModel.setMsg("Autherized Retailer can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									} else if (colIx == 3) {
										record = checkandreturnCellVal(cell,3);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("Sfr Address cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprAddress(record);
											}else {
												responseModel.setMsg("Sfr Address can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									} else if (colIx == 4) {
										record = checkandreturnCellVal(cell,4);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("Sfr phone cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												BigInteger phoneNumber = new BigInteger(record);
												stgSfrUploadEntity.setAprPhone(phoneNumber);
											}else {
												responseModel.setMsg("Sfr Phone can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									}
									else if (colIx == 5) {
										record = checkandreturnCellVal(cell,5);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("Sfr state cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprState(record);
											}else {
												responseModel.setMsg("Sfr state can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									}else if (colIx == 6) {
										record = checkandreturnCellVal(cell,6);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("sfr district cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprDistrictName(record);
											}else {
												responseModel.setMsg("Sfr district can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									}else if (colIx == 7) {
										record = checkandreturnCellVal(cell,7);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("Sfr talika cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprTalukaName(record);
											}else {
												responseModel.setMsg("Sfr taluka can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									}
									else if (colIx == 8) {
										record = checkandreturnCellVal(cell,8);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("sfr city cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprCityName(record);
											}else {
												responseModel.setMsg("sfr city can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
									}
									else if (colIx == 9) {
										record = checkandreturnCellVal(cell,9);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("sfr pincode cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprPinCode(record);
											}else {
												responseModel.setMsg("sfr pincode can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
									}
									else if (colIx == 10) {
										record = checkandreturnCellVal(cell,10);
										if(record != null && record.equalsIgnoreCase("wrong")) {
											responseModel.setMsg("sfr gst no cell contain formula ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											if (record != null && !record.equalsIgnoreCase("")) {
												stgSfrUploadEntity.setAprGSTNumber(record);
											}else {
												responseModel.setMsg("sfr GST can't be blank");
												responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
												return responseModel;
											}
										}
										
									}
								
									
								
								
								}
							
							
							
							
							}
						if(stgSfrUploadEntity.getKpdCode() !=null && !stgSfrUploadEntity.getKpdCode().equalsIgnoreCase(""))
						{
							list.add(stgSfrUploadEntity);
						}
						
						

					} catch (Exception ex) {
						isSuccess = false;
						if (msg == null) {
							msg = ex.getMessage();
						}
						logger.error(this.getClass().getName(), ex);
						break;
					}

				}
				if (isSuccess) {
					try {
						Integer save = 0;
						transaction = session.beginTransaction();
						if (list != null && list.size() > 0) {
							for (SfrMappingEntity obj : list) {
								save = (Integer) session.save(obj);
							}
						}
						transaction.commit();
						Query<?> query = null;
						String sqlQuery = "exec SP_INSERT_SFR_LIST";
						if (save != null) {
							try {
								query = session.createNativeQuery(sqlQuery);
								query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
								List<?> data = query.list();
								if (data != null && !data.isEmpty()) {
									String successFlag;
									for (Object object : data) {
										@SuppressWarnings("rawtypes")
										Map rowData = (Map) object;
										successFlag = (String) rowData.get("Message");
										if (successFlag != null) {
											if (successFlag.equalsIgnoreCase("Success")) {
												responseModel.setMsg("Sfr mapping uploaded Successfully.");
												responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
											} else {
												responseModel.setMsg(successFlag);
												responseModel
														.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											}
										} else {
											responseModel.setMsg("Sfr mapping already Available");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										}
									}
								}else {
									responseModel.setMsg("Some Issue..");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
								}
								// responseModel.setMsg("Stock Uploaded Successfully.");
								// responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);

							} catch (Exception e) { // TODO: handle exception e.printStackTrace(); }

							}

						}
					} catch (Exception e) {
						logger.error(this.getClass().getName(), e);
						if (transaction.isActive())
							transaction.rollback();
					} finally {
						if (session.isOpen())
							session.close();
						in.close();
					}

				} else {
					// error
					if (msg == null) {
						msg = "Error While Uploading Stock plan.";
					}
					isSuccess = false;
					logger.error("error");
					logger.error(msg);
					responseModel.setMsg(msg);
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			}
		} catch (

		Exception ex) {
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
				msg = "Error While Uploading stock.";
			}
			responseModel.setMsg(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;
	}
	private String checkandreturnCellVal(Cell cell,int count) {
		String cellval = null;
		DataFormatter fmt = new DataFormatter();
		
		if (cell != null) {
			CellType type = cell.getCellType();
			
			if(type == CellType.FORMULA) {
				cellval="wrong";
			}else {
				if (type == CellType.STRING) {
					cellval = cell.getStringCellValue().replace(System.getProperty("line.separator"), "")
							.replaceAll("\\r\\n|\\r|\\n", " ");
					cellval = fmt.formatCellValue(cell);
				} else if (type == CellType.NUMERIC) {
					if(count==4) {
						String phoneNumber = String.format("%.0f", cell.getNumericCellValue());
						cellval =phoneNumber.replace(System.getProperty("line.separator"), "")
								.replaceAll("\\r\\n|\\r|\\n", " ");
						//System.out.println("Phone Number: " + phoneNumber);
					}else if(count==9) {
						String phoneNumber = String.format("%.0f", cell.getNumericCellValue());
						cellval =phoneNumber.replace(System.getProperty("line.separator"), "")
								.replaceAll("\\r\\n|\\r|\\n", " ");
						//System.out.println("Phone Number: " + phoneNumber);
					}else {
						String phoneNumber = String.format("%.0f", cell.getNumericCellValue());
						cellval =phoneNumber.replace(System.getProperty("line.separator"), "")
								.replaceAll("\\r\\n|\\r|\\n", " ");
						//System.out.println("Phone Number: " + phoneNumber);
					}
				} else if (type == CellType.BLANK) {
					cellval = "";
				}
			}
		}
		return cellval;
	}
	
	// Helper method to check if a row contains any non-empty cell
    private static boolean hasNonEmptyCell(Row row) {
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                return true; // If any cell in the row is non-empty, return true
            }
        }
        return false; // If all cells are blank, return false
    }
	
}
