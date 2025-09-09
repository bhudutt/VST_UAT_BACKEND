package com.hitech.dms.web.dao.spare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.DozerBeanMapper;
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
import com.hitech.dms.web.entity.partmaster.create.request.AdminPartMasterEntity;
import com.hitech.dms.web.entity.partmaster.create.request.BranchPartMasterEntity;
import com.hitech.dms.web.model.spare.SparePartUploadResponseModel;

@Repository
public class SparePartUploadDaoImpl implements SparePartUploadDao{

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(SparePartUploadDaoImpl.class);

	@Override
	public SparePartUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			Integer branch, MultipartFile file) {
		

//		logger.debug("validateUploadedFile : " + spareUploadRequestModel.toString());
		logger.debug("userCode validateUploadedFile : " + userCode);
		SparePartUploadResponseModel responseModel = new SparePartUploadResponseModel();
		boolean isSuccess = true;
		String msg = null;
		try {
			ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("PartNumber", "Minimum Level", "Maximum Level",
					"ReOrder Level", "ABC", "FMS"));

			Map<Integer, String> map = new HashMap<Integer, String>();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(file.getInputStream());
			logger.debug("workbook - " , workbook);

			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			logger.debug("totalRows - " , totalRows);

			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				Cell cell = row.getCell(colIx);
				logger.info("cell - " , cell.getStringCellValue());
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
			
			BranchPartMasterEntity branchPartMasterEntity = null;
			List<BranchPartMasterEntity> branchPartMasterEntityList = null;
			Date currDate = new Date();
			if (isSuccess) {
				branchPartMasterEntityList = new ArrayList<BranchPartMasterEntity>();
				String record = null;
				branchPartMasterEntity = new BranchPartMasterEntity();
				
				for (int x = 1; x < totalRows; x++) {
					Row dataRow = sheet.getRow(x);
					int k = 1;
					if(!isSuccess) {
						break;
					}
					try {
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							Cell cell = dataRow.getCell(colIx);
							branchPartMasterEntity.setBranchId(branch);
							
							if (colIx == 0) {
								record = checkandreturnCellVal(cell);
								branchPartMasterEntity.setPartId(getPartIdFromPartTable(record));								
							} else if (colIx == 1) {
								record = checkandreturnCellVal(cell);
								branchPartMasterEntity.setMinLevelQty(new BigDecimal(record == null || record.equals("") ? "0" : record));
								
							} else if (colIx == 2) {
								record = checkandreturnCellVal(cell);
								branchPartMasterEntity.setMaxLevelQty(new BigDecimal(record == null || record.equals("") ? "0" : record));
								
							} else if (colIx == 3) {
								record = checkandreturnCellVal(cell);
								branchPartMasterEntity.setReorderLevelQty(new BigDecimal(record == null || record.equals("") ? "0" : record));
							
							} else if (colIx == 4) {
								record = checkandreturnCellVal(cell);
								if (record != null && (record.equalsIgnoreCase("A") 
										|| record.equalsIgnoreCase("B") || record.equalsIgnoreCase("C"))) {
									branchPartMasterEntity.setAbc(record);
								} else {
									responseModel.setMsg("Invalid data in column");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}
							} else if (colIx == 5) {
								record = checkandreturnCellVal(cell);
								if (record != null && (record.equalsIgnoreCase("F") 
										|| record.equalsIgnoreCase("M") || record.equalsIgnoreCase("S"))) {
									branchPartMasterEntity.setFms(record);
								} else {
									responseModel.setMsg("Invalid data in column");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}
							}
						}
						branchPartMasterEntityList.add(branchPartMasterEntity);
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
					// insert records into table

					Map<String, Object> mapData = saveSparePartBranchData(branchPartMasterEntityList);
					if (mapData != null && mapData.get("SUCCESS") != null) {
							// success
							responseModel.setPartMasterId(null);
							responseModel.setPartMasterNumber(null);
							responseModel.setMsg("MSL Uploaded Successfully.");
							responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					} else {
						// error
						if (msg == null) {
							msg = "Error While Uploading MSL.";
						}
						logger.error("Error While Saving MSL Into Staging Table.");
						isSuccess = false;
						responseModel.setMsg(msg);
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					}
				} 
			} else {
				// error
				if (msg == null) {
					msg = "Error While Uploading Spare MSL.";
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
				msg = "Error While Uploading MSL.";
			}
			responseModel.setMsg(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;
		
		
		
	}
	
	private Map<String, Object> saveSparePartBranchData(List<BranchPartMasterEntity> branchPartMasterEntityList) {
		boolean isSuccess = true;
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO STG TABLE.");
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (branchPartMasterEntityList != null) {
				for (BranchPartMasterEntity branchPartMasterEntity : branchPartMasterEntityList) {
					session.save(branchPartMasterEntity);
					mapData.put("SUCCESS", "SUCCESSFULLY INSERTED INTO Part Branch TABLE.");
				}
			}
			
			if (isSuccess) {
				transaction.commit();
				session.clear();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
	
	private Integer getPartIdFromPartTable(String partNumber) {
		Integer partId = null;
		AdminPartMasterEntity adminPartMasterEntity = null;
		List<AdminPartMasterEntity> adminPartMasterEntitylist = null;
		Session session = null;
		Transaction transaction = null;
//		String sqlQuery = "select part_id from PA_PART where PartNumber =:partNumber";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
//			Query query = session.createQuery(sqlQuery);
			Query query = session.createQuery("FROM AdminPartMasterEntity where partNumber= :partNumber");
			query.setParameter("partNumber", partNumber);
			adminPartMasterEntitylist = query.list();
//			query.setParameter("partNumber", "22071003");
//			List data = query.list();
			if (adminPartMasterEntitylist != null && !adminPartMasterEntitylist.isEmpty()) {
				adminPartMasterEntity =  adminPartMasterEntitylist.get(0);
				partId = adminPartMasterEntity.getId();
			}
		} catch (HibernateException he) {
			he.printStackTrace();
		}
		return partId;
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
		System.out.println(cellval);
		return cellval;
	}

}
