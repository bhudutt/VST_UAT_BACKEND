package com.hitech.dms.web.model.admin.role.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleSearchMainResponse {
	
	
	private List<AdminRoleSearchResponse> search;
	private Integer recordCount;

}
