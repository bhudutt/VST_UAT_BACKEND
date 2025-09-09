/**
 * 
 */
package com.hitech.dms.web.dao.issue.create;

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
import com.hitech.dms.web.entity.ledger.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvLedgerPEntity;
import com.hitech.dms.web.entity.stock.indent.IndentDtlEntity;
import com.hitech.dms.web.entity.stock.indent.IndentHdrEntity;
import com.hitech.dms.web.entity.stock.indent.IndentItemEntity;
import com.hitech.dms.web.entity.stock.indent.issue.IssueDtlEntity;
import com.hitech.dms.web.entity.stock.indent.issue.IssueHdrEntity;
import com.hitech.dms.web.entity.stock.indent.issue.IssueItemEntity;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.issue.create.request.IssueCreateRequestModel;
import com.hitech.dms.web.model.issue.create.request.IssueDtlCreateRequestModel;
import com.hitech.dms.web.model.issue.create.request.IssueItemCreateRequestModel;
import com.hitech.dms.web.model.issue.create.response.IssueCreateResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IssueCreateDaoImpl implements IssueCreateDao {
	public static final Logger logger = LoggerFactory.getLogger(IssueCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public IssueCreateResponseModel createIssue(String authorizationHeader, String userCode,
			IssueCreateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("create createIssue invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		IssueCreateResponseModel responseModel = new IssueCreateResponseModel();
		IssueHdrEntity hdrEntity = null;
		String issueNumber = null;
		boolean isSuccess = true;
		String sqlQuery = null;
		try {
			if (requestModel.getIssueDtlList() == null || requestModel.getIssueDtlList().isEmpty()) {
				isSuccess = false;
				logger.error(this.getClass().getName(), "At-least One Machine Is Required");
				responseModel.setMsg("At-least One Machine Is Required");
			}
			if (isSuccess) {
				List<IssueDtlCreateRequestModel> issueDtlList = requestModel.getIssueDtlList();
				issueDtlList = issueDtlList.stream().filter(i -> i.getIsSelected()).distinct()
						.collect(Collectors.toList());

				if (requestModel.getIssueItemList() != null && !requestModel.getIssueItemList().isEmpty()) {
					List<IssueItemCreateRequestModel> indentItemList = requestModel.getIssueItemList();
					indentItemList = indentItemList.stream().distinct().collect(Collectors.toList());
				}
				if (isSuccess) {
					hdrEntity = mapper.map(requestModel, IssueHdrEntity.class, "IssueCreateMapId");

					Map<BigInteger, Long> machineItemIdCountMap = hdrEntity.getIssueDtlList().stream()
							.collect(Collectors.groupingBy(IssueDtlEntity::getMachineItemId, Collectors.counting()));
					if (machineItemIdCountMap == null || machineItemIdCountMap.isEmpty()) {
						isSuccess = false;
						logger.error(this.getClass().getName(), "At-least One Machine Is Required.");
						responseModel.setMsg("At-least One Machine Is Required.");
					}
					if (isSuccess) {

						if (isSuccess) {
							session = sessionFactory.openSession();
							transaction = session.beginTransaction();
							Date currDate = new Date();
							BigInteger userId = null;
							mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
							if (mapData != null && mapData.get("SUCCESS") != null) {
								userId = (BigInteger) mapData.get("userId");
								// fetch Dealer Detail
								DealerDTLResponseModel dealerDtl = commonDao
										.fetchDealerDTLByDealerId(authorizationHeader, requestModel.getDealerId());
								if (dealerDtl != null) {
									BranchDTLResponseModel branchDTLModel = commonDao
											.fetchBranchDtlByBranchId(authorizationHeader, hdrEntity.getBranchId());
									if (branchDTLModel != null) {
										// fetching Indent Detail to update Issue, Pending Qty
										sqlQuery = "select * from SA_MACHINE_TR_INDENT_HDR (nolock) where indent_id =:indentId";
										query = session.createNativeQuery(sqlQuery).addEntity(IndentHdrEntity.class);
										query.setParameter("indentId", hdrEntity.getIndentId());
										IndentHdrEntity indentHdrEntity = (IndentHdrEntity) query.uniqueResult();
										if (indentHdrEntity != null) {
											List<IndentDtlEntity> indentDtlList = indentHdrEntity.getIndentDtlList();
											List<IndentItemEntity> indentItemList = indentHdrEntity.getIndentItemList();
											for (IndentDtlEntity dtlEntity : indentDtlList) {
												Long vinCount = machineItemIdCountMap.get(dtlEntity.getMachineItemId());
												if (vinCount != null && vinCount.compareTo(0l) > 0) {
													// Integer indentQty = dtlEntity.getIndentQty();
													Integer pendingQty = dtlEntity.getPendingQty();
													Integer issueQty = dtlEntity.getIssueQty();
													if (pendingQty < vinCount.intValue()) {
														logger.error(this.getClass().getName(),
																"Please Select less than equal to Pending Indent Qty. "
																		+ pendingQty);
														responseModel.setMsg(
																"Please Select less than equal to Pending Indent Qty. "
																		+ pendingQty);
														isSuccess = false;
														break;
													} else {
														pendingQty = pendingQty - vinCount.intValue();
														issueQty = issueQty + vinCount.intValue();
														dtlEntity.setPendingQty(pendingQty);
														dtlEntity.setIssueQty(issueQty);

														dtlEntity.setModifiedBy(userId);
														dtlEntity.setModifiedDate(currDate);

														session.merge(dtlEntity);
													}
												}
											}
											if (isSuccess) {
												if (hdrEntity.getIssueItemList() != null
														&& !hdrEntity.getIssueItemList().isEmpty()) {
													boolean isOk = true;
													for (IssueItemEntity itemEntity : hdrEntity.getIssueItemList()) {
														for (IndentItemEntity indentItemEntity : indentItemList) {
															if (itemEntity.getMachineItemId().compareTo(
																	indentItemEntity.getMachineItemId()) == 0) {
																Integer pendingQty = indentItemEntity.getPendingQty();
																Integer issueQty = indentItemEntity.getIssueQty();
																Integer itemIssueQty = itemEntity.getIssueQty();
																if (itemIssueQty == null) {
																	itemIssueQty = 0;
																}
																if (itemIssueQty != null && pendingQty < itemIssueQty) {
																	logger.error(this.getClass().getName(),
																			"Please Select less than equal to Pending Indent Item Qty. "
																					+ pendingQty);
																	responseModel.setMsg(
																			"Please Select less than equal to Pending Indent Item Qty. "
																					+ pendingQty);
																	isSuccess = false;
																	isOk = false;
																	break;
																} else {
																	pendingQty = pendingQty - itemIssueQty;
																	issueQty = issueQty + itemIssueQty;
																	indentItemEntity.setPendingQty(pendingQty);
																	indentItemEntity.setIssueQty(issueQty);

																	indentItemEntity.setModifiedBy(userId);
																	indentItemEntity.setModifiedDate(currDate);

																	session.merge(indentItemEntity);
																}
																break;
															}
														}
														if (!isOk) {
															break;
														}
														itemEntity.setIssueHdr(hdrEntity);
													}
												}
												if (isSuccess) {
													SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
													String strYear = simpleformat.format(currDate);

													mapData = fetchLastIssueDTLByBranchId(session,
															hdrEntity.getBranchId(), strYear);
													String lastIsNumber = null;
													simpleformat = new SimpleDateFormat("yy");
													strYear = simpleformat.format(currDate);
													if (mapData != null && mapData.get("SUCCESS") != null) {
														if (mapData.get("issNumber") != null) {
															lastIsNumber = (String) mapData.get("issNumber");
															int lastIndexOf = lastIsNumber.lastIndexOf(strYear);
															if (lastIndexOf > 0) {
																String prefix = lastIsNumber.substring(0, lastIndexOf);
																issueNumber = lastIsNumber.substring(lastIndexOf + 2,
																		lastIsNumber.length());
																Integer i = Integer.valueOf(issueNumber);
																issueNumber = prefix + strYear
																		+ String.format("%04d", i + 1);
															} else {
																issueNumber = "IS" + branchDTLModel.getBranchCode()
																		+ strYear + "0001";
															}
														} else {
															issueNumber = "IS" + branchDTLModel.getBranchCode()
																	+ strYear + "0001";
														}

														for (IssueDtlEntity dtlEntity : hdrEntity.getIssueDtlList()) {
															sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where vin_id =:vinId and branch_id =:branchId "
																	+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL order by machine_inventory_id desc ";
															query = session.createNativeQuery(sqlQuery)
																	.addEntity(SalesMachineInventoryLedgerEntity.class);
															query.setParameter("vinId", dtlEntity.getVinId());
															query.setParameter("branchId", hdrEntity.getBranchId());
															SalesMachineInventoryLedgerEntity inventoryLedgerEntity = (SalesMachineInventoryLedgerEntity) query
																	.uniqueResult();
															if (inventoryLedgerEntity == null) {
																logger.error(this.getClass().getName(),
																		"While Issuing Machine InventoryLedgerEntity is null/Empty for "
																				+ dtlEntity.getVinId() + " : "
																				+ hdrEntity.getBranchId());
																responseModel.setMsg(
																		"Stock Not Found While Issuing Machine. Kindly Contact Your Admnistrator.");
																isSuccess = false;
																break;
															} else if (inventoryLedgerEntity.isAllotFlag()) {
																logger.error(this.getClass().getName(),
																		"Machine is already issued "
																				+ dtlEntity.getVinId() + " : "
																				+ hdrEntity.getBranchId());
																responseModel.setMsg(
																		"Machine is already issued. Kindly Contact Your Admnistrator.");
																isSuccess = false;
																break;
															} else {
																inventoryLedgerEntity.setOutDocNo(issueNumber);
																inventoryLedgerEntity.setOutDocType(WebConstants.ISSUE);
																inventoryLedgerEntity.setOutDate(currDate);
																inventoryLedgerEntity.setModifiedBy(userId);
																inventoryLedgerEntity.setModifiedDate(currDate);

																session.merge(inventoryLedgerEntity);
															}
															dtlEntity.setIssueHdr(hdrEntity);
														}

														if (isSuccess) {
															for (IssueItemEntity itemEntity : hdrEntity
																	.getIssueItemList()) {
																sqlQuery = "Select * from SA_MACHINE_ITEM_INV (nolock) "
																		+ " where branch_id =:branchId and machine_item_id =:machineItemId";
																query = session.createNativeQuery(sqlQuery)
																		.addEntity(SalesMachineItemInvEntity.class);
																query.setParameter("branchId", hdrEntity.getBranchId());
																query.setParameter("machineItemId",
																		itemEntity.getMachineItemId());
																SalesMachineItemInvEntity itemInvEntity = (SalesMachineItemInvEntity) query
																		.uniqueResult();
																if (itemInvEntity == null) {
																	logger.error(this.getClass().getName(),
																			"Issue implDtlEntity is null/Empty for "
																					+ hdrEntity.getBranchId() + " : "
																					+ itemEntity.getMachineItemId());
																	responseModel.setMsg(
																			"Item Inventory is Empty While Issuing Machine. Kindly Contact Your Administrator.");
																	isSuccess = false;
																	break;
																} else {
																	Integer issueQty = itemEntity.getIssueQty();
																	if (issueQty == null
																			|| issueQty.compareTo(0) == 0) {
																		itemEntity.setIssueQty(0);;
																		break;
																	}
																	Integer stockQty = itemInvEntity.getStockQty();
																	Integer netStockQty = itemInvEntity
																			.getNetStockQty();
																	if (stockQty != null) {
																		if (stockQty.compareTo(issueQty) >= 0) {
																			stockQty = stockQty - issueQty;
																			netStockQty = netStockQty - issueQty;
																			itemInvEntity.setStockQty(stockQty);
																			itemInvEntity.setNetStockQty(netStockQty);
																			itemInvEntity.setModifiedBy(userId);
																			itemInvEntity.setModifiedDate(currDate);
																			session.merge(itemInvEntity);
																		} else {
																			logger.error(this.getClass().getName(),
																					"Qty Must Not Be Greater Than Stock Qty. for "
																							+ hdrEntity.getBranchId()
																							+ " : " + itemEntity
																									.getMachineItemId());
																			responseModel.setMsg(
																					"Qty Must Not Be Greater Than Stock Qty. Kindly Contact Your Administrator.");
																			isSuccess = false;
																			break;
																		}
																	} else {
																		logger.error(this.getClass().getName(),
																				"Stock Qty. Not Found for "
																						+ hdrEntity.getBranchId()
																						+ " : " + itemEntity
																								.getMachineItemId());
																		responseModel.setMsg(
																				"Stock Qty. Not Found While Issuing Machine. Kindly Contact Your Administrator.");
																		isSuccess = false;
																		break;
																	}

																	// Also insert into SA_MACHINE_ITEM_INV_LEDGER Table
																	SalesMachineItemInvLedgerEntity itemInvLedgerEntity = new SalesMachineItemInvLedgerEntity();
																	SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP = new SalesMachineItemInvLedgerPEntity();

																	salesMachineItemInvLedgerP
																			.setBranchId(hdrEntity.getBranchId());
																	salesMachineItemInvLedgerP.setMachineItemId(
																			itemEntity.getMachineItemId());
																	salesMachineItemInvLedgerP
																			.setTransactionNo(issueNumber);
																	salesMachineItemInvLedgerP
																			.setTransactionDate(currDate);

																	itemInvLedgerEntity.setSalesMachineItemInvLedgerP(
																			salesMachineItemInvLedgerP);
																	itemInvLedgerEntity.setInward(0);
																	itemInvLedgerEntity
																			.setOutward(itemEntity.getIssueQty());
																	itemInvLedgerEntity.setTransactionDesc(
																			"THROUGH INTER STOCK TRANSFER");
																	itemInvLedgerEntity.setModifiedBy(userId);
																	itemInvLedgerEntity.setModifiedDate(currDate);

																	session.save(itemInvLedgerEntity);
																}
															}
														}

														if (isSuccess) {
															hdrEntity.setIssueNumber(issueNumber);
															// hdrEntity.setIndentStatus(WebConstants.PENDING);
															hdrEntity.setCreatedBy(userId);
															hdrEntity.setCreatedDate(currDate);
															session.save(hdrEntity);
														}
													} else {
														logger.error(this.getClass().getName(),
																"Error While Validating Last Machine Indent Number.");
														responseModel.setMsg(
																"Error While Validating Last Machine Indent Number. Please Contact Your System Administrator.");
														isSuccess = false;
													}
												}
											}
										} else {
											logger.error(this.getClass().getName(), "Indent Number Not Found.");
											responseModel.setMsg(
													"Indent Number Not Found. Please Contact Your System Administrator.");
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
							responseModel.setMsg("Indent Qty. can not be zero.");
						}
					} else {
						// Indent Qty can not be zero.
						isSuccess = false;
						responseModel.setMsg("Indent Qty. can not be zero.");
					}
					if (isSuccess) {
						transaction.commit();
					}
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
				mapData = fetchIssueNoByIssId(session, hdrEntity.getIssueId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setIssueId(hdrEntity.getIssueId());
					responseModel.setIssueNumber((String) mapData.get("issNumber"));
					mapData = updateIndentStatusByIndentId(session, hdrEntity.getIndentId());
					if (mapData != null && mapData.get("SUCCESS") != null) {
						responseModel.setMsg("Machine Issue Number Created Successfully.");
						responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					} else {
						responseModel.setMsg((String) mapData.get("msg"));
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					}
				} else {
					responseModel.setMsg("Error While Fetching Machine Issue Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Issue.");
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
	public Map<String, Object> updateIndentStatusByIndentId(Session session, BigInteger indentId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "exec [SP_SA_TR_INDENT_STATUS_UPDATE] :indentId";
		mapData.put("msg", "Machine Indent Status Not Updated.");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("indentId", indentId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
				}
				mapData.put("msg", msg);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("msg", "ERROR WHILE UPDATING MACHINE INDENT STATUS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("msg", "ERROR WHILE UPDATING MACHINE INDENT STATUS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastIssueDTLByBranchId(Session session, BigInteger branchId, String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 issue_number "
				+ "	   from SA_MACHINE_TR_INDENT_ISSUE_HDR (nolock) pr where DATEPART(year,issue_date)=:fy "
				+ "	   and pr.branch_id =:branchId order by issue_id desc ";
		mapData.put("ERROR", "Machine Last Issue Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String issNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					issNumber = (String) row.get("issue_number");
				}
				mapData.put("issNumber", issNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchIssueNoByIssId(Session session, BigInteger hdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select issue_number from SA_MACHINE_TR_INDENT_ISSUE_HDR (nolock) pr where pr.issue_id =:hdrId";
		mapData.put("ERROR", "Machine Issue Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hdrId", hdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String issNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					issNumber = (String) row.get("issue_number");
				}
				mapData.put("issNumber", issNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
