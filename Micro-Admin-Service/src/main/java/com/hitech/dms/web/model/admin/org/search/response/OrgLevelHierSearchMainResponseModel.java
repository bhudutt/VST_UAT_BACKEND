/**
 * 
 */
package com.hitech.dms.web.model.admin.org.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class OrgLevelHierSearchMainResponseModel {
	private List<OrgLevelHierSearchResponseModel> searchList;
	private Integer recordCount;
}
