package com.hitech.dms.web.dao.spare.picklist;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.spare.counterSale.CounterSaleEntity;
import com.hitech.dms.web.entity.spare.picklist.PickListDtlEntity;
import com.hitech.dms.web.entity.spare.picklist.PickListHdrEntity;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderPartForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanPartDetailRequest;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.picklist.PickListPartDetailResponse;
import com.hitech.dms.web.model.spare.picklist.PickListResponse;

@Repository
public class PickListDaoImpl implements PickListDao {
	private static final Logger logger = LoggerFactory.getLogger(PickListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CommonDao commonDao;

	@Override
	public HashMap<BigInteger, String> searchCustomerOrderNumber(String searchText, String searchFor, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_Search_CO_No :userCode, :searchText, :searchFor";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", searchText);
			query.setParameter("searchFor", searchFor);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
//					Integer customerId = (Integer) row.get("Customer_Id");
					searchList.put(BigInteger.valueOf((Integer) row.get("Customer_Id")),
							(String) row.get("Customer_Order_Number"));
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
	public CustomerOrderNumberResponse createPicklist(String userCode, PicklistRequest picklistRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(picklistRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		CustomerOrderNumberResponse customerOrderNumberResponse = new CustomerOrderNumberResponse();
		Map<String, Object> mapData = null;
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

				PickListHdrEntity pickListHdrEntity = new PickListHdrEntity();

				String generatedNumber = generateDocNumber(picklistRequest.getBranchId(), "PIK");
				BigInteger counterSaleId = picklistRequest.getCounterSaleId();

				if (generatedNumber != null) {
					if (counterSaleId == null
							&& picklistRequest.getReferenceDocumentId().equals(BigInteger.valueOf(1))) {
						counterSaleId = saveCounterSaleData(session, picklistRequest, userCode);
					}

					pickListHdrEntity.setPickListNumber(generatedNumber);
					pickListHdrEntity.setBranchId(picklistRequest.getBranchId());
					pickListHdrEntity.setRefDoc(picklistRequest.getReferenceDocumentId());
					pickListHdrEntity.setCounterSaleId(counterSaleId);
					pickListHdrEntity.setPoHdrId(picklistRequest.getPoHdrId());
					pickListHdrEntity.setCoHdrId(picklistRequest.getCustomerOrderId());
					pickListHdrEntity.setPickListStatus(picklistRequest.getStatus());
					pickListHdrEntity.setPinId(picklistRequest.getPinId());
					pickListHdrEntity.setCreatedDate(todayDate);
					pickListHdrEntity.setCreatedBy(userCode);

					id = (BigInteger) session.save(pickListHdrEntity);
					updateDocumentNumber(generatedNumber, "PickList", picklistRequest.getBranchId(), "PIK", session);

//					for (PartDetailRequest partDetailRequest : picklistRequest.getPartDetails()) {
////						updateStockForIssue(session, partDetailRequest, picklistRequest.getBranchId(),
////								userCode);
					//
//						updateIssueQtyForCustomerOrder(session, partDetailRequest, userId);
//					}
					updatePickListFlag(session, picklistRequest.getCustomerOrderId(), userId);

					isSuccess = savePickListDtl(session, picklistRequest, id, userCode, userId);
				} else {
					isSuccess = false;
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				updateInOrderStatus(picklistRequest.getReferenceDocumentId(), picklistRequest, userCode);
				customerOrderNumberResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Pick List Created Successfully";
				customerOrderNumberResponse.setMsg(msg);
			} else {
				transaction.rollback();
				session.close();
				customerOrderNumberResponse.setStatusCode(statusCode);
				customerOrderNumberResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			customerOrderNumberResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			customerOrderNumberResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			customerOrderNumberResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return customerOrderNumberResponse;
	}

	public Integer updateInOrderStatus(BigInteger refDocId, PicklistRequest picklistRequest, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
		}
		Integer row = 0;
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
			BigInteger userId = null;

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);

			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				String updateQuery = "exec PA_Update_Status_In_Order_For_PickList " + " :refDocId, " + " :poHdrId, "
						+ " :customerHdrId," + " :UserCode ";

				query = session.createSQLQuery(updateQuery);
				query.setParameter("refDocId", refDocId);
				query.setParameter("poHdrId", picklistRequest.getPoHdrId());
				query.setParameter("customerHdrId", picklistRequest.getCustomerOrderId());
				query.setParameter("UserCode", userCode);
				row = query.executeUpdate();

			}

			logger.info("row " + row);
			if (isSuccess) {
				transaction.commit();
				session.close();
			} else {
				transaction.commit();
				session.close();
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
		}
		return row;
	}

