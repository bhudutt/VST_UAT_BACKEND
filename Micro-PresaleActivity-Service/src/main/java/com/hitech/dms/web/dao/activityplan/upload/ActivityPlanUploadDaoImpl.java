/**
 * 
 */
package com.hitech.dms.web.dao.activityplan.upload;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
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
import org.hibernate.internal.SessionImpl;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.app.utils.DateUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.dao.dealer.DealerListUnderUserDao;
import com.hitech.dms.web.entity.activityPlan.ActivityPlanApprovalEntity;
import com.hitech.dms.web.entity.activityPlan.ActivityPlanDLRModelDTLEntity;
import com.hitech.dms.web.entity.activityPlan.ActivityPlanDLRModelEntity;
import com.hitech.dms.web.entity.activityPlan.ActivityPlanHDREntity;
import com.hitech.dms.web.entity.activityPlan.stg.StgActivityPlanDLRModelDTLEntity;
import com.hitech.dms.web.entity.activityPlan.stg.StgActivityPlanDLRModelEntity;
import com.hitech.dms.web.entity.activityPlan.stg.StgActivityPlanHDREntity;
import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;
import com.hitech.dms.web.model.activityplan.approval.response.ActivityPlanDateResponse;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanFormRequestModel;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;
import com.hitech.dms.web.model.activityplan.upload.response.ActivityPlanUploadResponseModel;
import com.hitech.dms.web.model.dealer.UserDealerResponseModel;
import com.hitech.dms.web.model.dynamicColumns.JSONObject;
import com.hitech.dms.web.model.dynamicColumns.ReportModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentRequestModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityPlanUploadDaoImpl implements ActivityPlanUploadDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanUploadDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonServiceClient commonServiceClient;
	@Autowired
	private DealerListUnderUserDao dealerListUnderUserDao;
	@Autowired
	private ActivityCommonDao activityCommonDao;

	public List<ActivitySourceListResponseModel> fetchActivitySourceList(String authorizationHeader, Integer pcId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchActivitySourceList invoked..");
		}
		List<ActivitySourceListResponseModel> activitySourceList = null;
		try {
			ActivitySourceListRequestModel requestFormModel = new ActivitySourceListRequestModel();
			requestFormModel.setPcId(pcId);
			requestFormModel.setIsFor("Activity");
			requestFormModel.setIsIncludeInActive("Y");
			HeaderResponse headerResponse = commonServiceClient.fetchActivitySourceListByPcId(authorizationHeader,
					requestFormModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					activitySourceList = jsonArrayToObjectList(jsonString, ActivitySourceListResponseModel.class);
					logger.debug((activitySourceList == null ? "Activity Source Not Found."
							: activitySourceList.toString()));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return activitySourceList;
	}

	public List<ModelsForSeriesSegmentResponseModel> fetchModelsForSeriesSegment(String authorizationHeader,
			Integer pcId, String seriesName, String segment) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchModelsForSeriesSegment invoked..");
		}
		List<ModelsForSeriesSegmentResponseModel> responseList = null;
		try {
			ModelsForSeriesSegmentRequestModel requestFormModel = new ModelsForSeriesSegmentRequestModel();
			requestFormModel.setPcId(pcId);
			requestFormModel.setSeriesName(seriesName);
			requestFormModel.setSegment(segment);
			HeaderResponse headerResponse = commonServiceClient.fetchModelsForSeriesSegment(authorizationHeader,
					requestFormModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					responseList = jsonArrayToObjectList(jsonString, ModelsForSeriesSegmentResponseModel.class);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseList;
	}

	public ActivityPlanUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			ActivityPlanUploadRequestModel activityPlanUploadRequestModel, List<MultipartFile> files) {
		logger.debug("validateUploadedFile : " + activityPlanUploadRequestModel.toString());
		logger.debug("userCode validateUploadedFile : " + userCode);
		ActivityPlanUploadResponseModel responseModel = new ActivityPlanUploadResponseModel();
		boolean isSuccess = true;
		String msg = null;
		try {
			List<ActivitySourceListResponseModel> activitySourceList = fetchActivitySourceList(authorizationHeader,
					activityPlanUploadRequestModel.getPcID());
			List<UserDealerResponseModel> usrDealerList = dealerListUnderUserDao
					.fetchUserDealerList(authorizationHeader, activityPlanUploadRequestModel.getIsInactiveInclude());

			List<ModelsForSeriesSegmentResponseModel> modelList = fetchModelsForSeriesSegment(authorizationHeader,
					activityPlanUploadRequestModel.getPcID(), activityPlanUploadRequestModel.getSeriesName(),
					activityPlanUploadRequestModel.getSegmentName());

			logger.info(modelList != null ? modelList.toString() : "model List not found");

			ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("STATE", "DEALER CODE", "DEALER NAME",
					"MODEL", "PLAN DATE", "DELIVERY TARGET", "CONVERSION RATION", "VST SHARE %", "BILLING PLAN"));

			List<String> activityDescList = activitySourceList.stream().map(e -> e.getActivityDesc())
					.collect(Collectors.toList());

			List<String> combinedHeaderList = Stream.of(staticHeaderList, activityDescList).flatMap(x -> x.stream())
					.collect(Collectors.toList());
			logger.debug((combinedHeaderList == null ? "Header List Not Found." : combinedHeaderList.toString()));
			Map<Integer, String> map = new HashMap<Integer, String>();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(files.get(0).getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				Cell cell = row.getCell(colIx);
				if (combinedHeaderList.contains(cell.getStringCellValue().trim())) {
					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
				} else {
					logger.error("INVALID EXCEL FORMAT.");
					responseModel.setMsg("INVALID EXCEL FORMAT.");
					responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
					isSuccess = false;
					break;
				}
			}
			StgActivityPlanHDREntity stgActivityPlanHDREntity = null;
			List<StgActivityPlanDLRModelEntity> activityPlanDLRModelList = null;
			List<StgActivityPlanDLRModelDTLEntity> activityPlanDLRModelDTLList = null;
			Date currDate = new Date();
			String stgIdNumber = "STG_" + currDate.getYear() + currDate.getMonth() + currDate.getDay()
					+ currDate.getTime();
			if (isSuccess) {
				String record = null;
				stgActivityPlanHDREntity = new StgActivityPlanHDREntity();
				stgActivityPlanHDREntity.setActivityCreationDate(currDate);
				stgActivityPlanHDREntity.setActivityMonth(
						activityPlanUploadRequestModel.getMonth() == null ? DateUtils.getNextMonth(currDate).getMonth()
								: activityPlanUploadRequestModel.getMonth());
				stgActivityPlanHDREntity.setActivityYear(
						activityPlanUploadRequestModel.getYear() == null ? DateUtils.getNextMonth(currDate).getYear()
								: activityPlanUploadRequestModel.getYear());
				stgActivityPlanHDREntity.setPcId(activityPlanUploadRequestModel.getPcID());
				stgActivityPlanHDREntity.setSegmentName(activityPlanUploadRequestModel.getSegmentName() == null ? "All"
						: activityPlanUploadRequestModel.getSegmentName());
				stgActivityPlanHDREntity.setSeriesName(activityPlanUploadRequestModel.getSeriesName() == null ? "All"
						: activityPlanUploadRequestModel.getSeriesName());
				stgActivityPlanHDREntity.setStgActivityPlanHdrId(stgIdNumber);

				activityPlanDLRModelList = new ArrayList<StgActivityPlanDLRModelEntity>();
				for (int x = 1; x < totalRows; x++) {
					Row dataRow = sheet.getRow(x);
					stgIdNumber = "STG_" + x + "_" + currDate.getYear() + currDate.getMonth() + currDate.getDay()
							+ currDate.getTime();
					int k = 1;
					if (!isSuccess) {
						break;
					}
					try {
						StgActivityPlanDLRModelEntity activityPlanDLRModelEntity = new StgActivityPlanDLRModelEntity();
						activityPlanDLRModelDTLList = new ArrayList<StgActivityPlanDLRModelDTLEntity>();
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							Cell cell = dataRow.getCell(colIx);

							if (activityPlanDLRModelEntity.getStgActivityPlanDlrId() == null) {
								activityPlanDLRModelEntity.setStgActivityPlanDlrId(stgIdNumber);
								activityPlanDLRModelEntity
										.setStgActivityPlanHdrId(stgActivityPlanHDREntity.getStgActivityPlanHdrId());
							}
							if (colIx == 0) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setLocation(record);
								}
							} else if (colIx == 1) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setDealerCode(record);
									UserDealerResponseModel userBranchModel = usrDealerList.stream().filter(
											u -> u.getDealerCode().equals(activityPlanDLRModelEntity.getDealerCode()))
											.findFirst().orElse(null);
									logger.debug(userBranchModel == null ? "User branch is Null."
											: userBranchModel.toString());
									activityPlanDLRModelEntity.setDealerId(userBranchModel.getDealerId());
								}
							} else if (colIx == 2) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setDealerName(record);
								}
							} else if (colIx == 3) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setModelName(record);
									logger.info(activityPlanDLRModelEntity.getModelName());
									ModelsForSeriesSegmentResponseModel segmentResponseModel = modelList.stream().filter(m -> m.getModelName().trim().equalsIgnoreCase(activityPlanDLRModelEntity.getModelName().trim()))
											.findFirst().orElse(null);
									logger.info(segmentResponseModel != null ? segmentResponseModel.toString()
											: "segmentResponseModel is null");
									if (activityPlanDLRModelEntity.getModelName() != null
											&& !activityPlanDLRModelEntity.getModelName().equalsIgnoreCase("All")) {
										if (segmentResponseModel == null) {
											msg = "Model Not Found.";
											isSuccess = false;
											responseModel.setMsg("Model Not Found.");
											break;
										}
									}
								}
							} else if (colIx == 4) {
								record = checkandreturnCellVal(cell);
								if (record != null && record.equalsIgnoreCase("wrong")) {
									responseModel.setMsg("Invoice Date cell contain formula ");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								} else {
									if (record != null && !record.equalsIgnoreCase("")) {
										String dateFormat = checkDateFormat(record);
										if (!dateFormat.equalsIgnoreCase("not")) {
											activityPlanDLRModelEntity.setPlanDate(record);
										} else {
											responseModel.setMsg("Invoice Date should be in Indain Format");
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}

									} else {
										responseModel.setMsg("Invoice Date can't be blank");
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}
								}

							} else if (colIx == 5) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setDeliveryTarget(Integer.parseInt(record));
								}
							} else if (colIx == 6) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setConvRatio(Integer.parseInt(record));
								}
							} else if (colIx == 7) {
								String cellValue = cell.toString();
								double doubleValue = Double.parseDouble(cellValue);
								if(doubleValue==0.0) {
									responseModel.setMsg("VST Share can not be zero");
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setOemShare(new BigDecimal(record));
								}
							} else if (colIx == 8) {
								record = checkandreturnCellVal(cell);
								if (record != null) {
									activityPlanDLRModelEntity.setBillingPlan(Integer.parseInt(record));
								}
							}

							if (colIx > 8) {
								record = checkandreturnCellVal(cell);
								System.out.println("record "+record);
								if (record != null) {
									String stgDTLIdNumber = "STG_" + x + "_" + k + "_" + currDate.getYear()
											+ currDate.getMonth() + currDate.getDay() + currDate.getTime();

									StgActivityPlanDLRModelDTLEntity activityPlanDLRModelDTLEntity = new StgActivityPlanDLRModelDTLEntity();
									activityPlanDLRModelDTLEntity.setStgActivityPlanDlrDtlId(stgDTLIdNumber);
									activityPlanDLRModelDTLEntity.setStgActivityPlanDlrId(
											activityPlanDLRModelEntity.getStgActivityPlanDlrId());

									activityPlanDLRModelDTLEntity.setActivityDesc(map.get((int) colIx));

									activityPlanDLRModelDTLEntity.setNoOfDays(Integer.parseInt(record));

									activityPlanDLRModelDTLList.add(activityPlanDLRModelDTLEntity);

									k++;
								}
								System.out.println("activityPlanDLRModelDTLList size "+activityPlanDLRModelDTLList.size());
							}
						}
						if (activityPlanDLRModelDTLList == null || activityPlanDLRModelDTLList.isEmpty()) {
							msg = "No. Of Days Should Be Entered Against AtLeast One Activity For Each Row.";
							logger.error(msg);
							isSuccess = false;
							throw new Exception(msg);
						}
						activityPlanDLRModelEntity.setActivityPlanDLRModelDTLList(activityPlanDLRModelDTLList);
						activityPlanDLRModelList.add(activityPlanDLRModelEntity);

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
					stgActivityPlanHDREntity.setActivityPlanDLRModelList(activityPlanDLRModelList);
					// insert records into table

					Map<String, Object> mapData = saveActivityPlanToSTG(stgActivityPlanHDREntity, activitySourceList,
							usrDealerList);
					if (mapData != null && mapData.get("SUCCESS") != null) {
						// fetch Activity Plan List
						JSONArray activityPlanJsonArr = fetchActivityPlanList(
								stgActivityPlanHDREntity.getStgActivityPlanHdrId(), userCode);
						if (activityPlanJsonArr != null && !activityPlanJsonArr.isEmpty()) {
							// success
							responseModel.setPlanActivityNumber(stgActivityPlanHDREntity.getStgActivityPlanHdrId());
							responseModel.setActivityPlanJsonArr(activityPlanJsonArr);
							responseModel.setMsg("Activity Plan Uploaded Successfully.");
							responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
						} else {
							// error
							logger.error("Error While Fetching Activity Plan From Staging Table.");
							responseModel.setMsg("Error While Uploading Activity Plan.");
							isSuccess = false;
							responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						}
					} else {
						// error
						if (msg == null) {
							msg = "Error While Uploading Activity Plan.";
						}
						logger.error("Error While Saving Activity Plan Into Staging Table.");
						isSuccess = false;
						responseModel.setMsg(msg);
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					}
				} else {
					// error
					if (msg == null) {
						msg = "Error While Uploading Activity Plan.";
					}
					isSuccess = false;
					logger.error("error");
					logger.error(msg);
					responseModel.setMsg(msg);
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
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
				msg = "Error While Uploading Activity Plan.";
			}
			responseModel.setMsg(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;
	}

	private String checkDateFormat(String record) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		dateFormat.setLenient(false); // Disable lenient parsing
		String val = "not";
		try {
			dateFormat.parse(record);
			System.out.println("The date string is in the Indian format.");
			val = "done";
		} catch (ParseException e) {
			System.out.println("The date string is not in the Indian format.");
			val = "not";
		}

		return val;
	}

	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			BigInteger activityPlanHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO STG TABLE.");
		try {
			List data = activityCommonDao.fetchApprovalData(session, "SA_ACTIVITY_PLAN_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					ActivityPlanApprovalEntity activityPlanApprovalEntity = new ActivityPlanApprovalEntity();
					activityPlanApprovalEntity.setActivityPlanHdrId(activityPlanHdrId);
					activityPlanApprovalEntity.setAppLevelSeq((Integer) row.get("approver_level_seq"));
					activityPlanApprovalEntity.setApprovalStatus((String) row.get("approvalStatus"));
					activityPlanApprovalEntity.setDesignationLevelId((Integer) row.get("designation_level_id"));
					activityPlanApprovalEntity.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					Character isFinalApprovalStatus = (Character) row.get("isFinalApprovalStatus");
					if (isFinalApprovalStatus != null && isFinalApprovalStatus.toString().equals("Y")) {
						activityPlanApprovalEntity.setIsFinalApprovalStatus(true);
					} else {
						activityPlanApprovalEntity.setIsFinalApprovalStatus(false);
					}
					activityPlanApprovalEntity.setRejectedFlag(false);
					activityPlanApprovalEntity.setHoUserId(null);

					session.save(activityPlanApprovalEntity);
				}
			}
			mapData.put("SUCCESS", "Inserted Into Activity Approval Table.");
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	private Map<String, Object> saveActivityPlanToSTG(StgActivityPlanHDREntity stgActivityPlanHDREntity,
			List<ActivitySourceListResponseModel> activitySourceList, List<UserDealerResponseModel> usrBranchList) {
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		List<StgActivityPlanDLRModelEntity> activityPlanDLRModelList = stgActivityPlanHDREntity
				.getActivityPlanDLRModelList();
		List<StgActivityPlanDLRModelDTLEntity> activityPlanDLRModelDTLList = null;
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO STG TABLE.");
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (stgActivityPlanHDREntity != null && activityPlanDLRModelList != null
					&& !activityPlanDLRModelList.isEmpty()) {
				session.save(stgActivityPlanHDREntity);

				for (StgActivityPlanDLRModelEntity activityPlanDLRModelEntity : activityPlanDLRModelList) {
					activityPlanDLRModelDTLList = activityPlanDLRModelEntity.getActivityPlanDLRModelDTLList();
					for (int i = 0; i < activityPlanDLRModelDTLList.size(); i++) {
						StgActivityPlanDLRModelDTLEntity activityPlanDLRModelDTLEntity = activityPlanDLRModelDTLList
								.get(i);
//						ActivitySourceListResponseModel activitySourceListResponseModel = activitySourceList.get(i);

						activityPlanDLRModelEntity.setTargetHotProspect(activityPlanDLRModelEntity.getDeliveryTarget()
								* activityPlanDLRModelEntity.getConvRatio());
//						System.out.println(activityPlanDLRModelEntity.toString());
						session.save(activityPlanDLRModelEntity);

						ActivitySourceListResponseModel activitySourceListResponseModel = activitySourceList.stream()
								.filter(u -> u.getActivityDesc()
										.equals(activityPlanDLRModelDTLEntity.getActivityDesc()))
								.findFirst().orElse(null);
						if (activitySourceListResponseModel != null) {

							activityPlanDLRModelDTLEntity
									.setBudgetValue((activitySourceListResponseModel.getCostPerDay() != null
											? new BigDecimal(activitySourceListResponseModel.getCostPerDay())
											: BigDecimal.ZERO));
							activityPlanDLRModelDTLEntity.setOemShare(activityPlanDLRModelEntity.getOemShare());
							activityPlanDLRModelDTLEntity
									.setActivityId(activitySourceListResponseModel.getActivityId());
//							System.out.println(activityPlanDLRModelDTLEntity.toString());
							session.save(activityPlanDLRModelDTLEntity);
						}
					}
				}
				mapData.put("SUCCESS", "SUCCESSFULLY INSERTED INTO STG TABLE.");
			}
			transaction.commit();
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

	@SuppressWarnings({ "unchecked" })
	private JSONArray fetchActivityPlanList(String stgPlanHdrId, String userCode) {
		Session session = null;
		JSONArray finalJsonArr = null;
//		String sqlQuery = "exec [SP_SACT_GetActivityPlanFromStaging] :?, :?";
		try {
			session = sessionFactory.openSession();
//			Query query = session.createNativeQuery(sqlQuery);
//			query.setParameter("stgPlanHdrId", stgPlanHdrId);
//			query.setParameter("userCode", userCode);
//			query.setResultTransformer(AliasToEntityOrderedMapResultTransformer.INSTANCE);
//			List data = query.getResultList();
//			if(data != null && !data.isEmpty()) {
//				reportModelList = generateReportModelList(data);
//			}
			SessionImpl sessionImpl = (SessionImpl) session;
			Connection conn = sessionImpl.connection();
			String sql = "{CALL SP_SACT_GetActivityPlanFromStaging(?, ?)}";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, stgPlanHdrId);
			statement.setString(2, userCode);
			ResultSet rs = statement.executeQuery();
//			System.out.println(rs.toString());
			int columnCount = rs.getMetaData().getColumnCount();
//			System.out.println(columnCount);
			finalJsonArr = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				JSONArray jarrForDays = new JSONArray();
				JSONArray jarrForTotalBudget = new JSONArray();
				JSONArray jarrForCostToVst = new JSONArray();

				for (int i = 1; i <= columnCount; i++) {
//					System.out.println(i);
//					System.out.println(rs.getMetaData().getColumnName(i));
//					System.out.println(rs.getObject(i));
					String columnName = rs.getMetaData().getColumnName(i);
					if (columnName.contains("_DAYS")) {
						JSONObject joForDays = new JSONObject();
						int lastIndex = columnName.lastIndexOf("_");
						joForDays.put(columnName.substring(0, lastIndex),
								(rs.getObject(i) == null ? null : rs.getObject(i).toString()));
						jarrForDays.add(joForDays);
					} else if (columnName.contains("_BGT")) {
						JSONObject joTotalBudget = new JSONObject();
						int lastIndex = columnName.lastIndexOf("_");
						joTotalBudget.put(columnName.substring(0, lastIndex),
								(rs.getObject(i) == null ? null : rs.getObject(i).toString()));
						jarrForTotalBudget.add(joTotalBudget);
					} else if (columnName.contains("_OEMBGT")) {
						JSONObject joCostToVst = new JSONObject();
						int lastIndex = columnName.lastIndexOf("_");
						joCostToVst.put(columnName.substring(0, lastIndex),
								(rs.getObject(i) == null ? null : rs.getObject(i).toString()));
						jarrForCostToVst.add(joCostToVst);
					} else {
						jo.put(columnName, (rs.getObject(i) == null ? null : rs.getObject(i).toString()));
					}
//					mapData.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
				}
				jo.put("DAYS", jarrForDays);
				jo.put("TOTAL BUDGET", jarrForTotalBudget);
				jo.put("COST TO VST", jarrForCostToVst);
				finalJsonArr.add(jo);
			}
			logger.info(finalJsonArr.toString());
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return finalJsonArr;
	}

	@SuppressWarnings("unchecked")
	public List<ReportModel> generateReportModelList(List<?> data) {
		Integer i = 1;
		String key = "Sr.No.";
		String value = null;
		List<ReportModel> reportModelList = new LinkedList<ReportModel>();
		ReportModel reportModel = new ReportModel(key, "row" + i, String.valueOf(i));
		reportModelList.add(reportModel);
		for (Object object : data) {
			Map<Object, Object> row = (Map<Object, Object>) object;
			if (i != 1) {
				reportModelList.add(new ReportModel(key, "row" + i, String.valueOf(i)));
			}
			for (Map.Entry<Object, Object> entry : row.entrySet()) {
				key = (String) entry.getKey();
				value = "";
				if (entry.getValue() != null) {
					if (entry.getValue() instanceof String) {
						value = (String) entry.getValue();
					} else if (entry.getValue() instanceof Integer) {
						Integer v = (Integer) entry.getValue();
						value = String.valueOf(v);
					} else if (entry.getValue() instanceof Date) {
						Date d = (Date) entry.getValue();
						value = DateToStringParserUtils.parseDateToStringDDMMYYYY(d);
					} else if (entry.getValue() instanceof BigDecimal) {
						BigDecimal b = ((BigDecimal) entry.getValue());
						value = b.toString();
					}
				}
				reportModel = new ReportModel(key, "row" + i, value == null ? "" : value);
				reportModelList.add(reportModel);
			}
			i++;
			key = "Sr.No.";
		}
		return reportModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select HO.ho_usr_id from ADM_HO_USER (nolock) HO INNER JOIN ADM_USER (nolock) u ON u.ho_usr_id = HO.ho_usr_id where u.UserCode =:userCode";
		mapData.put("ERROR", "HO USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("ho_usr_id");
				}
				mapData.put("hoUserId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public ActivityPlanUploadResponseModel submitUploadedPlanActivities(String userCode,
			ActivityPlanFormRequestModel requestModel) {
		Session session = null;
		Transaction transaction = null;
		ActivityPlanUploadResponseModel responseModel = new ActivityPlanUploadResponseModel();
		Map<String, Object> mapData = null;
		ActivityPlanHDREntity activityPlanHDREntity = null;
		List<ActivityPlanDLRModelEntity> activityPlanDLRModelList = null;
		List<ActivityPlanDLRModelDTLEntity> activityPlanDLRModelDTLList = null;
		String activityNumber = null;
		boolean isSuccess = true;
		String sqlQuery = "Select * from STG_SA_ACT_PLAN_HDR where stg_activity_plan_hdr_id =:stgActivityPlanHdrId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				Query query = session.createNativeQuery(sqlQuery).addEntity(StgActivityPlanHDREntity.class);
				query.setParameter("stgActivityPlanHdrId", requestModel.getStgActivityPlanHdrId());
				StgActivityPlanHDREntity stgActivityPlanHDREntity = (StgActivityPlanHDREntity) query.uniqueResult();
				if (stgActivityPlanHDREntity != null) {
					activityPlanHDREntity = mapper.map(stgActivityPlanHDREntity, ActivityPlanHDREntity.class,
							"ActivityPlanHdrMapId");
					// current date
					Date currDate = new Date();
					activityPlanHDREntity.setActivityNo(
							"ACT/" + activityPlanHDREntity.getPcId() + "/" + activityPlanHDREntity.getActivityMonth()
									+ "/" + activityPlanHDREntity.getActivityYear() + "/" + currDate.getTime());
					activityPlanHDREntity.setActivityStatus(WebConstants.PENDING);

					activityPlanHDREntity.setCreatedBy(userId);
					activityPlanHDREntity.setCreatedDate(currDate);
					session.save(activityPlanHDREntity);

					sqlQuery = "Select * from STG_SA_ACT_PLAN_DLR_MODL where stg_activity_plan_hdr_id =:stgActivityPlanHdrId";
					query = session.createNativeQuery(sqlQuery).addEntity(StgActivityPlanDLRModelEntity.class);
					query.setParameter("stgActivityPlanHdrId", requestModel.getStgActivityPlanHdrId());
					List<StgActivityPlanDLRModelEntity> stgActivityPlanDLRModelList = query.list();
					if (stgActivityPlanDLRModelList != null && !stgActivityPlanDLRModelList.isEmpty()) {
						activityPlanDLRModelList = new ArrayList<ActivityPlanDLRModelEntity>();
						for (StgActivityPlanDLRModelEntity stgActivityPlanDLRModel : stgActivityPlanDLRModelList) {
							ActivityPlanDLRModelEntity activityPlanDLRModelEntity = mapper.map(stgActivityPlanDLRModel,
									ActivityPlanDLRModelEntity.class, "ActivityPlanDLrMapId");
							activityPlanDLRModelEntity
									.setActivityPlanHdrId(activityPlanHDREntity.getActivityPlanHdrId());

							sqlQuery = "Select * from STG_SA_ACT_PLAN_DLR_MODL_DTL where stg_activity_plan_dlr_id =:stgActivityPlanDlrId";
							query = session.createNativeQuery(sqlQuery)
									.addEntity(StgActivityPlanDLRModelDTLEntity.class);
							query.setParameter("stgActivityPlanDlrId",
									stgActivityPlanDLRModel.getStgActivityPlanDlrId());
							List<StgActivityPlanDLRModelDTLEntity> stgActivityPlanDLRModelDtlList = query.list();
							if (stgActivityPlanDLRModelDtlList != null && !stgActivityPlanDLRModelDtlList.isEmpty()) {
								activityPlanDLRModelDTLList = new ArrayList<ActivityPlanDLRModelDTLEntity>();
								for (StgActivityPlanDLRModelDTLEntity stgActivityPlanDLRModelDTL : stgActivityPlanDLRModelDtlList) {
									ActivityPlanDLRModelDTLEntity activityPlanDLRModelDTLEntity = mapper.map(
											stgActivityPlanDLRModelDTL, ActivityPlanDLRModelDTLEntity.class,
											"ActivityPlanDLrDtlMapId");
									activityPlanDLRModelDTLEntity.setActivityPlanDLR(activityPlanDLRModelEntity);
									activityPlanDLRModelDTLList.add(activityPlanDLRModelDTLEntity);
								}
								activityPlanDLRModelEntity.setActivityPlanDLRModelDTLList(activityPlanDLRModelDTLList);
								activityPlanDLRModelList.add(activityPlanDLRModelEntity);

							} else {
								isSuccess = false;
							}
						}
						if (activityPlanDLRModelList != null) {
							for (int i = 0; i < activityPlanDLRModelList.size(); i++) {
								ActivityPlanDLRModelEntity dlrModelEntity = activityPlanDLRModelList.get(i);
								session.save(dlrModelEntity);
								if (i % 20 == 0) {
									// flush a batch of inserts and release memory:
									session.flush();
									session.clear();
								}
							}
							activityNumber = activityPlanHDREntity.getActivityNo();
							sqlQuery = "delete t1 from STG_SA_ACT_PLAN_DLR_MODL_DTL t1 "
									+ "JOIN STG_SA_ACT_PLAN_DLR_MODL t2 ON t2.stg_activity_plan_dlr_id = t1.stg_activity_plan_dlr_id "
									+ "JOIN STG_SA_ACT_PLAN_HDR t3 ON t3.stg_activity_plan_hdr_id = t2.stg_activity_plan_hdr_id "
									+ "Where t3.stg_activity_plan_hdr_id =:activityPlanHdrId";
							query = session.createNativeQuery(sqlQuery);
							query.setParameter("activityPlanHdrId", requestModel.getStgActivityPlanHdrId());
							int k = query.executeUpdate();
							if (k > 0) {
								sqlQuery = "delete t2 from STG_SA_ACT_PLAN_DLR_MODL t2 "
										+ "JOIN STG_SA_ACT_PLAN_HDR t3 ON t3.stg_activity_plan_hdr_id = t2.stg_activity_plan_hdr_id "
										+ "Where t3.stg_activity_plan_hdr_id =:activityPlanHdrId";
								query = session.createNativeQuery(sqlQuery);
								query.setParameter("activityPlanHdrId", requestModel.getStgActivityPlanHdrId());
								k = query.executeUpdate();
								if (k > 0) {
									sqlQuery = "delete t3 from STG_SA_ACT_PLAN_HDR t3 "
											+ "Where t3.stg_activity_plan_hdr_id =:activityPlanHdrId";
									query = session.createNativeQuery(sqlQuery);
									query.setParameter("activityPlanHdrId", requestModel.getStgActivityPlanHdrId());
									k = query.executeUpdate();
									if (k > 0) {
									} else {
										isSuccess = false;
									}
								} else {
									isSuccess = false;
								}
							} else {
								isSuccess = false;
							}
						}
					} else {
						isSuccess = false;
					}
				} else {
					isSuccess = false;
				}
			} else {
				isSuccess = false;
			}
			if (isSuccess) {
//				mapData = fetchHOUserDTLByUserCode(session, userCode);
//				if (mapData != null && mapData.get("SUCCESS") != null) {
//					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
				mapData = saveIntoApproval(session, userId, null, activityPlanHDREntity.getActivityPlanHdrId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					transaction.commit();
				} else {
					isSuccess = false;
				}
//				}else {
//					isSuccess = false;
//				}
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
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setPlanActivityNumber(activityNumber);
				responseModel.setMsg("Activity Plan Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				
				try {
					updateActivityMail(userCode, "Create Plan", activityPlanHDREntity.getActivityPlanHdrId()).subscribe(e -> {
						logger.info(e.toString());
					});
				} catch (Exception exp) {
					logger.error(this.getClass().getName(), exp);
				}
				
				
			} else {
				// error
				responseModel.setMsg("Error While Creating Activity Plan.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	private String checkandreturnCellVal(Cell cell) {
		String cellval = null;
		if (cell != null) {
			CellType type = cell.getCellType();
			if (type == CellType.FORMULA) {
				cellval = "wrong";
				System.out.println("wrong "+cellval.toString());
			} else if (type == CellType.STRING) {
				cellval = cell.getStringCellValue().replace(System.getProperty("line.separator"), "")
						.replaceAll("\\r\\n|\\r|\\n", " ");
				System.out.println("string "+cellval.toString());
			} else if (type == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
//			            Date dateValue = (Date) cell;
					cellval = cell.toString();
				} else {
					Double numericVal = cell.getNumericCellValue();
					cellval = numericVal.toString();
					cellval = cellval.substring(0, cellval.length() - 2);
				}

			} else if (type == CellType.BLANK) {
				cellval = "";
				System.out.println("blank "+cellval.toString());
			}
		}
		
		return cellval;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updateActivityMail(String userCode, String eventName, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_SA_ACTIVITY_PLAN] :userCode, :eventName, :refId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("refId", hDRId);
			query.setParameter("isIncludeActive", "N");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					mailItemId = (BigInteger) row.get("mailItemId");
				}
				mapData.put("msg", msg);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY PLAN SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ACTIVITY PLAN SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				//publishModel.setTopic(senderValidatedEnqTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
//						rabbitTemplate.convertAndSend(senderValidatedEnqTopicExchange.getName(), routingKey,
//								commonUtils.objToJson(publishModel).toString());
//						logger.info("Published message for validated enquiry '{}'", publishModel.toString());

					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				});
			}
		}
		return Mono.just(mapData);
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}

	@Override
	public ActivityPlanDateResponse userDesignationLevelDesc(String userCode) {
		
			Session session = null;
			ActivityPlanDateResponse designationRes = null;
			Query query = null;
			String sqlQuery = "exec [SP_GET_DESIGNATION] :usercode";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("usercode", userCode);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data =  query.list();
				System.out.println("size "+data.size());
				if (data != null && !data.isEmpty()) {
					designationRes = new ActivityPlanDateResponse();
					for (Object object : data) {
						Map row = (Map) object;
						designationRes.setHoDesignationLevelId((Integer) row.get("ho_designation_level_id"));
						designationRes.setDesignationLevelDesc((String) row.get("DesignationLevelDesc"));
					}
				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}

			return designationRes;
		}
}
