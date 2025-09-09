package com.hitech.dms.web.model.dealer.employee.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerEmployeeSearchMainResponse {
	private List<DealerEmployeeSearchResponse> search;
	private Integer recordCount;

}
