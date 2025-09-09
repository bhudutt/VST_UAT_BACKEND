package com.hitech.dms.web.dao.spare.sale.invoiceCancel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Repository;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;

@Repository
public class SpareSaleInvoiceCancelDaoImpl implements SpareSaleInvoiceCancelDao {
	private static final Logger logger = LoggerFactory.getLogger(SpareSaleInvoiceCancelDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	CommonDao commonDao;

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
	public SpareSalesInvoiceResponse spareCancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(saleInvoiceCancelRequest.toString());
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
				String updateQuery = "update PA_SALE_INVOICE set isCancelled = 'Y' , "
						+ " Cancellation_Remarks  = :remarks," + "ModifiedBy = :userCode, ModifiedDate = :ModifiedDate "
						+ " where invoice_sale_id = :invoiceSaleId ";

				query = session.createSQLQuery(updateQuery);
				query.setParameter("invoiceSaleId", saleInvoiceCancelRequest.getInvoiceSaleId());
				query.setParameter("remarks", saleInvoiceCancelRequest.getCancelRemarks());
				query.setParameter("ModifiedDate", todayDate);
				query.setParameter("userCode", userCode);
				query.executeUpdate();

			}
			BigInteger referenceDocId = saleInvoiceCancelRequest.getReferenceDocumentId();

			boolean isCustomerOrder = referenceDocId.equals(BigInteger.valueOf(2));
			boolean isDcOrder = referenceDocId.equals(BigInteger.valueOf(3));

			boolean isPoOrder = referenceDocId.equals(BigInteger.valueOf(4))
					|| referenceDocId.equals(BigInteger.valueOf(5))
					|| referenceDocId.equals(BigInteger.valueOf(6));
			

			if (isSuccess) {
				for (PartDetailRequest partDetailRequest : saleInvoiceCancelRequest.getPartDetailRequest()) {
//					commonDao.updateStockForInPartBranchAndStockBin(session, "ADD", partDetailRequest,
//							saleInvoiceCancelRequest.getBranchId(), "PA_SALE_INVOICE_CANCEL", userCode);

					if(!isDcOrder) {
						partDetailRequest = commonDao.updateStockInPartBranchAndStockStore(session, "ADD", partDetailRequest,
								saleInvoiceCancelRequest.getBranchId(), partDetailRequest.getBranchStoreId(),
								"PA_SALE_INVOICE_CANCEL", userCode);
						

						BranchSpareIssueBinStockResponse binRequest = patchBinData(partDetailRequest);
						commonDao.updateStockInStockBin(session, binRequest, saleInvoiceCancelRequest.getBranchId(),
								partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(),
								"PA_SALE_INVOICE_CANCEL", userCode, "ADD");
					}
					
					String flag = isPoOrder ? "PO" : isCustomerOrder ? "CO" : isDcOrder ? "DC" : null;

					
					String orderType = isPoOrder ? "PO" : isCustomerOrder ? "CO" : isDcOrder ? "DC" : null;

					updateInvoiceQtyInOrder(partDetailRequest, flag, userCode, userId);
					updateInvoiceQtyInPickList(session, partDetailRequest.getPickListDtlId(), partDetailRequest.getInvoiceQty(), userCode);

				}

				transaction.commit();
				session.close();
				spareSalesInvoiceResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Sapre Sale Invoice Cancelled Successfully";
				spareSalesInvoiceResponse.setMsg(msg);
			} else {
				transaction.rollback();
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
		return spareSalesInvoiceResponse;
	}
	
	private BranchSpareIssueBinStockResponse patchBinData(PartDetailRequest partDetailRequest) {
//		List<BranchSpareIssueBinStockResponse> binList = new ArrayList<BranchSpareIssueBinStockResponse>();

			BranchSpareIssueBinStockResponse binRequest = new BranchSpareIssueBinStockResponse();
			binRequest.setPartBranchId(partDetailRequest.getPartBranchId().intValue());
			binRequest.setBranchStoreId(partDetailRequest.getBranchStoreId());
			binRequest.setUnitPrice(partDetailRequest.getBasicUnitPrice());
			binRequest.setBinLocation(partDetailRequest.getBinLocation());
			binRequest.setIssueQty(partDetailRequest.getInvoiceQty());
			binRequest.setBinId(partDetailRequest.getBinId());
//			binList.add(binRequest1);

//			partDetailRequest.setBinRequest();
			return binRequest;
	}


	public String parseDateInStringFormat(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String newDate = dateFormat.format(date);
			return newDate;
		}
		return null;
	}

	public Integer updateInvoiceQtyInOrder(PartDetailRequest partDetailRequest, String orderType, String userCode,
			BigInteger userId) {
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
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			String updateQuery = "exec PA_Update_InvoiceQty_In_Order :OrderType, :flag, :InvoiceQty, "
					+ ":poDtlId, :CustomerDtlId, :DcDtlId, :PickListDtlId, :UserCode";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("OrderType", orderType);
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("InvoiceQty", partDetailRequest.getInvoiceQty());
			query.setParameter("poDtlId", partDetailRequest.getPoDtlId());
			query.setParameter("CustomerDtlId",
					orderType.equalsIgnoreCase("CO") ? partDetailRequest.getCustomerOrderDtlId() : 0);
			query.setParameter("DcDtlId",
					orderType.equalsIgnoreCase("DC") ? partDetailRequest.getCustomerOrderDtlId() : 0);
			query.setParameter("PickListDtlId", 0);
			query.setParameter("UserCode", orderType.equalsIgnoreCase("PO") ? userCode : userId);

			row = query.executeUpdate();

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
	
	public Integer updateInvoiceQtyInPickList(Session session, BigInteger pickListDtlId, BigDecimal invoiceQty,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
		}
		Integer row = 0;
		String msg = null;
		Query query = null;
		boolean isSuccess = true;

		try {
			String updateQuery = "exec PA_Update_InvoiceQty_In_Order :OrderType, :flag, :InvoiceQty, "
					+ ":poDtlId, :CustomerDtlId, :DcDtlId, :PickListDtlId, :UserCode";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("OrderType", "pickList");
			query.setParameter("flag", "SUBTRACT");
			query.setParameter("InvoiceQty", invoiceQty);
			query.setParameter("poDtlId", 0);
			query.setParameter("CustomerDtlId", 0);
			query.setParameter("DcDtlId", 0);
			query.setParameter("PickListDtlId", pickListDtlId);
			query.setParameter("UserCode", userCode);

			row = query.executeUpdate();

			logger.info("row " + row);

		} catch (SQLGrammarException ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return row;
	}
}
