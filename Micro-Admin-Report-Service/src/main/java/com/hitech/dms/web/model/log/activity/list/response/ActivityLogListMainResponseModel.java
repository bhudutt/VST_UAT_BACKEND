/**
 * 
 */
package com.hitech.dms.web.model.log.activity.list.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ActivityLogListMainResponseModel {
	private List<ActivityLogListResponseModel> searchList;
	private Integer recordCount;
}
