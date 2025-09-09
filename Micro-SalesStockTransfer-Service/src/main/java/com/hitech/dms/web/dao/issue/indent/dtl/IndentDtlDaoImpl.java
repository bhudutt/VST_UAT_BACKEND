/**
 * 
 */
package com.hitech.dms.web.dao.issue.indent.dtl;

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

import com.hitech.dms.web.model.issue.indent.dtl.request.IndentDtlRequestModel;
import com.hitech.dms.web.model.issue.indent.dtl.response.IndentDtlResponseModel;
import com.hitech.dms.web.model.issue.indent.dtl.response.IndentItemDtlResponseModel;
import com.hitech.dms.web.model.issue.indent.dtl.response.IndentMachDtlResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IndentDtlDaoImpl implements IndentDtlDao {
	private static final Logger logger = LoggerFactory.getLogger(IndentDtlDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public IndentDtlResponseModel fetchIndentDtlForIssue(String userCode, IndentDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIndentDtlForIssue invoked.." + requestModel.toString());
		}
		logger.info("fetchDcDetailForInvoice : " + requestModel.toString());
		Session session = null;
		IndentDtlResponseModel responseModel = null;
		List<IndentMachDtlResponseModel> indentMachineList;
		List<IndentItemDtlResponseModel> indentItemList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchIndentDtlForIssue(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new IndentDtlResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setBranchId((BigInteger) row.get("BranchId"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setIndentBy((BigInteger) row.get("IndentBy"));
					responseModel.setIndentByName((String) row.get("IndentByName"));
					responseModel.setIndentDate((String) row.get("IndentDate"));
					responseModel.setIndentId((BigInteger) row.get("IndentId"));
					responseModel.setIndentNumber((String) row.get("IndentNumber"));
					responseModel.setIndentRemarks((String) row.get("IndentRemarks"));
					responseModel.setIndentToBranch((String) row.get("IndentToBranch"));
					responseModel.setIndentToBranchId((BigInteger) row.get("IndentToBranchId"));
				}
				requestModel.setFlag(2);
				data = fetchIndentDtlForIssue(session, userCode, requestModel);
				indentMachineList = new ArrayList<IndentMachDtlResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					IndentMachDtlResponseModel machDtl = new IndentMachDtlResponseModel();
					machDtl.setIndentDtlId((BigInteger) row.get("IndentDtlId"));
//					machDtl.setIssueDtlId((BigInteger) row.get("IssueDtlId"));
					machDtl.setMachineInventoryId((BigInteger) row.get("MachineInventoryId"));
					machDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
					machDtl.setItemNo((String) row.get("ItemNo"));
					machDtl.setItemDesc((String) row.get("ItemDesc"));
					machDtl.setModel((String) row.get("Model"));
					machDtl.setVinId((BigInteger) row.get("VinId"));
					machDtl.setChassisNo((String) row.get("ChassisNo"));
					machDtl.setVariant((String) row.get("Variant"));
					machDtl.setVinNo((String) row.get("VinNo"));
					machDtl.setEngineNo((String) row.get("EngineNo"));
					machDtl.setIndentQty((Integer) row.get("IndentQty"));
					machDtl.setIssueQty((Integer) row.get("IssueQty"));
					machDtl.setPendingQty((Integer) row.get("PendingQty"));

					indentMachineList.add(machDtl);
				}
				responseModel.setIndentDtlList(indentMachineList);

				requestModel.setFlag(3);
				data = fetchIndentDtlForIssue(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					indentItemList = new ArrayList<IndentItemDtlResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						IndentItemDtlResponseModel itemDtl = new IndentItemDtlResponseModel();
						itemDtl.setIndentDtlId((BigInteger) row.get("IndentDtlId"));
						itemDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						itemDtl.setItemNo((String) row.get("ItemNo"));
						itemDtl.setItemDesc((String) row.get("ItemDesc"));
						itemDtl.setModel((String) row.get("Model"));
						itemDtl.setIndentQty((Integer) row.get("IndentQty"));
						itemDtl.setIssueQty((Integer) row.get("IssueQty"));
						itemDtl.setPendingQty((Integer) row.get("PendingQty"));

						indentItemList.add(itemDtl);
					}
					responseModel.setIndentItemList(indentItemList);
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
	public List fetchIndentDtlForIssue(Session session, String userCode, IndentDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIndentDtlForIssue invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_TR_INDENT_DTL] :userCode, :indentId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("indentId", requestModel.getIndentId());
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
