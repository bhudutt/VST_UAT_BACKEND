/**
 * 
 */
package com.hitech.dms.web.dao.activity.source.dao;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.activity.source.response.ActivitySourceListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface ActivityNameDao {
	public List<ActivitySourceListResponseModel> fetchActivityNameListBasedOnActivityPlanId(String userCode,
			BigInteger activityPlanHDRId);
}
