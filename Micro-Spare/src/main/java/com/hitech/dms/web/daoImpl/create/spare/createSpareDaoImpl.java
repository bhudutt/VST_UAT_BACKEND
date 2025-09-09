package com.hitech.dms.web.daoImpl.create.spare;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.common.CommonDaoImpl;
import com.hitech.dms.web.dao.create.spare.createSpareDao;
import com.hitech.dms.web.entity.SparePo.SparePoDetailEntity;
import com.hitech.dms.web.entity.SparePo.SparePoEntity;
import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.SparePoOrderToResponseModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.SpareModel.partSerachDetailsByPohdrIdResponseModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.spare.create.response.SearchPartsByCategoryRequest;
import com.hitech.dms.web.model.spare.create.response.SpareJobCardResponse;
import com.hitech.dms.web.model.spare.create.response.SparePOTcsTotalAmntResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCalculationResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoStatusResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoTypesResponse;
import com.hitech.dms.web.model.spare.create.response.SubProductCategoryResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoCalculationRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoHeaderRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoTcsCalculationRequest;
import com.hitech.dms.web.model.spare.search.response.SparePoHdrDetailsResponse;
import com.hitech.dms.web.model.spare.search.response.partSearchDetailsResponse;
import com.hitech.dms.web.model.spare.search.response.sparePoSearchListResponse;
import com.hitech.dms.web.model.spare.search.resquest.SparePoCancelRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.SparePoUpdateRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.partSerachRequest;

