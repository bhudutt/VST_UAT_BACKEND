/**
 * 
 */
package com.hitech.dms.web.dao.activityplan.dtl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.internal.SessionImpl;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.model.activityplan.dtl.request.ActivityPlanDTLRequestModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanDTLResponseModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanHDRResponseModel;
import com.hitech.dms.web.model.dynamicColumns.JSONObject;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityPlanDTLDaoImpl implements ActivityPlanDTLDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanDTLDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ActivityCommonDao activityCommonDao;

	public ActivityPlanHDRResponseModel fetchActivityPlanHDRDTL(Session session, String userCode,
			ActivityPlanDTLRequestModel activityPlanDTLRequestModel) {
		ActivityPlanHDRResponseModel responseModel = null;
		try {
			responseModel = activityCommonDao.fetchActivityPlanHDRDTL(session, userCode,
					activityPlanDTLRequestModel.getPlanActivityId(), activityPlanDTLRequestModel.getIsFor());
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return responseModel;
	}

	@SuppressWarnings({ "unchecked" })
	public ActivityPlanDTLResponseModel fetchActivityPlanDTLList(String userCode,
			ActivityPlanDTLRequestModel activityPlanDTLRequestModel) {
		Session session = null;
		JSONArray finalJsonArr = null;
		ActivityPlanDTLResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();

			ActivityPlanHDRResponseModel activityPlanHDRDTL = fetchActivityPlanHDRDTL(session, userCode,
					activityPlanDTLRequestModel);
			if (activityPlanHDRDTL != null) {
				SessionImpl sessionImpl = (SessionImpl) session;
				Connection conn = sessionImpl.connection();
				String sql = "{CALL SP_SACT_GetActivityPlanDtls(?, ?)}";
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setLong(1, activityPlanDTLRequestModel.getPlanActivityId().longValue());
				statement.setString(2, userCode);
				ResultSet rs = statement.executeQuery();
				// System.out.println(rs.toString());
				int columnCount = rs.getMetaData().getColumnCount();
				// System.out.println(columnCount);
				finalJsonArr = new JSONArray();
				responseModel = new ActivityPlanDTLResponseModel();
				while (rs.next()) {
					JSONObject jo = new JSONObject();
					JSONArray jarrForDays = new JSONArray();
					JSONArray jarrForTotalBudget = new JSONArray();
					JSONArray jarrForCostToVst = new JSONArray();

					for (int i = 1; i <= columnCount; i++) {
						// System.out.println(i);
						// System.out.println(rs.getMetaData().getColumnName(i));
						// System.out.println(rs.getObject(i));
						String columnName = rs.getMetaData().getColumnName(i);
						if (columnName.contains("_DAYS")) {
							JSONObject joForDays = new JSONObject();
							int lastIndex = columnName.lastIndexOf("_");
							joForDays.put(columnName.substring(0, lastIndex),
									(rs.getObject(i) == null ? null : rs.getObject(i).toString()));
							jarrForDays.add(joForDays);
						} else if (columnName.contains("_BGT")) {
							JSONObject joTotalBudget = new JSONObject();
							int lastIndex = columnName.lastIndexOf("_");
							joTotalBudget.put(columnName.substring(0, lastIndex),
									(rs.getObject(i) == null ? null : rs.getObject(i).toString()));
							jarrForTotalBudget.add(joTotalBudget);
						} else if (columnName.contains("_OEMBGT")) {
							JSONObject joCostToVst = new JSONObject();
							int lastIndex = columnName.lastIndexOf("_");
							joCostToVst.put(columnName.substring(0, lastIndex),
									(rs.getObject(i) == null ? null : rs.getObject(i).toString()));
							jarrForCostToVst.add(joCostToVst);
						} else {
							jo.put(columnName, (rs.getObject(i) == null ? null : rs.getObject(i).toString()));
						}
						// mapData.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
					}
					jo.put("DAYS", jarrForDays);
					jo.put("TOTAL BUDGET", jarrForTotalBudget);
					jo.put("COST TO VST", jarrForCostToVst);
					finalJsonArr.add(jo);
				}
				responseModel.setActivityPlanHDTDTL(activityPlanHDRDTL);
				responseModel.setActivityPlanJsonArr(finalJsonArr);
			}
			logger.info(finalJsonArr.toString());
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
}
