package com.hitech.dms.web.dao.spare.inventorytransfer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
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

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.grn.mapping.SpareGRNDaoImpl;
import com.hitech.dms.web.entity.spare.grn.SpareGrnDtlEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnHdrEntity;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnHdr;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.request.PartDetailRequest;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventoryRequest;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventorySearchRequest;

@Repository
public class InventoryTransferForClaimDaoImpl implements InventoryTransferForClaimDao {

	private static final Logger logger = LoggerFactory.getLogger(SpareGRNDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Override
	public HashMap<BigInteger, String> searchGrnNumber(String searchType, String searchText, String page, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Grn_List] :searchText, :isFor, :claimTypeOrPage, :userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", searchType);
			query.setParameter("searchText", searchText);
			query.setParameter("claimTypeOrPage", page.equalsIgnoreCase("null") ? null : page);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					/* only GRN created from VST is allowed */
					if (((String) row.get("Grn_type")).equalsIgnoreCase("VST")) {
						searchList.put((BigInteger) row.get("mrn_hdr_id"), (String) row.get("MRNNumber"));
					}
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
	public SpareGrnInventoryResponse fetchGrnDetails(int grnHdrId, String pageName) {
		Session session = null;
		List<SpareGrnInventoryResponse> spareGrnInventoryResponseList = null;
		SpareGrnInventoryResponse spareGrnInventoryResponse = null;

		String msg = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn_Hdr_Details] :grnHdrId," + null + ", :pageName";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnHdrId", grnHdrId);
			query.setParameter("pageName", pageName);

//			query.setParameter("grnTypeId", (grnTypeId.compareTo(BigInteger.ZERO) == 0) ? null : grnTypeId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			spareGrnInventoryResponse = new SpareGrnInventoryResponse();

