package com.hitech.dms.web.dao.dealer.employee.search;

import com.hitech.dms.web.model.dealer.employee.search.request.DealerEmployeeSearchRequest;
import com.hitech.dms.web.model.dealer.employee.search.response.DealerEmployeeSearchMainResponse;

/**
 * @author vinay.gautam
 *
 */
public interface DealerEmployeeSearchDao {
	
	public DealerEmployeeSearchMainResponse fetchDealerEmployeeSearch(String userCode, DealerEmployeeSearchRequest requestModel);

}
