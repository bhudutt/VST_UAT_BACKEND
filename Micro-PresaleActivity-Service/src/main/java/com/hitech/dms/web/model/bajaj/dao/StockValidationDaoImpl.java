package com.hitech.dms.web.model.bajaj.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.dao.activityClaim.inv.dao.ActivityClaimInvDtlDaoImpl;
import com.hitech.dms.web.model.bajaj.request.StockRequestDTO;
import com.hitech.dms.web.model.bajaj.response.StockResponseDTO;

@Repository
public class StockValidationDaoImpl implements StockValidationDao {
	
	private static final Logger logger = LoggerFactory.getLogger(StockValidationDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ActivityCommonDao activityCommonDao;
	
	@Autowired
	private DozerBeanMapper mapper;

	/*
	 * @Override public List<StockResponseDTO>
	 * validateAndProcessStock(StockRequestDTO request) {
	 * 
	 * logger.info("request :"+request.toString());
	 * 
	 * Session session = null; Query query = null; String transactionId=null;
	 * StockResponseDTO resObj=null; List<StockResponseDTO> list=new
	 * ArrayList<StockResponseDTO>(); String sqlQuery =
	 * "exec [SP_Third_PARTY_VALIDATION] " +
	 * ":retailer_code, :product_identification_number, " +
	 * " :product_sku_code,:Distributor_Code, :Status";
	 * 
	 * try { session = sessionFactory.openSession(); query =
	 * session.createNativeQuery(sqlQuery); query.setParameter("retailer_code",
	 * request.getRetailerCode());
	 * query.setParameter("product_identification_number",
	 * request.getProductIdentificationNumber());
	 * query.setParameter("product_sku_code", request.getProductSkuCode());
	 * query.setParameter("Distributor_Code",request.getDistributorCode());
	 * query.setParameter("Status",request.getStatus());
	 * 
	 * query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP); List data =
	 * query.list(); // TODO Auto-generated method stub
	 * 
	 * if (data != null && !data.isEmpty()) { for (Object object : data) { Map row =
	 * (Map) object; resObj=new StockResponseDTO();
	 * resObj.setValidationItem((String) row.get("ValidationItem"));
	 * resObj.setResponsecode((Integer) row.get("ResponseCode"));
	 * resObj.setMessage((String) row.get("Message")); list.add(resObj); } }
	 * 
	 * transactionId = "BFL" + UUID.randomUUID().toString().substring(0,
	 * 6).toUpperCase(); } catch (SQLGrammarException ex) {
	 * logger.error(this.getClass().getName(), ex); } catch (HibernateException ex)
	 * { logger.error(this.getClass().getName(), ex); } catch (Exception ex) {
	 * logger.error(this.getClass().getName(), ex); } finally { if (session != null)
	 * { session.close(); } } return list; }
	 */
	
	
	@Override
	public StockResponseDTO validateAndProcessStock(StockRequestDTO request) {
		
		logger.info("request :"+request.toString());
		
		Session session = null;
		Query query = null;
		String transactionId=null;
		StockResponseDTO resObj=null;
		//List<StockResponseDTO> list=new ArrayList<StockResponseDTO>();
		String sqlQuery = "exec [SP_Third_PARTY_VALIDATION] "
				+ ":dealerCode,:chassisNo, :vinNo, "
				+ " :modelCode, :distributorCode";
		
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dealerCode", request.getDealerCode());
			query.setParameter("chassisNo", request.getChassisNo());
			query.setParameter("vinNo", request.getVinNo());
			query.setParameter("modelCode",request.getModelCode());
			query.setParameter("distributorCode",request.getDistributorCode());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
		// TODO Auto-generated method stub
			
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					resObj=new StockResponseDTO();
					resObj.setValidationItem((String) row.get("ValidationItem"));
					resObj.setResponsecode((Integer) row.get("ResponseCode"));
					resObj.setMessage((String) row.get("Message"));
					//list.add(resObj);
				}
			}

	         transactionId = "BFL" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
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
	        return resObj;
	}

}
