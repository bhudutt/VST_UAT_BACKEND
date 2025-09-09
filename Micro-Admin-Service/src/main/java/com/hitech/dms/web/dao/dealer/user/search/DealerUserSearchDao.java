/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.dao.dealer.user.search;

import com.hitech.dms.web.model.dealer.user.search.request.DealerUserSearchRequest;
import com.hitech.dms.web.model.dealer.user.search.response.DealerUserSearchMainResponse;

/**
 * @author vinay.gautam
 *
 */
public interface DealerUserSearchDao {
	
	public DealerUserSearchMainResponse fetchDealerUserSearch(String userCode, DealerUserSearchRequest requestModel);

}