			if (data != null && !data.isEmpty()) {
//					spareGrnDetailsResponseList = new ArrayList<SpareGrnInventoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("Message");
					if (msg != null && (msg.equalsIgnoreCase("Inventory Transfer already completed")
							|| msg.equalsIgnoreCase("Claim Already Generated"))) {
						spareGrnInventoryResponse.setMsg(msg);
						spareGrnInventoryResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					} else {
						spareGrnInventoryResponse.setInvoiceNo((String) row.get("InvoiceNo"));
						spareGrnInventoryResponse
								.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
						spareGrnInventoryResponse
								.setInventoryTransferNumber((String) row.get("InventoryTransferNumber"));
						spareGrnInventoryResponse.setInventoryTransferDate(
								parseDateInStringFormat((Date) row.get("InventoryTransferDate")));
						spareGrnInventoryResponse.setGrnNumber((String) row.get("MRNNumber"));
						spareGrnInventoryResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
						spareGrnInventoryResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
						spareGrnInventoryResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
						spareGrnInventoryResponse.setStore((String) row.get("StoreDesc"));
						spareGrnInventoryResponse.setDriverName((String) row.get("DriverName"));
						spareGrnInventoryResponse.setDriverMobNo((String) row.get("DriverMobNo"));
						spareGrnInventoryResponse.setTransporterName((String) row.get("transporterName"));
						spareGrnInventoryResponse.setSupplierPoNumber((String) row.get("PONumber"));
						spareGrnInventoryResponse.setProductCategory((String) row.get("productCategory"));
						spareGrnInventoryResponse.setInvoiceAmount((BigDecimal) row.get("MRNInvoiceAmount"));
//							spareGrnInventoryResponseList.add(spareGrnInventoryResponse);	
						spareGrnInventoryResponse.setMsg("GRN details fetched successfully");
						spareGrnInventoryResponse.setStatusCode(WebConstants.STATUS_OK_200);

					}
				}
			} else {
				spareGrnInventoryResponse.setMsg("No Data Found");
				spareGrnInventoryResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		} catch (SQLGrammarException exp) {
			spareGrnInventoryResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			spareGrnInventoryResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (Exception exp) {
			spareGrnInventoryResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			String errorMessage = "An error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return spareGrnInventoryResponse;
	}

	@Override
	public List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId, String page, String claimType) {
		Session session = null;
		List<PartNumberDetailResponse> partDetailResponsList = null;
		PartNumberDetailResponse partDetailResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Details] :grnHdrId";

		try {

			if (page != null) {
				sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Details] :grnHdrId, :page";
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("grnHdrId", grnHdrId);
				query.setParameter("page", page);

			} else {
				sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_By_Claim_Type] :grnHdrId, :claimType";
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("grnHdrId", grnHdrId);
				query.setParameter("claimType", claimType);
			}

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponsList = new ArrayList<PartNumberDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					BigDecimal basicUnitPrice = (BigDecimal) row.get("basicUnitPrice");
					BigDecimal discountPercentage = (BigDecimal) row.get("discountPercentage");
					BigDecimal cgstPercentage = (BigDecimal) row.get("cgst_per");
					BigDecimal igstPercentage = (BigDecimal) row.get("igst_per");
					BigDecimal sgstPercentage = (BigDecimal) row.get("sgst_per");

					BigDecimal discountAmount = ((discountPercentage).multiply(basicUnitPrice)
							.divide(BigDecimal.valueOf(100)));
					BigDecimal cgstAmount = (BigDecimal) row.get("cgst");
					BigDecimal sgstAmount = (BigDecimal) row.get("sgst");
					BigDecimal igstAmount = (BigDecimal) row.get("igst");

					partDetailResponse = new PartNumberDetailResponse();
					partDetailResponse.setGrnDtlId((BigInteger) row.get("mrn_dtl_id"));
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setPartBranchId((Integer) row.get("partBranch_id"));
					partDetailResponse.setPartSubCategory((String) row.get("partSubCategory"));
					partDetailResponse.setBinLocation((String) row.get("binName"));
					partDetailResponse.setAvailableQty((Integer) row.get("availableQty"));
					partDetailResponse.setHsnCode((String) row.get("hsnCode"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					partDetailResponse.setStoreDesc((String) row.get("StoreDesc"));
					partDetailResponse.setSerialNumber((String) row.get("serialNumber"));
					partDetailResponse.setBasicUnitPrice(basicUnitPrice);
					partDetailResponse.setInvoiceQty((BigDecimal) row.get("invoiceQty"));
					partDetailResponse.setUnRestrictedQty((BigDecimal) row.get("UnrestrictedQty"));
					partDetailResponse.setRestrictedQty((BigDecimal) row.get("RestrictedQty"));
					partDetailResponse.setClaimedQty((BigDecimal) row.get("ClaimedQty"));
					partDetailResponse.setClaimStatus((String) row.get("setClaimStatus"));
					partDetailResponse.setAccessibleValue((BigDecimal) row.get("accessibleValue"));
					partDetailResponse.setDiscountPercentage((BigDecimal) row.get("discountPercentage"));
					partDetailResponse.setDiscountAmount(discountAmount);
					partDetailResponse.setCgstPercentage(cgstPercentage);
					partDetailResponse.setCgstAmount(cgstAmount);
					partDetailResponse.setSgstPercentage(sgstPercentage);
					partDetailResponse.setSgstAmount(sgstAmount);
					partDetailResponse.setIgstPercentage(igstPercentage);
					partDetailResponse.setIgstAmount(igstAmount);
					partDetailResponse.setReceiptQty((BigDecimal) row.get("ReceiptQty"));
					partDetailResponse.setReceiptValue((BigDecimal) row.get("receiptValue"));
//					partDetailResponse.setIsAgree((Character) row.get("IsAgree"));

					partDetailResponsList.add(partDetailResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partDetailResponsList;
	}

	@Override
	public SpareGrnResponse createSpareInventory(String userCode, SpareInventoryRequest spareInventoryRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(spareInventoryRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
		List<SpareGrnResponse> spareGrnResponseList = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String active = "N";
			mapData = fetchUserDTLByUserCode(session, userCode);
			String inventoryTransferNumber = null;
			BigInteger id = null;
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				inventoryTransferNumber = generateInventoryNumber(spareInventoryRequest.getBranchId())
						.getGeneratedNumber();

				isExist = checkIfInventoryAlreadyExist(spareInventoryRequest, userCode);

				logger.info("isExist for inventoryTransferNumber {} {}--", inventoryTransferNumber, isExist);

				if (isExist) {

					SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();

					for (PartDetailRequest partDetailRequest : spareInventoryRequest.getSaveSpareInventory()) {

						SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
						String currentDate = dateFormat1.format(new java.util.Date());
						String updateQuery = "EXEC [PA_Update_Spare_Grn_Dtl] :grnDtlId, :unrestrictedQty, :restrictedQty,"
								+ null + ", :modifiedBy";

						query = session.createSQLQuery(updateQuery);
						query.setParameter("grnDtlId", partDetailRequest.getGrnDtlId());
						query.setParameter("unrestrictedQty", partDetailRequest.getUnRestrictedQty());
						query.setParameter("restrictedQty", partDetailRequest.getRestrictedQty());
						query.setParameter("modifiedBy", userCode);
						query.executeUpdate();

						updateStockForIssue(session, partDetailRequest, spareInventoryRequest.getBranchId(), userCode);
					}
				} else {
					updateSpareGrnHdr(inventoryTransferNumber, new Date(), spareInventoryRequest.getGrnHdrId(),
							userCode, session);
					SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();

					for (PartDetailRequest partDetailRequest : spareInventoryRequest.getSaveSpareInventory()) {

						SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
						String currentDate = dateFormat1.format(new java.util.Date());
						String updateQuery = "EXEC [PA_Update_Spare_Grn_Dtl] :grnDtlId, :unrestrictedQty, :restrictedQty,"
								+ null + ", :modifiedBy";

						query = session.createSQLQuery(updateQuery);
						query.setParameter("grnDtlId", partDetailRequest.getGrnDtlId());
						query.setParameter("unrestrictedQty", partDetailRequest.getUnRestrictedQty());
						query.setParameter("restrictedQty", partDetailRequest.getRestrictedQty());
						query.setParameter("modifiedBy", userCode);
						query.executeUpdate();

						logger.info("PA_Update_Spare_Grn_Dtl for inventoryTransferNumber {} --",
								inventoryTransferNumber);

						updateStockForIssue(session, partDetailRequest, spareInventoryRequest.getBranchId(), userCode);
						updateDocumentNumber(inventoryTransferNumber, "Inventory Transfer Number",
								spareInventoryRequest.getBranchId(), "ITN", session);
					}
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Inventory Transfer for Claim Created Successfully with Inventory Transfer Number - "
						+ inventoryTransferNumber;
				spareGrnResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(statusCode);
				spareGrnResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return spareGrnResponse;
	}

	private Boolean checkIfInventoryAlreadyExist(SpareInventoryRequest spareInventoryRequest, String userCode) {
		Session session = sessionFactory.openSession();
		Query query = null;
		boolean isExist = false;

		SpareGrnHdr spareGrnHdr = new SpareGrnHdr();
		String sqlQuery = "exec [PA_Check_Spare_Inventory] :grnHdrId," + null + ", :userCode";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("grnHdrId", spareInventoryRequest.getGrnHdrId());
		query.setParameter("userCode", userCode);

		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				String inventoryNumber = (String) row.get("InventoryTransferNumber");
				if (inventoryNumber != null) {
					isExist = true;
				}
			}
		}

		return isExist;
	}

	private void updateStockForIssue(Session session, PartDetailRequest partDetailRequest, Integer branchId,
			String userCode) {
		logger.info("updateStockForIssue for inventoryTransferNumber");

		updateStockInPartBranchAndStockStore(session, partDetailRequest, branchId, partDetailRequest.getBranchStoreId(),
				userCode);

//		for(BranchSpareIssueBinStockResponse binRequest: partDetailRequest.getBinRequest()) {
		updateStockInStockBin(session, partDetailRequest, branchId, partDetailRequest.getBranchStoreId(), userCode);
//		}	
	}

	private void updateStockInPartBranchAndStockStore(Session session, PartDetailRequest partDetailRequest,
			Integer branchId, Integer branchStoreId, String userCode) {
		if (partDetailRequest.getRestrictedQty() != null) {

			logger.info("updateStockInPartBranchAndStockStore for inventoryTransferNumber");

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Part_Branch " + ":flag ," + ":branchId," + ":partBranchId,"
					+ ":partId, :branchStoreId, :qtyToBeAdded, :qtyToBeSubtracted, " + ":basicUnitPrice," + ":modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", partDetailRequest.getPartBranchId());
			query.setParameter("partId", 0);
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("qtyToBeAdded", null);
			query.setParameter("qtyToBeSubtracted", partDetailRequest.getRestrictedQty());
			query.setParameter("basicUnitPrice", partDetailRequest.getBasicUnitPrice());
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}

	private void updateStockInStockBin(Session session, PartDetailRequest partDetailRequest, Integer branchId,
			Integer branchStoreId, String userCode) {

		if (partDetailRequest != null) {

			logger.info("updateStockInStockBin for inventoryTransferNumber");

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Stock_Bin " + " SUBTRACT , " + ":branchId, "
					+ ":partBranchId, " + ":branchStoreId, " + ":stockBinId, :binName, :qtyToBeAdded, :qtyToBeSubtracted, "
					+ ":basicUnitPrice, " + ":tableName, " + ":modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", partDetailRequest.getPartBranchId());
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("stockBinId", partDetailRequest.getStockBinId());
			query.setParameter("binName", partDetailRequest.getStockBinId());
			query.setParameter("qtyToBeAdded", partDetailRequest.getRestrictedQty());
			query.setParameter("qtyToBeSubtracted", partDetailRequest.getRestrictedQty());
			query.setParameter("basicUnitPrice", partDetailRequest.getBasicUnitPrice());
			query.setParameter("tableName", "PA_GRN_HDR");
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}

	private void updateDocumentNumber(String inventoryTransferNumber, String documentTypeDesc, Integer branchId,
			String documentType, Session session) {
		String lastDocumentNumber = inventoryTransferNumber.substring(inventoryTransferNumber.length() - 7);
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

	private void updateAvailableQty(PartDetailRequest partDetailRequest, String userCode, Session session) {
		if (partDetailRequest != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "update PA_PART_BRANCH set OnHandQty = :OnHandQty,"
					+ " ModifiedBy = :modifiedBy where partBranch_id = :partBranch_id";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("OnHandQty", partDetailRequest.getAvailableQty());
			query.setParameter("partBranch_id", partDetailRequest.getPartBranchId());
			query.setParameter("modifiedBy", userCode);

			query.executeUpdate();

		}

	}

	private void updateSpareGrnHdr(String inventoryNumber, Date inventoryTransferDate, BigInteger grnHdrId,
			String userCode, Session session) {
		logger.info("In update grn data for inventoryTransferNumber {} --", inventoryNumber);

		if (inventoryNumber != null) {
			logger.info("In update grn data line 2 inventoryTransferNumber {} --", inventoryNumber);

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
//			String updateQuery = "EXEC [PA_Update_Spare_Grn]" + null + ", :grnHdrId, :inventoryNumber," + null
//					+ " , :modifiedBy";

			String updateQuery = "update PA_GRN_HDR set InventoryTransferNumber = :inventoryNumber, "
					+ "InventoryTransferDate = :inventoryTransferDate, "
					+ "ModifiedBy = :modifiedBy,  ModifiedDate = :modifiedDate " + "where mrn_hdr_id = :grnHdrId";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("inventoryNumber", inventoryNumber);
			query.setParameter("inventoryTransferDate", inventoryTransferDate);
			query.setParameter("grnHdrId", grnHdrId);
			query.setParameter("modifiedBy", userCode);
			query.setParameter("modifiedDate", currentDate);

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

	public GeneratedNumberModel generateInventoryNumber(Integer branchId) {
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
			String docType = "ITN";
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

	@Override
	public ApiResponse<List<SpareGrnInventoryResponse>> fetchGrnList(
			SpareInventorySearchRequest spareSearchInventoryRequest, String userCode) {
		Session session = null;
		ApiResponse<List<SpareGrnInventoryResponse>> apiResponse = null;
		List<SpareGrnInventoryResponse> spareGrnInventoryResponseList = null;
		SpareGrnInventoryResponse spareGrnInventoryResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//			String dateToString = null;
//		String fromDateToString = dateFormat1.format(spareSearchInventoryRequest.getFromDate());
//		String toDateToString = dateFormat1.format(spareSearchInventoryRequest.getToDate());
		Integer dataCount = 0;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Inventory_For_Claim] " + null + ", :grnNumber, :inventoryNumber, " + "'"
				+ spareSearchInventoryRequest.getFromDate() + "','" + spareSearchInventoryRequest.getToDate() + "', "
				+ ":userCode, :page, :size, :pcId, :hoId, :zoneId, :stateId , :territoryId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("grnNumber", spareSearchInventoryRequest.getGrnNumber() == null ? null
					: spareSearchInventoryRequest.getGrnNumber());
			query.setParameter("inventoryNumber", spareSearchInventoryRequest.getInventoryNumber() == null ? null
					: spareSearchInventoryRequest.getInventoryNumber());
			query.setParameter("userCode", userCode);
			query.setParameter("page", spareSearchInventoryRequest.getPage());
			query.setParameter("size", spareSearchInventoryRequest.getSize());
			query.setParameter("pcId", null);
			query.setParameter("hoId", null);
			query.setParameter("zoneId", null);
			query.setParameter("stateId", null);
			query.setParameter("territoryId", null);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(query.toString());

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				spareGrnInventoryResponseList = new ArrayList<SpareGrnInventoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnInventoryResponse = new SpareGrnInventoryResponse();

					spareGrnInventoryResponse.setId((BigInteger) row.get("mrn_hdr_id"));
					spareGrnInventoryResponse.setInventoryTransferNumber((String) row.get("InventoryTransferNumber"));
					spareGrnInventoryResponse
							.setInventoryTransferDate(parseDateInStringFormat((Date) row.get("InventoryTransferDate")));
					spareGrnInventoryResponse.setGrnNumber((String) row.get("MRNNumber"));
					spareGrnInventoryResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
					spareGrnInventoryResponse.setStatus((String) row.get("status"));
					spareGrnInventoryResponse.setInvoiceNo((String) row.get("InvoiceNo"));
					spareGrnInventoryResponse.setInvoiceAmount((BigDecimal) row.get("InvoiceAmount"));
					spareGrnInventoryResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
					spareGrnInventoryResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
					spareGrnInventoryResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					spareGrnInventoryResponse.setStore((String) row.get("StoreDesc"));
					spareGrnInventoryResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnInventoryResponse.setTransporterName((String) row.get("transporterName"));
					spareGrnInventoryResponse.setTransporterVehicle((String) row.get("transporterVehicle"));
					spareGrnInventoryResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnInventoryResponse.setSupplierPoNumber((String) row.get("PONumber"));
					spareGrnInventoryResponse.setDriverName((String) row.get("DriverName"));
					spareGrnInventoryResponse.setDriverMobNo((String) row.get("DriverMobNo"));
					dataCount = (Integer) row.get("totalCount");
					spareGrnInventoryResponseList.add(spareGrnInventoryResponse);
				}
			}
		} catch (SQLGrammarException exp) {
			String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (Exception exp) {
			String errorMessage = "An error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} finally {
			if (spareGrnInventoryResponseList != null) {
				apiResponse = new ApiResponse<>();
				apiResponse.setCount(dataCount);
				apiResponse.setResult(spareGrnInventoryResponseList);
				apiResponse.setMessage("Inventory Details Fetched Successfully.");
				apiResponse.setStatus(WebConstants.STATUS_OK_200);
			} else {
				apiResponse = new ApiResponse<>();
				apiResponse.setCount(dataCount);
				apiResponse.setResult(spareGrnInventoryResponseList);
				apiResponse.setMessage("Inventory Details not found.");
				apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}

		return apiResponse;
	}

	public String parseDateInStringFormat(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String newDate = dateFormat.format(date);
			return newDate;
		}
		return null;
	}

}
