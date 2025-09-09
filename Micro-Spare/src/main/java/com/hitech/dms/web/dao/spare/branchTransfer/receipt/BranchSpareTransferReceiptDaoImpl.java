package com.hitech.dms.web.dao.spare.branchTransfer.receipt;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentDtlEntity;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.entity.branchTransfer.issue.IssueHdrEntity;
import com.hitech.dms.web.entity.branchTransfer.receipt.ReceiptHdrEntity;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.ReceiptIndentDtlDetails;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.request.BranchSpareTransferReceiptRequest;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.response.BranchSpareTransferReceiptHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.response.BranchSpareTransferReceiptResponse;

@Repository
public class BranchSpareTransferReceiptDaoImpl implements BranchSpareTransferReceiptDao {
	private static final Logger logger = LoggerFactory.getLogger(BranchSpareTransferReceiptDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Override
	public HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Issue_Number] :searchText, :userCode";

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
	public HashMap<BigInteger, String> fetchBranchStoreList(int indentToBranchId, String userCode) {
		Session session = null;
		HashMap<BigInteger, String> branchStoreList = null;
		Query query = null;
		String sqlQuery = "select branch_store_id, StoreDesc from [PA_BRANCH_STORE] where branch_id = "
				+ indentToBranchId;

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
//			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				branchStoreList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					Integer branch_store_id = (Integer) row.get("branch_store_id");
					BigInteger branchStoreId = BigInteger.valueOf((Long.valueOf(branch_store_id.toString())));
					branchStoreList.put(branchStoreId, (String) row.get("StoreDesc"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return branchStoreList;
	}

	@Override
	public HashMap<BigInteger, String> searchBinName(String searchText, Integer indentToBranchId, String userCode) {
		Session session = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_BinName] :searchText, :indentToBranchId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("indentToBranchId", indentToBranchId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("stock_bin_id"), (String) row.get("BinName"));
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
	public HashMap<BigInteger, String> searchReceiptNumber(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "  select pa_receipt_id, ReceiptNumber from [PA_INDENT_RECEIPT_HDR] "
				+ "where ReceiptNumber like '%" + searchText + "%'";

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
					searchList.put((BigInteger) row.get("pa_receipt_id"), (String) row.get("ReceiptNumber"));
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
	public BranchSpareTransferResponse createBranchSpareTransferReceipt(
			BranchSpareTransferReceiptRequest branchSpareTransferReceiptRequest, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(branchSpareTransferReceiptRequest.toString());
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

		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				ReceiptHdrEntity receiptHdrEntity = new ReceiptHdrEntity();
				String generatedNumber = generateReceiptNumber(
						Integer.parseInt(branchSpareTransferReceiptRequest.getBranchId().toString()))
						.getGeneratedNumber();

				for (ReceiptIndentDtlDetails receiptIndentDtlDetails : branchSpareTransferReceiptRequest
						.getReceiptIndentDtlDetails()) {

				}
				receiptHdrEntity.setReceiptNumber(generatedNumber);
				receiptHdrEntity.setReceiptDate(branchSpareTransferReceiptRequest.getReceiptDate());
				receiptHdrEntity.setPaIssueHdrId(branchSpareTransferReceiptRequest.getPaIssueHdrId());
				receiptHdrEntity.setReceiptRemarks(branchSpareTransferReceiptRequest.getRemarks());
				receiptHdrEntity.setReceiptBy(branchSpareTransferReceiptRequest.getReceiptBy());
				receiptHdrEntity.setReceiptByBranch(branchSpareTransferReceiptRequest.getBranchId());

//				issueHdrEntity.setTotalIssuedQty(branchSpareTransferIssueRequest.getTotalIssuedQty());
//				issueHdrEntity.setTotalIssuedPart(branchSpareTransferIssueRequest.getTotalIssuedPart());
				receiptHdrEntity.setCreatedBy(id);
				receiptHdrEntity.setCreatedDate(todayDate);
				session.save(receiptHdrEntity);
				updateDocumentNumber(generatedNumber, "Branch Stock Transfer Receipt",
						Integer.parseInt(branchSpareTransferReceiptRequest.getBranchId().toString()), "BTR", session);

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

//	private void updateIssueAndIndentQty(Session session, IndentNumberDetails indentNumberDetails, BigInteger userId) {
//		if (indentNumberDetails != null) {
//			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//			String currentDate = dateFormat1.format(new java.util.Date());
//			String updateQuery = "update PA_Indent_dtl set IssueQTY = " + indentNumberDetails.getIssueQty()
//			+ ", IndentQTY = " + indentNumberDetails.getIndentQty()
//			+ ", modifiedBy = " + userId
//			+ ", ModifiedDate = " + currentDate + "where id = " + indentNumberDetails.getPaIndDtlId();
//
//			Query query = session.createSQLQuery(updateQuery);
//			query.executeUpdate();
//		}
//	}
//
	public GeneratedNumberModel generateReceiptNumber(Integer branchId) {
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
			String docType = "BTR";
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
//
//	private IndentHdrEntity getIndentHdr(BranchSpareTransferIssueRequest branchSpareTransferIssueRequest,
//			Session session, BigInteger userId) {
//		String sqlQuery = "select * from PA_INDENT_HDR where id = :paIndHdrId";
//
//		Query query;
//		query = session.createSQLQuery(sqlQuery);
//		query.setParameter("paIndHdrId", branchSpareTransferIssueRequest.getPaIndHdrId());
//		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//
//		List data = query.list();
//		if (data != null && !data.isEmpty()) {
//			for (Object object : data) {
//				Map row = (Map) object;
//
//				IndentHdrEntity indentHdrEntity = new IndentHdrEntity();
//				indentHdrEntity.setBranchId(userId);
////				Gson gson = new Gson();
////				JsonElement jsonElement = gson.toJsonTree(row);
////				IssueHdrEntity indentHdrEntity = gson.fromJson(jsonElement, IssueHdrEntity.class);
//				return indentHdrEntity;
//			}
//		}
//		return null;
//	}

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
	public List<BranchSpareTransferReceiptHdrResponse> fetchReceiptDetails(String receiptNumber, BigInteger paReceiptId,
			Date fromDate, Date toDate) {
		Session session = null;
		List<BranchSpareTransferReceiptHdrResponse> branchSpareTransferReceiptHdrResponseList = null;
		BranchSpareTransferReceiptHdrResponse branchSpareTransferReceiptHdrResponse = null;

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

		if (receiptNumber == null) {
			receiptNumber = null;
		}

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_Transfer_Receipt] " + receiptNumber + ", " + paReceiptId + ",";
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
				branchSpareTransferReceiptHdrResponseList = new ArrayList<BranchSpareTransferReceiptHdrResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					branchSpareTransferReceiptHdrResponse = new BranchSpareTransferReceiptHdrResponse();

					branchSpareTransferReceiptHdrResponse.setId((BigInteger) row.get("pa_receipt_id"));
					branchSpareTransferReceiptHdrResponse.setPaIndHdrId((BigInteger) row.get("paIndHdrId"));
					branchSpareTransferReceiptHdrResponse.setReceiptNumber((String) row.get("ReceiptNumber"));
					branchSpareTransferReceiptHdrResponse.setReceiptDate((Date) row.get("ReceiptDate"));
					branchSpareTransferReceiptHdrResponse.setReceiptRemarks((String) row.get("ReceiptRemarks"));
					branchSpareTransferReceiptHdrResponse.setReceiptBy((String) row.get("ReceiptBy"));
					branchSpareTransferReceiptHdrResponse.setOtherCharges((String) row.get("OtherCharges"));
					branchSpareTransferReceiptHdrResponse.setIssueNumber((String) row.get("IssueNumber"));
					branchSpareTransferReceiptHdrResponse.setIssueDate((Date) row.get("IssueDate"));
					branchSpareTransferReceiptHdrResponse.setIssueBy((String) row.get("IssueBy"));
					branchSpareTransferReceiptHdrResponse.setIssueByBranch((String) row.get("issueByBranch"));
					branchSpareTransferReceiptHdrResponse.setIssueRemarks((String) row.get("IssueRemarks"));
					branchSpareTransferReceiptHdrResponse.setIndentOnBranchId((Integer) row.get("IndentOnBranchId"));
					branchSpareTransferReceiptHdrResponseList.add(branchSpareTransferReceiptHdrResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return branchSpareTransferReceiptHdrResponseList;
	}

	@Override
	public BranchSpareTransferReceiptResponse fetchReceiptTransferHdrAndDtl(BigInteger paReceiptId) {
		BranchSpareTransferReceiptResponse branchSpareTransferReceiptResponse = new BranchSpareTransferReceiptResponse();
		BranchSpareTransferReceiptHdrResponse branchSpareTransferReceiptHdrResponse = fetchReceiptDetails(null, paReceiptId,
				null, null).get(0);
		List<IndentNumberDetails> indentNumberDetailsList = fetchReceiptTransferDtl(
				branchSpareTransferReceiptHdrResponse.getPaIndHdrId());
		Integer totalReceiptQty = 0;
		BigDecimal totalReceiptValue = null;
		Integer totalReceiptPart = indentNumberDetailsList.size();
		for (IndentNumberDetails indentNumberDetails : indentNumberDetailsList) {
			totalReceiptQty = totalReceiptQty + indentNumberDetails.getIssueQty();
			totalReceiptValue = BigDecimal.valueOf(totalReceiptQty).multiply(indentNumberDetails.getBasicUnitPrice());
		}
		branchSpareTransferReceiptHdrResponse.setTotalReceiptQty(totalReceiptQty);
		branchSpareTransferReceiptHdrResponse.setTotalReceiptPart(totalReceiptPart);
		branchSpareTransferReceiptHdrResponse.setTotalReceiptValue(totalReceiptValue);
		branchSpareTransferReceiptResponse.setBranchSpareTransferReceiptHdrResponse(branchSpareTransferReceiptHdrResponse);
		branchSpareTransferReceiptResponse.setIndentNumberDetails(indentNumberDetailsList);

		return branchSpareTransferReceiptResponse;

	}

	public List<IndentNumberDetails> fetchReceiptTransferDtl(BigInteger paIndHdrId) {
		Session session = null;
		List<IndentNumberDetails> indentNumberDetailsList = null;
		IndentNumberDetails indentNumberDetails = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_indent_Dtl] :paIndHdrId";

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
}
