/**
 * 
 */
package com.hitech.dms.web.dao.activity.search;

import java.util.List;

import com.hitech.dms.web.model.activity.search.request.ActualActivitySearchRequestModel;
import com.hitech.dms.web.model.activity.search.response.ActualActivitySearchResponse;

/**
 * @author dinesh.jakhar
 *
 */
public interface ActualActivitySearchDao {
	public List<ActualActivitySearchResponse> fetchActualActivitySearchList(String userCode,
			ActualActivitySearchRequestModel requestModel);
}
