package com.hitech.dms.web.dao.attendance.search.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hamcrest.core.IsInstanceOf;
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

import com.hitech.dms.web.model.attendance.search.request.AttendanceSearchRequestModel;
import com.hitech.dms.web.model.attendance.search.response.AttendanceSearchResponse;
import com.hitech.dms.web.model.attendance.search.response.AttendanceSearchResponseModel;
import com.hitech.dms.web.model.grn.search.request.GrnSearchRequestModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseMainModel;
import com.hitech.dms.web.model.grn.search.response.GrnSearchResponseModel;


@Repository
public class AttendanceSearchDaoImpl implements AttendanceSearchDao{
	
	private static final Logger logger = LoggerFactory.getLogger(AttendanceSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
    private EntityManager entityManager;
	
	// Method to get headers (column names)
    public List<String> getHeaderValues() {
        String procedureCall = "EXEC sp_get_attendance_detail"; // Modify with your header procedure name
        javax.persistence.Query query = entityManager.createNativeQuery(procedureCall);

        // Assuming the headers are returned as a list of strings
        List<String> headers = new LinkedList();//query.getResultList();
        headers.add("employee_id");
        headers.add("01-Nov-2024");
        headers.add("02-Nov-2024");
        headers.add("03-Nov-2024");
        headers.add("04-Nov-2024");
        headers.add("05-Nov-2024");
        headers.add("06-Nov-2024");
        headers.add("07-Nov-2024");
        headers.add("08-Nov-2024");
        headers.add("09-Nov-2024");
        headers.add("10-Nov-2024");
        headers.add("11-Nov-2024");
        headers.add("12-Nov-2024");
        headers.add("13-Nov-2024");
        headers.add("14-Nov-2024");
        headers.add("15-Nov-2024");
        headers.add("16-Nov-2024");
        headers.add("17-Nov-2024");
        headers.add("18-Nov-2024");
        headers.add("19-Nov-2024");
        headers.add("20-Nov-2024");
        headers.add("21-Nov-2024");
        headers.add("22-Nov-2024");
        headers.add("23-Nov-2024");
        headers.add("24-Nov-2024");
        headers.add("25-Nov-2024");
        headers.add("26-Nov-2024");
        headers.add("27-Nov-2024");
        headers.add("28-Nov-2024");
        headers.add("29-Nov-2024");
        headers.add("30-Nov-2024");
        
        
        return headers;
    }
	
	@Override
	public AttendanceSearchResponse attendanceSearch(String userCode, AttendanceSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAttendance invoked.." + requestModel.toString());
		}
		Session session = null;
		AttendanceSearchResponse responseModel = null;
		try {
			session = sessionFactory.openSession();
			List<String> list=getHeaderValues();
			responseModel = searchAttendanceList(session, userCode, requestModel,list);
			
			
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
	public AttendanceSearchResponse searchAttendanceList(Session session, String userCode,
			AttendanceSearchRequestModel requestModel,List<String> headers) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchSalesGrnList invoked.." + requestModel.toString());
		}
		Query query = null;
		AttendanceSearchResponse responseListModel = null;
		List<AttendanceSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		List<Map<String, Object>> resultList = new ArrayList<>();
		String sqlQuery = "exec Sp_Get_Attendance_Detail";
		try {
			query = session.createSQLQuery(sqlQuery);
			/*
			 * query.setParameter("userCode", userCode); query.setParameter("pcID",
			 * requestModel.getPcId());
			 * 
			 * query.setParameter("fromDate", (requestModel.getFromDate1()));
			 * query.setParameter("todate", (requestModel.getToDate1()));
			 * query.setParameter("includeInactive", requestModel.getIncludeInActive());
			 * query.setParameter("page", requestModel.getPage());
			 * query.setParameter("size", requestModel.getSize());
			 */
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new AttendanceSearchResponse(); 
				
				for (Object object : data) {
					Map<String, Object> rowMap = new HashMap<>();
					Map row = (Map) object;
					for (int i = 0; i < headers.size(); i++) 
					{
						
							if(row.get(headers.get(i)) instanceof String) {
								rowMap.put(headers.get(i), (String) row.get(headers.get(i)));
							}else if(row.get(headers.get(i)) instanceof Integer) {
								rowMap.put(headers.get(i), (Integer) row.get(headers.get(i)));
							}
							
							
						
		                
		            }
					 resultList.add(rowMap);
				}
				
				
				/*
				 * responseListModel = new AttendanceSearchResponse(); 
				 * responseModelList = new
				 * ArrayList<AttendanceSearchResponseModel>(); AttendanceSearchResponseModel
				 * responseModel = null; for (Object object : data) { Map row = (Map) object;
				 * responseModel = new AttendanceSearchResponseModel(); //
				 * responseModel.setId((BigInteger) row.get("grn_id")); //
				 * responseModel.setId1((BigInteger) row.get("dealer_id")); //
				 * responseModel.setZone((String) row.get("ZONE")); //
				 * responseModel.setArea((String) row.get("AREA")); //
				 * responseModel.setGrnNumber((String) row.get("GrnNumber")); //
				 * responseModel.setGrnDate((String) row.get("GrnDate")); //
				 * responseModel.setGrnType((String) row.get("GrnType")); //
				 * responseModel.setDealerCode((String) row.get("DealerCode")); //
				 * responseModel.setDealerName((String) row.get("DealerName")); //
				 * responseModel.setPcDesc((String) row.get("PROFIT_CENTER")); //
				 * responseModel.setGrnStatus((String) row.get("GrnStatus")); //
				 * responseModel.setInvoiceNumber((String) row.get("InvoiceNo")); //
				 * responseModel.setInvoiceDate((String) row.get("InvoiceDate")); //
				 * responseModel.setPartyName((String) row.get("Party_Name")); //
				 * responseModel.setPartyCode((String) row.get("Party_Code")); //
				 * responseModel.setTransporterName((String) row.get("transporter_Name")); //
				 * responseModel.setGrossTotalValue((BigDecimal) row.get("gross_total_value"));
				 * // responseModel.setDriverMobileNo((String) row.get("driver_mobile")); //
				 * responseModel.setDriverName((String) row.get("driverName")); //
				 * responseModel.setTransporterVehicleNo((String)
				 * row.get("transporter_vehicle_number"));
				 * 
				 * 
				 * if (recordCount.compareTo(0) == 0) { recordCount = (Integer)
				 * row.get("totalRecords"); }
				 * 
				 * responseModelList.add(responseModel); }
				 */
				
				System.out.println("resultList  "+resultList);

				responseListModel.setRecordCount(resultList.size());
				responseListModel.setSearchList(resultList);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseListModel;
	}
	

}
