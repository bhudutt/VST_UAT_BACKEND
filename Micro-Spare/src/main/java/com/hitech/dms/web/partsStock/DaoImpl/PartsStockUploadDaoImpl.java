package com.hitech.dms.web.partsStock.DaoImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDaoImpl;
import com.hitech.dms.web.entity.user.SparePartsStockEntity;
import com.hitech.dms.web.entity.user.StockUploadStagingEntity;
import com.hitech.dms.web.partsStock.Dao.PartsStockUploadCreateDao;
import com.hitech.dms.web.partsStock.Model.PartBranchDetailResponse;
import com.hitech.dms.web.partsStock.Model.PartBranchDetailStatus;
import com.hitech.dms.web.partsStock.Model.PartsSearchResponseModel;
import com.hitech.dms.web.partsStock.Model.PartsStockUploadModel;
import com.hitech.dms.web.partsStock.Model.StockBinDetailModel;
import com.hitech.dms.web.partsStock.Model.StockBinDetailResponseModel;
import com.hitech.dms.web.partsStock.Model.StockHeaderWithPartBranchIdModel;
import com.hitech.dms.web.partsStock.Model.StockSaveHeaderResponseModel;
import com.hitech.dms.web.partsStock.Model.StockStoreResponse;
import com.hitech.dms.web.partsStock.Model.StoreSearchResponseModel;
import com.hitech.dms.web.partsStockController.create.response.PartsUploadCreateResponse;