@Repository
public class createSpareDaoImpl implements createSpareDao {
	private static final Logger logger = LoggerFactory.getLogger(createSpareDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDaoImpl commonDaoImpl;
	@Autowired
	private CommonDao commonDao;

	private DateToStringParserUtils utill = new DateToStringParserUtils();;

	@SuppressWarnings({ "deprecation", "deprecation" })
	@Override
	public List<partSearchResponseModel> fetchPartList(String userCode, SearchPartsByCategoryRequest searchRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + searchRequest);
		}
		Session session = null;
		Query query = null;
		List<partSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PART_SEARCH_FOR_PO] :partNumber,:branch_id, :ProductCategoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partNumber", searchRequest.getPartNumber());
			query.setParameter("branch_id", searchRequest.getBranchId());
			query.setParameter("ProductCategoryId", searchRequest.getCategoryId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<partSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					partSearchResponseModel responseModel = new partSearchResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
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

	@Override
	public List<SparePoTypesResponse> getTypes() {
		Session session = null;
		List<SparePoTypesResponse> responseList = null;
		SparePoTypesResponse response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_pa_po_type]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePoTypesResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePoTypesResponse();
					response.setPO_Type_Id((Integer) row.get("PO_Type_Id"));
					response.setPO_Type_Desc((String) row.get("PO_Type_Desc"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@Override
	public List<SparePoStatusResponse> fetchSparePoStatus() {
		Session session = null;
		List<SparePoStatusResponse> responseList = null;
		SparePoStatusResponse response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_pa_po_status]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePoStatusResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePoStatusResponse();
					response.setPO_Status_Id((Integer) row.get("PO_Status_Id"));
					response.setPO_Status_Desc((String) row.get("PO_Status_Desc"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}

	@Override
	public List<SparePoCategoryResponse> fetchSparePoAllCategory() {
		Session session = null;
		List<SparePoCategoryResponse> responseList = null;
		SparePoCategoryResponse response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_pa_po_category]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePoCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePoCategoryResponse();
					response.setPO_Category_Id((Integer) row.get("PO_Category_Id"));
					response.setPO_Category_Desc((String) row.get("PO_Category_Desc"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public SparePoCreateResponseModel createSparePo(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createSparePO invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		SparePoEntity poHdrEntity = null;
		SparePoCreateResponseModel responseModel = new SparePoCreateResponseModel();
		String action = requestModel.getAction();
		boolean isSubmit = false;
		boolean isSuccess = true;
		String poNumber=null;
		try {
			if (action == null || !action.equals(WebConstants.SUBMIT)) {
				isSubmit = false;
			} else if (action != null || action.equals(WebConstants.SUBMIT)) {
				isSubmit = true;
			}
			logger.debug("PO DRAFT MODE : ", isSubmit);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			poHdrEntity = mapper.map(requestModel, SparePoEntity.class, "SparePOMapId");
			System.out.println("requestModel:::::::::::::::::::::" + requestModel);
			System.out.println("poHdrEntity:::::::::::::::::::::" + poHdrEntity);
			System.out.println("isSubmit::::::::::::" + isSubmit);
			List<SparePoDetailEntity> sparePoDtlList = poHdrEntity.getSparePODtlList().stream().distinct()
					.collect(Collectors.toList());
			if (sparePoDtlList != null) {
				System.out.println("sparePoDtlList_data_for_testing" + sparePoDtlList);
				poHdrEntity.setSparePODtlList(sparePoDtlList);
			}

			logger.debug(poHdrEntity.toString());
			BigInteger userId = null;
			BigInteger dlrEmpId = null;
			BigInteger hoUserId = null;
			String branchType = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dlrEmpId = (BigInteger) mapData.get("dlrEmpId");
				hoUserId = (BigInteger) mapData.get("hoUserId");
				// fetch Branch Detail
				BranchDTLResponseModel branchDtl = commonDaoImpl.fetchBranchDtlByBranchId(authorizationHeader,
						poHdrEntity.getBranchId());
				if (branchDtl != null) {
					String branchCode = null;
					branchCode = branchDtl.getBranchCode();
					// current date
					Date currDate = new Date();
					if (action.equals(WebConstants.SAVE)) {
						// poHdrEntity.setDraftMode(true);
						// If PO is SAVE then PO Status will be DRAFT.
						poHdrEntity.setPoStatus(WebConstants.DRAFT);
					} else if (action.equals(WebConstants.CANCEL)) {
						// poHdrEntity.setDraftMode(false);
						poHdrEntity.setPoStatus(WebConstants.CANCELLED);
					} else if (action.equals(WebConstants.SUBMIT)) {
						// If PO is SUBMIT then PO status will be RELEASE. In case of
						poHdrEntity.setPoStatus(WebConstants.RELEASE);

					}
					poHdrEntity.setPOCreationDate(currDate);
					poHdrEntity.setPoReleaseDate(currDate);
					// set Temp PO No.
					// poHdrEntity.setPoNumber("SPO" + branchCode + currDate.getTime());

					 poNumber = commonDao.getDocumentNumberById("SPO", poHdrEntity.getBranchId(), session);
					commonDao.updateDocumentNumber("Spares Purchase Order", poHdrEntity.getBranchId(), poNumber,
							session);

					// poHdrEntity.setPoNumber("SPO" + branchCode + currDate.getTime());
					if(poNumber !=null) {
					poHdrEntity.setPoNumber(poNumber);
					}

					if (isSuccess) {
						int totalQty = 0;
						int totalItems = 0;
						// data inserting into PO Tables
						for (SparePoDetailEntity sparePODtlEntity : poHdrEntity.getSparePODtlList()) {
							totalQty = totalQty + sparePODtlEntity.getQuantity();
							sparePODtlEntity.setSparePOHdr(poHdrEntity);
							poHdrEntity.setTotalQty(totalQty);
							poHdrEntity.setCreatedBy(userCode);
							totalItems++;
							// poHdrEntity.setCreatedDate(currDate);
						}
						poHdrEntity.setTotelItem(totalItems);
						if(poNumber!=null){
							session.save(poHdrEntity);
						} else {
							responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
							responseModel.setStatusCode(500);
							return responseModel;
						}
						
					}
                     if(poNumber!=null){
                    	 session.save(poHdrEntity); 
                     } else {
							responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
							responseModel.setStatusCode(500);
							return responseModel;
						}
					
					// transaction.commit();
					responseModel.setStatusCode(201);
					responseModel.setMsg("sucess");

				} else {
					// Branch Not Found
					isSuccess = false;
					responseModel.setMsg("Branch Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}

			if (isSuccess && (isSubmit) && poNumber != null) {
				transaction.commit();

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
			if (isSuccess && poNumber != null) {
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPoHdrId(poHdrEntity.getPoHdrId());
					responseModel.setPoNumber(poHdrEntity.getPoNumber());
					responseModel.setMsg("SparePO Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else if (poNumber != null) {
				responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
				responseModel.setStatusCode(500);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id, dlr_emp_id, ho_usr_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("userId", (BigInteger) row.get("user_id"));
					mapData.put("dlrEmpId", (BigInteger) row.get("dlr_emp_id"));
					mapData.put("hoUserId", (BigInteger) row.get("ho_usr_id"));
				}
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

	@Override
	public List<SpareJobCardResponse> fetchAllJobCardByBranchId(int branchId) {
		Session session = null;
		List<SpareJobCardResponse> responseList = null;
		SpareJobCardResponse response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_jobcard_search] :SearchId ,:BranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchId", 1);
			query.setParameter("BranchId", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SpareJobCardResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SpareJobCardResponse();
					response.setRo_id((BigInteger) row.get("ro_id"));
					response.setRONumber((String) row.get("RONumber"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}

	@Override
	public List<partSearchDetailsResponse> fetchPartDetailsByPartId(String userCode, int partId, BigInteger branchId,
			int poCategoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDetailsByPartId invoked.." + partId);
		}
		Session session = null;
		Query query = null;
		List<partSearchDetailsResponse> responseModelList =  new ArrayList<partSearchDetailsResponse>();;
		String sqlQuery = "exec [SP_GET_PART_DETAILS_FOR_PO] :userCode,:branchid, :PartId, :Partnumber,:poCategoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("branchid", branchId);
			query.setParameter("PartId", partId);
			query.setParameter("Partnumber", null);
			query.setParameter("poCategoryId", poCategoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			Date date = new Date();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
					partSearchDetailsResponse responseModel = new partSearchDetailsResponse();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel
							.setPartDesc((String) row.get("PartDesc") != null ? (String) row.get("PartDesc") : null);
					responseModel.setPartNumber(
							(String) row.get("partNumber") != null ? (String) row.get("partNumber") : null);
					responseModel.setProductSubCategory(
							(String) row.get("ProductSubCategory") != null ? (String) row.get("ProductSubCategory")
									: null);
					responseModel.setPackQty((Integer) row.get("PackQty") != null ? (Integer) row.get("PackQty") : 0);
					responseModel.setMinOrderQty(
							(Integer) row.get("MinLevelQty") != null ? (Integer) row.get("MinLevelQty") : 0);
					responseModel.setCurrentStock(
							(Integer) row.get("CurrentStock") != null ? (Integer) row.get("CurrentStock") : 0);
					responseModel.setBackOrderQty(
							(Integer) row.get("BackOrderQty") != null ? (Integer) row.get("BackOrderQty") : 0);
					responseModel.setTransitQty(
							(Integer) row.get("InTransitQty") != null ? (Integer) row.get("InTransitQty") : 0);
					responseModel.setBasicUnitPrice(
							(BigDecimal) row.get("BasicUnitPrice") != null ? (BigDecimal) row.get("BasicUnitPrice")
									: null);
//					responseModel.setOrderQty((Integer) row.get("OrderQty"));
					responseModel.setTotalBasePrice(
							(BigDecimal) row.get("TotalBasePrice") != null ? (BigDecimal) row.get("TotalBasePrice")
									: null);
					responseModel.setGSTP((BigDecimal) row.get("GSTP") != null ? (BigDecimal) row.get("GSTP") : null);
//					responseModel.setGSTAmount((Integer) row.get("GSTAmount"));
//					responseModel.setAmount((Integer) row.get("Amount"));
					responseModel.setSONO((String) row.get("SONO") != null ? (String) row.get("SONO") : null);
					responseModel.setSODate((String) row.get("SODate") != null ? (String) row.get("SODate") : null);
					responseModel.setSAPRemarks(
							(String) row.get("SAPRemarks") != null ? (String) row.get("SAPRemarks") : null);
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

//	@Override
//	public SparePoPartUploadResponse uploadSparePoParts(String authorizationHeader, String userCode, BigInteger branch,
//			Integer productCategoryId, MultipartFile file) {
//		logger.debug("userCode uploadSparePoParts : " + userCode);
//		SparePoPartUploadResponse responseModel = new SparePoPartUploadResponse();
//		
//		boolean isSuccess = true;
//		String msg = null;
//		String partNumber = null;
//		int orderQty = 0;
//		Map<String, Integer> data = new HashMap<>();
//		Map<String, String> errorData = new HashMap<>();
//		try {
//			ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("PART NO.", "ORDER QUANTITY"));
//
//			Map<Integer, String> map = new HashMap<Integer, String>();
//			XSSFWorkbook workbook = null;
//			workbook = new XSSFWorkbook(file.getInputStream());
//			logger.debug("workbook - ", workbook);
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			int totalRows = sheet.getPhysicalNumberOfRows();
//			List<Integer> evenList = findEven(totalRows);
//			logger.debug("totalRows - ", totalRows);
//			Row row = sheet.getRow(0);
//			short minColIx = row.getFirstCellNum();
//			short maxColIx = row.getLastCellNum();
//			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
//				Cell cell = row.getCell(colIx);
//				logger.info("cell - ", cell.getStringCellValue());
//				if (staticHeaderList.contains(cell.getStringCellValue().trim())) {
//					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
//				} else {
//					logger.error("INVALID EXCEL FORMAT.");
//					responseModel.setMsg("INVALID EXCEL FORMAT.");
//					responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
//					isSuccess = false;
//					return responseModel;
//				}
//			}
//			Date currDate = new Date();
//			if (isSuccess) {
//				for (int x = 1; x < totalRows; x++) {
//					Row dataRow = sheet.getRow(x);
//					int k = 1;
//					if (!isSuccess) {
//						break;
//					}
//					try {
//						int count = 0;
//						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
//							Cell cell = dataRow.getCell(colIx);
//							if (colIx == 0) {
//								partNumber = checkandreturnCellVal(cell);
//								List<partSearchResponseModel> verify = fetchPartByPartNumber(userCode, partNumber);
//								System.out.println("verify" + verify);
//								if (partNumber == null || partNumber.isEmpty()) {
//									// int tempRowNo = cell.getRowIndex();
//									int rowNo = x + 1;
//									msg = "Row no :" + rowNo + "," + "partNumber is missing kindly update and upload";
//									errorData.put(Integer.toString(rowNo),
//											"partNumber is missing kindly update and upload");
////									logger.error(msg);
////									responseModel.setMsg(msg);
////									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//									// return responseModel;
//									count--;
//								} else if (verify == null || verify.isEmpty()) {
//									// int rowNo = cell.getRowIndex();
//									msg = "Row no :" + x + "," + "partNumber is not matched kindly cehck and upload";
//									errorData.put(Integer.toString(x),
//											partNumber + ":-partNumber is not matched kindly cehck and upload");
//									// logger.error(msg);
//									// responseModel.setMsg(msg);
//									// responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//									// return responseModel;
//									count--;
//								}
//
//							} else if (colIx == 1) {
//								String value = checkandreturnCellVal(cell);
//								System.out.println("OrderQty" + value);
//								if (value == null || value.isEmpty()) {
//									// int rowNo = cell.getRowIndex();
//									msg = "Row no :" + x + "," + "OrderQty is missing kindly update and upload";
//									if (partNumber != null) {
//										errorData.put(Integer.toString(x),
//												"OrderQty is missing kindly update and upload");
//									} else {
//										errorData.put(Integer.toString(x),
//												" PartNumber and OrderQty is missing kindly update and upload");
//
//									}
////									logger.error(msg);
////									responseModel.setMsg(msg);
////									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
////									return responseModel;
//									count--;
//								} else {
//									try {
//										orderQty = Integer.parseInt(value);
//										// System.out.println("Value is numerical: " + value);
//									} catch (NumberFormatException e) {
//										// System.out.println("Value is not numerical. Please enter a valid number.");
//										errorData.put(Integer.toString(x), "OrderQty is must be numerical value !!!");
//									}
//
//								}
//							}
//							count++;
//
//							if (evenList.contains(count)) {
//								data.put(partNumber, orderQty);
//								System.out.println("Count" + count);
//								System.out.println("MapValue" + data);
//								partNumber = " ";
//								orderQty = 0;
//							}
//
//						}
//
//					} catch (Exception ex) {
//						isSuccess = false;
//						if (msg == null) {
//							msg = ex.getMessage();
//						}
//						logger.error(this.getClass().getName(), ex);
//						break;
//					}
//				}
//				if (isSuccess) {
//
//					logger.info(" While Saving SparePart sucessfully.");
////					responseModel.setPartMasterNumber(partNumber);
////					responseModel.setOrderQty(orderQty);
////					JSONObject json = new JSONObject(data);
////					   System.out.println("Json:::::::::::::"+json);
//					responseModel.setPartAndQty(data);
//					responseModel.setErrorPartData(errorData);
//					responseModel.setMsg("SparePart Uploaded Successfully.");
//					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
//				} else {
//					// error
//					if (msg == null) {
//						msg = "Error While Uploading SparePart.";
//					}
//
//					isSuccess = false;
//					responseModel.setMsg(msg);
//					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//				}
//			} else {
//				// error
//				if (msg == null) {
//					msg = "Error While Uploading Spare SparePart.";
//				}
//				isSuccess = false;
//				logger.error("error");
//				logger.error(msg);
//				responseModel.setMsg(msg);
//				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//			}
//		} catch (Exception ex) {
//			isSuccess = false;
//			if (msg == null) {
//				msg = ex.getMessage();
//			}
//			isSuccess = false;
//			logger.error(this.getClass().getName(), ex);
//		}
//		if (!isSuccess) {
//			// error
//			if (msg == null) {
//				msg = "Error While Uploading SparePart.";
//			}
//			responseModel.setMsg(msg);
//			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//		}
//		return responseModel;
//
//	}

	@Override
	public SparePoPartUploadResponse uploadSparePoParts(String authorizationHeader, String userCode, BigInteger branch,
			Integer productCategoryId, MultipartFile file) {
		logger.debug("userCode uploadSparePoParts : " + userCode);
		SparePoPartUploadResponse responseModel = new SparePoPartUploadResponse();

		boolean isSuccess = true;
		String msg = null;
		String partNumber = null;
		int orderQty = 0;
		Map<String, Integer> data = new HashMap<>();
		Map<String, String> errorData = new HashMap<>();
		Set<String> partNumberSet = new HashSet<>();

		try {
			ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("PART NO.", "ORDER QUANTITY"));

			Map<Integer, String> map = new HashMap<>();
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			logger.debug("workbook - ", workbook);
			
			XSSFSheet sheet = workbook.getSheetAt(0);

			int totalRows = sheet.getPhysicalNumberOfRows();

			System.out.println("Total rows with data: " + totalRows);

			logger.debug("totalRows - " + totalRows);

			List<Integer> evenList = findEven(totalRows);

			Row row = sheet.getRow(0);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				Cell cell = row.getCell(colIx);
				logger.info("cell - " + cell.getStringCellValue());
				if (staticHeaderList.contains(cell.getStringCellValue().trim())) {
					map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
				} else {
					logger.error("INVALID EXCEL FORMAT.");
					responseModel.setMsg("INVALID EXCEL FORMAT.");
					responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
					isSuccess = false;
					return responseModel;
				}
			}
			Date currDate = new Date();
			if (isSuccess) {
				for (int x = 1; x < totalRows; x++) {
					Row dataRow = sheet.getRow(x);
					int k = 1;
					if (!isSuccess) {
						break;
					}
					try {
						int count = 0;
						String cellValue = null;
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							Cell cell = dataRow.getCell(colIx);
							if (cell != null) {
								switch (cell.getCellType()) {
								case STRING:
									cellValue = cell.getStringCellValue();
									break;
								case NUMERIC:
									// Format numeric value to avoid scientific notation
									DecimalFormat decimalFormat = new DecimalFormat("#");
									cellValue = decimalFormat.format(cell.getNumericCellValue());
									break;
								case BLANK:
									// Handle blank cells
									cellValue = "";
									break;
								}
							}

							if (colIx == 0) {
								partNumber = cellValue;
								System.out.println("partNumber "+partNumber);
								List<partSearchResponseModel> verify = fetchPartByPartNumber(userCode, partNumber);
								System.out.println("verify" + verify);
								if (partNumber == null || partNumber.isEmpty()) {
									int rowNo = x + 1;
									msg = "Row no :" + rowNo + "," + "partNumber is missing kindly update and upload";
									errorData.put(Integer.toString(rowNo),
											"partNumber is missing kindly update and upload");
									count--;
								} else if (verify == null || verify.isEmpty()) {
									msg = "Row no :" + x + "," + "partNumber is not matched kindly check and upload";
									errorData.put(Integer.toString(x),
											partNumber + ":-partNumber is not matched kindly check and upload");
									count--;
								}
								else if (!partNumberSet.add(partNumber)) {
		                            // Check for duplicate partNumber
									System.out.println("In elseif condititon");
		                            msg = "Row no :" + x + "," + "partNumber is duplicat";
		                            errorData.put(Integer.toString(x),
		                                    partNumber + ":-partNumber is duplicated");
		                            count--;
		                        }

							} else if (colIx == 1) {
								String value = checkandreturnCellVal(cell);
								System.out.println("OrderQty" + value);
								
								if (value == null || value.isEmpty()) {
									msg = "Row no :" + x + "," + "OrderQty is missing kindly update and upload";
									if (partNumber != null) {
										errorData.put(Integer.toString(x),
												"OrderQty is missing kindly update and upload");
									} else {
										errorData.put(Integer.toString(x),
												"PartNumber and OrderQty is missing kindly update and upload");

									}
									count--;
								}else if(Integer.parseInt(value)<=0) {
									
										msg = "Row no :" + x + "," + "OrderQty cann't be Negative or Zero";
										if (partNumber != null) {
											errorData.put(Integer.toString(x),
													"OrderQty cann't be Negative or Zero");
										} else {
											errorData.put(Integer.toString(x),
													"PartNumber is missing and OrderQty cann't be Negative or Zero");
	
										}
								}
								else {
									try {
										orderQty = Integer.parseInt(value);
									} catch (NumberFormatException e) {
										errorData.put(Integer.toString(x), "OrderQty must be a numerical value");
									}
								}
							}
							count++;

							if (evenList.contains(count)) {
								data.put(partNumber, orderQty);
								partNumber = null;
								orderQty = 0;
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
				if (isSuccess) {
					logger.info("SparePart successfully uploaded.");
					responseModel.setPartAndQty(data);
					responseModel.setErrorPartData(errorData);
					responseModel.setMsg("SparePart Uploaded Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					// Error
					if (msg == null) {
						msg = "Error While Uploading SparePart.";
					}
					isSuccess = false;
					responseModel.setMsg(msg);
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				// Error
				if (msg == null) {
					msg = "Error While Uploading Spare SparePart.";
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
			logger.error(this.getClass().getName(), ex);
		}
		if (!isSuccess) {
			// Error
			if (msg == null) {
				msg = "Error While Uploading SparePart.";
			}
			responseModel.setMsg(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;
	}

	/**
	 * @param userCode
	 * @param partNumber
	 * @return
	 */
	private List<partSearchResponseModel> fetchPartByPartNumber(String userCode, String partNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + partNumber);
		}
		Session session = null;
		Query query = null;
		List<partSearchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_GET_SPECIFIC_PART_SEARCH] :partNumber";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partNumber", partNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<partSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					partSearchResponseModel responseModel = new partSearchResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
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

	/**
	 * @param totalRows
	 * @return
	 */
	private List<Integer> findEven(int totalRows) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= totalRows; i++) {
			if (i % 2 == 0) {
				list.add(i);
			}
		}
		return list;
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
		System.out.println("cellval" + cellval);
		return cellval;
	}

	@Override
	public List<partSearchDetailsResponse> fetchPartDetailsByPartNumber(String userCode, BigInteger branchId,
			String partNumber, Integer productCategoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDetailsByPartNumber invoked.." + partNumber);
		}
		Session session = null;
		Query query = null;
		List<partSearchDetailsResponse> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PART_DETAILS_FOR_PO] :userCode,:branchId, :PartId, :Partnumber, :productCategoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", branchId);
			query.setParameter("PartId", null);
			query.setParameter("Partnumber", partNumber);
			query.setParameter("productCategoryId", productCategoryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			Date date = new Date();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<partSearchDetailsResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					partSearchDetailsResponse responseModel = new partSearchDetailsResponse();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel
							.setPartDesc((String) row.get("PartDesc") != null ? (String) row.get("PartDesc") : null);
					responseModel.setPartNumber(
							(String) row.get("partNumber") != null ? (String) row.get("partNumber") : null);
					responseModel.setProductSubCategory(
							(String) row.get("ProductSubCategory") != null ? (String) row.get("ProductSubCategory")
									: null);
					responseModel.setPartcategoryId(
							(Integer) row.get("partcategoryId") != null ? (Integer) row.get("partcategoryId") : 0);
					responseModel
							.setPackQty((Integer) row.get("PackQty") != null ? (Integer) row.get("PackQty") : null);
					responseModel.setMinOrderQty(
							(Integer) row.get("MinLevelQty") != null ? (Integer) row.get("MinLevelQty") : 0);
					responseModel.setCurrentStock(
							(Integer) row.get("CurrentStock") != null ? (Integer) row.get("CurrentStock") : 0);
					responseModel.setBackOrderQty(
							(Integer) row.get("BackOrderQty") != null ? (Integer) row.get("BackOrderQty") : 0);
					responseModel.setTransitQty(
							(Integer) row.get("InTransitQty") != null ? (Integer) row.get("InTransitQty") : 0);
					responseModel.setBasicUnitPrice(
							(BigDecimal) row.get("BasicUnitPrice") != null ? (BigDecimal) row.get("BasicUnitPrice")
									: null);
//					responseModel.setOrderQty((Integer) row.get("OrderQty"));
					responseModel.setTotalBasePrice(
							(BigDecimal) row.get("TotalBasePrice") != null ? (BigDecimal) row.get("TotalBasePrice")
									: null);
					responseModel.setGSTP((BigDecimal) row.get("GSTP") != null ? (BigDecimal) row.get("GSTP") : null);
//					responseModel.setGSTAmount((Integer) row.get("GSTAmount"));
//					responseModel.setAmount((Integer) row.get("Amount"));
					responseModel.setSONO((String) row.get("SONO") != null ? (String) row.get("SONO") : null);
					responseModel.setSODate((String) row.get("SODate") != null ? (String) row.get("SODate") : null);
					responseModel.setSAPRemarks(
							(String) row.get("SAPRemarks") != null ? (String) row.get("SAPRemarks") : null);
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

	@Override
	public List<SparePoCalculationResponse> fetchSparePoItemAmountCal(String userCode,
			SparePoCalculationRequest requestModel) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchSparePoItemAmountCal invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<SparePoCalculationResponse> responseModelList = null;
		String sqlQuery = "exec [SP_PO_ORD_CALCULATEPOItemAmnt] :dealerId, :branchId, :PartId, :qty ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("PartId", requestModel.getPartId());
			query.setParameter("qty", requestModel.getQty());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePoCalculationResponse>();
				SparePoCalculationResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePoCalculationResponse();
					responseModel.setNetAmount((BigDecimal) row.get("NetAmount"));
					responseModel.setItemGstPer((BigDecimal) row.get("ItemGSTPer"));
					responseModel.setItemGstAmount((BigDecimal) row.get("ItemGstAmount"));
					responseModel.setTotalAmount((BigDecimal) row.get("TotalItemAmount"));

					System.out.println("Print model for the testing :::::" + responseModel);
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

	@Override
	public List<SparePOTcsTotalAmntResponse> fetchSparePoTcsAmntCal(String userCode,
			SparePoTcsCalculationRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("calculateMachinePOTotalAmount invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<SparePOTcsTotalAmntResponse> responseModelList = null;
		String sqlQuery = "exec [SP_SA_PO_CALCULATEPOAMNT] :tcsPer, :totalBaseAmount, :totalGstAmount ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("tcsPer", requestModel.getTcsPer());
			query.setParameter("totalBaseAmount", requestModel.getTotalBaseAmount());
			query.setParameter("totalGstAmount", requestModel.getTotalGstAmount());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePOTcsTotalAmntResponse>();
				SparePOTcsTotalAmntResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePOTcsTotalAmntResponse();
					responseModel.setTotalTcsAmount((BigDecimal) row.get("TotalTcsAmount"));
					responseModel.setTotalPoAmnt((BigDecimal) row.get("TotalPOAmount"));

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

	@Override
	public SparePoCreateResponseModel saveSparePoform(String authorizationHeader, String userCode,
			SparePoHeaderRequest requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createSparePO invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		SparePoEntity poHdrEntity = null;
		SparePoCreateResponseModel responseModel = new SparePoCreateResponseModel();
		String action = requestModel.getAction();
		boolean isSave = false;
		boolean isSuccess = true;
		String poNumber = null;
		try {
			if (action == null || !action.equals(WebConstants.SAVE)) {
				isSave = false;
			} else if (action != null || action.equals(WebConstants.SAVE)) {
				isSave = true;
			}
			logger.debug("PO DRAFT MODE : ", isSave);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			poHdrEntity = mapper.map(requestModel, SparePoEntity.class, "SparePOMapId");
			System.out.println("requestModel:::::::::::::::::::::" + requestModel);
			System.out.println("poHdrEntity:::::::::::::::::::::" + poHdrEntity);
			System.out.println("isSave::::::::::::" + isSave);
			List<SparePoDetailEntity> sparePoDtlList = poHdrEntity.getSparePODtlList().stream().distinct()
					.collect(Collectors.toList());
			if (sparePoDtlList != null) {
				System.out.println("sparePoDtlList_data_for_testing" + sparePoDtlList);
				poHdrEntity.setSparePODtlList(sparePoDtlList);
			}

			logger.debug(poHdrEntity.toString());
			BigInteger userId = null;
			BigInteger dlrEmpId = null;
			BigInteger hoUserId = null;
			String branchType = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dlrEmpId = (BigInteger) mapData.get("dlrEmpId");
				hoUserId = (BigInteger) mapData.get("hoUserId");
				// fetch Branch Detail
				BranchDTLResponseModel branchDtl = commonDaoImpl.fetchBranchDtlByBranchId(authorizationHeader,
						poHdrEntity.getBranchId());
				if (branchDtl != null) {
					String branchCode = null;
					branchCode = branchDtl.getBranchCode();
					// current date
					Date currDate = new Date();
					if (action.equals(WebConstants.SAVE)) {
						// poHdrEntity.setDraftMode(true);
						// If PO is SAVE then PO Status will be DRAFT.
						poHdrEntity.setPoStatus(WebConstants.DRAFT);
					}
					poHdrEntity.setPOCreationDate(currDate);
					// set Temp PO No.

					poNumber = commonDao.getDocumentNumberById("SPO", poHdrEntity.getBranchId(), session);
					commonDao.updateDocumentNumber("Spares Purchase Order", poHdrEntity.getBranchId(), poNumber,
							session);
					if (poNumber != null) {
						poHdrEntity.setPoNumber(poNumber);
					}
					// poHdrEntity.setPoNumber("SPO" + branchCode + currDate.getTime());

					if (isSuccess) {
						int totalQty = 0;
						int totalItems = 0;
						// data inserting into PO Tables
						for (SparePoDetailEntity sparePODtlEntity : poHdrEntity.getSparePODtlList()) {
							totalQty = totalQty + sparePODtlEntity.getQuantity();
							sparePODtlEntity.setSparePOHdr(poHdrEntity);
							poHdrEntity.setTotalQty(totalQty);
							poHdrEntity.setCreatedBy(userCode);
							totalItems++;
							// poHdrEntity.setCreatedDate(currDate);
						}
						poHdrEntity.setTotelItem(totalItems);
						if (poNumber != null) {
							session.save(poHdrEntity);
						} else {
							responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
							responseModel.setStatusCode(500);
							return responseModel;
						}
					}
					if (poNumber != null) {
						session.save(poHdrEntity);
					} else {
						responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
						responseModel.setStatusCode(500);
						return responseModel;
					}

					responseModel.setStatusCode(201);
					responseModel.setMsg("sucess");

				} else {
					// Branch Not Found
					isSuccess = false;
					responseModel.setMsg("Branch Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}

			if (isSuccess && isSave && poNumber != null) {
				transaction.commit();
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
			if (isSuccess && poNumber != null) {
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPoHdrId(poHdrEntity.getPoHdrId());
					responseModel.setPoNumber(poHdrEntity.getPoNumber());
					responseModel.setMsg("SparePO Saved Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else if (poNumber != null) {
				responseModel.setMsg("Document number not generated. Please contact administration for assistance.");
				responseModel.setStatusCode(500);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public List<sparePoSearchListResponse> fetchSparePoDataForSerach(String userCode, partSerachRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(" fetchSparePoDataForSerach invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<sparePoSearchListResponse> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PA_PO_SEARCH] :FromDate, :Todate, :PoType, :Postatus, :PoCategory, :poon, :Partycode, :ProductSubCategory, :PoNumber, :partyName, :UserCode,:orgId, :ho, :Zone , :state, :territory,:pooncheckboxflag,:page,:size ";
		try {
			session = sessionFactory.openSession();
			System.out.println("ssssss" + requestModel);
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("FromDate", requestModel.getFromDate());
			query.setParameter("Todate", requestModel.getToDate());
			query.setParameter("PoType", requestModel.getPoType());
			query.setParameter("Postatus", requestModel.getPoStatus());
			query.setParameter("PoCategory", requestModel.getProductCategory());
			query.setParameter("poon", requestModel.getPoOn());
			query.setParameter("Partycode", requestModel.getPartyCode());
			query.setParameter("ProductSubCategory", requestModel.getProductSubCategory());
			query.setParameter("partyName", requestModel.getPartyName());
			query.setParameter("PoNumber", requestModel.getPartNumber());
			query.setParameter("UserCode", userCode);
			query.setParameter("orgId", 1);
			query.setParameter("ho", requestModel.getHo());
			query.setParameter("Zone", requestModel.getZone());
			query.setParameter("state", requestModel.getState());
			query.setParameter("territory", requestModel.getTerritory());
			query.setParameter("pooncheckboxflag", requestModel.getPoOnCheckBoxFlag());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<sparePoSearchListResponse>();
				sparePoSearchListResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new sparePoSearchListResponse();
					responseModel.setPoHdrId((BigInteger) row.get("po_hdr_id"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setPoNumber((String) row.get("PONumber"));
					responseModel.setDealerShipName((String) row.get("DealershipName"));
					// responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setPoOn((String) row.get("PoON"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyCode((String) row.get("Partycode"));
					responseModel.setProductCategory((String) row.get("productCategory"));
					responseModel.setPoType((String) row.get("poType"));
					// responseModel.setBranch_district((String) row.get("Branch_district"));
					responseModel.setPoCreationDate(utill.parseDateToStringDDMMYYYY((Date) row.get("POCreationDate")));
					responseModel.setPoStatus((String) row.get("POStatus"));
					responseModel.setPoReleaseDate(utill.parseDateToStringDDMMYYYY((Date) row.get("POReleaseDate")));
//					responseModel.setBaseAmount((String) row.get("BaseAmount"));
//					responseModel.setTotalGstAmount((BigDecimal) row.get("totalGstAmount"));
//					responseModel.setTcsPercent((String) row.get("tcsPercent"));
//					responseModel.setTotalTcsAmount((BigDecimal) row.get("totalTcsAmount"));
					responseModel.setTotalPoAmount((BigDecimal) row.get("totalPoAmount"));
//					responseModel.setRemarks((String) row.get("Remarks"));
//					responseModel.setZone((String) row.get("ZONE"));
//					responseModel.setArea((String) row.get("Area"));
//					responseModel.setState((String) row.get("state"));
//					responseModel.setTerritory((String) row.get("territory"));
					responseModel.setSoNumber((String) row.get("SoNumber"));
					responseModel.setSoDate((String) row.get("SOdate"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setTotalRecords((Integer) row.get("totalRecords"));
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

	@Override
	public List<SparePoHdrDetailsResponse> fetchPoHdrDetailsById(String userCode, Integer poHdrId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoHdrDetailsById invoked.." + userCode + " " + poHdrId);
		}
		Session session = null;
		Query query = null;
		List<SparePoHdrDetailsResponse> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PO_HDR_DETAILS] :poHdrId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("poHdrId", poHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePoHdrDetailsResponse>();
				SparePoHdrDetailsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePoHdrDetailsResponse();
					responseModel.setPoHdrId((BigInteger) row.get("po_hdr_id"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setPoNumber((String) row.get("PONumber"));
					responseModel.setPoStatus((String) row.get("POStatus"));
					responseModel.setPoON((String) row.get("poON"));
					responseModel.setPOCreationDate((Date) row.get("POCreationDate"));
					responseModel.setPartyName((String) row.get("partyName"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setProductCategory((String) row.get("CategoryCode"));
					responseModel.setPOType((String) row.get("poType"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setJobCardNo((String) row.get("jobcard_no"));
					responseModel.setRso((String) row.get("plantName") + "-" + (String) row.get("plantCode"));
					responseModel.setTotalQty((BigDecimal) row.get("TotalQty"));
					responseModel.setTotelItem((BigDecimal) row.get("TotalItems"));
					responseModel.setPoReleaseDate((Date) row.get("POReleaseDate"));
					responseModel.setTotalBaseAmount((BigDecimal) row.get("totalBaseAmount"));
					responseModel.setTotalGstAmount((BigDecimal) row.get("totalGstAmount"));
					responseModel.setTcsPercent((String) row.get("tcsPercent"));
					responseModel.setTotalPoAmount((BigDecimal) row.get("totalPoAmount"));
					responseModel.setTotalTcsAmount((BigDecimal) row.get("totalTcsAmount"));
					responseModel.setDealerId((BigInteger) row.get("parent_dealer_id"));
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setCategoryId((Integer) row.get("PO_Category_Id"));
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

	@Override
	public List<partSerachDetailsByPohdrIdResponseModel> fetchPoPartDetailsByPohdrId(String userCode, Integer poHdrId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoPartsDetailsBypoHdrId invoked.." + userCode + " " + poHdrId);
		}
		Session session = null;
		Query query = null;
		List<partSerachDetailsByPohdrIdResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PO_SPARE_PARTS_DETAILS] :poHdrId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("poHdrId", poHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<partSerachDetailsByPohdrIdResponseModel>();
				partSerachDetailsByPohdrIdResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new partSerachDetailsByPohdrIdResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPohdrId((BigInteger) row.get("poHdrId"));
					responseModel.setPartNumber((String) row.get("PartNumber"));
					responseModel.setPartDesc((String) row.get("PartDesc"));
					partSearchResponseModel partModel = new partSearchResponseModel();
					partModel.setPartId(responseModel.getPartId());
					partModel.setPartNo(responseModel.getPartNumber());
					responseModel.setPartDtl(partModel);
					responseModel.setProductSubCategory((String) row.get("PARTSUBCATEGORY"));
					responseModel.setPackQty((Integer) row.get("PackQty") != null ? (Integer) row.get("PackQty") : 0);
					responseModel.setMinOrderQty(
							(Integer) row.get("MinOrderQty") != null ? (Integer) row.get("MinOrderQty") : 0);
					responseModel.setCurrentStock(
							(Integer) row.get("CurrentStock") != null ? (Integer) row.get("CurrentStock") : 0);
					responseModel.setBackOrderQty(
							(Integer) row.get("BackOrderQty") != null ? (Integer) row.get("BackOrderQty") : 0);
					responseModel.setTransitQty(
							(Integer) row.get("InTransitQty") != null ? (Integer) row.get("InTransitQty") : 0);
					responseModel.setBasicUnitPrice(
							(BigDecimal) row.get("BasicUnitPrice") != null ? (BigDecimal) row.get("BasicUnitPrice")
									: null);
					responseModel.setOrderQty(
							(BigDecimal) row.get("quantity") != null ? (BigDecimal) row.get("quantity") : null);
					responseModel.setTotalBasePrice(
							(BigDecimal) row.get("net_amount") != null ? (BigDecimal) row.get("net_amount") : null);
					responseModel.setGSTAmount(
							(BigDecimal) row.get("gst_amount") != null ? (BigDecimal) row.get("gst_amount") : null);
					responseModel.setGSTP((BigDecimal) row.get("gst_percent"));
					responseModel.setAmount(
							(BigDecimal) row.get("total_amount") != null ? (BigDecimal) row.get("total_amount") : null);
					responseModel.setSONO((String) row.get("SONO"));
					responseModel.setSODate((String) row.get("SODATE"));
					responseModel.setSAPRemarks((String) row.get("SAPREMARKS"));
					responseModel.setViewImage((String) row.get("VIEWIMAGE"));

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

	@Override
	public List<SparePoDealerAndDistributerSearchResponse> fetchDealerAndDistributorList(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerAndDistributorList invoked.." + userCode + " "
					+ sparePoDealerAndDistributorRequest.toString());
		}
		Session session = null;
		Query query = null;
		List<SparePoDealerAndDistributerSearchResponse> responseModelList = null;
		String sqlQuery = "exec [PA_Get_Dealer_Distributor_Mapping_Search] :ParentDealerId, :DealerCode, :Isfor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ParentDealerId", sparePoDealerAndDistributorRequest.getDealerId());
			query.setParameter("DealerCode", sparePoDealerAndDistributorRequest.getDealerCode());
			query.setParameter("Isfor", sparePoDealerAndDistributorRequest.getIsfor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePoDealerAndDistributerSearchResponse>();
				SparePoDealerAndDistributerSearchResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePoDealerAndDistributerSearchResponse();
					responseModel.setParentDealerId((BigInteger) row.get("parent_dealer_id"));
					responseModel.setParentDealerCode((String) row.get("DealerCode"));
					responseModel.setParentDealerName((String) row.get("ParentDealerName"));
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

	@Override
	public SparePoCreateResponseModel updateSparePO(String authorizationHeader, String userCode,
			SparePoUpdateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateSparePO invked.." + userCode + "requestModel" + requestModel);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		SparePoEntity poHdrEntity = null;
		SparePoCreateResponseModel responseModel = new SparePoCreateResponseModel();
		responseModel.setPoHdrId(requestModel.getPoHdrId());
		responseModel.setPoNumber(requestModel.getPoNumber());
		String action = requestModel.getAction();
		boolean isUpdate = false;
		boolean isSubmit = false;
		boolean isSuccess = false;
		try {
			if (action != null && action.equals("Update")) {
				isUpdate = true;
				isSuccess = true;
			} else if (action != null && action.equals("Submit")) {
				isSubmit = true;
				isSuccess = true;
			}
			logger.debug("PO DRAFT MODE : ", isUpdate);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			poHdrEntity = mapper.map(requestModel, SparePoEntity.class, "SparePOUpdateMapId");
			BigInteger userId = null;
			BigInteger dlrEmpId = null;
			BigInteger hoUserId = null;
			String dealerType = null;
			SparePoOrderToResponseModel poToTypeResponseModel = null;
			SparePoEntity poHdrDBEntity = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dlrEmpId = (BigInteger) mapData.get("dlrEmpId");
				hoUserId = (BigInteger) mapData.get("hoUserId");
				String sqlQuery = "Select * from PA_PO_HDR (nolock) SPH where po_hdr_id =:poHdrId";
				query = session.createNativeQuery(sqlQuery).addEntity(SparePoEntity.class);
				query.setParameter("poHdrId", poHdrEntity.getPoHdrId());
				poHdrDBEntity = (SparePoEntity) query.uniqueResult();
				// System.out.println("poHdrDBEntity"+poHdrDBEntity.getSparePODtlList());
				String sqlDeleteQuery = "Delete from PA_PO_DTL where po_hdr_id =:poHdrId";
				query = session.createNativeQuery(sqlDeleteQuery);
				query.setParameter("poHdrId", poHdrEntity.getPoHdrId());
				query.executeUpdate();

				if (poHdrDBEntity != null) {
					if (!poHdrDBEntity.getPoStatus().equals(WebConstants.DRAFT)) {
						// dealer can not edit the Spare Po
						isSuccess = false;
						responseModel.setMsg("Spare PO Can not be Edited.");
					}
					if (isSuccess) {
						// current date
						Date currDate = new Date();

						if (isSuccess) {

							if (isSuccess) {
								// data inserting/updating into PO Tables
								int totalQty = 0;
								int totalItem = 0;
								poHdrDBEntity.setRsoNumber(requestModel.getRsoNumber());
								poHdrDBEntity.setRemarks(requestModel.getRemarks());
								poHdrDBEntity.setTotalBaseAmount(requestModel.getTotalBaseAmount());
								poHdrDBEntity.setTotalGstAmount(requestModel.getTotalGstAmount());
								poHdrDBEntity.setTotalPoAmount(requestModel.getTotalPoAmount());
								poHdrDBEntity.setTcsPercent(requestModel.getTcsPercent());
								poHdrDBEntity.setTotalTcsAmount(requestModel.getTotalTcsAmount());
								if (isSubmit) {
									poHdrDBEntity.setPoReleaseDate(currDate);
									poHdrDBEntity.setPoStatus(WebConstants.RELEASE);
								}
								poHdrDBEntity.setSparePODtlList(poHdrEntity.getSparePODtlList());
								for (SparePoDetailEntity sparePODtlEntity : poHdrDBEntity.getSparePODtlList()) {
									totalQty = totalQty + sparePODtlEntity.getQuantity();
									sparePODtlEntity.setSparePOHdr(poHdrDBEntity);
									totalItem++;
								}

								if (isSuccess) {
									poHdrDBEntity.setTotalQty(totalQty);
									poHdrDBEntity.setTotelItem(totalItem);
									poHdrDBEntity.setModifiedBy(userId);
									poHdrDBEntity.setModifiedDate(currDate);
									session.merge(poHdrDBEntity);
								}
							}
						}

					} else {
						// Spare PO To Type Not Found
						isSuccess = false;
						responseModel.setMsg("Spare PO To Type Not Found.");
					}

				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Spare PO Number Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}

			if (isSuccess) {
				transaction.commit();
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
				if (isUpdate) {
					responseModel.setMsg("Spare PO Updated Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else if (isSubmit) {
					responseModel.setMsg("Spare PO Update and Submit Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public SparePoCreateResponseModel cancelSparePO(String authorizationHeader, String userCode,
			SparePoCancelRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("cancelSparePO invked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		BigInteger userId = null;
		SparePoCreateResponseModel responseModel = new SparePoCreateResponseModel();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			SparePoEntity poHdrDBEntity = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				String sqlQuery = "Select * from PA_PO_HDR (nolock) SPH where po_hdr_id =:poHdrId";
				query = session.createNativeQuery(sqlQuery).addEntity(SparePoEntity.class);
				query.setParameter("poHdrId", requestModel.getPoHdrId());
				poHdrDBEntity = (SparePoEntity) query.uniqueResult();
				if (poHdrDBEntity != null) {
					if (!poHdrDBEntity.getPoStatus().equals(WebConstants.DRAFT)) {
						// dealer can not edit the Machine Po
						isSuccess = false;
						responseModel.setMsg("Spare PO Can not be Canceled.");
					}
					poHdrDBEntity.setPoCancelResionId(requestModel.getPoCancelReasonId());
					poHdrDBEntity.setPoCancelRemarks(requestModel.getPoCancelRemarks());
					poHdrDBEntity.setPoStatus("Cancelled");
					poHdrDBEntity.setPoCancelDate(new Date());
					poHdrDBEntity.setModifiedBy(userId);
					poHdrDBEntity.setModifiedDate(new Date());

					session.merge(poHdrDBEntity);
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Spare PO Number Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
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
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setPoHdrId(requestModel.getPoHdrId());
				responseModel.setPoNumber(requestModel.getPoNumber());
				responseModel.setMsg("Spare PO Canceled Successfully.");
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Canceling Spare PO.");

				}
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}
		}
		return responseModel;
	}

	@Override
	public List<SubProductCategoryResponse> fetchSubCategoryList(Integer category_id) {
		Session session = null;
		System.out.println("category_id:::::::::::::" + category_id);
		List<SubProductCategoryResponse> responseList = null;
		SubProductCategoryResponse response = null;
		SQLQuery query = null;
		String sqlQuery = "select ID,PO_CATEGORY_ID,PA_SUBCATEGORY_CODE,PA_SUBCATEGORY_DESC from PA_PO_SUBCATEGORY where PO_Category_id = :categoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("categoryId", category_id);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SubProductCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SubProductCategoryResponse();
					response.setId((Integer) row.get("ID"));
					response.setPoCategoryId((Integer) row.get("PO_CATEGORY_ID"));
					response.setPaSubcategoryCode((String) row.get("PA_SUBCATEGORY_CODE"));
					response.setPaSubcategoryDesc((String) row.get("PA_SUBCATEGORY_DESC"));

					responseList.add(response);
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}

	@Override
	public Map<String, Object> getPartyCodeList(String userCode, String searchText) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPartyCodeList invoked.." + userCode + "::::::::::searchText::::::::::" + searchText);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [PA_PO_SEARCH_PARTYCODE] :userCode,:searchText ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", searchText);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("partyCodeList", data);

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
		return responseListModel;
	}

	@Override
	public Map<String, Object> getPartyNameList(String userCode, String searchText) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPartyNameList invoked.." + userCode + "::::::::::searchText::::::::::" + searchText);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [PA_PO_SEARCH_PARTYNAME] :userCode,:searchText ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", searchText);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("PartyNameList", data);

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
		return responseListModel;
	}

}