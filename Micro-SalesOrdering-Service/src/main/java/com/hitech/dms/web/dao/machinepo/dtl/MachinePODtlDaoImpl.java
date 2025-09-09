/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.dtl;

import java.math.BigDecimal;
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

import com.hitech.dms.web.model.machinepo.dtl.request.MachinePODtlRequestModel;
import com.hitech.dms.web.model.machinepo.dtl.response.MachinePODtlResponseModel;
import com.hitech.dms.web.model.machinepo.dtl.response.MachinePOHdrResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePODtlDaoImpl implements MachinePODtlDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePODtlDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public MachinePOHdrResponseModel fetchMachinePODtl(String userCode, MachinePODtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachinePODtl invoked..");
		}
		Session session = null;
		MachinePOHdrResponseModel responseModel = null;
		List<MachinePODtlResponseModel> machinePODTLList = null;
		try {
			session = sessionFactory.openSession();
			// fetch Hdr Data
			List data = fetchMachinePOData(session, userCode, requestModel.getPoHdrId(), requestModel.getPoNumber(), 1);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachinePOHdrResponseModel();
					responseModel.setPoHdrId((BigInteger) row.get("PoHdrId"));
					responseModel.setPoNumber((String) row.get("PoNumber"));
					responseModel.setPoStatus((String) row.get("PoStatus"));
					responseModel.setPoReleasedDate((String) row.get("PoReleasedDate"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setPoTypeId((Integer) row.get("PoTypeId"));
					responseModel.setPoTypeDesc((String) row.get("PoTypeDesc"));
					responseModel.setPoToDealerId((BigInteger) row.get("PoToDealerId"));
					responseModel.setProductDivisionId((Integer) row.get("ProductDivisionId"));
					responseModel.setProductDivision((String) row.get("ProductDivision"));
					responseModel.setPoToPartyName((String) row.get("PoToPartyName"));
					responseModel.setPoPartyCode((String) row.get("PoPartyCode"));
					responseModel.setPoDate((String) row.get("PoDate"));
					responseModel.setPoPlantId((Integer) row.get("PoPlantId"));
					responseModel.setPoPlantDesc((String) row.get("PoPlantDesc"));
					responseModel.setPoRemarks((String) row.get("REMARKS"));
					responseModel.setTotalQuantity((Integer) row.get("TotalQuantity"));
					responseModel.setBasicAmount((BigDecimal) row.get("BasicAmount"));
					responseModel.setTotalGstAmount((BigDecimal) row.get("TotalGstAmount"));
					responseModel.setTcsPercent((BigDecimal) row.get("TcsPercent"));
					responseModel.setTcsValue((BigDecimal) row.get("TcsValue"));
					responseModel.setTotalAmount((BigDecimal) row.get("TotalAmount"));
					responseModel.setSonNo((String) row.get("SonNo"));
					responseModel.setSoDate((String) row.get("SoDate"));
					responseModel.setPoCancelReason((BigInteger) row.get("PoCancelReason"));
					responseModel.setPoCancelReasonDesc((String) row.get("PoCancelReasonDesc"));
					responseModel.setPoCancelRemarks((String) row.get("PoCancelRemarks"));
					responseModel.setPoCount((Integer) row.get("PoCount"));
				}
				// fetch Detail Data
				data = fetchMachinePOData(session, userCode, requestModel.getPoHdrId(), requestModel.getPoNumber(), 2);
				if (data != null && !data.isEmpty()) {
					machinePODTLList = new ArrayList<MachinePODtlResponseModel>();
					MachinePODtlResponseModel poDtlResponseModel = null;
					for (Object object : data) {
						Map row = (Map) object;
						poDtlResponseModel = new MachinePODtlResponseModel();
						poDtlResponseModel.setPoDtlId((BigInteger) row.get("PoDtlId"));
						poDtlResponseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
						poDtlResponseModel.setQuantity((Integer) row.get("Quantity"));
						poDtlResponseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
						poDtlResponseModel.setMrpPrice((BigDecimal) row.get("MrpPrice"));
						poDtlResponseModel.setDiscountAmount((BigDecimal) row.get("DiscountAmount"));
						poDtlResponseModel.setNetAmount((BigDecimal) row.get("NetAmount"));
						poDtlResponseModel.setGstPercent((BigDecimal) row.get("GstPercent"));
						poDtlResponseModel.setGstAmount((BigDecimal) row.get("GstAmount"));
						poDtlResponseModel.setTotalAmount((BigDecimal) row.get("TotalAmount"));
						poDtlResponseModel.setItemNumber((String) row.get("item_no"));
						poDtlResponseModel.setItemDescription((String) row.get("item_description"));
						poDtlResponseModel.setVariant((String) row.get("variant"));
						
						
						machinePODTLList.add(poDtlResponseModel);
					}

					responseModel.setMachinePODTLList(machinePODTLList);
				}
			} else {

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

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<?> fetchMachinePOData(Session session, String userCode, BigInteger poHdrId, String poNumber, int flag) {
		String sqlQuery = "exec [SP_SAORD_MACHINEPO_DTL] :userCode, :id, :poNumber, :flag";
		List data = null;
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("id", poHdrId);
			query.setParameter("poNumber", poNumber);
			query.setParameter("flag", flag);
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
}