@Repository
public class PartsStockUploadDaoImpl implements PartsStockUploadCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(PartsStockUploadDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDaoImpl commonDaoImpl;

	List<Integer> partsPackQunatity;
	private DateToStringParserUtils utill = new DateToStringParserUtils();;

	@Override
	public PartsUploadCreateResponse partsStockUploadDao(String authorizationHeader, String userCode, Integer dealer,
			Integer branch, MultipartFile file) {


		logger.debug("userCode uploadSparePoParts : " + userCode + " branch" + branch);

		PartsUploadCreateResponse responseModel = new PartsUploadCreateResponse();
		List<PartsStockUploadModel> modelList = new ArrayList<>();
		List<SparePartsStockEntity> partsList= new ArrayList<>();
		List<StockUploadStagingEntity> stagingPartList= new ArrayList<>();
		PartsStockUploadModel partsData = null;
		boolean isSuccess = true;
		String msg = null;
		String partNumber = null;
		int partId = 0;
		String store = null;
		String storeBinLocation = null;
		int quantity = 0;
		float mrp = 0;
		float ndp = 0;
		int orderQty = 0;
		Map<String, Integer> data = new HashMap<>();
		Map<String, String> errorData = new LinkedHashMap<>();
		Random random = new Random();

		try {
			ArrayList<String> staticHeaderList = new ArrayList<>(
					Arrays.asList("PART NO.", "STORE", "STORE BIN LOCATION", "QUANTITY", "MRP"));

			Map<Integer, String> map = new HashMap<Integer, String>();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(file.getInputStream());
			DataFormatter formatter = new DataFormatter();
			logger.debug("workbook - ", workbook);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			responseModel.setTotalRowCount(totalRows);
			logger.debug("totalRows - ",+totalRows);

			Row row = sheet.getRow(0);
			List<StoreSearchResponseModel> storeSearchList= new ArrayList<>();
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				Cell cell = row.getCell(colIx);
				logger.info("cell - ", cell.getStringCellValue());
				if (staticHeaderList.contains(cell.getStringCellValue().trim())) {
					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
				} else {

					logger.error("INVALID EXCEL FORMAT.");
					responseModel.setMessage("INVALID EXCEL FORMAT.");
					responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
					isSuccess = false;
					return responseModel;
				}
			}
			Date currDate = new Date();

			if (isSuccess) {

				for (int x = 1; x <= totalRows - 1; x++) {
					PartsStockUploadModel model = new PartsStockUploadModel();
					SparePartsStockEntity entity= new SparePartsStockEntity();
					StockUploadStagingEntity stgEntity= new StockUploadStagingEntity();
					StoreSearchResponseModel storeSearchModel = new StoreSearchResponseModel();
					int ColCount = 0;
					Row dataRow = sheet.getRow(x);
					if (dataRow == null || isRowEmpty(dataRow)) {
						continue;
					}
					int k = 1;
					if (!isSuccess) {
						break;
					}
					try {
						int count = 0;
						String cellValue = null;
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							int colcount = 0;
							Cell cell = dataRow.getCell(colIx);

							if (cell != null) {
								switch (cell.getCellType()) {
								case STRING:
									cellValue = cell.getStringCellValue();
									break;
								case NUMERIC:
									DecimalFormat decimalFormat = new DecimalFormat("#");
									cellValue = decimalFormat.format(cell.getNumericCellValue());
									break;
								case BLANK:
									cellValue = "";
									break;
								}
							}

							//
							if (colIx == 0) {
								ColCount++;
								partNumber=cellValue;
								colcount++;
//								if (partNumber != null) {
//									List<PartsSearchResponseModel> partsAlreadyAvailable = fetchduplicatePartByPartNumber(
//											userCode,partNumber,branch);
//									if (partsAlreadyAvailable != null) {
//										msg = "Row no :" + x +","+ "partNumber is already Available with this branch "+userCode;
//										errorData.put(Integer.toString(random.nextInt(1000)), msg);
//										responseModel.setErrorPartsData(errorData);
//										responseModel.setMessage(msg);
//										responseModel.setStatusCode(304);
//										return responseModel;
//									}
//
//								}
//								List<PartsSearchResponseModel> verify = fetchPartByPartNumber(userCode, partNumber);

								if (partNumber == null || partNumber.isEmpty()) {
									int tempRowNo = row.getRowNum() + 1;
									msg
									= "Row no :" + x + "," + "partNumber is missing  ";
									errorData.put(Integer.toString(random.nextInt(1000)), msg);
								}

//								else if (verify == null || verify.isEmpty()) {
//									msg =partNumber + "," + "partNumber is not matched";
//									errorData.put(Integer.toString(random.nextInt(1000)), msg);
//
//									// count--;
//								} else {
//									partId = verify.get(0).getPartId();
//								}
							} else if (colIx == 1) {
								ColCount++;
								colcount++;
								String value = checkandreturnCellVal(cell);
								value = value.stripTrailing();

//								if (value != null) {
//									StoreSearchResponseModel storeSearchList1 = fetchStoreNameBybranchId(userCode,
//											branch, value);
//									if (storeSearchList1 == null) {
//										msg = "Row no :" + x + "," + "Store is Not Available with this branch :";
//										errorData.put(Integer.toString(random.nextInt(1000)), msg);
//										responseModel.setErrorPartsData(errorData);
//										responseModel.setMessage(msg);
//										responseModel.setStatusCode(304);
//										return responseModel;
//									} else {
//										//System.out.println("in add else");
//										storeSearchList.add(storeSearchList1);
//										responseModel.setStoreListOfParts(storeSearchList);
//									}
//								}

								if (value == null) {
									int rowNo = row.getRowNum() + 1;
									msg = "Row no :" + x + "," + "Store is missing kindly update and upload";
									errorData.put(Integer.toString(random.nextInt(1000)), msg);
//								} else if (!value.matches("^[a-zA-Z0-9]+([ _][a-zA-Z0-9]+)*$")) {
//									int rowNo = row.getRowNum() + 1;
//									msg = "Row no :" + x + ","
//											+ "Store should be alphanumeric kindly update and upload";
//									errorData.put(Integer.toString(random.nextInt(1000)), msg);
//
								} 
									else {
									store = value;
								}
							}

							else if (colIx == 2) {
								ColCount++;
								String value = checkandreturnCellVal(cell);
								value = value.stripTrailing();
								if (value == null || value.isEmpty()) {
									int rowNo = row.getRowNum() + 1;
									msg = "Row no :" + x + ","
											+ "Store Bin location is missing kindly update and upload";
									errorData.put(Integer.toString(random.nextInt(1000)), msg);
								}
//								else if (!value.matches("^[a-zA-Z0-9]+([ _][a-zA-Z0-9]+)*$")) {
//									int rowNo = row.getRowNum() + 1;
//									msg = "Row no :" + x + ","
//											+ "Store Bin location should be alphanumeric kindly update and upload";
//									errorData.put(Integer.toString(random.nextInt(1000)), msg);
//									// count--;
//
//								} 
								else if (value.length() > 15) {
									int rowNo = row.getRowNum() + 1;
									msg = "Row no :" + x + ","
											+ "Store Bin location not more than 15 char kindly update and upload";
									errorData.put(Integer.toString(random.nextInt(1000)), msg);

								} else {
									storeBinLocation = value;
								}
							}

							else if (colIx == 3) {
								ColCount++;
								String value = checkandreturnCellVal(cell);

								if (value == null || value.isEmpty()) {
									int rowNo = row.getRowNum() + 1;
									msg = "Row no :" + x + "," + "Quantity is missing kindly update and upload";
									errorData.put(Integer.toString(random.nextInt(1000)), msg);
								}

								else {
									try {
										quantity = Integer.parseInt(value);
										quantity = Math.abs(quantity);

									} catch (NumberFormatException e) {
										msg = "Row no :" + x + "," + "Quantity should be in digit only";
										errorData.put(Integer.toString(random.nextInt(1000)), msg);

									}
								}
							}

							else if (colIx == 4) {
								ColCount++;
								String value = checkandreturnCellVal(cell);

								if (value == null || value.isEmpty()) {
									int rowNo = row.getRowNum() + 1;
									msg = "Row no :" + x + "," + "MRP is missing kindly update and upload";
									errorData.put(Integer.toString(random.nextInt(1000)), msg);

								}

								else if (value != null || !value.isEmpty()) {
									try {
										mrp = Float.parseFloat(value);
										mrp = Math.abs(mrp);
										double discountPercentage = 10.0 / 100.0; //

										ndp = (float) (mrp * (1 - discountPercentage));
										ndp = Math.abs(ndp);

									} catch (NumberFormatException e) {

										msg = "Row no :" + x + "," + "MRP should be in digit only";
										errorData.put(Integer.toString(random.nextInt(1000)), msg);
									}

								}

							}

							if (ColCount == 5) {
								model.setPartNo(partNumber);
								entity.setPartNo(partNumber);
								stgEntity.setPartNo(partNumber);
								model.setStore(store);
								entity.setStore(store);
								stgEntity.setStore(store);

								model.setStoreBinLocation(storeBinLocation);
								entity.setStoreBinLocation(storeBinLocation);
								stgEntity.setStoreBinLocation(storeBinLocation);

								model.setQuantity(quantity);
								entity.setQuantity(quantity);
								stgEntity.setQuantity(quantity);

								model.setMrpPrice(mrp);
								entity.setMrp(mrp);
								stgEntity.setMrp(mrp);

								model.setNdpPrice(ndp);
								entity.setNdp(ndp);
								stgEntity.setNdp(ndp);

								model.setDealer(dealer);
								entity.setDealer(dealer);
								stgEntity.setDealer(dealer);

								model.setBranch(branch);
								entity.setBranch(branch);
								stgEntity.setBranch(branch);

								modelList.add(model);
								partsList.add(entity);
								stagingPartList.add(stgEntity);
								partNumber = " ";
								store = "";
								storeBinLocation = "";
								quantity = 0;
								mrp = 0;
								ndp = 0;

							}

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
				if (isSuccess && errorData.isEmpty()) {
					logger.info(" While Saving SparePart sucessfully.");
					responseModel.setStoreListOfParts(storeSearchList);

					responseModel.setErrorPartsData(errorData);
					responseModel.setExcelListOfParts(modelList);
					responseModel.setSparePartsList(partsList);
					responseModel.setStagingPartsEntity(stagingPartList);
					responseModel.setMessage("SparePart Uploaded Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else if (isSuccess && !errorData.isEmpty()) {
					logger.info(" While Saving excel part error.");
					responseModel.setErrorPartsData(errorData);
					responseModel.setExcelListOfParts(modelList);
					responseModel.setMessage("SparePart Excel error.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				} else {
					if (msg == null) {
						msg = "Error While Uploading SparePart.";
					}

					isSuccess = false;
					responseModel.setMessage(msg);
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (msg == null) {
					msg = "Error While Uploading Spare SparePart.";
				}
				isSuccess = false;
				logger.error("error");
				logger.error(msg);
				responseModel.setMessage(msg);
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
				msg = "Error While Uploading SparePart.";
			}
			responseModel.setMessage(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;

	}

	private boolean isRowEmpty(Row row) {
		for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
			Cell cell = row.getCell(cellIndex);
			if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private StoreSearchResponseModel fetchStoreNameBybranchId(String userCode, Integer branchId,
			String storeCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + branchId);
		}
		Session session = null;
		Query query = null;
		StoreSearchResponseModel responseModelList = null;
		String sqlQuery = "Select * from PA_BRANCH_STORE where branch_id='" + branchId + "' and  StoreCode='"
				+ storeCode + "'";
		//.println("SQL query for search branch entry" + sqlQuery);
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("partNumber", partNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModelList= new StoreSearchResponseModel();
					responseModelList.setBranchStoreId((Integer) row.get("branch_store_id"));
					responseModelList.setStoreCode((String) row.get("StoreCode"));
					
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	private List<PartsSearchResponseModel> fetchduplicatePartByPartNumber(String userCode, String partNumber,Integer branch) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + partNumber);
		}
		Session session = null;
		Query query = null;
		List<PartsSearchResponseModel> responseModelList = null;
		String sqlQuery = "select top 2 * from  STOCK_UPLOAD_TEMP  where branch="+branch+"";
		//.println("SQL query for duplicate entry" + sqlQuery);
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("partNumber", partNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PartsSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					PartsSearchResponseModel responseModel = new PartsSearchResponseModel();
					responseModel.setPartId((Integer) row.get("id"));
					responseModel.setPartNo((String) row.get("partNo"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;

	}

	private List<PartsSearchResponseModel> fetchPartByPartNumber(String userCode, String partNumber) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + partNumber);
		}
		String trimmed = partNumber.replaceAll("^\\s+", "");
		Session session = null;
		Query query = null;
		logger.debug("fetchPartNo invoked" + partNumber);

		List<PartsSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_GET_SPECIFIC_PART_SEARCH] :partNumber";
		try {
			session = sessionFactory.openSession();
			partsPackQunatity = new ArrayList<>();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partNumber", trimmed);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PartsSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					PartsSearchResponseModel responseModel = new PartsSearchResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
					responseModel.setPackQuantity((Integer) row.get("PackQty"));
//					Integer packQuantity=(Integer)row.get("PackQty");
//					if(packQuantity!=null) {
//						partsPackQunatity.add(packQuantity);
//					}
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;

	}

	private String checkandreturnCellVal(Cell cell) {
		String cellval = null;
		if (cell != null) {
			DataFormatter dataFormatter = new DataFormatter();
			String formattedValue = dataFormatter.formatCellValue(cell);
			CellType type = cell.getCellType();
			if (type == CellType.STRING) {
				cellval = cell.getStringCellValue().replace(System.getProperty("line.separator"), "")
						.replaceAll("\\r\\n|\\r|\\n", " ");
			} else if (type == CellType.NUMERIC) {

				Double numericVal = cell.getNumericCellValue();
				if (formattedValue.contains(".")) {
					//.println("In if value ");
					cellval = numericVal.toString();
				} else {
					cellval = numericVal.toString();
					cellval = cellval.substring(0, cellval.length() - 2);
				}
			} else if (type == CellType.BLANK) {
				cellval = "";
			}
		}
		return cellval;
	}

	@Override
	public PartBranchDetailStatus checkStockBinandUploadStockDetail(List<StoreSearchResponseModel> storeListOfParts,
			Integer branch, String userCode, Integer delaer, List<PartsStockUploadModel> excelListOfParts,
			List<StoreSearchResponseModel> storeList) {
		PartBranchDetailStatus status = new PartBranchDetailStatus();
		List<PartBranchDetailResponse> partBranchList = new ArrayList<>();
		List<PartBranchDetailResponse> branchDetailListBypartBranchId = new ArrayList<>();
		try {
			for (PartsStockUploadModel model : excelListOfParts) {
				PartBranchDetailResponse branchdetail = new PartBranchDetailResponse();
				branchdetail = checkPartBranchDetailByPartIdNAdBranch(branch, model.getPartId(),
						model.getStoreBinLocation(), model.getStore());

				//.println("after fetch " + branchdetail.getPartId() + " " + branchdetail.getPartBranchId());

				partBranchList.add(branchdetail);

			}

			if (partBranchList.get(0).getPartId() == 0 && partBranchList.get(0).getBranchId() == 0) {
				int i = 0;
				for (PartsStockUploadModel model1 : excelListOfParts) {
					PartBranchDetailResponse branchdetail = new PartBranchDetailResponse();
					branchdetail = SavePartBranchByPartId(model1,storeList.get(i),userCode);
					branchDetailListBypartBranchId.add(branchdetail);
					i++;

				}
				//.println("Before sending actual response branchList is " + partBranchList);
				status.setPartBranchDetailResponse(branchDetailListBypartBranchId);
				status.setMessage("Data saved successFully");
				status.setStatusCode(200);

			} else {
				//.println("In elseeeeeeeeeeeeeee");

				status.setMessage("Part with part Id " + partBranchList.get(0).getPartId() + " and branch id  "
						+ partBranchList.get(0).getBranchId() + "is already available");
				status.setStatusCode(304);

			}

			//.println("after iterate list " + partBranchList.toString());

		} catch (Exception e) {
			status.setMessage(e.getMessage());
			status.setStatusCode(302);
			//.println(e.getMessage());
		}

		return status;
	}

	private PartBranchDetailResponse SavePartBranchByPartId(PartsStockUploadModel model,
			StoreSearchResponseModel storeSearchResponseModel, String userCode) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + model.getPartId() + " model " + model);
		}
		Session Insertsession = null;
		Session selectsession = null;
		Query query = null;
		String message = null;
		boolean isSuccess = false;
		PartBranchDetailResponse res= new PartBranchDetailResponse();
		PartBranchDetailResponse responseModelList = new PartBranchDetailResponse();
		PartBranchDetailResponse responseModelListStore = new PartBranchDetailResponse();
		responseModelList= getPartBranchDetail(model,userCode);
		//System.out.println("responseModelList "+responseModelList);

		
		if(responseModelList==null||responseModelList.getPartId()==0 ) {
			Integer SavePartBranchStatus=SavePartBranchDetail(model,storeSearchResponseModel,userCode);
			responseModelList= getPartBranchDetail(model,userCode);
			res.setPartId(responseModelList.getPartId());
			res.setPartBranchId(responseModelList.getPartBranchId());
			res.setBranchId(responseModelList.getBranchId());


		}
		else
		{
			Integer updatePartBranchStatus=updatePartBranchDetail(model,storeSearchResponseModel,userCode,responseModelList);
			res.setPartId(responseModelList.getPartId());
			res.setPartBranchId(responseModelList.getPartBranchId());
			res.setBranchId(responseModelList.getBranchId());

		}
		responseModelListStore=getStockStoreDetail(model,responseModelList,userCode,storeSearchResponseModel);
		if(responseModelListStore==null||responseModelListStore.getStockStoreId()==0)
		{
			
			StockStoreResponse store=saveStockStoreByPartBranchId(responseModelList.getPartBranchId(),model,storeSearchResponseModel,userCode);
			responseModelListStore=getStockStoreDetail(model,responseModelList,userCode,storeSearchResponseModel);
			res.setStockStoreId(store.getStockStoreId());
			res.setBranchStoreId(store.getBranchStoreId());
		}
		else
		{
			
				
			
					Integer updateStockStoreStatus=updateStockStore(responseModelListStore.getPartBranchId(),model,responseModelListStore,userCode);
					res.setStockStoreId(responseModelListStore.getStockStoreId());
					res.setBranchStoreId(responseModelListStore.getBranchStoreId());
			

			
						
		}
						
		try {
				
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (selectsession != null) {
				selectsession.close();
			}
		}
		return res;
	}

	private PartBranchDetailResponse getStockStoreByBranchStoreId(int partBranchId, int branchStoreId) {
		Session selectsession=null;
		Query query=null;
		String sqlQuery=null;
	     boolean isSuccess= false;
		PartBranchDetailResponse responseModelList= new PartBranchDetailResponse();

	     try
	     {
		selectsession = sessionFactory.openSession();
		sqlQuery = "select * from PA_STOCK_STORE where partBranch_id=:partBranchId and branch_store_id=:branchStoreId";
		
		query =selectsession.createSQLQuery(sqlQuery);
		query.setParameter("partBranchId",partBranchId);
		query.setParameter("branchStoreId",branchStoreId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		List data = query.list();
		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				isSuccess = true;
				responseModelList.setPartBranchId((Integer)row.get("partBranch_id"));
				responseModelList.setBranchStoreId((Integer)row.get("branch_store_id"));
				responseModelList.setStockStoreId((Integer) row.get("stock_store_id"));
				BigDecimal decimal=(BigDecimal) row.get("currentStock");
				responseModelList.setCurrentStock(decimal.toBigInteger());
				
				
			}
		}
		
	     }

		
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return responseModelList;

	}

	private Integer updateStockStore(int partBranchId, PartsStockUploadModel model,
			PartBranchDetailResponse storeSearchResponseModel, String userCode) {
		Session session=null;
		Integer updatedRowCount=0;
		Query query=null;
		try
		{
			String sqlQuery = "update PA_STOCK_STORE set currentStock=:currentStock where partBranch_id=:partBranchId and stock_store_id=:stockStoreId";
			session = sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("currentStock",model.getQuantity()+storeSearchResponseModel.getCurrentStock().intValue());
			query.setParameter("partBranchId",storeSearchResponseModel.getPartBranchId());
			query.setParameter("stockStoreId",storeSearchResponseModel.getStockStoreId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			updatedRowCount = query.executeUpdate();
	        
	        transaction.commit();
	        
			
			
		}catch(Exception e )
		{
			e.printStackTrace();
		}
		return updatedRowCount;

	}

	private PartBranchDetailResponse getStockStoreDetail(PartsStockUploadModel model, PartBranchDetailResponse storeSearchResponseModel, 
			String userCode,StoreSearchResponseModel storeSearchResponseModel1) {
		Session selectsession=null;
		Query query=null;
		String sqlQuery=null;
	     boolean isSuccess= false;
		PartBranchDetailResponse responseModelList= new PartBranchDetailResponse();

	     try
	     {
		selectsession = sessionFactory.openSession();
		sqlQuery = "select * from PA_STOCK_STORE where partBranch_id=:partBranchId and branch_store_id=:branchStoreId";
		
		query =selectsession.createSQLQuery(sqlQuery);
		query.setParameter("partBranchId",storeSearchResponseModel.getPartBranchId());
		query.setParameter("branchStoreId",storeSearchResponseModel1.getBranchStoreId());
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		List data = query.list();
		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				isSuccess = true;
				responseModelList.setPartBranchId((Integer)row.get("partBranch_id"));
				responseModelList.setBranchStoreId((Integer)row.get("branch_store_id"));
				responseModelList.setStockStoreId((Integer) row.get("stock_store_id"));
				BigDecimal decimal=(BigDecimal) row.get("currentStock");
				responseModelList.setCurrentStock(decimal.toBigInteger());
				
				
			}
		}
		
	     }

		
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return responseModelList;

	}

	private Integer updatePartBranchDetail(PartsStockUploadModel model,
			StoreSearchResponseModel storeSearchResponseModel, String userCode, PartBranchDetailResponse responseModelList) {
		
		Session session=null;
		Integer updatedRowCount=0;
		Query query=null;
		try
		{
			String sqlQuery = "update PA_PART_BRANCH set OnHandQty=:onHandQty where branch_id=:branchId and partBranch_id=:partBranchId";
			session = sessionFactory.openSession();
	        Transaction transaction = session.beginTransaction();
	        query = session.createSQLQuery(sqlQuery);
			query.setParameter("onHandQty",model.getQuantity()+responseModelList.getOnHandQty());
			query.setParameter("branchId",responseModelList.getBranchId());
			query.setParameter("partBranchId", responseModelList.getPartBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			updatedRowCount = query.executeUpdate();
	        
	        transaction.commit();
	        
			
			
		}catch(Exception e )
		{
			e.printStackTrace();
		}
		return updatedRowCount;
	}

	private PartBranchDetailResponse getPartBranchDetail(PartsStockUploadModel model, String userCode) {
		// TODO Auto-generated method stub
		Session selectsession=null;
		Query query=null;
		String sqlQuery=null;
	     boolean isSuccess= false;
			PartBranchDetailResponse responseModelList= new PartBranchDetailResponse();

	     try
	     {
		selectsession = sessionFactory.openSession();
		sqlQuery = "select  * from PA_PART_BRANCH where part_id=" + model.getPartId() + " and branch_id="+model.getBranch();
		query =selectsession.createSQLQuery(sqlQuery);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();
		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				responseModelList.setPartId((Integer) row.get("part_id"));
				responseModelList.setBranchId((Integer) row.get("branch_id"));
				responseModelList.setPartBranchId((Integer) row.get("partBranch_id"));
				responseModelList.setOnHandQty((Integer) row.get("OnHandQty"));

				isSuccess = true;
				
			}
		}
		
	     }

		
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return responseModelList;
	}

	private Integer SavePartBranchDetail(PartsStockUploadModel model, StoreSearchResponseModel storeSearchResponseModel,
			String userCode) {
		
		
		
		
		Session Insertsession = null;
		Session selectsession = null;
		Query query = null;
		int rowCount=0;
		String message = null;
		boolean isSuccess = false;
		PartBranchDetailResponse responseModelList = new PartBranchDetailResponse();
		StockStoreResponse stockStoreDetail = new StockStoreResponse();
	//	Integer SavePartBranchStatus=SavePartBranchDetail(model,storeSearchResponseModel,userCode);
		String sqlQuery = null;
		//.println("SQL query for PART BRANCH detail" + sqlQuery);
		try {
			sqlQuery = "INSERT INTO PA_PART_BRANCH(part_id, branch_id, MRP, NDP,OnHandQty,OnOrderQty,PackQty,LockForTranscation) "
					+ "VALUES(:part_id, :branch_id, :MRP, :NDP,:OnHandQty,:OnOrderQty,:PackQty,:LockForTranscation)";
			Insertsession = sessionFactory.openSession();
			Insertsession.beginTransaction();

			query = Insertsession.createSQLQuery(sqlQuery).setParameter("part_id", model.getPartId())
					.setParameter("branch_id", model.getBranch()).setParameter("MRP", model.getMrpPrice())
					.setParameter("NDP", model.getNdpPrice()).setParameter("OnHandQty", model.getQuantity())
					.setParameter("OnOrderQty", 0).setParameter("PackQty", 1).setParameter("LockForTranscation", "N");

			rowCount = query.executeUpdate(); // Execute the insert query
			Insertsession.getTransaction().commit();
		}
		catch(Exception e)
		{
			
		}
		return rowCount;
	}

	private StockStoreResponse saveStockStoreByPartBranchId(int partBranchId, PartsStockUploadModel model,
			StoreSearchResponseModel storeSearchResponseModel, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + partBranchId);
		}
		Session session = null;
		Session selectsession = null;
		Query query = null;

		StockStoreResponse responseModelList = new StockStoreResponse();
		;
		String sqlQuery = null;
		try {

			sqlQuery = "INSERT INTO PA_STOCK_STORE(branch_id, partBranch_id,branch_store_id,currentStock, currentStockAmount, CreatedDate,CreatedBy,ModifiedDate,ModifiedBy)"
					+ "VALUES(:branch_id, :partBranch_id, :branch_store_id, :currentStock ,:currentStockAmount ,:CreatedDate ,:CreatedBy, :ModifiedDate ,:ModifiedBy)";
			session = sessionFactory.openSession();
			//.println("SQL query for PA STOCK Store" + sqlQuery);

			session.beginTransaction();

			query = session.createSQLQuery(sqlQuery).setParameter("branch_id", model.getBranch())
					.setParameter("partBranch_id", partBranchId)
					.setParameter("branch_store_id", storeSearchResponseModel.getBranchStoreId())
					.setParameter("currentStock", model.getQuantity())
					.setParameter("currentStockAmount", (model.getQuantity() * model.getMrpPrice()))
					.setParameter("CreatedDate", new Date()).setParameter("CreatedBy", userCode)
					.setParameter("ModifiedDate", new Date()).setParameter("ModifiedBy", userCode);

			int rowCount = query.executeUpdate(); // Execute the insert query
			session.getTransaction().commit();
			if (rowCount == 1) {
				selectsession = sessionFactory.openSession();
				boolean isSuccess = false;
				sqlQuery = "select  * from  PA_STOCK_STORE where partBranch_id=" + partBranchId + "";
				query = selectsession.createSQLQuery(sqlQuery);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;

						responseModelList.setBranchStoreId((Integer) row.get("branch_store_id"));
						responseModelList.setPartBranchId((Integer) row.get("partBranch_id"));
						responseModelList.setStockStoreId((Integer) row.get("stock_store_id"));
						isSuccess = true;
						//.println("before return stock Store Detail " + responseModelList);
						rowCount = -1;

					}
				}
				if (isSuccess) {
					return responseModelList;
				}

			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseModelList;
	}

	private PartBranchDetailResponse checkPartBranchDetailByPartIdNAdBranch(int branch, int partId,
			String storeBinLocation, String store) {

		//.println("fetchPartNo " + branch + " partId" + " store " + store + "   " + storeBinLocation);
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + branch + " partId" + " store " + store + "   " + storeBinLocation);
		}
		Session session = null;
		Query query = null;
		PartBranchDetailResponse responseModelList = new PartBranchDetailResponse();
		;
		// String sqlQuery = "select * from PA_PART_BRANCH where branch_id=" + branch
		// +"";
		String sqlQuery = "select distinct PA.*,PBS.StoreDesc,PB.BinName from  PA_PART_BRANCH PA inner join PA_STOCK_STORE PS on PA.partbranch_id=ps.partBranch_id "
				+ "inner join PA_STOCK_BIN PB on PB.partBranch_id=PA.partBranch_id "
				+ "inner join PA_BRANCH_STORE PBS on PBS.branch_store_id=PS.branch_store_id"
				+ " where PA.part_id=:partId and PA.branch_id=:branchId";
		//.println("SQL query for PART BRANCH detail" + sqlQuery);
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partId", partId);
			query.setParameter("branchId", branch);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					Integer part_Id = (Integer) row.get("part_id");
					Integer branchId = (Integer) row.get("branch_id");
					Integer partBranchId = (Integer) row.get("partBranch_id");
					String StoreName = (String) row.get("StoreDesc");
					String BinName = (String) row.get("BinName");
					if (StoreName != null && BinName != null) {
						//.println("Beofre if " + part_Id + "  storeName " + StoreName + " binName " + BinName);
						if (part_Id == partId && StoreName.equalsIgnoreCase(store)
								&& BinName.equalsIgnoreCase(storeBinLocation)) {
							//.println("In if  to add ");

							responseModelList.setPartId((Integer) row.get("part_id"));
							responseModelList.setBranchId((Integer) row.get("branch_id"));
							responseModelList.setPartBranchId((Integer) row.get("partBranch_id"));

						} else {
							//.println("In else nothing to add ");
						}
					}

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		//.println("before sending response  " + responseModelList.toString());
		return responseModelList;
	}

	@Override
	public StockSaveHeaderResponseModel saveStockHeaderWithPartBranchId(
			List<PartBranchDetailResponse> partBranchDetailResponse, Integer branch, String userCode, Integer delaer,
			List<PartsStockUploadModel> excelListOfParts) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + branch);
		}
		StockSaveHeaderResponseModel createStockHeaderResponse = new StockSaveHeaderResponseModel();
		List<StockHeaderWithPartBranchIdModel> stockHeaderList = new ArrayList<>();

		Session session = null;
		Query query = null;
		int i = 0;
		boolean isSuccess = false;

		try {
			String sqlQuery = null;
			//.println("SQL query for PART BRANCH detail" + sqlQuery);
			try {

				for (PartsStockUploadModel model : excelListOfParts) {

					StockHeaderWithPartBranchIdModel stockBinResponse = new StockHeaderWithPartBranchIdModel();
					sqlQuery = "INSERT INTO PA_STOCK_BIN(branch_id, partBranch_id,stock_store_id,binName,binType,currentStock,currentStockAmount,IsDefault,IsBinLocked,LastUpdateDate,IsActive,CreatedDate,CreatedBy,ModifiedBy)"
							+ "VALUES(:branch_id,:partBranch_id,:stock_store_id, :binName,:binType,:currentStock,:currentStockAmount,:IsDefault,:IsBinLocked,:LastUpdateDate,:IsActive,:CreatedDate,:CreatedBy ,:ModifiedBy)";
					session = sessionFactory.openSession();
					//.println("SQL query for PA_STOCK_BIN " + sqlQuery);
					session.beginTransaction();

					query = session.createSQLQuery(sqlQuery).setParameter("branch_id", model.getBranch())
							.setParameter("partBranch_id", partBranchDetailResponse.get(i).getPartBranchId())
							.setParameter("stock_store_id", partBranchDetailResponse.get(i).getStockStoreId())
							.setParameter("binName", model.getStoreBinLocation()).setParameter("binType", "NORMAL")
							.setParameter("currentStock", model.getQuantity())
							.setParameter("currentStockAmount", (model.getQuantity() * model.getNdpPrice()))
							.setParameter("IsDefault", "N").setParameter("IsBinLocked", "N")
							.setParameter("LastUpdateDate", new Date()).setParameter("IsActive", "Y")
							.setParameter("CreatedDate", new Date()).setParameter("CreatedBy", userCode)
							.setParameter("ModifiedBy", userCode);

					int rowCount = query.executeUpdate(); // Execute the insert query
					session.getTransaction().commit();
					isSuccess = true;
					if (rowCount == 1) {

						stockBinResponse = getStockBeanResponseByBranchId(
								partBranchDetailResponse.get(i).getPartBranchId());
						stockHeaderList.add(stockBinResponse);

					}

					i++;
				}
				if (isSuccess) {
					createStockHeaderResponse.setStockHeaderList(stockHeaderList);
					// createStockHeaderResponse.setBranchId(i);
					createStockHeaderResponse.setMessage("Stock bin table updated Successfully");
					createStockHeaderResponse.setStatusCode(200);

				} else {
					createStockHeaderResponse.setMessage("Stock bin not updated ");
					createStockHeaderResponse.setStatusCode(304);

				}

			} catch (SQLGrammarException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			} finally {
				if (session != null) {
					session.close();
				}
			}

		} catch (Exception e) {

			createStockHeaderResponse.setMessage(e.getMessage());
			createStockHeaderResponse.setStatusCode(303);
		}

		return createStockHeaderResponse;
	}

	private StockHeaderWithPartBranchIdModel getStockBeanResponseByBranchId(int partBranchId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + partBranchId);
		}
		Session session = null;
		Query query = null;
		StockHeaderWithPartBranchIdModel responseModelList = new StockHeaderWithPartBranchIdModel();
		;
		String sqlQuery = "SELECT * FROM PA_STOCK_BIN where partBranch_id='" + partBranchId + "'";
		//.println("SQL query for PA_STOCK_BIN" + sqlQuery);
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("branch_id", branch);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					responseModelList.setStockBinId((BigInteger) row.get("stock_bin_id"));
					responseModelList.setBinName((String) row.get("BinName"));
					responseModelList.setBranchId((BigInteger) row.get("branch_id"));
					responseModelList.setStockStoreId((BigInteger) row.get("stock_store_id"));
					responseModelList.setPartBranchId((BigInteger) row.get("partBranch_id"));

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		//.println("before sending response " + responseModelList.toString());
		return responseModelList;

	}

