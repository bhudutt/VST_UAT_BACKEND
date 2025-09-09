/**
 * 
 */
package com.hitech.dms.web.dao.aop.template.upload;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.utils.FiscalDate;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.aop.StgAopHdrEntity;
import com.hitech.dms.web.entity.aop.StgAopTargetDtlEntity;
import com.hitech.dms.web.model.aop.template.upload.request.AopTargetUploadRequestModel;
import com.hitech.dms.web.model.aop.template.upload.response.AopTargetUploadResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AopTargetUploadDaoImpl implements AopTargetUploadDao {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetUploadDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public AopTargetUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			AopTargetUploadRequestModel requestModel, List<MultipartFile> files) {
		// TODO Auto-generated method stub
		logger.debug(userCode + " : " + requestModel.toString());
		logger.debug("fetching Models By PC Id : ");
		Session session = null;
		Transaction transaction = null;
		AopTargetUploadResponseModel responseModel = new AopTargetUploadResponseModel();
		String aopNumber = null;
		boolean isSuccess = true;
		try {
			long start = System.currentTimeMillis();
			List<ModelByPcIdResponseModel> modelByPcIdResponseModelList = commonDao
					.fetchModelListByPcId(authorizationHeader, requestModel.getPcId(), "AOPTARGET");
			if (modelByPcIdResponseModelList == null) {
				// set error msg & status code
				logger.error("Model Not Found. Kindly Contact Your Administrator.");
				responseModel.setMsg("Model Not Found. Kindly Contact Your Administrator.");
				responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
				isSuccess = false;
				return responseModel;
			}
			Map<String, Object> mapData = FiscalDate.displayFinancialDate(Calendar.getInstance());
			requestModel.setFinYear((String) mapData.get("FiscalYears"));
			Map<Integer, String> map = new HashMap<Integer, String>();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(files.get(0).getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			int months = 0;
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				Cell cell = row.getCell(colIx);
				if (requestModel.getFinYear().contains(cell.getStringCellValue().trim())) {
					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
					months = 12;
				}
				if (months > 0) {
					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
					months--;
				}
			}
			if (map.isEmpty()) {
				logger.error("INVALID EXCEL FORMAT.");
				responseModel.setMsg("INVALID EXCEL FORMAT.");
				responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
				isSuccess = false;
				return responseModel;
			}

			StgAopHdrEntity aopHdrEntity = null;
			List<StgAopTargetDtlEntity> aopTargetDtlList = null;
			if (isSuccess) {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				BigInteger userId = null;
				mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					userId = (BigInteger) mapData.get("userId");
					// fetch Dealer Detail
					DealerDTLResponseModel dealerDtl = commonDao.fetchDealerDTLByDealerId(authorizationHeader,
							requestModel.getDealerId(), "AOPTARGET");
					if (dealerDtl != null) {
						Date currDate = new Date();
						SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
						String strYearYYYY = simpleformat.format(currDate);

						String lastAopNumber = null;
						String lastStgAopNumber = null;
						simpleformat = new SimpleDateFormat("yy");
						String strYear = simpleformat.format(currDate);
						String pcCode = null;
						// validating same number in STG table also, two or more user can hit at same
						// time, so need to track doc number and prevent to upload same data
						mapData = fetchLastSTGAOPTargetDTLByDLRId(session, requestModel.getDealerId(),
								requestModel.getPcId(), strYearYYYY);
						if (mapData != null && mapData.get("SUCCESS") != null) {
							if (mapData.get("docNumber") != null) {
								// delete from stg table
								lastStgAopNumber = (String) mapData.get("docNumber");
								Query query = session.createNativeQuery("delete TD from STG_SA_MIS_AOP_TARGET_DTL TD "
										+ " inner join STG_SA_MIS_AOP_TARGET_HDR (nolock) TH ON TD.stg_aop_Id = TH.stg_aop_Id"
										+ " where TH.aop_number =:aopNumber");
								query.setParameter("aopNumber", lastStgAopNumber);
								int k = query.executeUpdate();
								if (k > 0) {
									query = session.createNativeQuery(
											"delete from STG_SA_MIS_AOP_TARGET_HDR " + " where aop_number =:aopNumber");
									query.setParameter("aopNumber", lastStgAopNumber);
									k = query.executeUpdate();
									if(k > 0) {
										lastStgAopNumber = null;
									}
								}
							}
						}
						mapData = fetchLastAOPTargetDTLByDLRId(session, userCode, requestModel.getDealerId(),
								requestModel.getPcId(), strYearYYYY, requestModel.getAopId());
						if (mapData != null && mapData.get("SUCCESS") != null) {
							pcCode = (String) mapData.get("pcCode");
							String lastAopStatus = (String) mapData.get("docStatus");
							int revisionCount = (int) mapData.get("revisionCount");
							if(revisionCount == 0) {
								revisionCount = 1;
							}else {
								revisionCount++;
							}
							if (mapData.get("docNumber") != null) {
								if (mapData.get("finYear") != null
										&& mapData.get("finYear").toString().equals(requestModel.getFinYear())
										&& (lastAopStatus != null && !lastAopStatus.equalsIgnoreCase("Rejected"))) {
									logger.error("Already AOP Target has been uploaded for Current FY : {}",
											requestModel.getFinYear());
									responseModel.setMsg("Already AOP Target has been uploaded for Current FY "
											+ requestModel.getFinYear());
									responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
									isSuccess = false;
								}
								if (isSuccess) {
									lastAopNumber = (String) mapData.get("docNumber");

									if (mapData != null && mapData.get("SUCCESS") != null) {
										if (lastStgAopNumber != null && lastAopNumber.equals(lastStgAopNumber)) {
											logger.error("Someone Already uploaded AOP Target for Current FY : {}",
													requestModel.getFinYear());
											responseModel.setMsg("Someone Already uploaded AOP Target for Current FY "
													+ requestModel.getFinYear());
											responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
											isSuccess = false;
										}
									}
									if (isSuccess) {
										int lastIndexOf = lastAopNumber.lastIndexOf(strYear);
										if (lastIndexOf > 0) {
											String prefix = lastAopNumber.substring(0, lastIndexOf);
											aopNumber = lastAopNumber.substring(lastIndexOf + 2,
													lastAopNumber.length());
											Integer i = Integer.valueOf(aopNumber);
											aopNumber = prefix + strYear + String.format("%04d", i + revisionCount);
										} else {
											aopNumber = "IN" + dealerDtl.getDealerCode() + pcCode + strYear + "0001";
										}
									}
								}
							} else {
								aopNumber = "IN" + dealerDtl.getDealerCode() + pcCode + strYear + "0001";
							}

						} else {
							logger.error(this.getClass().getName(),
									"Error While Validating Last Machine Indent Number.");
							responseModel.setMsg(
									"Error While Validating Last Machine Indent Number. Please Contact Your System Administrator.");
							isSuccess = false;
						}
						if (isSuccess) {
							// inserting into STG Table
							aopHdrEntity = new StgAopHdrEntity();
							aopHdrEntity.setDealerId(requestModel.getDealerId());
							aopHdrEntity.setAopDate(requestModel.getAopDate());
							aopHdrEntity.setAopFinYr(requestModel.getFinYear());
							aopHdrEntity.setPcId(requestModel.getPcId());
							aopHdrEntity.setAopNumber(aopNumber);
							aopHdrEntity.setAopStatus(WebConstants.PENDING);
							aopHdrEntity.setRemarks(requestModel.getRemarks());
							aopHdrEntity.setCreatedBy(userId);
							aopHdrEntity.setCreatedDate(currDate);

							aopTargetDtlList = new ArrayList<StgAopTargetDtlEntity>();
							for (int x = 1; x < totalRows - 1; x++) {
								Row dataRow = sheet.getRow(x);
								if (!isSuccess) {
									break;
								}
								StgAopTargetDtlEntity aopTargetDtlEntity = new StgAopTargetDtlEntity();
								aopTargetDtlEntity.setAop(aopHdrEntity);
								for (short colIx = minColIx; colIx < maxColIx; colIx++) {
									Cell cell = dataRow.getCell(colIx);

									if (colIx == 5) {
										String record = checkandreturnCellVal(cell);
										Optional<ModelByPcIdResponseModel> findFirst = modelByPcIdResponseModelList
												.stream().filter(m -> m.getItem().equals(record)).findFirst();
										logger.info(findFirst != null ? findFirst.toString() : "findFirst is null");
										if (findFirst.isEmpty()) {
											isSuccess = false;
											responseModel.setMsg("Model Not Found.");
											break;
										}
										logger.info(record);
										aopTargetDtlEntity.setModel(findFirst.get().getModel());
										aopTargetDtlEntity.setItem(findFirst.get().getItem());
										aopTargetDtlEntity.setItemDesc(findFirst.get().getItemDesc());
										aopTargetDtlEntity.setSegment(findFirst.get().getSegment());
										aopTargetDtlEntity.setSeries(findFirst.get().getSeries());
										aopTargetDtlEntity.setVariant(findFirst.get().getVariant());
										aopTargetDtlEntity.setMachineItemId(findFirst.get().getMachineItemId());
									} else if (colIx == 8) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth1(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 9) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth2(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 10) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth3(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 11) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth4(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 12) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth5(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 13) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth6(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 14) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth7(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 15) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth8(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 16) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth9(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 17) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth10(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 18) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth11(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 19) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										aopTargetDtlEntity.setMonth12(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									}
								}
								aopTargetDtlList.add(aopTargetDtlEntity);
							}
							logger.info("aopTargetDtlList {}", aopTargetDtlList.toString());
							aopHdrEntity.setAopTargetDtlList(aopTargetDtlList);

							session.save(aopHdrEntity);
						}
					} else {
						// Dealer Not Found.
						isSuccess = false;
						responseModel.setMsg("Dealer Not Found.");
					}
				} else {
					// User not found
					isSuccess = false;
					responseModel.setMsg("User Not Found.");
				}
			}
			if (isSuccess) {
				transaction.commit();
				responseModel.setAopId(aopHdrEntity.getStgAopId());
				responseModel.setAopNumber(aopNumber);
				responseModel.setAopHdrEntity(aopHdrEntity);
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setMsg("AOP Target Data has been uploaded successfuly.");
				logger.info("Aop Target Uploaded time : " + (System.currentTimeMillis() - start));
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			if (responseModel.getMsg() == null) {
				responseModel.setMsg(ex.getMessage());
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (!isSuccess) {
				// error
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Uploading Aop Target.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastAOPTargetDTLByDLRId(Session session, String userCode, BigInteger dealerId, Integer pcId,
			String fy, BigInteger aopId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "exec [SP_SA_MIS_AOP_LastAOPTargetDTLByDLRId] :userCode, :dealerId, :pcId, :fy, :aopId";
		mapData.put("ERROR", "AOP Target Last Number Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", dealerId);
			query.setParameter("pcId", pcId);
			query.setParameter("fy", fy);
			query.setParameter("aopId", aopId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("docNumber", row.get("aop_number"));
					mapData.put("docStatus", row.get("aop_status"));
					mapData.put("finYear", row.get("aop_fin_year"));
					mapData.put("pcCode", (String) row.get("pc_code"));
					mapData.put("revisionCount", (int) row.get("generation_number"));
				}
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE AOP TARGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE  AOP TARGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastSTGAOPTargetDTLByDLRId(Session session, BigInteger dealerId, Integer pcId,
			String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 aop_number, pc.pc_code from ADM_BP_MST_PROFIT_CENTER (nolock) pc "
				+ "	left join  STG_SA_MIS_AOP_TARGET_HDR (nolock) pr on pr.pc_id = pc.pc_id and pr.dealer_id =:dealerId and DATEPART(year,aop_date)=:fy "
				+ "	where pc.pc_id =:pcId order by stg_aop_Id desc ";
		mapData.put("ERROR", "STG AOP Target Last Number Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
			query.setParameter("pcId", pcId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String docNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					docNumber = (String) row.get("aop_number");
					mapData.put("pcCode", (String) row.get("pc_code"));
				}
				mapData.put("docNumber", docNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE STG AOP TARGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE  STG AOP TARGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchAOPTargetNoByHdrId(Session session, BigInteger hdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select aop_number from SA_MIS_AOP_TARGET_HDR (nolock) pr where pr.aop_Id =:hdrId";
		mapData.put("ERROR", "Machine AOP Target Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hdrId", hdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String docNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					docNumber = (String) row.get("aop_number");
				}
				mapData.put("docNumber", docNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE AOP TARGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE AOP TARGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
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
			} else if (type == CellType.BLANK) {
				cellval = "";
			}
		}
		return cellval;
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}

}
