/**
 * 
 */
package com.hitech.dms.web.dao.allotment.item.dtl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.hitech.dms.web.model.allot.item.dtl.request.ChassisNoDtlRequestModel;
import com.hitech.dms.web.model.allot.item.dtl.request.ItemDtlForAllotRequestModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ChassisDtlForAllotResponseModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ChassisNoForAllotResponseModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ItemDtlForAllotResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ItemDtlForAllotDaoImpl implements ItemDtlForAllotDao {
	private static final Logger logger = LoggerFactory.getLogger(ItemDtlForAllotDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ItemDtlForAllotResponseModel fetchItemDetailForAllot(String userCode,
			ItemDtlForAllotRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchItemDetailForAllot invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		ItemDtlForAllotResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SA_ALLOT_ITEM_DTL] :userCode, :dealerId, :branchId, :pcId, :itemId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("itemId", requestModel.getMachineItemId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ItemDtlForAllotResponseModel();
					responseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
					responseModel.setItemNo((String) row.get("ItemNo"));
					responseModel.setItemDesc((String) row.get("ItemDesc"));
					responseModel.setProductGroup((String) row.get("ProductGroup"));
				}

				List<ChassisNoForAllotResponseModel> chassisList = fetchChassisListForAllot(session, userCode,
						requestModel);
				responseModel.setChassisList(chassisList);
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
	public List<ChassisNoForAllotResponseModel> fetchChassisListForAllot(Session session, String userCode,
			ItemDtlForAllotRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchChassisListForAllot invoked.." + requestModel.toString());
		}
		List<ChassisNoForAllotResponseModel> chassisList = null;
		Query query = null;
		String sqlQuery = "exec [SP_SA_ALLOT_ITEM_CHASSIS_LIST] :userCode, :dealerId, :branchId, :pcId, :itemId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("itemId", requestModel.getMachineItemId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				chassisList = new ArrayList<ChassisNoForAllotResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					ChassisNoForAllotResponseModel responseModel = new ChassisNoForAllotResponseModel();
					responseModel.setVinId((BigInteger) row.get("VinId"));
					responseModel.setChassisNo((String) row.get("ChassisNo"));

					chassisList.add(responseModel);
				}

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {

		}
		return chassisList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ChassisDtlForAllotResponseModel fetchChassisNoDetailForAllot(String userCode,
			ChassisNoDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchChassisNoDetailForAllot invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		ChassisDtlForAllotResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SA_ALLOT_CHASSISNO_DTL] :userCode, :dealerId, :branchId, :pcId, :itemId, :vinId, :grnNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("itemId", requestModel.getMachineItemId());
			query.setParameter("vinId", requestModel.getVinId());
			query.setParameter("grnNo", requestModel.getGrnId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			Map<BigInteger,String> grnMap=new HashMap();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ChassisDtlForAllotResponseModel();
					responseModel.setVinId((BigInteger) row.get("VinId"));
					responseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
					responseModel.setMachineInventoryId((BigInteger) row.get("machine_inventory_id"));
					responseModel.setItemNo((String) row.get("ItemNo"));
					responseModel.setItemDesc((String) row.get("ItemDesc"));
					responseModel.setStockType((String) row.get("StockType"));
					responseModel.setChassisNo((String) row.get("ChassisNo"));
					responseModel.setVinNo((String) row.get("VinNo"));
					responseModel.setEngineNo((String) row.get("EngineNo"));
					responseModel.setGrnId((BigInteger) row.get("grn_id"));
					responseModel.setGrnNumber((String) row.get("GrnNumber"));
					responseModel.setInvoiceNo((String) row.get("InvoiceNo"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setAgeInDays((Integer) row.get("AgeInDays"));
					responseModel.setProductGroup((String) row.get("productGroup"));
					responseModel.setMachineItemId((BigInteger) row.get("itemid"));
					responseModel.setChassisRequired((Character) row.get("chassis_no_req"));
					
					
					
					if(responseModel.getProductGroup().equalsIgnoreCase("Accessory") || responseModel.getProductGroup().equalsIgnoreCase("Implement")) {
						grnMap.put((BigInteger) row.get("grn_id"), ((String) row.get("GrnNumber")+" ( "+(Integer) row.get("available")+" )"));
					}
					
				}
				if(grnMap !=null) {
					responseModel.setGrnMap(grnMap);
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
}