//	private StockHeaderWithPartBranchIdModel insertBinStockHeader(String binName,
//			PartsStockUploadModel partsStockUploadModel, int partBranchId) {
//		  
//		StockHeaderWithPartBranchIdModel stockHederAfterUpdate= new StockHeaderWithPartBranchIdModel();
//		StockStoreResponse stockStoreResponse= getStockStoreByPartBranchId(partBranchId);
//		
//		try
//		{
//			
//			
//			Session session = null;
//			Query query = null;
//			PartBranchDetailResponse responseModelList = new PartBranchDetailResponse();;
//			String sqlQuery = "select  * from PA_PART_BRANCH where branch_id='" + branch + "' and part_id ='"+partId+"'";
//			//.println("SQL query for PART BRANCH detail" + sqlQuery);
//			try {
//				session = sessionFactory.openSession();
//				query = session.createSQLQuery(sqlQuery);
//				//query.setParameter("branch_id", branch);
//				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//				List data = query.list();
//				if (data != null && !data.isEmpty()) {
//					for (Object object : data) {
//						Map row = (Map) object;
//						
//						responseModelList.setPartId((Integer) row.get("part_id"));
//						responseModelList.setBranchId((Integer) row.get("branch_id"));
//						responseModelList.setPartBranchId((Integer) row.get("partBranch_id"));
//					}
//				}
//			} catch (SQLGrammarException exp) {
//				logger.error(this.getClass().getName(), exp);
//			} catch (HibernateException exp) {
//				logger.error(this.getClass().getName(), exp);
//			} catch (Exception exp) {
//				logger.error(this.getClass().getName(), exp);
//			} finally {
//				if (session != null) {
//					session.close();
//				}
//			}
//			//.println("before sending response "+responseModelList.toString());
//		}catch(Exception e)
//		{
//			//.println(" "+e.getMessage());
//		}
//		
//		
//		
//	
//		return null;
//	}

	private StockStoreResponse getStockStoreByPartBranchId(int partBranchId) {

		//.println("fetchduplicateParts invoked with part No " + partBranchId);
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + partBranchId);
		}
		Session session = null;
		Query query = null;
		StockStoreResponse responseModelList = new StockStoreResponse();
		String sqlQuery = "select * from from PA_STOCK_STORE where partBranch_id='" + partBranchId + "'";
		//.println("SQL query for PA_STOCK_STORE" + sqlQuery);
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			// query.setParameter("partNumber", partNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModelList.setBranchStoreId((Integer) row.get("branch_store_id"));
					responseModelList.setPartBranchId((Integer) row.get("partBranchId"));
					responseModelList.setStockStoreId((Integer) row.get("stock_storeId"));

				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;

	}

	@Override
	public StockBinDetailResponseModel updateStockBinDetailTableStockBinId(
			List<StockHeaderWithPartBranchIdModel> stockHeaderList, List<PartsStockUploadModel> excelListOfParts,
			String userCode) {
		StockBinDetailResponseModel createStockDtlResponse = new StockBinDetailResponseModel();
		StockBinDetailModel stockBinModel = new StockBinDetailModel();
		Session session = null;
		Query query = null;
		int i = 0;
		boolean isSuccess = false;

		String sqlQuery = null;
		try {

			for (PartsStockUploadModel model : excelListOfParts) {

				StockHeaderWithPartBranchIdModel stockBinResponse = new StockHeaderWithPartBranchIdModel();
				sqlQuery = "INSERT INTO PA_STOCK_BIN_DTL(branch_id, partBranch_id,stock_store_id,stock_bin_id,document_id,"
						+ "TABLE_NAME,ref_doc_hdr_id,IssueQty,ReceiptQty,UsedQty,DiscountValue,"
						+ "TransactionDate,AvlbQty, NDP,MRP,GNDP,ExciseValue,TaxValue,GrossValue,LandedCost,"
						+ "CreatedDate,CreatedBy)"
						+ "VALUES(:branch_id,:partBranch_id,:stock_store_id,:stock_bin_id,:document_id,"
						+ ":TABLE_NAME,:ref_doc_hdr_id,:IssueQty,:ReceiptQty,"
						+ ":UsedQty,:DiscountValue,:TransactionDate,"
						+ ":AvlQty,:NDP,:MRP,:GNDP,:ExciseValue,:TaxValue,:GrossValue,:LandedCost,:CreatedDate,:CreatedBy)";
				session = sessionFactory.openSession();
				//.println("SQL query for PA_STOCK_BIN_DETAIL after make query " + sqlQuery);
				session.beginTransaction();

				query = session.createSQLQuery(sqlQuery).setParameter("branch_id", stockHeaderList.get(i).getBranchId())
						.setParameter("partBranch_id", stockHeaderList.get(i).getPartBranchId())
						.setParameter("stock_store_id", stockHeaderList.get(i).getStockStoreId())
						.setParameter("stock_bin_id", stockHeaderList.get(i).getStockBinId())
						.setParameter("document_id", 191).setParameter("TABLE_NAME", "OPENING_STOCK")
						.setParameter("ref_doc_hdr_id", 0).setParameter("IssueQty", (double) 0)
						.setParameter("ReceiptQty", (double) model.getQuantity()).setParameter("UsedQty", (double) 0)
						.setParameter("DiscountValue", (double) 0).setParameter("TransactionDate", new Date())
						.setParameter("AvlQty", (double) model.getQuantity()).setParameter("NDP", model.getNdpPrice())
						.setParameter("MRP", model.getMrpPrice()).setParameter("GNDP", (double) 0)
						.setParameter("ExciseValue", (double) 0).setParameter("TaxValue", (double) 0)
						.setParameter("GrossValue", (double) 0).setParameter("LandedCost", (double) 0)
						.setParameter("CreatedDate", new Date()).setParameter("CreatedBy", userCode);

				int rowCount = query.executeUpdate(); // Execute the insert query
				session.getTransaction().commit();
				isSuccess = true;
				if (rowCount == 1) {

					i++;

					stockHeaderList.add(stockBinResponse);

				}

			}

			if (isSuccess) {
				createStockDtlResponse.setMessage("StockBinDetail Updated Successfully");
				createStockDtlResponse.setStatusCode(200);
			} else {
				createStockDtlResponse.setMessage("StockBinDetail Not updated correctly");
				createStockDtlResponse.setStatusCode(304);
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return createStockDtlResponse;
	}

}
