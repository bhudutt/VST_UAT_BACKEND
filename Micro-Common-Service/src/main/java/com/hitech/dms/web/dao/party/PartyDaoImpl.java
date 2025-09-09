/**
 * 
 */
package com.hitech.dms.web.dao.party;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.party.request.FinancePartyByBranchRequestModel;
import com.hitech.dms.web.model.party.request.FinancePatyByPartyCodeRequestModel;
import com.hitech.dms.web.model.party.request.PartyDTLRequestModel;
import com.hitech.dms.web.model.party.request.PartyForInvoiceRequestModel;
import com.hitech.dms.web.model.party.request.PartyListRequestModel;
import com.hitech.dms.web.model.party.response.FinancePartyByBranchResponseModel;
import com.hitech.dms.web.model.party.response.FinancePatyByPartyCodeResponseModel;
import com.hitech.dms.web.model.party.response.PartyDTLResponseModel;
import com.hitech.dms.web.model.party.response.PartyListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PartyDaoImpl implements PartyDao {
	private static final Logger logger = LoggerFactory.getLogger(PartyDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<FinancePartyByBranchResponseModel> fetchFinancePatyList(String userCode, BigInteger branchID,
			String code) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchFinancePatyList invoked.." + userCode);
		}
		Session session = null;
		List<FinancePartyByBranchResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchFinancePatyList(session, userCode, branchID, code);
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<FinancePartyByBranchResponseModel> fetchFinancePatyList(Session session, String userCode,
			BigInteger branchID, String code) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchFinancePatyList invoked.." + userCode);
		}
		Query query = null;
		List<FinancePartyByBranchResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_BP_getParty] :branchID, :code";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branchID", branchID);
			query.setParameter("code", code);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<FinancePartyByBranchResponseModel>();
				FinancePartyByBranchResponseModel salesmanModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					salesmanModel = new FinancePartyByBranchResponseModel();
					salesmanModel.setFinancePartyId((BigInteger) row.get("financier_party_id"));
					salesmanModel.setPartyCode((String) row.get("PartyCode"));
					salesmanModel.setPartyName((String) row.get("PartyName"));
					salesmanModel.setPartLocation((String) row.get("Party_Location"));
					salesmanModel.setDisplayName((String) row.get("displayName"));
					responseModelList.add(salesmanModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<FinancePartyByBranchResponseModel> fetchFinancePatyList(String userCode,
			FinancePartyByBranchRequestModel financePartyByBranchRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchFinancePatyList invoked.." + financePartyByBranchRequestModel.toString());
		}
		Session session = null;
		List<FinancePartyByBranchResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchFinancePatyList(session, userCode, financePartyByBranchRequestModel.getBranchID(),
					financePartyByBranchRequestModel.getCode());
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@Override
	public List<FinancePartyByBranchResponseModel> fetchPatyList(String userCode,
			FinancePartyByBranchRequestModel financePartyByBranchRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPatyList invoked.." + financePartyByBranchRequestModel.toString());
		}
		Session session = null;
		List<FinancePartyByBranchResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchFinancePatyList(session, userCode, financePartyByBranchRequestModel.getBranchID(),
					financePartyByBranchRequestModel.getCode());
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PartyListResponseModel> fetchPatyListForInvoice(String userCode, PartyForInvoiceRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPatyListForInvoice invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<PartyListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_BP_getPartyListForInvoice] :userCode, :pcId, :dealerId, :branchId, :searchValue, :invTypeId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("searchValue", requestModel.getSearchValue());
			query.setParameter("invTypeId", requestModel.getInvTypeId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PartyListResponseModel>();
				PartyListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyListResponseModel();
					responseModel.setId((BigInteger) row.get("partyId"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setDisplayValue((String) row.get("diaplayValue"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PartyListResponseModel> fetchPatyList(String userCode, PartyListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPatyList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<PartyListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_BP_getPartyList] :userCode, :pcId, :dealerId, :branchId, :searchValue, :poOnId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("searchValue", requestModel.getSearchValue());
			query.setParameter("poOnId", requestModel.getPoOnId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PartyListResponseModel>();
				PartyListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyListResponseModel();
					responseModel.setId((BigInteger) row.get("partyId"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setDisplayValue((String) row.get("diaplayValue"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PartyDTLResponseModel fetchPatyDTL(String userCode, PartyDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPatyDTL invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		PartyDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SAORD_GETPARTYDTL] :userCode, :pcId, :dealerId, :branchId, :poOnId, :partyId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("poOnId", requestModel.getPoOnId());
			query.setParameter("partyId", requestModel.getPartyId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyDTLResponseModel();
					responseModel.setId((BigInteger) row.get("partyId"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setDisplayValue((String) row.get("diaplayValue"));
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public FinancePatyByPartyCodeResponseModel fetchFinancePatyDTL(String userCode,
			FinancePatyByPartyCodeRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchFinancePatyDTL invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		FinancePatyByPartyCodeResponseModel responseModel = null;
		String sqlQuery = "exec [SP_CM_GETPARTYDTL] :userCode, :pcId, :dealerId, :branchId, :partyId, :partyCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("partyId", requestModel.getPartyId());
			query.setParameter("partyCode", requestModel.getPartyCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new FinancePatyByPartyCodeResponseModel();
					responseModel.setId((BigInteger) row.get("partyId"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setDisplayValue((String) row.get("diaplayValue"));
					responseModel.setCountry((String) row.get("Country"));
					responseModel.setDistrict((String) row.get("District"));
					responseModel.setPartyAddress1((String) row.get("PartyAddress1"));
					responseModel.setPartyAddress2((String) row.get("PartyAddress2"));
					responseModel.setPartyAddress3((String) row.get("PartyAddress3"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setEmailId((String) row.get("EmailId"));
					responseModel.setPincode((String) row.get("Pincode"));
					responseModel.setPinId((BigInteger) row.get("PinId"));
					responseModel.setCity((String) row.get("City"));
					responseModel.setState((String) row.get("State"));
					responseModel.setTehsil((String) row.get("Tehsil"));
					responseModel.setVillage((String) row.get("Village"));
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
