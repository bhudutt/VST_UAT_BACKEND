/**
 * 
 */
package com.hitech.dms.web.dao.indent.view;

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

import com.hitech.dms.web.model.indent.view.request.IndentViewRequestModel;
import com.hitech.dms.web.model.indent.view.response.IndentDtlViewResponseModel;
import com.hitech.dms.web.model.indent.view.response.IndentItemViewResponseModel;
import com.hitech.dms.web.model.indent.view.response.IndentViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IndentViewDaoImpl implements IndentViewDao {
	private static final Logger logger = LoggerFactory.getLogger(IndentViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public IndentViewResponseModel fetchIndentView(String userCode, IndentViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIndentView invoked.." + requestModel.toString());
		}
		logger.info("fetchIndentView : " + requestModel.toString());
		Session session = null;
		IndentViewResponseModel responseModel = null;
		List<IndentDtlViewResponseModel> indentDtlList;
		List<IndentItemViewResponseModel> indentItemList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchIndentView(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new IndentViewResponseModel();
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
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setIndentBy((BigInteger) row.get("IndentBy"));
					responseModel.setIndentByName((String) row.get("IndentByName"));
					responseModel.setIndentDate((String) row.get("IndentDate"));
					responseModel.setIndentId((BigInteger) row.get("IndentId"));
					responseModel.setIndentNumber((String) row.get("IndentNumber"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setIndentToBranch((String) row.get("IndentToBranch"));
					responseModel.setIndentToBranchId((BigInteger) row.get("IndentToBranchId"));
				}
				requestModel.setFlag(2);
				data = fetchIndentView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					indentDtlList = new ArrayList<IndentDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						IndentDtlViewResponseModel machDtl = new IndentDtlViewResponseModel();
						machDtl.setIndentDtlId((BigInteger) row.get("IndentDtlId"));
						machDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						machDtl.setItemNo((String) row.get("ItemNo"));
						machDtl.setItemDesc((String) row.get("ItemDesc"));
						machDtl.setModel((String) row.get("Model"));
						machDtl.setVariant((String) row.get("Variant"));
						machDtl.setIndentQty((Integer) row.get("IndentQty"));
						machDtl.setIssueQty((Integer) row.get("IssueQty"));
						machDtl.setPendingQty((Integer) row.get("PendingQty"));

						indentDtlList.add(machDtl);
					}
					responseModel.setIndentDtlList(indentDtlList);
				}
				requestModel.setFlag(3);
				data = fetchIndentView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					indentItemList = new ArrayList<IndentItemViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						IndentItemViewResponseModel itemDtl = new IndentItemViewResponseModel();
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
	public List fetchIndentView(Session session, String userCode, IndentViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIndentView invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_TR_INDENT_VIEW] :userCode, :indentId, :flag";
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
