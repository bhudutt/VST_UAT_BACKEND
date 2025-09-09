/**
 * 
 */
package com.hitech.dms.web.dao.receipt.view;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.stock.receipt.view.request.ReceiptViewRequestModel;
import com.hitech.dms.web.model.stock.receipt.view.response.ReceiptDtlViewResponseModel;
import com.hitech.dms.web.model.stock.receipt.view.response.ReceiptItemViewResponseModel;
import com.hitech.dms.web.model.stock.receipt.view.response.ReceiptViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ReceiptViewDaoImpl implements ReceiptViewDao {
	private static final Logger logger = LoggerFactory.getLogger(ReceiptViewDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public ReceiptViewResponseModel fetchReceiptView(String userCode, ReceiptViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchReceiptView invoked.." + requestModel.toString());
		}
		logger.info("fetchReceiptView : " + requestModel.toString());
		Session session = null;
		ReceiptViewResponseModel responseModel = null;
		List<ReceiptDtlViewResponseModel> receiptDtlList;
		List<ReceiptItemViewResponseModel> receiptItemList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchReceiptView(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new ReceiptViewResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setBranchId((BigInteger) row.get("BranchId"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setReceiptId((BigInteger) row.get("ReceiptId"));
					responseModel.setReceiptDate((String) row.get("ReceiptDate"));
					responseModel.setReceiptBy((BigInteger) row.get("ReceiptBy"));
					responseModel.setReceiptByName((String) row.get("ReceiptByName"));
					responseModel.setReceiptNumber((String) row.get("ReceiptNumber"));
					responseModel.setIssueNumber((String) row.get("IssueNumber"));
					responseModel.setIssueByName((String) row.get("IssueByName"));
					responseModel.setIssueDate((String) row.get("IssueDate"));
					responseModel.setIssueRemarks((String) row.get("IssueRemarks"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setToBranch((String) row.get("ToBranch"));
					responseModel.setToBranchId((BigInteger) row.get("ToBranchId"));
					responseModel.setTotalIssuedQty((Integer) row.get("TotalIssuedQty"));
				}
				requestModel.setFlag(2);
				data = fetchReceiptView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					receiptDtlList = new ArrayList<ReceiptDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						ReceiptDtlViewResponseModel machDtl = new ReceiptDtlViewResponseModel();
						machDtl.setReceiptDtlId((BigInteger) row.get("ReceiptDtlId"));
						machDtl.setIssueDtlId((BigInteger) row.get("IssueDtlId"));
						machDtl.setMachineInventoryId((BigInteger) row.get("MachineInventoryId"));
						machDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						machDtl.setItemNo((String) row.get("ItemNo"));
						machDtl.setItemDesc((String) row.get("ItemDesc"));
						machDtl.setModel((String) row.get("Model"));
						machDtl.setVinId((BigInteger) row.get("VinId"));
						machDtl.setChassisNo((String) row.get("ChassisNo"));
						machDtl.setVariant((String) row.get("Variant"));
						machDtl.setEngineNo((String) row.get("EngineNo"));
	
						receiptDtlList.add(machDtl);
					}
					responseModel.setReceiptDtlList(receiptDtlList);
				}

				requestModel.setFlag(3);
				data = fetchReceiptView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					receiptItemList = new ArrayList<ReceiptItemViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						ReceiptItemViewResponseModel itemDtl = new ReceiptItemViewResponseModel();
						itemDtl.setReceiptItemId((BigInteger) row.get("ReceiptItemId"));
						itemDtl.setIssueItemId((BigInteger) row.get("IssueItemId"));
						itemDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						itemDtl.setItemNo((String) row.get("ItemNo"));
						itemDtl.setItemDesc((String) row.get("ItemDesc"));
						itemDtl.setReceiptQty((Integer) row.get("ReceiptQty"));

						receiptItemList.add(itemDtl);
					}
					responseModel.setReceiptItemList(receiptItemList);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchReceiptView(Session session, String userCode, ReceiptViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchReceiptView invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_TR_RECEIPT_VIEW] :userCode, :receiptId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("receiptId", requestModel.getReceiptId());
			query.setParameter("flag", requestModel.getFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
}
