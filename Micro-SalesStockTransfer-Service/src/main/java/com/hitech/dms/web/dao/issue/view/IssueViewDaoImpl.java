/**
 * 
 */
package com.hitech.dms.web.dao.issue.view;

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

import com.hitech.dms.web.model.issue.view.request.IssueViewRequestModel;
import com.hitech.dms.web.model.issue.view.response.IssueDtlViewResponseModel;
import com.hitech.dms.web.model.issue.view.response.IssueItemViewResponseModel;
import com.hitech.dms.web.model.issue.view.response.IssueViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IssueViewDaoImpl implements IssueViewDao {
	private static final Logger logger = LoggerFactory.getLogger(IssueViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public IssueViewResponseModel fetchIssueView(String userCode, IssueViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIssueView invoked.." + requestModel.toString());
		}
		logger.info("fetchIssueView : " + requestModel.toString());
		Session session = null;
		IssueViewResponseModel responseModel = null;
		List<IssueDtlViewResponseModel> issueDtlList;
		List<IssueItemViewResponseModel> issueItemList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchIssueView(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new IssueViewResponseModel();
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
					responseModel.setIssueId((BigInteger) row.get("IssueId"));
					responseModel.setIssueNumber((String) row.get("IssueNumber"));
					responseModel.setIssueBy((BigInteger) row.get("IssueBy"));
					responseModel.setIssueByName((String) row.get("IssueByName"));
					responseModel.setIssueDate((String) row.get("IssueDate"));
					responseModel.setIndentDate((String) row.get("IndentDate"));
					responseModel.setIndentId((BigInteger) row.get("IndentId"));
					responseModel.setIndentNumber((String) row.get("IndentNumber"));
					responseModel.setIndentByName((String) row.get("IndentByName"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setToBranch((String) row.get("ToBranch"));
					responseModel.setToBranchId((BigInteger) row.get("ToBranchId"));
					responseModel.setTotalIssuedQty((Integer) row.get("TotalIssuedQty"));
				}
				requestModel.setFlag(2);
				data = fetchIssueView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					issueDtlList = new ArrayList<IssueDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						IssueDtlViewResponseModel machDtl = new IssueDtlViewResponseModel();
						machDtl.setIssueDtlId((BigInteger) row.get("IssueDtlId"));
						machDtl.setIndentDtlId((BigInteger) row.get("IndentDtlId"));
						machDtl.setMachineInventoryId((BigInteger) row.get("MachineInventoryId"));
						machDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						machDtl.setItemNo((String) row.get("ItemNo"));
						machDtl.setItemDesc((String) row.get("ItemDesc"));
						machDtl.setModel((String) row.get("Model"));
						machDtl.setVinId((BigInteger) row.get("VinId"));
						machDtl.setChassisNo((String) row.get("ChassisNo"));
						machDtl.setVariant((String) row.get("Variant"));
	
						issueDtlList.add(machDtl);
					}
					responseModel.setIssueDtlList(issueDtlList);
				}

				requestModel.setFlag(3);
				data = fetchIssueView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					issueItemList = new ArrayList<IssueItemViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						IssueItemViewResponseModel itemDtl = new IssueItemViewResponseModel();
						itemDtl.setIssueItemId((BigInteger) row.get("IssueItemId"));
						itemDtl.setIndentDtlId((BigInteger) row.get("IndentDtlId"));
						itemDtl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						itemDtl.setItemNo((String) row.get("ItemNo"));
						itemDtl.setItemDesc((String) row.get("ItemDesc"));
						itemDtl.setIssueQty((Integer) row.get("IssueQty"));

						issueItemList.add(itemDtl);
					}
					responseModel.setIssueItemList(issueItemList);
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
	public List fetchIssueView(Session session, String userCode, IssueViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIssueView invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_TR_ISSUE_VIEW] :userCode, :issueId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("issueId", requestModel.getIssueId());
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
