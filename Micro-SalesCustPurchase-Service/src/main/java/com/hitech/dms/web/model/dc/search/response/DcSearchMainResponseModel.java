/**
 * 
 */
package com.hitech.dms.web.model.dc.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcSearchMainResponseModel {
	private List<DcSearchResponseModel> searchList;
	private Integer recordCount;
}