	private BigInteger saveCounterSaleData(Session session, PicklistRequest picklistRequest, String userCode) {
		CounterSaleEntity counterSaleEntity = new CounterSaleEntity();

		counterSaleEntity.setCustomerName(picklistRequest.getCustomerName());
		counterSaleEntity.setMobileNo(picklistRequest.getMobileNo());
		counterSaleEntity.setPinId(picklistRequest.getPinId());
		counterSaleEntity.setCreatedBy(userCode);
		counterSaleEntity.setCreatedDate(new Date());

		return (BigInteger) session.save(counterSaleEntity);
	}

	private boolean savePickListDtl(Session session, PicklistRequest picklistRequest, BigInteger id, String userCode,
			BigInteger userId) {
		boolean isSuccess = true;

		BigInteger referenceDocId = picklistRequest.getReferenceDocumentId();

		boolean isCustomerOrder = referenceDocId.equals(BigInteger.valueOf(2));

		boolean isPoOrder = referenceDocId.equals(BigInteger.valueOf(4)) || referenceDocId.equals(BigInteger.valueOf(5))
				|| referenceDocId.equals(BigInteger.valueOf(6));

		for (PartDetailRequest partDetailRequest : picklistRequest.getPartDetails()) {
			String orderFlag = isPoOrder ? "PO" : isCustomerOrder ? "CO" : null;

			for (BranchSpareIssueBinStockResponse binRequest : partDetailRequest.getBinRequest()) {

				PickListDtlEntity pickListDtlEntity = new PickListDtlEntity();

				pickListDtlEntity.setPickListHdrId(id);
				pickListDtlEntity.setPartId(partDetailRequest.getPartId());
				pickListDtlEntity.setPartBranchId(partDetailRequest.getPartBranchId());
				pickListDtlEntity.setStockBinId(binRequest.getBinId());
				pickListDtlEntity.setMRP(partDetailRequest.getMrp());
				Character individualBin = 'N';
				if (partDetailRequest.getIsIndividualBin()) {
					individualBin = 'Y';
				}

				pickListDtlEntity.setIsIndividualBin(individualBin);
				pickListDtlEntity.setIssueQty(partDetailRequest.getIsIndividualBin() ? partDetailRequest.getIssueQty()
						: binRequest.getIssueQty());

				pickListDtlEntity.setOrderQty(partDetailRequest.getOrderQty());
//				pickListDtlEntity.setIssueQty(binRequest.getIssueQty());
				pickListDtlEntity.setBalanceQty(partDetailRequest.getBalanceQty());
				pickListDtlEntity.setCreatedBy(userCode);
				pickListDtlEntity.setCreatedDate(new Date());

				pickListDtlEntity.setPoDtlId(partDetailRequest.getPoDtlId());
				pickListDtlEntity.setCoDtlId(partDetailRequest.getCustomerOrderDtlId());

				BigInteger pickListDtlId = (BigInteger) session.save(pickListDtlEntity);
			}
			updateIssueQtyForCustomerOrder(session, partDetailRequest, orderFlag, userId);

		}
		return isSuccess;
	}

	public String generateDocNumber(BigInteger branchId, String docType) {
		Session session = null;
		SpareGrnResponse spareGrnResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());

