package com.hitech.dms.web.dao.sales.upload.template;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.utils.DateUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.sales.stock.upload.stg.StgStockAdjustmentUpload;
import com.hitech.dms.web.model.sales.upload.request.StockAdjustmentRequestModel;
import com.hitech.dms.web.model.sales.upload.response.StockAdjustmentResponseModel;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;

@Repository
public class StockAjustmentTemplateDaoImpl implements StockAjustmentTemplateDao {

	private static final Logger logger = LoggerFactory.getLogger(StockAjustmentTemplateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private MachinePOCommonDao machinePOCommonDao;

	@SuppressWarnings("resource")
	public StockAdjustmentResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			StockAdjustmentRequestModel stockAdjustmentRequestModel, MultipartFile files) {
		logger.debug("validateUploadedFile : " + stockAdjustmentRequestModel.toString());
		logger.debug("userCode validateUploadedFile : " + userCode);
		StockAdjustmentResponseModel responseModel = new StockAdjustmentResponseModel();
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
			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			
			if(maxColIx !=12) {
				responseModel.setMsg("Column No. Not Matched");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				return responseModel;
			}
			
			
			List<StgStockAdjustmentUpload> list = new ArrayList<StgStockAdjustmentUpload>();
			StgStockAdjustmentUpload stgStockUploadEntity = null;
			Date currDate = new Date();
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			String grnNumber = "OGRN" + stockAdjustmentRequestModel.getDealerCode() + currDate.getTime();
			if (isSuccess) {
				String record = null;
				Map<String, Object> mapData = null;
				String userId = null;
				mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger Id = (BigInteger) mapData.get("userId");
					userId=Id.toString();
				}
				for (int x = 1; x < totalRows; x++) {
					Row dataRow = sheet.getRow(x);
					try {
						stgStockUploadEntity = new StgStockAdjustmentUpload();
						stgStockUploadEntity.setBranchCode(stockAdjustmentRequestModel.getBranchCode());
						stgStockUploadEntity.setDealerCode(stockAdjustmentRequestModel.getDealerCode());
						stgStockUploadEntity.setDealederId(stockAdjustmentRequestModel.getDealerId());
						stgStockUploadEntity.setPcId(stockAdjustmentRequestModel.getPcID());
						stgStockUploadEntity.setCreatedDate(currDate);
						stgStockUploadEntity.setCreatedBy(userId);
						stgStockUploadEntity.setGrnNo(grnNumber);
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							Cell cell = dataRow.getCell(colIx);

							if (colIx == 0) {
								record = checkandreturnCellVal(cell,0);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Item No. contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null && !record.equalsIgnoreCase("")) {
										stgStockUploadEntity.setItemNo(record);
									}else {
										responseModel.setMsg("Item No. can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}
								
								
							} else if (colIx == 1) {
								record = checkandreturnCellVal(cell,1);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Item Item Desc contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null) {
										stgStockUploadEntity.setItemDesc(record);
									}
								}
								
							} else if (colIx == 2) {
								record = checkandreturnCellVal(cell,2);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Model Name contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null) {
										stgStockUploadEntity.setModelName(record);
									}
								}
								
							} else if (colIx == 3) {
								record = checkandreturnCellVal(cell,3);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("chassis no cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null) {
										stgStockUploadEntity.setChassisNo(record);
									}
								}
								
							} else if (colIx == 4) {
								record = checkandreturnCellVal(cell,4);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("vin no cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null) {
										stgStockUploadEntity.setVinNo(record);
									}
								}
								
							}
							else if (colIx == 5) {
								record = checkandreturnCellVal(cell,5);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("engine no cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null) {
										stgStockUploadEntity.setEngineNo(record);
									}
								}
								
							}else if (colIx == 6) {
								record = checkandreturnCellVal(cell,6);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("selling dealer code cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null) {
										stgStockUploadEntity.setSellingDealerCode(record);
									}
								}
								
							}else if (colIx == 7) {
								record = checkandreturnCellVal(cell,7);
								
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Invoice No. cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null && !record.equalsIgnoreCase("")) {
										
										if(record.contains(".")) {
											responseModel.setMsg("Invoice No. should be in proper format ");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}else {
											stgStockUploadEntity.setMfgInvoiceNumber(record);
										}
										
									}else {
										responseModel.setMsg("Invoice No. can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}
								
								
							}
							else if (colIx == 8) {
								record = checkandreturnCellVal(cell,8);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Invoice Date cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null && !record.equalsIgnoreCase("")) {
										String dateFormat=checkDateFormat(record);
										if(!dateFormat.equalsIgnoreCase("not")) {
											stgStockUploadEntity.setMfgInvoiceDate(record);
										}else {
											responseModel.setMsg("Invoice Date should be in Indain Format");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}
										
									}
									else {
										responseModel.setMsg("Invoice Date can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}
								
							}
							else if (colIx == 9) {
								record = checkandreturnCellVal(cell,9);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Unit Price cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null && !record.equalsIgnoreCase("")) {
										stgStockUploadEntity.setUnitPrice(Double.parseDouble(record));
									}else {
										responseModel.setMsg("Unit Price can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}
								
								
							}
							else if (colIx == 10) {
								record = checkandreturnCellVal(cell,10);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Quantity cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null && !record.equalsIgnoreCase("")) {
										stgStockUploadEntity.setQuantity(Integer.parseInt(record));
									}else {
										responseModel.setMsg("Quantity can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}
								
							}
							else if (colIx == 11) {
								record = checkandreturnCellVal(cell,11);
								if(record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("plnat code issue ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}else {
									if (record != null && !record.equalsIgnoreCase("")) {
										stgStockUploadEntity.setPlanCode(record);
									}else {
										responseModel.setMsg("plant code can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}
								
								
							}
							
							
							
							

						}
						list.add(stgStockUploadEntity);

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
							for (StgStockAdjustmentUpload obj : list) {
								save = (Integer) session.save(obj);
							}

						}
						transaction.commit();

						Query<?> query = null;
						String sqlQuery = "exec IS_Sync_UploadStock";
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
												responseModel.setMsg("Stock Uploaded Successfully.");
												responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
											} else {
												responseModel.setMsg(successFlag);
												responseModel
														.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											}
										} else {
											responseModel.setMsg("Stock already Available");
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

	private String checkDateFormat(String record) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false); // Disable lenient parsing
		String val="not";
		try {
            dateFormat.parse(record);
            System.out.println("The date string is in the Indian format.");
            val="done";
        } catch (ParseException e) {
            System.out.println("The date string is not in the Indian format.");
            val="not";
        }
		
		return val;
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
					
					
					if(count==9) {
						Double numericVal = cell.getNumericCellValue();
						 cellval =String.valueOf(numericVal); 
						
					}else if(count==10) {
						Double numericVal = cell.getNumericCellValue();
						 cellval =String.valueOf(numericVal); 
						if(count==10) {
							cellval = cellval.substring(0, cellval.length() - 2);
						}
					}else {
						Double numericVal = cell.getNumericCellValue();
						DecimalFormat decimalFormat = new DecimalFormat("#");
						cellval = decimalFormat.format(numericVal);
						
					}
					
					
					
				} else if (type == CellType.BLANK) {
					cellval = "";
				}
			}
			
			
			
		}
		return cellval;
	}

	

}
