/**
 * 
 */
package com.hitech.dms.web.model.branch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class UserBranchResponseModel {
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<UserBranchListModel> userBranchList;
}
