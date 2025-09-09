/**
 * 
 */
package com.hitech.dms.web.dao.log.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.hibernate.AliasToEntityOrderedMapResultTransformer;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.export.report.ReportModel;
import com.hitech.dms.web.model.log.activity.list.request.ActivityLogListRequestModel;
import com.hitech.dms.web.model.log.activity.list.response.ActivityLogListMainResponseModel;
import com.hitech.dms.web.model.log.activity.list.response.ActivityLogListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityLogDaoImpl implements ActivityLogDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityLogDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public ActivityLogListMainResponseModel fetchAcivityLogExport(Session session, String userCode,
			ActivityLogListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAcivityLogList invoked.." + requestModel.toString());
		}
		ActivityLogListMainResponseModel responseMainModel = null;
		List<ActivityLogListResponseModel> responseModelList = null;
		try {
			List data = fetchAcivityLogList(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseMainModel = new ActivityLogListMainResponseModel();
				responseModelList = new ArrayList<ActivityLogListResponseModel>();
				Integer recordCount = 0;
				for (Object object : data) {
					Map row = (Map) object;
					ActivityLogListResponseModel listResponseModel = new ActivityLogListResponseModel();
					listResponseModel.setUserCode((String) row.get("userCode"));
					listResponseModel.setEvent((String) row.get("event"));
					listResponseModel.setIp((String) row.get("ip"));
					listResponseModel.setPage((String) row.get("page"));
					listResponseModel.setUrl((String) row.get("url"));
					listResponseModel.setLoggedTime((String) row.get("loggedTime"));
					listResponseModel.setUserAgent((String) row.get("userAgent"));
					listResponseModel.setLatitude((String) row.get("latitude"));
					listResponseModel.setLongitude((String) row.get("longitude"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(listResponseModel);
				}
				responseMainModel.setSearchList(responseModelList);
				responseMainModel.setRecordCount(recordCount);
			}
		} catch (Exception ex) {

		}
		return responseMainModel;
	}

	@Override
	public ActivityLogListMainResponseModel fetchAcivityLogExport(String userCode,
			ActivityLogListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAcivityLogExport invoked.." + requestModel.toString());
		}
		Session session = null;
		ActivityLogListMainResponseModel responseMainModel = null;
		try {
			session = sessionFactory.openSession();
			responseMainModel = fetchAcivityLogExport(session, userCode, requestModel);
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
		return responseMainModel;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> exportAcivityLog(String userCode, ActivityLogListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("exportAcivityLog invoked.." + requestModel.toString());
		}
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		try {
			session = sessionFactory.openSession();
			List data = fetchAcivityLogList(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				mapData.put("reportModelList", generateReportModelForExcel(data));
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
		return mapData;
	}

	@SuppressWarnings("unchecked")
	public List<ReportModel> generateReportModelForExcel(List<?> data) {
		Integer i = 1;
		String key = "Sr.No.";
		String value = null;
		List<ReportModel> reportModelList = new LinkedList<ReportModel>();
		ReportModel reportModel = new ReportModel(key, "row" + i, String.valueOf(i));
		reportModelList.add(reportModel);
		for (Object object : data) {
			Map<Object, Object> row = (Map<Object, Object>) object;
			if (i != 1) {
				reportModelList.add(new ReportModel(key, "row" + i, String.valueOf(i)));
			}
			for (Map.Entry<Object, Object> entry : row.entrySet()) {
				key = (String) entry.getKey();
				if (key.equals("totalRecords")) {
					continue;
				}
				value = "";
				if (entry.getValue() != null) {
					if (entry.getValue() instanceof String) {
						value = (String) entry.getValue();
					} else if (entry.getValue() instanceof Integer) {
						Integer v = (Integer) entry.getValue();
						value = String.valueOf(v);
					} else if (entry.getValue() instanceof Date) {
						Date d = (Date) entry.getValue();
						value = DateToStringParserUtils.parseDateToStringDDMMYYYY(d);
					} else if (entry.getValue() instanceof BigDecimal) {
						BigDecimal b = ((BigDecimal) entry.getValue());
						value = b.toString();
					}
				}
				reportModel = new ReportModel(key, "row" + i, value == null ? "" : value);
				reportModelList.add(reportModel);
			}
			i++;
			key = "Sr.No.";
		}
		return reportModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchAcivityLogList(Session session, String userCode, ActivityLogListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAcivityLogList invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_ADM_GET_USER_ACTIVITY_LOG] :userCode, :usr, :pcId, :orgHierId, :dealerId, :branchId, :fromDate, :toDate, :includeInactive, :page, :size";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("usr", requestModel.getUserCode());
			query.setParameter("pcId", requestModel.getPcID());
			query.setParameter("orgHierId", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerID());
			query.setParameter("branchId", requestModel.getBranchID());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());

			query.setResultTransformer(AliasToEntityOrderedMapResultTransformer.INSTANCE);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
}
