/**
 * 
 */
package com.hitech.dms.web.dao.dealer.assign.branch.dtl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.dealer.branchassign.view.request.AssignUserToBranchDTLRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.view.response.AssignUserToBranchDTLResponseModel;
import com.hitech.dms.web.model.dealer.branchassign.view.response.AssignUserToBranchListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AssignUserToBranchDTLDaoImpl implements AssignUserToBranchDTLDao {
	private static final Logger logger = LoggerFactory.getLogger(AssignUserToBranchDTLDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public AssignUserToBranchDTLResponseModel fetchAssignUserToBranchDTL(String userCode,
			AssignUserToBranchDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAssignUserToBranchDTL invoked..");
		}
		Session session = null;
		Query query = null;
		AssignUserToBranchDTLResponseModel responseModel = null;
		String sqlQuery = "Select ABD.ParentDealerName, ABD.ParentDealerCode, ABD.ParentDealerLocation, EMP.dealer_Id, "
				+ " EMP.EmpCode, ISNULL(stuff(coalesce(' '+nullif(EMP.firstname, ''), '') + coalesce(' '+nullif(EMP.middlename, ''), '') + coalesce(' '+nullif(EMP.lastname, ''), ''),1,1,''),'') as EmpName "
				+ " from ADM_BP_DEALER_EMP (nolock) EMP "
				+ " INNER JOIN ADM_BP_DEALER (nolock) ABD ON EMP.dealer_id = ABD.parent_dealer_id "
				+ " where EMP.emp_id =:empId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("empId", requestModel.getEmpId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModel = new AssignUserToBranchDTLResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setDealerId((BigInteger) row.get("dealer_Id"));
					responseModel.setEmpId(requestModel.getEmpId());
					responseModel.setEmpCode((String) row.get("EmpCode"));
					responseModel.setDealerCode((String) row.get("ParentDealerCode"));
					responseModel.setDealerName((String) row.get("ParentDealerName"));
					responseModel.setDealerLocation((String) row.get("ParentDealerLocation"));
					responseModel.setEmpName((String) row.get("EmpName"));

				}
				List<AssignUserToBranchListResponseModel> responseModelList = fetchBranchList(session, userCode,
						requestModel);
				responseModel.setBranchList(responseModelList);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<AssignUserToBranchListResponseModel> fetchBranchList(Session session, String userCode,
			AssignUserToBranchDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAssignUserToBranchDTL invoked..");
		}
		Query query = null;
		List<AssignUserToBranchListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_DLR_GET_EMP_DLRBRANCHES] :userCode, :dealerId, :empId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("empId", requestModel.getEmpId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<AssignUserToBranchListResponseModel>();
				AssignUserToBranchListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new AssignUserToBranchListResponseModel();
					responseModel.setBranchId((BigInteger) row.get("BranchId"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setBranchEmpId((BigInteger) row.get("BranchEmpId"));
					responseModel.setEmpId((BigInteger) row.get("EmpId"));

					Character isActive = (Character) row.get("IsActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						responseModel.setIsActive(true);
					} else {
						responseModel.setIsActive(false);
					}
					Character isBranchPrimary = (Character) row.get("IsBranchPrimary");
					if (isBranchPrimary != null && isBranchPrimary.toString().equals("Y")) {
						responseModel.setIsBranchPrimary(true);
					} else {
						responseModel.setIsBranchPrimary(false);
					}

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return responseModelList;
	}
}
