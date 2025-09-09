/**
 * 
 */
package com.hitech.dms.web.model.admin.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserSearchMainResponseModel {
	private List<HoUserSearchResponseModel> searchList;
	private int recordCount;
}
