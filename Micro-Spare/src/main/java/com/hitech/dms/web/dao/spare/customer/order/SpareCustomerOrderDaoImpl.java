package com.hitech.dms.web.dao.spare.customer.order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.spare.customer.order.SpareCustomerOrderDetailEntity;
import com.hitech.dms.web.entity.spare.customer.order.SpareCustomerOrderEntity;
import com.hitech.dms.web.entity.spare.picklist.PickListDtlEntity;
import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderDownloadTemplate;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartDetailsRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoListRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCalculationRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCancelRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderDetailRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderUpdateRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderCreateResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderCalculationResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderPartUploadResponse;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSelectCustomerOrderRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcUpdateSparePartRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanPartDetailRequest;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

@Repository
public class SpareCustomerOrderDaoImpl implements SpareCustomerOrderDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(SpareCustomerOrderDaoImpl.class);

	@Override
	public SpareCustOrderCreateResponseModel createCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderRequest requestModel, Device device) {

		if (logger.isDebugEnabled()) {
			logger.debug("create customer order invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		SpareCustomerOrderEntity spareCustomerOrderEntity = null;
		SpareCustomerOrderDetailEntity spareCustomerOrderDetailEntity = null;
		SpareCustOrderCreateResponseModel responseModel = new SpareCustOrderCreateResponseModel();
		BigInteger customerId = null;
		boolean isSuccess = true;
		BigInteger detailId = null;
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			Long branchId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			mapData = fetchUserDTLByUserCode(session, userCode);
			spareCustomerOrderEntity = mapper.map(requestModel, SpareCustomerOrderEntity.class);
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = (BigInteger) mapData.get("userId");
				spareCustomerOrderEntity.setCreatedBy(userId);
				spareCustomerOrderEntity.setCreatedDate(new Date());

			}
			spareCustomerOrderEntity.setCustomerOrderDate(new Date());
			session.save(spareCustomerOrderEntity);
			customerId = spareCustomerOrderEntity.getCustomerId();
		//	branchId = spareCustomerOrderEntity.getBranchId();
			List<SpareCustomerOrderDetailRequest> list = requestModel.getSparePartDetails();
			if (!list.isEmpty()) {
				for (SpareCustomerOrderDetailRequest bean : list) {
					spareCustomerOrderDetailEntity = new SpareCustomerOrderDetailEntity();
					spareCustomerOrderDetailEntity.setCustomerId(customerId);
					spareCustomerOrderDetailEntity.setPartId(bean.getPartId());
					spareCustomerOrderDetailEntity.setPartBranchId(bean.getPartBranchId());
					spareCustomerOrderDetailEntity.setOrderQty(bean.getOrderQty());
					spareCustomerOrderDetailEntity.setCreatedBy(userId);
					spareCustomerOrderDetailEntity.setCreatedDate(new Date());
					spareCustomerOrderDetailEntity.setInvoicedQty(bean.getInvoicedQty());
					spareCustomerOrderDetailEntity.setBalanceQty(BigInteger.ZERO);
					spareCustomerOrderDetailEntity.setBasicUnitPrice(bean.getBasicUnitPrice());
					spareCustomerOrderDetailEntity.setSgst(bean.getSgst());
					spareCustomerOrderDetailEntity.setIgst(bean.getIgst());
					spareCustomerOrderDetailEntity.setCgst(bean.getCgst());
					 detailId = (BigInteger) session.save(spareCustomerOrderDetailEntity);
				}

			}
		} catch (SQLGrammarException ex) {
			if (transaction != null && detailId==null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null && detailId!=null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess && detailId!=null) {

				responseModel.setMsg("Customer Order Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setCustomerHdrId(customerId);
				responseModel.setCustomerOrderNumber(requestModel.getCustomerOrderNumber());

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@SuppressWarnings("deprecation")
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

//	@Override
//	public SparePoCreateResponseModel saveCustomerOrderTemplate(String authorizationHeader, String userCode,
//			CustomerOrderDownloadTemplate requestModel, Device device) {
//
//		Session session = sessionFactory.getCurrentSession();
//		Long id = (Long) session.save(requestModel);
//
//		return null;
//	}

	@Override
	public CustOrderProductCtgResponseModel getDocumentNumber(String documentPrefix, BigInteger branchId) {

		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		SpareCustomerOrderEntity spareCustomerOrderEntity = null;
		CustOrderProductCtgResponseModel responseModel = new CustOrderProductCtgResponseModel();
		boolean isSuccess = true;
		try {

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = null;
			String documentNumber = null;
			List<?> documentNoList = null;
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String dateToString = null;
			dateToString = dateFormat1.format(new java.util.Date());
			String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, :DocDate";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DocumentType", documentPrefix);
			query.setParameter("BranchID", branchId);
			query.setParameter("DocDate", dateToString);

			documentNoList = query.list();
			if (null != documentNoList && !documentNoList.isEmpty()) {
				documentNumber = (String) documentNoList.get(0);

				if(documentPrefix.equals("CSO")) {
					updateDocumentNumber(documentNumber, "Customer Order", Integer.parseInt(branchId.toString()), "CSO",session);
				}
				if(documentPrefix.equals("SDC")) {
					updateDocumentNumber(documentNumber, "SPARE DELIVERY CHALLAN", Integer.parseInt(branchId.toString()), "SDC",session);
				}
				if(documentPrefix.equals("CRN")) {
					updateDocumentNumber(documentNumber, "Credit Note", Integer.parseInt(branchId.toString()), "CRN",session);
				}
				if(documentPrefix.equals("DRN")) {
					updateDocumentNumber(documentNumber, "Debite Note", Integer.parseInt(branchId.toString()), "DRN",session);
				}
				if(documentPrefix.equals("PAYV")) {
					updateDocumentNumber(documentNumber, "Payment Voucher", Integer.parseInt(branchId.toString()), "PAYV",session);
				}
				if(documentPrefix.equals("RECV")) {
					updateDocumentNumber(documentNumber, "Receipt Voucher", Integer.parseInt(branchId.toString()), "RECV",session);
				}
				if(documentPrefix.equals("APR-R")) {
					updateDocumentNumber(documentNumber, "APR RETURN", Integer.parseInt(branchId.toString()), "APR-R",session);
				}
			}
			responseModel.setProductCtgNumber(documentNumber);
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
			if (isSuccess) {
				transaction.commit();
				session.close();
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	private void updateDocumentNumber(String indentNumber, String documentTypeDesc, Integer branchId,
			String documentType, Session session) {
		String lastDocumentNumber = indentNumber.substring(indentNumber.length() - 7);
		System.out.println(lastDocumentNumber);
		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC [Update_INV_Doc_No] :lastDocumentNo,"
					+ ":documentTypeDesc, :currentDate, :branchId, :documentType";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("documentTypeDesc", documentTypeDesc);
			query.setParameter("branchId", branchId);
			query.setParameter("documentType", documentType);
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
	}

	@Override
	public List<SparePoCategoryResponse> getAllCustOrderPoCategory() {
		Session session = null;
		List<SparePoCategoryResponse> responseList = null;
		SparePoCategoryResponse response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_pa_po_CATEGORY]";
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
					response.setPO_Category_Desc((String) row.get("PO_Category_Desc"));
					response.setPO_Category_Id((Integer) row.get("PO_Category_Id"));
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
	public SpareCustomerOrderPartUploadResponse uploadSpareCustomerOrderPart(String authorizationHeader,
			String userCode, BigInteger branchId, Integer productCategoryId, Integer dealerId, MultipartFile file) {
		logger.debug("userCode uploadSparePoParts : " + userCode);
		CustomerOrderPartNoRequest beanReq = new CustomerOrderPartNoRequest();
		beanReq.setBranchId(branchId);
		beanReq.setProductCategoryId(productCategoryId);
		SpareCustomerOrderPartUploadResponse responseModel = new SpareCustomerOrderPartUploadResponse();
		boolean isSuccess = true;
		String msg = null;
		String partNumber = null;
		int orderQty = 0;
		Map<String, Integer> data = new HashMap<>();
		Map<String, String> errorData = new HashMap<>();
		try {
			ArrayList<String> staticHeaderList = new ArrayList<>(Arrays.asList("PART NO.", "ORDER QUANTITY"));
			String cellValue=null;
			Map<Integer, String> map = new HashMap<Integer, String>();
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(file.getInputStream());
			logger.debug("workbook - ", workbook);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			List<Integer> evenList = findEven(totalRows);
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
					return responseModel;
				}
			}
			if (isSuccess) {
				for (int x = 1; x < totalRows; x++) {
					Row dataRow = sheet.getRow(x);
					int k = 1;
					if (!isSuccess) {
						break;
					}
					try {
						int count = 0;
						for (short colIx = minColIx; colIx < maxColIx; colIx++) {
							Cell cell = dataRow.getCell(colIx);
							if (colIx == 0) {
								
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
								
								partNumber = cellValue; //checkandreturnCellVal(cell);
								beanReq.setPartNumber(partNumber);
								List<partSearchResponseModel> verify =null;
								boolean verifyFlag = true;
								List<String> resMap = data.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
								
								if(resMap.contains(beanReq.getPartNumber())) {
										int rowNo = x + 1;
										msg = "Row no :" + rowNo + "," + "Duplicate part number not allow kindly check and upload ";
										errorData.put(Integer.toString(rowNo),
												partNumber + ":-Duplicate part number not allow kindly check and upload");
										count--;	
										verifyFlag =false;
									}else {
										
										if(!resMap.isEmpty() && beanReq.getPartNumber()==null) {
											int rowNo = x + 1;
											responseModel.setPartAndQty(data);
											responseModel.setMsg("SparePart Uploaded Successfully.");
											responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
											return responseModel;
										}
										if(beanReq.getPartNumber()==null){
											int rowNo = x + 1;
											msg = "Row no :" + rowNo + "," + "Empty List Not Allow";
											errorData.put(Integer.toString(rowNo),
													"Empty partNuber And Qty not allow kindly update and upload");
											responseModel.setPartAndQty(data);
											responseModel.setErrorPartData(errorData);
											responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
											return responseModel;
										}
										
										verify = fetchPartByPartNumber(userCode, beanReq.getPartNumber());
								}
								
								
								if (partNumber == null || partNumber.isEmpty()) {
									int rowNo = x + 1;
									msg = "Row no :" + rowNo + "," + "partNumber is missing kindly update and upload";
									errorData.put(Integer.toString(rowNo),
											"partNumber is missing kindly update and upload");
									count--;
									responseModel.setPartAndQty(data);
									responseModel.setErrorPartData(errorData);
									responseModel.setMsg(msg);
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								} else if (verifyFlag && (verify == null || verify.isEmpty())) {
									msg = "Row no :" + x + "," + "partNumber is not matched kindly cehck and upload";
									errorData.put(Integer.toString(x),
											partNumber + ":-partNumber is not matched kindly cehck and upload");
									count--;
									responseModel.setPartAndQty(data);
									responseModel.setErrorPartData(errorData);
									responseModel.setMsg(msg);
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}
								

							} else if (colIx == 1) {
								String value = checkandreturnCellVal(cell);
								if(value!=null && Integer.parseInt(value)<=0) {
									
									msg = "Row no :" + x + "," + "OrderQty can not be negative and zero";
									if (partNumber != null) {
										errorData.put(Integer.toString(x),
												"OrderQty can not be negative and zero");
										
									} else {
										errorData.put(Integer.toString(x),
												"OrderQty can not be negative and zero");
										
									}
									count--;
									responseModel.setMsg(msg);
									responseModel.setErrorPartData(errorData);
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								}
								
								else if (value == null || value.isEmpty()) {
									msg = "Row no :" + x + "," + "OrderQty is missing kindly update and upload";
									if (partNumber != null) {
										errorData.put(Integer.toString(x),
												"OrderQty is missing kindly update and upload");
										
									} else {
										errorData.put(Integer.toString(x),
												" PartNumber and OrderQty is missing kindly update and upload");
										

									}
									count--;
									responseModel.setMsg(msg);
									responseModel.setErrorPartData(errorData);
									responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
									return responseModel;
								} else {
									try {
										orderQty = Integer.parseInt(value);
									} catch (NumberFormatException e) {
										errorData.put(Integer.toString(x), "OrderQty is must be numerical value !!!");
										responseModel.setPartAndQty(data);
										responseModel.setErrorPartData(errorData);
										responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
										return responseModel;
									}

								}
							}
							count++;

							if (evenList.contains(count)) {
								data.put(partNumber, orderQty);
								partNumber = " ";
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
					logger.info(" While Saving SparePart sucessfully.");
					responseModel.setPartAndQty(data);
					responseModel.setErrorPartData(errorData);
					responseModel.setMsg("SparePart Uploaded Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					if (msg == null) {
						msg = "Invalid data in excel sheet ";
					}

					isSuccess = false;
					responseModel.setMsg(msg);
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (msg == null) {
					msg = "Invalid data in excel sheet ";
				}
				isSuccess = false;
				logger.error("error");
				logger.error(msg);
				responseModel.setMsg(msg);
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		} catch (Exception ex) {
			if (msg == null) {
				msg = ex.getMessage();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		}
		if (!isSuccess) {
			if (msg == null) {
				msg = "Invalid data in excel sheet ";
			}
			responseModel.setMsg(msg);
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		}
		return responseModel;

	}

	@Override
	@Transactional(readOnly = true)
	public List<SpareCustOrderPartDetailResponse> fetchPartDetailsByPartNumber(String userCode, String partNumber,
			Integer OrderQty, Integer productCtgId, BigInteger branchId, Integer partyTypeId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDetailsByPartNumber invoked.." + partNumber);
		}
		Session session = null;
		Query query = null;
		List<SpareCustOrderPartDetailResponse> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PART_DETAILS_By_PART_ORDER_QTY] :Partnumber, :OrderQty, :BranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Partnumber", partNumber);
			query.setParameter("OrderQty", OrderQty);
			query.setParameter("BranchId", branchId);
//			query.setParameter("partyTypeId", partyTypeId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			Date date = new Date();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SpareCustOrderPartDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					SpareCustOrderPartDetailResponse responseModel = new SpareCustOrderPartDetailResponse();
					String message = (String) row.get("msg");
					if(message.equalsIgnoreCase("OK")) {

					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
					responseModel.setPartDesc((String) row.get("PartDesc"));
					responseModel.setHSNCode((String) row.get("hsn_code"));
					responseModel.setCurrentStock((Integer) row.get("CurrentStock"));
					responseModel.setProductSubCategory((String) row.get("ProductSubCategory"));
					responseModel.setPartBranchId((Integer) row.get("partBranchId"));
					responseModel.setMsg((String) row.get("msg"));
					responseModelList.add(responseModel);
					if(productCtgId!=row.get("PO_CATEGORY_ID")) {
						responseModel.setProductCtgFlag(true);
						responseModel.setMsg((String) row.get("partNumber") +" Part category not matched");
						responseModelList.add(responseModel);
						break;
					}
					}else {
						responseModel.setMsg((String) row.get("msg"));
						responseModelList.add(responseModel);
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
		return responseModelList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SpareCustomerOrderCalculationResponse> fetchSpareCoItemAmountCal(String userCode,
			SpareCustomerOrderCalculationRequest requestModel ,Integer partyBranchId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchSparePoItemAmountCal invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<SpareCustomerOrderCalculationResponse> responseModelList = null;
		String sqlQuery = "exec [SP_ORD_CALCULATEPOItemAmnt] :dealerId, :branchId, :PartId, :qty, :partyBranchId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("PartId", requestModel.getPartId());
			query.setParameter("qty", requestModel.getQty());
			query.setParameter("partyBranchId", partyBranchId!=null? partyBranchId : requestModel.getPartBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SpareCustomerOrderCalculationResponse>();
				SpareCustomerOrderCalculationResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SpareCustomerOrderCalculationResponse();
					
					
					
					responseModel.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
//					BigDecimal dealerMrp = (BigDecimal) row.get("Dealer_MRP");
//					responseModel.setTotalBasePrice(dealerMrp.multiply(requestModel.getOrderQty()));
//					responseModel.setCgst((BigDecimal) row.get("CGST_PER"));
//					responseModel.setCgstAmount((BigDecimal) row.get("CGST_AMT"));
//
//					responseModel.setSgst((BigDecimal) row.get("SGST_PER"));
//					responseModel.setSgstAmount((BigDecimal) row.get("SGST_AMT"));
//
//					responseModel.setIgst((BigDecimal) row.get("IGST_PER"));
//					responseModel.setIgstAmount((BigDecimal) row.get("IGST_AMT"));

					responseModel.setTotalBasePrice(((BigDecimal) row.get("basicUnitPrice")).multiply(BigDecimal.valueOf(requestModel.getQty())));
					
					responseModel.setCgst((BigDecimal) row.get("CGST_PER"));
					responseModel.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setSgst((BigDecimal) row.get("SGST_PER"));
					responseModel.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
				   
					responseModel.setIgst((BigDecimal) row.get("IGST_PER"));
				    responseModel.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
				    
					
//					responseModel.setNetAmount((BigDecimal) row.get("NetAmount"));
//					responseModel.setItemGstPer((BigDecimal) row.get("ItemGSTPer"));
//					responseModel.setItemGstAmount((BigDecimal) row.get("ItemGstAmount"));
//					responseModel.setTotalAmount((BigDecimal) row.get("TotalItemAmount"));

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

	@SuppressWarnings({ "deprecation", "deprecation" })
	@Override
	public List<partSearchResponseModel> fetchPartNumber(String userCode, CustomerOrderPartNoRequest requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartNo invoked.." + requestModel);
		}
		Session session = null;
		Query query = null;
		List<partSearchResponseModel> responseModelList = new ArrayList<partSearchResponseModel>();
		String sqlQuery = "exec [SP_GET_PART_SEARCH_FOR_CO] :SearchText, :branch_id, :ProductCategoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchText", requestModel.getPartNumber());
			query.setParameter("branch_id", requestModel.getBranchId());
			query.setParameter("ProductCategoryId", requestModel.getProductCategoryId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
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

	@SuppressWarnings("deprecation")
	@Override
	public List<SpareCustOrderPartDetailResponse> fetchCOPartDetailsByPartId(String userCode,
			CustomerOrderPartDetailsRequest bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDetailsByPartId invoked.." + bean);
		}
		Session session = null;
		Query query = null;
		List<SpareCustOrderPartDetailResponse> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PART_DETAILS_FOR_CO] :usercode, :Branchid,:PartId, :partyId, :partyCodeId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode",userCode);
			query.setParameter("PartId", bean.getPartId());
			query.setParameter("partyId", bean.getPartyId());
			query.setParameter("Branchid", bean.getBranchId());
			query.setParameter("partyCodeId", bean.getPartyCodeId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			Date date = new Date();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SpareCustOrderPartDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					SpareCustOrderPartDetailResponse responseModel = new SpareCustOrderPartDetailResponse();
//					responseModel.setPartId((Integer) row.get("part_id"));
//					responseModel.setPartNo((String) row.get("partNumber") != null ? (String) row.get("partNumber") : null);
//					responseModel.setPartDesc((String) row.get("PartDesc") != null ? (String) row.get("PartDesc") : null);
//					responseModel.setProductSubCategory((String) row.get("ProductSubCategory") != null ? (String) row.get("ProductSubCategory"): null);

				  if(row.get("msg") != null) {
				    	 responseModel.setMsg((String)row.get("msg"));
				  }else {
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNo((String) row.get("partNumber"));
					responseModel.setPartDesc((String) row.get("PartDesc"));
					responseModel.setHSNCode((String) row.get("HSN_CODE"));
					responseModel.setCurrentStock((Integer) row.get("CurrentStock"));
					responseModel.setProductSubCategory((String) row.get("ProductSubCategory"));
				    responseModel.setBasicUnitPrice((BigDecimal) row.get("BasicUnitPrice"));

					responseModel.setHSNCode((String)row.get("HSN_CODE"));
					responseModel.setCgst((BigDecimal) row.get("CGST_PER"));
					responseModel.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("BasicUnitPrice"))).multiply(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setSgst((BigDecimal) row.get("SGST_PER"));
					responseModel.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("BasicUnitPrice"))).multiply(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
				   
					responseModel.setIgst((BigDecimal) row.get("IGST_PER"));
				    responseModel.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("BasicUnitPrice"))).multiply(BigDecimal.valueOf(1)).setScale(2, BigDecimal.ROUND_HALF_UP));
				    
				    
				    responseModel.setTotalGst((BigDecimal)row.get("TotalGST"));
				    responseModel.setPartBranchId((Integer)row.get("partBranch_id"));
				  }  
				  

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
	public List<SpareCustOrderPartDetailResponse> customerOrderPartDetail(String userCode,
			CustomerOrderPartNoRequest requestModel) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPartDetailsByPartId invoked.." + requestModel);
		}
		Session session = null;
		Query query = null;
		List<SpareCustOrderPartDetailResponse> responseModelList = null;
		String sqlQuery = "exec [SP_ORD_CALCULATEPOItemAmnt] :dealerId, :branchId, :PartId, :qty, :partyBranchId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealarId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("PartId", requestModel.getPartId());
			query.setParameter("qty", requestModel.getOrderQty());
			query.setParameter("partyBranchId", requestModel.getPartyBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			Date date = new Date();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SpareCustOrderPartDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					
					SpareCustOrderPartDetailResponse responseModel = new SpareCustOrderPartDetailResponse();
//					responseModel.setBasicUnitPrice((BigDecimal) row.get("Dealer_MRP"));
//					responseModel.setCgst((BigDecimal) row.get("CGST_PER"));
//					responseModel.setCgstAmount((BigDecimal) row.get("CGST_AMT"));
//					responseModel.setSgst((BigDecimal) row.get("SGST_PER"));
//					responseModel.setSgstAmount((BigDecimal) row.get("SGST_AMT"));
//					responseModel.setIgst((BigDecimal) row.get("IGST_PER"));
//					responseModel.setIgstAmount((BigDecimal) row.get("IGST_AMT"));
					responseModel.setTotalBasePrice((BigDecimal) row.get("NetAmount"));
					
					
					
					responseModel.setCgst((BigDecimal) row.get("CGST_PER"));
					responseModel.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) requestModel.getOrderQty())).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setSgst((BigDecimal) row.get("SGST_PER"));
					responseModel.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) requestModel.getOrderQty())).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setIgst((BigDecimal) row.get("IGST_PER"));
					
					responseModel.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) requestModel.getOrderQty())).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setTotalGst((BigDecimal) row.get("NetAmount"));
					responseModel.setMrp(((BigDecimal) row.get("basicUnitPrice")).multiply(BigDecimal.valueOf((Integer) requestModel.getOrderQty())));
					responseModel.setTotalBasePrice(((BigDecimal) row.get("basicUnitPrice")).multiply(BigDecimal.valueOf((Integer) requestModel.getOrderQty())));

					
					responseModel.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));

					
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
	public List<SparePoCategoryResponse> getAllSubCategory() {
	
		Query query = null;
		Session session = null;
		String sqlQuery = "Select PO_CATEGORY_ID,PA_SUBCATEGORY_CODE from PA_PO_SUBCATEGORY(nolock) where ISACTIVE = 'Y'";
	
		List<SparePoCategoryResponse> responseList = null;
		SparePoCategoryResponse response = null;
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePoCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePoCategoryResponse();
					response.setPO_Category_Desc((String) row.get("PA_SUBCATEGORY_CODE"));
					response.setPO_Category_Id((Integer) row.get("PO_CATEGORY_ID"));
					responseList.add(response);
				}
			}
		} catch (SQLGrammarException ex) {
		
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
		
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@Override
	public SpareCustOrderCreateResponseModel updateCustomerOrder(String authorizationHeader, String userCode,
			@Valid List<SpareCustomerOrderUpdateRequest> requestModel, Device device) {
		

		if (logger.isDebugEnabled()) {
			logger.debug("update customer order invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		SpareCustomerOrderDetailEntity spareCustomerOrderDetailEntity = null;
		SpareCustOrderCreateResponseModel responseModel = new SpareCustOrderCreateResponseModel();
		BigInteger customerId = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			for(SpareCustomerOrderUpdateRequest list: requestModel) {
				
			SpareCustomerOrderDetailEntity entity = (SpareCustomerOrderDetailEntity) session.get(SpareCustomerOrderDetailEntity.class, list.getId());
				
				if (mapData != null && mapData.get("SUCCESS") != null){
					userId = (BigInteger) mapData.get("userId");
				}
				
				spareCustomerOrderDetailEntity = new SpareCustomerOrderDetailEntity();
				spareCustomerOrderDetailEntity.setId(list.getCustomerId());
				spareCustomerOrderDetailEntity.setCustomerId(entity.getCustomerId());
				spareCustomerOrderDetailEntity.setPartId(list.getPartId());
				spareCustomerOrderDetailEntity.setOrderQty(list.getOrderQty());
				spareCustomerOrderDetailEntity.setModifiedBy(userId);
				spareCustomerOrderDetailEntity.setModifiedDate(new Date());
				spareCustomerOrderDetailEntity.setInvoicedQty(entity.getInvoicedQty());
				session.saveOrUpdate(spareCustomerOrderDetailEntity);
					
			}
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {

				responseModel.setMsg("Customer Order Detail updated Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setCustomerHdrId(customerId);
			

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}
	
	/**
	 * @param userCode
	 * @param partNumber
	 * @return
	 */
	@Transactional(readOnly = true)
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

	@Override
	public SpareCustOrderCreateResponseModel cancelCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderCancelRequest requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("cancel customer order invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		SpareCustOrderCreateResponseModel responseModel = new SpareCustOrderCreateResponseModel();
		BigInteger customerId = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			
				
			SpareCustomerOrderEntity entity = (SpareCustomerOrderEntity) session.get(SpareCustomerOrderEntity.class, requestModel.getCustomerId());
				
				if (mapData != null && mapData.get("SUCCESS") != null){
					userId = (BigInteger) mapData.get("userId");
				}
				entity.setCustomerOrderStatus(requestModel.getCustomerOrderStatus());
				entity.setModifiedBy(userId);
				entity.setModifiedDate(new Date());
				entity.setRemark(requestModel.getRemark());
				
				session.update(entity);
				transaction.commit();
			
			
		} catch (SQLGrammarException ex) {
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

				responseModel.setMsg("Customer Order Detail cancel Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setCustomerHdrId(customerId);
			

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public void updateDcFlagToCO(List<DcSelectCustomerOrderRequest> dcSelectCO, String dcNumber, String userCode) {
		
		
		Session session = null;
		Transaction transaction = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			SpareCustomerOrderEntity entity = null;
			
			if (mapData != null && mapData.get("SUCCESS") != null){
				userId = (BigInteger) mapData.get("userId");
			}
			for( DcSelectCustomerOrderRequest list:dcSelectCO) {
				
				entity = (SpareCustomerOrderEntity) session.get(SpareCustomerOrderEntity.class, list.getCustomerId());
				entity.setCustomerId(list.getCustomerId());
				entity.setDcSelect(list.isDcSelect());
				if(list.isDcSelect()) {
					entity.setDcNumber(dcNumber);	
				}
				entity.setModifiedBy(userId);
				entity.setModifiedDate(new Date());
				session.update(entity);
				
			}
		
	} catch (SQLGrammarException ex) {
		if (transaction != null) {
			transaction.rollback();
		}
		isSuccess = false;
		logger.error(this.getClass().getName(), ex);

	} finally {
		if (session != null) {
			transaction.commit();
			session.close();
		}
		

	}

	}

	@Override
	public SpareCustOrderCreateResponseModel editSpareCustomerOrder(String authorizationHeader, String userCode,
			@Valid SpareCustomerOrderRequest requestModel, Device device) {
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("create customer order invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		SpareCustomerOrderEntity spareCustomerOrderEntity = new SpareCustomerOrderEntity();
		SpareCustomerOrderDetailEntity spareCustomerOrderDetailEntity = null;
		SpareCustOrderCreateResponseModel responseModel = new SpareCustOrderCreateResponseModel();
		BigInteger customerId = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			mapData = fetchUserDTLByUserCode(session, userCode);
			spareCustomerOrderEntity = session.get(SpareCustomerOrderEntity.class, requestModel.getCustomerId());
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = (BigInteger) mapData.get("userId");
				spareCustomerOrderEntity.setModifiedBy(userId);
				spareCustomerOrderEntity.setModifiedDate(new Date());

			}
			spareCustomerOrderEntity.setCustomerOrderDate(new Date());
			spareCustomerOrderEntity.setCustomerOrderStatus(requestModel.getCustomerOrderStatus()); 
			spareCustomerOrderEntity.setTotalPart(requestModel.getTotalPart());
			spareCustomerOrderEntity.setTotalQuantity(requestModel.getTotalQuantity());
			session.update(spareCustomerOrderEntity);
			customerId = spareCustomerOrderEntity.getCustomerId();
			List<SpareCustomerOrderDetailRequest> list = requestModel.getSparePartDetails();
			if (!list.isEmpty()) {
				for (SpareCustomerOrderDetailRequest bean : list) {
					spareCustomerOrderDetailEntity = new SpareCustomerOrderDetailEntity();
					spareCustomerOrderDetailEntity.setCustomerId(customerId);
					spareCustomerOrderDetailEntity.setId(bean.getId());
					spareCustomerOrderDetailEntity.setPartId(bean.getPartId());
					spareCustomerOrderDetailEntity.setOrderQty(bean.getOrderQty());
					spareCustomerOrderDetailEntity.setPartBranchId(bean.getPartBranchId());
					spareCustomerOrderDetailEntity.setCreatedBy(userId);
					spareCustomerOrderDetailEntity.setCreatedDate(new Date());
					spareCustomerOrderDetailEntity.setInvoicedQty(bean.getInvoicedQty());
					spareCustomerOrderDetailEntity.setBalanceQty(BigInteger.ZERO);
					spareCustomerOrderDetailEntity.setBasicUnitPrice(bean.getBasicUnitPrice());
					spareCustomerOrderDetailEntity.setCgst(bean.getCgst());	
					spareCustomerOrderDetailEntity.setIgst(bean.getIgst());	
					spareCustomerOrderDetailEntity.setSgst(bean.getSgst());	
					
					if(spareCustomerOrderDetailEntity.getId()!=null) {
						spareCustomerOrderDetailEntity.setModifiedBy(userId);
						spareCustomerOrderDetailEntity.setModifiedDate(new Date());
						session.update(spareCustomerOrderDetailEntity);
					//	session.evict(spareCustomerOrderDetailEntity);
					}else {
						session.save(spareCustomerOrderDetailEntity);
					}
					
				}

			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {

				responseModel.setMsg("Customer Order Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setCustomerHdrId(customerId);
				responseModel.setCustomerOrderNumber(spareCustomerOrderEntity.getCustomerOrderNumber());

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public void coQtyAndStatusUpdate(List<DeliveryChallanPartDetailRequest> dcPartDetailMap, String userCode) {
		Session session = null;
		Map<String, Object> mapData = null;
		Transaction transaction = null;
	
		BigInteger userId = null;
		boolean isSuccess = true;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
		try {
		mapData = fetchUserDTLByUserCode(session, userCode);
		if (mapData != null && mapData.get("SUCCESS") != null) {

			userId = (BigInteger) mapData.get("userId");
		}
		List<DeliveryChallanPartDetailRequest> dcPartDetailList = new ArrayList<>(dcPartDetailMap);
//		Long balanceQty = dcPartDetailList.stream().collect(Collectors.summarizingLong(s->s.getBalanceQty().longValue())).getSum();
		Long issueQty =  dcPartDetailList.stream().collect(Collectors.summarizingLong(s->s.getDcQty().longValue())).getSum();
		
		for(DeliveryChallanPartDetailRequest list:dcPartDetailList) {
			
			SpareCustomerOrderDetailEntity entity = (SpareCustomerOrderDetailEntity) session.get(SpareCustomerOrderDetailEntity.class, list.getCustomerId());
			updateCOstatus(entity.getCustomerId(),list.getTotalSumOfIssue()+issueQty.intValue(), userId);
			BigInteger issue = entity.getDcIssueQty().add(list.getDcQty());
			entity.setDcIssueQty(issue);
			entity.setBalanceQty(list.getBalanceQty());
			entity.setModifiedBy(userId);
			entity.setModifiedDate(new Date());
			session.update(entity);
			
		}
		}catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
		
		}
		
	}

	private void updateCOstatus(BigInteger customerId, Integer totalIssueQty, BigInteger userId) {
		
		Session session = null;
		Transaction transaction = null;
		boolean isSuccess = true;
		try {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		SpareCustomerOrderEntity entity = (SpareCustomerOrderEntity) session.get(SpareCustomerOrderEntity.class, customerId);
		Integer totalQty = (Integer) session.createSQLQuery("select sum(Order_Qty) from PA_Customer_Order_DTL where Customer_Id="+customerId).uniqueResult();
		
		if(totalIssueQty==totalQty) {
			entity.setCustomerOrderStatus("Fully-DC");	
		}else {
			entity.setCustomerOrderStatus("Partially-DC");	
		}
		entity.setModifiedBy(userId);
		entity.setModifiedDate(new Date());
		session.update(entity);
		}catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
		
		}
	}

	@Override
	@Transactional
	public Integer deletePartNofromDTL(String authorizationHeader, String userCode,
			CustomerOrderPartNoListRequest requestModel, Device device) {
	
		NativeQuery<?> query = sessionFactory.getCurrentSession().createNativeQuery("delete from PA_Customer_Order_DTL where Customer_Id = :customerId and Part_Id in(:partId)");
		query.setReadOnly(true);
		query.setParameter("customerId", requestModel.getCustomerId());
		query.setParameterList("partId", requestModel.getPartIdList());
		return query.executeUpdate();
		
	}

	@Override
	public void coBalanceAndStatusUpdate(List<DcUpdateSparePartRequest> sparePartDetails, String userCode) {
			Session session = null;
			Map<String, Object> mapData = null;
			Transaction transaction = null;
		
			BigInteger userId = null;
			boolean isSuccess = true;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			try {
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = (BigInteger) mapData.get("userId");
			}
			
//			Long balanceQty = dcPartDetailList.stream().collect(Collectors.summarizingLong(s->s.getBalanceQty().longValue())).getSum();
//			Long issueQty =  sparePartDetails.stream().collect(Collectors.summarizingLong(s->s.getDcQty().longValue())).getSum();
			
			for(DcUpdateSparePartRequest list:sparePartDetails) {
				
				
				if(list.getPickListDtlId()!=null) {
					PickListDtlEntity entity = (PickListDtlEntity) session.get(PickListDtlEntity.class, BigInteger.valueOf(list.getPickListDtlId().intValue()));
					if(entity!=null) {
						entity.setDcIssueQty(entity.getDcIssueQty()-(list.getIssuedQty()));
					//	entity.setBalanceQty(entity.getBalanceQty().add(BigDecimal.valueOf(entity.getDcIssueQty()-(list.getIssuedQty()))));
						entity.setModifiedBy(userCode);
						entity.setModifiedDate(new Date());
						session.update(entity);
					}
					
				}
				
				
				SpareCustomerOrderDetailEntity entity = (SpareCustomerOrderDetailEntity) session.get(SpareCustomerOrderDetailEntity.class, BigInteger.valueOf(list.getCustomerDtlId().intValue()));
				SpareCustomerOrderEntity hdrEntity = (SpareCustomerOrderEntity) session.get(SpareCustomerOrderEntity.class, BigInteger.valueOf(entity.getCustomerId().intValue()));
				Integer totalQty = (Integer) session.createSQLQuery("select sum(Order_Qty) from PA_Customer_Order_DTL where Customer_Id="+entity.getCustomerId()).uniqueResult();
				BigInteger issue = (entity.getDcIssueQty()).subtract(BigInteger.valueOf(list.getIssuedQty().intValue()));
				BigInteger balanceQty =   entity.getBalanceQty().add(issue);
				BigInteger coQty = BigInteger.ZERO;
				   if(entity.getDcIssueQty()!=null && entity.getDcIssueQty().intValue()>0) {
					   coQty = issue;
				   }
				
				if(coQty.intValue()==totalQty) {
					hdrEntity.setCustomerOrderStatus("Fully-DC");
				}else if(coQty.intValue()==0) {
					hdrEntity.setCustomerOrderStatus("Submit");	
				}else {
					hdrEntity.setCustomerOrderStatus("Partially-DC");	
					
				}
				hdrEntity.setModifiedBy(userId);
				hdrEntity.setModifiedDate(new Date());
				session.update(hdrEntity);
				
				entity.setDcIssueQty(issue);
				entity.setBalanceQty(balanceQty);
				entity.setModifiedBy(userId);
				entity.setModifiedDate(new Date());
				session.update(entity);
				
			}
			}catch (SQLGrammarException ex) {
				if (transaction != null) {
					transaction.rollback();
				}
				isSuccess = false;
				logger.error(this.getClass().getName(), ex);

			} finally {
				if (session != null) {
					transaction.commit();
					session.close();
				}
			
			}
			
		}
	
}
