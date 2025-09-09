package com.hitech.dms.web.dao.spare.branchTransfer.issue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentDtlEntity;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.entity.branchTransfer.issue.IssueDtlEntity;
import com.hitech.dms.web.entity.branchTransfer.issue.IssueHdrEntity;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.IssueDetailsResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;

@Repository
public class BranchSpareTransferIssueDaoImpl implements BranchSpareTransferIssueDao {
	private static final Logger logger = LoggerFactory.getLogger(BranchSpareTransferIssueDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Override
	public HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = " exec [PA_Search_Indent_Number] :searchText , :userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("id"), (String) row.get("IndentNumber"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

	@Override
	public List<IndentNumberDetails> fetchIndentNumberDetails(BigInteger paIndHdrId) {
		Session session = null;
		List<IndentNumberDetails> indentNumberDetailsList = null;
		IndentNumberDetails indentNumberDetails = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_indent_issue_Dtl] :paIndHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("paIndHdrId", paIndHdrId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				indentNumberDetailsList = new ArrayList<IndentNumberDetails>();
				for (Object object : data) {
					Map row = (Map) object;
					indentNumberDetails = new IndentNumberDetails();

					indentNumberDetails.setPaIndDtlId((BigInteger) row.get("id"));
					indentNumberDetails.setPartId((Integer) row.get("part_id"));
					indentNumberDetails.setPartNumber((String) row.get("PartNumber"));
					indentNumberDetails.setPartdesc((String) row.get("PartDesc"));
					indentNumberDetails.setBasicUnitPrice((BigDecimal) row.get("BasicUnitPrice"));
					indentNumberDetails.setIndentQty((Integer) row.get("IndentQty"));
					indentNumberDetails.setTotalStock((Integer) row.get("TotalStock"));
					indentNumberDetails.setTotalValue((BigDecimal) row.get("TotalValue"));
					indentNumberDetailsList.add(indentNumberDetails);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return indentNumberDetailsList;

	}

	@Override
	public BranchSpareTransferResponse createBranchSpareTransferIssue(
			BranchSpareTransferIssueRequest branchSpareTransferIssueRequest, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(branchSpareTransferIssueRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		BranchSpareTransferResponse branchSpareTransferResponse = new BranchSpareTransferResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger paIssueid=null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				IssueHdrEntity issueHdrEntity = new IssueHdrEntity();

				String generatedNumber = generateIssueNumber(
						Integer.parseInt(branchSpareTransferIssueRequest.getBranchId().toString()))
						.getGeneratedNumber();

				for (IndentNumberDetails indentNumberDetails : branchSpareTransferIssueRequest
						.getIndentNumberDetails()) {
					updateStockForIssue(session, indentNumberDetails, branchSpareTransferIssueRequest.getBranchId(),
							userCode);
					updateIssueAndIndentQty(session, indentNumberDetails, userId);
				}
				issueHdrEntity.setIssueNumber(generatedNumber);
				issueHdrEntity.setIssueDate(branchSpareTransferIssueRequest.getIssueDate());
				issueHdrEntity.setPaIndentHdrId(branchSpareTransferIssueRequest.getPaIndHdrId());
				issueHdrEntity.setRemarks(branchSpareTransferIssueRequest.getRemarks());
				issueHdrEntity.setIssueBy(branchSpareTransferIssueRequest.getIssueBy());
				issueHdrEntity.setCreatedBy(id);
				issueHdrEntity.setCreatedDate(todayDate);
				paIssueid = (BigInteger) session.save(issueHdrEntity);
				updateDocumentNumber(generatedNumber, "Branch Stock Transfer Issue",
						Integer.parseInt(branchSpareTransferIssueRequest.getBranchId().toString()), "BTI", session);
				
				if(branchSpareTransferIssueRequest.getIndentNumberDetails()!=null && !branchSpareTransferIssueRequest.getIndentNumberDetails().isEmpty()) {
					
					for(IndentNumberDetails bean : branchSpareTransferIssueRequest.getIndentNumberDetails()) {
						IssueDtlEntity  dtlBean  =  null;
						
						if(bean.getBinRequest()!=null && !bean.getBinRequest().isEmpty()) {
							for(BranchSpareIssueBinStockResponse binBean: bean.getBinRequest()) {
								dtlBean  =  new IssueDtlEntity();
							dtlBean.setPaIndIssueId(paIssueid);
							dtlBean.setPartId(bean.getPartId());
							dtlBean.setPartBranchId(bean.getPartBranchId());
							dtlBean.setIndentQty(bean.getIndentQty());
							dtlBean.setIssueQty(binBean.getIssueQty().intValue());
							dtlBean.setBasicUnitPrice(bean.getBasicUnitPrice());
							dtlBean.setTotalStock(bean.getToStore());
							dtlBean.setTotalValue(binBean.getIssueQty().multiply(bean.getBasicUnitPrice()));
							dtlBean.setBranchStoreId(binBean.getBranchStoreId());
							dtlBean.setStoreBinId(binBean.getBinId());
							dtlBean.setCreatedDate(new Date());
							dtlBean.setCreatedBy(userId);
							session.save(dtlBean);
							}
						}else {
							dtlBean  =  new IssueDtlEntity();
							dtlBean.setPaIndIssueId(paIssueid);
							dtlBean.setPartId(bean.getPartId());
							dtlBean.setPartBranchId(bean.getPartBranchId());
							dtlBean.setIndentQty(bean.getIndentQty());
							dtlBean.setIssueQty(bean.getIssueQty());
							dtlBean.setBasicUnitPrice(bean.getBasicUnitPrice());
							dtlBean.setTotalStock(bean.getToStore());
							dtlBean.setTotalValue(BigDecimal.valueOf(bean.getIssueQty()).multiply(bean.getBasicUnitPrice()));
							dtlBean.setBranchStoreId(bean.getBranchStoreId());
							dtlBean.setStoreBinId(bean.getBinId());
							dtlBean.setCreatedDate(new Date());
							dtlBean.setCreatedBy(userId);
							session.save(dtlBean);
						}
					}
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				branchSpareTransferResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Branch Transfer Issue Created Successfully";
				branchSpareTransferResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				branchSpareTransferResponse.setStatusCode(statusCode);
				branchSpareTransferResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			branchSpareTransferResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			branchSpareTransferResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			branchSpareTransferResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return branchSpareTransferResponse;
	}

	private void updateStockForIssue(Session session, IndentNumberDetails indentNumberDetails, BigInteger branchId,
			String userCode) {

		updateStockInPartBranchAndStockStore(session, indentNumberDetails, branchId,
				indentNumberDetails.getBranchStoreId(), userCode);
		if(indentNumberDetails.getBinRequest()!=null && !indentNumberDetails.getBinRequest().isEmpty()) {
		for (BranchSpareIssueBinStockResponse binRequest : indentNumberDetails.getBinRequest()) {
			updateStockInStockBin(session, binRequest, branchId, indentNumberDetails.getBranchStoreId(), userCode);
		}
		}else {
			updateStockInStockBinSingle(session, indentNumberDetails.getPartBranchId(),indentNumberDetails.getBinId(), indentNumberDetails.getBasicUnitPrice(), indentNumberDetails.getIssueQty(), branchId, indentNumberDetails.getBranchStoreId(), userCode);
		}
		
	}

	private void updateStockInPartBranchAndStockStore(Session session, IndentNumberDetails indentNumberDetails,
			BigInteger branchId, Integer branchStoreId, String userCode) {
		if (indentNumberDetails.getIssueQty() != null
				&& BigInteger.valueOf(indentNumberDetails.getPartBranchId()).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Part_Branch :flag, :branchId, :partBranchId, :partId, :branchStoreId, :qtyToBeAdded, :qtyToBeSubtracted, :basicUnitPrice, :modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", indentNumberDetails.getPartBranchId());
			query.setParameter("partId", 0);
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("qtyToBeAdded", 0);
			query.setParameter("qtyToBeSubtracted", indentNumberDetails.getIssueQty());
			query.setParameter("basicUnitPrice", indentNumberDetails.getBasicUnitPrice());
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}

	private void updateStockInStockBin(Session session, BranchSpareIssueBinStockResponse binRequest,
			BigInteger branchId, Integer branchStoreId, String userCode) {

		if (binRequest != null && BigInteger.valueOf(binRequest.getPartBranchId()).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Stock_Bin :flag, :branchId, :partBranchId, :branchStoreId, :stockBinId, :binName, :qtyToBeAdded, :qtyToBeSubtracted, :basicUnitPrice, :tableName, :modifiedBy";
			
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", binRequest.getPartBranchId());
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("stockBinId", binRequest.getBinId());
			query.setParameter("binName", null);
			query.setParameter("qtyToBeAdded", binRequest.getIssueQty().intValue());
			query.setParameter("qtyToBeSubtracted", 0);
			query.setParameter("basicUnitPrice", binRequest.getUnitPrice());
			query.setParameter("tableName", "PA_INDENT_ISSUE_HDR");
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}
	
	private void updateStockInStockBinSingle(Session session, Integer partBranchId, BigInteger binId, BigDecimal basicUnitPrice, Integer issueQty,
			BigInteger branchId, Integer branchStoreId, String userCode) {

		if (BigInteger.valueOf(partBranchId).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Stock_Bin :flag, :branchId, :partBranchId, :branchStoreId, :stockBinId, :binName, :qtyToBeAdded, :qtyToBeSubtracted, :basicUnitPrice, :tableName, :modifiedBy";
			
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", partBranchId);
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("stockBinId", binId);
			query.setParameter("binName", null);
			query.setParameter("qtyToBeAdded", null);
			query.setParameter("qtyToBeSubtracted", issueQty);
			query.setParameter("basicUnitPrice", basicUnitPrice);
			query.setParameter("tableName", "PA_INDENT_ISSUE_HDR");
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}

	private void updateIssueAndIndentQty(Session session, IndentNumberDetails indentNumberDetails, BigInteger userId) {
		if (indentNumberDetails != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "update PA_Indent_dtl set IssueQTY = " + indentNumberDetails.getIssueQty()
					+ ", IndentQTY = " + indentNumberDetails.getIndentQty() + ", modifiedBy = " + userId
					+ ", ModifiedDate = " + currentDate + "where id = " + indentNumberDetails.getPaIndDtlId();

			Query query = session.createSQLQuery(updateQuery);
			query.executeUpdate();
		}
	}

	public GeneratedNumberModel generateIssueNumber(Integer branchId) {
		Session session = null;
		GeneratedNumberModel generatedNumberModel = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());

		Query query = null;
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			String docType = "BTI";
			query.setParameter("DocumentType", docType);
			query.setParameter("BranchID", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					generatedNumberModel = new GeneratedNumberModel();
					generatedNumberModel.setGeneratedNumber((String) row.get("Doc_number"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return generatedNumberModel;
	}

	private void updateDocumentNumber(String issueNumber, String documentTypeDesc, Integer branchId,
			String documentType, Session session) {
		String lastDocumentNumber = issueNumber.substring(issueNumber.length() - 7);
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

	private IndentHdrEntity getIndentHdr(BranchSpareTransferIssueRequest branchSpareTransferIssueRequest,
			Session session, BigInteger userId) {
		String sqlQuery = "select * from PA_INDENT_HDR where id = :paIndHdrId";

		Query query;
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("paIndHdrId", branchSpareTransferIssueRequest.getPaIndHdrId());
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List data = query.list();
		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;

				IndentHdrEntity indentHdrEntity = new IndentHdrEntity();
				indentHdrEntity.setBranchId(userId);
//				Gson gson = new Gson();
//				JsonElement jsonElement = gson.toJsonTree(row);
//				IssueHdrEntity indentHdrEntity = gson.fromJson(jsonElement, IssueHdrEntity.class);
				return indentHdrEntity;
			}
		}
		return null;
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

	@Override
	public HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "select id, IssueNumber from [PA_INDENT_ISSUE_HDR] " + "where IssueNumber like '%"
				+ searchText + "%'";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
//			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("id"), (String) row.get("IssueNumber"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

	@Override
	public List<IssueDetailsResponse> fetchIssueDetails(String issueNumber, BigInteger paIssueId, Date fromDate,
			Date toDate) {
		Session session = null;
		List<IssueDetailsResponse> issueDetailsResponseList = null;
		IssueDetailsResponse issueDetailsResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String dateToString = null;

		String fromDateToString = null;
		String toDateToString = null;
		if (fromDate != null) {
			fromDateToString = dateFormat1.format(fromDate);
		}

		if (toDate != null) {
			toDateToString = dateFormat1.format(toDate);
		}

		if (issueNumber == null) {
			issueNumber = null;
		}

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_issue] " + issueNumber + ", " + paIssueId + ",";
		if (fromDate != null) {
			sqlQuery = sqlQuery + "'" + fromDateToString + "',";
		} else {
			sqlQuery = sqlQuery + null + ",";
		}

		if (toDate != null) {
			sqlQuery = sqlQuery + "'" + toDateToString + "'";

		} else {
			sqlQuery = sqlQuery + null;
		}

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				issueDetailsResponseList = new ArrayList<IssueDetailsResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					issueDetailsResponse = new IssueDetailsResponse();

					issueDetailsResponse.setId((BigInteger) row.get("id"));
	//				issueDetailsResponse.setPaIndHdrId((BigInteger) row.get("paIndHdrId"));
					issueDetailsResponse.setIssueNumber((String) row.get("IssueNumber"));
					issueDetailsResponse.setIssueDate((Date) row.get("IssueDate"));
					issueDetailsResponse.setIssueRemarks((String) row.get("Remarks"));
					issueDetailsResponse.setIssueBy((String) row.get("IssueBy"));
					issueDetailsResponse.setIndentNumber((String) row.get("IndentNumber"));
					issueDetailsResponse.setIndentDate((Date) row.get("IndentDate"));
					issueDetailsResponse.setIndentOnBranch((String) row.get("IndentOnBranch"));
//					issueDetailsResponse.setIndentBy((String) row.get("IndentBy"));
					issueDetailsResponse.setIndentRemarks((String) row.get("IndentRemarks"));
					issueDetailsResponseList.add(issueDetailsResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return issueDetailsResponseList;
	}

	@Override
	public BranchSpareTransferIssueResponse fetchIssueTransferHdrAndDtl(BigInteger paIssueHdrId) {
		BranchSpareTransferIssueResponse branchSpareTransferIssueResponse = new BranchSpareTransferIssueResponse();
		BranchSpareTransferIssueHdrResponse branchSpareTransferIssueHdrResponse = fetchIssueHdrDetailsForReceipt(null,
				paIssueHdrId, null, null).get(0);
		List<IndentNumberDetails> indentNumberDetailsList = fetchIssueTransferDtl(paIssueHdrId);
		Integer totalIssuedQty = 0;
		BigDecimal totalIssuedValue = null;
		Integer totalIssuedPart = indentNumberDetailsList.size();
		for (IndentNumberDetails indentNumberDetails : indentNumberDetailsList) {
			totalIssuedQty = totalIssuedQty + indentNumberDetails.getIssueQty();
			totalIssuedValue = BigDecimal.valueOf(totalIssuedQty).multiply(indentNumberDetails.getBasicUnitPrice());
		}
		branchSpareTransferIssueHdrResponse.setTotalIssuedQty(totalIssuedQty);
		branchSpareTransferIssueHdrResponse.setTotalIssuedPart(totalIssuedPart);
		branchSpareTransferIssueHdrResponse.setTotalIssuedValue(totalIssuedValue);
		branchSpareTransferIssueResponse.setBranchSpareTransferIssueHdrResponse(branchSpareTransferIssueHdrResponse);
		branchSpareTransferIssueResponse.setIndentNumberDetails(indentNumberDetailsList);

		return branchSpareTransferIssueResponse;

	}

	public List<IndentNumberDetails> fetchIssueTransferDtl(BigInteger paIndHdrId) {
		Session session = null;
		List<IndentNumberDetails> indentNumberDetailsList = null;
		IndentNumberDetails indentNumberDetails = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_issue_view] :paIndHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("paIndHdrId", paIndHdrId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				indentNumberDetailsList = new ArrayList<IndentNumberDetails>();
				for (Object object : data) {
					Map row = (Map) object;
					indentNumberDetails = new IndentNumberDetails();
					indentNumberDetails.setPaIndDtlId((BigInteger) row.get("id"));
					indentNumberDetails.setPartId((Integer) row.get("part_id"));
					indentNumberDetails.setPartNumber((String) row.get("PartNumber"));
					indentNumberDetails.setPartdesc((String) row.get("PartDesc"));
					indentNumberDetails.setBasicUnitPrice((BigDecimal) row.get("BasicUnitPrice"));
					indentNumberDetails.setIndentQty((Integer) row.get("IndentQty"));
					indentNumberDetails.setIssueQty((Integer) row.get("IssueQty"));
					indentNumberDetails.setTotalStock((Integer) row.get("TotalStock"));
					indentNumberDetails.setTotalValue((BigDecimal) row.get("TotalValue"));
					indentNumberDetails.setFromStore((String) row.get("FromStore"));
					indentNumberDetails.setStoreBinLocation((String) row.get("StoreBinLocation"));
				//	indentNumberDetails.setBinStock((String) row.get("BinStock"));

					indentNumberDetailsList.add(indentNumberDetails);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return indentNumberDetailsList;
	}

	public List<BranchSpareTransferIssueHdrResponse> fetchIssueHdrDetailsForReceipt(String issueNumber,
			BigInteger paIssueId, Date fromDate, Date toDate) {
		Session session = null;
		List<BranchSpareTransferIssueHdrResponse> branchSpareTransferIssueHdrResponseList = null;
		BranchSpareTransferIssueHdrResponse branchSpareTransferIssueHdrResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String dateToString = null;

		String fromDateToString = null;
		String toDateToString = null;
		if (fromDate != null) {
			fromDateToString = dateFormat1.format(fromDate);
		}

		if (toDate != null) {
			toDateToString = dateFormat1.format(toDate);
		}

		if (issueNumber == null) {
			issueNumber = null;
		}

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_issue] " + issueNumber + ", " + paIssueId + ",";
		if (fromDate != null) {
			sqlQuery = sqlQuery + "'" + fromDateToString + "',";
		} else {
			sqlQuery = sqlQuery + null + ",";
		}

		if (toDate != null) {
			sqlQuery = sqlQuery + "'" + toDateToString + "'";

		} else {
			sqlQuery = sqlQuery + null;
		}

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				branchSpareTransferIssueHdrResponseList = new ArrayList<BranchSpareTransferIssueHdrResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					branchSpareTransferIssueHdrResponse = new BranchSpareTransferIssueHdrResponse();

					branchSpareTransferIssueHdrResponse.setId((BigInteger) row.get("id"));
					branchSpareTransferIssueHdrResponse.setPaIndHdrId((BigInteger) row.get("paIndHdrId"));
					branchSpareTransferIssueHdrResponse.setIssueNumber((String) row.get("IssueNumber"));
					branchSpareTransferIssueHdrResponse.setIssueDate((Date) row.get("IssueDate"));
					branchSpareTransferIssueHdrResponse.setIssueRemarks((String) row.get("Remarks"));
					branchSpareTransferIssueHdrResponse.setIssueBy((String) row.get("IssueBy"));
					branchSpareTransferIssueHdrResponse.setIndentNumber((String) row.get("IndentNumber"));
					branchSpareTransferIssueHdrResponse.setIndentDate((Date) row.get("IndentDate"));
					branchSpareTransferIssueHdrResponse.setIndentOnBranch((String) row.get("IndentOnBranch"));
					branchSpareTransferIssueHdrResponse.setIndentBy((String) row.get("IndentBy"));
					branchSpareTransferIssueHdrResponse.setIndentOnBranchId((Integer) row.get("IndentOnBranchId"));
					branchSpareTransferIssueHdrResponse.setIndentById((Integer) row.get("IndentById"));
					branchSpareTransferIssueHdrResponse.setIndentRemarks((String) row.get("IndentRemarks"));
					branchSpareTransferIssueHdrResponseList.add(branchSpareTransferIssueHdrResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return branchSpareTransferIssueHdrResponseList;
	}

	@Override
	public BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode,
			String dealerCode, String page, String size) {
		BranchSpareTransferResponse branchSpareTransferResponse = new BranchSpareTransferResponse();
		branchSpareTransferResponse.setBranchSpareTransferIndentHdrResponse(
				fetchIndentDetails(null, paIndHdrId, null, null, 0, 10).get(0));
		branchSpareTransferResponse.setIndentNumberDetails(fetchIndentTransferDtl(paIndHdrId, userCode, dealerCode));

		return branchSpareTransferResponse;
	}

	public List<BranchSpareTransferIndentHdrResponse> fetchIndentDetails(String indentNumber, BigInteger paIndHdrId,
			Date fromDate, Date toDate, Integer page, Integer size) {
		Session session = null;
		List<BranchSpareTransferIndentHdrResponse> branchSpareTransferIndentResponseList = null;
		BranchSpareTransferIndentHdrResponse branchSpareTransferIndentResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String dateToString = null;

		String fromDateToString = null;
		String toDateToString = null;
		if (fromDate != null) {
			fromDateToString = dateFormat1.format(fromDate);
		}

		if (toDate != null) {
			toDateToString = dateFormat1.format(toDate);
		}

		if (indentNumber == null) {
			indentNumber = null;
		}

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_indent] " + indentNumber + ", " + paIndHdrId + ",";
		if (fromDate != null) {
			sqlQuery = sqlQuery + "'" + fromDateToString + "',";
		} else {
			sqlQuery = sqlQuery + null + ",";
		}

		if (toDate != null) {
			sqlQuery = sqlQuery + "'" + toDateToString + "',";

		} else {
			sqlQuery = sqlQuery + null + ",";
		}

		if (page != null) {
			sqlQuery = sqlQuery + page + ",";

		} else {
			sqlQuery = sqlQuery + null + ",";
		}

		if (size != null) {
			sqlQuery = sqlQuery + size;

		} else {
			sqlQuery = sqlQuery + null;
		}

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				branchSpareTransferIndentResponseList = new ArrayList<BranchSpareTransferIndentHdrResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					branchSpareTransferIndentResponse = new BranchSpareTransferIndentHdrResponse();

					branchSpareTransferIndentResponse.setId((BigInteger) row.get("id"));
					branchSpareTransferIndentResponse.setIndentNumber((String) row.get("IndentNumber"));
					branchSpareTransferIndentResponse.setIndentDate((Date) row.get("IndentDate"));
					branchSpareTransferIndentResponse.setIndentOnBranch((String) row.get("IndentOnBranch"));
					branchSpareTransferIndentResponse.setIndentBy((String) row.get("IndentBy"));
					branchSpareTransferIndentResponse.setIndentRemarks((String) row.get("IndentRemarks"));
					branchSpareTransferIndentResponseList.add(branchSpareTransferIndentResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return branchSpareTransferIndentResponseList;
	}

	public List<IndentNumberDetails> fetchIndentTransferDtl(BigInteger paIndHdrId, String userCode, String dealerCode) {
		Session session = null;
		List<IndentNumberDetails> indentNumberDetailsList = null;
		IndentNumberDetails indentNumberDetails = null;
		List list = new ArrayList<>();

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_transfer_indent_issue_Dtl] :paIndHdrId, :userCode, :dealerCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("paIndHdrId", paIndHdrId);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerCode", dealerCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {

				indentNumberDetailsList = new ArrayList<IndentNumberDetails>();
				for (Object object : data) {
					Map row = (Map) object;
					indentNumberDetails = new IndentNumberDetails();
					indentNumberDetails.setPaIndDtlId((BigInteger) row.get("id"));
					indentNumberDetails.setPartBranchId((Integer) row.get("partBranch_id"));
					indentNumberDetails.setPartId((Integer) row.get("part_id"));
					indentNumberDetails.setPartNumber((String) row.get("PartNumber"));
					indentNumberDetails.setPartdesc((String) row.get("PartDesc"));
					indentNumberDetails.setBasicUnitPrice((BigDecimal) row.get("BasicUnitPrice"));
					indentNumberDetails.setIndentQty((Integer) row.get("IndentQty"));
					indentNumberDetails.setTotalStock((Integer) row.get("TotalStock"));
					indentNumberDetails.setTotalValue((BigDecimal) row.get("TotalValue"));
					indentNumberDetails.setFromStore((String) row.get("FromStore"));
					indentNumberDetails.setStoreBinLocation((String) row.get("StoreBinLocation"));
					indentNumberDetails.setBinStock((String) row.get("BinStock"));

					indentNumberDetailsList.add(indentNumberDetails);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return indentNumberDetailsList;
	}

	@Override
	public ApiResponse<List<BranchSpareIssueBinStockResponse>> fetchAvailableStock(BigInteger partBranchId, BigInteger partId,
			BigInteger branchId, BigInteger dealerId, BigInteger stockBinId, String userCode) {
		Session session = null;
		String msg = null;

		ApiResponse<List<BranchSpareIssueBinStockResponse>> apiResponse = null; 
		List<BranchSpareIssueBinStockResponse> branchSpareIssueBinStockResponseList = null;
		BranchSpareIssueBinStockResponse branchSpareIssueBinStockResponse = null;

		NativeQuery<?> query = null;
		String sqlQuery = "exec [PA_Get_Available_Stock_From_Stock_Bin] :PARTID, :PartbranchId, :branchId, :dealerId, :StockBinId";
		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("PARTID", partId != null ? partId : null);
			query.setParameter("PartbranchId", partBranchId != null ? partBranchId : null);
			query.setParameter("branchId", branchId);
			query.setParameter("dealerId", dealerId);
			query.setParameter("StockBinId", stockBinId);
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
	    		apiResponse = new ApiResponse<>();
				branchSpareIssueBinStockResponseList = new ArrayList<BranchSpareIssueBinStockResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					branchSpareIssueBinStockResponse = new BranchSpareIssueBinStockResponse();
					msg = (String) row.get("Message");

					if (msg != null && msg.equalsIgnoreCase("No Stock Available For This Dealer")) {
						apiResponse.setMessage(msg);
						apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					} else {

						branchSpareIssueBinStockResponse.setAvailableQty((BigDecimal) row.get("AvailableStock"));
						branchSpareIssueBinStockResponse.setStoreName((String) row.get("StoreName"));
						branchSpareIssueBinStockResponse.setBranchStoreId((Integer) row.get("branchStoreId"));
						branchSpareIssueBinStockResponse.setBinLocation((String) row.get("StoreBinLocation"));
						branchSpareIssueBinStockResponse.setBinId((BigInteger) row.get("stock_bin_id"));
						branchSpareIssueBinStockResponse.setUnitPrice((BigDecimal) row.get("BasicUnitPrice"));
						branchSpareIssueBinStockResponse.setPartBranchId((Integer) row.get("partBranch_id"));
						branchSpareIssueBinStockResponse.setStockStoreId((Integer) row.get("stockStoreId"));
						
						branchSpareIssueBinStockResponseList.add(branchSpareIssueBinStockResponse);
						apiResponse.setStatus(WebConstants.STATUS_OK_200);

					}
				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
	    	if (branchSpareIssueBinStockResponseList != null) {
	    		apiResponse.setResult(branchSpareIssueBinStockResponseList);
			}
	        if (session != null) {
	            session.close();
	        }
	    }
		return apiResponse;
	}
	
	@Override
	public List<BranchSpareTransferIndentHdrResponse> fetchIssueHeader(String usercode, String dealerCode,
			BigInteger paIndHdrId, Date fromDate, Date toDate, Integer page, Integer size) {
		Session session = null;
		List<BranchSpareTransferIndentHdrResponse> branchSpareTransferIndentResponseList = null;
		BranchSpareTransferIndentHdrResponse branchSpareTransferIndentResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String dateToString = null;

		String fromDateToString = null;
		String toDateToString = null;
		if (fromDate != null) {
			fromDateToString = dateFormat1.format(fromDate);
		}

		if (toDate != null) {
			toDateToString = dateFormat1.format(toDate);
		}

		if (dealerCode == null) {
			dealerCode = null;
		}

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_issue_header] :userCode, :indentNumber, :paIndHdrId, :FromDate, :ToDate, :page, :size";
	

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", usercode);
			query.setParameter("indentNumber", dealerCode);
			query.setParameter("paIndHdrId", paIndHdrId);
			query.setParameter("FromDate", fromDateToString);
			query.setParameter("ToDate", toDateToString);
			query.setParameter("page", page);
			query.setParameter("size", size);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				branchSpareTransferIndentResponseList = new ArrayList<BranchSpareTransferIndentHdrResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					branchSpareTransferIndentResponse = new BranchSpareTransferIndentHdrResponse();

					branchSpareTransferIndentResponse.setId((BigInteger) row.get("id"));
					branchSpareTransferIndentResponse.setIndentNumber((String) row.get("IndentNumber"));
					branchSpareTransferIndentResponse.setIndentDate((Date) row.get("IndentDate"));
					branchSpareTransferIndentResponse.setIndentOnBranch((String) row.get("IndentOnBranch"));
					branchSpareTransferIndentResponse.setIndentBy((String) row.get("IndentBy"));
					branchSpareTransferIndentResponse.setIndentRemarks((String) row.get("IndentRemarks"));
					branchSpareTransferIndentResponseList.add(branchSpareTransferIndentResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return branchSpareTransferIndentResponseList;
	}

	@Override
	public List<IndentNumberDetails> fetchIssueTransferDtl(BigInteger paIndHdrId, String userCode, String dealerCode) {

		Session session = null;
		List<IndentNumberDetails> indentNumberDetailsList = null;
		IndentNumberDetails indentNumberDetails = null;
		List list = new ArrayList<>();

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_transfer_indent_issue_Dtl] :paIndHdrId, :userCode, :dealerCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("paIndHdrId", paIndHdrId);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerCode", dealerCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {

				indentNumberDetailsList = new ArrayList<IndentNumberDetails>();
				for (Object object : data) {
					Map row = (Map) object;
					indentNumberDetails = new IndentNumberDetails();
					indentNumberDetails.setPaIndDtlId((BigInteger) row.get("id"));
					indentNumberDetails.setPartBranchId((Integer) row.get("partBranch_id"));
					indentNumberDetails.setPartId((Integer) row.get("part_id"));
					indentNumberDetails.setPartNumber((String) row.get("PartNumber"));
					indentNumberDetails.setPartdesc((String) row.get("PartDesc"));
					indentNumberDetails.setBasicUnitPrice((BigDecimal) row.get("BasicUnitPrice"));
					indentNumberDetails.setIndentQty((Integer) row.get("IndentQty"));
					indentNumberDetails.setTotalStock((Integer) row.get("TotalStock"));
					indentNumberDetails.setTotalValue((BigDecimal) row.get("TotalValue"));
					indentNumberDetails.setFromStore((String) row.get("FromStore"));
					indentNumberDetails.setStoreBinLocation(((String) row.get("StoreBinLocation")).trim());
					indentNumberDetails.setBinStock((String) row.get("BinStock"));
					indentNumberDetails.setBinLenght((Integer) row.get("BinLength"));
					indentNumberDetails.setIssueQty((Integer)row.get("IssueQty"));
					indentNumberDetails.setBranchStoreId((Integer)row.get("branch_store_id"));
					 String str =   (String) row.get("stock_bin_id");
					 String[] st = str.split(",");
					indentNumberDetails.setBinId(BigInteger.valueOf(Integer.valueOf(st[0])));
					indentNumberDetailsList.add(indentNumberDetails);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return indentNumberDetailsList;
	}

	
		

}
