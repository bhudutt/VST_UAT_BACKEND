package com.hitech.dms.web.service.report.partGrnSearch;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.hitech.dms.web.dao.report.kpdSearch.KpdOrderStatusSearchDaoImpl;
import com.hitech.dms.web.dao.report.partGrnSearch.PartGrnSearchDao;
import com.hitech.dms.web.model.report.model.PartGrnSearchList;
import com.hitech.dms.web.model.report.model.PartGrnSearchRequest;
import com.hitech.dms.web.model.report.model.PartGrnSearchResponse;

@Service
public class PartGrnSearchServiceImpl implements PartGrnSearchService{
	
	private static final Logger logger = LoggerFactory.getLogger(KpdOrderStatusSearchDaoImpl.class);
	
	@Autowired
	private PartGrnSearchDao partGrnSearchDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PartGrnSearchList partGrnSearch(String userCode, PartGrnSearchRequest resquest, Device device) {
		


		Session session = null;
		Transaction transaction = null;
		List<PartGrnSearchResponse> responseList = null;
		PartGrnSearchResponse responseModel = null;
		PartGrnSearchList bean = new PartGrnSearchList();
		Integer totalRow = 0;
		Integer rowCount = 0;
		boolean isSuccess = true;
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
//        session = sessionFactory.openSession();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

	        List<?> data = partGrnSearchDao.partGrnReportSearch(session, userCode, resquest, device);
	        
	        if (data != null && !data.isEmpty()) {
				responseList = new LinkedList<PartGrnSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
								
					responseModel = new PartGrnSearchResponse();
					
					responseModel.setDealerCode((String) row.get("dealerCode"));
					responseModel.setDealerName((String) row.get("dealerName"));			
					responseModel.setInvoiceNo((String) row.get("invoiceNo"));
					responseModel.setGrnNumber((String) row.get("grnNumber"));
					responseModel.setGrnDate((Date) row.get("grnDate"));
					responseModel.setProdCategory((String) row.get("prodCategory"));
					responseModel.setPartNumber((String) row.get("partNumber"));
					responseModel.setPartDesc((String)row.get("partDesc"));
					responseModel.setQty((BigDecimal) row.get("qty"));
					responseModel.setUnitPrice((BigDecimal) row.get("unitPrice"));
					responseModel.setTotalValue((BigDecimal) row.get("totalValue"));
					responseModel.setHsnCode((String) row.get("hsnCode"));
					responseModel.setGstPercentage((BigDecimal) row.get("gstPercentage"));
					responseModel.setGstValue((BigDecimal) row.get("gstValue"));
					responseModel.setGrossValue((BigDecimal) row.get("grossValue"));
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
				bean.setMsg("Part GRN Fetches Successfully...");

			} else {
				bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
        return bean;
	}
}
