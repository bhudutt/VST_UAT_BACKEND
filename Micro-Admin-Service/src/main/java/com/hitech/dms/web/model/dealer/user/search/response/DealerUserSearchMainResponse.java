/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.dealer.user.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerUserSearchMainResponse {
	private List<DealerSearchResponseModel> search;
	private Integer recordCount;

}
