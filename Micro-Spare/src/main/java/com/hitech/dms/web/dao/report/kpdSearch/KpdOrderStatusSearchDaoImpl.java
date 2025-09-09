package com.hitech.dms.web.dao.report.kpdSearch;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchRequest;

@Repository
@Transactional
public class KpdOrderStatusSearchDaoImpl implements KpdOrderStatusSearchDao{
	
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public List<?> kpdOrderReportSearch(Session session, String userCode, KpdOrderStatusSearchRequest resquest, Device device) {
		
		Query query = null;
		List<?> data = null;
		boolean isSuccess = true;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String[] cu =null;
			if(resquest.getCustomerName()!=null) {
			 cu = resquest.getCustomerName().split("\\|");
			}
			
			String[] kd =null;
			if(resquest.getKpd()!=null) {
				kd = resquest.getKpd().split("\\|");
			}
			String sqlQuery = "exec [SP_KPD_ORDER_STATUS]  :userCode, :customerName, :KPD, :PONo, :PartNo, :fromDate, :toDate,"
					+ ":page, :size, :branchId";
			
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("userCode", userCode);
			query.setParameter("customerName", cu!=null?cu[1].trim():null);
			query.setParameter("KPD", kd!=null?kd[1].trim():null);
			query.setParameter("PONo", resquest.getPoNo());
			query.setParameter("PartNo", resquest.getPartNumber());
			query.setParameter("fromDate", resquest.getFromDate());
			query.setParameter("toDate", resquest.getToDate());
			query.setParameter("page", resquest.getPage());
			query.setParameter("size", resquest.getSize());
			query.setParameter("branchId", resquest.getBranchId());
		
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			data = query.list();

		} catch (Exception e) {
		     e.printStackTrace();
		}
		return data;
	}

}
