/**
 * 
 */
package com.hitech.dms.web.dao.receipt.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.stock.indent.receipt.ReceiptDtlEntity;
import com.hitech.dms.web.entity.stock.indent.receipt.ReceiptHdrEntity;
import com.hitech.dms.web.entity.stock.indent.receipt.ReceiptItemEntity;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.stock.receipt.request.ReceiptCreateRequestModel;
import com.hitech.dms.web.model.stock.receipt.request.ReceiptDtlCreateRequestModel;
import com.hitech.dms.web.model.stock.receipt.request.ReceiptItemCreateRequestModel;
import com.hitech.dms.web.model.stock.receipt.response.ReceiptCreateResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ReceiptCreateDaoImpl implements ReceiptCreateDao {
	public static final Logger logger = LoggerFactory.getLogger(ReceiptCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	public ReceiptCreateResponseModel createReceipt(String authorizationHeader, String userCode,
			ReceiptCreateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("create createReceipt invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		ReceiptCreateResponseModel responseModel = new ReceiptCreateResponseModel();
		ReceiptHdrEntity hdrEntity = null;
		String receiptNumber = null;
		boolean isSuccess = true;
		try {
			if (requestModel.getReceiptDtlList() == null || requestModel.getReceiptDtlList().isEmpty()) {
				isSuccess = false;
				logger.error(this.getClass().getName(), "At-least One Machine Is Required");
				responseModel.setMsg("At-least One Machine Is Required");
			}
			List<ReceiptDtlCreateRequestModel> receiptDtlList = requestModel.getReceiptDtlList();
			receiptDtlList = receiptDtlList.stream().distinct().collect(Collectors.toList());

			if (requestModel.getReceiptItemList() != null && !requestModel.getReceiptItemList().isEmpty()) {
				List<ReceiptItemCreateRequestModel> receiptItemList = requestModel.getReceiptItemList();
				receiptItemList = receiptItemList.stream().distinct().collect(Collectors.toList());
			}
			if (isSuccess) {
				hdrEntity = mapper.map(requestModel, ReceiptHdrEntity.class, "ReceiptCreateMapId");

				for (ReceiptDtlEntity dtlEntity : hdrEntity.getReceiptDtlList()) {
					if (dtlEntity.getIssueDtlId() == null
							|| dtlEntity.getIssueDtlId().compareTo(BigInteger.ZERO) == 0) {
						isSuccess = false;
						break;
					}
					dtlEntity.setReceiptHdr(hdrEntity);
				}
				if (isSuccess) {

					if (hdrEntity.getReceiptItemList() != null && !hdrEntity.getReceiptItemList().isEmpty()) {
						for (ReceiptItemEntity itemEntity : hdrEntity.getReceiptItemList()) {
							if (itemEntity.getIssueItemId() == null
									|| itemEntity.getIssueItemId().compareTo(BigInteger.ZERO) == 0) {
								isSuccess = false;
								break;
							}
							itemEntity.setReceiptHdr(hdrEntity);
						}
					}
					if (isSuccess) {
						session = sessionFactory.openSession();
						transaction = session.beginTransaction();
						BigInteger userId = null;
						mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
						if (mapData != null && mapData.get("SUCCESS") != null) {
							userId = (BigInteger) mapData.get("userId");
							// fetch Dealer Detail
							DealerDTLResponseModel dealerDtl = commonDao.fetchDealerDTLByDealerId(authorizationHeader,
									requestModel.getDealerId());
							if (dealerDtl != null) {
								BranchDTLResponseModel branchDTLModel = commonDao
										.fetchBranchDtlByBranchId(authorizationHeader, hdrEntity.getBranchId());
								if (branchDTLModel != null) {
									Date currDate = new Date();
									SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
									String strYear = simpleformat.format(currDate);

									mapData = fetchLastReceiptDTLByBranchId(session, hdrEntity.getBranchId(), strYear);
									String lastReNumber = null;
									simpleformat = new SimpleDateFormat("yy");
									strYear = simpleformat.format(currDate);
									if (mapData != null && mapData.get("SUCCESS") != null) {
										if (mapData.get("docNumber") != null) {
											lastReNumber = (String) mapData.get("docNumber");
											int lastIndexOf = lastReNumber.lastIndexOf(strYear);
											if (lastIndexOf > 0) {
												String prefix = lastReNumber.substring(0, lastIndexOf);
												receiptNumber = lastReNumber.substring(lastIndexOf + 2,
														lastReNumber.length());
												Integer i = Integer.valueOf(receiptNumber);
												receiptNumber = prefix + strYear + String.format("%04d", i + 1);
											} else {
												receiptNumber = "RE" + branchDTLModel.getBranchCode() + strYear + "0001";
											}
										} else {
											receiptNumber = "RE" + branchDTLModel.getBranchCode() + strYear + "0001";
										}

										hdrEntity.setReceiptNumber(receiptNumber);
										hdrEntity.setCreatedBy(userId);
										hdrEntity.setCreatedDate(currDate);
										session.save(hdrEntity);
									} else {
										logger.error(this.getClass().getName(),
												"Error While Validating Last Machine Receipt Number.");
										responseModel.setMsg(
												"Error While Validating Last Machine Receipt Number. Please Contact Your System Administrator.");
										isSuccess = false;
									}
								} else {
									// Branch Not Found.
									isSuccess = false;
									responseModel.setMsg("Branch Not Found.");
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
					} else {
						// Indent Qty can not be zero.
						isSuccess = false;
						responseModel.setMsg("Receipt Qty. can not be zero.");
					}
				} else {
					// Indent Qty can not be zero.
					isSuccess = false;
					responseModel.setMsg("Receipt Qty. can not be zero.");
				}
				if (isSuccess) {
					transaction.commit();
				}
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
			if (isSuccess) {
				mapData = fetchReceiptNoByInId(session, hdrEntity.getReceiptId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setReceiptId(hdrEntity.getReceiptId());
					responseModel.setReceiptNumber((String) mapData.get("docNumber"));
					responseModel.setMsg("Machine Receipt Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					responseModel.setMsg("Error While Fetching Machine Receipt Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Receipt.");
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
	public Map<String, Object> fetchLastReceiptDTLByBranchId(Session session, BigInteger branchId, String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 receipt_number "
				+ "	   from SA_MACHINE_TR_INDENT_RECEIPT_HDR (nolock) pr where DATEPART(year,receipt_date)=:fy "
				+ "	   and pr.branch_id =:branchId order by receipt_id desc ";
		mapData.put("ERROR", "Machine Last Receipt Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String docNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					docNumber = (String) row.get("indent_number");
				}
				mapData.put("docNumber", docNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE RECEIPT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE RECEIPT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchReceiptNoByInId(Session session, BigInteger hdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select receipt_number from SA_MACHINE_TR_INDENT_RECEIPT_HDR (nolock) pr where pr.receipt_id =:hdrId";
		mapData.put("ERROR", "Machine Receipt Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hdrId", hdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String docNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					docNumber = (String) row.get("receipt_number");
				}
				mapData.put("docNumber", docNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE RECEIPT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE RECEIPT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
