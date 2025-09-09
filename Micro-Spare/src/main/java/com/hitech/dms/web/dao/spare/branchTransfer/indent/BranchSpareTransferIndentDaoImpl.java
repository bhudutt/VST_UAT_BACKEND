package com.hitech.dms.web.dao.spare.branchTransfer.indent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentDtlEntity;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;

@Repository
public class BranchSpareTransferIndentDaoImpl implements BranchSpareTransferIndentDao {
	private static final Logger logger = LoggerFactory.getLogger(BranchSpareTransferIndentDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Override
	public HashMap<BigInteger, String> searchPartNumber(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = new HashMap<>();;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Part_No] :userCode, :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put(BigInteger.valueOf((Integer) row.get("part_id")), (String) row.get("PartNumber"));
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
	public PartNumberDetails fetchPartNumberDetails(Integer partId, String userCode, Integer branchId) {
		Session session = null;
		PartNumberDetails partNumberDetails = null;
		List<PartNumberDetails> partNumberDetailsList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Part_Number_Details] :partId, :userCode, :branchId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partId", BigInteger.valueOf(partId));
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", branchId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partNumberDetailsList = new ArrayList<PartNumberDetails>();
				for (Object object : data) {
					Map row = (Map) object;
					partNumberDetails = new PartNumberDetails();
					partNumberDetails.setPartId((Integer) row.get("part_id"));
					partNumberDetails.setPartNumber((String) row.get("PartNumber"));
					partNumberDetails.setPartDesc((String) row.get("PartDesc"));
					partNumberDetails.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					partNumberDetails.setIndentQty((Integer) row.get("indentQty"));
					partNumberDetails.setTotalStock((Integer) row.get("totalStock"));
					partNumberDetails.setTotalValue((BigDecimal) row.get("totalValue"));
					partNumberDetails.setFromStore((String) row.get("fromStore"));
					partNumberDetails.setStoreBinLocation((String) row.get("storeBinLocation"));
					partNumberDetails.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partNumberDetails.setHsnCode((String) row.get("hsnCode"));
					partNumberDetails.setSerialNo((String) row.get("serailNo"));
					partNumberDetails.setCgst((BigDecimal) row.get("cgst"));
					partNumberDetails.setSgst((BigDecimal) row.get("sgst"));
					partNumberDetails.setIgst((BigDecimal) row.get("igst"));
					partNumberDetails.setDiscount((BigDecimal) row.get("discount"));
					
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partNumberDetails;
	}

	@Override
	public BranchSpareTransferResponse createBranchSpareTransferIndent(IndentHdrEntity indentHdrEntity,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(indentHdrEntity.toString());
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
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				if (indentHdrEntity.getId() == null) {
					String generatedNumber = generateIndentNumber(Integer.parseInt(indentHdrEntity.getBranchId().toString()))
							.getGeneratedNumber();
					indentHdrEntity.setIndentNumber(generatedNumber);
					indentHdrEntity.setCreatedBy(userId);
					indentHdrEntity.setCreatedDate(todayDate);
					session.save(indentHdrEntity);
					updateDocumentNumber(generatedNumber, "Branch Stock Transfer Indent",
							Integer.parseInt(indentHdrEntity.getBranchId().toString()), "BSTI", session);

				} else {
					indentHdrEntity.setModifiedBy(userId);
					indentHdrEntity.setModifiedDate(todayDate);
					session.update(indentHdrEntity);
					for (IndentDtlEntity indentDtlEntity : indentHdrEntity.getIndentDtlEntity()) {
						indentDtlEntity.setModifiedBy(userId);
						indentDtlEntity.setModifiedDate(todayDate);
						session.update(indentDtlEntity);
					}

				}

			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				branchSpareTransferResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Branch Transfer Indent Created Successfully";
				branchSpareTransferResponse.setMsg(msg);
			} else {
				transaction.rollback();
				session.close();
				branchSpareTransferResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				branchSpareTransferResponse.setMsg("Issue In Creation Of Branch Transfer Indent");
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

	public GeneratedNumberModel generateIndentNumber(Integer branchId) {
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
			String docType = "BSTI";
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
	public HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "  select id, IndentNumber from [PA_INDENT_HDR] where IndentNumber LIKE :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", "%" + searchText + "%");

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
	public List<BranchSpareTransferIndentHdrResponse> fetchIndentDetails(
	        String indentNumber,
	        BigInteger paIndHdrId,
	        Date fromDate,
	        Date toDate,
	        Integer page,
	        Integer size,
	        String userCode) {
		Session session = null;
	    Query<?> query = null;
	    List<BranchSpareTransferIndentHdrResponse> responseList = new ArrayList<>();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateStr = (fromDate != null) ? dateFormat.format(fromDate) : null;
        String toDateStr = (toDate != null) ? dateFormat.format(toDate) : null;
        String str = null;
	    try {
	      
	        String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_indent]" +
	                " :userCode, :indentNumber, :paIndHdrId, :FromDate, :ToDate, :page, :size";

	        session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
	        // Date formatting
	      
			if(indentNumber!=null && !"null".equals(indentNumber) && !indentNumber.isEmpty()) {
				str=indentNumber;
			}
		
	        // Setting query parameters
	        query.setParameter("userCode", userCode);
	        query.setParameter("indentNumber", str);
	        query.setParameter("paIndHdrId", paIndHdrId);
	        query.setParameter("FromDate", fromDateStr);
	        query.setParameter("ToDate", toDateStr);
	        query.setParameter("page", page);
	        query.setParameter("size", size);

	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

	        List results = query.list();

	        if (results != null && !results.isEmpty()) {
	            for (Object result : results) {
	                Map row = (Map) result;
	                BranchSpareTransferIndentHdrResponse response = new BranchSpareTransferIndentHdrResponse();

	                response.setId((BigInteger) row.get("id"));
	                response.setIndentNumber((String) row.get("IndentNumber"));
	                response.setIndentDate((Date) row.get("IndentDate"));
	                response.setIndentOnBranch((String) row.get("IndentOnBranch"));
	                response.setIndentBy((String) row.get("IndentBy"));
	                response.setIndentRemarks((String) row.get("IndentRemarks"));

	                responseList.add(response);
	            }
	        }

	    } catch (Exception e) {
	        logger.error("Error in fetchIndentDetails", e);
	    } finally {
	       
	    }

	    return responseList;
	}


	@Override
	public BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode, String dealerCode) {
		BranchSpareTransferResponse branchSpareTransferResponse = new BranchSpareTransferResponse();
		branchSpareTransferResponse
				.setBranchSpareTransferIndentHdrResponse(fetchIndentDetails(null, paIndHdrId, null, null, 0, 1,userCode).get(0));
		branchSpareTransferResponse.setIndentNumberDetails(fetchIndentTransferDtl(paIndHdrId, userCode, dealerCode));

		return branchSpareTransferResponse;

	}

	public List<IndentNumberDetails> fetchIndentTransferDtl(BigInteger paIndHdrId, String userCode, String dealerCode) {
		Session session = null;
		List<IndentNumberDetails> indentNumberDetailsList = null;
		IndentNumberDetails indentNumberDetails = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Branch_Spare_transfer_indent_Dtl] :paIndHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("paIndHdrId", paIndHdrId);
//			query.setParameter("userCode", userCode);
//			query.setParameter("dealerCode", dealerCode);

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

}