		Query query = null;
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DocumentType", docType);
			query.setParameter("BranchID", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnResponse = new SpareGrnResponse();
					spareGrnResponse.setGrnNumber((String) row.get("Doc_number"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnResponse.getGrnNumber();
	}

	private void updateDocumentNumber(String grnNumber, String documentTypeDesc, BigInteger branchId,
			String documentType, Session session) {
		String lastDocumentNumber = grnNumber.substring(grnNumber.length() - 7);
		logger.info(lastDocumentNumber);
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

	private void updatePickListFlag(Session session, BigInteger customerId, BigInteger userId) {

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = dateFormat1.format(new java.util.Date());

		String updateQuery = "update PA_Customer_Order_Hdr set is_pickList_created = 'Y'" + ", modifiedBy = " + userId
				+ ", ModifiedDate = " + currentDate + " where Customer_Id = " + customerId;

		Query query = session.createSQLQuery(updateQuery);
		query.executeUpdate();
	}

	private void updateStockForIssue(Session session, PartDetailRequest partDetailRequest, BigInteger branchId,
			String userCode) {

		updateStockInPartBranchAndStockStore(session, partDetailRequest, branchId, partDetailRequest.getBranchStoreId(),
				userCode);
	}

	private void updateStockInPartBranchAndStockStore(Session session, PartDetailRequest partDetailRequest,
			BigInteger branchId, Integer branchStoreId, String userCode) {
		if (partDetailRequest.getIssueQty() != null
				&& partDetailRequest.getPartBranchId().compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Part_Branch " + ":flag ," + ":branchId," + ":partBranchId,"
					+ ":branchStoreId, " + null + ", " + ":issueQty, " + ":basicUnitPrice," + ":modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", partDetailRequest.getPartBranchId());
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("basicUnitPrice", partDetailRequest.getBasicUnitPrice());
			query.setParameter("issueQty", partDetailRequest.getIssueQty());
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();

			for (BranchSpareIssueBinStockResponse binRequest : partDetailRequest.getBinRequest()) {
				updateStockInStockBin(session, binRequest, branchId, partDetailRequest.getBranchStoreId(), userCode);
			}
		}
	}

	private void updateStockInStockBin(Session session, BranchSpareIssueBinStockResponse binRequest,
			BigInteger branchId, Integer branchStoreId, String userCode) {

		if (binRequest != null && BigInteger.valueOf(binRequest.getPartBranchId()).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "exec PA_Update_Stock_In_Stock_Bin " + " SUBTRACT ," + ":branchId," + ":partBranchId, "
					+ ":branchStoreId," + ":stockBinId, " + null + ", " + ":issueQty, " + ":basicUnitPrice,"
					+ ":modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", binRequest.getPartBranchId());
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("stockBinId", binRequest.getBinId());
			query.setParameter("basicUnitPrice", binRequest.getUnitPrice());
			query.setParameter("issueQty", binRequest.getIssueQty());
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}

	private void updateIssueQtyForCustomerOrder(Session session, PartDetailRequest partDetailRequest, String orderFlag,
			BigInteger userId) {
		if (partDetailRequest != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			Boolean isPickListCreated = (partDetailRequest.getOrderQty().compareTo(partDetailRequest.getIssueQty()) > 0
					? true
					: false);
			String updateQuery = "exec [PA_SAVE_PICKLIST_DATA] " + ":userCode," + ":issueQty," + ":BalanceQty,"
					+ ":CustomerOrderDtlId, :poDtlId, :orderFlag";

//			String updateQuery = "update PA_Customer_Order_DTL set Issue_QTY = " + partDetailRequest.getIssueQty()
//					+ ", Balance_Qty = " + partDetailRequest.getOrderQty().subtract(partDetailRequest.getIssueQty())
//					+ ", modifiedBy = " + userId + ", ModifiedDate = " + currentDate + "where id = "
//					+ partDetailRequest.getCustomerOrderDtlId();

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("userCode", userId);
			query.setParameter("issueQty", partDetailRequest.getIssueQty());
			query.setParameter("BalanceQty", partDetailRequest.getOrderQty().subtract(partDetailRequest.getIssueQty()));
			query.setParameter("CustomerOrderDtlId", partDetailRequest.getCustomerOrderDtlId());
			query.setParameter("poDtlId", partDetailRequest.getPoDtlId());
			query.setParameter("orderFlag", orderFlag);

			query.executeUpdate();
		}
	}

	@Override
	public SpareCustOrderForPickListResponse getCustomerOrderById(String userCode, Integer customerOrderId) {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		boolean isSuccess = true;
		Integer rowCount = 0;
		SpareCustOrderForPickListResponse responseModel = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_VIEW_CUSTOMER_ORDER] :customerOrderId, :Flag ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerOrderId", customerOrderId);
			query.setParameter("Flag", "HEADER");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SpareCustOrderForPickListResponse();
					responseModel.setId((Integer) row.get("Customer_Id"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
					responseModel.setCustOrderStatus((String) row.get("Customer_Order_Status"));
					responseModel.setBranchId((BigInteger) row.get("Branch_id"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setPartyCategoryName((String) row.get("Party_Type"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPo_Category_Desc((String) row.get("ProductCategory"));
					responseModel.setCustomDate((Date) row.get("Customer_Order_Date"));
					responseModel.setCity((String) row.get("CityDesc"));
				}
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setMsg("Customer order details have been fetch successfully.");

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public List<SpareCustOrderPartForPickListResponse> getCustomerOrderDetailsById(String userCode,
			Integer customerOrderId) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		boolean isSuccess = true;
		Integer rowCount = 0;
		SpareCustOrderPartForPickListResponse partDetail = null;
		List<SpareCustOrderPartForPickListResponse> response = new ArrayList<SpareCustOrderPartForPickListResponse>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_VIEW_CUSTOMER_ORDER_FOR_PICKLIST] :customerOrderId, :userCode ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerOrderId", customerOrderId);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;

					// Added Part Details

					partDetail = new SpareCustOrderPartForPickListResponse();
					partDetail.setId((Integer) row.get("Id"));
					partDetail.setPartId((Integer) row.get("part_id"));
					partDetail.setPartBranchId((Integer) row.get("partBranch_id"));
					partDetail.setPartNo((String) row.get("PartNumber"));
					partDetail.setPartDesc((String) row.get("PartDesc"));
					partDetail.setProductSubCategory((String) row.get("PA_SUBCATEGORY_DESC"));
					partDetail.setOrderQty((Integer) row.get("Order_Qty"));
					partDetail.setBinlocation((String) row.get("StoreBinLocation"));
					partDetail.setCurrentStock((Integer) row.get("totalStock"));
					partDetail.setStore((String) row.get("FromStore"));
					partDetail.setBasicUnitPrice((BigDecimal) row.get("BasicUnitPrice"));

					response.add(partDetail);
				}
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
		}
		return response;
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
	public List<PartNumberDetails> fetchPartNumberForPickList(Integer partId, String userCode, Integer branchId,
			Integer poHdrId, Integer coId, Integer refDocId, String flag) {
		Session session = null;
		PartNumberDetails partNumberDetails = null;
		List<PartNumberDetails> partNumberDetailsList = new ArrayList<PartNumberDetails>();;
		Query query = null;
		String msg = null;

		String sqlQuery = "exec [PA_Get_Part_Number_For_Picklist] :partId, :userCode, :branchId, :poHdrId,"
				+ " :COId, :RefDocId, :flag";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partId", BigInteger.valueOf(partId));
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", branchId);
			query.setParameter("poHdrId", poHdrId);
			query.setParameter("COId", coId);
			query.setParameter("RefDocId", refDocId);
			query.setParameter("flag", flag);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("Message");
					partNumberDetails = new PartNumberDetails();

//					if (msg != null && msg.equalsIgnoreCase("No Stock Available For This Dealer")) {
//						partNumberDetails.setMessage(msg);
//						partNumberDetails.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//					} else {
					partNumberDetails.setPoDtlId((BigInteger) row.get("po_dtl_id"));
					partNumberDetails.setPartId((Integer) row.get("part_id"));
					partNumberDetails.setPartBranchId((Integer) row.get("partBranch_id"));
					partNumberDetails.setPartNumber((String) row.get("PartNumber"));
					partNumberDetails.setPartDesc((String) row.get("PartDesc"));
					partNumberDetails.setPartSubCategory((String) row.get("PartSubCategory"));
//					partNumberDetails.setMrp((BigDecimal) row.get("mrp"));
					partNumberDetails.setMrp(commonDao.fetchMRPList((Integer) row.get("part_id"), userCode));
					partNumberDetails.setDiscount((BigDecimal) row.get("discount"));
//					partNumberDetails.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					partNumberDetails.setTotalStock((Integer) row.get("totalStock"));
					partNumberDetails.setTotalValue((BigDecimal) row.get("totalValue"));
					partNumberDetails.setFromStore((String) row.get("FromStore"));
					partNumberDetails.setStoreBinLocation((String) row.get("StoreBinLocation"));
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
	public ApiResponse<List<PickListResponse>> fetchPickListDetails(String picklistNumber, String coNo, String poNumber,
			Date fromDate, Date toDate, String userCode, Integer page, Integer size) {
		Session session = null;
		ApiResponse<List<PickListResponse>> apiResponse = null;

		List<PickListResponse> pickListResponseList = null;
		PickListResponse pickListResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String dateToString = null;
		String fromDateToString = dateFormat1.format(fromDate);
		String toDateToString = dateFormat1.format(toDate);
		Integer dataCount = 0;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Pick_List] :picklistNumber, :coNo, :poNumber, " + "'" + fromDateToString + "','"
				+ toDateToString + "'," + " :userCode, :page, :size, "
				+ ":pcId, :hoId, :zoneId, :stateId, :territoryId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("picklistNumber", picklistNumber.equalsIgnoreCase("null") ? null : picklistNumber);
			query.setParameter("coNo", coNo.equalsIgnoreCase("null") ? null : coNo);
			query.setParameter("poNumber", poNumber.equalsIgnoreCase("null") ? null : poNumber);
			query.setParameter("userCode", userCode);
			query.setParameter("page", page);
			query.setParameter("size", size);
			query.setParameter("pcId", null);
			query.setParameter("hoId", null);
			query.setParameter("zoneId", null);
			query.setParameter("stateId", null);
			query.setParameter("territoryId", null);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				pickListResponseList = new ArrayList<PickListResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					pickListResponse = new PickListResponse();

					pickListResponse.setId((BigInteger) row.get("Pick_List_Hdr_Id"));
					pickListResponse.setPickListNumber((String) row.get("Pick_List_Number"));
					pickListResponse.setPickListDate(parseDateInStringFormat((Date) row.get("PickListDate")));
					pickListResponse.setReferenceDocument((String)row.get("referenceDocument"));
					pickListResponse.setStatus((String) row.get("Pick_List_Status"));
					pickListResponse.setCustomerName((String) row.get("Customer_Name"));
					pickListResponse.setMobileNo((String) row.get("Mobile_No"));
					pickListResponse.setPinCode((String)row.get("PinCode"));
					pickListResponse.setPostOffice((String)row.get("LocalityName"));
					pickListResponse.setCity((String)row.get("CityDesc"));
					pickListResponse.setTehsil((String)row.get("TehsilDesc"));
					pickListResponse.setDistrict((String)row.get("DistrictDesc"));
					pickListResponse.setState((String)row.get("State"));
					pickListResponse.setCountry((String)row.get("Country"));
					pickListResponse.setPoNo((String) row.get("PONumber"));
					pickListResponse.setPoDate(parseDateInStringFormat((Date) row.get("POCreationDate")));
					pickListResponse.setCoNo((String) row.get("Customer_Order_Number"));
					pickListResponse.setCoDate(parseDateInStringFormat((Date) row.get("Customer_Order_Date")));
					dataCount = (Integer) row.get("totalCount");

					pickListResponseList.add(pickListResponse);
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
			if (pickListResponseList != null) {
				apiResponse = new ApiResponse<>();
				apiResponse.setCount(dataCount);
				apiResponse.setResult(pickListResponseList);
				apiResponse.setMessage("PickList Details Search Successfully.");
				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
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

	@Override
	public PickListResponse fetchHdrAndDtl(int pickListHdrId) {
		PickListResponse pickListResponse = null;

		pickListResponse = fetchHdrDetails(pickListHdrId);
		pickListResponse.setPartNumberDetailResponse(fetchPartDetails(pickListHdrId));
		return pickListResponse;
	}

	private PickListResponse fetchHdrDetails(int pickListHdrId) {
		Session session = null;
		PickListResponse pickListResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Pick_List_Details] :pickListHdrId, :flag";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pickListHdrId", pickListHdrId);
			query.setParameter("flag", "Header");

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					pickListResponse = new PickListResponse();
					pickListResponse.setId((BigInteger) row.get("Pick_List_Hdr_Id"));
					pickListResponse.setPickListNumber((String) row.get("Pick_List_Number"));
					pickListResponse.setPickListDate(parseDateInStringFormat((Date) row.get("PickListDate")));
					pickListResponse.setStatus((String) row.get("Pick_List_Status"));
					pickListResponse.setReferenceDocument((String) row.get("referenceDocument"));
					pickListResponse.setCustomerName((String) row.get("Customer_Name"));
					pickListResponse.setMobileNo((String) row.get("Mobile_No"));
					pickListResponse.setPoNo((String) row.get("PONumber"));
					pickListResponse.setPoDate(parseDateInStringFormat((Date) row.get("POCreationDate")));
					pickListResponse.setCoNo((String) row.get("Customer_Order_Number"));
					pickListResponse.setCoDate(parseDateInStringFormat((Date) row.get("Customer_Order_Date")));
					pickListResponse.setPinCode((String) row.get("PinCode"));
					pickListResponse.setPostOffice((String) row.get("PostOffice"));
					pickListResponse.setCity((String) row.get("CityDesc"));
					pickListResponse.setTehsil((String) row.get("TehsilDesc"));
					pickListResponse.setDistrict((String) row.get("DistrictDesc"));
					pickListResponse.setState((String) row.get("StateDesc"));

				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return pickListResponse;
	}

	public List<PickListPartDetailResponse> fetchPartDetails(int pickListHdrId) {
		Session session = null;
//		List<PartNumberDetailResponse> partDetailResponsList = null;
//		PartNumberDetailResponse partDetailResponse = null;
		List<PickListPartDetailResponse> partDetailResponsList = null;
		PickListPartDetailResponse partDetailResponse = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Pick_List_Details] :pickListHdrId, :flag";

		try {

			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pickListHdrId", pickListHdrId);
			query.setParameter("flag", "Details");

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponsList = new ArrayList<PickListPartDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;

					partDetailResponse = new PickListPartDetailResponse();
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setMrp((BigDecimal) row.get("MRP"));
					partDetailResponse.setOrderQty((BigDecimal) row.get("Order_Qty"));
					partDetailResponse.setBalanceQty((BigDecimal) row.get("Balance_Qty"));
					partDetailResponse.setIssueQty((BigDecimal) row.get("Issue_Qty"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					partDetailResponse.setStoreDesc((String) row.get("StoreDesc"));
					partDetailResponse.setBinLocation((String) row.get("BinName"));
					partDetailResponse.setAvailableQty((Integer) row.get("OnHandQty"));
//					partDetailResponse.setBasicUnitPrice(basicUnitPrice);
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
	public HashMap<BigInteger, String> searchPickListNumber(String searchText, int counterSaleId, String userCode) {
		Session session = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_Search_PickList_Number :searchText, :counterSaleId, :UserCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("searchText", searchText);
			query.setParameter("counterSaleId", counterSaleId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;

					searchList.put((BigInteger) row.get("Pick_List_Hdr_Id"), (String) row.get("Pick_List_Number"));
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
	@Transactional
	public void updateDcQty(List<DeliveryChallanPartDetailRequest> dcPartDetailList, String userCode) {
		Transaction transaction = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (!dcPartDetailList.isEmpty()) {
				for (DeliveryChallanPartDetailRequest bean : dcPartDetailList) {
					PickListDtlEntity entity = session.get(PickListDtlEntity.class, bean.getPickListDtlId());
					if (entity != null) {
						if (entity.getDcIssueQty() != null && entity.getDcIssueQty() > 0) {
							entity.setDcIssueQty(entity.getDcIssueQty() + bean.getDcQty().intValue());
						} else {
							entity.setDcIssueQty(bean.getDcQty().intValue());
						}
						session.update(entity);

					}
				}
			}
		} catch (Exception e) {
			logger.error("Enable to update dcQty inside pick list detail table", e);
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				transaction.commit();
			}

		}
	}

	@Override
	public List<String> getAllPicListNo(String searchText, String userCode) {
		
		Session session = null;
		String grnNumber = null;
		List<String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_SEARCH_PICKLIST] :searchText, :UserCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					 
						searchList.add((String) row.get("Pick_List_Number"));

						
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		System.out.println("before Return PickList"+searchList);
		return searchList;

	}

}
