/**
 * 
 */
package com.hitech.dms.web.model.dealer.branchassign.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AssignUserToBranchSearchMainResponseModel {
	private List<AssignUserToBranchSearchResponseModel> searchList;
	private int recordCount;
}
