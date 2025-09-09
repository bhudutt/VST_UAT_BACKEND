/**
 * 
 */
package com.hitech.dms.web.dao.indent.create;

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

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.stock.indent.IndentDtlEntity;
import com.hitech.dms.web.entity.stock.indent.IndentHdrEntity;
import com.hitech.dms.web.entity.stock.indent.IndentItemEntity;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.indent.create.request.IndentCreateRequestModel;
import com.hitech.dms.web.model.indent.create.request.IndentDtlCreateRequestModel;
import com.hitech.dms.web.model.indent.create.request.IndentItemCreateRequestModel;
import com.hitech.dms.web.model.indent.create.response.IndentCreateResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IndentCreateDaoImpl implements IndentCreateDao {
	public static final Logger logger = LoggerFactory.getLogger(IndentCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	public IndentCreateResponseModel createIndent(String authorizationHeader, String userCode,
			IndentCreateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("create createIndent invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		IndentCreateResponseModel responseModel = new IndentCreateResponseModel();
		IndentHdrEntity hdrEntity = null;
		String indentNumber = null;
		boolean isSuccess = true;
		String sqlQuery = null;
		try {
			if (requestModel.getIndentDtlList() == null || requestModel.getIndentDtlList().isEmpty()) {
				isSuccess = false;
				logger.error(this.getClass().getName(), "At-least One Machine Is Required");
				responseModel.setMsg("At-least One Machine Is Required");
			}
			List<IndentDtlCreateRequestModel> indentDtlList = requestModel.getIndentDtlList();
			indentDtlList = indentDtlList.stream().distinct().collect(Collectors.toList());

			if (requestModel.getIndentItemList() != null && !requestModel.getIndentItemList().isEmpty()) {
				List<IndentItemCreateRequestModel> indentItemList = requestModel.getIndentItemList();
				indentItemList = indentItemList.stream().distinct().collect(Collectors.toList());
			}
			if (isSuccess) {
				hdrEntity = mapper.map(requestModel, IndentHdrEntity.class, "IndentCreateMapId");

				for (IndentDtlEntity dtlEntity : hdrEntity.getIndentDtlList()) {
					if (dtlEntity.getIndentQty() == null || dtlEntity.getIndentQty().compareTo(0) == 0) {
						isSuccess = false;
						break;
					}
					dtlEntity.setPendingQty(dtlEntity.getIndentQty());
					dtlEntity.setIssueQty(0);
					dtlEntity.setIndentHdr(hdrEntity);
				}
				if (isSuccess) {

					if (hdrEntity.getIndentItemList() != null && !hdrEntity.getIndentItemList().isEmpty()) {
						for (IndentItemEntity itemEntity : hdrEntity.getIndentItemList()) {
							if (itemEntity.getIndentQty() == null || itemEntity.getIndentQty().compareTo(0) == 0) {
								isSuccess = false;
								break;
							}
							itemEntity.setPendingQty(itemEntity.getIndentQty());
							itemEntity.setIssueQty(0);
							itemEntity.setIndentHdr(hdrEntity);
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

								BranchDTLResponseModel branchDTLModel = commonDao.fetchBranchDtlByBranchId(authorizationHeader,
										hdrEntity.getBranchId());
								if (branchDTLModel != null) {
									Date currDate = new Date();
									SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
									String strYear = simpleformat.format(currDate);

									mapData = fetchLastIndentDTLByBranchId(session, hdrEntity.getBranchId(), strYear);
									String lastInNumber = null;
									simpleformat = new SimpleDateFormat("yy");
									strYear = simpleformat.format(currDate);
									if (mapData != null && mapData.get("SUCCESS") != null) {
										if (mapData.get("inNumber") != null) {
											lastInNumber = (String) mapData.get("inNumber");
											int lastIndexOf = lastInNumber.lastIndexOf(strYear);
											if (lastIndexOf > 0) {
												String prefix = lastInNumber.substring(0, lastIndexOf);
												indentNumber = lastInNumber.substring(lastIndexOf + 2,
														lastInNumber.length());
												Integer i = Integer.valueOf(indentNumber);
												indentNumber = prefix + strYear + String.format("%04d", i + 1);
											} else {
												indentNumber = "IN" + branchDTLModel.getBranchCode() + strYear + "0001";
											}
										} else {
											indentNumber = "IN" + branchDTLModel.getBranchCode() + strYear + "0001";
										}

										hdrEntity.setIndentNumber(indentNumber);
										hdrEntity.setIndentStatus(WebConstants.PENDING);
										hdrEntity.setCreatedBy(userId);
										hdrEntity.setCreatedDate(currDate);
										session.save(hdrEntity);
									} else {
										logger.error(this.getClass().getName(),
												"Error While Validating Last Machine Indent Number.");
										responseModel.setMsg(
												"Error While Validating Last Machine Indent Number. Please Contact Your System Administrator.");
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
				mapData = fetchIndentNoByInId(session, hdrEntity.getIndentId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setIndentId(hdrEntity.getIndentId());
					responseModel.setIndentNumber((String) mapData.get("inNumber"));
					responseModel.setMsg("Machine Indent Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					responseModel.setMsg("Error While Fetching Machine Indent Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Indent.");
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
	public Map<String, Object> fetchLastIndentDTLByBranchId(Session session, BigInteger branchId, String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 indent_number "
				+ "	   from SA_MACHINE_TR_INDENT_HDR (nolock) pr where DATEPART(year,indent_date)=:fy "
				+ "	   and pr.branch_id =:branchId order by indent_id desc ";
		mapData.put("ERROR", "Machine Last Indent Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String inNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					inNumber = (String) row.get("indent_number");
				}
				mapData.put("inNumber", inNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE INDENT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchIndentNoByInId(Session session, BigInteger hdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select indent_number from SA_MACHINE_TR_INDENT_HDR (nolock) pr where pr.indent_id =:hdrId";
		mapData.put("ERROR", "Machine Indent Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hdrId", hdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String inNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					inNumber = (String) row.get("indent_number");
				}
				mapData.put("inNumber", inNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE INDENT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE INDENT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
