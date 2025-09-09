/**
 * 
 */
package com.hitech.dms.web.model.issue.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IssueSearchMainResponseModel {
	private List<IssueSearchResponseModel> searchList;
	private Integer recordCount;
}
