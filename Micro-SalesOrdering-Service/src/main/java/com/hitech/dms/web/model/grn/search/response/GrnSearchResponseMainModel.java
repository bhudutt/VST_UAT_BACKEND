/**
 * 
 */
package com.hitech.dms.web.model.grn.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class GrnSearchResponseMainModel {
	private List<GrnSearchResponseModel> searchList;
	private Integer recordCount;
}
