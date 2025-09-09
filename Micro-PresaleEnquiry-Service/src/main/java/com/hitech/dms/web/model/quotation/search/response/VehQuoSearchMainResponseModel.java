/**
 * 
 */
package com.hitech.dms.web.model.quotation.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoSearchMainResponseModel {
	private List<VehQuoSearchResponse> responseModelList;
	private Integer totalRecords;
}
