/**
 * 
 */
package com.hitech.dms.web.dao.common;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.common.PartyLedgerEntity;
import com.hitech.dms.web.entity.spare.sale.invoice.SaleInvoiceEntity;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.common.PinCodeDetails;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentRequestModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceResponse;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class CommonDaoImpl implements CommonDao {
	private static final Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonServiceClient commonServiceClient;

	public GeoStateDTLResponseModel fetchStateDtlByStateID(String authorizationHeader, BigInteger stateId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateDtlByStateID invoked..");
		}
		GeoStateDTLResponseModel responseModel = null;
		try {
			HeaderResponse headerResponse = commonServiceClient.fetchStateDtlByStateID(authorizationHeader, stateId);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					responseModel = gson.fromJson(jsonString, GeoStateDTLResponseModel.class);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseModel;
	}

	public DealerDTLResponseModel fetchDealerDTLByDealerId(String authorizationHeader, BigInteger dealerId,
			String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerDTLByDealerId invoked..");
		}
		DealerDTLResponseModel responseModel = null;
		try {
			DealerDTLRequestModel requestModel = new DealerDTLRequestModel();
			requestModel.setDealerId(dealerId);
			requestModel.setIsFor(isFor);
			HeaderResponse headerResponse = commonServiceClient.fetchDealerDTLByDealerId(authorizationHeader,
					requestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					responseModel = gson.fromJson(jsonString, DealerDTLResponseModel.class);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseModel;
	}

	public BranchDTLResponseModel fetchBranchDtlByBranchId(String authorizationHeader, BigInteger branchId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchBranchDtlByBranchId invoked..");
		}
		BranchDTLResponseModel responseModel = null;
		try {
			BranchDTLRequestModel requestModel = new BranchDTLRequestModel();
			requestModel.setBranchId(branchId);
			requestModel.setIsFor("SALESINDENT");
			HeaderResponse headerResponse = commonServiceClient.fetchBranchDtlByBranchId(authorizationHeader,
					requestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					responseModel = gson.fromJson(jsonString, BranchDTLResponseModel.class);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseModel;
	}

	public List<ModelByPcIdResponseModel> fetchModelListByPcId(String authorizationHeader, Integer pcId, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchModelListByPcId invoked..");
		}
		List<ModelByPcIdResponseModel> responseModelList = null;
		try {
			HeaderResponse headerResponse = commonServiceClient.fetchModelListByPcId(authorizationHeader, pcId, isFor);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					ModelByPcIdResponseModel[] arr = gson.fromJson(jsonString, ModelByPcIdResponseModel[].class);
					responseModelList = Arrays.asList(arr);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseModelList;
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

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<?> fetchApprovalData(Session session, String approvalCode) {
		String sqlQuery = "select approver_level_seq, designation_level_id, grp_seq_no, 'Pending at '+dl.DesignationLevelDesc as approvalStatus,"
				+ "       isFinalApprovalStatus" + "       from SYS_APPROVAL_FLOW_MST(nolock) sf"
				+ "       inner join ADM_HO_MST_DESIG_LEVEL(nolock) dl on sf.designation_level_id=dl.ho_designation_level_id"
				+ "       where transaction_name=:approvalCode" + "       order by approver_level_seq,grp_seq_no";
		List data = null;
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("approvalCode", approvalCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return data;
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
	}

	@Override
	public List<PartNumberDetails> fetchPartNumberDetails(Integer partId, String userCode, Integer branchId,
			Integer poHdrId, Integer coId, Integer dcId, Integer pickListId, Integer refDocId, String flag) {
		Session session = null;
		PartNumberDetails partNumberDetails = null;
		List<PartNumberDetails> partNumberDetailsList = null;
		Query query = null;
		String msg = null;

		String sqlQuery = "exec [PA_Get_Part_Number_Details_bin_wise] :partId, :userCode, :branchId, :poHdrId,"
				+ " :COId, :DCId, :pickListId, :RefDocId, :flag";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partId", BigInteger.valueOf(partId));
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", branchId);
			query.setParameter("poHdrId", poHdrId);
			query.setParameter("COId", coId);
			query.setParameter("DCId", dcId);
			query.setParameter("pickListId", pickListId);
			query.setParameter("RefDocId", refDocId);
			query.setParameter("flag", flag);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partNumberDetailsList = new ArrayList<PartNumberDetails>();
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("Message");
					partNumberDetails = new PartNumberDetails();

//					if (msg != null && msg.equalsIgnoreCase("No Stock Available For This Dealer")) {
//						partNumberDetails.setMessage(msg);
//						partNumberDetails.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//					} else {
					partNumberDetails.setPickListDtlId((BigInteger) row.get("Pick_List_Dtl_Id"));
					partNumberDetails.setPoDtlId((BigInteger) row.get("po_dtl_id"));
					partNumberDetails.setPartId((Integer) row.get("part_id"));
					partNumberDetails.setPartBranchId((Integer) row.get("partBranch_id"));
					partNumberDetails.setPartNumber((String) row.get("PartNumber"));
					partNumberDetails.setPartDesc((String) row.get("PartDesc"));
					partNumberDetails.setPickListNumber((String) row.get("pickListNumber"));
					partNumberDetails.setPartSubCategory((String) row.get("PartSubCategory"));
//					partNumberDetails.setMrp((BigDecimal) row.get("mrp"));
					partNumberDetails.setMrp(fetchMRPList((Integer) row.get("part_id"), userCode));
					partNumberDetails.setDiscount((BigDecimal) row.get("discount"));
//					partNumberDetails.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					partNumberDetails.setTotalStock((Integer) row.get("totalStock"));
					partNumberDetails.setTotalValue((BigDecimal) row.get("totalValue"));
					partNumberDetails.setFromStore((String) row.get("FromStore"));
					partNumberDetails.setStoreBinLocation((String) row.get("StoreBinLocation"));
					partNumberDetails.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partNumberDetails.setOrderQty((BigDecimal) row.get("orderQty"));
					partNumberDetails.setBalanceQty((BigDecimal) row.get("BalanceQty"));
					partNumberDetails.setCustomerOrderOrDCId((Integer) row.get("customerOrderOrDCId"));
					partNumberDetails.setCustomerDtlId((Integer) row.get("customerDtlId"));
					partNumberDetails.setDcId((BigInteger) row.get("DChallan_Id"));
					partNumberDetails.setDcDtlId((BigInteger) row.get("DChallan_Dtl_Id"));
					partNumberDetails.setLockForTranscation((String) row.get("LockForTranscation"));
					partNumberDetails.setErrorMsg((String) row.get("errorMsg"));
					partNumberDetails.setLockPartCount((Integer) row.get("count"));
					partNumberDetailsList.add(partNumberDetails);
//					}
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partNumberDetailsList;
	}

	@Override
	public List<BigDecimal> fetchMRPList(Integer partId, String userCode) {

		Session session = null;

		List<BigDecimal> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_Get_MRP_List :userCode, :partId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("partId", partId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.add((BigDecimal) row.get("Dealer_MRP"));
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

	// DocNumber generate
	@Override
	public String getDocumentNumber(String documentPrefix, Integer branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);

		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		session.close();
		return documentNumber;
	}

	@Override
	public String getDocumentNumberById(String documentPrefix, BigInteger branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);

		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		return documentNumber;
	}

	@Override
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session) {

		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC [UpdateDocumentNo] :lastDocumentNo,:documentPrefix,:branchId,:currentDate";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("documentPrefix", documentPrefix);
			query.setParameter("branchId", Integer.parseInt(branchId));
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
	}

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void updateDocumentNumber(String documentType, BigInteger branchID, String pcrNo, Session session) {
		Query query = null;

		// Transaction transaction = session.beginTransaction();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String currentDate = null;
		currentDate = formatter.format(new Date());
		String sqlQuery = "exec [Update_INV_Doc_No] :LastDocumentNumber, :DocumentTypeDesc,'" + currentDate
				+ "',:BranchID";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("LastDocumentNumber", pcrNo);
		query.setParameter("DocumentTypeDesc", documentType);
		query.setParameter("BranchID", branchID);
		// query.setParameter("DocumentType", documentType);

		query.executeUpdate();
		System.out.println("after update ");
		// transaction.commit();
		System.out.println("after physically commit ");

	}

	@Override
	public PartDetailRequest updateStockForInPartBranchAndStockBin(Session session, String flag,
			PartDetailRequest partDetailRequest, BigInteger branchId, String tableName, String userCode) {
		return updateStockInPartBranchAndStockStore(session, flag, partDetailRequest, branchId,
				partDetailRequest.getBranchStoreId(), tableName, userCode);
	}

	@Override
	public PartDetailRequest updateStockInPartBranchAndStockStore(Session session, String flag,
			PartDetailRequest partDetailRequest, BigInteger branchId, Integer branchStoreId, String tableName,
			String userCode) {
		Integer partBranchId = null;
		BigInteger stockBinId = null;
		if (partDetailRequest.getInvoiceQty() != null
//				&& partDetailRequest.getPartBranchId().compareTo(BigInteger.ZERO) > 0
		) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Part_Branch " + ":flag ," + ":branchId,"
					+ ":partBranchId, :partId, " + ":branchStoreId, :qtyToBeAdded, :qtyToBeSubtracted, "
					+ ":basicUnitPrice," + ":modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", flag);
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", partDetailRequest.getPartBranchId());
			query.setParameter("partId", partDetailRequest.getPartId());
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("qtyToBeAdded", (flag.equalsIgnoreCase("ADD") ? partDetailRequest.getInvoiceQty() : 0));
			query.setParameter("qtyToBeSubtracted",
					(flag.equalsIgnoreCase("SUBTRACT") ? partDetailRequest.getInvoiceQty() : 0));
			query.setParameter("basicUnitPrice", partDetailRequest.getBasicUnitPrice());
			query.setParameter("modifiedBy", userCode);

//			query.executeUpdate();
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {

				String binLocation = null;
				for (Object object : data) {
					Map row = (Map) object;
					partBranchId = (Integer) row.get("partBranch_id");
					partDetailRequest.setPartBranchId(BigInteger.valueOf(partBranchId));
				}
			}
//			List<BranchSpareIssueBinStockResponse> binList = new ArrayList<BranchSpareIssueBinStockResponse>();
//
//			if (tableName.equalsIgnoreCase("PA_GRN_DTL") || tableName.equalsIgnoreCase("PA_CLAIM_DTL")
//					|| tableName.equalsIgnoreCase("PA_SALE_INVOICE_CANCEL")) {
//				BranchSpareIssueBinStockResponse binRequest1 = new BranchSpareIssueBinStockResponse();
//				binRequest1.setPartBranchId(partBranchId);
//				binRequest1.setBranchStoreId(branchStoreId);
//				binRequest1.setUnitPrice(partDetailRequest.getBasicUnitPrice());
//				binRequest1.setBinLocation(partDetailRequest.getBinLocation());
//				binRequest1.setIssueQty(partDetailRequest.getInvoiceQty());
//				binRequest1.setBinId(partDetailRequest.getBinId());
//				binList.add(binRequest1);
//
//				partDetailRequest.setBinRequest(binList);
//			}
//
//			for (BranchSpareIssueBinStockResponse binRequest : partDetailRequest.getBinRequest()) {
//				if (binRequest.getPartBranchId().equals(0)) {
//					binRequest.setPartBranchId(partBranchId);
//					stockBinId = updateStockInStockBin(session, binRequest, branchId,
//							partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), tableName,
//							userCode, flag);
//					binList = new ArrayList<BranchSpareIssueBinStockResponse>();
//					binRequest.setBinId(stockBinId);
//					binList.add(binRequest);
//					partDetailRequest.setBinRequest(binList);
//				} else if (binRequest.getBinId() == null || binRequest.getBinId().equals(0)) {
//					binRequest.setPartBranchId(partBranchId);
//					stockBinId = updateStockInStockBin(session, binRequest, branchId,
//							partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), tableName,
//							userCode, flag);
//					binList = new ArrayList<BranchSpareIssueBinStockResponse>();
//					binRequest.setBinId(stockBinId);
//					binList.add(binRequest);
//					partDetailRequest.setBinRequest(binList);
//				} else {
//					stockBinId = updateStockInStockBin(session, binRequest, branchId,
//							partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), tableName,
//							userCode, flag);
//				}
//			}
		}
		return partDetailRequest;
	}

	@Override
	public BigInteger updateStockInStockBin(Session session, BranchSpareIssueBinStockResponse binRequest,
			BigInteger branchId, Integer branchStoreId, BigDecimal InvoiceQty, String tableName, String userCode,
			String flag) {
		BigInteger stockBinId = null;

		if (binRequest != null && BigInteger.valueOf(binRequest.getPartBranchId()).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Stock_Bin :flag, " + ":branchId," + ":partBranchId, "
					+ ":branchStoreId, :stockBinId, :binName, :qtyToBeAdded, :qtyToBeSubtracted, :basicUnitPrice,"
					+ ":tableName, :modifiedBy";

			Query query = session.createSQLQuery(updateQuery);

			query.setParameter("flag", flag);
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", binRequest.getPartBranchId());
			query.setParameter("branchStoreId", binRequest.getBranchStoreId());
			query.setParameter("stockBinId", binRequest.getBinId() != null ? binRequest.getBinId() : 0);
			query.setParameter("binName", binRequest.getBinLocation());
			query.setParameter("basicUnitPrice", binRequest.getUnitPrice());
			query.setParameter("qtyToBeAdded", (flag.equalsIgnoreCase("ADD") ? binRequest.getIssueQty() : 0));
			query.setParameter("qtyToBeSubtracted",
					(flag.equalsIgnoreCase("SUBTRACT")
							? binRequest.getIssueQty() == null ? InvoiceQty : binRequest.getIssueQty()
							: 0));
			query.setParameter("tableName", tableName);
			query.setParameter("modifiedBy", userCode);
//			query.executeUpdate();

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {

				String binLocation = null;
				for (Object object : data) {
					Map row = (Map) object;
					stockBinId = (BigInteger) row.get("stock_bin_id");
				}
			}
		}

		return stockBinId;
	}

	@Override
	public PinCodeDetails fetchPinCodeDetails(Integer pinId) {
		Session session = null;
		PinCodeDetails pinCodeDetails = null;
		Query query = null;
		String sqlQuery = "exec [SP_CM_GEO_PinCode_Details] :pinId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pinId", pinId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					pinCodeDetails = new PinCodeDetails();
					pinCodeDetails.setPinId((BigInteger) row.get("pin_id"));
					pinCodeDetails.setPinCode((String) row.get("PinCode"));
					pinCodeDetails.setPostOffice((String) row.get("LocalityName"));
					pinCodeDetails.setCity((String) row.get("CityDesc"));
					pinCodeDetails.setTehsil((String) row.get("TehsilDesc"));
					pinCodeDetails.setDistrict((String) row.get("DistrictDesc"));
					pinCodeDetails.setState((String) row.get("StateDesc"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return pinCodeDetails;
	}

	@Override
	public HashMap<BigInteger, String> searchPinCode(String searchText) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [SP_CM_GEO_Search_PinCode] :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("pin_id"), (String) row.get("PinCode"));
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
	public String saveInPartyLedger(PartyLedgerEntity partyLedgerEntity, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(partyLedgerEntity.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();
		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				id = (BigInteger) session.save(partyLedgerEntity);
			}
			if (isSuccess) {
				transaction.commit();
				session.close();
				spareSalesInvoiceResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Party Ledger Created Successfully";
				spareSalesInvoiceResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				spareSalesInvoiceResponse.setStatusCode(statusCode);
				spareSalesInvoiceResponse.setMsg(msg);
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return msg;
	}

	// add by Ram
	@Override
	public SpareInvoicePriceResponse getPartsUnitPrice(SpareInvoicePriceRequest req, String userCode) {
		
		logger.info("get unit price invoked "+req);
		SpareInvoicePriceResponse res=null;
		boolean isSuccess=true;
		Session session=null;
		Query query=null;
		String sqlQuery = "exec [PA_Get_BasicUnitPrice] :UserCode ,:partId,:RefDocId ,:MRP,:PartyType";
		try
		{
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode",userCode);
			query.setParameter("partId",req.getPartId());
			query.setParameter("RefDocId",req.getRefDocId());
			query.setParameter("MRP",req.getMrp());
			query.setParameter("PartyType",req.getPartyType());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					res= new SpareInvoicePriceResponse();
					Map row = (Map) object;
					res.setPartStatePriceId((BigInteger)row.get("Part_StatePrice"));
					res.setPartId((BigInteger)row.get("part_id"));
					res.setDealerMrp((BigDecimal)row.get("Dealer_MRP"));
					res.setDiscount((BigDecimal)row.get("discount"));
					res.setBasicUnitPrice((BigDecimal)row.get("BasicUnitPrice"));
					
				}
			}
		}catch (SQLGrammarException ex) {
			
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
		
		
		return res;
	}
}
