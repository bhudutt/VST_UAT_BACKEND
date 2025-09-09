/**
 * 
 */
package com.hitech.dms.web.dao.opex.template.upload;

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
import com.hitech.dms.web.entity.opex.StgOpexBudgetDtlEntity;
import com.hitech.dms.web.entity.opex.StgOpexHdrEntity;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.opex.gl.response.GlMstResponseModel;
import com.hitech.dms.web.model.opex.template.upload.request.OpexBudgetUploadRequestModel;
import com.hitech.dms.web.model.opex.template.upload.response.OpexBudgetUploadResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OpexBudgetUploadDaoImpl implements OpexBudgetUploadDao {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetUploadDaoImpl.class);

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
	public OpexBudgetUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			OpexBudgetUploadRequestModel requestModel, List<MultipartFile> files) {
		// TODO Auto-generated method stub
		logger.debug(userCode + " : " + requestModel.toString());
		logger.debug("fetching Models By PC Id : ");
		Session session = null;
		Transaction transaction = null;
		OpexBudgetUploadResponseModel responseModel = new OpexBudgetUploadResponseModel();
		String opexNumber = null;
		boolean isSuccess = true;
		try {
			long start = System.currentTimeMillis();
			session = sessionFactory.openSession();
			List<GlMstResponseModel> glMstList = commonDao.fetchGlMstList(session, userCode, requestModel.getPcId());
			if (glMstList == null) {
				// set error msg & status code
				logger.error("GL Master Not Found. Kindly Contact Your Administrator.");
				responseModel.setMsg("GL Master Not Found. Kindly Contact Your Administrator.");
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

			StgOpexHdrEntity opexHdrEntity = null;
			List<StgOpexBudgetDtlEntity> opexBudgetDtlList = null;
			if (isSuccess) {
				transaction = session.beginTransaction();
				BigInteger userId = null;
				mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					userId = (BigInteger) mapData.get("userId");
					// fetch State Detail
					GeoStateDTLResponseModel stateDTLResponseModel = commonDao
							.fetchStateDtlByStateID(authorizationHeader, requestModel.getStateId());
					if (stateDTLResponseModel != null) {
						Date currDate = new Date();
						SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
						String strYearYYYY = simpleformat.format(currDate);

						String lastOpexNumber = null;
						String lastStgOpexNumber = null;
						simpleformat = new SimpleDateFormat("yy");
						String strYear = simpleformat.format(currDate);
						String pcCode = null;
						// validating same number in STG table also, two or more user can hit at same
						// time, so need to track doc number and prevent to upload same data
						mapData = fetchLastSTGOPEXBudgetDTLByStateId(session, requestModel.getStateId(),
								requestModel.getPcId(), strYearYYYY);
						if (mapData != null && mapData.get("SUCCESS") != null) {
							if (mapData.get("docNumber") != null) {
								// delete from stg table
								lastStgOpexNumber = (String) mapData.get("docNumber");
								Query query = session.createNativeQuery("delete TD from STG_SA_MIS_OPEX_BUDGET_DTL TD "
										+ " inner join STG_SA_MIS_OPEX_BUDGET_HDR (nolock) TH ON TD.stg_opex_Id = TH.stg_opex_Id"
										+ " where TH.opex_number =:opexNumber");
								query.setParameter("opexNumber", lastStgOpexNumber);
								int k = query.executeUpdate();
								if (k > 0) {
									query = session.createNativeQuery("delete from STG_SA_MIS_OPEX_BUDGET_HDR "
											+ " where opex_number =:opexNumber");
									query.setParameter("opexNumber", lastStgOpexNumber);
									k = query.executeUpdate();
									if (k > 0) {
										lastStgOpexNumber = null;
									}
								}
							}
						}
						mapData = fetchLastOPEXBudgetDTLByStateId(session, userCode, requestModel.getStateId(),
								requestModel.getPcId(), strYearYYYY, requestModel.getOpexId());
						if (mapData != null && mapData.get("SUCCESS") != null) {
							pcCode = (String) mapData.get("pcCode");
							String lastAopStatus = (String) mapData.get("docStatus");
							int revisionCount = (int) mapData.get("revisionCount");
							if (revisionCount == 0) {
								revisionCount = 1;
							} else {
								revisionCount++;
							}
							if (mapData.get("docNumber") != null) {
								if (mapData.get("finYear") != null
										&& mapData.get("finYear").toString().equals(requestModel.getFinYear())
										&& (lastAopStatus != null && !lastAopStatus.equalsIgnoreCase("Rejected"))) {
									logger.error("Already OPEX Budget has been uploaded for Current FY : {}",
											requestModel.getFinYear());
									responseModel.setMsg("Already OPEX Budget has been uploaded for Current FY "
											+ requestModel.getFinYear());
									responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
									isSuccess = false;
								}
								if (isSuccess) {
									lastOpexNumber = (String) mapData.get("docNumber");

									if (mapData != null && mapData.get("SUCCESS") != null) {
										if (lastStgOpexNumber != null && lastOpexNumber.equals(lastStgOpexNumber)) {
											logger.error("Someone Already uploaded OPEX Budget for Current FY : {}",
													requestModel.getFinYear());
											responseModel.setMsg("Someone Already uploaded OPEX Budget for Current FY "
													+ requestModel.getFinYear());
											responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
											isSuccess = false;
										}
									}
									if (isSuccess) {
										int lastIndexOf = lastOpexNumber.lastIndexOf(strYear);
										if (lastIndexOf > 0) {
											String prefix = lastOpexNumber.substring(0, lastIndexOf);
											opexNumber = lastOpexNumber.substring(lastIndexOf + 2,
													lastOpexNumber.length());
											Integer i = Integer.valueOf(opexNumber);
											opexNumber = prefix + strYear + String.format("%04d", i + revisionCount);
										} else {
											opexNumber = "OP" + stateDTLResponseModel.getStateCode() + pcCode + strYear
													+ "0001";
										}
									}
								}
							} else {
								opexNumber = "OP" + stateDTLResponseModel.getStateCode() + pcCode + strYear + "0001";
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
							opexHdrEntity = new StgOpexHdrEntity();
							opexHdrEntity.setStateId(requestModel.getStateId());
							opexHdrEntity.setOpexDate(requestModel.getOpexDate());
							opexHdrEntity.setOpexFinYr(requestModel.getFinYear());
							opexHdrEntity.setPcId(requestModel.getPcId());
							opexHdrEntity.setOpexNumber(opexNumber);
							opexHdrEntity.setOpexStatus(WebConstants.PENDING);
							opexHdrEntity.setRemarks(requestModel.getRemarks());
							opexHdrEntity.setCreatedBy(userId);
							opexHdrEntity.setCreatedDate(currDate);

							opexBudgetDtlList = new ArrayList<StgOpexBudgetDtlEntity>();
							for (int x = 1; x < totalRows - 1; x++) {
								Row dataRow = sheet.getRow(x);
								if (!isSuccess) {
									break;
								}
								StgOpexBudgetDtlEntity opexBudgetDtlEntity = new StgOpexBudgetDtlEntity();
								opexBudgetDtlEntity.setOpex(opexHdrEntity);
								for (short colIx = minColIx; colIx < maxColIx; colIx++) {
									Cell cell = dataRow.getCell(colIx);

									if (colIx == 1) {
										String record = checkandreturnCellVal(cell);
										Optional<GlMstResponseModel> findFirst = glMstList.stream()
												.filter(g -> g.getGlHeaderCode().equals(record)).findFirst();
										logger.info(findFirst != null ? findFirst.toString() : "findFirst GL is null");
										if (findFirst.isEmpty()) {
											isSuccess = false;
											responseModel.setMsg("GL Not Found.");
											break;
										}
										logger.info(record);
										opexBudgetDtlEntity.setGlId(findFirst.get().getGlId());
										opexBudgetDtlEntity.setGlHeadCode(findFirst.get().getGlHeaderCode());
										opexBudgetDtlEntity.setGlHeadName(findFirst.get().getGlHeaderName());
									} else if (colIx == 4) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth1(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 5) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth2(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 6) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth3(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 7) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth4(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 8) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth5(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 9) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth6(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 10) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth7(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 11) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth8(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 12) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth9(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 13) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth10(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 14) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth11(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									} else if (colIx == 15) {
										String record = checkandreturnCellVal(cell);
										System.out.println(record);
										opexBudgetDtlEntity.setMonth12(
												new BigDecimal(record == null || record.equals("") ? "0" : record));
									}
								}
								opexBudgetDtlList.add(opexBudgetDtlEntity);
							}
							logger.info("opexBudgetDtlList {}", opexBudgetDtlList.toString());
							opexHdrEntity.setOpexBudgetDtlList(opexBudgetDtlList);

							session.save(opexHdrEntity);
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
				responseModel.setOpexId(opexHdrEntity.getStgOpexId());
				responseModel.setOpexNumber(opexNumber);
				responseModel.setOpexHdrEntity(opexHdrEntity);
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setMsg("Opex Budget Data has been uploaded successfuly.");
				logger.info("Opex Budget Uploaded time : " + (System.currentTimeMillis() - start));
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
	public Map<String, Object> fetchLastOPEXBudgetDTLByStateId(Session session, String userCode, BigInteger stateId,
			Integer pcId, String fy, BigInteger opexId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "exec [SP_SA_MIS_OPEX_LastOPEXBUDGETDTLBySTATEId] :userCode, :stateId, :pcId, :fy, :opexId";
		mapData.put("ERROR", "OPEX Budget Last Number Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("stateId", stateId);
			query.setParameter("pcId", pcId);
			query.setParameter("fy", fy);
			query.setParameter("opexId", opexId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("docNumber", row.get("opex_number"));
					mapData.put("docStatus", row.get("opex_status"));
					mapData.put("finYear", row.get("opex_fin_year"));
					mapData.put("pcCode", (String) row.get("pc_code"));
					mapData.put("revisionCount", (int) row.get("generation_number"));
				}
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last OPEX BUDGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE OPEX BUDGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastSTGOPEXBudgetDTLByStateId(Session session, BigInteger stateId, Integer pcId,
			String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 opex_number, pc.pc_code from ADM_BP_MST_PROFIT_CENTER (nolock) pc "
				+ "	left join  STG_SA_MIS_OPEX_BUDGET_HDR (nolock) pr on pr.pc_id = pc.pc_id and pr.state_id =:stateId and DATEPART(year,opex_date)=:fy "
				+ "	where pc.pc_id =:pcId order by stg_opex_Id desc ";
		mapData.put("ERROR", "STG AOP Target Last Number Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("stateId", stateId);
			query.setParameter("pcId", pcId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String docNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					docNumber = (String) row.get("opex_number");
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
	public Map<String, Object> fetchOpexBudgetNoByHdrId(Session session, BigInteger hdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select opex_number from SA_MIS_OPEX_BUDGET_HDR (nolock) pr where pr.opex_Id =:hdrId";
		mapData.put("ERROR", "Machine OPEX Budget Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hdrId", hdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String docNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					docNumber = (String) row.get("opex_number");
				}
				mapData.put("docNumber", docNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE OPEX BUDGET DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE OPEX BUDGET DETAILS");
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
