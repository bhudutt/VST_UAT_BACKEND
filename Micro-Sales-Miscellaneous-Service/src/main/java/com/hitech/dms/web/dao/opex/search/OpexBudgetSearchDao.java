/**
 * 
 */
package com.hitech.dms.web.dao.opex.search;

import com.hitech.dms.web.model.opex.search.request.OpexBudgetSearchRequestModel;
import com.hitech.dms.web.model.opex.search.response.OpexBudgetSearchMainResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface OpexBudgetSearchDao {
	public OpexBudgetSearchMainResponseModel searchOpexBudgetList(String userCode,
			OpexBudgetSearchRequestModel requestModel);
}
