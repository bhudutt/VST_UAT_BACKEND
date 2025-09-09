package com.hitech.dms.web.dao.report.partGrnSearch;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.report.model.PartGrnSearchRequest;

@Repository
@Transactional
public class PartGrnSearchDaoImpl implements PartGrnSearchDao{
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public List<?> partGrnReportSearch(Session session, String userCode, PartGrnSearchRequest resquest, Device device) {
		
		Query query = null;
		List<?> data = null;
		boolean isSuccess = true;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String[] cu =null;
			if(resquest.getCustomerName()!=null) {
			 cu = resquest.getCustomerName().split("\\|");
			}
			String sqlQuery = "exec [SP_PA_GRN_REPORT]  :userCode, :dealerName, :partNo, :partCatId, :fromDate, :toDate,"
					+ ":page, :size, :branchId";
			
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("userCode", userCode);
			query.setParameter("dealerName", cu!=null?cu[1].trim():null);
			query.setParameter("partNo", resquest.getPartNumber());
			query.setParameter("partCatId", resquest.getPartCategoryId());
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
