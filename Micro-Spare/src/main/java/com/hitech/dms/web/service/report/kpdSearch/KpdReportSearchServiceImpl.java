package com.hitech.dms.web.service.report.kpdSearch;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.report.kpdSearch.KpdOrderStatusSearchDao;
import com.hitech.dms.web.dao.report.kpdSearch.KpdOrderStatusSearchDaoImpl;
import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchList;
import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchRequest;
import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchResponse;

@Service
public class KpdReportSearchServiceImpl implements KpdReportSearchService{
	private static final Logger logger = LoggerFactory.getLogger(KpdOrderStatusSearchDaoImpl.class);
	
	@Autowired
	private KpdOrderStatusSearchDao  kpdOrderStatusSearchDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public KpdOrderStatusSearchList kpdOrderReportSearch(String userCode, KpdOrderStatusSearchRequest resquest, Device device) {
		


		Session session = null;
		Transaction transaction = null;
		List<KpdOrderStatusSearchResponse> responseList = null;
		KpdOrderStatusSearchResponse responseModel = null;
		KpdOrderStatusSearchList bean = new KpdOrderStatusSearchList();
		Integer totalRow = 0;
		Integer rowCount = 0;
		boolean isSuccess = true;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
//        session = sessionFactory.openSession();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

	        List<?> data = kpdOrderStatusSearchDao.kpdOrderReportSearch(session, userCode, resquest, device);
	        
	        if (data != null && !data.isEmpty()) {
				responseList = new LinkedList<KpdOrderStatusSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
								
					responseModel = new KpdOrderStatusSearchResponse();
					
					responseModel.setCustomerCode((String) row.get("cutomerCode"));
					responseModel.setCustomerName((String) row.get("customerName"));			
					responseModel.setKpdCode((String) row.get("KPDCode"));
					responseModel.setKpdName((String) row.get("KPDName"));
					responseModel.setPoNumber((String) row.get("poNumber"));
					responseModel.setOrderDate((Date) row.get("orderDate"));
					responseModel.setPartNumber((String) row.get("partNumber"));
					responseModel.setPartDescription((String) row.get("partDesc"));
					responseModel.setOrderQty((BigDecimal) row.get("orderQty"));
					responseModel.setShipQty((BigDecimal) row.get("shipQty"));
					responseModel.setBalanceQty((BigDecimal) row.get("balanceQty"));
					responseModel.setPoStatus((String) row.get("poStatus"));
					responseModel.setPoValue((BigDecimal) row.get("poValue"));
					responseModel.setInvoiceValue((BigDecimal) row.get("invoiceValue"));
					responseModel.setBalanceValue((BigDecimal) row.get("balanceValue"));
					responseModel.setPercentage((BigDecimal) row.get("percentage"));
					responseModel.setAgeingDay((BigDecimal) row.get("ageingDays"));
					totalRow = (Integer) row.get("totalCount");
	
					responseList.add(responseModel);
					rowCount++;
				}
				bean.setTotalRowCount(totalRow);
				bean.setSearchList(responseList);
				bean.setRowCount(rowCount);
			}
		}catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {
				bean.setStatusCode(WebConstants.STATUS_OK_200);
				bean.setMsg("KPD Order Status Fetches Successfully...");

			} else {
				bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
        return bean;
	}

}
